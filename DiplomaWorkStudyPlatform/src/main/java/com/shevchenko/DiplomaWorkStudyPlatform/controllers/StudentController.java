package com.shevchenko.DiplomaWorkStudyPlatform.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shevchenko.DiplomaWorkStudyPlatform.models.StudentResult;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;


@Controller
public class StudentController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final CourseService courseService;

    private final EnrollmentService enrollmentService;
    private final TestService testService;

    private final StudentResultService studentResultService;

    @Autowired
    public StudentController(UserService userService, UserDetailsService userDetailsService, CourseService courseService, EnrollmentService enrollmentService, TestService testService, StudentResultService studentResultService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.testService = testService;
        this.studentResultService = studentResultService;
    }

    @GetMapping("/student_view_course/{courseId}")
    public String studentViewCoursePage(@PathVariable int courseId, Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        List<Test> courseTests = testService.findTestsByCourseId(courseId);
        List<String> studyMaterials = courseService.getStudyMaterials(courseId);
        List<Integer> studentPassedTestIds = studentResultService.getTestIdsByCourseIdAndUserId(courseId, user.getId());
        List<Test> studentCoursePassedTests = testService.findTestsByIds(studentPassedTestIds);
        courseTests.removeIf(test -> studentPassedTestIds.contains(test.getId()));
        model.addAttribute("studentCoursePassedTests", studentCoursePassedTests);
        model.addAttribute("studyMaterials", studyMaterials);
        model.addAttribute("courseTests", courseTests);
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        return "view_course_page_student";
    }

    @DeleteMapping("/student_leave_course/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String studentDeleteCoursePage(Model model, Principal principal, @PathVariable("id") int id){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        System.out.println(id);
        System.out.println(user.getId());
        enrollmentService.deleteEnrollmentByCourseIdAndUserId(id, user.getId());
        return "redirect:/teacher_home";
    }

    @GetMapping("/student_tests")
    public String studentTestsPage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        List<StudentResult> studentResults = studentResultService.getStudentResultByUserId(user.getId());

        List<Map<String, Object>> studentTestInfos = new ArrayList<>();
        for (StudentResult result : studentResults) {
            Map<String, Object> testInfo = new HashMap<>();
            testInfo.put("testName", result.getTest().getTitle());
            testInfo.put("courseName", result.getTest().getCourse().getTitle());
            testInfo.put("mark", result.getMark());
            testInfo.put("completionTime", result.getCompletionTime());
            studentTestInfos.add(testInfo);
        }

        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("studentTestInfos", studentTestInfos);
        return "student_results_page";
    }

    @GetMapping("/student_start_test/{testId}")
    public String studentStartTest(@PathVariable int testId, Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());

        Optional<Test> optionalTest = testService.findTestById(testId);

        if (optionalTest.isEmpty()) {
            System.out.println("Test was not found!");
            model.addAttribute("error", "Test was not found");
            return "error_page";
        }

        Test test = optionalTest.get();
        String testContent = test.getContent();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(testContent);
            ArrayNode questionsArray = (ArrayNode) rootNode.path("questions");

            for (JsonNode questionNode : questionsArray) {
                if (questionNode instanceof ObjectNode) {
                    ((ObjectNode) questionNode).remove("correctAnswers");
                }
            }

            testContent = objectMapper.writeValueAsString(rootNode);
            System.out.println(testContent);
            test.setContent(testContent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error processing test content");
            return "error_page";
        }

        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("test", test);
        return "start_test_page";
    }






}
