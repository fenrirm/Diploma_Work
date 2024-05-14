package com.shevchenko.DiplomaWorkStudyPlatform.dao;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseDAO {

    private final EntityManager entityManager;

    @Autowired
    public CourseDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Course> courses(int teacherId){
        return null;
    }
}
