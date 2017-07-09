package me.zsj.dan.widget;

import android.content.Context;
import android.util.AttributeSet;

import me.zsj.dan.utils.ScreenUtils;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author zsj
 */

public class GifRatioScaleImageView extends GifImageView {

    private int maxHeight;
    private int minWidth;

    private int originalWidth;
    private int originalHeight;

    public GifRatioScaleImageView(Context context) {
        this(context, null);
    }

    public GifRatioScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifRatioScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        minWidth = ScreenUtils.getScreenWidth(context) - ScreenUtils.dpToPx(10);
        maxHeight = ScreenUtils.getScreenHeight(context) - ScreenUtils.dpToPx(56);
    }

    public void setOriginalSize(int originalWidth, int originalHeight) {
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (originalWidth > 0 && originalHeight > 0) {
            float ratio = (float) originalWidth / (float) originalHeight;

            int width = minWidth;
            int height = 0;

            if (originalWidth < minWidth) {
                width = minWidth;
                height = (int) ((float) width / ratio);
            } else if (originalWidth >= minWidth) {
                width = minWidth;
                height = (int) ((float) width / ratio);
            }

            if (originalHeight >= maxHeight) {
                height = ScreenUtils.dpToPx(200);
            }

            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
