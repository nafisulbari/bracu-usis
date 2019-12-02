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
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
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


    @GetMapping("/teacher/student-panel/{id}")
    public ModelAndView studentPanel(@PathVariable("id") int studentId, String courseCode, String courseType, Model model) {

        User student = userService.findUserById(studentId);
        List<Course> routine = getRoutine(studentId);
        List<Course> searchedCourse = courseService.getSpecificCourseList(courseCode);

//------studentPanelSwapSection() success gives courseCode=null---------------------
        if (courseCode == null) {
            model.addAttribute("student", student);
            model.addAttribute("routine", routine);
            model.addAttribute("flagCourseSearch", "swapSuccessful");
            return new ModelAndView("/teacher/student-panel");
        }
//------Did not input course type flag----------------------------------------------
        if (!courseCode.equals("") && courseType == null) {
            model.addAttribute("student", student);
            model.addAttribute("routine", routine);
            model.addAttribute("flagCourseSearch", "inputCourseType");
            return new ModelAndView("/teacher/student-panel");
        }
//------Narrowing down searchedCourse as only lab or only theory, and excluding pre advised section--
        List<Course> narrowedSearchedCourse = new ArrayList<>();

        if (courseType != null && Integer.parseInt(courseType) != 2) {
            for (Course course : searchedCourse) {
                if (course.getLab() == Integer.parseInt(courseType) && !routine.contains(course)) {
                    narrowedSearchedCourse.add(course);
                }
            }
        }
//------Narrowing down searchedCourse as both, excluding pre advised section-------------------------
        if (courseType != null && Integer.parseInt(courseType) == 2) {
            for (Course course : searchedCourse) {
                if (!routine.contains(course)) {
                    narrowedSearchedCourse.add(course);
                }
            }
        }
//------Searched Course not Found flag----------------------------------------------
        if (!courseCode.equals("") && narrowedSearchedCourse.isEmpty()) {
            model.addAttribute("student", student);
            model.addAttribute("routine", routine);
            model.addAttribute("flagCourseSearch", "notFound");
            return new ModelAndView("/teacher/student-panel");
        }
//------When nothing is searched----------------------------------------------------
        if (courseCode.equals("")) {
            model.addAttribute("student", student);
            model.addAttribute("routine", routine);
            return new ModelAndView("/teacher/student-panel");
        }
//------Searched course is Not in routine----------------------------------------------
        boolean flagCourseNotAdvised=true;
        for (Course course: routine){
            if (course.getCourseCode().equals(courseCode)){
                flagCourseNotAdvised=false;
            }
        }
        if (flagCourseNotAdvised) {
            model.addAttribute("flagCourseSearch", "courseNotAdvised");
            model.addAttribute("student", student);
            model.addAttribute("routine", routine);
            return new ModelAndView("/teacher/student-panel");
        }



//------Detect advisableCourses which do no clash, from narrowedDown list------------
        List<Course> advisableCourse = new ArrayList<>();
        for (Course course : narrowedSearchedCourse) {
            boolean flag = true;
            for (Course rou : routine) {

                if (!course.getSaturday().equals("") && !rou.getSaturday().equals("") && course.getSaturday().equals(rou.getSaturday())) {
                    flag = false;
                }
                if (!course.getSunday().equals("") && !rou.getSunday().equals("") && course.getSunday().equals(rou.getSunday())) {
                    flag = false;
                }
                if (!course.getMonday().equals("") && !rou.getMonday().equals("") && course.getMonday().equals(rou.getMonday())) {
                    flag = false;
                }
                if (!course.getTuesday().equals("") && !rou.getTuesday().equals("") && course.getTuesday().equals(rou.getTuesday())) {
                    flag = false;
                }
                if (!course.getWednesday().equals("") && !rou.getWednesday().equals("") && course.getWednesday().equals(rou.getWednesday())) {
                    flag = false;
                }
                if (!course.getThursday().equals("") && !rou.getThursday().equals("") && course.getThursday().equals(rou.getThursday())) {
                    flag = false;
                }

            }
            if (flag) {
                advisableCourse.add(course);
            }
        }
//------Detects available sections where both theory and lab doesnt clash--------------
        List<Course> bothAdvisableCourse = new ArrayList<>();
        if (Integer.parseInt(courseType) == 2) {
            for (int i = 0; i < advisableCourse.size(); i++) {
                int count = 0;
                for (int j = i; j < advisableCourse.size(); j++) {
                    if (advisableCourse.get(i).getSection() == advisableCourse.get(j).getSection()) {
                        count++;
                        if (count == 2) {
                            bothAdvisableCourse.add(advisableCourse.get(i));
                            break;
                        }
                    }
                }
            }
            //--Could not find both non clashing section---------------------------------
            if (bothAdvisableCourse.isEmpty()) {
                model.addAttribute("flagAvailableCourse", "notFound");
            } else {
                advisableCourse = bothAdvisableCourse;
            }
        }
        //--Could not find non clashing section---------------------------------
        if (Integer.parseInt(courseType) != 2 && advisableCourse.isEmpty()) {
            model.addAttribute("flagAvailableCourse", "notFound");
        }

        model.addAttribute("courseType", courseType);
        model.addAttribute("advisableCourse", advisableCourse);
        model.addAttribute("student", student);
        model.addAttribute("routine", routine);
        return new ModelAndView("/teacher/student-panel");
    }

    //---------Swap sections-------------------------------------------------------------------
    @GetMapping("/teacher/student-panel/{id}/{courseId}/{courseType}")
    public RedirectView studentPanelSwapSection(@PathVariable("id") String stdId,
                                                @PathVariable("courseId") String crId,
                                                @PathVariable("courseType") String crType, Model model) {

        int studentId = Integer.parseInt(stdId);
        int courseId = Integer.parseInt(crId);
        int courseType = Integer.parseInt(crType);

        List<Advising> studentAdvising = advisingService.findAdvisedCourses(studentId);
        Course addCourse = courseService.findCourseById(courseId);
        List<Course> routine = getRoutine(studentId);
//-------Both theory and lab swap----------------------------------------------------
        if (courseType == 2) {
            Course labCourse = courseService.getMatchingLabCourse(addCourse);
            for (Course course : routine) {
                if (course.getCourseCode().equals(addCourse.getCourseCode()) && course.getLab() == 0) {
                    swapAdvising(studentAdvising, course, studentId, addCourse);
                }
                if (course.getCourseCode().equals(labCourse.getCourseCode()) && course.getLab() == 1) {
                    swapAdvising(studentAdvising, course, studentId, labCourse);
                }
            }

//-------Theory or lab swap-----------------------------------------------------------
        } else {
            for (Course course : routine) {
                if (course.getCourseCode().equals(addCourse.getCourseCode()) && course.getLab() == courseType) {

                    swapAdvising(studentAdvising, course, studentId, addCourse);
                }
            }
        }
        model.addAttribute("student", userService.findUserById(studentId));
        model.addAttribute("routine", getRoutine(studentId));
        return new RedirectView("/teacher/student-panel/" + studentId);

    }

    //----------Methods--------------------------------------------------------------------------------------------
    //------Generates routine from studentId-------------------------------------------------------------------
    private List<Course> getRoutine(int studentId) {
        List<Advising> advisedCourses = advisingService.findAdvisedCourses(studentId);
        List<Course> routine = new ArrayList<>();
        for (Advising advising : advisedCourses) {
            routine.add(courseService.findCourseById(advising.getCourseId()));
        }
        return routine;
    }

    //------Swap course in Advising Table  &  Update seat status--------------------------------------------------
    private void swapAdvising(List<Advising> studentAdvising, Course rouCourse, int studentId, Course newCourse) {

        rouCourse.setSeat(rouCourse.getSeat() - 1);
        courseService.saveOrUpdateCourse(rouCourse);

        newCourse.setSeat(newCourse.getSeat() + 1);
        courseService.saveOrUpdateCourse(newCourse);

        for (Advising advising : studentAdvising) {
            if (advising.getCourseId() == rouCourse.getId()) {
                advisingService.deleteAdvicedCourse(advising.getId());

                Advising add = new Advising();
                add.setStdId(studentId);
                add.setCourseId(newCourse.getId());
                advisingService.saveAdvisedCourse(add);
            }
        }
    }
}
