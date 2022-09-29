package com.dl.playfun.utils;

import android.net.Uri;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.manager.OSSWrapper;
import com.dl.playfun.videocompressor.VideoCompress;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import top.zibin.luban.Luban;

/**
 * @author litchi
 */
public class FileUploadUtils {

    public static final int FILE_TYPE_IMAGE = 1;
    public static final int FILE_TYPE_VIDEO = 2;
    public static final int FILE_TYPE_AUDIO = 3;

    public static String ossUploadFile(String filePath, int fileType) throws Exception {
        return ossUploadFile(null, fileType, filePath, null);
    }

    public static String ossUploadFile(String directory, int fileType, String filePath) throws Exception {
        return ossUploadFile(directory, fileType, filePath, null);
    }

    public static String ossUploadFile(String directory, int fileType, String filePath, FileUploadProgressListener fileUploadProgressListener) throws Exception {
        if (filePath == null) {
            return null;
        }
        if (fileType == FILE_TYPE_IMAGE) {
            if (filePath.toLowerCase().endsWith(".jpg") || filePath.toLowerCase().endsWith(".jpeg") || filePath.toLowerCase().endsWith(".png")) {
                List<File> list = Luban.with(AppContext.instance().getApplicationContext()).load(Uri.fromFile(new File(filePath))).setTargetDir(AppContext.instance().getCacheDir().getAbsolutePath()).get();
                if (list == null || list.isEmpty()) {
                    return null;
                }
                if (StringUtils.isEmpty(directory)) {
                    directory = "";
                }
                filePath = list.get(0).getAbsolutePath();
            }
        } else if (fileType == FILE_TYPE_VIDEO) {
            String outPath = String.format("%s/%s", AppContext.instance().getCacheDir().getAbsolutePath(), getFileName(filePath));

            final CountDownLatch latch = new CountDownLatch(1);
            final boolean[] success = {false};
            VideoCompress.compressVideoMedium(filePath, outPath, new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                    System.out.println();
                }

                @Override
                public void onSuccess() {
                    success[0] = true;
                    latch.countDown();
                }

                @Override
                public void onFail() {
                    success[0] = false;
                    latch.countDown();
                }

                @Override
                public void onProgress(float percent) {
                    fileUploadProgressListener.fileCompressProgress((int) percent);
                }
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (success[0]) {
                filePath = outPath;
            }
        }
        OSSClient ossClient = OSSWrapper.sharedWrapper().getClient();
        String key = String.format("images/%s%s", directory, getFileName(filePath));
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(AppConfig.BUCKET_NAME, key, filePath);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback((request, currentSize, totalSize) -> {
            if (fileUploadProgressListener != null) {
                fileUploadProgressListener.fileUploadProgress((int) (((float) currentSize / (float) totalSize) * 100));
            }
        });

        PutObjectResult putObjectResult = ossClient.putObject(put);
        if (putObjectResult.getStatusCode() != 200) {
            throw new ClientException("upload failed:" + putObjectResult.getStatusCode());
        }
        return key;
    }

    public static String ossUploadFileVideo(String directory, int fileType, String filePath, FileUploadProgressListener fileUploadProgressListener) throws Exception {
        if (filePath == null) {
            return null;
        }
        if (fileType == FILE_TYPE_VIDEO) {
            String outPath = String.format("%s/%s", AppContext.instance().getCacheDir().getAbsolutePath(), getFileName(filePath));

            final CountDownLatch latch = new CountDownLatch(1);
            final boolean[] success = {false};
            VideoCompress.compressVideoMedium(filePath, outPath, new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                    System.out.println();
                }

                @Override
                public void onSuccess() {
                    success[0] = true;
                    latch.countDown();
                }

                @Override
                public void onFail() {
                    success[0] = false;
                    latch.countDown();
                }

                @Override
                public void onProgress(float percent) {
                    fileUploadProgressListener.fileCompressProgress((int) percent);
                }
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (success[0]) {
                filePath = outPath;
            }
        }
        OSSClient ossClient = OSSWrapper.sharedWrapper().getClient();
        String key = String.format("video/%s%s", directory, getFileName(filePath));
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(AppConfig.BUCKET_NAME, key, filePath);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback((request, currentSize, totalSize) -> {
            if (fileUploadProgressListener != null) {
                fileUploadProgressListener.fileUploadProgress((int) (((float) currentSize / (float) totalSize) * 100));
            }
        });

        PutObjectResult putObjectResult = ossClient.putObject(put);
        if (putObjectResult.getStatusCode() != 200) {
            throw new ClientException("upload failed:" + putObjectResult.getStatusCode());
        }
        return key;
    }

    public static String ossUploadFileAudio(String directory, int fileType, String filePath, FileUploadProgressListener fileUploadProgressListener) throws Exception {
        if (filePath == null) {
            return null;
        }
        OSSClient ossClient = OSSWrapper.sharedWrapper().getClient();
        String key = String.format("audio/%s%s", directory, getFileName(filePath));
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(AppConfig.BUCKET_NAME, key, filePath);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback((request, currentSize, totalSize) -> {
            if (fileUploadProgressListener != null) {
                fileUploadProgressListener.fileUploadProgress((int) (((float) currentSize / (float) totalSize) * 100));
            }
        });

        PutObjectResult putObjectResult = ossClient.putObject(put);
        if (putObjectResult.getStatusCode() != 200) {
            throw new ClientException("upload failed:" + putObjectResult.getStatusCode());
        }
        return key;
    }

    /**
     * 自定义文件上传
     * @param fileType 文件类型
     * @param filePath 文件路径
     * @param directory oss文件夹位置
     * @param fileUploadProgressListener 上传进度回调
     * @return
     * @throws Exception
     */
    public static String ossUploadFileCustom(final int fileType,String filePath,String directory,FileUploadProgressListener fileUploadProgressListener) throws Exception{
        if(filePath == null || directory == null){
            return null;
        }
        if (fileType == FILE_TYPE_IMAGE) {
            List<File> list = Luban.with(AppContext.instance().getApplicationContext()).load(Uri.fromFile(new File(filePath))).setTargetDir(AppContext.instance().getCacheDir().getAbsolutePath()).get();
            if (list == null || list.isEmpty()) {
                return null;
            }
            if (StringUtils.isEmpty(directory)) {
                directory = "";
            }
            filePath = list.get(0).getAbsolutePath();
        }else if (fileType == FILE_TYPE_VIDEO) {
            String outPath = String.format("%s/%s", AppContext.instance().getCacheDir().getAbsolutePath(), getFileName(filePath));

            final CountDownLatch latch = new CountDownLatch(1);
            final boolean[] success = {false};
            VideoCompress.compressVideoMedium(filePath, outPath, new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                    System.out.println();
                }

                @Override
                public void onSuccess() {
                    success[0] = true;
                    latch.countDown();
                }

                @Override
                public void onFail() {
                    success[0] = false;
                    latch.countDown();
                }

                @Override
                public void onProgress(float percent) {
                    if(fileUploadProgressListener!=null){
                        fileUploadProgressListener.fileCompressProgress((int) percent);
                    }
                }
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (success[0]) {
                filePath = outPath;
            }
        }else{
            throw new InstantiationException("error fileType is not ");
        }
        OSSClient ossClient = OSSWrapper.sharedWrapper().getClient();
        String key = directory+"/"+getFileName(filePath);
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(AppConfig.BUCKET_NAME, key, filePath);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback((request, currentSize, totalSize) -> {
            if (fileUploadProgressListener != null) {
                fileUploadProgressListener.fileUploadProgress((int) (((float) currentSize / (float) totalSize) * 100));
            }
        });

        PutObjectResult putObjectResult = ossClient.putObject(put);
        if (putObjectResult.getStatusCode() != 200) {
            throw new ClientException("upload failed:" + putObjectResult.getStatusCode());
        }
        return key;
    }

    private static String getFileName(String file) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return String.format("%s.%s", uuid, ext(file));
    }

    /**
     * 获取文件扩展名
     *
     * @return
     */
    public static String ext(String filename) {
        int index = filename.lastIndexOf(".");

        if (index == -1) {
            return "";
        }
        String result = filename.substring(index + 1);
        return result;
    }

    public interface FileUploadProgressListener {
        void fileCompressProgress(int progress);

        void fileUploadProgress(int progress);
    }
}
