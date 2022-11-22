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
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.fine.friendlycc.bean.EvaluateItemBean;
import com.fine.friendlycc.ui.dialog.adapter.CommitEvaluateAdapter;
import com.fine.friendlycc.R;

import java.util.List;

/**
 * 匿名评价对话框
 *
 * @author wulei
 */
public class CommitEvaluateDialog extends BaseDialogFragment implements View.OnClickListener {

    private final List<EvaluateItemBean> evaluateList;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private Button btnCommit;
    private ImageView ivClose;
    private CommitEvaluateDialogListener commitEvaluateDialogListener;
    private CommitEvaluateAdapter commitEvaluateAdapter;
    private int type;
    private EvaluateItemBean selectedEvaluateItemEntity;

    public CommitEvaluateDialog(List<EvaluateItemBean> evaluateList) {
        this.type = type;
        this.evaluateList = evaluateList;
    }

    public CommitEvaluateDialogListener getCommitEvaluateDialogListener() {
        return commitEvaluateDialogListener;
    }

    public void setCommitEvaluateDialogListener(CommitEvaluateDialogListener commitEvaluateDialogListener) {
        this.commitEvaluateDialogListener = commitEvaluateDialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = view.findViewById(R.id.tv_title);
        recyclerView = view.findViewById(R.id.recycler_view);
        btnCommit = view.findViewById(R.id.btn_commit);
        ivClose = view.findViewById(R.id.iv_dialog_close);

        ivClose.setOnClickListener(this);
        btnCommit.setOnClickListener(this);

        GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManage);

        commitEvaluateAdapter = new CommitEvaluateAdapter(recyclerView);
        commitEvaluateAdapter.setOnItemClickListener((v, position) -> {
            selectedEvaluateItemEntity = evaluateList.get(position);
            for (EvaluateItemBean evaluateItemEntity : evaluateList) {
                evaluateItemEntity.setSelected(false);
            }
            evaluateList.get(position).setSelected(true);
            commitEvaluateAdapter.notifyDataSetChanged();
        });
        recyclerView.setAdapter(commitEvaluateAdapter);

        commitEvaluateAdapter.setData(this.evaluateList);

    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setLayout(mWidthAndHeight[0] - ConvertUtils.dp2px(10), ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_commit_evaluate;
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
        } else if (view.getId() == R.id.btn_commit) {
            if (commitEvaluateDialogListener != null) {
                commitEvaluateDialogListener.onCommitClick(this, selectedEvaluateItemEntity);
            }
        }
    }

    public interface CommitEvaluateDialogListener {
        void onCommitClick(CommitEvaluateDialog dialog, EvaluateItemBean entity);
    }
}