package com.example.university_attendance_system_app.Student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.university_attendance_system_app.MainActivity;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.FragmentStudentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_Profile_Fragment extends Fragment {


    public Student_Profile_Fragment() {
        // Required empty public constructor
    }

    private FragmentStudentProfileBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String imageUrl,userRole,uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.signOutBtnStudent.setOnClickListener((v -> {
            auth.signOut();

            startActivity(new Intent(getActivity(), MainActivity.class));
        }));
        DatabaseReference nameRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child("Student")
                .child(uid);

        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    userRole = snapshot.child("role").getValue(String.class);
                    reference = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(userRole)
                            .child(uid);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                imageUrl = snapshot.child("image").getValue(String.class);
                                String name = snapshot.child("name").getValue(String.class);
                                loadProfileImage(imageUrl,name);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadProfileImage(String imageUrl,String name) {

        Glide.with(getActivity())
                .load(imageUrl)
                .error(R.drawable.profile) // Optional error image
                .into(binding.studentProfileImage);

        binding.studentProfileName.setText(name);
    }
}