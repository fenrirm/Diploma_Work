package com.shevchenko.DiplomaWorkStudyPlatform.controllers;


import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import com.shevchenko.DiplomaWorkStudyPlatform.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }


    @PostMapping("/saveTest")
    @ResponseBody
    public String saveTest(@RequestBody Map<String, Object> requestData) {
        try {
            String json = (String) requestData.get("json");
            String courseIdStr = (String) requestData.get("courseId");
            int courseId = Integer.parseInt(courseIdStr);
            String title = (String) requestData.get("testName");
            String pointsStr = (String) requestData.get("totalPoints");
            int points = Integer.parseInt(pointsStr);

            Test test = new Test();
            test.setContent(json);
            Course course = new Course();
            course.setId(courseId);
            test.setCourse(course);
            test.setTitle(title);
            test.setPoints(points);

            testService.save(test);
            return "Test saved successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to save test";
        }
    }

}
