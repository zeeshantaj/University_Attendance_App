package com.example.university_attendance_system_app.Login;

import android.content.Intent;
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
import com.example.university_attendance_system_app.Instructor.Instructor_Activity;
import com.example.university_attendance_system_app.MainActivity;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.Student.Student_Activity;
import com.example.university_attendance_system_app.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Fragment extends Fragment {


    public Login_Fragment() {
        // Required empty public constructor
    }
    FragmentLoginBinding binding;
    private FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentLoginBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);

        binding.signUpText.setOnClickListener((view -> {
            if (getActivity() != null) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentUtils.setFragment(manager,R.id.fragmentContainer,new SignUp_Fragment());
            }
        }));

        binding.loginBtn.setOnClickListener((view -> {
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPass.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()){
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkUser();
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
        checkUser();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void checkUser() {
//        FirebaseAuth auth1 = FirebaseAuth.getInstance();
//        String uid = auth1.getCurrentUser().getUid();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String role = snapshot.child("role").getValue(String.class);
//
//                    Log.e("MyApp", "role: " + role);
//
//                    if ("Instructor".equals(role)) {
//                        startActivity(new Intent(getActivity(), Instructor_Activity.class));
//                        Toast.makeText(getActivity(), "Instructor", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getActivity(), "SingIn Successfully", Toast.LENGTH_SHORT).show();
//                    } else if ("Student".equals(role)) {
//                        startActivity(new Intent(getActivity(), Student_Activity.class));
//                        Toast.makeText(getActivity(), "SingIn Successfully", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getActivity(), "Student", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            String uid = user.getUid();
            DatabaseReference insRef = FirebaseDatabase.getInstance().getReference("Users").child("Instructor");
            insRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Toast.makeText(getActivity(), "SignIn Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), Instructor_Activity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else {
                        DatabaseReference stuRef = FirebaseDatabase.getInstance().getReference("Users").child("Student");
                        stuRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // User is a passenger
                                    Toast.makeText(getActivity(), "SignIn Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), Student_Activity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    // User is neither a driver nor a passenger
                                    Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}