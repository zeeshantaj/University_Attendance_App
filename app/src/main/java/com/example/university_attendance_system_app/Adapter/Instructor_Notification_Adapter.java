package com.example.university_attendance_system_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.university_attendance_system_app.Model.Instructor_Notification_Model;
import com.example.university_attendance_system_app.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Instructor_Notification_Adapter extends RecyclerView.Adapter<Instructor_Notification_Adapter.ViewHolder> {

    List<Instructor_Notification_Model> list;

    public Instructor_Notification_Adapter(List<Instructor_Notification_Model> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Instructor_Notification_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout_instructor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Instructor_Notification_Adapter.ViewHolder holder, int position) {
        Instructor_Notification_Model model = list.get(position);


        holder.name.setText(model.getName());
        holder.courseName.setText(model.getCourseName());

        Glide.with(holder.itemView.getContext())
                .load(model.getImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageView;
        private TextView name, courseName;
        private Button acceptBtn, rejectBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.notification_name);
            courseName = itemView.findViewById(R.id.notification_courseName);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
            imageView = itemView.findViewById(R.id.notificationImageInstructor);
        }
    }
}
