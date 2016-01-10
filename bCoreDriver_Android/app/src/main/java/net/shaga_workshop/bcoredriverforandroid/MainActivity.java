package net.shaga_workshop.bcoredriverforandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.shaga_workshop.bcore_lib.BcoreScanner;
import net.shaga_workshop.bcore_lib.BcoreScannerListener;
import net.shaga_workshop.bcoredriverforandroid.models.BcoreInfo;
import net.shaga_workshop.bcoredriverforandroid.models.BcoreInfoCatHands;
import net.shaga_workshop.bcoredriverforandroid.models.BcoreInfoOpenHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_ALLOW_LOCATION = 100;
    private static final int REQ_CODE_ALLOW_BLUETOOTH = 101;

    private class DeviceInfo {
        private String name;
        private String addr;

        public DeviceInfo(String name, String addr) {
            this.name = name;
            this.addr = addr;
        }

        public String getName() { return name; }
        public String getAddr() { return addr; }

        @Override
        public boolean equals(Object o) {
            DeviceInfo info = (DeviceInfo)o;

            return name.equals(info.getName()) && addr.equals(info.getAddr());
        }
    }

    private class BcoreAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<DeviceInfo> infos;

        public BcoreAdapter(Context context, ArrayList<DeviceInfo> infos) {
            inflater = LayoutInflater.from(context);
            this.infos = infos;
        }

        @Override
        public int getCount() {
            return infos != null ? infos.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return infos != null && 0 <= position && position < infos.size() ? infos.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.bcore_list_item, parent, false);
            }

            DeviceInfo info = (DeviceInfo)getItem(position);

            if (info != null) {
                TextView textName = (TextView) view.findViewById(R.id.text_bcore_name);
                textName.setText(info.getName());
                TextView textAddr = (TextView) view.findViewById(R.id.text_bcore_addr);
                textAddr.setText(info.getAddr());
            }

            return view;
        }
    }

    private ListView listViewBcore;
    private Button buttonScan;
    private ProgressBar progressScan;

    private BcoreScanner bcoreScanner;
    private BcoreAdapter bcoreAdapter;
    private ArrayList<DeviceInfo> advertisingBcoreInfos;

    private SQLiteDatabase boreInfoDB;

    private BcoreScannerListener bcoreScannerListener = new BcoreScannerListener() {
        @Override
        public void onFoundBcore(String name, String addr) {

            BcoreInfo info  = BcoreInfoCatHands.findByDeviceAddr(boreInfoDB, addr);
            if (info != null) name = info.getDisplayName();

            DeviceInfo devInfo = new DeviceInfo(name, addr);
            if (!advertisingBcoreInfos.contains(devInfo)) {
                advertisingBcoreInfos.add(devInfo);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bcoreAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public void onScanCompleted() {
            stoppedScan();
        }
    };

    private ListView.OnItemClickListener listViewBcoreItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DeviceInfo info = advertisingBcoreInfos.get(position);

            if (info == null) return;

            Intent intent = new Intent(MainActivity.this, BcoreControllerActivity.class);
            intent.putExtra(BcoreControllerActivity.KEY_BCORE_NAME, info.getName());
            intent.putExtra(BcoreControllerActivity.KEY_BCORE_ADDR, info.getAddr());
            startActivity(intent);

            if (bcoreScanner.isScanning()) stoppedScan();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        BcoreInfoOpenHelper helper = new BcoreInfoOpenHelper(getApplicationContext());
        boreInfoDB = helper.getWritableDatabase();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bcoreScanner = new BcoreScanner(this);
        bcoreScanner.setOnBcoreScannerListener(bcoreScannerListener);

        progressScan = (ProgressBar) findViewById(R.id.progressBar);

        buttonScan = (Button) findViewById(R.id.btn_scan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bcoreScanner.isScanning()) {
                    stopScan();
                } else {
                    startScan();
                }
            }
        });

        listViewBcore = (ListView) findViewById(R.id.bcore_list_view);
        advertisingBcoreInfos = new ArrayList<>();
        bcoreAdapter = new BcoreAdapter(this, advertisingBcoreInfos);
        listViewBcore.setAdapter(bcoreAdapter);
        listViewBcore.setOnItemClickListener(listViewBcoreItemClickListener);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_CODE_ALLOW_LOCATION);
        }
    }

    private void requestBluetoothEnable() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQ_CODE_ALLOW_BLUETOOTH);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQ_CODE_ALLOW_LOCATION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, R.string.msg_use_permission_location, Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_ALLOW_BLUETOOTH) {
            if (!bcoreScanner.isBluetoothEnabled()) {
                Toast.makeText(this, R.string.msg_set_bt_enable, Toast.LENGTH_LONG).show();
                finish();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestLocationPermission();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!bcoreScanner.isBluetoothEnabled()) {
            requestBluetoothEnable();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestLocationPermission();
        }
        initInfos();
    }

    private void initInfos() {
        advertisingBcoreInfos.clear();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bcoreAdapter.notifyDataSetChanged();
            }
        });
    }

    private void startScan() {
        initInfos();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonScan.setText(R.string.btn_scan_stop);
                progressScan.setVisibility(View.VISIBLE);
            }
        });

        bcoreScanner.startScan();
    }

    private void stopScan(){
        bcoreScanner.stopScan();
    }

    private void stoppedScan() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonScan.setText(R.string.btn_scan_start);
                progressScan.setVisibility(View.INVISIBLE);
            }
        });
    }
}
