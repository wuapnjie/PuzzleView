package com.xiaopo.flying.photolayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xiaopo.flying.puzzle.PuzzleView;

public class MainActivity extends AppCompatActivity {

    private PuzzleView mPuzzleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPuzzleView = (PuzzleView) findViewById(R.id.photo_layout_view);

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.demo1);
        final Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.demo2);
        final Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.demo3);

        mPuzzleView.post(new Runnable() {
            @Override
            public void run() {
                mPuzzleView.addPhoto(bitmap);
                mPuzzleView.addPhoto(bitmap1);
                mPuzzleView.addPhoto(bitmap2);
            }
        });
    }
}
