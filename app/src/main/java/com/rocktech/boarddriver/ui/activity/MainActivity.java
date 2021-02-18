package com.rocktech.boarddriver.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rocktech.boarddriver.R;
import com.rocktech.boarddriver.base.BaseActivity;
import com.rocktech.boarddriver.base.myinterface.RockInterface;
import com.rocktech.boarddriver.coremodule.lockcontrol.ILocker;
import com.rocktech.boarddriver.coremodule.lockcontrol.LockerFactory;
import com.rocktech.boarddriver.tools.Action;
import com.rocktech.boarddriver.tools.ConfigureTools;
import com.rocktech.boarddriver.tools.Constant;
import com.rocktech.boarddriver.tools.DialogManager;
import com.rocktech.boarddriver.tools.PermissionUtils;
import com.rocktech.boarddriver.tools.SPHelper;
import com.rocktech.boarddriver.tools.ToastUtil;
import com.rocktech.boarddriver.tools.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android_serialport_api.SerialPortFinder;
import butterknife.BindView;

public class MainActivity extends BaseActivity implements RockInterface {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.spinner_qr_code)
    Spinner spinnerQrCode;
    @BindView(R.id.spinner_qr_code_tty)
    Spinner spinnerQrCodeTty;
    @BindView(R.id.spinner_printer)
    Spinner spinnerPrinter;
    @BindView(R.id.spinner_printer_tty)
    Spinner spinnerPrinterTty;

    @BindView(R.id.rl_rocktech_layout)
    RelativeLayout rlRocktechLayout;
    @BindView(R.id.et_blower_a_open)
    EditText etBlowerAOpen;
    @BindView(R.id.et_blower_a_close)
    EditText etBlowerAClose;
    @BindView(R.id.btn_blower_a_setup)
    Button btnBlowerASetup;
    @BindView(R.id.btn_blower_a_query)
    Button btnBlowerAQuery;
    @BindView(R.id.et_blower_b_open)
    EditText etBlowerBOpen;
    @BindView(R.id.et_blower_b_close)
    EditText etBlowerBClose;
    @BindView(R.id.btn_blower_b_setup)
    Button btnBlowerBSetup;
    @BindView(R.id.btn_blower_b_query)
    Button btnBlowerBQuery;
    @BindView(R.id.et_heater_a_open)
    EditText etHeaterAOpen;
    @BindView(R.id.et_heater_a_close)
    EditText etHeaterAClose;
    @BindView(R.id.btn_heater_a_setup)
    Button btnHeaterASetup;
    @BindView(R.id.btn_heater_a_query)
    Button btnHeaterAQuery;
    @BindView(R.id.sw_alert_switch)
    Switch swAlertSwitch;
    @BindView(R.id.sw_lock_state)
    Switch swLockState;
    @BindView(R.id.sw_doorlock_state)
    Switch swDoorlockState;
    @BindView(R.id.btn_board_id_change)
    Button btnBoardIdChange;
    @BindView(R.id.tv_board_id_show)
    TextView tvBoardIdShow;
    @BindView(R.id.spinner_watthour_meter_485)
    Spinner spinnerWatthourMeter485;
    @BindView(R.id.spinner_watthour_meter_bps)
    Spinner spinnerWatthourMeterBps;
    @BindView(R.id.btn_watthour_meter_query)
    Button btnWatthourMeterQuery;
    @BindView(R.id.btn_watthour_meter_setup)
    Button btnWatthourMeterSetup;
    @BindView(R.id.tv_watthour_meter_number)
    TextView tvWatthourMeterNumber;
    @BindView(R.id.btn_watthour_meter_get)
    Button btnWatthourMeterGet;
    @BindView(R.id.sw_manual_control_blower_a)
    Switch swManualControlBlowerA;
    @BindView(R.id.sw_manual_control_heater)
    Switch swManualControlHeater;
    @BindView(R.id.sw_manual_control_blower_b)
    Switch swManualControlBlowerB;
    @BindView(R.id.sw_manual_control_doorlock)
    Switch swManualControlDoorlock;
    @BindView(R.id.btn_manual_control_get_control)
    Button btnManualControlGetControl;
    @BindView(R.id.tv_manual_control_temp)
    TextView tvManualControlTemp;
    @BindView(R.id.btn_manual_control_get_temp)
    Button btnManualControlGetTemp;
    @BindView(R.id.et_manual_control_get_temp)
    EditText etManualControlGetTemp;
    @BindView(R.id.btn_manual_control_temp_compensate)
    Button btnManualControlTempCompensate;
    @BindView(R.id.btn_manual_control_get_temp_compensate)
    Button btnManualControlGetTempCompensate;
    @BindView(R.id.tv_manual_control_humidity)
    TextView tvManualControlHumidity;
    @BindView(R.id.btn_manual_control_get_humidity)
    Button btnManualControlGetHumidity;
    @BindView(R.id.et_manual_control_get_humidity)
    EditText etManualControlGetHumidity;
    @BindView(R.id.btn_manual_control_humidity_compensate)
    Button btnManualControlHumidityCompensate;
    @BindView(R.id.btn_manual_control_get_humidity_compensate)
    Button btnManualControlGetHumidityCompensate;
    @BindView(R.id.ll_manual_control)
    LinearLayout llManualControl;
    @BindView(R.id.bt_exit)
    Button btExit;
    @BindView(R.id.bt_set_assetcode)
    Button btSetAssetcode;
    @BindView(R.id.bt_import_config_file)
    Button btImportConfigFile;

    private SerialPortFinder serialPortFinder;
    private ArrayAdapter<String> QrCodeAdapter, QrCodeTtyAdapter, PrinterAdapter, PrinterTtyAdapter;
    private ArrayAdapter<String> WatthourMeter485Adapter, WatthourMeterBpsAdapter;
    private String[] devicesPath;
    private ILocker locker;
    private DataReceiver dataReceiver;
    private static boolean hasControl = false;
    private volatile boolean isInit = true;
    private static List<String> sNeedPermissions = new ArrayList<>();
    private PermissionUtils permissionUtils = null;
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100));


    static {
        sNeedPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        sNeedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private class DataReceiver extends BroadcastReceiver {

        private boolean mcSet = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (Action.Query485.equals(intentAction)) {
                String s = intent.getExtras().getString("meter");
                if (s.equals("not enough data")) {
                    return;
                }
                tvWatthourMeterNumber.setText(s + "  kW.h");
            }
            if (Action.Temp.equals(intentAction)) {
                String temp = intent.getExtras().getString("temp");
                if (TextUtils.isEmpty(temp)) {
                    temp = "00";
                }
                tvManualControlTemp.setText(temp);
            }
            if (Action.queryTemp.equals(intentAction)) {
                String s = intent.getExtras().getString("temp");
                etManualControlGetTemp.setText(s);
            }
            if (Action.queryShidu.equals(intentAction)) {
                String s = intent.getExtras().getString("shidu");
                etManualControlGetHumidity.setText(s);
            }
            if (Action.query485SET.equals(intentAction)) {
                Bundle b = intent.getExtras();
                int[] _485_set = b.getIntArray("state_485_set");
                spinnerWatthourMeter485.setSelection(_485_set[0]);
                spinnerWatthourMeterBps.setSelection(_485_set[1]);
                if (isInit) {
                    isInit = false;
                    threadPoolExecutor.execute(saveCodesRunnable);
                }
            }
            if (Action.query12SET.equals(intentAction)) {
                //查询风扇A设置温度
                String temO = intent.getStringExtra("temOpen");
                String temC = intent.getStringExtra("temClose");
                etBlowerAOpen.setText(temO);
                etBlowerAClose.setText(temC);
            }
            if (Action.query25SET.equals(intentAction)) {
                //查询风扇B设置温度
                String temO = intent.getStringExtra("temOpen");
                String temC = intent.getStringExtra("temClose");
                etBlowerBOpen.setText(temO);
                etBlowerBClose.setText(temC);
            }
            if (Action.query26SET.equals(intentAction)) {
                //查询加热器设置温度
                String temO = intent.getStringExtra("temOpen");
                String temC = intent.getStringExtra("temClose");
                etHeaterAOpen.setText(temO);
                etHeaterAClose.setText(temC);
            }
            if (Action.queryMCSET.equals(intentAction)) {
                mcSet = intent.getBooleanExtra("mcSet", false);
                swDoorlockState.setChecked(mcSet);
                if (isInit) {
                    swDoorlockState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            ConfigureTools.setBuzEnableOne(MainActivity.this, isChecked);
                            threadPoolExecutor.execute(new setMCCheckRunnable(isChecked));
                        }
                    });
                    threadPoolExecutor.execute(new getChipVersionRunnable(Constant.boardList));
                }
            }
            if (Action.sendComplete.equals(intentAction)) {
                int handle = intent.getIntExtra("handle", 0);
                byte[] data = intent.getByteArrayExtra("data");
                if (handle == Constant.STATE_QUERY_TEMP && isInit) {
                    if (data[4] == 0x69) {
                        threadPoolExecutor.execute(getBlowerBRunnable);
                    }
                    if (data[4] == 0x6A) {
                        threadPoolExecutor.execute(getHeaterRunnable);
                    }
                    if (data[4] == 0x6B) {
                        threadPoolExecutor.execute(getMCRunnable);
                    }
                }
            }
            if (Action.OVERTIME.equals(intentAction)) {
                int handle = intent.getExtras().getInt("handle");
                if (handle == Constant.FLAG_UPDATE_BOARD_ADDR) {
                    threadPoolExecutor.execute(new getChipVersionRunnable(Constant.boardList));
                }
            }
        }
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        isInit = true;
        serialPortFinder = new SerialPortFinder();
        tvTitle.setText(Tools.getAppVersion(this));
        devicesPath = serialPortFinder.getAllDevicesPath();

        QrCodeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Constant.m1);
        QrCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQrCode.setAdapter(QrCodeAdapter);
        QrCodeTtyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, devicesPath);
        QrCodeTtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQrCodeTty.setAdapter(QrCodeTtyAdapter);

        PrinterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Constant.m2);
        PrinterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrinter.setAdapter(PrinterAdapter);
        PrinterTtyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, devicesPath);
        PrinterTtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrinterTty.setAdapter(PrinterTtyAdapter);

        WatthourMeter485Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Constant.m3);
        WatthourMeter485Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWatthourMeter485.setAdapter(WatthourMeter485Adapter);

        WatthourMeterBpsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Constant.m4);
        WatthourMeterBpsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWatthourMeterBps.setAdapter(WatthourMeterBpsAdapter);
    }

    @Override
    protected void addListener() {
        spinnerQrCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ConfigureTools.setQrCode(MainActivity.this, "");
                        break;
                    case 1:
                        ConfigureTools.setQrCode(MainActivity.this, Constant.ScannerTYPE.HONEYWELL_USB);
                        break;
                    case 2:
                        ConfigureTools.setQrCode(MainActivity.this, Constant.ScannerTYPE.HONEYWELL_SERIES);
                        break;
                    case 3:
                        ConfigureTools.setQrCode(MainActivity.this, Constant.ScannerTYPE.DEWO);
                        break;
                    case 4:
                        ConfigureTools.setQrCode(MainActivity.this, Constant.ScannerTYPE.FM50);
                        break;
                    case 5:
                        ConfigureTools.setQrCode(MainActivity.this, Constant.ScannerTYPE._4102S);
                        break;
                    case 6:
                        ConfigureTools.setQrCode(MainActivity.this, Constant.ScannerTYPE.SPR4308);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerQrCodeTty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConfigureTools.setQrCodeTty(MainActivity.this, devicesPath[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPrinter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ConfigureTools.setPrinter(MainActivity.this, "");
                        break;
                    case 1:
                        ConfigureTools.setPrinter(MainActivity.this, Constant.PrinterTYPE.SPRT);
                        break;
                    case 2:
                        ConfigureTools.setPrinter(MainActivity.this, Constant.PrinterTYPE.QR);
                        break;
                    case 3:
                        ConfigureTools.setPrinter(MainActivity.this, Constant.PrinterTYPE.XBY);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPrinterTty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConfigureTools.setPrinterTty(MainActivity.this, devicesPath[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btSetAssetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AssetCodeActivity.class);
            }
        });

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void initData() {
        String qrcode = ConfigureTools.getQrCode(this);
        if (!TextUtils.isEmpty(qrcode)) {
            if (qrcode.equals(Constant.ScannerTYPE.HONEYWELL_USB)) {
                spinnerQrCode.setSelection(1);
            } else if (qrcode.equals(Constant.ScannerTYPE.HONEYWELL_SERIES)) {
                spinnerQrCode.setSelection(2);
            } else if (qrcode.equals(Constant.ScannerTYPE.DEWO)) {
                spinnerQrCode.setSelection(3);
            } else if (qrcode.equals(Constant.ScannerTYPE.FM50)) {
                spinnerQrCode.setSelection(4);
            } else if (qrcode.equals(Constant.ScannerTYPE._4102S)) {
                spinnerQrCode.setSelection(5);
            } else if (qrcode.equals(Constant.ScannerTYPE.SPR4308)) {
                spinnerQrCode.setSelection(6);
            }
        } else {
            spinnerQrCode.setSelection(0);
        }
        String qrcodetty = ConfigureTools.getQrCodeTty(this);
        if (!TextUtils.isEmpty(qrcodetty)) {
            for (int i = 0; i < devicesPath.length; i++) {
                if (devicesPath[i].equals(qrcodetty)) {
                    spinnerQrCodeTty.setSelection(i);
                    break;
                }
            }
        } else {
            spinnerQrCodeTty.setSelection(0);
            ConfigureTools.setQrCodeTty(MainActivity.this, devicesPath[0]);
        }
        String printer = ConfigureTools.getPrinter(this);
        if (!TextUtils.isEmpty(printer)) {
            if (printer.equals(Constant.PrinterTYPE.SPRT)) {
                spinnerPrinter.setSelection(1);
            } else if (printer.equals(Constant.PrinterTYPE.QR)) {
                spinnerPrinter.setSelection(2);
            } else if (printer.equals(Constant.PrinterTYPE.XBY)) {
                spinnerPrinter.setSelection(3);
            }
        } else {
            spinnerPrinter.setSelection(0);
        }
        String printerTty = ConfigureTools.getPrinterTty(this);
        if (!TextUtils.isEmpty(printerTty)) {
            for (int i = 0; i < devicesPath.length; i++) {
                if (devicesPath[i].equals(printerTty)) {
                    spinnerPrinterTty.setSelection(i);
                    break;
                }
            }
        } else {
            spinnerPrinterTty.setSelection(0);
            ConfigureTools.setPrinterTty(MainActivity.this, devicesPath[0]);
        }

        permissionUtils = new PermissionUtils(this);
        if (Build.VERSION.SDK_INT >= 23) {
            permissionUtils.request(sNeedPermissions, 100, new PermissionUtils.CallBack() {
                @Override
                public void grantAll() {
                    initMainData();
                }

                @Override
                public void denied() {
                    finish();
                }
            });
        } else {
            initMainData();
        }
    }


    private void initMainData() {
        if (ConfigureTools.getBoardTypeBean(this).getId() == Constant.BoardTYPE.ROCKTECH) {
            if (ConfigureTools.getNeedTemperatureBean(this).getIsShow() == Constant.ShowTemperature.SHOW) {
                rlRocktechLayout.setVisibility(View.VISIBLE);
                initRocktechReceiver();
                initRocktechData();
                initRocktechListener();
            } else {
                rlRocktechLayout.setVisibility(View.GONE);
            }
        } else {
            rlRocktechLayout.setVisibility(View.GONE);
        }
    }

    private void initRocktechReceiver() {
        locker = LockerFactory.getLocker(ConfigureTools.getLockerTypeBean(this).getId(), this);
        locker.initRockInterface(this);
        IntentFilter myFilter = new IntentFilter();
        myFilter.addAction(Action.Query485);
        myFilter.addAction(Action.Temp);
        myFilter.addAction(Action.queryTemp);
        myFilter.addAction(Action.queryShidu);
        myFilter.addAction(Action.query485SET);
        myFilter.addAction(Action.query12SET);
        myFilter.addAction(Action.query25SET);
        myFilter.addAction(Action.query26SET);
        myFilter.addAction(Action.queryMCSET);
        myFilter.addAction(Action.sendComplete);
        myFilter.addAction(Action.OVER);
        myFilter.addAction(Action.OVERTIME);
        dataReceiver = new DataReceiver();
        registerReceiver(dataReceiver, myFilter);
    }

    private void initRocktechData() {
        swAlertSwitch.setChecked(ConfigureTools.getBuzzer(this));
        locker.setBuzzer(ConfigureTools.getBuzzer(this));
        swLockState.setChecked(ConfigureTools.getLockStat(this));
        spinnerWatthourMeter485.setSelection(ConfigureTools.getParity(this));
        spinnerWatthourMeterBps.setSelection(ConfigureTools.getBaudrate(this));
        DialogManager.showDialog(this);
        threadPoolExecutor.execute(getBlowerARunnable);
    }

    private void initRocktechListener() {
        btnBlowerAQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPoolExecutor.execute(getBlowerARunnable);
            }
        });
        btnBlowerASetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupBoard(0x58, "风扇A(ch12)", etBlowerAOpen.getText().toString(), etBlowerAClose.getText().toString());
            }
        });
        btnBlowerBQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPoolExecutor.execute(getBlowerBRunnable);
            }
        });
        btnBlowerBSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupBoard(0x59, "风扇B(ch25)", etBlowerBOpen.getText().toString(), etBlowerBClose.getText().toString());
            }
        });
        btnHeaterAQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPoolExecutor.execute(getHeaterRunnable);
            }
        });
        btnHeaterASetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupBoard(0x5A, "加热器", etHeaterAOpen.getText().toString(), etHeaterAClose.getText().toString());
            }
        });
        swAlertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConfigureTools.setBuzzer(MainActivity.this, isChecked);
                threadPoolExecutor.execute(new setBuzzerRunnable(isChecked));
            }
        });
        swLockState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConfigureTools.setLockStat(MainActivity.this, isChecked);
            }
        });

        btnBoardIdChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.showDialog(MainActivity.this);
                threadPoolExecutor.execute(changeIdRunnable);
            }
        });
        spinnerWatthourMeter485.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConfigureTools.setParity(MainActivity.this, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerWatthourMeterBps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConfigureTools.setBaudrate(MainActivity.this, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnWatthourMeterQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPoolExecutor.execute(query485Runnable);
            }
        });
        btnWatthourMeterSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPoolExecutor.execute(set485Runnable);
            }
        });
        btnWatthourMeterGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPoolExecutor.execute(getWatthourMeterRunnable);
            }
        });
        swManualControlBlowerA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                threadPoolExecutor.execute(new doDebugRunnable(12, isChecked));
            }
        });
        swManualControlHeater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                threadPoolExecutor.execute(new doDebugRunnable(123, isChecked));
            }
        });
        swManualControlBlowerB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                threadPoolExecutor.execute(new doDebugRunnable(25, isChecked));
            }
        });
        swManualControlDoorlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                threadPoolExecutor.execute(new setMCLampRunnable(isChecked));
            }
        });
        btnManualControlGetControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasControl) {
                    threadPoolExecutor.execute(new getControlRunnable(true));
                    hasControl = true;
                    btnManualControlGetControl.setText("释放控制权");
                } else {
                    threadPoolExecutor.execute(new getControlRunnable(false));
                    hasControl = false;
                    btnManualControlGetControl.setText("获取控制权");
                }
            }
        });
        btnManualControlGetTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPoolExecutor.execute(getTempRunnable);
            }
        });
        btnManualControlTempCompensate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etManualControlGetTemp.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    threadPoolExecutor.execute(new setWDBCRunnable(Byte.parseByte(s)));
                }
            }
        });
        btnManualControlGetTempCompensate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPoolExecutor.execute(querWDBCRunnable);
            }
        });
        btnManualControlGetHumidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPoolExecutor.execute(getHumidityRunnable);
            }
        });
        btnManualControlHumidityCompensate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etManualControlGetHumidity.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    threadPoolExecutor.execute(new setSDBCRunnable(Byte.parseByte(s)));
                }
            }
        });
        btnManualControlGetHumidityCompensate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPoolExecutor.execute(querySDBCRunnable);
            }
        });
    }

    private void setupBoard(final int cmd, String name, final String open, final String close) {
        DialogManager.showSetupBoardTipsDialog(this, name, open, close, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {
                    locker.setControl(cmd, open, close);
                    dialog.dismiss();
                } else if (which == DialogAction.NEGATIVE) {
                    dialog.dismiss();
                }
            }
        });
    }

    Runnable getBlowerARunnable = new Runnable() {
        @Override
        public void run() {
            //查询风扇A设置温度
            locker.querySetting(0x69);
        }
    };
    Runnable getBlowerBRunnable = new Runnable() {
        @Override
        public void run() {
            //查询风扇B设置温度
            locker.querySetting(0x6A);
        }
    };
    Runnable getHeaterRunnable = new Runnable() {
        @Override
        public void run() {
            //查询加热器设置温度
            locker.querySetting(0x6B);
        }
    };
    Runnable getMCRunnable = new Runnable() {
        @Override
        public void run() {
            //查询门磁设置
            locker.queryMCsetting();
        }
    };
    Runnable changeIdRunnable = new Runnable() {
        @Override
        public void run() {
            locker.updateBoardId();
        }
    };
    Runnable query485Runnable = new Runnable() {
        @Override
        public void run() {
            locker.query485Setting();
        }
    };
    Runnable getTempRunnable = new Runnable() {
        @Override
        public void run() {
            locker.getTemp();
        }
    };
    Runnable getHumidityRunnable = new Runnable() {
        @Override
        public void run() {
            locker.getHumidity();
        }
    };
    Runnable querWDBCRunnable = new Runnable() {
        @Override
        public void run() {
            locker.querWDBC();
        }
    };
    Runnable querySDBCRunnable = new Runnable() {
        @Override
        public void run() {
            locker.querySDBC();
        }
    };
    Runnable set485Runnable = new Runnable() {
        @Override
        public void run() {
            int parity = ConfigureTools.getParity(MainActivity.this);
            int baudrate = ConfigureTools.getBaudrate(MainActivity.this);
            if (parity != 0 && baudrate != 0) {
                locker.set485(parity + "", baudrate + "");
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showLong(MainActivity.this, "请设置校验模式或波特率!");
                    }
                });
            }
        }
    };
    Runnable getWatthourMeterRunnable = new Runnable() {
        @Override
        public void run() {
            int parity = ConfigureTools.getParity(MainActivity.this);
            int baudrate = ConfigureTools.getBaudrate(MainActivity.this);
            if (parity != 0 && baudrate != 0) {
                locker.queryPower();
            }
        }
    };


    class getControlRunnable implements Runnable {

        private boolean hasControl;

        public getControlRunnable(boolean hasControl) {
            this.hasControl = hasControl;
        }

        @Override
        public void run() {
            locker.getControl(hasControl);
        }
    }

    class doDebugRunnable implements Runnable {

        private int cmd;
        private boolean isChecked;

        public doDebugRunnable(int cmd, boolean isChecked) {
            this.cmd = cmd;
            this.isChecked = isChecked;
        }

        @Override
        public void run() {
            locker.doDebug(cmd, isChecked);
        }
    }

    class setBuzzerRunnable implements Runnable {

        private boolean isChecked;

        public setBuzzerRunnable(boolean isChecked) {
            this.isChecked = isChecked;
        }

        @Override
        public void run() {
            locker.setBuzzer(isChecked);
        }
    }

    class setMCCheckRunnable implements Runnable {

        private boolean isChecked;

        public setMCCheckRunnable(boolean isChecked) {
            this.isChecked = isChecked;
        }

        @Override
        public void run() {
            locker.setMCCheck(isChecked);
        }
    }

    class setMCLampRunnable implements Runnable {

        private boolean isChecked;

        public setMCLampRunnable(boolean isChecked) {
            this.isChecked = isChecked;
        }

        @Override
        public void run() {
            locker.setMCLamp(isChecked);
        }
    }

    class getChipVersionRunnable implements Runnable {

        private String[] boxIds;

        public getChipVersionRunnable(String[] boxIds) {
            this.boxIds = boxIds;
        }

        @Override
        public void run() {
            locker.getChipVersion(boxIds);
        }
    }

    class setWDBCRunnable implements Runnable {

        private byte b;

        public setWDBCRunnable(byte b) {
            this.b = b;
        }

        @Override
        public void run() {
            if (b > 12 || b < -12) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showLong(MainActivity.this, "温度补偿值必须介于-12到12度之间！！！");
                    }
                });
                return;
            }
            if (b < 0) {
                b = (byte) (Math.abs(b) * 10 | 0x80);
            } else {
                b *= 10;
            }
            locker.setWDBC(b);
        }
    }

    class setSDBCRunnable implements Runnable {

        private byte b;

        public setSDBCRunnable(byte b) {
            this.b = b;
        }

        @Override
        public void run() {
            locker.setSDBC(b);
        }
    }

    @Override
    public void getChipVer(final List<String> boxV) {
        DialogManager.dismissProgressDialog();
        SPHelper.getInstance(this).putInt(Constant.SPKEY.CONNECTBOXSIZE, boxV.size());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvBoardIdShow.setText(Tools.listToString(boxV));
                if (isInit) {
                    threadPoolExecutor.execute(query485Runnable);
                }
            }
        });
    }

    Runnable saveCodesRunnable = new Runnable() {
        @Override
        public void run() {
            locker.saveCodes();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataReceiver != null) {
            unregisterReceiver(dataReceiver);
        }
        threadPoolExecutor.shutdown();
        isInit = true;
    }
}
