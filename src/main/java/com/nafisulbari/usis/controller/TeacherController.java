package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.entity.Advising;
import com.nafisulbari.usis.entity.Course;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.service.AdvisingService;
import com.nafisulbari.usis.service.CourseService;
import com.nafisulbari.usis.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeacherController {

    private UserService userService;
    private AdvisingService advisingService;
    private CourseService courseService;

    public TeacherController(UserService theUserService, AdvisingService theAdvisingService, CourseService theCourseService) {
        userService = theUserService;
        advisingService = theAdvisingService;
        courseService = theCourseService;

    }


    @GetMapping("/teacher/teacher-home")
    public ModelAndView teacherHome(@RequestParam(value = "stdEmail", required = false) String stdEmail, Model model) {
//------if empty string or null is searched-----------------------------------------------
        if (stdEmail == null || stdEmail.equals("")) {
            model.addAttribute("flagStudentPresent", "no message");
            return new ModelAndView("/teacher/teacher-home");
        }
        User student = new User();
        student.setEmail(stdEmail);
        student = userService.findUserByEmail(student);
//------if student not found in db ------------------------------------------------------
        if (student == null) {
            model.addAttribute("flagStudentPresent", "notFound");
            return new ModelAndView("/teacher/teacher-home");
        }

//--------Student FOUND in db------------------------------------------------------------
//--------Generate routine(A Course's list) and return student-panel---------------------
        List<Advising> advisedCourses = advisingService.findAdvisedCourses(student.getId());
        List<Course> routine = new ArrayList<>();
        for (Advising advising : advisedCourses) {
            routine.add(courseService.findCourseById(advising.getCourseId()));
        }
        model.addAttribute("student", student);
        model.addAttribute("routine", routine);
        return new ModelAndView("/teacher/student-panel");
    }


//-------------on dev-------------------------------------------------------------------------------------------------------
    @GetMapping("/teacher/student-panel")
    public ModelAndView studentPanel(String courseCode, String courseType, @ModelAttribute("student") User student, Model model) {

        System.out.println("CC: " + courseCode);
        System.out.println("CT:" + courseType);

        System.out.println("model   :" + model);
        System.out.println(student);

//--------Generate routine(A Course's list) and return student-panel---------------------
        List<Advising> advisedCourses = advisingService.findAdvisedCourses(student.getId());
        List<Course> routine = new ArrayList<>();
        for (Advising advising : advisedCourses) {
            routine.add(courseService.findCourseById(advising.getCourseId()));
        }
        model.addAttribute("student", student);
        model.addAttribute("routine", routine);
        return new ModelAndView("/teacher/student-panel");
    }

}
