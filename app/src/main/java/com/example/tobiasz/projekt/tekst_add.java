package com.example.tobiasz.projekt;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.larswerkman.holocolorpicker.ColorPicker;

import java.util.UUID;

public class tekst_add extends AppCompatActivity {
    DrawingView2 dv;
    int height, width, Dwidth, Dheight;
    Bitmap image;
    LinearLayout lina;
    EditText et;
    Dialog dia_zapis;
    ColorPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tekst_add);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dv = (DrawingView2) findViewById(R.id.pisanieDV);
        dv.setCon(this);
        image = ((tablicaMetod) this.getApplication()).getBitmap();
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Dwidth = display.getWidth();
        Dheight = display.getHeight();
        height = image.getHeight();
        width = image.getWidth();
        Log.d("Test4", "Start aktywności");
        if (width > height) {
            if (width > Dwidth) {
                height = (height * ((Dwidth * 100) / width)) / 100;
                width = Dwidth;
            } else if (height < Dwidth) {
                height = (height * ((width * 100) / Dwidth)) / 100;
                width = Dwidth;
            }
        } else if (width < height) {
            if (height > Dheight) {
                width = (width * ((Dheight * 100) / height)) / 100;
                height = Dheight;
            } else if (height < Dheight) {
                width = (width * ((height * 100) / height)) / 100;
                height = Dheight;
            }
        } else {
            height = Dwidth;
            height = Dwidth;
        }
        image = Bitmap.createScaledBitmap(image, width, height, false);
        Drawable d = new BitmapDrawable(getResources(), image);
        lina = (LinearLayout) findViewById(R.id.linaPis);
        lina.getLayoutParams().height = height;
        lina.getLayoutParams().width = width;
        lina.requestLayout();
        dv.setBackground(d);
        et = (EditText) findViewById(R.id.editTextP);
        et.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dv.setText(et.getText().toString());
            }
        });
        if(((tablicaMetod) this.getApplication()).getOrg()){
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setMessage("UWAGA\nNa potrzeby dodawania tekstu zdjęcie zostało przeskalowane !");
            ad.show();
        }
    }

    public void colorB(View view){
        dia_zapis = new Dialog(this);
        dia_zapis.setContentView(R.layout.dia_color);
        dia_zapis.show();
    }

    public void colorW(View view){
        picker = (ColorPicker) dia_zapis.findViewById(R.id.picker);
        int color = picker.getColor();
        picker.setOldCenterColor(picker.getColor());
        dv.setColorDV(color);
        dia_zapis.dismiss();
    }

    public void zapisP(View view) {
        dia_zapis = new Dialog(this);
        dia_zapis.setContentView(R.layout.dia_zapis);
        dia_zapis.show();
        (new zapis()).execute();
    }

    private class zapis extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    dv.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), dv.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "drawing");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (imgSaved != null) {
                        dia_zapis.dismiss();
                    }
                    dv.destroyDrawingCache();
                }
            }).start();
            Log.d("Test5", "Zapisywanie powiodło się");
            return null;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onPostExecute(Void aVoid) {}
    }

}
