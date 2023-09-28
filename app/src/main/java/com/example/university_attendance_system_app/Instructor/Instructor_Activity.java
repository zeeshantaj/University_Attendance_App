package com.example.university_attendance_system_app.Instructor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.university_attendance_system_app.FragmentUtils.FragmentUtils;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.ActivityInstructorBinding;
import com.google.android.material.navigation.NavigationBarView;

public class Instructor_Activity extends AppCompatActivity {

    private ActivityInstructorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstructorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        FragmentManager manager = getSupportFragmentManager();
        FragmentUtils.setFragment(manager,R.id.instructor_components_container,new Show_Courses_Fragment());

        binding.instructorBottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id==R.id.showCourse){
                    FragmentUtils.setFragment(manager,R.id.instructor_components_container,new Show_Courses_Fragment());
                    return true;
                }
                if (id==R.id.notification){
                    FragmentUtils.setFragment(manager,R.id.instructor_components_container,new Instructor_Notification_Fragment());
                    return true;
                }
                if (id==R.id.profile){
                    FragmentUtils.setFragment(manager,R.id.instructor_components_container,new Instructor_Profile_Fragment());

                    return true;
                }
                return false;
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.instructor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id==R.id.instructor_profile){
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.instructor_setting) {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}