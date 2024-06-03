package com.shevchenko.DiplomaWorkStudyPlatform.models;

public class StudentResultView {
    private final String courseTitle;
    private final String testTitle;
    private final int mark;

    private final int max_mark;

    public StudentResultView(String courseTitle, String testTitle, int mark, int max_mark) {
        this.courseTitle = courseTitle;
        this.testTitle = testTitle;
        this.mark = mark;
        this.max_mark = max_mark;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public int getMark() {
        return mark;
    }

    public int getMaxMark() {
        return max_mark;
    }
}
