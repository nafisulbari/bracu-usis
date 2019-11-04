package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.service.CourseService;
import com.nafisulbari.usis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    private CourseService courseService;
    private UserService userService;


    public StudentController(CourseService theCourseService, UserService theUserService) {
        courseService = theCourseService;
        userService = theUserService;
    }


    @GetMapping("/student/student-home")
    public String adminHome(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "/student/student-home";
    }

    @GetMapping("/student/advising-panel")
    public String showCourseList(Model model) {
        model.addAttribute("courses", courseService.findAllCourses());
        System.out.println(model.toString());
        return "/student/advising-panel";
    }
}
