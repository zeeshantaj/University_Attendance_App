package com.example.university_attendance_system_app.Model;

public class CourseModel {
    private String TeacherName;
    private String CourseName;
    private String CourseCode;
    private String CourseDuration;

    public CourseModel() {

    }



    public CourseModel(String courseName, String courseCode, String courseDuration) {
        CourseName = courseName;
        CourseCode = courseCode;
        CourseDuration = courseDuration;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }

    public String getCourseDuration() {
        return CourseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        CourseDuration = courseDuration;
    }
}
