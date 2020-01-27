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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


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
    public RedirectView homeToLoginPage() {
        return new RedirectView("/login");
    }

    @GetMapping("/login")
    public ModelAndView login(User theUser, BindingResult result, Model theModel) {
    theModel.addAttribute(theModel);
    return new ModelAndView("login", String.valueOf(theModel), theUser);
    }

    @PostMapping("/home-detector")
    public ModelAndView forwardToHome(User theUser, BindingResult result, Model theModel) {

        String role = userService.findUserByEmail(theUser).getRole();

        if (role.equals("ADMIN")) {
            return new ModelAndView("redirect:/admin/admin-home", String.valueOf(theModel), theUser);
        }
        if (role.equals("TEACHER")) {
            return new ModelAndView("redirect:/teacher/teacher-home", String.valueOf(theModel), theUser);
        }
        if (role.equals("STUDENT")) {
            return new ModelAndView("redirect:/student/student-home", String.valueOf(theModel), theUser);
        }

        return new ModelAndView("login", String.valueOf(theModel), theUser);
    }


    @GetMapping("/logout")
    public ModelAndView logout() {
        return new ModelAndView("logout");
    }

    @GetMapping("/forgot-password")
    public ModelAndView forgotPasswordPage(PasswordRequest thePasswordRequest) {
        return new ModelAndView("forgot-password");
    }

    @PostMapping("/request-password")
    public ModelAndView passwordRequestService(PasswordRequest thePasswordRequest, Model themodel, BindingResult result) {

        User tempUser = new User();
        tempUser.setEmail(thePasswordRequest.getEmail());
        String password = thePasswordRequest.getPassword();
        if (!password.matches("[a-zA-Z0-9]{8,}")) {
            themodel.addAttribute("messagePasswordPattern", true);
            themodel.addAttribute(thePasswordRequest);
            return new ModelAndView("forgot-password");
        }

        if (userService.findUserByEmail(tempUser) == null || userService.findUserByEmail(tempUser).getEmail().isEmpty()) {
            themodel.addAttribute("messageEmailDoesNotExists", true);
            themodel.addAttribute(thePasswordRequest);
            return new ModelAndView("forgot-password");
        }

        if (previousPasswordService.findPreviousPasswordByEmail(thePasswordRequest)) {
            themodel.addAttribute("messagePasswordUsed", true);
            return new ModelAndView("forgot-password");
        } else {
            passwordRequestService.savePasswordRequest(thePasswordRequest);
            return new ModelAndView("redirects/password-requested");
        }

    }



}
