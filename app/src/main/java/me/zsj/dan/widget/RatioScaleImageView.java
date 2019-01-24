package me.zsj.dan.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import me.zsj.dan.utils.ScreenUtils;

/**
 * @author zsj
 */

public class RatioScaleImageView extends AppCompatImageView {

    private int maxHeight;
    private int minHeight;
    private int minWidth;

    private int originalWidth;
    private int originalHeight;

    public RatioScaleImageView(Context context) {
        this(context, null);
    }

    public RatioScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        maxHeight = ScreenUtils.getScreenHeight(context) - ScreenUtils.dpToPx(56);
        minHeight = ScreenUtils.dpToPx(120);
    }

    public void setOriginalSize(int originalWidth, int originalHeight) {
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        minWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (originalWidth > 0 && originalHeight > 0) {
            float ratio = (float) originalWidth / (float) originalHeight;

            int width = 0;
            int height = 0;

            if (originalWidth < minWidth) {
                width = minWidth;
                height = (int) ((float) width / ratio);
            } else if (originalWidth >= minWidth) {
                width = minWidth;
                height = (int) ((float) width / ratio);
            }

            if (originalHeight >= maxHeight) {
                height = minHeight;
            }

            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
