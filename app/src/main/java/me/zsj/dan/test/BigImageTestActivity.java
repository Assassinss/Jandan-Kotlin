package me.zsj.dan.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import me.zsj.dan.R;
import me.zsj.dan.glide.ProgressTarget;
import me.zsj.dan.widget.RatioScaleImageView;

/**
 * @author zsj
 */

public class BigImageTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image_test);

        final RatioScaleImageView imageView = (RatioScaleImageView) findViewById(R.id.big_image);

        String url = "http://wx4.sinaimg.cn/mw600/005upFlzgy1fgqygad42cj30ht9uonpe.jpg";

        Glide.with(this).load(url).downloadOnly(new ProgressTarget<String, File>(url, null) {

            @Override
            public void onProgress(long bytesRead, long expectedLength) {
            }

            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> animation) {
                super.onResourceReady(resource, animation);
                //imageView.setImage(new FileBitmapDecoderFactory(resource));
                try {
                    InputStream fileInputStream = new FileInputStream(resource.getPath());
                    //imageView.setBigImage(fileInputStream, resource.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getSize(SizeReadyCallback cb) {
                cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            }
        });
    }
}
