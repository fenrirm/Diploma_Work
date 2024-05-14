package com.shevchenko.DiplomaWorkStudyPlatform.repositories;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findTestsByCourseIdOrderByIdDesc(int courseId);

}
