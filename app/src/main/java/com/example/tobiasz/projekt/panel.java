package com.example.tobiasz.projekt;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.UUID;

public class panel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView img;
    Uri myUri;
    Bitmap image;
    int[][] imgT;
    int[]metodyDoUsuniecia;
    int i = 0, width, height, max = 100, Dwidth, Dheight;
    Dialog dia_hi, dia_ob, dia_od, dia_odlu, dia_zapis, dia_lista, dia_start, dia_ok, dia_progress, dia_text;
    Button bt1, bt2, bt3;
    CheckBox checkBox;
    LinearLayout scroll, ramka;
    Metoda [] metods;
    boolean oryginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Log.d("Test6", "Start aktywności");
        img = (ImageView) findViewById(R.id.panelimg);
        bt1 = (Button) findViewById(R.id.btnmenu1); bt1.setAlpha(.5f); bt1.setEnabled(false);
        bt2 = (Button) findViewById(R.id.btnmenu2); bt2.setAlpha(.5f); bt2.setEnabled(false);
        bt3 = (Button) findViewById(R.id.btnmenu3); bt3.setAlpha(.5f); bt3.setEnabled(false);
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Dwidth = display.getWidth();
        Dheight = display.getHeight();
        ramka =(LinearLayout) findViewById(R.id.ramka);
        metodyDoUsuniecia = new int[max];
        metods = new Metoda[max];
        myUri = ((tablicaMetod) this.getApplication()).getUri();
        dia_progress = new Dialog(this);
        dia_progress.setContentView(R.layout.dia_progress);
        dia_start = new Dialog(this);
        dia_start.setContentView(R.layout.dia_start);
        if(!(((tablicaMetod) this.getApplication()).getW())) {
            dia_start.show();
            dia_start.setCanceledOnTouchOutside(false);
            dia_ok = new Dialog(this);
            dia_start.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dia_ok.setContentView(R.layout.dia_ok);
                    dia_ok.show();
                    dia_ok.setCanceledOnTouchOutside(false);
                }
            });
            (new start()).execute();
        }
        oryginal = ((tablicaMetod) this.getApplication()).getOrg();
    }

    public void startowanie(View view){
        dia_ok.dismiss();
        ((tablicaMetod) this.getApplication()).setTabOrg(imgT);
        (new wyswietl()).execute();
    }

    private class start extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                ParcelFileDescriptor parcelFileDescriptor =
                        getContentResolver().openFileDescriptor(myUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            width = image.getWidth();
            height = image.getHeight();
            if(!oryginal) {
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
            imgT = new int [height][width];
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    imgT[j][k] = image.getPixel(k, j);
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    img.post(new Runnable() {
                        public void run() {
                            dia_start.dismiss();
                        }
                    });
                }
            }).start();
            return null;
        }
    }

    private class wyswietl extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    img.post(new Runnable() {
                        public void run() {
                            ramka.getLayoutParams().height = height;
                            ramka.getLayoutParams().width = width;
                            ramka.requestLayout();
                            img.setImageBitmap(image);
                            dia_progress.dismiss();
                        }
                    });
                }
            }).start();
            return null;
        }
    }

    private class maluj extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {

                void nakładanie() {
                    if(i == 1){
                        for (int j = 0; j < height; j++) {
                            for (int k = 0; k < width; k++) {
                                imgT[j][k] = image.getPixel(k, j);
                            }
                        }
                    }
                    if (i > 0) {
                        metody((i-1));
                    }
                }

                @Override
                public void run() {
                    img.post(new Runnable() {
                        public void run() {
                            nakładanie();
                            int A=0;
                            int[] colors = new int[width*height];
                            for(int j=0; j<height; j++){
                                for(int k=0; k<width; k++){
                                    colors[A]=imgT[j][k];
                                    A++;
                                }
                            }
                            Log.d("Test6", "Dodawanie przekształcenia");
                            image = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
                            (new wyswietl()).execute();
                        }
                    });
                }
            }).start();
            return null;
        }

        @Override
        protected void onPreExecute() {
            dia_progress.show();
            bt1.setEnabled(true);
            bt1.setAlpha(1);
            bt2.setEnabled(true);
            bt2.setAlpha(1);
            bt3.setEnabled(true);
            bt3.setAlpha(1);
        }
    }

    private class zapis extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), setImg(imgT),
                            UUID.randomUUID().toString() + ".png", "drawing");
                    Log.d("Test6", "Start zapisywania");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (imgSaved != null) {
                        dia_zapis.dismiss();
                        Log.d("Test6", "Zapisywanie powiodło się");
                    }
                }
            }).start();
            return null;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onPostExecute(Void aVoid) {}
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setMessage("Aplikacje stworzył:\nTobiasz Lasociński");
            ad.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_neg) {
            if (i <= (max - 1)) {
                ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.negative)));
                metods = ((tablicaMetod) this.getApplication()).getTab();
                (new maluj()).execute();
                i++;
            } else {
                Toast.makeText(this, R.string.brak, Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_sep) {
            if(i<=(max-1)){
                ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.sepia)));
                metods = ((tablicaMetod) this.getApplication()).getTab();
                (new maluj()).execute();
                i++;
            }else{
                Toast.makeText(this, R.string.brak,Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_sza) {
            if (i <= (max - 1)) {
                ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.gray)));
                metods = ((tablicaMetod) this.getApplication()).getTab();
                (new maluj()).execute();
                i++;
            } else {
                Toast.makeText(this, R.string.brak, Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_his) {
            if (i <= (max - 1)) {
                dia_hi = new Dialog(this);
                dia_hi.setContentView(R.layout.dia_hi);
                dia_hi.show();
            } else {
                Toast.makeText(this, R.string.brak, Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_text) {
            if (i <= (max - 1)) {
                dia_text = new Dialog(this);
                dia_text.setContentView(R.layout.dia_text);
                dia_text.show();
            } else {
                Toast.makeText(this, R.string.brak, Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_obr) {
            dia_ob = new Dialog(this);
            dia_ob.setContentView(R.layout.dia_ob);
            dia_ob.show();
        } else if (id == R.id.nav_odb) {
            dia_od = new Dialog(this);
            dia_od.setContentView(R.layout.dia_od);
            dia_od.show();
        } else if (id == R.id.nav_odl) {
            dia_odlu = new Dialog(this);
            dia_odlu.setContentView(R.layout.dia_odlu);
            dia_odlu.show();
        } else if (id == R.id.nav_fi3x3me) {
            if(i<=(max-1)){
                ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.medianowy)));
                metods = ((tablicaMetod) this.getApplication()).getTab();
                (new maluj()).execute();
                i++;
            }
        } else if (id == R.id.nav_fi3x3us) {
            if(i<=(max-1)){
                ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.usredniajacy)));
                metods = ((tablicaMetod) this.getApplication()).getTab();
                (new maluj()).execute();
                i++;
            }
        }else if (id == R.id.nav_fi3x3la) {
            if(i<=(max-1)){
                ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.laplacea1)));
                metods = ((tablicaMetod) this.getApplication()).getTab();
                (new maluj()).execute();
                i++;
            }
        }else if (id == R.id.nav_fi3x3wy) {
            if(i<=(max-1)){
                ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.wyost)));
                metods = ((tablicaMetod) this.getApplication()).getTab();
                (new maluj()).execute();
                i++;
            }
        }else if (id == R.id.nav_fi3x3uw) {
            if(i<=(max-1)){
                ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.uwyp)));
                metods = ((tablicaMetod) this.getApplication()).getTab();
                (new maluj()).execute();
                i++;
            }
        } else if (id == R.id.nav_prz) {
            ((tablicaMetod) this.getApplication()).setImage(image);
            Intent prztnijg = new Intent(panel.this, przycinanie.class);
            startActivity(prztnijg);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void wyHi(View view){
        int[] hi = new int[3];
        CheckBox ware = (CheckBox) dia_hi.findViewById(R.id.hiBox1);
        CheckBox wagr = (CheckBox) dia_hi.findViewById(R.id.hiBox2);
        CheckBox wabl = (CheckBox) dia_hi.findViewById(R.id.hiBox3);
        try {
            if(ware.isChecked()) {
                hi[0] = 1;
            }else hi[0] = 0;
            if(wagr.isChecked()) {
                hi[1] = 1;
            }else hi[1] = 0;
            if(wabl.isChecked()) {
                hi[2] = 1;
            }else hi[2] = 0;
        }catch (NullPointerException brak){Log.d("ERROR","NULL");}
        ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.histo), hi));
        dia_hi.dismiss();
        metods = ((tablicaMetod) this.getApplication()).getTab();
        (new maluj()).execute();
        i++;
    }

    public void obracaniel(View view){
        dia_ob.dismiss();
        if(i<=(max-1)){
            ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.obrotl)));
            metods = ((tablicaMetod) this.getApplication()).getTab();
            (new maluj()).execute();
            i++;
        }else{
            Toast.makeText(this, R.string.brak,Toast.LENGTH_LONG).show();
        }
    }

    public void obracaniep(View view){
        dia_ob.dismiss();
        if(i<=(max-1)){
            ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.obrotr)));
            metods = ((tablicaMetod) this.getApplication()).getTab();
            (new maluj()).execute();
            i++;
        }else{
            Toast.makeText(this, R.string.brak,Toast.LENGTH_LONG).show();
        }
    }

    public void odbiciel(View view){
        dia_od.dismiss();
        if(i<=(max-1)){
            ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.odbicielp)));
            metods = ((tablicaMetod) this.getApplication()).getTab();
            (new maluj()).execute();
            i++;
        }else{
            Toast.makeText(this, R.string.brak,Toast.LENGTH_LONG).show();
        }
    }

    public void odbicieg(View view){
        dia_od.dismiss();
        if(i<=(max-1)){
            ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.odbiciegd)));
            metods = ((tablicaMetod) this.getApplication()).getTab();
            (new maluj()).execute();
            i++;
        }else{
            Toast.makeText(this, R.string.brak,Toast.LENGTH_LONG).show();
        }
    }

    public void odbicielul(View view){
        dia_odlu.dismiss();
        if(i<=(max-1)){
            ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.odbicielul)));
            metods = ((tablicaMetod) this.getApplication()).getTab();
            (new maluj()).execute();
            i++;
        }else{
            Toast.makeText(this, R.string.brak,Toast.LENGTH_LONG).show();
        }
    }

    public void odbicielug(View view){
        dia_odlu.dismiss();
        if(i<=(max-1)){
            ((tablicaMetod) this.getApplication()).setTab(new Metoda(getString(R.string.odbicielug)));
            metods = ((tablicaMetod) this.getApplication()).getTab();
            (new maluj()).execute();
            i++;
        }else{
            Toast.makeText(this, R.string.brak,Toast.LENGTH_LONG).show();
        }
    }

    public void zapis(View view) {
        dia_zapis = new Dialog(this);
        dia_zapis.setContentView(R.layout.dia_zapis);
        dia_zapis.show();
        metods = ((tablicaMetod) this.getApplication()).getTab();
        (new zapis()).execute();
    }

    public void lista(View view){
            dia_lista = new Dialog(this);
            dia_lista.setContentView(R.layout.dia_lista);
            dia_lista.show();
            scroll = (LinearLayout) dia_lista.findViewById(R.id.lista);
            for (int ii = 0; ii < i; ii++) {
                checkBox = new CheckBox(this);
                checkBox.setId(ii);
                checkBox.setText(((tablicaMetod) this.getApplication()).getTab(ii).getNazwa());
                checkBox.setOnClickListener(coUsunac(checkBox));
                scroll.addView(checkBox);
            }
    }

    public void pisz(View view){
        dia_text.dismiss();
        ((tablicaMetod) this.getApplication()).setImage(image);
        Intent pisanie = new Intent(panel.this, tekst_add.class);
        startActivity(pisanie);
    }

    View.OnClickListener coUsunac(final CheckBox ch){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ch.isChecked()){
                    metodyDoUsuniecia[ch.getId()]=1;
                }else{
                    metodyDoUsuniecia[ch.getId()]=0;
                }
            }
        };
    }

    public void wykonanieListy(View view){
        ((tablicaMetod) this.getApplication()).usun(metodyDoUsuniecia);
        i = ((tablicaMetod) this.getApplication()).getI();
        for(int ii = 0; ii < max; ii++) {
            metodyDoUsuniecia[ii]=0;
        }
        if(i<1) {
            bt1 = (Button) findViewById(R.id.btnmenu1);
            bt1.setAlpha(.5f);
            bt1.setEnabled(false);
            bt2 = (Button) findViewById(R.id.btnmenu2);
            bt2.setAlpha(.5f);
            bt2.setEnabled(false);
            bt3 = (Button) findViewById(R.id.btnmenu3);
            bt3.setAlpha(.5f);
            bt3.setEnabled(false);
        }
        metods = ((tablicaMetod) this.getApplication()).getTab();
        int tmpImg[][] = ((tablicaMetod) this.getApplication()).getTabOrg();
        width = tmpImg[0].length;
        height = tmpImg.length;
        imgT = new int[height][width];
        for(int j = 0; j < height; j++){
            for(int jj = 0; jj < width; jj++){
                imgT[j][jj] = tmpImg[j][jj];
            }
        }
        (new malowanie()).execute();
        dia_lista.dismiss();
    }

    public int[][] fil3x3(int[] tabF, int w, int h, int tabImg[][]){
        int tab[][] = new int[h][w];
        int red, green, blue;
        for (int re = 1; re < (w-1); re++) {
            for (int ke = 1; ke < (h-1); ke++) {
                red = ((((tabImg[ke-1][re-1] >> 16) & 0xFF)*tabF[0])
                        +(((tabImg[ke][re-1] >> 16) & 0xFF)*tabF[1])
                        +(((tabImg[ke+1][re-1] >> 16) & 0xFF)*tabF[2])
                        +(((tabImg[ke-1][re] >> 16) & 0xFF)*tabF[3])
                        +(((tabImg[ke][re] >> 16) & 0xFF)*tabF[4])
                        +(((tabImg[ke+1][re] >> 16) & 0xFF)*tabF[5])
                        +(((tabImg[ke-1][re+1] >> 16) & 0xFF)*tabF[6])
                        +(((tabImg[ke][re+1] >> 16) & 0xFF)*tabF[7])
                        +(((tabImg[ke+1][re+1] >> 16) & 0xFF)*tabF[8]));

                green = ((((tabImg[ke-1][re-1] >> 8) & 0xFF)*tabF[0])
                        +(((tabImg[ke][re-1] >> 8) & 0xFF)*tabF[1])
                        +(((tabImg[ke+1][re-1] >> 8) & 0xFF)*tabF[2])
                        +(((tabImg[ke-1][re] >> 8) & 0xFF)*tabF[3])
                        +(((tabImg[ke][re] >> 8) & 0xFF)*tabF[4])
                        +(((tabImg[ke+1][re] >> 8) & 0xFF)*tabF[5])
                        +(((tabImg[ke-1][re+1] >> 8) & 0xFF)*tabF[6])
                        +(((tabImg[ke][re+1] >> 8) & 0xFF)*tabF[7])
                        +(((tabImg[ke+1][re+1] >> 8) & 0xFF)*tabF[8]));

                blue = (((tabImg[ke-1][re-1] & 0xFF)*tabF[0])
                        +((tabImg[ke][re-1] & 0xFF)*tabF[1])
                        +((tabImg[ke+1][re-1] & 0xFF)*tabF[2])
                        +((tabImg[ke-1][re] & 0xFF)*tabF[3])
                        +((tabImg[ke][re] & 0xFF)*tabF[4])
                        +((tabImg[ke+1][re] & 0xFF)*tabF[5])
                        +((tabImg[ke-1][re+1] & 0xFF)*tabF[6])
                        +((tabImg[ke][re+1] & 0xFF)*tabF[7])
                        +((tabImg[ke+1][re+1] & 0xFF)*tabF[8]));

                if(red<0){red = 0;}
                if(red>255){red = 255;}
                if(green<0){green = 0;}
                if(green>255){green = 255;}
                if(blue<0){blue = 0;}
                if(blue>255){blue = 255;}

                tab[ke][re] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
            }
        }
        return tab;
    }

    public void cofnij(View view){
        ((tablicaMetod) this.getApplication()).cofnij();
        --i;
        if(i<1) {
            bt1 = (Button) findViewById(R.id.btnmenu1);
            bt1.setAlpha(.5f);
            bt1.setEnabled(false);
            bt2 = (Button) findViewById(R.id.btnmenu2);
            bt2.setAlpha(.5f);
            bt2.setEnabled(false);
            bt3 = (Button) findViewById(R.id.btnmenu3);
            bt3.setAlpha(.5f);
            bt3.setEnabled(false);
        }
        int tmpImg[][] = ((tablicaMetod) this.getApplication()).getTabOrg();
        width = tmpImg[0].length;
        height = tmpImg.length;
        imgT = new int[height][width];
        for(int j = 0; j < height; j++){
            for(int jj = 0; jj < width; jj++){
                imgT[j][jj] = tmpImg[j][jj];
            }
        }
        (new malowanie()).execute();
    }

    private class malowanie extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new Thread(new Runnable() {

                void nakładanie(int licznik) {
                    if (licznik >= 0) {
                        if (licznik != 0) {
                            nakładanie((licznik - 1));
                        }
                        metody(licznik);
                    }
                }

                @Override
                public void run() {
                    img.post(new Runnable() {
                        public void run() {
                            nakładanie((i-1));
                            int A=0;
                            int[] colors = new int[width*height];
                            for(int j=0; j<height; j++){
                                for(int k=0; k<width; k++){
                                    colors[A]=imgT[j][k];
                                    A++;
                                }
                            }
                            image = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
                            (new wyswietl()).execute();
                        }
                    });
                }
            }).start();
            return null;
        }

        @Override
        protected void onPreExecute() {
            dia_progress.show();
        }
    }

    @Override
    protected void onResume() {
        if(((tablicaMetod) this.getApplication()).getW()) {
            ((tablicaMetod) this.getApplication()).setW(false);
            i = ((tablicaMetod) this.getApplication()).getI();
            metods = ((tablicaMetod) this.getApplication()).getTab();
            int tmpImg[][] = ((tablicaMetod) this.getApplication()).getTabOrg();
            width = tmpImg[0].length;
            height = tmpImg.length;
            imgT = new int[height][width];
            for(int j = 0; j < height; j++){
                for(int jj = 0; jj < width; jj++){
                    imgT[j][jj] = tmpImg[j][jj];
                }
            }
            bt1.setEnabled(true);
            bt1.setAlpha(1);
            bt2.setEnabled(true);
            bt2.setAlpha(1);
            bt3.setEnabled(true);
            bt3.setAlpha(1);
            (new malowanie()).execute();
        }
        super.onResume();
    }

    public void metody(int ktory){
        int red, green, blue;
        double tmp;
        double c1[][] = new double[3][256];
        if (metods[ktory].getNazwa().equals(getString(R.string.negative))) {
            for (int re = 0; re < height; re++) {
                for (int ke = 0; ke < width; ke++) {
                    red = (imgT[re][ke] >> 16) & 0xFF;
                    green = (imgT[re][ke] >> 8) & 0xFF;
                    blue = imgT[re][ke] & 0xFF;
                    red = 255 - red;
                    green = 255 - green;
                    blue = 255 - blue;
                    if (red < 0)
                        red = 0;
                    if (green < 0)
                        green = 0;
                    if (blue < 0)
                        blue = 0;
                    imgT[re][ke] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
                }
            }
        } else if (metods[ktory].getNazwa().equals(getString(R.string.sepia))) {
            int depth = 20;
            for (int re = 0; re < height; re++) {
                for (int ke = 0; ke < width; ke++) {
                    red = (imgT[re][ke] >> 16) & 0xFF;
                    green = (imgT[re][ke] >> 8) & 0xFF;
                    blue = imgT[re][ke] & 0xFF;
                    red = green = blue = (red + green + blue) / 3;
                    red += (depth * 2);
                    green += depth;
                    if (red > 255)
                        red = 255;
                    if (green > 255)
                        green = 255;
                    imgT[re][ke] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
                }
            }
        } else if (metods[ktory].getNazwa().equals(getString(R.string.gray))) {
            for (int re = 0; re < height; re++) {
                for (int ke = 0; ke < width; ke++) {
                    red = (imgT[re][ke] >> 16) & 0xFF;
                    green = (imgT[re][ke] >> 8) & 0xFF;
                    blue = imgT[re][ke] & 0xFF;
                    red = (red + green + blue) / 3;
                    green = red;
                    blue = red;
                    if (red < 0)
                        red = 0;
                    if (green < 0)
                        green = 0;
                    if (blue < 0)
                        blue = 0;
                    imgT[re][ke] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
                }
            }
        } else if (metods[ktory].getNazwa().equals(getString(R.string.histo))) {
            int[] th = metods[ktory].getTab();
            for (int re = 0; re < height; re++) {
                for (int ke = 0; ke < width; ke++) {
                    red = (imgT[re][ke] >> 16) & 0xFF;
                    green = (imgT[re][ke] >> 8) & 0xFF;
                    blue = imgT[re][ke] & 0xFF;
                    c1[0][red] += 1;
                    c1[1][green] += +1;
                    c1[2][blue] += 1;
                }
            }
            tmp = width * height;
            for (int re = 0; re < 3; re++) {
                for (int ke = 0; ke < 256; ke++) {
                    c1[re][ke] /= tmp;
                }
            }
            for (int b = 0; b < 3; b++) {
                for (int z = 1; z < 256; z++) {
                    c1[b][z] = c1[b][z] + c1[b][z - 1];
                }
            }
            for (int re = 0; re < height; re++) {
                for (int ke = 0; ke < width; ke++) {
                    if (th[0] == 1) {
                        red = (int) (c1[0][((imgT[re][ke] >> 16) & 0xFF)] * 255.0);
                    } else red = (imgT[re][ke] >> 16) & 0xFF;
                    if (th[1] == 1) {
                        green = (int) (c1[1][((imgT[re][ke] >> 8) & 0xFF)] * 255.0);
                    } else green = (imgT[re][ke] >> 8) & 0xFF;
                    if (th[2] == 1) {
                        blue = (int) (c1[2][(imgT[re][ke] & 0xFF)] * 255.0);
                    } else blue = imgT[re][ke] & 0xFF;
                    if (red > 255)
                        red = 255;
                    if (green > 255)
                        green = 255;
                    if (blue > 255)
                        blue = 255;
                    imgT[re][ke] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
                }
            }
        } else if (metods[ktory].getNazwa().equals(getString(R.string.obrotl))) {
            int w = width;
            int tabor[][] = new int[width][height];
            for (int re = 0; re < width; re++) {
                --w;
                for (int ke = 0; ke < height; ke++) {
                    tabor[w][ke] = imgT[ke][re];
                }
            }
            int tmpP = width;
            width = height;
            height = tmpP;
            imgT = new int [height][width];
            imgT = tabor;
        } else if (metods[ktory].getNazwa().equals(getString(R.string.obrotr))) {
            int h = height;
            int tabor[][] = new int[width][height];
            for (int ke = 0; ke < height; ke++) {
                --h;
                for (int re = 0; re < width; re++) {
                    tabor[re][h] = imgT[ke][re];
                }
            }
            int tmpP = width;
            width = height;
            height = tmpP;
            imgT = new int [height][width];
            imgT = tabor;
        } else if (metods[ktory].getNazwa().equals(getString(R.string.odbicielp))) {
            int tablp[][] = new int[height][width];
            int w = width;
            for (int re = 0; re < width; re++) {
                w--;
                for (int ke = 0; ke < height; ke++) {
                    tablp[ke][w] = imgT[ke][re];
                }
            }
            imgT = tablp;
        } else if (metods[ktory].getNazwa().equals(getString(R.string.odbiciegd))) {
            int tabgd[][] = new int[height][width];
            int h = height;
            for (int re = 0; re < height; re++) {
                h--;
                for (int ke = 0; ke < width; ke++) {
                    tabgd[h][ke] = imgT[re][ke];
                }
            }
            imgT = tabgd;
        } else if (metods[ktory].getNazwa().equals(getString(R.string.odbicielul))) {
            int w = width;
            for (int re = 0; re < width; re++) {
                w--;
                for (int ke = 0; ke < height; ke++) {
                    imgT[ke][w] = imgT[ke][re];
                }
            }
        } else if (metods[ktory].getNazwa().equals(getString(R.string.odbicielug))) {
            int h = height;
            for (int re = 0; re < height; re++) {
                h--;
                for (int ke = 0; ke < width; ke++) {
                    imgT[h][ke] = imgT[re][ke];
                }
            }
        } else if (metods[ktory].getNazwa().equals(getString(R.string.usredniajacy))) {
            int[][] tab = new int[height][width];
            for (int re = 0; re < height; re++) {
                for (int ke = 0; ke < width; ke++) {
                    tab[re][ke] = imgT[re][ke];
                }
            }
            imgT = tab;
            for (int re = 1; re < (width - 1); re++) {
                for (int ke = 1; ke < (height - 1); ke++) {
                    red = (((imgT[ke - 1][re - 1] >> 16) & 0xFF) + ((imgT[ke][re - 1] >> 16) & 0xFF) + ((imgT[ke + 1][re - 1] >> 16) & 0xFF) + ((imgT[ke - 1][re] >> 16) & 0xFF) + ((imgT[ke][re] >> 16) & 0xFF) + ((imgT[ke + 1][re] >> 16) & 0xFF) + ((imgT[ke - 1][re + 1] >> 16) & 0xFF) + ((imgT[ke][re + 1] >> 16) & 0xFF) + ((imgT[ke + 1][re + 1] >> 16) & 0xFF));
                    green = (((imgT[ke - 1][re - 1] >> 8) & 0xFF) + ((imgT[ke][re - 1] >> 8) & 0xFF) + ((imgT[ke + 1][re - 1] >> 8) & 0xFF) + ((imgT[ke - 1][re] >> 8) & 0xFF) + ((imgT[ke][re] >> 8) & 0xFF) + ((imgT[ke + 1][re] >> 8) & 0xFF) + ((imgT[ke - 1][re + 1] >> 8) & 0xFF) + ((imgT[ke][re + 1] >> 8) & 0xFF) + ((imgT[ke + 1][re + 1] >> 8) & 0xFF));
                    blue = ((imgT[ke - 1][re - 1] & 0xFF) + (imgT[ke][re - 1] & 0xFF) + (imgT[ke + 1][re - 1] & 0xFF) + (imgT[ke - 1][re] & 0xFF) + (imgT[ke][re] & 0xFF) + (imgT[ke + 1][re] & 0xFF) + (imgT[ke - 1][re + 1] & 0xFF) + (imgT[ke][re + 1] & 0xFF) + (imgT[ke + 1][re + 1] & 0xFF));
                    red = red / 9;
                    green = green / 9;
                    blue = blue / 9;
                    tab[ke][re] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
                }
            }
        } else if (metods[ktory].getNazwa().equals(getString(R.string.medianowy))) {
            int[][] tab = new int[height][width];
            for (int re = 0; re < height; re++) {
                for (int ke = 0; ke < width; ke++) {
                    tab[re][ke] = imgT[re][ke];
                }
            }
            int[] r = new int[9];
            int[] g = new int[9];
            int[] b = new int[9];
            int n = 8;
            int tmpP;
            for (int re = 1; re < (width - 1); re++) {
                for (int ke = 1; ke < (height - 1); ke++) {
                    r[0] = ((imgT[ke - 1][re - 1] >> 16) & 0xFF);
                    r[1] = ((imgT[ke][re - 1] >> 16) & 0xFF);
                    r[2] = ((imgT[ke + 1][re - 1] >> 16) & 0xFF);
                    r[3] = ((imgT[ke - 1][re] >> 16) & 0xFF);
                    r[4] = ((imgT[ke][re] >> 16) & 0xFF);
                    r[5] = ((imgT[ke + 1][re] >> 16) & 0xFF);
                    r[6] = ((imgT[ke - 1][re + 1] >> 16) & 0xFF);
                    r[7] = ((imgT[ke][re + 1] >> 16) & 0xFF);
                    r[8] = ((imgT[ke + 1][re + 1] >> 16) & 0xFF);
                    g[0] = ((imgT[ke - 1][re - 1] >> 8) & 0xFF);
                    g[1] = ((imgT[ke][re - 1] >> 8) & 0xFF);
                    g[2] = ((imgT[ke + 1][re - 1] >> 8) & 0xFF);
                    g[3] = ((imgT[ke - 1][re] >> 8) & 0xFF);
                    g[4] = ((imgT[ke][re] >> 8) & 0xFF);
                    g[5] = ((imgT[ke + 1][re] >> 8) & 0xFF);
                    g[6] = ((imgT[ke - 1][re + 1] >> 8) & 0xFF);
                    g[7] = ((imgT[ke][re + 1] >> 8) & 0xFF);
                    g[8] = ((imgT[ke + 1][re + 1] >> 8) & 0xFF);
                    b[0] = (imgT[ke - 1][re - 1] & 0xFF);
                    b[1] = (imgT[ke][re - 1] & 0xFF);
                    b[2] = (imgT[ke + 1][re - 1] & 0xFF);
                    b[3] = (imgT[ke - 1][re] & 0xFF);
                    b[4] = (imgT[ke][re] & 0xFF);
                    b[5] = (imgT[ke + 1][re] & 0xFF);
                    b[6] = (imgT[ke - 1][re + 1] & 0xFF);
                    b[7] = (imgT[ke][re + 1] & 0xFF);
                    b[8] = (imgT[ke + 1][re + 1] & 0xFF);
                    for (int i = 0; i < 7; i++) {
                        for (int ii = 0; ii < n; ii++) {
                            if (r[ii] > r[(ii + 1)]) {
                                tmpP = r[(ii + 1)];
                                r[(ii + 1)] = r[ii];
                                r[ii] = tmpP;
                            }
                            if (g[ii] > g[(ii + 1)]) {
                                tmpP = g[(ii + 1)];
                                g[(ii + 1)] = g[ii];
                                g[ii] = tmpP;
                            }
                            if (b[ii] > b[(ii + 1)]) {
                                tmpP = b[(ii + 1)];
                                b[(ii + 1)] = b[ii];
                                b[ii] = tmpP;
                            }
                        }
                    }
                    tab[ke][re] = (0xFF << 24) | (r[4] << 16) | (g[4] << 8) | b[4];
                }
                imgT = tab;
            }
        } else if (metods[ktory].getNazwa().equals(getString(R.string.laplacea1))) {
            int fil[] = new int[9];
            fil[0] = -1;
            fil[1] = -1;
            fil[2] = -1;
            fil[3] = -1;
            fil[4] = 8;
            fil[5] = -1;
            fil[6] = -1;
            fil[7] = -1;
            fil[8] = -1;

            imgT = fil3x3(fil, width, height, imgT);
        } else if (metods[ktory].getNazwa().equals(getString(R.string.wyost))) {
            int fil[] = new int[9];
            fil[0] = -1;
            fil[1] = -1;
            fil[2] = -1;
            fil[3] = -1;
            fil[4] = 9;
            fil[5] = -1;
            fil[6] = -1;
            fil[7] = -1;
            fil[8] = -1;

            imgT = fil3x3(fil, width, height, imgT);
        } else if (metods[ktory].getNazwa().equals(getString(R.string.uwyp))) {
            int fil[] = new int[9];
            fil[0] = -1;
            fil[1] = 0;
            fil[2] = 1;
            fil[3] = -1;
            fil[4] = 1;
            fil[5] = 1;
            fil[6] = -1;
            fil[7] = 0;
            fil[8] = 1;

            imgT = fil3x3(fil, width, height, imgT);
        } else if(metods[ktory].getNazwa().equals("Przycinanie góra")){
            int tmpP = (int) (metods[ktory].getStosunek() * height);
            if((height-tmpP)>9) {
                int imgT2[][] = new int[(height - tmpP)][width];
                for (int j = (height - 1); j > tmpP; j--) {
                    for (int k = 0; k < width; k++) {
                        imgT2[(j - tmpP)][k] = imgT[j][k];
                    }
                }
                imgT = new int[(height - tmpP)][width];
                height -= tmpP;
                imgT = imgT2;
            }
        } else if(metods[ktory].getNazwa().equals("Przycinanie dół")){
            height = (int) (metods[ktory].getStosunek() * height);
            if(height>9) {
                int imgT2[][] = new int[height][width];
                for (int j = 0; j < height; j++) {
                    for (int k = 0; k < width; k++) {
                        imgT2[j][k] = imgT[j][k];
                    }
                }
                imgT = new int[height][width];
                imgT = imgT2;
            }
        } else if(metods[ktory].getNazwa().equals("Przycinanie lewo")){
            int tmpP = (int) (metods[ktory].getStosunek() * width);
            if((width-tmpP)>9) {
                int imgT2[][] = new int[height][(width - tmpP)];
                for (int j = 0; j < height; j++) {
                    for (int k = (width - 1); k > tmpP; k--) {
                        imgT2[j][(k - tmpP)] = imgT[j][k];
                    }
                }
                imgT = new int[height][(width - tmpP)];
                width -= tmpP;
                imgT = imgT2;
            }
        } else if(metods[ktory].getNazwa().equals("Przycinanie prawo")){
            width = (int) (metods[ktory].getStosunek() * width);
            if(width>9) {
                int imgT2[][] = new int[height][width];
                for (int j = 0; j < height; j++) {
                    for (int k = 0; k < width; k++) {
                        imgT2[j][k] = imgT[j][k];
                    }
                }
                imgT = new int[height][width];
                for (int j = 0; j < height; j++) {
                    for (int k = 0; k < width; k++) {
                        imgT[j][k] = imgT2[j][k];
                    }
                }
            }
        }
    }
}
