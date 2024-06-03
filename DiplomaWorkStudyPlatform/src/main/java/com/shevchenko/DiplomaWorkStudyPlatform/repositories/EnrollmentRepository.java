package com.shevchenko.DiplomaWorkStudyPlatform.repositories;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Enrollment;
import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    boolean existsByUserAndCourse(User user, Course course);

    @Query("SELECT e.course.id FROM Enrollment e WHERE e.user.id = :userId")
    List<Integer> findCourseIdsByUserId(int userId);

    @Query("SELECT e.user.id FROM Enrollment e WHERE e.course.id = :courseId")
    List<Integer> findUserIdsByCourseId(int courseId);

    @Query("SELECT e.user.id FROM Enrollment e WHERE e.course.id IN :courseIds")
    List<Integer> findUserIdsByCourseIds(@Param("courseIds") List<Integer> courseIds);

    void deleteEnrollmentByCourseIdAndUserId(int courseId, int userId);

}
