package com.fine.friendlycc.widget.custom;

import androidx.databinding.BindingAdapter;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * Author: 彭石林
 * Time: 2021/8/10 19:38
 * Description: This is BindingFowLayout
 */
public class BindingFowLayout {
    @BindingAdapter(value = {"onRefreshCommand", "onLoadMoreCommand"}, requireAll = false)
    public static void onRefreshAndLoadMoreCommand(SmartRefreshLayout layout, final BindingRecyclerViewAdapter adapter, final BindingCommand onLoadMoreCommand) {

    }
}
