package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.entity.PasswordRequest;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.service.PasswordRequestService;
import com.nafisulbari.usis.service.PreviousPasswordService;
import com.nafisulbari.usis.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {

    private UserService userService;
    private PasswordRequestService passwordRequestService;
    private PreviousPasswordService previousPasswordService;
    private PasswordEncoder passwordEncoder;

    public LoginController(UserService userService, PasswordRequestService passwordRequestService, PreviousPasswordService previousPasswordService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordRequestService = passwordRequestService;
        this.previousPasswordService = previousPasswordService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/")
    public ModelAndView homeToLoginPage(User theUser, BindingResult result, Model theModel) {
        return new ModelAndView("login", String.valueOf(theModel), theUser);
    }
    @GetMapping("login")
    public String login(){
        return "login";
    }

//    @PostMapping("/login")
//    public ModelAndView homePage(User theUser, BindingResult result, Model theModel) {
//
//        String hashed = passwordEncoder.encode(theUser.getPassword());
//
//        System.out.println(hashed);
//        theUser.setPassword(hashed);
//
//        User tempUser = userService.findUserByEmail(theUser);
//
//        if (tempUser == null || tempUser.getEmail() == null) {
//            theModel.addAttribute("user", theUser);
//            theModel.addAttribute("wrongEmail", true);
//            return new ModelAndView("/login", String.valueOf(theModel), theUser);
//        }
//
//        String role = userService.loginAuthenticator(theUser);
//
//        if (role.equals("ADMIN")) {
//            return new ModelAndView("redirect:/admin/admin-home", String.valueOf(theModel), theUser);
//        }
//        if (role.equals("TEACHER")) {
//            return new ModelAndView("redirect:/teacher/teacher-home", String.valueOf(theModel), theUser);
//        }
//        if (role.equals("STUDENT")) {
//            return new ModelAndView("redirect:/student/student-home", String.valueOf(theModel), theUser);
//        }
//
//        theModel.addAttribute("user", theUser);
//        theModel.addAttribute("wrongCredentials", true);
////----------------------------------------ModelAndView("redirect:/login" gives error which cannot be fixed----------------------------------
//        return new ModelAndView("/login", String.valueOf(theModel), theUser);
//    }


    @GetMapping("/logout")
    public String logout() {
        return "/logout";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(PasswordRequest thePasswordRequest) {

        return "forgot-password";
    }

    @PostMapping("/request-password")
    public String passwordRequestService(PasswordRequest thePasswordRequest, Model themodel, BindingResult result) {

        User tempUser = new User();
        tempUser.setEmail(thePasswordRequest.getEmail());
        String password = thePasswordRequest.getPassword();
        if (!password.matches("[a-zA-Z0-9]{8,}")) {
            themodel.addAttribute("messagePasswordPattern", true);
            themodel.addAttribute(thePasswordRequest);
            return "/forgot-password";
        }

        if (userService.findUserByEmail(tempUser) == null || userService.findUserByEmail(tempUser).getEmail().isEmpty()) {
            themodel.addAttribute("messageEmailDoesNotExists", true);
            themodel.addAttribute(thePasswordRequest);
            return "/forgot-password";
        }


        if (previousPasswordService.findPreviousPasswordByEmail(thePasswordRequest)) {
            themodel.addAttribute("messagePasswordUsed", true);
            return "/forgot-password";
        } else {
            passwordRequestService.savePasswordRequest(thePasswordRequest);
            return "/redirects/password-requested";
        }

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
