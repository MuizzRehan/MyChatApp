package com.example.admin.mychatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.FileObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference image_storageReference;
    String current_uid;
    CircleImageView circleImageView;
    TextView user_name;
    TextView user_status;
    Button changeStatusBtn;
    Button changeImage_btn;
    ProgressDialog progressDialog;
    int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        image_storageReference = FirebaseStorage.getInstance().getReference();
        circleImageView = findViewById(R.id.setting_image);
        user_name = findViewById(R.id.setting_name);
        user_status = findViewById(R.id.setting_status);
        changeStatusBtn = findViewById(R.id.setting_changeStatus);
        changeImage_btn = findViewById(R.id.setting_changeImg);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        current_uid = firebaseUser.getUid();

        changeStatusBtn.setOnClickListener(changeStatus);
        changeImage_btn.setOnClickListener(changeImage);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                user_name.setText(name);
                user_status.setText(status);

                if(!image.equals("default"))
                Picasso.get().load(image).placeholder(R.drawable.avatar_image).into(circleImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public View.OnClickListener changeStatus = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent statusActivity = new Intent(SettingActivity.this, StatusActivity.class);
            statusActivity.putExtra("user_status", user_status.getText().toString());
            startActivity(statusActivity);
        }
    };

    public View.OnClickListener changeImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(galleryIntent, Gallery_Pick);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Pick && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressDialog = new ProgressDialog(SettingActivity.this);
                progressDialog.setTitle("Uploading Image");
                progressDialog.setMessage("Please wait while we upload the image.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Uri resultUri = result.getUri();
                File actualImageFile = new File(resultUri.getPath());

                try {
                    final Bitmap thumb_bitmap = new Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .compressToBitmap(actualImageFile);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    final byte[] thumb_bytes = byteArrayOutputStream.toByteArray();


                    StorageReference filePath = image_storageReference.child("profile_images").child(current_uid + ".jpg");
                    final StorageReference thumb_filePath = image_storageReference.child("profile_images").child("thumbs")
                            .child(current_uid + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filePath.putBytes(thumb_bytes);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()){

                                        String thumb_downloadUrl = task.getResult().getDownloadUrl().toString();

                                        Map update_Hashmap = new HashMap();
                                        update_Hashmap.put("image", download_url);
                                        update_Hashmap.put("thumb_image", thumb_downloadUrl);

                                        databaseReference.updateChildren(update_Hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(SettingActivity.this, "Successful Uploading", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SettingActivity.this, "not working", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else{
                            progressDialog.dismiss();
                            Toast.makeText(SettingActivity.this, "not working", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}