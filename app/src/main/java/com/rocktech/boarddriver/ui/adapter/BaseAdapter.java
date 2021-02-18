package com.rocktech.boarddriver.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.rocktech.boarddriver.bean.AssetCodeBean;

import java.util.ArrayList;

public class BaseAdapter extends BaseQuickAdapter<AssetCodeBean, BaseViewHolder> {


    public ICallback iCallback;

    public BaseAdapter(int layoutResId, @Nullable ArrayList<AssetCodeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AssetCodeBean item) {

    }

    public interface ICallback {
        void IRefresh();
    }

    public void setiCallback(ICallback iCallback) {
        this.iCallback = iCallback;
    }
}
