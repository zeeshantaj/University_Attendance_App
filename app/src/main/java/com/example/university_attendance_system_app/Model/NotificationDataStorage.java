package com.example.university_attendance_system_app.Model;

import java.util.ArrayList;
import java.util.List;

public class NotificationDataStorage {
    private static NotificationDataStorage instance;
    private List<Instructor_Notification_Model> notificationModels;

    private NotificationDataStorage() {
        // Initialize the list or perform any other setup
        notificationModels = new ArrayList<>();
    }

    public static synchronized NotificationDataStorage getInstance() {
        if (instance == null) {
            instance = new NotificationDataStorage();
        }
        return instance;
    }

    public List<Instructor_Notification_Model> getNotificationModels() {
        return notificationModels;
    }

    public void setNotificationModels(List<Instructor_Notification_Model> notificationModels) {
        this.notificationModels = notificationModels;
    }
}
