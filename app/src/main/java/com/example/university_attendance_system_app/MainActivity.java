package com.example.university_attendance_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.university_attendance_system_app.FragmentUtils.FragmentUtils;
import com.example.university_attendance_system_app.Instructor.Instructor_Activity;
import com.example.university_attendance_system_app.Login.Login_Fragment;
import com.example.university_attendance_system_app.Student.Student_Activity;
import com.example.university_attendance_system_app.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager manager = getSupportFragmentManager();
        FragmentUtils.setFragment(manager, R.id.fragmentContainer, new Login_Fragment());
        checkUser();
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
                        Toast.makeText(MainActivity.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Instructor_Activity.class);
                        startActivity(intent);
                    }
                    else {
                        DatabaseReference stuRef = FirebaseDatabase.getInstance().getReference("Student");
                        stuRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // User is a passenger
                                    Toast.makeText(MainActivity.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, Student_Activity.class);
                                    startActivity(intent);
                                } else {
                                    // User is neither a driver nor a passenger
                                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}