package com.shevchenko.DiplomaWorkStudyPlatform.controllers;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Enrollment;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.CourseService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.EnrollmentService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class EnrollmentController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(UserService userService, UserDetailsService userDetailsService, CourseService courseService, EnrollmentService enrollmentService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/join_course")
    public String joinCourse(@RequestParam("courseKey") String courseKey, RedirectAttributes redirectAttributes, Principal principal) {

        Course course = courseService.findCourseByCourseKey(courseKey);
        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid course key");
            return "redirect:/student_home";
        }

        User user = userService.findUserByUsername(principal.getName());

        if (enrollmentService.existsByUserAndCourse(user, course)) {
            redirectAttributes.addFlashAttribute("error", "You are already enrolled in this course");
            return "redirect:/student_home";
        }

        Enrollment enrollment = new Enrollment(user, course);
        enrollmentService.save(enrollment);

        return "redirect:/student_home";
    }
}
