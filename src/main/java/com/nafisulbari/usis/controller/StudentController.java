package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.entity.Advising;
import com.nafisulbari.usis.entity.Course;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.service.AdvisingService;
import com.nafisulbari.usis.service.CourseService;
import com.nafisulbari.usis.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

//--------Generate routine(A Course's list)----------------------------------------------
        List<Course> routine = getRoutine(getUserByEmail(principal.getName()).getId());

//-------Update  live routine ------------------------------------------
        theModel.addAttribute("routine", routine);
        return new ModelAndView("/student/student-home");
    }


    @GetMapping("/student/advising-panel")
    public ModelAndView showCourseList(@RequestParam(value = "searchKey", required = false) String searchKey, Model theModel, Principal principal) {

        List<Course> routine = getRoutine(getUserByEmail(principal.getName()).getId());

//------Update Advising panel and live routine ---------------------------------------
//------Searches courses or returns all courses---------------------------------------
        if (searchKey == null) {
            theModel.addAttribute("courses", courseService.findAllTheoryCourses());
        } else {
            theModel.addAttribute("courses", courseService.searchTheoryCourses(searchKey));
        }
        theModel.addAttribute("routine", routine);

        return new ModelAndView("/student/advising-panel");
    }


    @GetMapping("student/advising-panel/add-course/{id}")
    public ModelAndView addCourseToAdvise(@PathVariable("id") int id, Principal principal, Model model) {

        User student = getUserByEmail(principal.getName());

        Course theoryCourse = courseService.findCourseById(id);
        Course labCourse = courseService.getMatchingLabCourse(theoryCourse);

        String courseCodeToAdvice = theoryCourse.getCourseCode();
        List<Advising> advisedCourses = advisingService.findAdvisedCourses(student.getId());


//------Check total advised course, from routine to know Student's credit limit--------------------
        int totalAdvisedCourse = 0;
        List<Course> routine = new ArrayList<>();
        Course tempCourse;
        for (Advising advising : advisedCourses) {
            tempCourse = courseService.findCourseById(advising.getCourseId());
            routine.add(tempCourse);
            if (tempCourse.getLab() == 0) {
                totalAdvisedCourse++;
            }
        }

//------Checking Student's advising credit limit-------------------------------------------------------
        if (totalAdvisedCourse >= student.getCourseLimit()) {
            model.addAttribute("courseLimit", true);
            model.addAttribute("courses", courseService.findAllTheoryCourses());
            model.addAttribute("routine", routine);
            return new ModelAndView("/student/advising-panel");
        }

//------Checking if the course is already taken--------------------------------------------
        for (Advising course : advisedCourses) {

            if (courseService.findCourseById(course.getCourseId()).getCourseCode().equals(courseCodeToAdvice)) {
                model.addAttribute("courseAlreadyTaken", true);
                model.addAttribute("courses", courseService.findAllTheoryCourses());
                model.addAttribute("routine", routine);
                //--Course already taken flag enabled and returned updated ModelAndView----------------
                return new ModelAndView("/student/advising-panel");
            }
        }
//------Checking course available seat limit, here 5 is hard coded-------------------------------
        if (theoryCourse.getSeat() >= 5) {

            model.addAttribute("seatLimit", true);
            model.addAttribute("courses", courseService.findAllTheoryCourses());
            model.addAttribute("routine", routine);
            return new ModelAndView("/student/advising-panel");
        }
//------Course can be advised now---------------------------------------------------------------
        //----theory and lab seat status updated with +1-----
        theoryCourse.setSeat(theoryCourse.getSeat() + 1);
        courseService.saveOrUpdateCourse(theoryCourse);
//------If theory's lab exist it ll advise the lab course too----------------------------------
        if (labCourse.getCourseCode() != null) {
            labCourse.setSeat(labCourse.getSeat() + 1);
            courseService.saveOrUpdateCourse(labCourse);
        }

//------Theory and lab course advised----------------------------------------------------------
        Advising advisingTheory = new Advising();
        advisingTheory.setCourseId(id);
        advisingTheory.setStdId(student.getId());
        advisingService.saveAdvisedCourse(advisingTheory);

        if (labCourse.getCourseCode() != null) {
            Advising advisingLab = new Advising();
            advisingLab.setCourseId(labCourse.getId());
            advisingLab.setStdId(student.getId());
            advisingService.saveAdvisedCourse(advisingLab);
        }
        return new ModelAndView("redirect:/student/advising-panel");
    }

    @GetMapping("student/advising-panel/drop-course/{id}")
    public ModelAndView dropCourseFromAdvise(@PathVariable("id") int id, Principal principal, Model model) {

        Course theoryCourse = courseService.findCourseById(id);

        User student = getUserByEmail(principal.getName());

        List<Advising> stdAdvising = advisingService.findAdvisedCourses(student.getId());

        List<Course> routine = getRoutine(student.getId());
//------If the theory course is dropped, its lab course is also dropped-------------------------------
        for (Course course : routine) {
            if (course.getCourseCode().equals(theoryCourse.getCourseCode())) {
                for (Advising advising : stdAdvising) {
                    if (advising.getCourseId() == course.getId()) {

                        course.setSeat(course.getSeat() - 1);
                        courseService.saveOrUpdateCourse(course);
                        advisingService.deleteAdvicedCourse(advising.getId());
                    }
                }
            }
        }


        return new ModelAndView("redirect:/student/advising-panel");
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

    private User getUserByEmail(String email) {
        User student = new User();
        student.setEmail(email);
        student = userService.findUserByEmail(student);
        return student;
    }
}
