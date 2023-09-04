package com.example.university_attendance_system_app.InstructorLogin;

import android.content.Intent;
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
import com.example.university_attendance_system_app.Instructor.Instructor_Activity;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.FragmentInstructorLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Instructor_Login_Fragment extends Fragment {


    public Instructor_Login_Fragment() {
        // Required empty public constructor
    }
    FragmentInstructorLoginBinding binding;
    private FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentInstructorLoginBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);

        binding.instructorSignUpText.setOnClickListener((view -> {
            if (getActivity() != null) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentUtils.setFragment(manager,R.id.fragmentContainer,new Instructo_SignUp_Fragment());
            }
        }));

        binding.instructorLoginBtn.setOnClickListener((view -> {
            String email = binding.instructorLoginEmail.getText().toString();
            String password = binding.instructorLoginPass.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()){
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getActivity(), "SingIn Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(),Instructor_Activity.class));
                        getActivity().finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(getActivity(), "Field is empty", Toast.LENGTH_SHORT).show();
            }
        }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}