/**
 * Created by Ram_Thirupathy on 9/2/2016.
 */
package com.ramkt.rdownloader.RequestTool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.ImageView;

import com.ramkt.rdownloader.ResponseListener;
import com.ramkt.rdownloader.RDownloadLog;
import com.ramkt.rdownloader.Request;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;


/**
 * ImageRequest extends {@link Request} to server image download request
 */
public class ImageRequest extends Request<Bitmap> {
    private static final String TAG = ImageRequest.class.getSimpleName();
    private int mDesiredWidth;
    private int mDesiredHeight;
    private int mActualWidth;
    private int mActualHeight;

    /**
     * ImageResponseListener extends {@link ResponseListener} binds imageview
     * with request, takes care of binding correct bitamp
     * to the view, preferable listener to use with {@link ImageRequest}
     */
    public abstract static class ImageResponseListener<Bitmap> extends ResponseListener<Bitmap> {
        WeakReference<ImageView> viewReference;

        public ImageResponseListener(ImageView view) {
            view.setImageBitmap(null);
            viewReference = new WeakReference<ImageView>(view);
        }

        @Override
        public void onResponse(Bitmap bitmap) {
            //ignore
        }
    }

    /**
     * Constructor of the class to be used if image aspect ratio is not available
     * and if listener is {@link ImageResponseListener} then it set the request key to the
     * image view tag
     *
     * @param url      url of the image to download
     * @param listener to wait for the response
     */
    public ImageRequest(String url, ResponseListener<Bitmap> listener) {
        super(url, url, listener);
        setKey(listener);
    }

    /**
     * Constructor of the class to be used if aspect ratio of image is available
     * and if listener is {@link ImageResponseListener} then it set the request key to the
     * image view tag
     *
     * @param url           url of the image to download
     * @param listener      to wait for the response
     * @param desiredWidth  to resize
     * @param desiredHeight to resize
     * @param actualWidth   actual width of the image
     * @param actualHeight  actual height of the image
     */
    public ImageRequest(String url, ResponseListener<Bitmap> listener, int desiredWidth, int desiredHeight, int actualWidth, int actualHeight) {
        super(url, String.format("%s%d%d", url, desiredWidth, desiredHeight), listener);
        setKey(listener);
        this.mDesiredHeight = desiredHeight;
        this.mDesiredWidth = desiredWidth;
        this.mActualHeight = actualHeight;
        this.mActualWidth = actualWidth;
    }

    /**
     * Method converts the stream to Bitmap
     * binding
     *
     * @param stream stream downloaded from server
     * @return bitmap from stream
     */
    @Override
    public Bitmap parseResponse(InputStream stream) throws Exception {
        return decodeBitmap(stream);
    }

    /**
     * Sends response to all the listeners
     *
     * @param bitmap to bind with image view if key matches
     * @param key    key to verify
     */
    @Override
    protected void sendResponse(Bitmap bitmap, String key) {
        if (!mResponseListenerList.isEmpty()) {
            for (ResponseListener<Bitmap> item : mResponseListenerList) {
                if (item instanceof ImageResponseListener) {
                    if (((ImageResponseListener) item).viewReference.get() != null) {
                        if (key.equals(((ImageResponseListener<Bitmap>) item).viewReference.get().getTag())) {
                            ((ImageResponseListener<Bitmap>) item).viewReference.get().setImageBitmap(bitmap);
                        }
                    }
                } else {
                    item.onResponse(bitmap);
                }

            }
        }
    }

    /**
     * Set the unique request key to the tag of image view to avoid wrong bitmap
     * binding
     *
     * @param listener
     */
    private void setKey(ResponseListener<Bitmap> listener) {
        if (listener instanceof ImageResponseListener) {
            if (((ImageResponseListener<Bitmap>) listener).viewReference.get() != null) {
                ((ImageResponseListener<Bitmap>) listener).viewReference.get().setTag(mRequestKey);
            }
        }
    }


    /**
     * Decode bitamp to fit the size of image place holder
     *
     * @param stream
     */
    private Bitmap decodeBitmap(InputStream stream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (mDesiredWidth > 0 && mDesiredHeight > 0) {//resize
            RDownloadLog.i("%s: Resize Desired size [%d] [%d]", TAG, mDesiredWidth, mDesiredHeight);
            options.outWidth = mActualWidth;
            options.outHeight = mActualHeight;
            RDownloadLog.i("%s: Resize Actual size [%d] [%d]", TAG, options.outWidth, options.outHeight);
            options.inSampleSize = manipulateInSampleSize(options);
            RDownloadLog.i("%s: Sample size [%d]", TAG, options.inSampleSize);
            if ((options.inSampleSize * mDesiredHeight) != options.outHeight || (options.inSampleSize * mDesiredWidth) != options.outWidth) {
                options.inScaled = true;
                if (mDesiredWidth > mDesiredHeight) {
                    options.inDensity = options.outWidth;
                    options.inTargetDensity = mDesiredWidth * options.inSampleSize;
                } else {
                    options.inDensity = options.outHeight;
                    options.inTargetDensity = mDesiredHeight * options.inSampleSize;
                }
            }
            addInBitmapOptions(options);
        }
        return BitmapFactory.decodeStream(stream, null, options);
    }


    /**
     * Find perfect insample value
     *
     * @param options
     * @return int insample factor
     */
    private int manipulateInSampleSize(BitmapFactory.Options options) {
        final int photoWidth = options.outWidth;
        final int photoHeight = options.outHeight;
        int scaleFactor = 1;
        if (photoWidth > mDesiredWidth || photoHeight > mDesiredHeight) {
            final int resizedWidth = photoWidth / 2;
            final int resizedHeight = photoHeight / 2;
            while (resizedWidth / scaleFactor >= mDesiredWidth
                    || resizedHeight / scaleFactor >= mDesiredHeight) {
                scaleFactor *= 2;
            }
        }
        return scaleFactor;
    }

    /**
     * Reuse if any available bitmap to save some run time
     *
     * @param options
     */
    private void addInBitmapOptions(BitmapFactory.Options options) {
        options.inMutable = true;
        Bitmap bitmap = getReuseSet(options);
        if (bitmap != null) {
            RDownloadLog.i("%s: Reusable bitamp [%s]", TAG, bitmap.toString());
            options.inBitmap = bitmap;
        } else {
            RDownloadLog.i("%s: Reusable bitamp not available", TAG);
        }
    }

    /**
     * Verify if any reusable bitmap available
     *
     * @param options
     * @return Bitmap
     */
    private Bitmap getReuseSet(BitmapFactory.Options options) {
        Bitmap bitmap = null;
        if (mRDownloader != null) {
            Set<SoftReference<Bitmap>> reuseSet = mRDownloader.getReuseSet();
            if (reuseSet != null) {
                synchronized (reuseSet) {
                    Iterator<SoftReference<Bitmap>> iterator = reuseSet.iterator();
                    Bitmap item = null;
                    while (iterator.hasNext()) {
                        item = iterator.next().get();
                        if (item != null && item.isMutable()) {
                            if (canFit(item, options)) {
                                bitmap = item;
                                iterator.remove();
                                break;
                            }
                        } else {
                            iterator.remove();
                        }
                    }
                }
            }
        }
        return bitmap;
    }

    /**
     * Check is bitmap valid to reuse for the current request
     *
     * @param options
     * @return boolean
     */
    private boolean canFit(Bitmap bitmap, BitmapFactory.Options options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int width = options.outWidth / options.inSampleSize;
            int height = options.outHeight / options.inSampleSize;
            int byteCount = width * height * getBytesPerPixel(bitmap.getConfig());
            return byteCount <= bitmap.getAllocationByteCount();
        } else {
            return bitmap.getWidth() == options.outWidth && bitmap.getHeight() == options.outHeight
                    && options.inSampleSize == 1;
        }
    }

    /**
     * To config value of size per pixel
     *
     * @param config
     * @return int size of a pixel
     */
    private int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }
}
