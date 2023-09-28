package com.example.university_attendance_system_app.Instructor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.university_attendance_system_app.FragmentUtils.FragmentUtils;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.FragmentAddCourseBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Add_Course_Fragment extends Fragment {

    public Add_Course_Fragment() {
        // Required empty public constructor
    }
    private FragmentAddCourseBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddCourseBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        Log.e("MyApp", "uid" + uid);
        reference= FirebaseDatabase.getInstance().getReference("Users").child("Instructor").child(uid).child("Courses");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", ""); // Provide a default value if necessary

        binding.uploadCourseBtn.setOnClickListener((v -> {
            String name = binding.courseNameEdit.getText().toString();
            String code = binding.courseCodeEdit.getText().toString();
            String duration = binding.courseDurationEdit.getText().toString();
            if (!name.isEmpty() && !code.isEmpty() &&!duration.isEmpty()){
                HashMap<String, String> values = new HashMap<>();

                values.put("CourseName", name);
                values.put("CourseCode", code);
                values.put("CourseDuration", duration);
                values.put("TeacherName", userName);


                long date = System.currentTimeMillis();
                String courseId = String.valueOf(date);
                Log.e("MyApp", "courseId" + courseId);
                reference.child(courseId).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Successfully_Uploaded", Toast.LENGTH_SHORT).show();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentUtils.setFragment(manager,R.id.instructor_components_container,new Show_Courses_Fragment());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                Toast.makeText(getActivity(), "Field is Empty", Toast.LENGTH_SHORT).show();
            }
        }));

    }
}