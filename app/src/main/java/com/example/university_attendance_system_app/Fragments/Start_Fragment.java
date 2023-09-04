package com.example.university_attendance_system_app.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.university_attendance_system_app.FragmentUtils.FragmentUtils;
import com.example.university_attendance_system_app.InstructorLogin.Instructor_Login_Fragment;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.StudentLogin.Student_Login_Fragment;
import com.example.university_attendance_system_app.databinding.FragmentStartBinding;

public class Start_Fragment extends Fragment {

    public Start_Fragment() {
        // Required empty public constructor
    }


    private FragmentStartBinding binding;
    private FragmentManager manager;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater, container, false);
        if (getActivity()!=null){
            manager = getActivity().getSupportFragmentManager();
        }
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);
        binding.loginInstructorBtn.setOnClickListener((view -> FragmentUtils.setFragment(manager,R.id.fragmentContainer,new Instructor_Login_Fragment())));
        binding.loginStudentBtn.setOnClickListener((view -> FragmentUtils.setFragment(manager,R.id.fragmentContainer,new Student_Login_Fragment())));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}