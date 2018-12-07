package me.zsj.dan.test;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import me.zsj.dan.R;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author zsj
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final Button play = (Button) findViewById(R.id.play);

        final GifImageView gifImageView = (GifImageView) findViewById(R.id.gif_image);

        gifImageView.setImageDrawable(new ColorDrawable(Color.GRAY));

        new Thread() {
            @Override
            public void run() {
                try {
                    File file = Glide.with(TestActivity.this)
                            .load("http://wx4.sinaimg.cn/mw690/92677416gy1fgpcvb1578g20ib08ve85.gif")
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    InputStream is = new BufferedInputStream(new FileInputStream(file));
                    final GifDrawable gifDrawable = new GifDrawable(is);
                    Log.d("TestActivity", gifDrawable.toString());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("TestActivity", "on main thread....");
                            gifImageView.setImageDrawable(gifDrawable);
                            play.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (gifDrawable.isPlaying()) {
                                        gifDrawable.stop();
                                    } else {
                                        gifDrawable.start();
                                    }
                                }
                            });
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }
}
