package com.shevchenko.DiplomaWorkStudyPlatform.repositories;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findTestsByCourseIdOrderByIdDesc(int courseId);

    Test findTestsById(int testId);

    List<Test> findTestsByUserIdOrderByIdDesc(int teacherId);

    List<Test> findByUserIdAndCourseIsNullOrderByIdDesc(int teacherId);

    List<Test> findTestsByCourseId(int courseId);



}
