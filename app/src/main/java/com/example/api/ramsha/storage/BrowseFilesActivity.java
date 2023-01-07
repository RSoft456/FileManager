package com.example.api.ramsha.storage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import kotlin.io.FileAlreadyExistsException;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BrowseFilesActivity extends AppCompatActivity {
    RecyclerView RV;
    String root, folderPath, filePath;
    Toolbar toolbar;
    Button add;
    TextView textView;
    Button update, cancel, createFile, cancelFile, createFolder, cancelFolder;
    EditText filename, filepath, file, folder;
    ImageView Add;
    Adapter RVadapter = new Adapter();
    ArrayList<StorageItems> pathData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_files);
        RV = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_add_24));
        //Add=findViewById(R.id.add);
        textView = findViewById(R.id.name);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        root = intent.getStringExtra("ROOTPATH");
        checkStoragePermission();
//    Add.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Dialog dialog = new Dialog(BrowseFilesActivity.this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setCancelable(false);
//            dialog.setContentView(R.layout.add_data);
//
//            update = dialog.findViewById(R.id.updateButton);
//            cancel = dialog.findViewById(R.id.cancelButton);
//            filename = dialog.findViewById(R.id.filename);
//            filepath = dialog.findViewById(R.id.filepath);
//
//            update.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String name = filename.getText().toString();
//                    String path = filepath.getText().toString();
//
//                    StorageItems path2 = new StorageItems(name, path, "folder");
//                    pathData.add(path2);
//                    RV.setAdapter(RVadapter);
//                    RV.setLayoutManager(new LinearLayoutManager(RV.getContext()));
//                    RVadapter.setData(pathData);
//                    dialog.dismiss();
//                }
//            });
//            cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//        }
//    });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == R.id.createFile) {
            Dialog dialog = new Dialog(BrowseFilesActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.create_file_dialog);

            createFile = dialog.findViewById(R.id.okButtonFile);
            cancelFile = dialog.findViewById(R.id.cancelButtonFile);
            file = dialog.findViewById(R.id.fileName);
            createFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        CreateFile(file.getText().toString().trim());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });
            cancelFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        } else if (menuItemId == R.id.createFolder) {
            Dialog dialog = new Dialog(BrowseFilesActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.create_folder_dialog);

            createFolder = dialog.findViewById(R.id.okButtonFolder);
            cancelFolder = dialog.findViewById(R.id.cancelButtonFolder);
            folder = dialog.findViewById(R.id.folderName);
            createFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    CreateFolder(folder.getText().toString().trim());

                    dialog.dismiss();
                }
            });
            cancelFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void CreateFolder(String folderName) {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        folderPath = rootPath + "/" + folderName;

        File folder = new File(folderPath);
        if (!folder.exists()) {
            if (folder.mkdir())
                Toast.makeText(this, "Folder Created Successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Sorry!not created", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Folder Already Exists", Toast.LENGTH_SHORT).show();
        }
    }

    public void CreateFile(String fileName) throws IOException {

        filePath = folderPath + "/" + fileName + ".txt";

        File file = new File(filePath);
        if (!file.exists()) {
            if (file.createNewFile())
                Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Sorry!File not created", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "File already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, 123);
        } else {
            getAllFilesPath();
        }
    }

    private void getAllFilesPath() {

        File _root = new File(root);
//        textView.setText(_root.getAbsolutePath());
        File[] childFiles = _root.listFiles();
        for (int i = 0; i < childFiles.length; i++) {
            StorageItems obj = new StorageItems("", "", "");
            obj.path = childFiles[i].getAbsolutePath();
            obj.name = childFiles[i].getName();

            if (obj.path.endsWith(".mp3"))
                obj.icon = "music";
            else if (obj.path.endsWith(".mp4"))
                obj.icon = "video";
            else if (obj.path.endsWith(".txt"))
                obj.icon = "text";
            else if (obj.path.endsWith(".png") || obj.path.endsWith(".jpg") || obj.path.endsWith(".jpeg") || obj.path.endsWith(".webp"))
                obj.icon = "image";
            else if (childFiles[i].isFile())
                obj.icon = "file";
            else if (childFiles[i].isDirectory())
                obj.icon = "folder";

            pathData.add(obj);
        }

        RV.setAdapter(RVadapter);
        RV.setLayoutManager(new LinearLayoutManager(RV.getContext()));
        RVadapter.setData(pathData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAllFilesPath();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Request code not same", Toast.LENGTH_SHORT).show();
        }
    }
}