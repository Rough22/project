package com.example.team_rough.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class activity_main extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navView;
    private MediaPlayer tutorialmp, faqmp, contactmp,creditmp;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tutorialmp = MediaPlayer.create(this, R.raw.tts_tutorial);
        faqmp = MediaPlayer.create(this, R.raw.tts_faqs);
        contactmp = MediaPlayer.create(this, R.raw.tts_contact_us);
        creditmp = MediaPlayer.create(this, R.raw.tts_credits);

        navView = findViewById(R.id.navid);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);

                return false;
            }
        });

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean storageResult = StoragePermission.checkPermission(activity_main.this);
                boolean cameraResult = CameraPermission.checkPermission(activity_main.this);

                if(storageResult && cameraResult){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,0);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
        }catch (Exception ex){
            Intent intent = new Intent(activity_main.this, activity_main.class);
            startActivity(intent);
        }

        Bitmap bitmap = (Bitmap)data.getExtras().get("data");

        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/chobi.jpeg";
        File file = new File(fileName);
        Boolean created = false;
        if(!file.exists()) {
            try {
                created = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            created = true;
        }
        if(created) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream.toByteArray();

        Intent intent = new Intent(activity_main.this, activity_ocr.class);
        intent.putExtra("image2", bitmap);
//        intent.putExtra("Image", byteArray);
        startActivity(intent);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.tutorial:
                tutorialmp.start();
                Intent intentt = new Intent(activity_main.this,activity_tutorial.class);
                startActivity(intentt);
                break;
            case R.id.faq:
                faqmp.start();
                Intent intentf = new Intent(activity_main.this,activity_faq.class);
                startActivity(intentf);
                break;
            case R.id.contact:
                contactmp.start();
               Intent intent1 = new Intent(activity_main.this,activity_contact.class);
               startActivity(intent1);
               break;
            case R.id.credit:
                creditmp.start();
                Intent intentc = new Intent(activity_main.this,activity_credit.class);
                startActivity(intentc);
                break;
            case R.id.logout:
                Intent intentlg = new Intent(activity_main.this,activity_login.class);
                startActivity(intentlg);
                activity_main.this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (tutorialmp.isPlaying()) {
            tutorialmp.stop();
            tutorialmp.release();
        }
      
        if (contactmp.isPlaying()) {
            contactmp.stop();
            contactmp.release();
        }
        if (faqmp.isPlaying()) {
            faqmp.stop();
            faqmp.release();
        }
        if (creditmp.isPlaying()) {
            creditmp.stop();
            creditmp.release();
        }
        super.onDestroy();
    }
}