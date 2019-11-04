package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.entity.PasswordRequest;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.security.MD5;
import com.nafisulbari.usis.service.PasswordRequestService;
import com.nafisulbari.usis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
public class AdminController {


    private UserService userService;
    private PasswordRequestService passwordRequestService;

    public AdminController(UserService theUserService, PasswordRequestService pthePasswordRequestService) {
        userService = theUserService;
        passwordRequestService = pthePasswordRequestService;
    }




    @GetMapping("/admin/admin-home")
    public String adminHome(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "/admin/admin-home";
    }


    @GetMapping("/admin/user-portal")
    public String showUsersPage(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "/admin/user-portal";
    }


    @GetMapping("/admin/edit-user/{id}")
    public String editUserPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "/admin/edit-user";
    }


    @GetMapping("/admin/add-user")
    public String addUserPage(User theUser) {

        return "admin/add-user";
    }

    @PostMapping("/admin/add-user-account")
    public String addUserAccount(@Valid User user, BindingResult result, Model model) {

        String email = user.getEmail();
        if (!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){
            model.addAttribute(user);
            model.addAttribute("garbageEmail", true);
            return "/admin/add-user";
        }
        String mobile =user.getMobile();
        if (!mobile.matches("^(\\+8801|8801|01)(\\d){9}")){
            model.addAttribute(user);
            model.addAttribute("garbageMobile", true);
            return "/admin/add-user";
        }

        try {
            User tempUser = userService.findUserByEmail(user);
            if (tempUser != null) {
                model.addAttribute("emailExists", true);
                return "/admin/add-user";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.hasErrors()) {
            return "admin/add-user";
        }

        String hashed = MD5.getMd5(user.getPassword());
        user.setPassword(hashed);

        userService.saveOrUpdateUser(user);

        model.addAttribute("users", userService.findAllUsers());
        return "admin/user-portal";
    }


    @PostMapping("/admin/update-user/{id}")
    public String updateUser(@PathVariable("id") int id, User theUser, BindingResult result, Model model) {


        if (result.hasErrors()) {
            theUser.setId(id);
            return "admin/edit-user";
        }

        MD5 md5 = new MD5();
        String hashed = md5.getMd5(theUser.getPassword());
        theUser.setPassword(hashed);


        userService.saveOrUpdateUser(theUser);

        model.addAttribute("users", userService.findAllUsers());
        return "admin/edit-user";
    }

    @GetMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable("id") int id, User theUser, BindingResult result, Model model) {

        if (result.hasErrors()) {
            throw new RuntimeException("no user found with id :" + id);
        }

        userService.deleteUserById(id);

        model.addAttribute("users", userService.findAllUsers());
        return "admin/user-portal";
    }

    //------------------------------------------------------------------------------------
    @GetMapping("/admin/password-accept/{id}")
    public String passwordAccept(@PathVariable("id") int id, PasswordRequest thePasswordRequest, BindingResult result, Model model) {

        if (result.hasErrors()) {
            throw new RuntimeException("no user found with id :" + id);
        }
        //might be wrong TODO
        passwordRequestService.acceptByPasswordEmail(id);

        model.addAttribute("passwordrequests", passwordRequestService.findAllPasswordRequest());
        return "admin/password-request";
    }

    @GetMapping("/admin/password-reject/{id}")
    public String passwordReject(@PathVariable("id") int id, PasswordRequest thePasswordRequest, BindingResult result, Model model) {

        if (result.hasErrors()) {
            throw new RuntimeException("no user found with id :" + id);
        }
        //might be wrong TODO
        passwordRequestService.rejectByPasswordId(id);

        model.addAttribute("passwordrequests", passwordRequestService.findAllPasswordRequest());
        return "admin/password-request";
    }


    @GetMapping("/admin/password-request")
    public String passwordRequestPage(Model model) {
        model.addAttribute("passwordrequests", passwordRequestService.findAllPasswordRequest());
        return "/admin/password-request";
    }

}
