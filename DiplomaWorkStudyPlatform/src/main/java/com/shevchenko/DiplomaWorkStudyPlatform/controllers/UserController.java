package com.shevchenko.DiplomaWorkStudyPlatform.controllers;
import com.shevchenko.DiplomaWorkStudyPlatform.enums.Role;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.StudentResult;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final TestService testService;

    private final StudentResultService studentResultService;
    @Autowired
    public UserController(UserService userService, UserDetailsService userDetailsService, CourseService courseService, EnrollmentService enrollmentService, TestService testService, StudentResultService studentResultService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.testService = testService;
        this.studentResultService = studentResultService;
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

        List<Integer> courseIds = enrollmentService.getCourseIdsByUserId(user.getId());
        List<Course> studentCourses = courseService.findCoursesByIds(courseIds);

        Map<Integer, Integer> coursePoints = new HashMap<>();

        for (Course course : studentCourses) {
            List<Test> tests = testService.getTestsByCourseId(course.getId());
            int totalPoints = tests.stream().mapToInt(Test::getPoints).sum();
            coursePoints.put(course.getId(), totalPoints);
        }

        Map<Integer, Integer> studentCoursePoints = new HashMap<>();

        for (Course course : studentCourses) {
            List<StudentResult> results = studentResultService.getStudentResultByCourseIdAndUserId(course.getId(), user.getId());
            int totalPoints = results.stream().mapToInt(StudentResult::getMark).sum();
            studentCoursePoints.put(course.getId(), totalPoints);
        }

        List<Test> studentCoursesTests = new ArrayList<>();
        for (Course course : studentCourses) {
            List<Test> tests = testService.findTestsByCourseId(course.getId());
            studentCoursesTests.addAll(tests);
        }

        List<StudentResult> studentResults = studentResultService.getStudentResultByUserId(user.getId());
        List<Integer> studentPassedTests = new ArrayList<>();
        for (StudentResult result : studentResults) {
            studentPassedTests.add(result.getTest().getId());
        }

        studentCoursesTests.removeIf(test -> studentPassedTests.contains(test.getId()));

        List<Integer> studentCoursesIds = enrollmentService.getCourseIdsByUserId(user.getId());

        List<Integer> studentTeachersIds = new ArrayList<>();
        for (Integer courseId : studentCoursesIds) {
            int teacherId = courseService.getUserIdByCourseId(courseId);
            if (teacherId != 0) {
                studentTeachersIds.add(teacherId);
            }
        }


        model.addAttribute("courseCount", studentCourses.size());
        model.addAttribute("passedTests", studentPassedTests.size());
        model.addAttribute("notPassedTests", studentCoursesTests.size());
        model.addAttribute("studentTeachers", studentTeachersIds.size());

        model.addAttribute("studentCourses", studentCourses);
        model.addAttribute("coursePoints", coursePoints);
        model.addAttribute("studentCoursePoints", studentCoursePoints);
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
        List<Test> teacherTests = testService.getTestsByTeacherId(user.getId());

        List<Integer> courseIds = teacherCourses.stream()
                .map(Course::getId)
                .toList();

        List<Integer> studentIds = enrollmentService.getUserIdsByCourseIds(courseIds);

        List<User> teacherStudents = userService.findUsersByIds(studentIds);


        model.addAttribute("courseCount", teacherCourses.size());
        model.addAttribute("testCount", teacherTests.size());
        model.addAttribute("studentCount", teacherStudents.size());
        model.addAttribute("teacherCourses", teacherCourses);
        model.addAttribute("fullName", user.getFullName());

        return "teacher_home_page";
    }



}
