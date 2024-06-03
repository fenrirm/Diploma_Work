package com.shevchenko.DiplomaWorkStudyPlatform.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "students_results")
@Getter
@Setter
@NoArgsConstructor
public class StudentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "mark")
    private int mark;

    @Column(name = "completion_time")
    private Timestamp completionTime;

    public StudentResult(User user, Test test, int courseId, int mark, Timestamp completionTime) {
        this.user = user;
        this.test = test;
        this.courseId = courseId;
        this.mark = mark;
        this.completionTime = completionTime;
    }
}

