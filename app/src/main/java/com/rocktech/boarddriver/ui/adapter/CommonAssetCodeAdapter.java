package com.rocktech.boarddriver.ui.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.rocktech.boarddriver.R;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.Tools;

import java.util.ArrayList;

public class CommonAssetCodeAdapter extends BaseAdapter {

    public CommonAssetCodeAdapter(int layoutResId, @Nullable ArrayList<AssetCodeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final AssetCodeBean item) {
        item.setRealBoxId(Tools.numberToLetter(helper.getAdapterPosition()));
        //主/副柜
        Spinner spCupboardType = helper.getView(R.id.sp_cupboard_type);
        ArrayAdapter<String> adapterCupboardType = new ArrayAdapter<>(getContext(), R.layout.item_sp_code, Constant.st_cupboard_type);
        adapterCupboardType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCupboardType.setAdapter(adapterCupboardType);
        if (item.getAssetCode().substring(0, 1).equalsIgnoreCase("Z")) {
            spCupboardType.setSelection(0);
        } else {
            spCupboardType.setSelection(1);
        }
        //室内/外
        Spinner spCupboardPosition = helper.getView(R.id.sp_cupboard_position);
        ArrayAdapter<String> adapterCupboardPosition = new ArrayAdapter<>(getContext(), R.layout.item_sp_code, Constant.str_cupboard_position);
        adapterCupboardPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCupboardPosition.setAdapter(adapterCupboardPosition);
        if (item.getAssetCode().substring(6, 7).equals("1")) {
            spCupboardPosition.setSelection(0);
        } else {
            spCupboardPosition.setSelection(1);
        }
        //功能
        Spinner spCupboardFunction = helper.getView(R.id.sp_cupboard_function);
        ArrayAdapter<String> adapterCupboardFunction = new ArrayAdapter<>(getContext(), R.layout.item_sp_code, ConfigureTools.getCupboardFunction(getContext()));
        adapterCupboardFunction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCupboardFunction.setAdapter(adapterCupboardFunction);
        int function = 0;
        try {
            function = Integer.parseInt(item.getAssetCode().substring(7, 9));
            spCupboardFunction.setSelection(function);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //产品种类
        Spinner spCupboardProductType = helper.getView(R.id.sp_cupboard_product_type);
        ArrayAdapter<String> adapterCupboardProductType = new ArrayAdapter<>(getContext(), R.layout.item_sp_code, ConfigureTools.getCupboardProductType(getContext(), function));
        adapterCupboardProductType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCupboardProductType.setAdapter(adapterCupboardProductType);
        try {
            int producttype = Integer.parseInt(item.getAssetCode().substring(9, 11));
            spCupboardProductType.setSelection(producttype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //摆放位置
        Spinner spCupboardPlacement = helper.getView(R.id.sp_cupboard_placement);
        ArrayAdapter<String> adapterCupboardPlacement = new ArrayAdapter<>(getContext(), R.layout.item_sp_code, Constant.st_cupboard_placement);
        adapterCupboardPlacement.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCupboardPlacement.setAdapter(adapterCupboardPlacement);
        String placement = item.getAssetCode().substring(0, 3);
        for (int i = 0; i < Constant.st_cupboard_placement.length; i++) {
            if (placement.equals(Constant.st_cupboard_placement[i])) {
                spCupboardPlacement.setSelection(i);
                break;
            }
        }
        spCupboardPlacement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!item.getAssetCode().substring(0, 3).equals(Constant.st_cupboard_placement[position])) {
                    StringBuilder sb = new StringBuilder(item.getAssetCode());
                    sb.replace(0, 3, Constant.st_cupboard_placement[position]);
                    item.setAssetCode(sb.toString());
                    iCallback.IRefresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //流水号
        EditText etCupboardSerialNumber = helper.getView(R.id.et_cupboard_serial_number);
        etCupboardSerialNumber.setText(item.getAssetCode().substring(11));
    }

}
