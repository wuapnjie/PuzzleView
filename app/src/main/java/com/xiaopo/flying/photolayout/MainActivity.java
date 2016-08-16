package com.xiaopo.flying.photolayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xiaopo.flying.photolayout.photolayout.PhotoLayoutView;

public class MainActivity extends AppCompatActivity {

    private PhotoLayoutView mPhotoLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoLayoutView = (PhotoLayoutView) findViewById(R.id.photo_layout_view);

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo1);
        final Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.photo2);
        final Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.photo3);

        mPhotoLayoutView.post(new Runnable() {
            @Override
            public void run() {
                mPhotoLayoutView.addPhoto(bitmap);
                mPhotoLayoutView.addPhoto(bitmap1);
                mPhotoLayoutView.addPhoto(bitmap2);
            }
        });
    }
}
