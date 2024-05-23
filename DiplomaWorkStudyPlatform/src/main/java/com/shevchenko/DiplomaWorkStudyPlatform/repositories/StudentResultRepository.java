package com.shevchenko.DiplomaWorkStudyPlatform.repositories;

import com.shevchenko.DiplomaWorkStudyPlatform.models.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentResultRepository extends JpaRepository<StudentResult, Integer> {
    @Query("SELECT sr.test.id FROM StudentResult sr WHERE sr.courseId = :courseId AND sr.user.id = :userId")
    List<Integer> findTestIdsByCourseIdAndUserId(int courseId, int userId);

    @Query("SELECT sr FROM StudentResult sr WHERE sr.user.id = :userId ORDER BY sr.completionTime DESC")
    List<StudentResult> findStudentResultsByUserIdOrderByCompletionTimeDesc(int userId);

    List<StudentResult> findByCourseIdAndUserId(int courseId, int userId);


}
