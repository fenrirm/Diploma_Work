package com.shevchenko.DiplomaWorkStudyPlatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tests")
@Getter
@Setter
@NoArgsConstructor
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty(message = "Title cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9_\\s\\-]{1,20}$", message = "Title must contain only letters and spaces and be up to 20 characters long")
    @Column(name = "title")
    private String title;

    @Min(value = 1, message = "Points must be greater than 0")
    @Max(value = 999, message = "Points must be less than 1000")
    @Column(name = "points")
    private int points;

    @NotEmpty(message = "Content cannot be empty")
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonIgnore
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;
}
