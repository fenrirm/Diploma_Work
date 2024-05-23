package com.shevchenko.DiplomaWorkStudyPlatform.models;

import com.shevchenko.DiplomaWorkStudyPlatform.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Pattern(regexp="^[a-zA-Z0-9_-]{3,20}$",
            message="Please enter a valid username (3-20 characters, containing only letters, digits, hyphens, and underscores)")
    @Column(name = "username")
    private String username;


    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message="Please enter a valid password (at least 8 characters long, containing at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace)")
    @Column(name = "password")
    private String password;

    @Pattern(regexp="^[A-Z][a-zA-Z]{0,59}\\s[A-Z][a-zA-Z]{0,59}$",
            message="Please enter a valid full name (up to 60 characters for both first and last names, each starting with an uppercase letter)")
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Test> tests;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    public User(String username, String password, String fullName, Role role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

}
