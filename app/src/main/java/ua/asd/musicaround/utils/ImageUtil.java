package ua.asd.musicaround.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {
    public static void selectPhoto(Activity activity, int requestCode) {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            activity.startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), requestCode);
        } else {
            activity.startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), requestCode);
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        }
        return res;
    }

    /**
     * Method returns cropped by center(square), resized and rotated Bitmap.
     *
     * @param imageUri uri to photo (taked from camera or from gallery)
     * @param width    needed width
     * @param height   needed height
     * @return resulted Bitmap
     */
    public static Bitmap rotateImage(String imageUri, Integer width, Integer height, boolean cropCenter) {
        Bitmap resultBitmap = null;
        try {
            if (width != null && height != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                BitmapFactory.decodeFile(imageUri, options);
                options.inSampleSize = calculateInSampleSize(options, width, height);
                options.inJustDecodeBounds = false;
                resultBitmap = BitmapFactory.decodeFile(imageUri, options);
            } else {
                resultBitmap = BitmapFactory.decodeFile(imageUri);
            }
            if (cropCenter) {
                resultBitmap = cropCenter(resultBitmap);
            }
            ExifInterface ei = new ExifInterface(imageUri);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(resultBitmap, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(resultBitmap, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(resultBitmap, 270);
            }
        } catch (IOException e) {
            Log.e(e.getMessage(), "IO Exception in rotating image");
            return resultBitmap;
        }
        return resultBitmap;
    }

    /**
     * Crop source bitmap to square.
     *
     * @param source Source Bitmap
     * @return Cropped in center Bitmap
     */
    public static Bitmap cropCenter(Bitmap source) {
        Bitmap resultBitmap;
        if (source.getWidth() >= source.getHeight()) {
            resultBitmap = Bitmap.createBitmap(
                    source,
                    source.getWidth() / 2 - source.getHeight() / 2,
                    0,
                    source.getHeight(),
                    source.getHeight()
            );
        } else {
            resultBitmap = Bitmap.createBitmap(
                    source,
                    0,
                    source.getHeight() / 2 - source.getWidth() / 2,
                    source.getWidth(),
                    source.getWidth()
            );
        }
        if (!source.equals(resultBitmap)) {
            source.recycle();
        }
        return resultBitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * Rotate image on needed angle.
     *
     * @param source source Bitmap
     * @param angle  angle for rotating Bitmap
     * @return Rotated Bitmap
     */
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        if (!retVal.equals(source)) {
            source.recycle();
        }
        return retVal;
    }

    public static String getAvatarBase64(String path) {
        if (path != null) {
            Bitmap avatarBitmap = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            avatarBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        } else {
            return null;
        }
    }

}

