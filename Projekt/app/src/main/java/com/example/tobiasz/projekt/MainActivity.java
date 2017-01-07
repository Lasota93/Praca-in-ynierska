package com.example.tobiasz.projekt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "PermissionDemo";
    private static final int REQUEST_WRITE_STORAGE = 112;
    ImageView im;
    Bitmap image;
    Button bt1, bt2;
    int imgT[][];
    TextView myText;
    Uri selectedImageUri;
    final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/";
    File newdir = new File(dir);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sprawdz();
        newdir.mkdirs();
        im = (ImageView) findViewById(R.id.imagemenu);
        im.setVisibility(View.GONE);
        bt1 = (Button) findViewById(R.id.btnm1);
        bt2 = (Button) findViewById(R.id.btnm2);
        myText = (TextView) findViewById(R.id.textViewmenu );
        myText.setVisibility(View.GONE);
        ((tablicaMetod) this.getApplication()).clear();
        Log.d("Test", "Start aktywności");
    }

    public void ustawienia(View view){
        Intent read_intent = new Intent(MainActivity.this, ustawienia.class);
        startActivity(read_intent);
    }

    public void sprawdz(){

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the SD-CARD is required for this app")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequest();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequest();
            }
        }

        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the CAMERA is required for this app")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequest2();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequest2();
            }
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

    protected void makeRequest2() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                SELECT_PICTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");

                } else {

                    Log.i(TAG, "Permission has been granted by user");

                }
                return;
            }
        }
    }

    private static final int SELECT_PICTURE = 1;
    public void galeria(View arg0) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        Log.d("Test1", "Wybieranie zdjęcia z galerii");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                uriToBitmap(selectedImageUri);
                skaluj();
                im.setImageBitmap(image);
                Log.d("Test1", "Pomyślnie wczytano zdjęcie z galerii");
            }
        }
        if (requestCode == TAKE_PHOTO_CODE) {
            uriToBitmap(selectedImageUri);
            skaluj();
            im.setImageBitmap(image);
            Log.d("Test2", "Pomyślnie wczytano zdjęcie zrobione aparatem");
        }
    }

    private void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void skaluj () {
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        double width = image.getWidth();
        double height = image.getHeight();
        int width2, height2;
        double Dwidth = (display.getWidth()*0.69);
        double Dheight = (display.getHeight()*0.59);
        if(width > height){
            if(width > Dwidth){
                height = (height*((Dwidth*100)/width))/100;
                width = Dwidth;
            }else if(width < Dwidth){
                height = (height*((width*100)/Dwidth))/100;
                width = Dwidth;
            }
        }else if(width < height){
            if(height > Dheight){
                width = (width*((Dheight*100)/height))/100;
                height = Dheight;
            }else if(height < Dheight){
                width = (width*((height*100)/Dheight))/100;
                height = Dheight;
            }
        }else { height = Dwidth; width = Dwidth;}
        width2 = (int) width;
        height2 = (int) height;
        ViewGroup.LayoutParams params1 = bt1.getLayoutParams();
        params1.width = width2;
        bt1.setLayoutParams(params1);
        ViewGroup.LayoutParams params2 = bt2.getLayoutParams();
        params2.width = width2;
        bt1.setLayoutParams(params2);
        image = Bitmap.createScaledBitmap(image, (int)width, (int)height, false);

        imgT = new int[height2][width2];
        for (int j = 0; j < height2; j++) {
            for (int k = 0; k <width2; k++) {
                imgT[j][k] = image.getPixel(k, j);
            }
        }
        int red, green, blue;
        for (int j = 0; j < height2; j++) {
            for (int k = 0; k < 5; k++) {
                red = Color.WHITE;
                green = Color.WHITE;
                blue = Color.WHITE;
                imgT[j][k] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
            }
            for (int k = (width2-5); k < width2; k++) {
                red = Color.WHITE;
                green = Color.WHITE;
                blue = Color.WHITE;
                imgT[j][k] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
            }
        }
        int A=0;
        int[] colors = new int[width2*height2];
        for(int j=0; j<height2; j++){
            for(int k=0; k<width2; k++){
                colors[A]=imgT[j][k];
                A++;
            }
        }
        image = Bitmap.createBitmap(colors, width2, height2, Bitmap.Config.ARGB_8888);
        myText.setVisibility(View.VISIBLE);
        im.setVisibility(View.VISIBLE);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        myText.startAnimation(anim);
    }
    public void start (View view){
        Intent read_intent = new Intent(MainActivity.this, panel.class);
        ((tablicaMetod) this.getApplication()).setUri(selectedImageUri);
        startActivity(read_intent);
    }

    private static final int TAKE_PHOTO_CODE = 2;
    public void capturarFoto(View view) {
        String file = dir+ DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg";


        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {}

        selectedImageUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        Log.d("Test2", "Wykonywanie nowego zdjęcia");
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }
}

