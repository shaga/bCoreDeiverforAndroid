package net.shaga_workshop.cica_controller;

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
import net.shaga_workshop.cica_controller.views.fragments.BcoreControllerFragment;

public class BcoreControllerActivity extends AppCompatActivity {

    public static final int TIMER_SPAN_READ_BATTERY = 5000;
    public static final String KEY_BCORE_NAME = "net.shaga_workshop.bcoredriverforandroid.BcoreControllerActivity.BcoreName";
    public static final String KEY_BCORE_ADDR = "net.shaga_workshop.bcoredriverforandroid.BcoreControllerActivity.BcoreAddr";

    private String mBcoreName;
    private String mBcoreAddr;

    private BcoreControllerFragment mControllerFragment;
    private BcoreControlService mService;
    private TextView mTextState;

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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.show(mControllerFragment);
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
            if (idx == 0) {
                for (int i = 0; i < BcoreConsts.MAX_MOTOR_COUNT; i++) {
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mBcoreName = getIntent().getExtras().getString(KEY_BCORE_NAME);
        mBcoreAddr = getIntent().getExtras().getString(KEY_BCORE_ADDR);

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

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.controller_frame, mControllerFragment);
        transaction.hide(mControllerFragment);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void setMotorSpeed(int idx, int value) {
        if (mService == null) return;

        byte data = BcoreValueUtil.convertMotorValue(value, false);

        mService.writeMotorPwm((byte) idx, data);
    }

    private void setServoPos(int idx, int value) {
    }
}
