package com.example.university_attendance_system_app.Model;

public class Instructor_Notification_Model {
    private String Name, courseId, studentUid, CourseName,image;

    public Instructor_Notification_Model(){

    }

    public Instructor_Notification_Model(String name, String courseId, String studentUid, String courseName, String image) {
        Name = name;
        this.courseId = courseId;
        this.studentUid = studentUid;
        CourseName = courseName;
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentUid() {
        return studentUid;
    }

    public void setStudentUid(String studentUid) {
        this.studentUid = studentUid;
    }
}
