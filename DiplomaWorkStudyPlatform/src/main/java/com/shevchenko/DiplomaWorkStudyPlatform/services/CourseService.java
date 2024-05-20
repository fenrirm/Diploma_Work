package com.shevchenko.DiplomaWorkStudyPlatform.services;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public void save(Course course){
        courseRepository.save(course);
    }

    public List<Course> findCoursesByUserId(int userId){
        return courseRepository.findCoursesByUserIdOrderByIdDesc(userId);
    }

    @Transactional
    public void delete(int id){
        courseRepository.deleteById(id);
    }

    public Course findCourseByCourseId(int courseId){
        return courseRepository.findCourseById(courseId);
    }

    @Transactional
    public void updateCourse(Course course) {
        Course existingCourse = courseRepository.findById(course.getId()).orElse(null);
        if (existingCourse != null) {
            existingCourse.setTitle(course.getTitle());
            existingCourse.setPoints(course.getPoints());
            courseRepository.save(existingCourse);
        } else {
            throw new NullPointerException();
        }
    }

    @Transactional
    public void updateCourseMaterials(Course course) {
        Course existingCourse = courseRepository.findById(course.getId()).orElse(null);
        if (existingCourse != null) {
            existingCourse.setStudyMaterials(course.getStudyMaterials());
            courseRepository.save(existingCourse);
        } else {
            throw new NullPointerException();
        }
    }

    public List<String> getStudyMaterials(int courseId) {
        Course course = courseRepository.findCourseById(courseId);
        if (course != null) {
            String studyMaterials = course.getStudyMaterials();
            if (studyMaterials != null && !studyMaterials.isEmpty()) {
                return Arrays.asList(studyMaterials.split(";"));
            }
        }
        return new ArrayList<>();
    }

    @Transactional
    public void deleteMaterialById(int courseId, int materialId){
        Course course = findCourseByCourseId(courseId);
        List<String> courseMaterials = getStudyMaterials(courseId);
        courseMaterials.remove(materialId);
        course.setStudyMaterials(courseMaterials.toString());
        save(course);
    }


}
