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
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.ui.view.MySpinner;

import java.util.ArrayList;

public class CourtAssetCodeAdapter extends BaseAdapter {


    public CourtAssetCodeAdapter(int layoutResId, @Nullable ArrayList<AssetCodeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final AssetCodeBean item) {
        item.setRealBoxId(item.getBoxId());
        //主/副柜
        MySpinner spCupboardCourtType = helper.getView(R.id.sp_cupboard_court_type);
        ArrayAdapter<String> adapterCupboardType = new ArrayAdapter<>(getContext(), R.layout.item_sp_code, Constant.st_cupboard_type);
        adapterCupboardType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCupboardCourtType.setAdapter(adapterCupboardType);
        if (item.getAssetCode().substring(0, 1).equalsIgnoreCase("Z")) {
            spCupboardCourtType.setSelection(0);
        } else {
            spCupboardCourtType.setSelection(1);
        }
        //功能类型
        MySpinner spCupboardCourtBoardType = helper.getView(R.id.sp_cupboard_court_board_type);
        String boardType = item.getAssetCode().substring(3, 4);
        String functionType = item.getAssetCode().substring(6, 8);
        if ("1".equals(boardType)) {
            ArrayAdapter<String> adapterCupboardCourtMainBoardType = new ArrayAdapter<>(getContext(), R.layout.item_sp_code, Constant.st_cupboard_court_mainboard_type);
            adapterCupboardCourtMainBoardType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCupboardCourtBoardType.setAdapter(adapterCupboardCourtMainBoardType);
            if ("21".equals(functionType)) {
                spCupboardCourtBoardType.setSelection(0);
            } else if ("19".equals(functionType)) {
                spCupboardCourtBoardType.setSelection(1);
            }
        } else {
            ArrayAdapter<String> adapterCupboardCourtBoardType = new ArrayAdapter<>(getContext(), R.layout.item_sp_code, Constant.st_cupboard_court_board_type);
            adapterCupboardCourtBoardType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCupboardCourtBoardType.setAdapter(adapterCupboardCourtBoardType);
            if ("20".equals(functionType)) {
                spCupboardCourtBoardType.setSelection(0);
            } else if ("28".equals(functionType)) {
                spCupboardCourtBoardType.setSelection(1);
            }
        }

        //摆放位置
        Spinner spCupboardCourtPlacement = helper.getView(R.id.sp_cupboard_court_placement);
        ArrayAdapter<String> adapterCupboardPlacement = new ArrayAdapter<>(getContext(), R.layout.item_sp_code, Constant.st_cupboard_placement);
        adapterCupboardPlacement.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCupboardCourtPlacement.setAdapter(adapterCupboardPlacement);
        String placement = item.getAssetCode().substring(0, 3);
        for (int i = 0; i < Constant.st_cupboard_placement.length; i++) {
            if (placement.equals(Constant.st_cupboard_placement[i])) {
                spCupboardCourtPlacement.setSelection(i);
                break;
            }
        }
        spCupboardCourtPlacement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        //默认数字
        EditText etCupboardCourtDefaultNum = helper.getView(R.id.et_cupboard_court_default_num);
        etCupboardCourtDefaultNum.setText(item.getAssetCode().substring(9, 11));
        //流水号
        EditText etCupboardCourtSerialNumber = helper.getView(R.id.et_cupboard_court_serial_number);
        etCupboardCourtSerialNumber.setText(item.getAssetCode().substring(11, 15));
    }

}
