package com.example.university_attendance_system_app.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.university_attendance_system_app.Adapter.Student_ShowCourseAdapter;
import com.example.university_attendance_system_app.Model.CourseModel;
import com.example.university_attendance_system_app.Model.StudentShowCourseModel;
import com.example.university_attendance_system_app.R;
import com.example.university_attendance_system_app.databinding.FragmentShowCourseStudentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Show_Course_Student_Fragment extends Fragment {


    public Show_Course_Student_Fragment() {
        // Required empty public constructor
    }

    private FragmentShowCourseStudentBinding binding;

    private Student_ShowCourseAdapter adapter;
    private List<StudentShowCourseModel> courseModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowCourseStudentBinding.inflate(inflater, container, false);
        courseModelList = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        CourseModel model = new CourseModel("name", "123", "6Months","6months");
//        CourseModel model1 = new CourseModel("name", "123", "6Months","da");
//        CourseModel model2 = new CourseModel("name", "123", "6Months");
//        CourseModel model3 = new CourseModel("name", "123", "6Months");
//        CourseModel model4 = new CourseModel("name", "123", "6Months");
//
//        courseModelList.add(model);
//        courseModelList.add(model1);
//        courseModelList.add(model2);
//        courseModelList.add(model3);
//        courseModelList.add(model4);

//        StudentShowCourseModel model = new StudentShowCourseModel("zeeshan", "math", "421", "6month");
//        StudentShowCourseModel model1 = new StudentShowCourseModel("zeeshan", "math", "421", "6month");
//        StudentShowCourseModel model2 = new StudentShowCourseModel("zeeshan", "math", "421", "6month");
//        StudentShowCourseModel model3 = new StudentShowCourseModel("zeeshan", "math", "421", "6month");
//        StudentShowCourseModel model4 = new StudentShowCourseModel("zeeshan", "math", "421", "6month");
//
//        courseModelList.add(model);
//        courseModelList.add(model1);
//        courseModelList.add(model2);
//        courseModelList.add(model3);
//        courseModelList.add(model4);

        adapter = new Student_ShowCourseAdapter(courseModelList);
        binding.showCourseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.showCourseRecycler.setAdapter(adapter);
        initData();
    }

    private void initData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Instructor");
        //DatabaseReference coursesRef = reference.child("Courses");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot instructorSnapshot) {
                courseModelList.clear(); // Clear the list before populating it with new data

                for (DataSnapshot instructorData : instructorSnapshot.getChildren()) {
                    // For each instructor, get their courses
                    DataSnapshot coursesSnapshot = instructorData.child("Courses");

                    for (DataSnapshot courseData : coursesSnapshot.getChildren()) {
                        // Retrieve course data
                        String courseId = courseData.getKey();
                        String courseName = courseData.child("CourseName").getValue(String.class);
                        String teacherName = courseData.child("TeacherName").getValue(String.class);
                        String  courseCode= courseData.child("CourseCode").getValue(String.class);
                        String courseDuration = courseData.child("CourseDuration").getValue(String.class);

                        // Create a course model or perform any necessary operations
                        StudentShowCourseModel courseModel = new StudentShowCourseModel();
                        courseModel.setCourseId(courseId);
                        courseModel.setCourseName(courseName);
                        courseModel.setTeacherName(teacherName);
                        courseModel.setCourseCode(courseCode);
                        courseModel.setCourseDuration(courseDuration);

                        // Add the course model to your list
                        courseModelList.add(courseModel);
                    }
                }

                // Notify the adapter of the data change
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}