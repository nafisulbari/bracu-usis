package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.entity.Advising;
import com.nafisulbari.usis.entity.Course;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.service.AdvisingService;
import com.nafisulbari.usis.service.CourseService;
import com.nafisulbari.usis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
    public ModelAndView studentHome(Model theModel, Principal principal) {

//--------Find Student's advising info---------------------------------------------------
        User student = new User();
        student.setEmail(principal.getName());
        int stdId = userService.findUserByEmail(student).getId();
        List<Advising> advisedCourses = advisingService.findAdvisedCourses(stdId);
//--------Generate routine(A Course's list)----------------------------------------------
        List<Course> routine = new ArrayList<>();
        for (Advising advising : advisedCourses) {
            routine.add(courseService.findCourseById(advising.getCourseId()));
        }

//-------Update  live routine ------------------------------------------
        theModel.addAttribute("routine", routine);
        return new ModelAndView("/student/student-home");
    }


    @GetMapping("/student/advising-panel")
    public ModelAndView showCourseList(@RequestParam(value = "searchKey", required = false) String searchKey, Model theModel, Principal principal) {
//--------Find Student's advising info---------------------------------------------------
        User student = new User();
        student.setEmail(principal.getName());
        int stdId = userService.findUserByEmail(student).getId();
        List<Advising> advisedCourses = advisingService.findAdvisedCourses(stdId);
//--------Generate routine(A Course's list)----------------------------------------------
        List<Course> routine = new ArrayList<>();
        for (Advising advising : advisedCourses) {
            routine.add(courseService.findCourseById(advising.getCourseId()));
        }

//------Update Advising panel and live routine ---------------------------------------
//------Searches courses or returns all courses---------------------------------------
        if (searchKey == null) {
            theModel.addAttribute("courses", courseService.findAllCourses());
        } else {
            theModel.addAttribute("courses", courseService.searchCourses(searchKey));
        }
        theModel.addAttribute("routine", routine);

        return new ModelAndView("/student/advising-panel");
    }


    @GetMapping("student/advising-panel/add-course/{id}")
    public ModelAndView addCourseToAdvise(@PathVariable("id") int id, Principal principal, Model model) {

        User student = new User();
        student.setEmail(principal.getName());
        student = userService.findUserByEmail(student);
        int stdId = student.getId();
        int totalAdvisedCourse = 0;

        Course courseToAdvice = courseService.findCourseById(id);
        String courseCodeToAdvice = courseToAdvice.getCourseCode();
        List<Advising> advisedCourses = advisingService.findAdvisedCourses(stdId);


//--------Generate routine(AKA Advised Course's list)--------------------------------------
        List<Course> routine = new ArrayList<>();
        for (Advising advising : advisedCourses) {
            routine.add(courseService.findCourseById(advising.getCourseId()));
        }


//------Checking Student's advising limit-------------------------------------------------------
        for (Advising course : advisedCourses) {
            totalAdvisedCourse++;
        }
        if (totalAdvisedCourse >= student.getCourseLimit()) {
            model.addAttribute("courseLimit", true);
            model.addAttribute("courses", courseService.findAllCourses());
            model.addAttribute("routine", routine);
            return new ModelAndView("/student/advising-panel");
        }


//------Checking if the course is already taken--------------------------------------------
        for (Advising course : advisedCourses) {
            if (courseService.findCourseById(course.getCourseId()).getCourseCode().equals(courseCodeToAdvice)) {

                model.addAttribute("courseAlreadyTaken", true);
                model.addAttribute("courses", courseService.findAllCourses());
                model.addAttribute("routine", routine);
//------Course already taken flag enabled and returned updated ModelAndView----------------
                return new ModelAndView("/student/advising-panel");
            }
        }
//------Checking course available seat limit, here 5 is hard coded-------------------------------
        if (courseToAdvice.getSeat() >= 5) {

            model.addAttribute("seatLimit", true);
            model.addAttribute("courses", courseService.findAllCourses());
            model.addAttribute("routine", routine);
            return new ModelAndView("/student/advising-panel");
        }
//-------Course can be advised now---------------------------------------------------------------
        //----course seat status updated with +1-----
        courseToAdvice.setSeat(courseToAdvice.getSeat() + 1);
        courseService.saveOrUpdateCourse(courseToAdvice);

        Advising advising = new Advising();
        advising.setCourseId(id);
        advising.setStdId(stdId);
        advisingService.saveAdvisedCourse(advising);

        return new ModelAndView("redirect:/student/advising-panel");
    }

    @GetMapping("student/advising-panel/drop-course/{id}")
    public ModelAndView dropCourseFromAdvise(@PathVariable("id") int id, Principal principal, Model model) {

        User student = new User();
        student.setEmail(principal.getName());
        int stdId = userService.findUserByEmail(student).getId();

        List<Advising> advisedCourses = advisingService.findAdvisedCourses(stdId);
        int deleteAdvising = 0;
        for (Advising advising : advisedCourses) {
            if (advising.getCourseId() == id) {
                deleteAdvising = advising.getId();
                break;
            }
        }
        //-------Removing course by advisingID and course seat status updating---------
        Course theCourse = courseService.findCourseById(id);
        theCourse.setSeat(theCourse.getSeat() - 1);
        courseService.saveOrUpdateCourse(theCourse);
        advisingService.deleteAdvicedCourse(deleteAdvising);

        return new ModelAndView("redirect:/student/advising-panel");
    }

}
