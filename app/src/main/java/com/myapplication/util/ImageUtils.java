package com.myapplication.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {




    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";

    /**
     * 请求相册
     */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /**
     * 请求相机
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
    /**
     * 请求裁剪
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;


    /**
     * 压缩图片(按比例)
     *
     * @param mMaxWidth
     * @param mMaxHeight
     * @param path
     * @return
     */
    private static Bitmap compress(int mMaxWidth, int mMaxHeight, String path) {

        if (StringUtils.isEmpty(path)) {
            return null;
        }
        Bitmap bitmap = null;

        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        // If we have to resize this image, first get the natural bounds.
        decodeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, decodeOptions);
        int actualWidth = decodeOptions.outWidth;
        int actualHeight = decodeOptions.outHeight;

        // Then compute the dimensions we would ideally like to decode to.
        int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                actualWidth, actualHeight);
        int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                actualHeight, actualWidth);

        // Decode to the nearest power of two scaling factor.
        decodeOptions.inJustDecodeBounds = false;
        // TODO(ficus): Do we need this or is it okay since API 8 doesn't support it?
        // decodeOptions.inPreferQualityOverSpeed = PREFER_QUALITY_OVER_SPEED;
        decodeOptions.inSampleSize =
                findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
        Bitmap tempBitmap = BitmapFactory.decodeFile(path, decodeOptions);

        // If necessary, scale down to the maximal acceptable size.
        if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth ||
                tempBitmap.getHeight() > desiredHeight)) {
            bitmap = Bitmap.createScaledBitmap(tempBitmap,
                    desiredWidth, desiredHeight, true);
            tempBitmap.recycle();
        } else {
            bitmap = tempBitmap;
        }
        return bitmap;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary,
                                           int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    public static int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }

    /**
     * 保存图片 文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @param context
     * @param fileName
     * @param bitmap
     * @param quality
     */
    public static void saveImage(Context context, String fileName,
                                 Bitmap bitmap, int quality) {
        if (bitmap == null || fileName == null || context == null)
            return;

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            byte[] bytes = stream.toByteArray();
            fos.write(bytes);
            fos.flush();
            fos.close();
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveImage(Context context, String fileName, Bitmap bitmap)
            throws IOException {
        saveImage(context, fileName, bitmap, 100);
    }

    /**
     * 保存图片
     *
     * @param context
     * @param filePath
     * @param bitmap
     * @param quality
     */
    public static void saveImageByPath(Context context, String filePath,
                                       Bitmap bitmap, int quality) {
        if (filePath == null || filePath.equals("") || bitmap == null) {
            return;
        }
        File file = new File(filePath.substring(0,
                filePath.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(
                    new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            bitmap.recycle();
            scanPhoto(context, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 立刻扫描图片
     */
    private static void scanPhoto(Context context, String imgFileName) {
        if (imgFileName == null || imgFileName.equals("")) {
            return;
        }
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取照相机使用的目录
     *
     * @return
     */
    public static String getCamerPath() {
        return Environment.getExternalStorageDirectory() + File.separator
                + "FounderNews" + File.separator;
    }

    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     *
     * @param mUri
     * @return
     */

    public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + SDCARD + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }

    /**
     * 通过uri获取文件的绝对路径
     *
     * @param uri
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getAbsoluteImagePath(Activity context, Uri uri) {
        String imagePath = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(uri, proj, // Which columns to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(column_index);
            }
        }

        return imagePath;
    }

    /**
     * 获取SD卡中最新图片路径
     *
     * @return
     */
    public static String getLatestImage(Activity context) {
        String latestImage = null;
        String[] items = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, items, null,
                null, MediaStore.Images.Media._ID + " desc");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                latestImage = cursor.getString(1);
                break;
            }
        }

        return latestImage;
    }

    /**
     * 计算缩放图片的宽高
     *
     * @param img_size
     * @param square_size
     * @return
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size)
            return img_size;
        double ratio = square_size
                / (double) Math.max(img_size[0], img_size[1]);
        return new int[]{(int) (img_size[0] * ratio),
                (int) (img_size[1] * ratio)};
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                    true);
        }
        return newbmp;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap) {
        // 获取这个图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = 200;
        int newHeight = 200;
        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        // 旋转图片 动作
        // matrix.postRotate(45);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return resizedBitmap;
    }

    /**
     * (缩放)重绘图片
     *
     * @param context Activity
     * @param bitmap
     * @return
     */
    public static Bitmap reDrawBitMap(Activity context, Bitmap bitmap) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int rHeight = dm.heightPixels;
        int rWidth = dm.widthPixels;
        // float rHeight=dm.heightPixels/dm.density+0.5f;
        // float rWidth=dm.widthPixels/dm.density+0.5f;
        // int height=bitmap.getScaledHeight(dm);
        // int width = bitmap.getScaledWidth(dm);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float zoomScale;

        /** 方式3 **/
        if (width >= rWidth)
            zoomScale = ((float) rWidth) / width;
        else
            zoomScale = 1.0f;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(zoomScale, zoomScale);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 获得圆角图片的方法
     *
     * @param bitmap
     * @param roundPx 一般设成14
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获得带倒影的图片方法
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 将bitmap转化为drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * 获取图片类型
     *
     * @param file
     * @return
     */
    public static String getImageType(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            String type = getImageType(in);
            return type;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取图片的类型信息
     *
     * @param in
     * @return
     * @see #getImageType(byte[])
     */
    public static String getImageType(InputStream in) {
        if (in == null) {
            return null;
        }
        try {
            byte[] bytes = new byte[8];
            in.read(bytes);
            return getImageType(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取图片的类型信息
     *
     * @param bytes 2~8 byte at beginning of the image file
     * @return image mimetype or null if the file is not image
     */
    public static String getImageType(byte[] bytes) {
        if (isJPEG(bytes)) {
            return "image/jpeg";
        }
        if (isGIF(bytes)) {
            return "image/gif";
        }
        if (isPNG(bytes)) {
            return "image/png";
        }
        if (isBMP(bytes)) {
            return "application/x-bmp";
        }
        return null;
    }

    private static boolean isJPEG(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
    }

    private static boolean isGIF(byte[] b) {
        if (b.length < 6) {
            return false;
        }
        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    private static boolean isPNG(byte[] b) {
        if (b.length < 8) {
            return false;
        }
        return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
                && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10);
    }

    private static boolean isBMP(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == 0x42) && (b[1] == 0x4d);
    }

    /**
     * 获取图片路径
     *
     * @param uri
     * @param context
     * @return
     */
    public static String getImagePath(Uri uri, Activity context) {

        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String ImagePath = cursor.getString(columIndex);
            cursor.close();
            return ImagePath;
        }

        return uri.toString();
    }

    static Bitmap mbitmap = null;

    /**
     * @param uri
     * @param context E-mail:mr.huangwenwei@gmail.com
     */
    public static Bitmap loadPicasaImageFromGalley(final Uri uri,
                                                   final Activity context) {

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int columIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (columIndex != -1) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            mbitmap = android.provider.MediaStore.Images.Media
                                    .getBitmap(context.getContentResolver(),
                                            uri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
            cursor.close();
            return mbitmap;
        } else
            return null;
    }


}
