package com.dl.playfun.ui.message.mediagallery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.dl.playfun.R;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.utils.FileUploadUtils;
import com.dl.playfun.utils.Utils;
import com.dl.playfun.viewmodel.BaseViewModel;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2022/9/19 11:05
 * Description: This is MediaGalleryVideoSettingViewModel
 */
public class MediaGalleryVideoSettingViewModel extends BaseViewModel<AppRepository> {
    public MediaGalleryVideoSettingViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }
    //是否付费
    public ObservableBoolean isPayState = new ObservableBoolean(false);
    //本地文件地址
    public ObservableField<String> srcPath = new ObservableField<>();

    public SingleLiveEvent<Void> settingEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<String> setResultDataEvent = new SingleLiveEvent<>();

    //返回上一页
    public BindingCommand<Void> onBackViewClick = new BindingCommand<>(this::finish);

    public BindingCommand settingClick = new BindingCommand(()->{
        settingEvent.call();
    });

    //确认上传
    public BindingCommand<Void> clickReportFile = new BindingCommand(() -> {
        uploadFileOSS(srcPath.get());
    });

    //上传文件到阿里云
    public void uploadFileOSS(final String filePath){
        Observable.just(filePath)
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map((Function<String, String>) s -> {
                    String fileName = AppConfig.OSS_CUSTOM_FILE_NAME_CHAT +"/"+ Utils.formatYYMMSS.format(new Date());
                    return FileUploadUtils.ossUploadFileCustom(FileUploadUtils.FILE_TYPE_VIDEO, filePath, fileName, new FileUploadUtils.FileUploadProgressListener() {
                        @Override
                        public void fileCompressProgress(int progress) {
                            showProgressHUD(String.format("%ss", progress), progress);
                        }

                        @Override
                        public void fileUploadProgress(int progress) {
                            showProgressHUD(String.format("%ss", progress), progress);
                        }
                    });
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        dismissHUD();
                        if(fileKey!=null){
                            setResultDataEvent.setValue(fileKey);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playfun_upload_failed);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
