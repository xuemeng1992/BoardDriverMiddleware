package com.rocktech.boarddriver.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            View view = inflater.inflate(getContentViewLayoutID(), null);
            ButterKnife.bind(this, view);
            initView();
            addListener();
            initDatas();
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }


    protected void addListener() {

    }


    /**
     * 初始化数据
     */
    protected void initDatas() {

    }

    /**
     * 初始化View
     */
    protected void initView() {

    }


    /**
     * 设置ContentView
     *
     * @return
     */
    protected abstract int getContentViewLayoutID();


}
