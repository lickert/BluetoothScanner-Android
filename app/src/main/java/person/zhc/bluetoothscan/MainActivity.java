package person.zhc.bluetoothscan;

import android.Manifest;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import person.zhc.bluetoothscan.adapter.BluetoothListAdapter;
import person.zhc.bluetoothscan.utils.ToastUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="BluetoothScanner:";
    private Context mContext;
    private BluetoothAdapter bluetoothAdapter;
    private ListView mListView;
    private ArrayList<BluetoothDevice> mBluetoothList = new ArrayList<>();
    private BluetoothListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initBluetooth();
    }

    private void initView(){
        mListView = (ListView)findViewById(R.id.lv_devices);

        adapter = new BluetoothListAdapter(mContext,mBluetoothList);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initBluetooth(){
        // 注册广播接收器。接收蓝牙发现讯息
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        registerReceiver(mReceiver, mFilter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        askForPermission();
                        bluetoothAdapter.enable();
                    }
                }).start();
            } else {
                if (!bluetoothAdapter.isDiscovering()) {
                    askForPermission();
                    bluetoothAdapter.startDiscovery();
                } else {
                    ToastUtils.showShort(mContext,"正在扫描");
                }
            }
        } else {  //无蓝牙功能
            ToastUtils.showShort(mContext,"当前设备未找到蓝牙功能");
        }
    }

    private void askForPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        }
    }

    // 广播接收发现蓝牙设备
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG,"receiver action:"+action);

            switch (action){
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // 添加到ListView的Adapter中。
                    mBluetoothList.add(device);
                    adapter.notifyDataSetChanged();
                    Log.i(TAG,"MAC:"+device.getAddress()+"\n Scan Mode:"+bluetoothAdapter.getScanMode());
                    break;
            }
        }
    };

}
