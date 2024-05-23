package com.shevchenko.DiplomaWorkStudyPlatform.services;

import com.shevchenko.DiplomaWorkStudyPlatform.models.Test;
import com.shevchenko.DiplomaWorkStudyPlatform.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TestService {
    private final TestRepository testRepository;

    @Autowired
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Transactional
    public void save(Test test){
        testRepository.save(test);
    }

    public List<Test> findTestsByCourseId(int courseId){
        return testRepository.findTestsByCourseIdOrderByIdDesc(courseId);
    }

    public int findCourseIdByTestId(int testId){
        Test test = testRepository.findTestsById(testId);
        return test.getCourse().getId();
    }

    @Transactional
    public void deleteTestById(int testId){
        testRepository.deleteById(testId);
    }

    @Transactional
    public void removeTestFromCourseById(int testId){
        Test test = testRepository.findTestsById(testId);
        test.setCourse(null);
        testRepository.save(test);
    }

    public List<Test> getTestsByTeacherId(int teacherId){
        return testRepository.findTestsByUserIdOrderByIdDesc(teacherId);
    }

    public List<Test> getAllTests(){
        return testRepository.findAll();
    }

    public Optional<Test> findTestById(int testId){
        return testRepository.findById(testId);
    }


    public List<Test> findTestsByTeacherIdAndCourseIsNull(int teacherId) {
        return testRepository.findByUserIdAndCourseIsNullOrderByIdDesc(teacherId);
    }

    public List<Test> getTestsByCourseId(int courseId){
        return testRepository.findTestsByCourseId(courseId);
    }

    public List<Test> findTestsByIds(List<Integer> ids) {
        return testRepository.findAllById(ids);
    }



}
