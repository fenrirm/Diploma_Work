package com.shevchenko.DiplomaWorkStudyPlatform.services;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Course;
import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import com.shevchenko.DiplomaWorkStudyPlatform.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TestService {
    private final TestRepository testRepository;

    @Autowired
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public void save(Test test){
        testRepository.save(test);
    }

    public List<Test> findTestsByCourseId(int courseId){
        return testRepository.findTestsByCourseIdOrderByIdDesc(courseId);
    }
}
