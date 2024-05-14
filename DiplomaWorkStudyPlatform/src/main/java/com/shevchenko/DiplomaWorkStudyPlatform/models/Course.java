package com.shevchenko.DiplomaWorkStudyPlatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "points")
    private int points;
    @Column(name = "course_key")
    private String courseKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Column(name = "study_materials")
    private String studyMaterials;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Test> tests;


}
