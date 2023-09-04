package com.example.university_attendance_system_app.StudentLogin;

import static android.os.Build.VERSION_CODES.R;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.university_attendance_system_app.FragmentUtils.FragmentUtils;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.FragmentStudentLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Student_Login_Fragment extends Fragment {


    public Student_Login_Fragment() {
        // Required empty public constructor
    }


    private FragmentStudentLoginBinding binding;
    private FirebaseAuth auth;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentLoginBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signUpText.setOnClickListener((view1 -> {
            if (getActivity() != null) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentUtils.setFragment(manager, com.example.university_attendance_system_app.R.id.fragmentContainer, new Student_SignUp_Fragment());
            }
        }));
        binding.studentLoginBtn.setOnClickListener((view1 -> {
            String email = binding.studentLoginEmail.getText().toString();
            String password = binding.studentLoginPass.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getActivity(), "SignIn Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {

                Toast.makeText(getActivity(), "field empty", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}