package com.example.api.ramsha.storage.Lab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.api.ramsha.storage.R;

public class Activity_A extends AppCompatActivity {
Button button;
TextView name,course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        button=findViewById(R.id.buttonA);
        name=findViewById(R.id.name);
        course=findViewById(R.id.course);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_A.this,Activity_B.class);
                startActivityForResult(intent,100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==100){
            if(resultCode== Activity.RESULT_OK){
                if(data!=null){
                    String mName = data.getStringExtra("NAME");
                    String mCourse = data.getStringExtra("COURSE");
                    name.setText("Name: "+mName);
                    course.setText("Course: "+mCourse);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}