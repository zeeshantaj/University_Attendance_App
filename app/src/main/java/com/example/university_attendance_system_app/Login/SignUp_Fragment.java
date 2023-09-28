package com.example.university_attendance_system_app.Login;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.university_attendance_system_app.FragmentUtils.FragmentUtils;
import com.example.university_attendance_system_app.Instructor.Instructor_Activity;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.Student.Student_Activity;
import com.example.university_attendance_system_app.databinding.FragmentSignUpBinding;
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

public class SignUp_Fragment extends Fragment {


    public SignUp_Fragment() {
        // Required empty public constructor
    }

    private FragmentSignUpBinding binding;
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
        binding = FragmentSignUpBinding.inflate(inflater, container, false);


        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

// Create a unique filename for the image
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);

        binding.loginText.setOnClickListener((v -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentUtils.setFragment(manager,R.id.fragmentContainer,new Login_Fragment());
        }));
        binding.singUpBtn.setOnClickListener((view -> {
            String name = binding.signUpName.getText().toString();
            String email = binding.signUpEmail.getText().toString();
            String pass = binding.signUpPass.getText().toString();
            String conPass = binding.signUpConPass.getText().toString();
            String role = binding.roleSpinner.getSelectedItem().toString();

            HashMap<String, String> value = new HashMap<>();

            if (role.equals("Select your Role")){
                Toast.makeText(getActivity(), "Select Role", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!name.isEmpty() && !email.isEmpty()
                    && !pass.isEmpty() && !conPass.isEmpty()){
                if (pass.equals(conPass)){




                    binding.singUpProgressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {


                            String uid = auth.getUid();
                            String imageName = "image_" + uid + ".jpg";
                            imageRef = storageRef.child("InstructorImages/"+ imageName);

                            if (selectedImageUri!=null) {
                                uploadTask = imageRef.putFile(selectedImageUri);
                            }
                            else {
                                Uri placeholderUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                                        "://" + getResources().getResourcePackageName(R.drawable.profile)
                                        + '/' + getResources().getResourceTypeName(R.drawable.profile)
                                        + '/' + getResources().getResourceEntryName(R.drawable.profile));
                                uploadTask = imageRef.putFile(placeholderUri);

                            }

                            uploadTask.addOnSuccessListener(taskSnapshot -> {
                                imageRef.getDownloadUrl().addOnSuccessListener(uir -> {
                                    String image = uir.toString();
                                     value.put("name", name);
                                    value.put("image", image);
                                    value.put("role", role);
                                    value.put("uid", uid);

                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("userRole", role); // Store the user's role
                                    editor.putString("userName", name);
                                    editor.apply();
                                    reference = FirebaseDatabase.getInstance().getReference("Users").child(role).child(uid);
                                    reference.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show();

                                            if (role.equals("Student")){
                                                startActivity(new Intent(getActivity(), Student_Activity.class));
                                                getActivity().finish();
                                                binding.singUpProgressBar.setVisibility(View.GONE);
                                            }
                                            else if (role.equals("Instructor")){
                                                startActivity(new Intent(getActivity(), Instructor_Activity.class));
                                                getActivity().finish();
                                                binding.singUpProgressBar.setVisibility(View.GONE);

                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("MyApp", "error" + e.getLocalizedMessage());
                                            binding.singUpProgressBar.setVisibility(View.GONE);
                                        }
                                    });
                                });
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("MyApp", "error" + e.getLocalizedMessage());
                                    Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    binding.singUpProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("MyApp", "error" + e.getLocalizedMessage());
                            Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            binding.singUpProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
                else {
                    binding.singUpProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "password is not same as confirm password", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                binding.singUpProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Field is empty", Toast.LENGTH_SHORT).show();
            }
        }));
        saveImage();
    }

    private void saveImage() {
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
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
                    binding.profileImage.setImageURI(selectedImageUri);
                }
            });
}