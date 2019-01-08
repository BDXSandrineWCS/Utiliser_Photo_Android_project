package com.wcs.photo.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.v4.content.FileProvider.getUriForFile;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1234;
    private Uri fileUri = null;
    private ImageView imageView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       init();

    }



    private void dispatchTakePictureIntent() {
        // ouvrir l'application de prise de photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // lors de la validation de la photo
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // déclenche l'appel de onActivityResult
            File photoFile = null;
            try {
                photoFile=createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if ( null != photoFile){
                fileUri = FileProvider.getUriForFile(this, "com.wcs.photo.app.fileprovider",photoFile);
               takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageView.setImageURI(fileUri);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(getString(R.string.date_format)).format(new Date());
        String imgFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imgFileName, ".jpg", storageDir);
        return image;
    }

    private void init(){
        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.buttonPhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }
}
