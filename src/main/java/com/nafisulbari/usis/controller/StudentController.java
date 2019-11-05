package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.service.AdvisingService;
import com.nafisulbari.usis.service.CourseService;
import com.nafisulbari.usis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StudentController {

    private CourseService courseService;
    private UserService userService;
    private AdvisingService advisingService;


    public StudentController(CourseService theCourseService, UserService theUserService, AdvisingService theAdvisingService) {
        courseService = theCourseService;
        userService = theUserService;
        advisingService = theAdvisingService;
    }


    @GetMapping("/student/student-home")
    public String studentHome(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "/student/student-home";
    }

    @GetMapping("/student/advising-panel")
    public ModelAndView showCourseList(User theUser, BindingResult result, Model theModel) {
        theModel.addAttribute("courses", courseService.findAllCourses());

        System.out.println(theUser.toString());
        System.out.println(theModel.toString());

        return new ModelAndView("/student/advising-panel", String.valueOf(theModel), theUser);
    }
}
