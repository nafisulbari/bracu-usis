package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeacherController {



    private UserService userService;


    public TeacherController(UserService theUserSer){
        userService=theUserSer;
    }




    @GetMapping("/teacher/teacher-home")
    public String teacherHome(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "/teacher/teacher-home";
    }
}
