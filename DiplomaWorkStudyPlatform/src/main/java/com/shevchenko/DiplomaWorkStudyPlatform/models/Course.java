package com.shevchenko.DiplomaWorkStudyPlatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    private Integer id;

    @NotEmpty(message = "Title cannot be empty")
    @Pattern(regexp="^[A-Za-z\\s]{1,50}$", message="Course title must contain only letters and spaces, and be up to 50 characters long")
    @Column(name = "title")
    private String title;

    @Column(name = "course_key")
    private String courseKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Size(max = 10000, message = "Study materials must be up to 10000 characters long")
    @Column(name = "study_materials")
    private String studyMaterials;


    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Test> tests;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;


}
