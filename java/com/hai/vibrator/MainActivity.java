package com.hai.vibrator;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    ViewPager myPager;
    ToggleButton toggleButton;
    int[] images = {R.drawable.mode0, R.drawable.mode1, R.drawable.mode2, R.drawable.mode3, R.drawable.mode4};
    private int MODE = 1;
    private int COUNT = 0;
    private StartAppAd startAppAd;

    long[] pattern0 = { 0, 200, 0, 200, 0, 200, 0, 200, 0};
    long[] pattern1 = { 0, 200, 200};
    long[] pattern2 = { 0, 200, 500};
    long[] pattern3 = { 0, 500, 200};
    long[] pattern4 = { 0, 100, 500};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startapp ads
        StartAppSDK.init(this, "204159934", false);
        startAppAd = new StartAppAd(this);
        startAppAd.loadAd();

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.toggle_switch);
        toggleButton = (ToggleButton)findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                COUNT++;
                if(COUNT >= 10){
                    startAppAd.showAd(); // show the ad
                    startAppAd.loadAd(); // load the next ad
                    COUNT = 1;
                }
                if(isChecked){
                    mediaPlayer.start();
                    vibrate(MODE);
                }else {
                    mediaPlayer.start();
                    Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    vib.cancel();
                }
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, images);
        myPager = (ViewPager) findViewById(R.id.pager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(1);
        myPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                COUNT++;
                if(COUNT >= 10){
                    startAppAd.showAd(); // show the ad
                    startAppAd.loadAd(); // load the next ad
                    COUNT = 1;
                }
                if(toggleButton.isChecked()){
                    MODE = position;
                    vibrate(MODE);
                }else {
                    Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    vib.cancel();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void vibrate(int pos){
        Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        switch (pos){
            case 0:
                vib.vibrate(pattern0, 0);
                break;
            case 1:
                vib.vibrate(pattern1, 0);
                break;
            case 2:
                vib.vibrate(pattern2, 0);
                break;
            case 3:
                vib.vibrate(pattern3, 0);
                break;
            case 4:
                vib.vibrate(pattern4, 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vib.cancel();
        startAppAd.onBackPressed();
        super.onBackPressed();
    }
}
