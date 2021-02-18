package com.rocktech.boarddriver.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rocktech.boarddriver.R;
import com.rocktech.boarddriver.base.BaseActivity;
import com.rocktech.boarddriver.bean.AssetCodeBean;
import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.coremodule.lockcontrol.LockerFactory;
import com.rocktech.boarddriver.coremodule.lockcontrol.handleresult.customized.CustomizedFactory;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.DialogManager;
import com.rocktech.boarddriver.tools.LoggerUtils;
import com.rocktech.boarddriver.ui.adapter.CommonAssetCodeAdapter;
import com.rocktech.boarddriver.ui.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class AssetCodeActivity extends BaseActivity implements CommonAssetCodeAdapter.ICallback {


    @BindView(R.id.sp_assetcode_specs)
    Spinner spAssetcodeSpecs;
    @BindView(R.id.rv_assetcode_code)
    RecyclerView rvAssetcodeCode;
    @BindView(R.id.tv_assetcode_code)
    TextView tvAssetcodeCode;
    @BindView(R.id.tv_assetcode_new_code)
    TextView tvAssetcodeNewCode;
    @BindView(R.id.bt_assetcode_write)
    Button btAssetcodeWrite;
    @BindView(R.id.bt_assetcode_read)
    Button btAssetcodeRead;
    @BindView(R.id.bt_assetcode_create)
    Button btAssetcodeCreate;
    private ILocker locker;
    private AssetCodeReceiver assetCodeReceiver;
    private IntentFilter intentFilter;
    private BaseAdapter assetCodeAdapter;
    private int assetCodeLength;
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100));

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_assetcode;
    }

    @Override
    protected void initData() {
        locker = LockerFactory.getLocker(ConfigureTools.getLockerTypeBean(this).getId(), this);
        rvAssetcodeCode.setLayoutManager(new LinearLayoutManager(this));
        assetCodeAdapter = CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(this).getId()).getAssetCodeAdapter();
        assetCodeLength = CustomizedFactory.getCustomized(ConfigureTools.getLockerTypeBean(this).getId()).getAssetCodeLength();
        rvAssetcodeCode.setAdapter(assetCodeAdapter);
        assetCodeAdapter.setiCallback(this);
        assetCodeReceiver = new AssetCodeReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.hal.boxinfo.result.inner");
        intentFilter.addAction("android.intent.action.writeAssetCode.result");
        registerReceiver(assetCodeReceiver, intentFilter);
        threadPoolExecutor.execute(readAssetCodeRunnable);
        DialogManager.showDialog(this);
    }

    @Override
    protected void addListener() {
        btAssetcodeWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.showDialog(AssetCodeActivity.this);
                threadPoolExecutor.execute(writeCodeRunnable);
            }
        });
        btAssetcodeRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.showDialog(AssetCodeActivity.this);
                threadPoolExecutor.execute(readAssetCodeRunnable);
            }
        });
        btAssetcodeCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshUI(false, true);
            }
        });
    }

    @Override
    public void IRefresh() {
        refreshUI(false, false);
    }

    class AssetCodeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String act = intent.getAction();
            DialogManager.dismissProgressDialog();
            if ("android.intent.action.hal.boxinfo.result.inner".equals(act)) {
                ArrayList<AssetCodeBean> data = (ArrayList<AssetCodeBean>) intent.getSerializableExtra("assetCode_list");
                int lockerType = ConfigureTools.getLockerTypeBean(context).getId();
                List<AssetCodeBean> newdata = CustomizedFactory.getCustomized(lockerType).assetCodeSort(data);
                assetCodeAdapter.setNewData(newdata);
                LoggerUtils.Log().e(assetCodeAdapter.getData().toString());
                refreshUI(true, false);
            }
            if ("android.intent.action.writeAssetCode.result".equals(act)) {
                ArrayList<String> data = intent.getStringArrayListExtra("writeAssetCode_result");
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < data.size(); i++) {
                    sb.append(data.get(i) + "\n");
                }
                tvAssetcodeCode.setText(sb.toString());
            }
        }
    }

    private void refreshUI(boolean isInit, boolean isCreate) {
        List<AssetCodeBean> list = assetCodeAdapter.getData();
        StringBuffer sbcode = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (isCreate) {
                sbcode.append(list.get(i).getAssetCode().substring(0, assetCodeLength) + "：" + list.get(i).getBoxId() + "\n");
            } else {
                sbcode.append(list.get(i).getAssetCode().substring(0, assetCodeLength) + "\n");
            }
        }
        if (isInit) {
            tvAssetcodeCode.setText("读取到的资产编码为:" + '\n' + sbcode.toString());
            tvAssetcodeNewCode.setText("");
        } else {
            tvAssetcodeNewCode.setText("预设:" + '\n' + sbcode.toString());
        }
    }


    Runnable readAssetCodeRunnable = new Runnable() {
        @Override
        public void run() {
            locker.readCodes();
        }
    };

    Runnable writeCodeRunnable = new Runnable() {
        @Override
        public void run() {
            locker.writeCode(assetCodeAdapter.getData());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(assetCodeReceiver);
    }
}
