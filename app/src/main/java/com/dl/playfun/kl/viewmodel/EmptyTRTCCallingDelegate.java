package com.dl.playfun.kl.viewmodel;

import android.util.Log;

import com.tencent.liteav.trtccalling.model.TRTCCallingDelegate;
import com.tencent.trtc.TRTCCloudDef;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmptyTRTCCallingDelegate implements TRTCCallingDelegate {
    private static final String TAG = "trtcJoy";

    @Override
    public void onError(int code, String msg) {
        Log.e(TAG, "onError: " + code + " " + msg);
    }

    @Override
    public void onInvited(String sponsor, List<String> userIdList, boolean isFromGroup, int callType) {
        Log.i(TAG, "onInvited: " + sponsor);
    }

    @Override
    public void onGroupCallInviteeListUpdate(List<String> userIdList) {
        Log.i(TAG, "onGroupCallInviteeListUpdate: " + userIdList.toString());
    }

    @Override
    public void onUserEnter(String userId) {
        Log.i(TAG, "onUserEnter: " + userId);
    }

    @Override
    public void onUserLeave(String userId) {
        Log.i(TAG, "onUserLeave: " + userId);
    }

    @Override
    public void onReject(String userId) {
        Log.i(TAG, "onReject: " + userId);
    }

    @Override
    public void onNoResp(String userId) {
        Log.i(TAG, "onNoResp: " + userId);
    }

    @Override
    public void onLineBusy(String userId) {
        Log.i(TAG, "onLineBusy: " + userId);
    }

    @Override
    public void onCallingCancel() {
        Log.i(TAG, "onCallingCancel: ");
    }

    @Override
    public void onCallingTimeout() {
        Log.i(TAG, "onCallingTimeout: ");
    }

    @Override
    public void onCallEnd() {
        Log.i(TAG, "onCallEnd: ");
    }

    @Override
    public void onUserVideoAvailable(String userId, boolean isVideoAvailable) {
        Log.i(TAG, "onUserVideoAvailable: " + userId + ", " + isVideoAvailable);
    }

    @Override
    public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {
        Log.i(TAG, "onUserAudioAvailable: ");
    }

    @Override
    public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
//        Log.i(TAG, "onUserVoiceVolume: ");
    }

    @Override
    public void onNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, ArrayList<TRTCCloudDef.TRTCQuality> remoteQuality) {
        Log.i(TAG, "onNetworkQuality: ");
    }

    @Override
    public void onSwitchToAudio(boolean success, String message) {
        Log.i(TAG, "onSwitchToAudio: ");
    }

    @Override
    public void onTryToReconnect() {
        Log.i(TAG, "onTryToReconnect: ");
    }
}
