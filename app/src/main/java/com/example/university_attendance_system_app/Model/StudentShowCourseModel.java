package com.example.university_attendance_system_app.Model;

public class StudentShowCourseModel {

    String TeacherName, CourseName, CourseCode, CourseDuration,courseId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public StudentShowCourseModel(){

    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
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

    public StudentShowCourseModel(String teacherName, String courseName, String courseCode, String courseDuration) {
        TeacherName = teacherName;
        CourseName = courseName;
        CourseCode = courseCode;
        CourseDuration = courseDuration;


    }
}
