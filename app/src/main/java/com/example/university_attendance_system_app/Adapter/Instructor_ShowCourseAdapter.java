package com.example.university_attendance_system_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university_attendance_system_app.Model.CourseModel;
import com.example.university_attendance_system_app.Model.Instructor_Notification_Model;
import com.example.university_attendance_system_app.Model.NotificationDataStorage;
import com.example.university_attendance_system_app.R;

import java.util.List;

public class Instructor_ShowCourseAdapter extends RecyclerView.Adapter<Instructor_ShowCourseAdapter.ViewHolder> {
    private List<CourseModel> courseModelList;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClicked(CourseModel courseModel);
    }

    public Instructor_ShowCourseAdapter(List<CourseModel> courseModelList,OnItemClickListener onItemClicked) {
        this.courseModelList = courseModelList;
        this.onItemClickListener=onItemClicked;
    }

    @NonNull
    @Override
    public Instructor_ShowCourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_layout_for_instructor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Instructor_ShowCourseAdapter.ViewHolder holder, int position) {
        CourseModel courseModel = courseModelList.get(position);
        holder.name.setText(courseModel.getCourseName());
        holder.code.setText(courseModel.getCourseCode());
        holder.duration.setText(courseModel.getCourseDuration());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClicked(courseModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,code, duration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.courseName);
            code = itemView.findViewById(R.id.courseCode);
            duration = itemView.findViewById(R.id.courseDuration);
        }
    }
}
