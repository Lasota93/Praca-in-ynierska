package com.example.tobiasz.projekt;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

public class tablicaMetod extends Application {
    private Metoda [] metods = new Metoda[100];
    private int i =0;
    private Uri uri;
    private boolean wznowienie;
    private Bitmap image;
    private int [][] tab;
    private boolean org = false;

    public void clear(){
        metods = null;
        metods = new Metoda[100];
        i = 0;
        uri = null;
        wznowienie = false;
        image = null;
    }

    public tablicaMetod(){};

    public void setTab(Metoda met){
        metods[i] = met;
        i++;
    }

    public Metoda[] getTab(){
        return metods;
    }

    public Metoda getTab(int ii){
        return metods[ii];
    }

    public int getI(){
        return i;
    }

    public void usun(int tabCU[]){
        Log.d("E/Android", "tM1 "+i);
        for(int ii = 0; ii < i; ii++){
            if(tabCU[ii]==1){
                    metods[ii] = null;
            }
        }
        int miejsce = 0;
        for (int iii = 0; iii < i; iii++) {
            if(tabCU[iii] == 0){
                metods[miejsce] = metods[iii];
                miejsce ++;
            }
        }
        i = miejsce;
        Log.d("E/Android", "tM2 "+i);
    }

    public boolean cofnij(){
        if(i>0) {
            metods[i] = null;
            i--;
            return true;
        }
        return false;
    }

    public void setUri(Uri u){
        uri = u;
    }

    public Uri getUri(){
        return uri;
    }

    public void setW(boolean w){
        wznowienie = w;
    }

    public boolean getW(){
        return wznowienie;
    }

    public void setImage(Bitmap img){
        image = img;
    }

    public Bitmap getBitmap(){
        return image;
    }

    public void setTabOrg(int[][] tabI){
        int w = tabI[0].length;
        int h = tabI.length;
        tab = new int[h][w];
        for(int j = 0; j < h; j++){
            for(int jj = 0; jj < w; jj++){
                tab[j][jj] = tabI[j][jj];
            }
        }
    }

    public int[][] getTabOrg(){
        int w = tab[0].length;
        int h = tab.length;
        int tmp [][] = new int [h][w];
        for(int j = 0; j < h; j++){
            for(int jj = 0; jj < w; jj++){
                tmp[j][jj] = tab[j][jj];
            }
        }
        return tmp;
    }

    @Override
    public String toString(){
        String metody = "";
        for(int j = 0; j < i; j++){
            metody += metods[j].getNazwa()+" "+j;
        }
        return metody;
    }

    public void setOrg(boolean set){
        org = set;
    }

    public boolean getOrg(){
        return org;
    }

}
