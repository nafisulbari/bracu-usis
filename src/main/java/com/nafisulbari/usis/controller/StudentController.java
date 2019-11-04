package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    private CourseService courseService;


    public StudentController(CourseService theCourseService){
        courseService=theCourseService;
    }




    @GetMapping("/student/advising-panel")
    public String showCourseList(Model model) {
        model.addAttribute("courses", courseService.findAllCourses());
        System.out.println(model.toString());
        return "/student/advising-panel";
    }
}
