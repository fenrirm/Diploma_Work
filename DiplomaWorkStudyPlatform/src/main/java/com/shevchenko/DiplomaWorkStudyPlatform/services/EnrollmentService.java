package com.shevchenko.DiplomaWorkStudyPlatform.services;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Enrollment;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public void save(Enrollment enrollment){
        enrollmentRepository.save(enrollment);
    }
    public boolean existsByUserAndCourse(User user, Course course){
        return enrollmentRepository.existsByUserAndCourse(user, course);
    }
    public List<Integer> getCourseIdsByUserId(int userId) {
        return enrollmentRepository.findCourseIdsByUserId(userId);
    }

    @Transactional
    public void deleteEnrollmentByCourseIdAndUserId(int courseId, int userId){
        enrollmentRepository.deleteEnrollmentByCourseIdAndUserId(courseId, userId);
    }

    public List<Integer> getUserIdsByCourseId(int courseId) {
        return enrollmentRepository.findUserIdsByCourseId(courseId);
    }

}
