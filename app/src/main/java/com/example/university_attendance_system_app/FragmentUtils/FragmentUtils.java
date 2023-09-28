package com.example.university_attendance_system_app.FragmentUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.university_attendance_system_app.Login.Login_Fragment;

public class FragmentUtils {
    public static void setFragment(FragmentManager fragmentManager, int fragmentContainer, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentContainer, fragment);
        // Only add to back stack if the fragment being added is not the default fragment
        if (!(fragment instanceof Login_Fragment)) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
