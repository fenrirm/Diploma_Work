package com.shevchenko.DiplomaWorkStudyPlatform.controllers;

import com.shevchenko.DiplomaWorkStudyPlatform.services.CourseService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;


@Controller
public class StudentController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final CourseService courseService;

    @Autowired
    public StudentController(UserService userService, UserDetailsService userDetailsService, CourseService courseService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.courseService = courseService;
    }



}
