package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {


    private UserService userService;

    public LoginController(UserService theUserService) {
        userService = theUserService;
    }

    @GetMapping("/")
    public String loginPage(User theUser) {
        return "login";
    }

    @PostMapping("/login")
    public String homePage(User theUser, Model theModel) {

        String role = userService.loginAuthenticator(theUser);

        if (role.equals("admin"))
            return "admin/admin-home";
        if (role.equals("teacher"))
            return "teacher/teacher-home";
        if (role.equals("student"))
            return "student/student-home";

        theModel.addAttribute("user", theUser);
        return "login";
    }




}
