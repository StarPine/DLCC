package com.fine.friendlycc.ui.mine.level;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentLevelEquityBinding;
import com.fine.friendlycc.entity.LevelCoinOptionInfo;
import com.fine.friendlycc.entity.LevelCoinSelectInfo;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.widget.AppBarStateChangeListener;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;


/**
 * Author: 彭石林
 * Time: 2022/6/18 18:16
 * Description: This is LevelEquityFragment
 */
public class LevelEquityFragment extends BaseFragment<FragmentLevelEquityBinding, LevelEquityViewModel> {

    private static final String TAG = "LevelEquityFragment";
    private PagerSnapHelper pagingScrollHelper;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBarUtils.setupStatusBar(this, false, true);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_level_equity;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LevelEquityViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(LevelEquityViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        binding.appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    binding.toolBarTitleView.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    binding.toolBarTitleView.setVisibility(View.VISIBLE);
                }

            }
        });
        //文字调整进度条宽度测量
        binding.seekbarMessage.setMeasureWidthCallBack((width,width2) -> {
            ViewGroup.LayoutParams layoutParams = binding.seekbarMessageView.getLayoutParams();
            layoutParams.width = width2;
            binding.seekbarMessageView.setLayoutParams(layoutParams);
        });
        //文字调整进度条宽度测量
        binding.seekbarAudio.setMeasureWidthCallBack((width,width2) -> {
            ViewGroup.LayoutParams layoutParams = binding.seekbarAudioView.getLayoutParams();
            layoutParams.width = width2;
            binding.seekbarAudioView.setLayoutParams(layoutParams);
        });
        //文字调整进度条宽度测量
        binding.seekbarVideo.setMeasureWidthCallBack((width,width2) -> {
            ViewGroup.LayoutParams layoutParams = binding.seekbarVideoView.getLayoutParams();
            layoutParams.width = width2;
            binding.seekbarVideoView.setLayoutParams(layoutParams);
        });
        binding.seekbarMessageView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.seekbarMessage.setProgress(seekBar.getProgress());
                LevelCoinOptionInfo levelChatSelectInfo = viewModel.levelChatSelectInfo.getOptions().get(progress);
                viewModel.Tv_ChatMoney.set(String.valueOf(levelChatSelectInfo.getProfits()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //拖动条停止拖动的时候调用
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                LevelCoinSelectInfo levelChatSelectInfo = viewModel.levelChatSelectInfo;
                if (levelChatSelectInfo.getEnableChange() == 0) {
                    return;
                }
                if (levelChatSelectInfo.getSelectedCoins().equals(levelChatSelectInfo.getOptions().get(progress).getCoins())) {
                    viewModel.clickChatSubDisplay.set(false);
                } else {
                    viewModel.clickChatIndex = seekBar.getProgress();
                    viewModel.clickChatSubDisplay.set(true);
                }
            }
        });
        binding.seekbarAudioView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.seekbarAudio.setProgress(seekBar.getProgress());
                LevelCoinOptionInfo levelAudioSelectInfo = viewModel.levelAudioSelectInfo.getOptions().get(progress);
                viewModel.Tv_VoiceMoney.set(String.valueOf(levelAudioSelectInfo.getProfits()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //拖动条停止拖动的时候调用
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                LevelCoinSelectInfo levelAudioSelectInfo = viewModel.levelAudioSelectInfo;
                if (levelAudioSelectInfo.getEnableChange() == 0) {
                    return;
                }
                if (levelAudioSelectInfo.getSelectedCoins().equals(levelAudioSelectInfo.getOptions().get(progress).getCoins())) {
                    viewModel.clickAudioSubDisplay.set(false);
                } else {
                    viewModel.clickAudioIndex = seekBar.getProgress();
                    viewModel.clickAudioSubDisplay.set(true);
                }
            }
        });
        binding.seekbarVideoView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.seekbarVideo.setProgress(seekBar.getProgress());
                LevelCoinOptionInfo levelCoinOptionInfo = viewModel.levelVideoSelectInfo.getOptions().get(progress);
                viewModel.Tv_VideoMoney.set(String.valueOf(levelCoinOptionInfo.getProfits()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //拖动条停止拖动的时候调用
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                LevelCoinSelectInfo levelVideoSelectInfo = viewModel.levelVideoSelectInfo;
                if (levelVideoSelectInfo.getEnableChange() == 0) {
                    return;
                }
                if (levelVideoSelectInfo.getSelectedCoins().equals(levelVideoSelectInfo.getOptions().get(progress).getCoins())) {
                    viewModel.clickVideoSubDisplay.set(false);
                } else {
                    viewModel.clickVideoIndex = seekBar.getProgress();
                    viewModel.clickVideoSubDisplay.set(true);
                }
            }
        });

        // 滑动时使父布局不响应事件
        //binding.rcvBanner.setNestedScrollingEnabled(false);
        //banner图片滑动回调 仿pageview实现效果
        pagingScrollHelper = new PagerSnapHelper() {
            // 在 Adapter的 onBindViewHolder 之后执行
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                // TODO 找到对应的Index
                if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
                    return RecyclerView.NO_POSITION;
                }

                final View currentView = findSnapView(layoutManager);

                if (currentView == null) {
                    return RecyclerView.NO_POSITION;
                }

                LinearLayoutManager myLayoutManager = (LinearLayoutManager) layoutManager;

                int position1 = myLayoutManager.findFirstVisibleItemPosition();
                int position2 = myLayoutManager.findLastVisibleItemPosition();

                int currentPosition = layoutManager.getPosition(currentView);

                if (velocityX > 400) {
                    currentPosition = position2;
                } else if (velocityX < 400) {
                    currentPosition = position1;
                }

                if (currentPosition == RecyclerView.NO_POSITION) {
                    return RecyclerView.NO_POSITION;
                }
                if (currentPosition >= 0) {
                    if (viewModel.listLevelTips != null) {
                        viewModel.currentTextHint.set(viewModel.listLevelTips.get(currentPosition).getTips());
                    }
                    viewModel.titleRcvItemClick(currentPosition, false);
                }
                return currentPosition;
            }

            // 在 Adapter的 onBindViewHolder 之后执行
            @Nullable
            @Override
            public View findSnapView(RecyclerView.LayoutManager layoutManager) {
                // TODO 找到对应的View
                return super.findSnapView(layoutManager);
            }
        };
        pagingScrollHelper.attachToRecyclerView(binding.rcvBanner);

        viewModel.loadDataInfo();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.scrollBannerIndex.observe(this, integer -> {
            if (integer != null) {
                if (pagingScrollHelper != null) {
                    //pagingScrollHelper.(integer);
                    if (viewModel.listLevelTips != null) {
                        viewModel.currentTextHint.set(viewModel.listLevelTips.get(integer).getTips());
                    }
                }
                binding.rcvBanner.scrollToPosition(integer);
            }
        });
        viewModel.uc.levelInfoPageInfoEvent.observe(this, userLevelPageInfoEntity -> {
            //文字调节
            LevelCoinSelectInfo levelChatSelectInfo = userLevelPageInfoEntity.getChatSelectInfo();
            viewModel.levelChatSelectInfo = levelChatSelectInfo;
            int checkChatIdx = 0;
            int checkChatSize = levelChatSelectInfo.getOptions().size();
            int checkChatMax = 0;
            ArrayList<String> checkChatList = new ArrayList<>();
            for (int i = 0; i < checkChatSize; i++) {
                if (levelChatSelectInfo.getSelectedCoins().equals(levelChatSelectInfo.getOptions().get(i).getCoins())) {
                    checkChatIdx = i;
                }
                checkChatList.add(String.valueOf(levelChatSelectInfo.getOptions().get(i).getCoins()));
                if (levelChatSelectInfo.getOptions().get(i).getCoins() <= levelChatSelectInfo.getUnlockedCoins()) {
                    checkChatMax = i;
                }
            }
            binding.seekbarMessageView.setMax(checkChatMax);
            binding.seekbarMessageView.setProgress(checkChatIdx);
            binding.seekbarMessage.initData(checkChatList, checkChatMax);
            binding.seekbarMessage.setProgress(checkChatIdx);

            //语音调节
            LevelCoinSelectInfo levelAudioSelectInfo = userLevelPageInfoEntity.getAudioSelectInfo();
            viewModel.levelAudioSelectInfo = levelAudioSelectInfo;
            int checkAudioIdx = 0;
            int checkAudioMax = 0;
            int checkAudioSize = levelAudioSelectInfo.getOptions().size();
            ArrayList<String> checkAudioList = new ArrayList<>();
            for (int i = 0; i < checkAudioSize; i++) {
                if (levelAudioSelectInfo.getSelectedCoins().equals(levelAudioSelectInfo.getOptions().get(i).getCoins())) {
                    checkAudioIdx = i;
                }
                checkAudioList.add(String.valueOf(levelAudioSelectInfo.getOptions().get(i).getCoins()));
                if (levelAudioSelectInfo.getOptions().get(i).getCoins() <= levelAudioSelectInfo.getUnlockedCoins()) {
                    checkAudioMax = i;
                }
            }
            binding.seekbarAudio.initData(checkAudioList, checkAudioMax);
            binding.seekbarAudio.setProgress(checkAudioIdx);
            binding.seekbarAudioView.setMax(checkAudioMax);
            binding.seekbarAudioView.setProgress(checkAudioIdx);
            //视频调节
            LevelCoinSelectInfo levelVideoSelectInfo = userLevelPageInfoEntity.getVideoSelectInfo();
            viewModel.levelVideoSelectInfo = levelVideoSelectInfo;
            int checkVideoIdx = 0;
            int checkVideoMax = 0;
            int checkVideoSize = levelVideoSelectInfo.getOptions().size();
            ArrayList<String> checkVideoList = new ArrayList<>();
            for (int i = 0; i < checkVideoSize; i++) {
                if (levelVideoSelectInfo.getSelectedCoins().equals(levelVideoSelectInfo.getOptions().get(i).getCoins())) {
                    checkVideoIdx = i;
                }
                checkVideoList.add(String.valueOf(levelVideoSelectInfo.getOptions().get(i).getCoins()));
                if (levelVideoSelectInfo.getOptions().get(i).getCoins() <= levelVideoSelectInfo.getUnlockedCoins()) {
                    checkVideoMax = i;
                }
            }
            binding.seekbarVideo.initData(checkVideoList, checkVideoMax);
            binding.seekbarVideo.setProgress(checkVideoIdx);
            binding.seekbarVideoView.setMax(checkVideoMax);
            binding.seekbarVideoView.setProgress(checkVideoIdx);
            String text1 = levelChatSelectInfo.getFirstCoins() + "~" + levelChatSelectInfo.getUnlockedCoins();
            String text2 = levelAudioSelectInfo.getFirstCoins() + "~" + levelAudioSelectInfo.getUnlockedCoins();
            String text3 = levelVideoSelectInfo.getFirstCoins() + "~" + levelVideoSelectInfo.getUnlockedCoins();
            String textHint = StringUtils.getString(R.string.fragment_level_text7);
            viewModel.Tv_ChatInterval.set(text1 + textHint);
            viewModel.Tv_VoiceInterval.set(text2 + textHint);
            viewModel.Tv_VideoInterval.set(text3 + textHint);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AutoSizeUtils.closeAdapt(getResources());
    }
}
