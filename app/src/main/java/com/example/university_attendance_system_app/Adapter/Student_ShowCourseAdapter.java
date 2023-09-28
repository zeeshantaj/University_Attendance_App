package com.example.university_attendance_system_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university_attendance_system_app.Model.StudentShowCourseModel;
import com.example.university_attendance_system_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Student_ShowCourseAdapter extends RecyclerView.Adapter<Student_ShowCourseAdapter.ViewHolder> {

    private List<StudentShowCourseModel> courseModelList;

    private String CID;
    public Student_ShowCourseAdapter(List<StudentShowCourseModel> courseModelList) {
        this.courseModelList = courseModelList;
    }

    @NonNull
    @Override
    public Student_ShowCourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_layout_for_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Student_ShowCourseAdapter.ViewHolder holder, int position) {
        StudentShowCourseModel model = courseModelList.get(position);
        holder.teacherName.setText(model.getTeacherName());
        holder.name.setText(model.getCourseName());
        holder.code.setText(model.getCourseCode());
        holder.duration.setText(model.getCourseDuration());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        CID = model.getCourseId();

        holder.enroll.setOnClickListener(new View.OnClickListener() {
             String name;
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "data" + model.getCourseId(), Toast.LENGTH_SHORT).show();

                DatabaseReference nameRef = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child("Student")
                        .child(uid);

                SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("EnrolledCourses", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Store the course ID as a key with a boolean value (true means enrolled)
                editor.putBoolean(model.getCourseId(), true);
                editor.apply();

                nameRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            name = snapshot.child("name").getValue(String.class);
                            String imageUrl = snapshot.child("image").getValue(String.class);
                            Toast.makeText(holder.itemView.getContext(), "Name"+name, Toast.LENGTH_SHORT).show();
                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child("Enroll_Requests")
                                    .child(uid)
                                    .child(model.getCourseId());

                            HashMap<String, String> values = new HashMap<>();
                            values.put("courseId", model.getCourseId());
                            values.put("studentUid", uid);
                            values.put("Name", name);
                            values.put("image", imageUrl);
                            values.put("CourseName", model.getCourseName());
                            reference.setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        int p = holder.getAdapterPosition();
                                        if (p != RecyclerView.NO_POSITION){
                                            removeItem(p);
                                        }
                                        Toast.makeText(holder.itemView.getContext(), "Enroll Request sent", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.itemView.getContext(), "Error"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(holder.itemView.getContext(), "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        String courseId = model.getCourseId(); // Get the course ID for the current item
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user's ID

        DatabaseReference enrollRequestsRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child("Enroll_Requests")
                .child(userId) // Navigate to the current user's enrollment requests
                .child(courseId); // Check if there's a request for the current course

        enrollRequestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // The user has sent an enrollment request for this course, so hide the item
                    //holder.itemView.setVisibility(View.GONE);
                    int p = holder.getAdapterPosition();
                    if (p != RecyclerView.NO_POSITION){
                        removeItem(p);
                        // Toast.makeText(holder.itemView.getContext(), "Enroll Request sent", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any database error here
            }
        });

    }


    @Override
    public int getItemCount() {
        return courseModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView teacherName, name, code, duration;
        private Button enroll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacherNameStudent);
            name = itemView.findViewById(R.id.courseNameStudent);
            code = itemView.findViewById(R.id.courseCodeStudent);
            duration = itemView.findViewById(R.id.courseDurationStudent);
            enroll = itemView.findViewById(R.id.enrollBtn);

        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position < courseModelList.size()) {
            courseModelList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
