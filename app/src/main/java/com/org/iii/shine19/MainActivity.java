package com.org.iii.shine19;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private String strPlayMusic;

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

    }

    public void b1(View v){
        Intent intent =
                new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, 1);
    }

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