package com.shevchenko.DiplomaWorkStudyPlatform.services;

import com.shevchenko.DiplomaWorkStudyPlatform.models.StudentResult;
import com.shevchenko.DiplomaWorkStudyPlatform.repositories.StudentResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentResultService {
    private final StudentResultRepository studentResultRepository;

    @Autowired
    public StudentResultService(StudentResultRepository studentResultRepository) {
        this.studentResultRepository = studentResultRepository;
    }

    @Transactional
    public void save(StudentResult studentResult){
        studentResultRepository.save(studentResult);
    }

    public List<Integer> getTestIdsByCourseIdAndUserId(int courseId, int userId) {
        return studentResultRepository.findTestIdsByCourseIdAndUserId(courseId, userId);
    }

    public List<StudentResult> getStudentResultByUserId(int userId){
        return studentResultRepository.findStudentResultsByUserIdOrderByCompletionTimeDesc(userId);
    }


}
