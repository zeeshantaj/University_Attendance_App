package com.example.university_attendance_system_app.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.university_attendance_system_app.FragmentUtils.FragmentUtils;
import com.example.university_attendance_system_app.Instructor.Show_Courses_Fragment;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.ActivityStudentBinding;
import com.google.android.material.navigation.NavigationBarView;

public class Student_Activity extends AppCompatActivity {



    private ActivityStudentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentManager manager = getSupportFragmentManager();
        FragmentUtils.setFragment(manager,R.id.student_compenent_container,new Show_Course_Student_Fragment());
        binding.studentBottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id==R.id.showCourse){
                    FragmentUtils.setFragment(manager,R.id.student_compenent_container,new Show_Course_Student_Fragment());
                    return true;
                }
                if (id==R.id.notification){
                    //FragmentUtils.setFragment(manager,R.id.student_compenent_container,new StudentN());
                    return true;
                }
                if (id==R.id.profile){
                    FragmentUtils.setFragment(manager,R.id.student_compenent_container,new Student_Profile_Fragment());
                    return true;
                }
                return false;
            }
        });


    }
}