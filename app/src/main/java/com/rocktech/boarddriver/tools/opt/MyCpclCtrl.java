package com.rocktech.boarddriver.tools.opt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCpclCtrl {

    private static final String Tag = "MyCpclCtrl";

    public static String getCpclFromJson(String strJson) {
        String strRst = "";
        try {
            OptsList mOptsList = new OptsList();
            mOptsList.setList(new ArrayList<IPrintOpt>());

            JSONObject jsonObject = new JSONObject(strJson);
            JSONArray jsonArray = jsonObject.getJSONArray("opts");
            for (int iLoop = 0x00; iLoop < jsonArray.length(); ++iLoop) {
                JSONObject jsonsubObject = jsonArray.getJSONObject(iLoop);
                int iOptCode = jsonsubObject.getInt("optCode");

                switch (iOptCode) {
                    case OptTable.OPT_CODE_READY:
                        mOptsList.getList().add(ReadyOpt.fromJson(jsonsubObject));
                        break;
                    case OptTable.OPT_CODE_OVER:
                        mOptsList.getList().add(OverOpt.fromJson(jsonsubObject));
                        break;
                    case OptTable.OPT_CODE_TEXT:
                        mOptsList.getList().add(TextOpt.fromJson(jsonsubObject));
                        break;
                    case OptTable.OPT_CODE_LINE:
                        mOptsList.getList().add(LineOpt.fromJson(jsonsubObject));
                        break;
                    case OptTable.OPT_CODE_BARCODE:
                        mOptsList.getList().add(BarCodeOpt.fromJson(jsonsubObject));
                        break;
                    case OptTable.OPT_CODE_QRCODE:
                        mOptsList.getList().add(QrCodeOpt.fromJson(jsonsubObject));
                        break;
                    case OptTable.OPT_CODE_BITMAP:
                        mOptsList.getList().add(BitmapOpt.fromJson(jsonsubObject));
                        break;
                    case OptTable.OPT_CODE_NOP:
                        mOptsList.getList().add(NopOpt.fromJson(jsonsubObject));
                        break;
                    default:
                        break;
                }
            }

            for (int iLoop = 0x00; iLoop < mOptsList.getList().size(); ++iLoop) {
                IPrintOpt opt = mOptsList.getList().get(iLoop);
                strRst += opt.toCpcl();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strRst;
    }
}
