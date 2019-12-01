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
import org.springframework.web.bind.annotation.PathVariable;
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

        model.addAttribute("student", student);
        model.addAttribute("routine", getRoutine(student.getId()));
        return new ModelAndView("/teacher/student-panel");
    }


    //-------------on dev-------------------------------------------------------------------------------------------------------
    @GetMapping("/teacher/student-panel/{id}")
    public ModelAndView studentPanel(@PathVariable("id") int studentId, String courseCode, String courseType, Model model) {

        User student = userService.findUserById(studentId);
        System.out.println(student);
        System.out.println("CC: " + courseCode);
        System.out.println("CT:" + courseType);


        model.addAttribute("student", student);
        model.addAttribute("routine", getRoutine(studentId));
        return new ModelAndView("/teacher/student-panel");
    }

//------Generates routine from studentId------------------------------------------------------------------------------------
    private List<Course> getRoutine(int studentId) {
        List<Advising> advisedCourses = advisingService.findAdvisedCourses(studentId);
        List<Course> routine = new ArrayList<>();
        for (Advising advising : advisedCourses) {
            routine.add(courseService.findCourseById(advising.getCourseId()));
        }
        return routine;
    }
}
