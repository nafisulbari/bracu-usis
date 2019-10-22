package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.entity.PasswordRequest;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.security.MD5;
import com.nafisulbari.usis.service.PasswordRequestService;
import com.nafisulbari.usis.service.PreviousPasswordService;
import com.nafisulbari.usis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private UserService userService;
    private PasswordRequestService passwordRequestService;
    private PreviousPasswordService previousPasswordService;

    public LoginController(UserService theUserService, PasswordRequestService thePasswordRequestService, PreviousPasswordService thePreviousPasswordService) {
        userService = theUserService;
        passwordRequestService = thePasswordRequestService;
        previousPasswordService = thePreviousPasswordService;
    }

    @GetMapping("/")
    public String loginPage(User theUser) {
        return "login";
    }

    @PostMapping("/login")
    public String homePage(User theUser, BindingResult result, Model theModel) {

        String hashed = MD5.getMd5(theUser.getPassword());

        System.out.println(hashed);
        theUser.setPassword(hashed);

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
        theModel.addAttribute("wrongCredentials", true);
        return "login";
    }


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
        if (!password.matches("[a-zA-Z0-9]{8,}")){
            themodel.addAttribute("messagePasswordPattern", true);
            themodel.addAttribute(thePasswordRequest);
            return "/forgot-password";
        }
        try {
            if (userService.findUserByEmail(tempUser).getEmail() == null) {
              //keeping empty if clause intentionally to handel null
            }
        }catch (NullPointerException npe){
            npe.printStackTrace();
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
