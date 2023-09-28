package com.example.university_attendance_system_app.Instructor;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.university_attendance_system_app.Adapter.Instructor_ShowCourseAdapter;
import com.example.university_attendance_system_app.FragmentUtils.FragmentUtils;
import com.example.university_attendance_system_app.Model.CourseModel;
import com.example.university_attendance_system_app.Model.Instructor_Notification_Model;
import com.example.university_attendance_system_app.Model.NotificationDataStorage;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.FragmentShowCoursesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Show_Courses_Fragment extends Fragment {

    public Show_Courses_Fragment() {
        // Required empty public constructor
    }

    private FragmentShowCoursesBinding binding;
    private List<CourseModel> courseModelList;
    private Instructor_ShowCourseAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowCoursesBinding.inflate(inflater, container, false);
        courseModelList = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addCourseBtn.setOnClickListener((v -> {
            if (getActivity()!=null) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentUtils.setFragment(manager,R.id.instructor_components_container , new Add_Course_Fragment());
            }
        }));


        initData();
        adapter = new Instructor_ShowCourseAdapter(courseModelList, new Instructor_ShowCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(CourseModel courseModel) {

            }
        });
        binding.showCoursesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.showCoursesRecycler.setAdapter(adapter);
    }

    private void initData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Instructor").child(uid).child("Courses");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        CourseModel courseModel = dataSnapshot.getValue(CourseModel.class);
                        courseModelList.add(courseModel);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}