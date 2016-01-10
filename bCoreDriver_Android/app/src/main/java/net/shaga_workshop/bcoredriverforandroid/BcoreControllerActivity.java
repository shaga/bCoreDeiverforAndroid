package net.shaga_workshop.bcoredriverforandroid;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import net.shaga_workshop.bcore_lib.BcoreConsts;
import net.shaga_workshop.bcore_lib.BcoreControlService;
import net.shaga_workshop.bcore_lib.BcoreInfoReceiver;
import net.shaga_workshop.bcore_lib.BcoreInfoUpdateListener;
import net.shaga_workshop.bcore_lib.BcoreValueUtil;
import net.shaga_workshop.bcoredriverforandroid.views.fragments.BcoreControllerFragment;
import net.shaga_workshop.bcoredriverforandroid.views.fragments.BcoreSettingFragment;
import net.shaga_workshop.bcoredriverforandroid.models.BcoreInfo;
import net.shaga_workshop.bcoredriverforandroid.models.BcoreInfoCatHands;
import net.shaga_workshop.bcoredriverforandroid.models.BcoreInfoOpenHelper;

public class BcoreControllerActivity extends AppCompatActivity {

    public static final int TIMER_SPAN_READ_BATTERY = 5000;
    public static final String KEY_BCORE_NAME = "net.shaga_workshop.bcoredriverforandroid.BcoreControllerActivity.BcoreName";
    public static final String KEY_BCORE_ADDR = "net.shaga_workshop.bcoredriverforandroid.BcoreControllerActivity.BcoreAddr";

    private String mBcoreName;
    private String mBcoreAddr;

    private BcoreControllerFragment mControllerFragment;
    private BcoreSettingFragment mSettingFragment;
    private BcoreControlService mService;
    private TextView mTextState;

    private SQLiteDatabase mBcoreInfoDB;

    private BcoreInfo mBcoreInfo;

    private boolean mIsShowSetting;

    private BcoreInfoUpdateListener bcoreInfoUpdateListener = new BcoreInfoUpdateListener() {
        @Override
        public void onConnectionChanged(int state) {
            if (state == BcoreControlService.BCORE_CONNECTION_STATE_CONNECTED) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextState.setText(R.string.text_state_initializing);
                    }
                });
            }
            else if (state == BcoreControlService.BCORE_CONNECTION_STATE_DISCONNECTED) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BcoreControllerActivity.this, R.string.msg_bcore_disconnected, Toast.LENGTH_SHORT).show();
                        BcoreControllerActivity.this.finish();
                    }
                });
            }
        }

        @Override
        public void onDiscoveredService(boolean isDiscovered) {
            if (isDiscovered) {
                mService.readFunctions();
                for (int i = 0; i < BcoreConsts.MAX_SERVO_COUNT; i++) {
                    int trim = mBcoreInfo.getTrimServo(i);
                    if (trim != 0) updateServoPosTrim(i);
                }
            } else {
                // bCoreサービスが見つからないので切断して終了する。
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BcoreControllerActivity.this, R.string.msg_device_not_have_bcore_service, Toast.LENGTH_SHORT).show();
                        mService.disconnect();
                        finish();
                    }
                });
            }
        }

        @Override
        public void onReadBatteryVoltage(int vol) {
            mControllerFragment.setBatteryValue(vol);
        }

        @Override
        public void onReadBcoreFunctions(byte[] value) {
            mControllerFragment.setBcoreFunctions(value);
            mSettingFragment.setFunctions(value);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.show(mControllerFragment);
                    transaction.hide(mSettingFragment);
                    transaction.commit();
                    mTextState.setVisibility(View.GONE);
                }
            });
        }
    };

    private BcoreInfoReceiver mReceiver;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service.getClass() == BcoreControlService.LocalBinder.class) {
                BcoreControlService.LocalBinder binder = (BcoreControlService.LocalBinder) service;
                mService = binder.getService();
                mService.connect(mBcoreAddr, TIMER_SPAN_READ_BATTERY);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private BcoreControllerFragment.OnBcoreControllerListener mFragmentControlListener = new BcoreControllerFragment.OnBcoreControllerListener() {

        @Override
        public void setMotorSpeed(int idx, int speed) {
            BcoreControllerActivity.this.setMotorSpeed(idx, speed);
        }

        @Override
        public void setServoPos(int idx, int pos) {
            if (!mBcoreInfo.getIsSyncServo(idx)) {
                BcoreControllerActivity.this.setServoPos(idx, pos);
            }

            if (idx == 0) {
                for (int i = 0; i < BcoreConsts.MAX_MOTOR_COUNT; i++) {
                    if (!mBcoreInfo.getIsSyncServo(i)) continue;

                    BcoreControllerActivity.this.setServoPos(i, pos);
                }
            }
        }

        private byte mPortState = 0x00;

        @Override
        public void setPortOut(int idx, boolean state) {
            if (mService != null) {
                if (state) {
                    mPortState = (byte)(mPortState | (0x01 << idx));
                } else {
                    mPortState = (byte)(mPortState & ~(0x01 << idx));
                }

                mService.writePortOut(mPortState);
            }
        }
    };

    private BcoreSettingFragment.OnSettingUpdateListener mSettingUpdateListener = new BcoreSettingFragment.OnSettingUpdateListener() {
        @Override
        public void onUpdatedBcoreInfo() {
            if (!mBcoreInfo.getDisplayName().equals(mBcoreName)) {
                mBcoreName = mBcoreInfo.getDisplayName();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getSupportActionBar().setTitle(mBcoreInfo.getDisplayName());
                    }
                });
            }
            mControllerFragment.updateVisibility();

            for (int i = 0; i < BcoreConsts.MAX_SERVO_COUNT; i++) {
                updateServoPosTrim(i);
            }

            BcoreInfoCatHands.update(mBcoreInfoDB, mBcoreInfo);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        BcoreInfoOpenHelper helper = new BcoreInfoOpenHelper(getApplicationContext());
        mBcoreInfoDB = helper.getWritableDatabase();

        mBcoreName = getIntent().getExtras().getString(KEY_BCORE_NAME);
        mBcoreAddr = getIntent().getExtras().getString(KEY_BCORE_ADDR);

        mBcoreInfo = BcoreInfoCatHands.findByDeviceAddr(mBcoreInfoDB, mBcoreAddr);

        if (mBcoreInfo == null) {
            mBcoreInfo = new BcoreInfo(mBcoreName, mBcoreAddr);
            BcoreInfoCatHands.insert(mBcoreInfoDB, mBcoreInfo);
        }

        setContentView(R.layout.activity_bcore_controller);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mBcoreName);

        mTextState = (TextView) findViewById(R.id.textState);
        mTextState.setText(R.string.text_state_connecting);

        mReceiver = new BcoreInfoReceiver();
        mReceiver.setBcoreInfoUpdateListener(bcoreInfoUpdateListener);

        Intent intent = new Intent(this, BcoreControlService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);

        mControllerFragment = BcoreControllerFragment.newInstance();
        mControllerFragment.setListener(mFragmentControlListener);
        mControllerFragment.setBcoreInfo(mBcoreInfo);

        mSettingFragment = BcoreSettingFragment.newInstance();
        mSettingFragment.setBcoreInfo(mBcoreInfo);
        mSettingFragment.setOnSettingUpdateListener(mSettingUpdateListener);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.controller_frame, mControllerFragment);
        transaction.hide(mControllerFragment);
        transaction.add(R.id.controller_frame, mSettingFragment);
        transaction.hide(mSettingFragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            unbindService(mConnection);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        registerReceiver(mReceiver, BcoreInfoReceiver.createIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        unregisterReceiver(mReceiver);

        if (mService != null) {
            mService.disconnect();
        }

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mIsShowSetting) {
            getMenuInflater().inflate(R.menu.menu_controller, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_setting, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (!mIsShowSetting) {
                    finish();
                } else {
                    switchFragment(false);
                }
                return false;
            case R.id.controller_menu_setting:
                if (!mControllerFragment.isHidden())
                    switchFragment(true);
                return false;
            case R.id.menu_setting_reset:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_name);
                builder.setMessage(R.string.msg_reset_bcore_settings);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBcoreInfo.reset();
                        mSettingFragment.setControlValue();
                        mControllerFragment.updateVisibility();
                        for (int i = 0; i < BcoreConsts.MAX_SERVO_COUNT; i++) {
                            updateServoPosTrim(i);
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.no, null);
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mIsShowSetting) {
            switchFragment(false);
            return;
        }

        super.onBackPressed();
    }

    private void switchFragment(boolean isShowSetting) {
         if (mIsShowSetting == isShowSetting) return;

        mIsShowSetting = isShowSetting;

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (mIsShowSetting) {
            transaction.hide(mControllerFragment);
            transaction.show(mSettingFragment);
        } else {
            transaction.hide(mSettingFragment);
            transaction.show(mControllerFragment);
        }
        transaction.commit();

        manager.invalidateOptionsMenu();
    }

    private void setMotorSpeed(int idx, int value) {
        if (mService == null) return;

        byte data = BcoreValueUtil.convertMotorValue(value, mBcoreInfo.getIsFlipMotor(idx));

        mService.writeMotorPwm((byte) idx, data);
    }

    private void setServoPos(int idx, int value) {
        if (mService == null) return;

        byte data = BcoreValueUtil.convertServoValue(value, mBcoreInfo.getIsFlipServo(idx), mBcoreInfo.getTrimServo(idx));

        mService.writeServoPos(idx, data);
    }

    private void updateServoPosTrim(int idx) {
        if (mService == null) return;

        int value = mControllerFragment.getServoCurrentValue(idx);

        setServoPos(idx, value);
    }
}
