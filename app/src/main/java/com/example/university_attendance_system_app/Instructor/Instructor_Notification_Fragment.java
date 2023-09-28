package com.example.university_attendance_system_app.Instructor;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.university_attendance_system_app.Adapter.Instructor_Notification_Adapter;
import com.example.university_attendance_system_app.Model.Instructor_Notification_Model;
import com.example.university_attendance_system_app.Model.NotificationDataStorage;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.FragmentInstructorNotificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Instructor_Notification_Fragment extends Fragment {

    public Instructor_Notification_Fragment() {
        // Required empty public constructor
    }


    private FragmentInstructorNotificationBinding binding;
    private DatabaseReference reference,notificationRef;
    private FirebaseAuth auth;
    private String courseId;

    private List<String> courseIdList = new ArrayList<>();
    private List<Instructor_Notification_Model> list;
    private Instructor_Notification_Adapter adapter;

    private List<Instructor_Notification_Model> newModelList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInstructorNotificationBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Instructor").child(uid).child("Courses");
        notificationRef = FirebaseDatabase.getInstance().getReference("Users").child("Enroll_Requests");
        list = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        courseId = dataSnapshot.getKey();
                        courseIdList.add(courseId);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        notificationRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        for (DataSnapshot courseSnap:dataSnapshot.getChildren()){
                            String courseId2 = courseSnap.getKey();

                            Instructor_Notification_Model model = courseSnap.getValue(Instructor_Notification_Model.class);
                            if (courseIdList.contains(courseId2)) {
                                newModelList.add(model);


                            }
                        }
                    }
                    list.clear();
                    list.addAll(newModelList);
                    //NotificationDataStorage.getInstance().setNotificationModels(list);
                    adapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new Instructor_Notification_Adapter(list);
        binding.notificationRecyclerInstructor.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.notificationRecyclerInstructor.setAdapter(adapter);
    }
}