package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.entity.PasswordRequest;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.security.MD5;
import com.nafisulbari.usis.service.PasswordRequestService;
import com.nafisulbari.usis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {


    private UserService userService;
    private PasswordRequestService passwordRequestService;


    public LoginController(UserService theUserService, PasswordRequestService thePasswordRequestService) {
        userService = theUserService;
        passwordRequestService = thePasswordRequestService;
    }

    @GetMapping("/")
    public String loginPage(User theUser) {
        return "login";
    }

    @PostMapping("/login")
    public String homePage(User theUser, Model theModel) {

        MD5 md5 = new MD5();
        String hashed = md5.getMd5(theUser.getPassword());

        System.out.println(hashed);

        String role = userService.loginAuthenticator(theUser);


        if (role.equals("ADMIN")) {
            return "admin/admin-home";
        }
        if (role.equals("TEACHER")) {
            return "teacher/teacher-home";
        }
        if (role.equals("STUDENT")) {
            return "student/student-home";
        }
        theModel.addAttribute("user", theUser);

        return "login2";
    }

    @GetMapping("/forgot-password")
    public String editUserPage(User theUser) {

        return "/forgot-password";
    }

    @PostMapping("/request-password")
    public String editUserPage(PasswordRequest thePasswordRequest) {
        passwordRequestService.savePasswordRequest(thePasswordRequest);

        return "password-requested";
    }

//--------------------------------------------------------------------------------


//    @RequestMapping(value = { "/"}, method = RequestMethod.GET)
//    public ModelAndView welcomePage() {
//        ModelAndView model = new ModelAndView();
//        model.setViewName("welcomePage");
//        return model;
//    }
//
//    @RequestMapping(value = { "/homePage"}, method = RequestMethod.GET)
//    public ModelAndView homePage() {
//        ModelAndView model = new ModelAndView();
//        model.setViewName("homePage");
//        return model;
//    }
//
//    @RequestMapping(value = {"/userPage"}, method = RequestMethod.GET)
//    public ModelAndView userPage() {
//        ModelAndView model = new ModelAndView();
//        model.setViewName("userPage");
//        return model;
//    }
//
//    @RequestMapping(value = {"/adminPage"}, method = RequestMethod.GET)
//    public ModelAndView adminPage() {
//        ModelAndView model = new ModelAndView();
//        model.setViewName("adminPage");
//        return model;
//    }
//
//    @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
//    public ModelAndView loginPage(@RequestParam(value = "error",required = false) String error,
//                                  @RequestParam(value = "logout",	required = false) String logout) {
//
//        ModelAndView model = new ModelAndView();
//        if (error != null) {
//            model.addObject("error", "Invalid Credentials provided.");
//        }
//
//        if (logout != null) {
//            model.addObject("message", "Logged out from JournalDEV successfully.");
//        }
//
//        model.setViewName("loginPage");
//        return model;
//    }
//
//
//
//
//


}
