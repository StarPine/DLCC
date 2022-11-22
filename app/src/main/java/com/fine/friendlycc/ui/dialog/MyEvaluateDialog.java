package com.fine.friendlycc.ui.dialog;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.fine.friendlycc.bean.EvaluateItemBean;
import com.fine.friendlycc.ui.dialog.adapter.MyEvaluateAdapter;
import com.fine.friendlycc.R;

import java.util.List;

/**
 * 评价对话框
 *
 * @author wulei
 */
public class MyEvaluateDialog extends BaseDialogFragment implements View.OnClickListener {

    public static final int TYPE_MYSELF = 1;
    public static final int TYPE_USER_MALE = 2;
    public static final int TYPE_USER_FEMALE = 3;
    private final int type;
    private final List<EvaluateItemBean> evaluateList;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private Button btnEvaluate;
    private TextView tvTip;
    private ImageView ivClose;
    private MyEvaluateAdapter myEvaluateAdapter;
    private EvaluateDialogListener evaluateDialogListener;

    public MyEvaluateDialog(int type, List<EvaluateItemBean> evaluateList) {
        this.type = type;
        this.evaluateList = evaluateList;
    }

    public EvaluateDialogListener getEvaluateDialogListener() {
        return evaluateDialogListener;
    }

    public void setEvaluateDialogListener(EvaluateDialogListener evaluateDialogListener) {
        this.evaluateDialogListener = evaluateDialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = view.findViewById(R.id.tv_title);
        recyclerView = view.findViewById(R.id.recycler_view);
        btnEvaluate = view.findViewById(R.id.btn_evaluate);
        tvTip = view.findViewById(R.id.tv_tip);
        ivClose = view.findViewById(R.id.iv_dialog_close);

        ivClose.setOnClickListener(this);
        btnEvaluate.setOnClickListener(this);
        tvTip.setOnClickListener(this);

        GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManage);

        myEvaluateAdapter = new MyEvaluateAdapter(recyclerView);
        recyclerView.setAdapter(myEvaluateAdapter);
        myEvaluateAdapter.setData(this.evaluateList);

        if (type == TYPE_USER_MALE) {
            tvTitle.setText(R.string.playcc_he_true_evaluation);
            btnEvaluate.setVisibility(View.VISIBLE);
            tvTip.setVisibility(View.VISIBLE);
        } else if (type == TYPE_USER_FEMALE) {
            tvTitle.setText(R.string.playcc_he_true_evaluation);
            btnEvaluate.setVisibility(View.VISIBLE);
            tvTip.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setText(R.string.playcc_your_true_evaluation);
            btnEvaluate.setVisibility(View.GONE);
            tvTip.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setLayout(mWidthAndHeight[0] - ConvertUtils.dp2px(44), ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_my_evaluate;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_dialog_close) {
            dismiss();
        } else if (view.getId() == R.id.btn_evaluate) {
            if (evaluateDialogListener != null) {
                evaluateDialogListener.onEvaluateClick(this);
            }
        } else if (view.getId() == R.id.tv_tip) {
            if (evaluateDialogListener != null) {
                evaluateDialogListener.onAnonymousReportClick(this);
            }
        }
    }

    public interface EvaluateDialogListener {

        void onEvaluateClick(MyEvaluateDialog dialog);

        void onAnonymousReportClick(MyEvaluateDialog dialog);
    }
}