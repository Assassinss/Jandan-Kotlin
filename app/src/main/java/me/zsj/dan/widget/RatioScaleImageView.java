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
        minWidth = ScreenUtils.getScreenWidth(context) - ScreenUtils.dpToPx(10);
        maxHeight = ScreenUtils.getScreenHeight(context) - ScreenUtils.dpToPx(56);
        minHeight = ScreenUtils.dpToPx(120);
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
                height = minHeight;
            }

            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /*//TODO: 更好的获取图片的宽高方法？
    public void setBigImage(final PictureAdapter.SingleHolder holder,
                            final File resource, final InputStream inputStream) {
        try {
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            tmpOptions.inJustDecodeBounds = true;
            tmpOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            BitmapFactory.decodeStream(inputStream, null, tmpOptions);
            int width = tmpOptions.outWidth;
            int height = tmpOptions.outHeight;
            BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(resource.getPath(), false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap;
            if (height >= screenHeight) {
                holder.getBrowsePicture().setVisibility(VISIBLE);
                setOriginalSize(width, minHeight);
                bitmap = regionDecoder.decodeRegion(new Rect(0, 0, width, minHeight), tmpOptions);
            } else {
                holder.getBrowsePicture().setVisibility(GONE);
                setOriginalSize(width, height);
                bitmap = regionDecoder.decodeRegion(new Rect(0, 0, width, height), tmpOptions);
            }
            setImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBigImage(final File resource, int width) {
        try {
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            tmpOptions.inJustDecodeBounds = true;
            tmpOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(resource.getPath(), false);
            setOriginalSize(width, minHeight);
            Bitmap bitmap = regionDecoder.decodeRegion(new Rect(0, 0, width, minHeight), tmpOptions);
            setImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImageView(Bitmap bitmap, int originalWidth, int originalHeight) {
        float ratio = (float) originalWidth / (float) originalHeight;

        int width = 0;
        int height = 0;

        if (originalWidth < minWidth) {
            width = minWidth;
            height = (int) ((float) width / ratio);
        } else {
            width = minWidth;
            height = (int) ((float) width / ratio);
        }

        setOriginalSize(width, height);
        setImage(bitmap);
    }*/
}
