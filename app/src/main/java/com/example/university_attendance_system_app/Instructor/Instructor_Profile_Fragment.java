package com.example.university_attendance_system_app.Instructor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.os.Handler;
import android.provider.ContactsContract;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.university_attendance_system_app.FragmentUtils.FragmentUtils;
import com.example.university_attendance_system_app.MainActivity;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.FragmentInstructorProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Field;

import de.hdodenhof.circleimageview.CircleImageView;

public class Instructor_Profile_Fragment extends Fragment {

    public Instructor_Profile_Fragment() {
        // Required empty public constructor
    }

    private FragmentInstructorProfileBinding binding;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private String imageUrl,userRole,uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInstructorProfileBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();





        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference nameRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child("Instructor")
                .child(uid);

        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                            Log.e("MyApp", "name"+name);
                            Log.e("MyApp", "name"+imageUrl);
                            loadProfileImage(imageUrl,name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.instructorProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImage();
            }
        });

        binding.signOutBtn.setOnClickListener((v -> {
            auth.signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }));

    }
    private void loadProfileImage(String imageUrl,String name) {

        Glide.with(getActivity())
                .load(imageUrl)
                .error(R.drawable.profile) // Optional error image
                .into(binding.instructorProfileImage);

        binding.instructorProfileName.setText(name);
    }

    private void previewImage() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.image_preview_layout);
        Button button = dialog.findViewById(R.id.reUploadBtn);
        CircleImageView imageView = dialog.findViewById(R.id.previewImage);

        // Set the transition name for the dialog's image view
        ViewCompat.setTransitionName(imageView, "profileImageTransition");


        Glide.with(getActivity())
                .load(imageUrl)
                .error(R.drawable.profile)

                .into(imageView);
        button.setOnClickListener((v -> {
            dialog.dismiss();
        }));

        // Configure the shared element transition
        dialog.setOnShowListener(dialogInterface -> {
            /*TransitionSet transitionSet = new TransitionSet();

            // Set a longer duration (e.g., 2000 milliseconds)
            transitionSet.setDuration(500);

            // Add transition animations
            transitionSet.addTransition(new ChangeBounds());
            transitionSet.addTransition(new ChangeTransform());

            // Apply a custom interpolator to control the speed
            transitionSet.setInterpolator(new DecelerateInterpolator());

            TransitionManager.beginDelayedTransition((ViewGroup) dialog.findViewById(android.R.id.content), transitionSet);
*/

            TransitionSet transitionSet = new TransitionSet();
            transitionSet.addTransition(new ChangeBounds());
            transitionSet.addTransition(new ChangeTransform());
            transitionSet.setInterpolator(new FastOutSlowInInterpolator());

            TransitionManager.beginDelayedTransition((ViewGroup) dialog.findViewById(android.R.id.content), transitionSet);

        });


        dialog.show();
    }
}