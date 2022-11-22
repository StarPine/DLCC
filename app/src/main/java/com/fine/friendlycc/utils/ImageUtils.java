package com.fine.friendlycc.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import com.fine.friendlycc.app.CCApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.Locale;
import java.util.UUID;

public class ImageUtils {

    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * 将Base64编码转换为图片
     *
     * @param base64Str
     * @param path
     * @return true
     */
    public static boolean base64ToFile(String base64Str, String path) {
        byte[] data = Base64.decode(base64Str, Base64.NO_WRAP);
        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                //调整异常数据
                data[i] += 256;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
            os.flush();
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * bitmap转base64
     * */
    private static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getVideoThumbnail(String videoPath, int kind, int width, int height) {
        String filePath = "";
        try {
            // 获取视频的缩略图
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
            if (width > 0 && height > 0) {
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            }
            if (bitmap == null) {
                return null;
            }
            File file = new File(CCApplication.instance().getCacheDir().getAbsolutePath() + "/" + UUID.randomUUID().toString() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();

            filePath = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public static String getVideoThumbnailBase64(String videoPath) {
        String base = null;
        try {
            // 获取视频的缩略图
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
            if (bitmap == null) {
                return null;
            }
            return bitmapToBase64(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base;
    }

    public static synchronized String drawableToByte(Drawable drawable) {
        if (drawable != null) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;

            // 创建一个字节数组输出流,流的大小为size
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // 将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();

            String icon = Base64.encodeToString(imagedata, Base64.DEFAULT);
            return icon;
        }
        return null;
    }

    public static synchronized Drawable byteToDrawable(String icon) {

        byte[] img = Base64.decode(icon.getBytes(), Base64.DEFAULT);
        Bitmap bitmap;
        if (img != null) {


            bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bitmap);

            return drawable;
        }
        return null;

    }

    /**
     * 时间转换
     */
    public static String conversionTime(int time) {
        Formatter formatter = new Formatter(Locale.getDefault());
        // 总秒数
        int totalSeconds = time / 1000;
        // 小时数
        int hours = totalSeconds / 3600;
        // 分钟数
        int minutes = (totalSeconds / 60) % 60;
        // 秒数
        int seconds = totalSeconds % 60;
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}
