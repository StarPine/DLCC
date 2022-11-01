package com.fine.friendlycc.viewadapter;

import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.util.Timer;
import java.util.TimerTask;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class ViewAdapter {

    static float startX = 0;
    static float startY = 0;
    static Timer timer = null;
    private static boolean isLongClickModule = false;

    @BindingAdapter({"htmlText"})
    public static void setHtmlText(TextView textView, String text) {
        if (textView == null) {
            return;
        }
        if (text == null) {
            textView.setText("");
        } else {
            textView.setText(Html.fromHtml(text));
        }
    }

    @BindingAdapter(value = {"onLongTouchCommand"}, requireAll = false)
    public static void onTouchCommand(View view, final BindingCommand<Boolean> onTouchCommand) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (onTouchCommand != null) {
                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            startX = ev.getX();
                            startY = ev.getY();
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    isLongClickModule = true;
                                    onTouchCommand.execute(true);
                                }
                            }, 500); // 按下时长设置
                            break;
                        case MotionEvent.ACTION_MOVE:
//                            double deltaX = Math.sqrt((ev.getX() - startX) * (ev.getX() - startX) + (ev.getY() - startY) * (ev.getY() - startY));
//                            if (deltaX > 20 && timer != null) { // 移动大于20像素
//                                timer.cancel();
//                                timer = null;
//                            }
//                            if (isLongClickModule) {
//                                //添加你长按之后的方法
//                                //getDrawingXY();
//                                timer = null;
//
//                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (isLongClickModule) {
                                onTouchCommand.execute(false);
                            }
                            isLongClickModule = false;
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            break;
                        default:
                            isLongClickModule = false;
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                    }

                    return true;

                }
                return true;
            }
        });
    }

}
