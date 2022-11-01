package com.fine.friendlycc.ui.mine.invitationcode;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.viewmodel.BaseViewModel;

/**
 * @author wulei
 */
public class InvitationCodeViewModel extends BaseViewModel<AppRepository> {


    public InvitationCodeViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);

    }
}