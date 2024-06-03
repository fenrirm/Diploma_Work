package com.shevchenko.DiplomaWorkStudyPlatform.controllers;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.CourseService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.TestService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Controller
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final TestService testService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService, UserDetailsService userDetailsService, TestService testService) {
        this.courseService = courseService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.testService = testService;
    }

    @PostMapping("/courses/create")
    public String createCourse(@ModelAttribute("course") @Valid Course course, BindingResult bindingResult, Principal principal, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("course", course);
            return "create_course_page";
        }
        User user = userService.findUserByUsername(principal.getName());
        String courseKey = UUID.randomUUID().toString();
        course.setCourseKey(courseKey);
        course.setUser(user);
        courseService.save(course);
        model.addAttribute("generatedKey", courseKey);

        return "redirect:/teacher_home";
    }

    @PostMapping("/material/save")
    public String saveMaterial(@RequestParam("material") String material, @RequestParam("courseId") int courseId, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        Course course = courseService.findCourseByCourseId(courseId);
        if (course != null && course.getUser().getId() == user.getId()) {
            String existingMaterials = course.getStudyMaterials();
            String updatedMaterials = existingMaterials != null ? existingMaterials + ";" + material : material;
            System.out.println(updatedMaterials);
            course.setStudyMaterials(updatedMaterials);
            courseService.updateCourseMaterials(course);
            return "redirect:/teacher_edit_course/" + courseId;
        } else {
            System.out.println("error while saving");
            return "redirect:/error";
        }
    }

    @PostMapping("/teacher_add_test_to_course")
    public String addTestToCourse(@RequestParam int testId, @RequestParam int courseId) {
        Test test = testService.findTestById(testId).orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + testId));
        Course course = courseService.findCourseByCourseId(courseId);
        test.setCourse(course);
        testService.save(test);
        return "redirect:/teacher_edit_course/" + courseId;

    }





}
