package com.example.tobiasz.projekt;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class przycinanie extends AppCompatActivity {
    DrawingView dv;
    Bitmap image;
    int height = 0, side = 0, Dwidth, Dheight;
    int width = 0; double stosunek = 0;
    double ret [];
    int [][] imgT;
    int [][] imgT2;
    Paint paint;
    Drawable d;
    RadioGroup rg;
    RadioButton rb1, rb2, rb3, rb4;
    Dialog dia;
    Metoda metods[];
    int jakie [];
    LinearLayout lina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_przycinanie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        image = ((tablicaMetod) this.getApplication()).getBitmap();
        if(((tablicaMetod) this.getApplication()).getOrg()){
            Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Dwidth = display.getWidth();
            Dheight = display.getHeight();
            height = image.getHeight();
            width = image.getWidth();
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
        }
        width=image.getWidth();
        height=image.getHeight();
        imgT = new int[height][width];
        paint = new Paint(0);
        dv = (DrawingView) findViewById(R.id.imageprzycinanie);
        metods = new Metoda[4];
        jakie = new int[4];
        Drawable d = new BitmapDrawable(getResources(), image);
        dv.setBackground(d);
        lina = (LinearLayout) findViewById(R.id.lina);
        lina.getLayoutParams().height = height;
        lina.getLayoutParams().width = width;
        lina.requestLayout();
        dia = new Dialog(this);
        rg = (RadioGroup) findViewById(R.id.rgp);
        rb1 = (RadioButton) rg.findViewById(R.id.prb1);
        rb2 = (RadioButton) rg.findViewById(R.id.prb2);
        rb3 = (RadioButton) rg.findViewById(R.id.prb3);
        rb4 = (RadioButton) rg.findViewById(R.id.prb4);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (rb1.isChecked())
                {
                    dv.setSide(1);
                    dv.clear();
                }else if (rb2.isChecked())
                {
                    dv.setSide(2);
                    dv.clear();
                }else if (rb3.isChecked())
                {
                    dv.setSide(3);
                    dv.clear();
                }else if (rb4.isChecked())
                {
                    dv.setSide(4);
                    dv.clear();
                }
            }
        });
    }

    public void przytnijzdj(View view){
        ret = dv.getValues();
        side = (int) ret[0];
        stosunek = ret[1];
        (new przytnij()).execute();
    }

    private Bitmap setImg(int[][] tab){
        int A=0;
        int[] colors = new int[width*height];
        for(int j=0; j<height; j++){
            for(int k=0; k<width; k++){
                colors[A]=tab[j][k];
                A++;
            }
        }
        return Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
    }

    private class przytnij extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                private Drawable przyc(){
                    imgT=new int[image.getHeight()][image.getWidth()];
                    for(int j = 0;j<image.getHeight();j++){
                        for (int k = 0; k < image.getWidth(); k++) {
                            imgT[j][k] = image.getPixel(k, j);
                        }
                    }
                    side=(int)ret[0];
                    stosunek=ret[1];
                    if(side==1){
                        int tmp = (int) (stosunek * height);
                        if((height-tmp)>9) {
                            imgT2 = new int[(height - tmp)][width];
                            for (int j = (height - 1); j > tmp; j--) {
                                for (int k = 0; k < width; k++) {
                                    imgT2[(j - tmp)][k] = imgT[j][k];
                                }
                            }
                            height -= tmp;
                            image = setImg(imgT2);
                            d = new BitmapDrawable(getResources(), image);
                            metods[0] = new Metoda("Przycinanie góra", stosunek);
                            jakie[0] = 1;
                        }else {
                            dia.setContentView(R.layout.dia_blad);
                            dia.show();
                        }
                    }else if(side==2){
                        height = (int) (stosunek * height);
                        if(height>9) {
                            imgT2 = new int[height][width];
                            for (int j = 0; j < height; j++) {
                                for (int k = 0; k < width; k++) {
                                    imgT2[j][k] = imgT[j][k];
                                }
                            }
                            image = setImg(imgT2);
                            d = new BitmapDrawable(getResources(), image);
                            metods[1] = new Metoda("Przycinanie dół", stosunek);
                            jakie[1] = 1;
                        }else {
                            dia.setContentView(R.layout.dia_blad);
                            dia.show();
                        }
                    }else if(side==3){
                        int tmp = (int) (stosunek * width);
                        if((width-tmp)>9) {
                            imgT2 = new int[height][(width - tmp)];
                            for (int j = 0; j < height; j++) {
                                for (int k = (width - 1); k > tmp; k--) {
                                    imgT2[j][(k - tmp)] = imgT[j][k];
                                }
                            }
                            width -= tmp;
                            image = setImg(imgT2);
                            d = new BitmapDrawable(getResources(), image);
                            metods[2] = new Metoda("Przycinanie lewo", stosunek);
                            jakie[2] = 1;
                        }else {
                            dia.setContentView(R.layout.dia_blad);
                            dia.show();
                        }
                    }else if(side==4){
                        width = (int) (stosunek * width);
                        if(width>9) {
                            imgT2 = new int[height][width];
                            for (int j = 0; j < height; j++) {
                                for (int k = 0; k < width; k++) {
                                    imgT2[j][k] = imgT[j][k];
                                }
                            }
                            image = setImg(imgT2);
                            d = new BitmapDrawable(getResources(), image);
                            metods[3] = new Metoda("Przycinanie prawo", stosunek);
                            jakie[3] = 1;
                        }else {
                            dia.setContentView(R.layout.dia_blad);
                            dia.show();
                        }
                    }
                    return d;
                }
                @Override
                public void run() {
                    dv.post(new Runnable() {
                        public void run() {
                            dv.setBackground(przyc());
                            lina.getLayoutParams().height = height;
                            lina.getLayoutParams().width = width;
                            lina.requestLayout();
                            dv.clear();
                        }
                    });
                }
            }).start();
            return null;
        }
    }

    public void ok (View view){
        dia.dismiss();
    }

    @Override
    protected void onPause() {
        for(int i = 0; i<4; i++){
            if(jakie[i]==1) {
                ((tablicaMetod) this.getApplication()).setTab(metods[i]);
                ((tablicaMetod) this.getApplication()).setW(true);
            }
        }
        super.onPause();
    }

}

