package com.example.api.ramsha.storage.Lab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.api.ramsha.storage.R;

public class Activity_B extends AppCompatActivity {
    EditText name,course;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        name=findViewById(R.id.nameET);
        course=findViewById(R.id.courseET);
        button=findViewById(R.id.buttonB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mName = name.getText().toString();
                String mCourse = course.getText().toString();
                Intent i = new Intent();
                i.putExtra("NAME",mName);
                i.putExtra("COURSE",mCourse);
                setResult(Activity.RESULT_OK,i);
                finish();
            }
        });

    }
}