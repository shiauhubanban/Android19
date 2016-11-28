package com.org.iii.shine19;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void b1(View v){
        Intent intent =
                new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            //記憶卡 真是路徑
            Log.v("shine", getRealPathFromURI(uri));
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