package com.shevchenko.DiplomaWorkStudyPlatform.controllers;

import com.shevchenko.DiplomaWorkStudyPlatform.models.*;
import com.shevchenko.DiplomaWorkStudyPlatform.services.*;
import jakarta.persistence.Tuple;
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
import java.util.*;

@Controller
public class TeacherController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final CourseService courseService;
    private final TestService testService;
    private final EnrollmentService enrollmentService;
    private final StudentResultService studentResultService;

    @Autowired
    public TeacherController(UserService userService, UserDetailsService userDetailsService, CourseService courseService, TestService testService, EnrollmentService enrollmentService, StudentResultService studentResultService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.courseService = courseService;
        this.testService = testService;
        this.enrollmentService = enrollmentService;
        this.studentResultService = studentResultService;
    }



    @GetMapping("/teacher_tests")
    public String teacherTestsPage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        List<Test> teacherTests = testService.getTestsByTeacherId(user.getId());
        model.addAttribute("teacherTests", teacherTests);
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        return "teacher_tests_page";
    }

    @GetMapping("/teacher_students")
    public String teacherStudentsPage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());

        List<Course> teacherCourses = courseService.findCoursesByUserId(user.getId());

        List<Integer> teacherStudentsIds = new ArrayList<>();
        for (Course course : teacherCourses) {
            List<Integer> studentIds = enrollmentService.getUserIdsByCourseId(course.getId());
            teacherStudentsIds.addAll(studentIds);
        }

        Set<Integer> uniqueStudentIds = new HashSet<>(teacherStudentsIds);

        List<User> teacherStudents = userService.findUsersByIds(new ArrayList<>(uniqueStudentIds));

        model.addAttribute("teacherStudent", teacherStudents);
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        return "teacher_students_page";
    }

    @GetMapping("/teacher_view_student/{studentId}")
    public String teacherViewStudentPage(@PathVariable int studentId, Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());

        List<Integer> studentCourseIds = enrollmentService.getCourseIdsByUserId(studentId);
        List<Course> studentCourses = courseService.findCoursesByIdsAndTeacherId(studentCourseIds, user.getId());

        List<StudentResultView> courseResults = new ArrayList<>();
        for (Course course : studentCourses) {
            List<StudentResult> results = studentResultService.getStudentResultByCourseIdAndUserId(course.getId(), studentId);
            for (StudentResult result : results) {
                courseResults.add(new StudentResultView(course.getTitle(), result.getTest().getTitle(), result.getMark(), result.getTest().getPoints()));
            }
        }
        User student = userService.findUserById(studentId);

        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("courseResults", courseResults);
        model.addAttribute("student", student);
        return "view_student_page";
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
        List<Test> tests = testService.findTestsByCourseId(id);
        for (Test test : tests) {
            test.setCourse(null);
            testService.save(test);
        }
        courseService.delete(id);
        return "redirect:/teacher_home";
    }
    @DeleteMapping ("/teacher_delete_course_test/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String teacherDeleteCourseTestPage(Model model, Principal principal, @PathVariable("id") int id){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        int courseId = testService.findCourseIdByTestId(id);
        testService.removeTestFromCourseById(id);
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
        List<Test> freeTests = testService.findTestsByTeacherIdAndCourseIsNull(user.getId());
        Course course = courseService.findCourseByCourseId(courseId);
        List<String> studyMaterials = courseService.getStudyMaterials(courseId);
        model.addAttribute("course", course);
        model.addAttribute("studyMaterials", studyMaterials);
        model.addAttribute("courseTests", courseTests);
        model.addAttribute("freeTests", freeTests);
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

    @GetMapping("/teacher_view_test/{testId}")
    public String teacherViewTest(@PathVariable int testId, Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());

        Optional<Test> optionalTest = testService.findTestById(testId);

        if (optionalTest.isEmpty()) {
            System.out.println("Test was not found!");
            model.addAttribute("error", "Test was not found");
            return "error_page";
        }

        Test test = optionalTest.get();

        Course course = null;
        if (test.getCourse() != null && test.getCourse().getId() != null) {
            course = courseService.findCourseByCourseId(test.getCourse().getId());
        }

        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("test", test);
        model.addAttribute("course", course);

        return "view_test_page";
    }



    @GetMapping("/teacher_edit_test/{testId}")
    public String teacherEditTest(@PathVariable int testId, Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());

        Optional<Test> optionalTest = testService.findTestById(testId);

        if (optionalTest.isEmpty()) {
            System.out.println("Test was not found!");
            model.addAttribute("error", "Test was not found");
            return "error_page";
        }

        Test test = optionalTest.get();

        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("test", test);
        return "edit_test_page";
    }

    @DeleteMapping ("/teacher_delete_test/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String teacherDeleteTest(Model model, Principal principal, @PathVariable("id") int id){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("fullName", user.getFullName());
        testService.deleteTestById(id);
        return "redirect:/teacher_tests";
    }
}
