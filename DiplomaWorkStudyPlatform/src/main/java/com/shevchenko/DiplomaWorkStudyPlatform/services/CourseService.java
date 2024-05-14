package com.shevchenko.DiplomaWorkStudyPlatform.services;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
