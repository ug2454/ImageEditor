package com.example.imageeditor;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.imageeditor.EditImageActivity.cropThenRotateBitmap;
import static com.example.imageeditor.EditImageActivity.croppedBitmap;
import static com.example.imageeditor.EditImageActivity.rotateBitmap;
import static com.example.imageeditor.EditImageActivity.rotateThenCropBitmap;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    File cameraImage;
    TextView textView;
   static Bitmap bitmap;
    static String imageFileName;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName  = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        System.out.println(currentPhotoPath);
        return image;
    }


    private void getPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println(ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.imageeditor.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }

    }

//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    private void getPhoto() throws IOException {
//        File photoFile = null;
//        try {
//            photoFile = createImageFile();
//        } catch (IOException ex) {
//            // Error occurred while creating the File
//            System.out.println(ex.getMessage());
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName + ".jpg");
//        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
//        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
//
//        ContentResolver resolver = getContentResolver();
//        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//        OutputStream imageOutStream = null;
//
//        try {
//            if (uri == null) {
//                throw new IOException("Failed to insert MediaStore row");
//            }
//
//            imageOutStream = resolver.openOutputStream(uri);
//
//            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream)) {
//                throw new IOException("Failed to compress bitmap");
//            }
//
//
//            Toast.makeText(this, "Imave Saved", Toast.LENGTH_SHORT).show();
//
//        } finally {
//            if (imageOutStream != null) {
//                imageOutStream.close();
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
////
////            // Continue only if the File was successfully created
//                    if (photoFile != null) {
//                        Uri photoURI = FileProvider.getUriForFile(this,
//                                "com.example.imageeditor.fileprovider",
//                                photoFile);
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(takePictureIntent, 1);
//                    }
//                }
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);
        if(cropThenRotateBitmap !=null){
            imageView.setImageBitmap(cropThenRotateBitmap);
            textView.setText("Edited Image");
            croppedBitmap=null;
            rotateBitmap=null;
            cropThenRotateBitmap=null;
            rotateThenCropBitmap=null;
        }
        else if(rotateThenCropBitmap !=null){
            imageView.setImageBitmap(rotateThenCropBitmap);
            textView.setText("Edited Image");
            croppedBitmap=null;
            rotateBitmap=null;
            cropThenRotateBitmap=null;
            rotateThenCropBitmap=null;

        }
        else if(rotateBitmap !=null){
            imageView.setImageBitmap(rotateBitmap);
            textView.setText("Edited Image");
            croppedBitmap=null;
            rotateBitmap=null;
            cropThenRotateBitmap=null;
            rotateThenCropBitmap=null;
        }
        else if(croppedBitmap!=null){
            imageView.setImageBitmap(croppedBitmap);
            textView.setText("Edited Image");
            croppedBitmap=null;
            rotateBitmap=null;
            cropThenRotateBitmap=null;
            rotateThenCropBitmap=null;
        }

        else{
            imageView.setImageBitmap(bitmap);
            textView.setText("Edited Image");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void clickSelfie(View view) throws IOException {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);

        } else {
            getPhoto();
        }
    }

    public void clickGallery(View view) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Uri uri = data.getData();

        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                bitmap = BitmapFactory.decodeFile(currentPhotoPath);
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                Intent intent = new Intent(this, EditImageActivity.class);
////                imageView.setImageBitmap(bitmap);
////                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                intent.putExtra("image", bitmap);
//                startActivity(intent);
                Intent intent = new Intent(this, EditImageActivity.class);

//                intent.putExtra("image", bitmap);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}