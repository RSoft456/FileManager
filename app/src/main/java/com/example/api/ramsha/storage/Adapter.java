package com.example.api.ramsha.storage;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<StorageItems> list = new ArrayList<>();
    Button update, cancel, ok, cancelFile, writeData;
    EditText updatedtext, writeText;
    Boolean visibility = false;
    TextView name, path, size;
    String extension = null;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        StorageItems paths = list.get(position);
        itemViewHolder VH = (itemViewHolder) holder;
        VH.path.setText(paths.getPath());
        VH.name.setText(paths.getName());
        VH.delete.setImageResource(R.drawable.ic_baseline_more_vert_24);
        if (paths.getIcon().equals("folder"))
            VH.image.setImageResource(R.drawable.ic_folder);
        else if (paths.getIcon().equals("file"))
            VH.image.setImageResource(R.drawable.ic_file);
        else if (paths.getIcon().equals("music"))
            VH.image.setImageResource(R.drawable.ic_music);
        else if (paths.getIcon().equals("text"))
            VH.image.setImageResource(R.drawable.ic_text);
        else if (paths.getIcon().equals("video"))
            VH.image.setImageResource(R.drawable.ic_video);
        else if (paths.getIcon().equals("image"))
            VH.image.setImageResource(R.drawable.ic_image);

        VH.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extension=paths.getPath();
                if (extension.endsWith(".face"))
                    visibility = true;
                else
                    visibility = false;
                //pop-up menu
                PopupMenu popupMenu = new PopupMenu(VH.context, VH.delete);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());

                if (visibility)
                    popupMenu.getMenu().findItem(R.id.write).setVisible(true);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        String item = menuItem.getTitle().toString();
                        //Toast.makeText(VH.context, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        if (item.equals("Delete")) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(VH.context);
                            builder1.setMessage("Delete this item?");
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            list.remove(paths);
                                            notifyItemRemoved(VH.getAdapterPosition());
                                            notifyItemRangeChanged(VH.getAdapterPosition(), list.size());
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        } else if (item.equals("Rename")) {
                            Dialog dialog = new Dialog(VH.context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.dialog_box);

                            update = dialog.findViewById(R.id.updateButton);
                            cancel = dialog.findViewById(R.id.cancelButton);
                            updatedtext = dialog.findViewById(R.id.update);
                            update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String text2 = updatedtext.getText().toString();
                                    StorageItems path2 = new StorageItems("", text2, "");
                                    list.set(VH.getAdapterPosition(), path2);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();


                        } else if (item.equals("Detail")) {
                            Dialog dialog = new Dialog(VH.context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.details_dialog);
                            //file size
                            File file = new File(paths.getPath());
                            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                            name = dialog.findViewById(R.id.fileNameDetails);
                            path = dialog.findViewById(R.id.filePathDetails);
                            size = dialog.findViewById(R.id.fileSizeDetails);
                            name.setText("File name: " + paths.getName());
                            path.setText("File path: " + paths.getPath());
                            size.setText(String.valueOf("File size: " + file_size));
                            ok = dialog.findViewById(R.id.okButtonDetails);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else if (item.equals("Write")) {

                            Dialog dialog = new Dialog(VH.context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.write_data_dialog);

                            writeText = dialog.findViewById(R.id.writeInFile);
                            writeData = dialog.findViewById(R.id.okButtonWrite);
                            cancelFile = dialog.findViewById(R.id.cancelButtonWrite);
                            writeData.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        WriteInFile(writeText.getText().toString().trim());
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


                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();


            }
        });
        VH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File _root = new File(paths.getPath());
                if (_root.isDirectory()) {
                    Intent intent = new Intent(VH.context, BrowseFilesActivity.class);
                    intent.putExtra("ROOTPATH", paths.getPath());
                    VH.context.startActivity(intent);
                } else {
                    Toast.makeText(VH.context, "not a directory", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        VH.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Dialog dialog = new Dialog(VH.context);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(false);
//                dialog.setContentView(R.layout.dialog_box);
//
//                update = dialog.findViewById(R.id.updateButton);
//                cancel = dialog.findViewById(R.id.cancelButton);
//                updatedtext = dialog.findViewById(R.id.update);
//                update.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String text2 = updatedtext.getText().toString();
//                        StorageItems path2 = new StorageItems("", text2, "");
//                        list.set(position, path2);
//                        notifyDataSetChanged();
//                        dialog.dismiss();
//                    }
//                });
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//                return false;
//
//            }
//
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(ArrayList<StorageItems> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class itemViewHolder extends RecyclerView.ViewHolder {
        TextView path, name;
        ImageView image;
        Context context;
        ImageView delete;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            path = itemView.findViewById(R.id.path);
            name = itemView.findViewById(R.id.name);
            delete = itemView.findViewById(R.id.delete);
            image = itemView.findViewById(R.id.imageView);
        }
    }

    public void WriteInFile(String fileText) throws IOException {
        try {
            FileOutputStream stream = new FileOutputStream(extension);
            try {
                stream.write(fileText.getBytes());
            } finally {
                stream.close();
            }
        } catch (Exception e) {

        }
    }
}
