package com.nafisulbari.usis.controller;

import com.nafisulbari.usis.entity.Course;
import com.nafisulbari.usis.entity.PasswordRequest;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.service.CourseService;
import com.nafisulbari.usis.service.PasswordRequestService;
import com.nafisulbari.usis.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;


@Controller
public class AdminController {


    private UserService userService;
    private PasswordRequestService passwordRequestService;
    private CourseService courseService;
    private PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, PasswordRequestService passwordRequestService, CourseService courseService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordRequestService = passwordRequestService;
        this.courseService = courseService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/admin/admin-home")
    public String adminHome() {

        return "/admin/admin-home";
    }
//----------------------------USER MANAGEMENT----------------------------------

    @GetMapping("/admin/user-portal")
    public String showUsersPage(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "/admin/user-portal";
    }


    @GetMapping("/admin/add-user")
    public String addUserPage(User theUser) {
        //returning the add user form
        return "admin/add-user";
    }

    @PostMapping("/admin/add-user-account")
    public String addUserAccount(@Valid User user, BindingResult result, Model model) {
        //garbage email checker
        String email = user.getEmail();
        if (!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)" +
                "*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09" +
                "\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]" +
                "*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]" +
                "?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|" +
                "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")) {
            model.addAttribute(user);
            model.addAttribute("garbageEmail", true);
            return "/admin/add-user";
        }
        //garbage mobile checker
        String mobile = user.getMobile();
        if (!mobile.matches("^(\\+8801|8801|01)(\\d){9}")) {
            model.addAttribute(user);
            model.addAttribute("garbageMobile", true);
            return "/admin/add-user";
        }

        try {
            //Checking if user already exists with same email
            User tempUser = userService.findUserByEmail(user);
            if (tempUser != null) {
                model.addAttribute("emailExists", true);
                return "/admin/add-user";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.hasErrors()) {
            return "admin/add-user";
        }
        //encoding password with Bcrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //finally after all checkings we save the user to db
        userService.saveOrUpdateUser(user);

        //after successful operation we will be returned to user-portal page
        model.addAttribute("users", userService.findAllUsers());
        return "admin/user-portal";
    }

    // Fetch the edit user page with user details
    @GetMapping("/admin/edit-user/{id}")
    public String editUserPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "/admin/edit-user";
    }

    // operation of edit page
    @PostMapping("/admin/update-user/{id}")
    public String updateUser(@PathVariable("id") int id, User theUser, BindingResult result, Model model) {

        if (result.hasErrors()) {
            theUser.setId(id);
            return "admin/edit-user";
        }

        //encoding with bcrypt
        theUser.setPassword(passwordEncoder.encode(theUser.getPassword()));

        //updates the user info
        userService.saveOrUpdateUser(theUser);

        model.addAttribute("users", userService.findAllUsers());
        return "admin/edit-user";
    }

    //deletes the user
    @GetMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable("id") int id, User theUser, BindingResult result, Model model) {

        if (result.hasErrors()) {
            throw new RuntimeException("no user found with id :" + id);
        }

        userService.deleteUserById(id);

        model.addAttribute("users", userService.findAllUsers());
        return "admin/user-portal";
    }

    //----------------------------PASSWORD RESET MANAGEMENT----------------------------------
    //fetches the password request page with all password requests
    @GetMapping("/admin/password-request")
    public String passwordRequestPage(Model model) {
        model.addAttribute("passwordrequests", passwordRequestService.findAllPasswordRequest());
        return "/admin/password-request";
    }

    //admin accepts the password request
    @GetMapping("/admin/password-accept/{id}")
    public String passwordAccept(@PathVariable("id") int id, PasswordRequest thePasswordRequest, BindingResult result, Model model) {

        if (result.hasErrors()) {
            throw new RuntimeException("no user found with id :" + id);
        }
        passwordRequestService.acceptByPasswordEmail(id);

        model.addAttribute("passwordrequests", passwordRequestService.findAllPasswordRequest());
        return "admin/password-request";
    }

    //admin rejects the password request
    @GetMapping("/admin/password-reject/{id}")
    public String passwordReject(@PathVariable("id") int id, PasswordRequest thePasswordRequest, BindingResult result, Model model) {

        if (result.hasErrors()) {
            throw new RuntimeException("no user found with id :" + id);
        }
        passwordRequestService.rejectByPasswordId(id);

        model.addAttribute("passwordrequests", passwordRequestService.findAllPasswordRequest());
        return "admin/password-request";
    }


    //----------------------------COURSE MANAGEMENT----------------------------------

    @GetMapping("/admin/course-portal")
    public ModelAndView loginPage(User theUser, BindingResult result, Model theModel) {

        theModel.addAttribute("courses", courseService.findAllCourses());

        return new ModelAndView("/admin/course-portal", String.valueOf(theModel), theUser);
    }


    @GetMapping("/admin/add-course")
    public String addCoursePage(Course theCourse) {

        return "admin/add-course";
    }

    @PostMapping("/admin/add-new-course")
    public ModelAndView addCourse(@Valid Course theCourse, BindingResult result, Model theModel) {

        if(theCourse.getSaturday()==null) theCourse.setSaturday("");
        if(theCourse.getSunday()==null) theCourse.setSunday("");
        if(theCourse.getMonday()==null) theCourse.setMonday("");
        if(theCourse.getTuesday()==null) theCourse.setTuesday("");
        if(theCourse.getWednesday()==null) theCourse.setWednesday("");
        if(theCourse.getThursday()==null) theCourse.setThursday("");

        courseService.saveOrUpdateCourse(theCourse);

        theModel.addAttribute("courses", courseService.findAllCourses());
        return new ModelAndView("/admin/course-portal", String.valueOf(theModel), theCourse);
    }

    @GetMapping("/admin/edit-course/{id}")
    public ModelAndView editCoursePage(@PathVariable("id") int id, Model theModel, Course theCourse) {
        theModel.addAttribute("course", courseService.findCourseById(id));
        return new ModelAndView("/admin/edit-course", String.valueOf(theModel), theCourse);
    }

    @PostMapping("/admin/update-course/{id}")
    public ModelAndView updateCourse(@PathVariable("id") int id, Course theCourse, BindingResult result, Model theModel) {

        if (result.hasErrors()) {
            theCourse.setId(id);
            return new ModelAndView("/admin/edit-course", String.valueOf(theModel), theCourse);
        }

        if(theCourse.getSaturday()==null) theCourse.setSaturday("");
        if(theCourse.getSunday()==null) theCourse.setSunday("");
        if(theCourse.getMonday()==null) theCourse.setMonday("");
        if(theCourse.getTuesday()==null) theCourse.setTuesday("");
        if(theCourse.getWednesday()==null) theCourse.setWednesday("");
        if(theCourse.getThursday()==null) theCourse.setThursday("");

        courseService.saveOrUpdateCourse(theCourse);

        theModel.addAttribute("courses", courseService.findAllCourses());
        return new ModelAndView("/admin/course-portal", String.valueOf(theModel), theCourse);
    }

    @GetMapping("/admin/delete-course/{id}")
    public ModelAndView deleteCourse(@PathVariable("id") int id, Course theCourse, BindingResult result) {

        if (result.hasErrors()) {
            throw new RuntimeException("no course found with id :" + id);
        }

        courseService.deleteCourse(id);

        return new ModelAndView("redirect:/admin/course-portal");
    }

}
