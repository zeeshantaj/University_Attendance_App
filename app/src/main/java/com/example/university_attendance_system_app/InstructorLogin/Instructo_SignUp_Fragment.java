package com.example.university_attendance_system_app.InstructorLogin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.university_attendance_system_app.Instructor.Instructor_Activity;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.FragmentInstructoSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Instructo_SignUp_Fragment extends Fragment {


    public Instructo_SignUp_Fragment() {
        // Required empty public constructor
    }

    private FragmentInstructoSignUpBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Uri selectedImageUri;
    private UploadTask uploadTask;
    private StorageReference imageRef,storageRef;
    private FirebaseStorage storage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInstructoSignUpBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Instructors").child(uid);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

// Create a unique filename for the image
        String imageName = "image_" + uid + ".jpg";
        imageRef = storageRef.child("InstructorImages/"+ imageName);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);
        binding.instructorSingUpBtn.setOnClickListener((view -> {
            String name = binding.instructorSignUpName.getText().toString();
            String email = binding.instructorSignUpEmail.getText().toString();
            String pass = binding.instructorSignUpPass.getText().toString();
            String conPass = binding.instructorSignUpConPass.getText().toString();

            HashMap<String, String> value = new HashMap<>();

            if (!name.isEmpty() && !email.isEmpty()
                    && !pass.isEmpty() && !conPass.isEmpty()){
                if (pass.equals(conPass)){
                    binding.instructorProgressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            uploadTask.addOnSuccessListener(taskSnapshot -> {
                                imageRef.getDownloadUrl().addOnSuccessListener(uir -> {
                                    String image = uir.toString();
                                     value.put("name", name);
                                    value.put("image", image);

                                    reference.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getActivity(), Instructor_Activity.class));
                                            getActivity().finish();
                                            binding.instructorProgressBar.setVisibility(View.GONE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("MyApp", "error" + e.getLocalizedMessage());
                                            binding.instructorProgressBar.setVisibility(View.GONE);
                                        }
                                    });
                                });
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("MyApp", "error" + e.getLocalizedMessage());
                                    Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    binding.instructorProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("MyApp", "error" + e.getLocalizedMessage());
                            Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            binding.instructorProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
                else {
                    binding.instructorProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "password is not same as confirm password", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                binding.instructorProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Field is empty", Toast.LENGTH_SHORT).show();
            }
        }));
        saveImage();
    }

    private void saveImage() {
        binding.profileImageInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imageLauncher.launch(intent);
            }
        });

    }
    private  ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    selectedImageUri = result.getData().getData();
                    binding.profileImageInstructor.setImageURI(selectedImageUri);
                    uploadTask = imageRef.putFile(selectedImageUri);
                }
            });
}