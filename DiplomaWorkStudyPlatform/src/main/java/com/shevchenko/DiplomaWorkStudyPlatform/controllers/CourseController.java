package com.shevchenko.DiplomaWorkStudyPlatform.controllers;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.CourseService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PostMapping("/courses/create")
    public String createCourse(@ModelAttribute Course course, Principal principal, Model model){
        User user = userService.findUserByUsername(principal.getName());
        String courseKey = UUID.randomUUID().toString();
        course.setCourseKey(courseKey);
        course.setUser(user);
        courseService.save(course);
        model.addAttribute("generatedKey", courseKey);

        return "redirect:/teacher_home";
    }





}
