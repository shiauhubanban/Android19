package com.org.iii.shine19;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private String strPlayMusic;
    private MediaRecorder mr;
    private File sdroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //取得權限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        }
        sdroot = Environment.getExternalStorageDirectory(); //得到根目錄

    }
    
    //使用外部錄音
    public void b1(View v){
        //Intent https://developer.android.com/guide/components/intents-filters.html
        Intent intent =
                new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, 1); //轉跳的Activity

        //如果想要讓一個Activity跳到另外一個Activity的話, 就可以使用Intent,
        //設定好要轉跳的Activity, 然後利用startActivity或者startActivityForResult來執行。
    }

    //播放外部的
    public void b2(View v){
        if (strPlayMusic == null) return;

        try {
            mp = new MediaPlayer();
            mp.setDataSource(strPlayMusic);
            mp.prepare();
            mp.start();
        }catch(Exception e){
            Log.v("shine", e.toString());
        }
    }

    //https://mengzn.gitbooks.io/android-basicteaching/content/onactivityresult.html
    //在一個主界面(主Activity)上能連接往許多不同子功能模塊(子Activity上去)，
    //當子模塊的事情做完之後就回到主界面，或許還同時返回一些子模塊完成的數據交給主Activity處理。這樣的數據交流就要用到回調函數onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            //真實路徑
            //Log.v("shine", getRealPathFromURI(uri));
            strPlayMusic = getRealPathFromURI(uri);
        }
    }

    //錄音
    public void b3(View v) {
        mr = new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mr.setOutputFile(sdroot.getAbsolutePath() + "/shine.3gp");


        try {
            mr.prepare();
            mr.start();
        }catch (Exception ee){
            Log.v("shine", ee.toString());
        }
    }

    //暫停
    public void b4(View v){
        if (mr!=null){
            mr.stop();
            mr.release();
            mr = null;
        }
    }

    //播放
    public void b5(View v){
        try {
            mp = new MediaPlayer();
            mp.setDataSource(sdroot.getAbsolutePath() + "/shine.3gp");
            mp.prepare();
            mp.start();
        }catch(Exception e){
            Log.v("shine", e.toString());
        }
    }
    
    //真實路徑
    public String getRealPathFromURI(Uri contentUri) {
        ContentResolver re = getContentResolver();
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = re.query(contentUri, proj, null,
                null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}