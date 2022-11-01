package com.fine.friendlycc.ui.message.chatdetail.notepad;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.ActivityNotepadBinding;
import com.fine.friendlycc.ui.base.BaseActivity;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.widget.BasicToolbar;

import me.tatarka.bindingcollectionadapter2.BR;

/**
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/6/15 17:23
 * 修改备注：
 */
public class NotepadActivity extends BaseActivity<ActivityNotepadBinding, NotepadViewModel> implements BasicToolbar.ToolbarListener{

    private Integer toUserDataId = null;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_notepad;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public NotepadViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(NotepadViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public void initParam() {
        super.initParam();
        Bundle bundle = this.getIntent().getExtras();
        toUserDataId = bundle.getInt("toUserId");
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.userId = toUserDataId;
        viewModel.getNoteText(toUserDataId);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.basicToolbar.setToolbarListener(this);
        binding.editNotepad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.notepadTextFlag.set(String.format(getString(R.string.notepad_word_count_format),binding.editNotepad.getText().toString().length()+""));
                viewModel.notepadText.set(binding.editNotepad.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackClick(BasicToolbar toolbar) {
        this.finish();
    }
}
