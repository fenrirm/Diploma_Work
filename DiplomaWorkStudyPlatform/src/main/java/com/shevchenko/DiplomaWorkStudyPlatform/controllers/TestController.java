package com.shevchenko.DiplomaWorkStudyPlatform.controllers;


import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.TestService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Controller
public class TestController {

    private final TestService testService;

    private final UserService userService;

    @Autowired
    public TestController(TestService testService, UserService userService) {
        this.testService = testService;
        this.userService = userService;
    }


    @PostMapping("/saveTest")
    @ResponseBody
    public String saveTest(@RequestBody Map<String, Object> requestData, Principal principal) {
        try {
            String json = (String) requestData.get("json");
            String courseIdStr = (String) requestData.get("courseId");
            Integer courseId = null;
            if (!"null".equals(courseIdStr)) {
                courseId = Integer.parseInt(courseIdStr);
            }
            String title = (String) requestData.get("testName");
            String pointsStr = (String) requestData.get("totalPoints");
            int points = Integer.parseInt(pointsStr);

            User user = userService.findUserByUsername(principal.getName());

            Test test = new Test();
            test.setContent(json);
            if (courseId != null) {
                Course course = new Course();
                course.setId(courseId);
                test.setCourse(course);
            }
            test.setTitle(title);
            test.setPoints(points);
            test.setUser(user);

            testService.save(test);
            return "Test saved successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to save test";
        }
    }



    @PostMapping("/updateTest")
    @ResponseBody
    public String updateTest(@RequestBody Map<String, Object> requestData) {
        try {
            int testId = Integer.parseInt((String) requestData.get("testId"));
            System.out.println(testId);
            String json = (String) requestData.get("json");
            /*String courseIdStr = (String) requestData.get("courseId");
            int courseId = Integer.parseInt(courseIdStr);*/
            String title = (String) requestData.get("testName");
            String pointsStr = (String) requestData.get("totalPoints");
            int points = Integer.parseInt(pointsStr);

            Optional<Test> optionalTest = testService.findTestById(testId);
            if (optionalTest.isEmpty()) {
                return "Test not found";
            }
            Test test = optionalTest.get();
            test.setContent(json);

            /*Course course = new Course();
            course.setId(courseId);
            test.setCourse(course);*/

            test.setTitle(title);
            test.setPoints(points);

            testService.save(test);
            return "Test updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update test";
        }
    }
}
