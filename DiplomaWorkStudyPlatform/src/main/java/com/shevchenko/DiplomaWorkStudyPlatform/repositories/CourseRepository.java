package com.shevchenko.DiplomaWorkStudyPlatform.repositories;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{
    List<Course> findCoursesByUserIdOrderByIdDesc(int userId);
    Course findCourseById(int courseId);

    Course findCourseByCourseKey(String courseKey);

    List<Course> findByIdInAndUserId(List<Integer> courseIds, int teacherId);

    @Query("SELECT c.user.id FROM Course c WHERE c.id = :courseId")
    int getUserIdByCourseId(int courseId);





}
