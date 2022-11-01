package com.fine.friendlycc.ui.message.chatdetail.notepad;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.NoteInfoEntity;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/6/15 18:23
 * 修改备注：
 */
public class NotepadViewModel extends BaseViewModel<AppRepository> {
    public ObservableField<String> notepadTextFlag = new ObservableField<>("0/400");
    public ObservableField<String> notepadText = new ObservableField<>();
    public int userId;

    /**
     * 笔记保存
     */
    public BindingCommand noteSaveOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            putNoteText(userId,notepadText.get());
        }
    });

    public NotepadViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
        notepadTextFlag.set(String.format(application.getString(R.string.notepad_word_count_format), "0"));
    }

    public void getNoteText(int userId) {
        model.getNoteText(userId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<NoteInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<NoteInfoEntity> response) {
                        NoteInfoEntity data = response.getData();
                        notepadText.set(data.getNote());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    public void putNoteText(int userId, String note) {
        model.putNoteText(userId, note)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<NoteInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<NoteInfoEntity> response) {
                        Toast.makeText(AppContext.instance(), R.string.save_success, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }
}
