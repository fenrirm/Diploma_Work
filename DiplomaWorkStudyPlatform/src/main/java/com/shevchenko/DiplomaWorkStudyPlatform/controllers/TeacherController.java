package com.shevchenko.DiplomaWorkStudyPlatform.controllers;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.services.CourseService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.TestService;
import com.shevchenko.DiplomaWorkStudyPlatform.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("course", new Course());
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
    @DeleteMapping ("/teacher_delete_test/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String teacherDeleteCourseTestPage(Model model, Principal principal, @PathVariable("id") int id){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        int courseId = testService.findCourseIdByTestId(id);
        testService.deleteTestById(id);
        return "redirect:/teacher_edit_course/"+courseId;
    }

    @DeleteMapping("delete_material/{courseId}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteMaterial(@PathVariable("courseId") int courseId, @PathVariable("id") int materialId, Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        courseService.deleteMaterialById(courseId, materialId);
        return "redirect:/teacher_edit_course/"+courseId;
    }

    @GetMapping("/teacher_view_course/{courseId}")
    public String teacherViewCoursePage(@PathVariable int courseId, Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        List<Test> courseTests = testService.findTestsByCourseId(courseId);
        List<String> studyMaterials = courseService.getStudyMaterials(courseId);
        model.addAttribute("studyMaterials", studyMaterials);
        model.addAttribute("courseTests", courseTests);
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        return "view_course_page";
    }

    @GetMapping("/teacher_edit_course/{courseId}")
    public String teacherEditCoursePage(@PathVariable int courseId, Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        List<Test> courseTests = testService.findTestsByCourseId(courseId);
        Course course = courseService.findCourseByCourseId(courseId);
        List<String> studyMaterials = courseService.getStudyMaterials(courseId);
        model.addAttribute("course", course);
        model.addAttribute("studyMaterials", studyMaterials);
        model.addAttribute("courseTests", courseTests);
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        return "edit_course_page";
    }

    @PostMapping("/teacher_update_course")
    public String updateCourse(@ModelAttribute("course") @Valid Course course, BindingResult bindingResult, Model model) {
        System.out.println("in update controller");
        if(bindingResult.hasErrors()){
            System.out.println("in if controller, errors: " + bindingResult.getAllErrors());
            model.addAttribute("course", course);
            return "edit_course_page";
        }
        System.out.println("in after if controller");
        courseService.updateCourse(course);
        return "redirect:/teacher_edit_course/" + course.getId();
    }


    @GetMapping("/teacher_create_test")
    public String teacherCreateTestPage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        List<Course> courses = courseService.findCoursesByUserId(user.getId());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("courses", courses);
        return "create_test_page";
    }

    @GetMapping("/teacher_create_test/{courseId}")
    public String teacherCreateTestCourseEditPage(@PathVariable int courseId, Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        Course course = courseService.findCourseByCourseId(courseId);
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("course", course);
        return "create_test_course_edit";
    }
}
