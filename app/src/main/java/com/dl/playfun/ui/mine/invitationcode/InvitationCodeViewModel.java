package com.dl.playfun.ui.mine.invitationcode;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.viewmodel.BaseViewModel;

/**
 * @author wulei
 */
public class InvitationCodeViewModel extends BaseViewModel<AppRepository> {


    public InvitationCodeViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);

    }
}