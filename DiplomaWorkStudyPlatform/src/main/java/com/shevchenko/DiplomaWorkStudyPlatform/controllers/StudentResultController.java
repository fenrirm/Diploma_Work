package com.shevchenko.DiplomaWorkStudyPlatform.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shevchenko.DiplomaWorkStudyPlatform.models.StudentResult;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.StudentResultService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.TestService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class StudentResultController {
    private final UserService userService;
    private final TestService testService;
    private final StudentResultService studentResultService;

    @Autowired
    public StudentResultController(UserService userService, TestService testService, StudentResultService studentResultService) {
        this.userService = userService;
        this.testService = testService;
        this.studentResultService = studentResultService;
    }

    @PostMapping("/submitTest")
    @ResponseBody
    public String submitTest(@RequestBody Map<String, Object> requestData, Principal principal) {
        try {
            String submittedTestAnswersJson = (String) requestData.get("json");
            String testIdStr = (String) requestData.get("testId");
            Integer testId = null;
            if (!"null".equals(testIdStr)) {
                testId = Integer.parseInt(testIdStr);
            }

            Optional<Test> optionalTest;
            if (testId != null) {
                optionalTest = testService.findTestById(testId);
            } else {
                System.out.println("Test ID is null!");
                return "Failed to save test";
            }

            Test testFromDB = optionalTest.orElse(new Test());
            String testFromDBContent = testFromDB.getContent();

            System.out.println("submitted test answers - " + submittedTestAnswersJson);
            System.out.println("test from db content - " + testFromDBContent);

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> submittedTestAnswers = objectMapper.readValue(submittedTestAnswersJson, new TypeReference<List<Map<String, Object>>>() {});
            Map<String, Object> testFromDBMap = objectMapper.readValue(testFromDBContent, new TypeReference<Map<String, Object>>() {});

            List<Map<String, Object>> questionsFromDB = (List<Map<String, Object>>) testFromDBMap.get("questions");

            int totalTestPoints = 0;
            int studentPoints = 0;

            for (Map<String, Object> dbQuestion : questionsFromDB) {
                int questionID = (int) dbQuestion.get("questionID");
                String questionType = (String) dbQuestion.get("questionType");
                int difficultyLevel = Integer.parseInt((String) dbQuestion.get("difficultyLevel"));

                totalTestPoints += difficultyLevel;

                for (Map<String, Object> studentAnswer : submittedTestAnswers) {
                    if ((int) studentAnswer.get("questionID") == questionID) {
                        List<Object> studentCorrectAnswers = (List<Object>) studentAnswer.get("correctAnswers");

                        if (questionType.equals("word")) {
                            List<String> dbAnswers = (List<String>) dbQuestion.get("answers");
                            if (studentCorrectAnswers.size() == 1 && dbAnswers.size() == 1 &&
                                    studentCorrectAnswers.get(0).equals(dbAnswers.get(0))) {
                                studentPoints += difficultyLevel;
                            }
                        } else {
                            List<Object> dbCorrectAnswers = (List<Object>) dbQuestion.get("correctAnswers");
                            if (studentCorrectAnswers.equals(dbCorrectAnswers)) {
                                studentPoints += difficultyLevel;
                            }
                        }
                        break;
                    }
                }
            }

            int testMark = testFromDB.getPoints();

            int testPoint = testMark / totalTestPoints;

            int studentMark = studentPoints * testPoint;

            Timestamp completionTime = new Timestamp(System.currentTimeMillis());

            User user = userService.findUserByUsername(principal.getName());

            int courseId = testFromDB.getCourse().getId();

            StudentResult studentResult = new StudentResult(user, testFromDB, courseId, studentMark, completionTime);
            studentResultService.save(studentResult);

            return "Test saved successfully with mark: " + studentMark;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to save test";
        }
    }

}
