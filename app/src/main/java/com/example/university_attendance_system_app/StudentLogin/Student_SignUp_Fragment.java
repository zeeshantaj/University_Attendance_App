package com.example.university_attendance_system_app.StudentLogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.FragmentStudentSignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class Student_SignUp_Fragment extends Fragment {

    public Student_SignUp_Fragment() {
        // Required empty public constructor
    }


    private FragmentStudentSignUpBinding binding;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentSignUpBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.studentsingUpBtn.setOnClickListener((view1 -> {
            String email = binding.studentSignUpEmail.getText().toString();
            String password = binding.studentSignUpPass.getText().toString();
            String conPass = binding.studentSignUpConPass.getText().toString();
            String name = binding.studentSignUpName.getText().toString();
            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()
            && !conPass.isEmpty()){
                if (password.equals(conPass)){
                    auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getActivity(), "User Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), "Password should be same in password and confirm Password", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getActivity(), "field in empty", Toast.LENGTH_SHORT).show();
            }
        }));

    }
}