package com.example.api.ramsha.storage.createFile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.api.ramsha.storage.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewFile extends AppCompatActivity {

    Button btn,btn2,btn3,btn4;
    EditText editText;
    String folderpath;
    String filepath;
    String text;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file);
        btn=findViewById(R.id.folderbtn);
        editText=findViewById(R.id.ed);
        textView=findViewById(R.id.tv);
        btn2=findViewById(R.id.createfile);

        btn3=findViewById(R.id.rename);
        btn4=findViewById(R.id.delete);
//        getSupportActionBar().hide();
// btn4.setOnClickListener(new View.OnClickListener() {
// @Override
// public void onClick(View view) {
// File file=new File(filepath);
// if (file.exists()){
// file.delete();
// Toast.makeText(FileHandling.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
// }else {
// Toast.makeText(FileHandling.this, "File not deleted", Toast.LENGTH_SHORT).show();
// }
// btn4.setVisibility(View.GONE);
// }
// });
// btn3.setOnClickListener(new View.OnClickListener() {
// @Override
// public void onClick(View view) {
// String newName=editText.getText().toString().trim();
// if (!newName.equals("")){
// renameFile(newName);
// }else {
// Toast.makeText(FileHandling.this, "Give a new name", Toast.LENGTH_SHORT).show();
// }
// btn3.setVisibility(View.GONE);
//
// }
// });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromFile();
                btn4.setVisibility(View.GONE);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName= editText.getText().toString().trim();
                writeInFile(newName);
                Toast.makeText(NewFile.this, "Successfull writeinfile", Toast.LENGTH_SHORT).show();
                btn3.setVisibility(View.GONE);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createfile(text);
                btn2.setVisibility(View.GONE);

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text=editText.getText().toString().trim();
                checkStoragePermission();
                btn.setVisibility(View.GONE);

            }
        });
    }

    private void writeInFile(String content){
        try {
            FileOutputStream stream= new FileOutputStream(filepath);
            try {
                stream.write(content.getBytes());
            }finally {
                stream.close();
            }


        }catch (Exception e){

        }
    }
    private void readFromFile(){
        try {
            FileInputStream inputStream =new FileInputStream(filepath);
            File file=new File(filepath);
            int size=(int) file.length();
            byte[] array= new byte[size];
            try {
                inputStream.read(array);
            }finally {
                inputStream.close();
            }
            String content= new String(array);
            textView.setText(content);

        }catch (Exception e){

        }


    }

    private void renameFile(String newname){
        File file=new File(filepath);
        try {
            if (file.exists()){
                if (file.renameTo(new File(folderpath+"/"+newname+".txt"))){
                    filepath=new File(folderpath+"/"+newname+".txt").getAbsolutePath();
                    Toast.makeText(this, "File rename", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "File not rename", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "File not exit", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void createfile(String fileName){

        filepath= folderpath+"/"+fileName+".txt";
        File file=new File(filepath);
        if (!file.exists()){
            try {
                if (file.createNewFile()){
                    Toast.makeText(this, "File created", Toast.LENGTH_SHORT).show();
                }
            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }


        }else {
            Toast.makeText(this, "File already exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void createFolder(String foldername){
        String roothpath= Environment.getExternalStorageDirectory().getAbsolutePath();
        folderpath=roothpath+"/"+foldername;
        File folder= new File(folderpath);
        if (!folder.exists()){
            if (folder.mkdir()){
                Toast.makeText(this, "Folder Created", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this, "Folder already exist", Toast.LENGTH_SHORT).show();
            }

        }


    }
    private void checkStoragePermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, 123);

        } else {
            createFolder(text);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
                createFolder(text);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
