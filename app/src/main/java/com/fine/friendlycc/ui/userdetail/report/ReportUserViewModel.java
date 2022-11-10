package com.fine.friendlycc.ui.userdetail.report;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.ConfigItemEntity;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.utils.ListUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class ReportUserViewModel extends BaseViewModel<AppRepository> {

    private final List<ConfigItemEntity> reportList = new ArrayList<>();
    public List<String> images = new ArrayList<>();
    public List<String> filePaths = new ArrayList<>();
    public String type;
    public int count = 0;
    public ObservableField<String> description = new ObservableField<>("");
    public BindingRecyclerViewAdapter<ReportItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<ReportItemViewModel> reportItemViewModels = new ObservableArrayList<>();
    public ItemBinding<ReportItemViewModel> reportItemViewModelItemBinding = ItemBinding.of(BR.viewModel, R.layout.item_report_reason);
    UIChangeObservable uc = new UIChangeObservable();
    private String reasonId;
    private Integer id;
    //完成按钮的点击事件
    public BindingCommand commitOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (reasonId == null) {
                ToastUtils.showShort(R.string.playcc_report_user_reason);
                return;
            }
            if (type.equals("home")) {
                if (!ListUtils.isEmpty(filePaths)){
                    for (int i = 0; i < filePaths.size(); i++) {
                        count++;
                        uploadAvatar(filePaths.get(i));
                    }
                    return;
                }
            }
            commitReport();
        }
    });

    public ReportUserViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        reportList.addAll(model.readReportReasonConfig());
        if (reportList.size() != 0) {
            for (ConfigItemEntity config : reportList) {
                ReportItemViewModel reportItemViewModel = new ReportItemViewModel(this, config);
                reportItemViewModels.add(reportItemViewModel);
            }
        }
    }

    //提交举报
    public void commitReport() {

        model.report(id, type, reasonId, images, description.get())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_report_success);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                        pop();
                    }
                });
    }

    public void uploadAvatar(String filePath) {
        Observable.just(filePath)
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return FileUploadUtils.ossUploadFile("report/", FileUploadUtils.FILE_TYPE_IMAGE, s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        images.add(fileKey);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(R.string.playcc_upload_failed);
                    }

                    @Override
                    public void onComplete() {
                        count--;
                        if (count == 0){
                            commitReport();
                        }
                        dismissHUD();
                    }
                });
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }

    public void itemClick(int pisition) {
        if (reportItemViewModels != null) {
            for (int i = 0; i < reportItemViewModels.size(); i++) {
                reportItemViewModels.get(i).configItemEntityObservableField.get().setIsChoose(false);
                if (i == pisition) {
                    reportItemViewModels.get(i).configItemEntityObservableField.get().setIsChoose(true);
                    reasonId = String.valueOf(reportItemViewModels.get(i).configItemEntityObservableField.get().getId());
                }
            }
        }
    }

    public class UIChangeObservable {
        public SingleLiveEvent clickitem = new SingleLiveEvent<>();
    }
}