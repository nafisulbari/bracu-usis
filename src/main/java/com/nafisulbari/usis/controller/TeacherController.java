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
import org.springframework.web.servlet.view.RedirectView;

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
        model.addAttribute("courses", courseService.findAllTheoryCourses());
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
            model.addAttribute("courses", courseService.findAllTheoryCourses());
            model.addAttribute("flagCourseSearch", "swapSuccessful");
            return new ModelAndView("/teacher/student-panel");
        }

        //--------------Making upercase coz in db things are in uppercase-------------
        // courseCode=courseCode.toUpperCase();
//------Did not input course type flag----------------------------------------------
        if (!courseCode.equals("") && courseType == null) {
            model.addAttribute("student", student);
            model.addAttribute("routine", routine);
            model.addAttribute("courses", courseService.findAllTheoryCourses());
            model.addAttribute("flagCourseSearch", "inputCourseType");
            return new ModelAndView("/teacher/student-panel");
        }
//------Narrowing down searchedCourse as only lab or only theory, also excluding pre advised section and seat limit--
//----------------------NARROW DOWN------------------------------------------------------------------
        List<Course> narrowedSearchedCourse = new ArrayList<>();

        if (courseType != null && Integer.parseInt(courseType) != 2) {
            for (Course course : searchedCourse) {
                if (course.getLab() == Integer.parseInt(courseType) && !routine.contains(course) && course.getSeat() <= 6) {
                    narrowedSearchedCourse.add(course);
                }
            }
        }
//------Narrowing down searchedCourse as both, excluding pre advised section-------------------------
//----------------------NARROW DOWN------------------------------------------------------------------
        if (courseType != null && Integer.parseInt(courseType) == 2) {
            for (Course course : searchedCourse) {
                if (!routine.contains(course) && course.getSeat() <= 6) {
                    narrowedSearchedCourse.add(course);
                }
            }
        }
//------Searched Course not Found flag----------------------------------------------
        if (!courseCode.equals("") && narrowedSearchedCourse.isEmpty()) {
            model.addAttribute("student", student);
            model.addAttribute("routine", routine);
            model.addAttribute("courses", courseService.findAllTheoryCourses());
            model.addAttribute("flagCourseSearch", "notFound");
            return new ModelAndView("/teacher/student-panel");
        }
//------When nothing is searched----------------------------------------------------
        if (courseCode.equals("")) {
            model.addAttribute("student", student);
            model.addAttribute("routine", routine);
            model.addAttribute("courses", courseService.findAllTheoryCourses());
            return new ModelAndView("/teacher/student-panel");
        }
//------Searched course is Not in routine----------------------------------------------
        boolean flagCourseNotAdvised = true;
        for (Course course : routine) {
            if (course.getCourseCode().equals(courseCode)) {
                flagCourseNotAdvised = false;
                break;
            }
        }
        if (flagCourseNotAdvised) {
            model.addAttribute("flagCourseSearch", "courseNotAdvised");
            model.addAttribute("student", student);
            model.addAttribute("routine", routine);
            model.addAttribute("courses", courseService.findAllTheoryCourses());
            return new ModelAndView("/teacher/student-panel");
        }


//------Detect advisableCourses which do no clash, from narrowedDown list------------
        List<Course> advisableCourse = new ArrayList<>();
        for (Course course : narrowedSearchedCourse) {
            boolean flag = true;
            for (Course rou : routine) {

                if (!course.getSaturday().equals("") && !rou.getSaturday().equals("") &&
                        (course.getSaturday().contains(rou.getSaturday()) || rou.getSaturday().contains(course.getSaturday()))) {
                    flag = false;
                }
                if (!course.getSunday().equals("") && !rou.getSunday().equals("") &&
                        (course.getSunday().contains(rou.getSunday()) || rou.getSunday().contains(course.getSunday()))) {
                    flag = false;
                }
                if (!course.getMonday().equals("") && !rou.getMonday().equals("") &&
                        (course.getMonday().contains(rou.getMonday()) || rou.getMonday().contains(course.getMonday()))) {
                    flag = false;
                }
                if (!course.getTuesday().equals("") && !rou.getTuesday().equals("") &&
                        (course.getTuesday().contains(rou.getTuesday()) || rou.getTuesday().contains(course.getTuesday()))) {
                    flag = false;
                }
                if (!course.getWednesday().equals("") && !rou.getWednesday().equals("") &&
                        (course.getWednesday().contains(rou.getWednesday()) || rou.getWednesday().contains(course.getWednesday()))) {
                    flag = false;
                }
                if (!course.getThursday().equals("") && !rou.getThursday().equals("") &&
                        (course.getThursday().contains(rou.getThursday()) || rou.getThursday().contains(course.getThursday()))) {
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
        model.addAttribute("courses", courseService.findAllTheoryCourses());
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
        Course theoryCourse = courseService.findCourseById(courseId);
        List<Course> routine = getRoutine(studentId);
//-------Both theory and lab swap----------------------------------------------------
        if (courseType == 2) {
            Course labCourse = courseService.getMatchingLabCourse(theoryCourse);
            for (Course course : routine) {
                if (course.getCourseCode().equals(theoryCourse.getCourseCode()) && course.getLab() == 0) {
                    swapAdvising(studentAdvising, course, studentId, theoryCourse);
                }
                if (course.getCourseCode().equals(labCourse.getCourseCode()) && course.getLab() == 1) {
                    swapAdvising(studentAdvising, course, studentId, labCourse);
                }
            }

//-------Theory or lab swap-----------------------------------------------------------
        } else {
            for (Course course : routine) {
                if (course.getCourseCode().equals(theoryCourse.getCourseCode()) && course.getLab() == courseType) {

                    swapAdvising(studentAdvising, course, studentId, theoryCourse);
                }
            }
        }
        model.addAttribute("student", userService.findUserById(studentId));
        model.addAttribute("routine", getRoutine(studentId));
        return new RedirectView("/teacher/student-panel/" + studentId);

    }


    @GetMapping("/teacher/student-panel/add-course/{studentId}/{id}")
    public ModelAndView teachAddCourseToAdvise(@PathVariable("studentId") int studentId,
                                               @PathVariable("id") int id,
                                               Model model) {

        User student = userService.findUserById(studentId);

        int totalAdvisedCourse = 0;

        Course theoryCourse = courseService.findCourseById(id);
        Course labCourse = courseService.getMatchingLabCourse(theoryCourse);
        String courseCodeToAdvice = theoryCourse.getCourseCode();
        List<Advising> advisedCourses = advisingService.findAdvisedCourses(student.getId());


//--------Generate routine(AKA Advised Course's list)--------------------------------------
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
            model.addAttribute("routine", getRoutine(studentId));
            model.addAttribute("student", student);
            return new ModelAndView("/teacher/student-panel");
        }

//------Checking if the course is already taken--------------------------------------------
        for (Advising course : advisedCourses) {

            if (courseService.findCourseById(course.getCourseId()).getCourseCode().equals(courseCodeToAdvice)) {
                model.addAttribute("courseAlreadyTaken", true);
                model.addAttribute("courses", courseService.findAllTheoryCourses());
                model.addAttribute("routine", getRoutine(studentId));
                model.addAttribute("student", student);
                //--Course already taken flag enabled and returned updated ModelAndView----------------
                return new ModelAndView("/teacher/student-panel");
            }
        }
//------Checking course available seat limit, here 6 is hard coded for teacher-------------------------------
        if (theoryCourse.getSeat() >= 6) {

            model.addAttribute("seatLimit", true);
            model.addAttribute("courses", courseService.findAllTheoryCourses());
            model.addAttribute("routine", getRoutine(studentId));
            model.addAttribute("student", student);
            return new ModelAndView("/teacher/student-panel");
        }
//------Course can be advised now---------------------------------------------------------------
        //----theory and lab seat status updated with +1-----
        theoryCourse.setSeat(theoryCourse.getSeat() + 1);
        courseService.saveOrUpdateCourse(theoryCourse);

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
        model.addAttribute("courses", courseService.findAllTheoryCourses());
        model.addAttribute("routine", getRoutine(studentId));
        model.addAttribute("student", student);
        return new ModelAndView("/teacher/student-panel");
    }


    @GetMapping("teacher/student-panel/drop-course/{studentId}/{id}")
    public ModelAndView teachDropCourseFromAdvise(@PathVariable("id") int id,
                                                  @PathVariable("studentId") int studentId,
                                                  Model model) {

        Course dropCourse = courseService.findCourseById(id);

        User student = userService.findUserById(studentId);

        List<Advising> stdAdvising = advisingService.findAdvisedCourses(student.getId());
        List<Course> routine = getRoutine(student.getId());
//------If the theory course is dropped, its lab course is also dropped-------------------------------
        for (Course course : routine) {
            if (course.getCourseCode().equals(dropCourse.getCourseCode())) {
                for (Advising advising : stdAdvising) {
                    if (advising.getCourseId() == course.getId()) {

                        course.setSeat(course.getSeat() - 1);
                        courseService.saveOrUpdateCourse(course);
                        advisingService.deleteAdvicedCourse(advising.getId());
                    }
                }
            }
        }


        model.addAttribute("student", student);
        model.addAttribute("routine", getRoutine(studentId));
        model.addAttribute("courses", courseService.findAllTheoryCourses());
        return new ModelAndView("/teacher/student-panel");
    }


    //---------Search-------------------------------------------------------------------------
    @GetMapping("/teacher/student-panel/search/{id}")
    public ModelAndView showCourseList(@PathVariable("id") int studentId,
                                       @RequestParam(value = "searchKey", required = false) String searchKey,
                                       Model model) {

        User student = userService.findUserById(studentId);
        List<Course> routine = getRoutine(studentId);

//------Searches courses or returns all courses---------------------------------------
        if (searchKey == null) {
            model.addAttribute("courses", courseService.findAllTheoryCourses());
        } else {
            model.addAttribute("courses", courseService.searchTheoryCourses(searchKey));
        }
        model.addAttribute("routine", routine);
        model.addAttribute("student", student);
        return new ModelAndView("/teacher/student-panel");
    }


    //----------Methods-------------------------------------------------------------------------------------------------------------------------------
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
                break;
            }
        }
    }


    private User getUserByEmail(String email) {
        User student = new User();
        student.setEmail(email);
        student = userService.findUserByEmail(student);
        return student;
    }
}
