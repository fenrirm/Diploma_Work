package com.shevchenko.DiplomaWorkStudyPlatform.controllers;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.CourseService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.TestService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TeacherController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final CourseService courseService;
    private final TestService testService;

    @Autowired
    public TeacherController(UserService userService, UserDetailsService userDetailsService, CourseService courseService, TestService testService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.courseService = courseService;
        this.testService = testService;
    }



    @GetMapping("/teacher_tests")
    public String teacherTestsPage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        List<Course> teacherCourses = courseService.findCoursesByUserId(user.getId());
        List<Test> teacherTests = teacherCourses.stream()
                .flatMap(course -> testService.findTestsByCourseId(course.getId()).stream())
                .toList();
        model.addAttribute("teacherTests", teacherTests);
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        return "teacher_tests_page";
    }

    @GetMapping("/teacher_students")
    public String teacherStudentsPage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        return "teacher_students_page";
    }

    @GetMapping("/teacher_create_course")
    public String teacherCreateCoursePage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        return "create_course_page";
    }

    @DeleteMapping ("/teacher_delete_course/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String teacherDeleteCoursePage(Model model, Principal principal, @PathVariable("id") int id){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        courseService.delete(id);
        return "redirect:/teacher_home";
    }

    @GetMapping("/teacher_view_course")
    public String teacherViewCoursePage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        return "view_course_page";
    }

    @GetMapping("/teacher_create_test")
    public String teacherCreateTestPage(Model model, Principal principal){
        System.out.println("IN teacher_create_test");
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        List<Course> courses = courseService.findCoursesByUserId(user.getId());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("courses", courses);
        return "create_test_page";
    }
}
