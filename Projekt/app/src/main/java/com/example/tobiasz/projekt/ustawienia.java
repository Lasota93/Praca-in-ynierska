package com.example.tobiasz.projekt;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;

public class ustawienia extends AppCompatActivity {
    private boolean oryginalne;
    private Switch taknie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustawienia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        oryginalne = ((tablicaMetod) this.getApplication()).getOrg();
        taknie = (Switch) findViewById(R.id.switch2);
        taknie.setChecked(oryginalne);
        taknie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setOryginalne(taknie.isChecked());
                timer.start();
            }
        });
    }

    CountDownTimer timer = new CountDownTimer(500, 500) {
        public void onTick(long millisUntilFinished) {}
        public void onFinish() {powrot();}
    };

    private void powrot(){
        this.finish();
    }

    private void setOryginalne(boolean set){
        oryginalne = set;
        ((tablicaMetod) this.getApplication()).setOrg(oryginalne);
    }

}
