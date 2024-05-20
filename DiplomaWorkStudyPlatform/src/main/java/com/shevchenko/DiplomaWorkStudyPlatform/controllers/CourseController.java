package com.shevchenko.DiplomaWorkStudyPlatform.controllers;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.CourseService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    public CourseController(CourseService courseService, UserService userService, UserDetailsService userDetailsService) {
        this.courseService = courseService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
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
            course.setStudyMaterials(updatedMaterials);
            courseService.updateCourseMaterials(course);
            return "redirect:/teacher_edit_course/" + courseId;
        } else {
            return "redirect:/error";
        }
    }



}
