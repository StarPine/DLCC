package com.fine.friendlycc.ui.dialog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.fine.friendlycc.entity.ThemeItemEntity;
import com.fine.friendlycc.ui.dialog.adapter.ProgramSubjectChooseAdapter;
import com.fine.friendlycc.R;

import java.util.List;

/**
 * 主题选择
 *
 * @author wulei
 */
public class ProgramSubjectChooseDialog extends BaseDialogFragment implements View.OnClickListener {

    private final List<ThemeItemEntity> datas;
    private RecyclerView recyclerView;
    private ImageView ivClose;
    private ProgramSubjectChooseAdapter adapter;
    private ProgramSubjectChooseDialogListener programSubjectChooseDialogListener;

    public ProgramSubjectChooseDialog(List<ThemeItemEntity> themes) {
        this.datas = themes;
    }

    public ProgramSubjectChooseDialogListener getProgramSubjectChooseDialogListener() {
        return programSubjectChooseDialogListener;
    }

    public void setProgramSubjectChooseDialogListener(ProgramSubjectChooseDialogListener programSubjectChooseDialogListener) {
        this.programSubjectChooseDialogListener = programSubjectChooseDialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Glide.with(getContext())
//                .load(getResources().getDrawable(R.drawable.login_top))
//                .apply(RequestOptions.bitmapTransform(new Blur(25,8)))
//                .into(view);
//        Blurry.with(getContext()).radius(25).sampling(2).onto(view.findViewById(R.id.iv_content));
//        ImageView ivContent = view.findViewById(R.id.iv_content);
//        Blurry.with(getContext()).capture(view).into(ivContent);

        View mainContainerView = mActivity.getWindow().getDecorView();
        mainContainerView.setDrawingCacheEnabled(true);
        mainContainerView.buildDrawingCache();
        Bitmap bp = Bitmap.createBitmap(mainContainerView.getDrawingCache(), 0, 0, mainContainerView.getMeasuredWidth(),
                mainContainerView.getMeasuredHeight());

//        mainContainerView.setDrawingCacheEnabled(false);
//        mainContainerView.destroyDrawingCache();

//        Glide.with(this).load(bp)
//                .apply(bitmapTransform(new BlurTransformation(100)))
//                .into((ImageView) view.findViewById(R.id.iv_content));
//
//        Blurry.with(this.getContext())
//                .radius(5)//模糊半径
//                .sampling(8)//缩放大小，先缩小再放大
//                .color(Color.argb(78, 255, 255, 255))//颜色
//                .async()//是否异步
//                .animate(300)
//                .from(bp)//传入bitmap
//                .into(view.findViewById(R.id.iv_content));//显示View
        recyclerView = view.findViewById(R.id.recycler_view);
        ivClose = view.findViewById(R.id.iv_dialog_close);

        ivClose.setOnClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new ProgramSubjectChooseAdapter(recyclerView);
        adapter.setData(this.datas);
        recyclerView.setAdapter(adapter);

        adapter.setProgramSubjectChooseAdapterListener(itemEntity -> {
            if (programSubjectChooseDialogListener != null) {
                programSubjectChooseDialogListener.onItemClick(ProgramSubjectChooseDialog.this, itemEntity);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = ScreenUtils.getAppScreenWidth();
        mWindow.setGravity(Gravity.CENTER);
//        mWindow.setWindowAnimations(R.style.TopAnimation);
        mWindow.setLayout(Double.valueOf(width * 0.8).intValue(), ViewGroup.LayoutParams.MATCH_PARENT);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_program_subject_choose_toast;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_dialog_close) {
            this.dismiss();
        }
    }

    public interface ProgramSubjectChooseDialogListener {
        void onItemClick(ProgramSubjectChooseDialog dialog, ThemeItemEntity itemEntity);
    }
}
