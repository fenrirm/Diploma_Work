package com.shevchenko.DiplomaWorkStudyPlatform.controllers;
import com.shevchenko.DiplomaWorkStudyPlatform.enums.Role;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.CourseService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final CourseService courseService;

    @Autowired
    public UserController(UserService userService, UserDetailsService userDetailsService, CourseService courseService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.courseService = courseService;
    }


    @GetMapping("/login")
    public String login(Model model, User user){
        model.addAttribute("user", user);
        return "login_page";
    }

    @GetMapping("/register")
    public String registration(@ModelAttribute("user") User user) {
        return "register_page";
    }

    @PostMapping("/register")
    public String registerSave(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam("role") String roleString, Model model){
        User userFromDB = userService.findUserByUsername(user.getUsername());
        if(userFromDB!=null){
            model.addAttribute("userexist", user);
            return "register_page";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "register_page";
        }

        if (roleString.equals("TEACHER")) {
            user.setRole(Role.TEACHER);
            System.out.println(user.getRole());
            userService.save(user);
        } else if (roleString.equals("STUDENT")) {
            user.setRole(Role.STUDENT);
            System.out.println(user.getRole());
            userService.save(user);
        }
        return "redirect:/register?success";
    }


    @GetMapping("/student_home")
    public String studentPage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        return "student_home_page";
    }

    @GetMapping("/teacher_home")
    public String teacherPage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);

        User user = userService.findUserByUsername(principal.getName());
        List<Course> teacherCourses = courseService.findCoursesByUserId(user.getId());
        model.addAttribute("teacherCourses", teacherCourses);

        model.addAttribute("fullName", user.getFullName());

        return "teacher_home_page";
    }



}
