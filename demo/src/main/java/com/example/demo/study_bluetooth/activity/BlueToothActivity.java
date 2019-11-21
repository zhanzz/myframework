package com.example.demo.study_bluetooth.activity;

import android.Manifest;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.study_bluetooth.adapter.ExpandAdapter;
import com.example.demo.study_bluetooth.presenter.BlueToothPresenter;
import com.example.demo.study_bluetooth.view.IBlueToothView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.ToastUtil;
import com.framework.model.demo.BlueTooThBean;
import com.framework.model.demo.BlueTooThHeadBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class BlueToothActivity extends BaseActivity implements IBlueToothView {
    private static final int REQUEST_ENABLE_BT = 100;
    @BindView(R2.id.btn_bonded)
    Button btnBonded;
    @BindView(R2.id.btn_found)
    Button btnFound;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    private BlueToothPresenter mPresenter;
    private BlueTooThHeadBean mBonded, mFound;
    private ExpandAdapter adapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothA2dp mA2dp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_blue_tooth;
    }

    @Override
    public void bindData() {
        adapter = new ExpandAdapter(recyclerView);
        if (!isSupportBluetooth()) {
            // Device doesn't support Bluetooth
            ToastUtil.show(this, "设备不支持蓝牙");
        } else if (!isBluetoothOpen()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            //mBluetoothAdapter.enable();
        } else {
            init();
        }
    }

    /**
     * 设备是否支持蓝牙
     **/
    public boolean isSupportBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            return true;
        }
        return false;
    }

    /**
     * 蓝牙是否已经启动
     **/
    public boolean isBluetoothOpen() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.isEnabled();
        }
        return false;
    }

    private void init() {
        mBonded = new BlueTooThHeadBean();
        mBonded.name = "已配对的设备";
        mBonded.setExpanded(true);
        mFound = new BlueTooThHeadBean();
        mFound.name = "已发现的设备";
        mFound.setExpanded(true);
        List<MultiItemEntity> list = new ArrayList<>();
        list.add(mBonded);
        list.add(mFound);

        adapter.setNewData(list);
        recyclerView.setAdapter(adapter);

        getBondeds();
        getBluetoothA2DP();
    }

    private void getBondeds() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            List<BlueTooThBean> beans = new ArrayList<>();
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                BlueTooThBean bean = new BlueTooThBean();
                bean.name = deviceName;
                bean.address = deviceHardwareAddress;
                bean.device = device;
                beans.add(bean);
            }
            mBonded.subItems = beans;
            adapter.notifyExpandDataChange();
        }
    }

    @Override
    public void initEvent() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity itemEntity = (MultiItemEntity) adapter.getItem(position);
                if (itemEntity instanceof BlueTooThBean) {
                    BluetoothDevice device = ((BlueTooThBean) itemEntity).device;
                    createBond(device);
                    //connectA2dp(device);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            init();
        }
    }

    private void connectA2dp(BluetoothDevice device){
        mBluetoothAdapter.cancelDiscovery();
        setPriority(device, 100); //设置priority
        try {
            //通过反射获取BluetoothA2dp中connect方法（hide的），进行连接。
            Method connectMethod =BluetoothA2dp.class.getMethod("connect",
                    BluetoothDevice.class);
            Object o = connectMethod.invoke(mA2dp, device);
            LogUtil.e(o.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disConnectA2dp(BluetoothDevice device){
        setPriority(device, 0);
        try {
            //通过反射获取BluetoothA2dp中connect方法（hide的），断开连接。
            Method connectMethod =BluetoothA2dp.class.getMethod("disconnect",
                    BluetoothDevice.class);
            connectMethod.invoke(mA2dp, device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPriority(BluetoothDevice device, int priority) {
        if (mA2dp == null) return;
        try {//通过反射获取BluetoothA2dp中setPriority方法（hide的），设置优先级
            Method connectMethod =BluetoothA2dp.class.getMethod("setPriority",
                    BluetoothDevice.class,int.class);
            connectMethod.invoke(mA2dp, device, priority);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new BlueToothPresenter();
        }
        return mPresenter;
    }

    @OnClick({R2.id.btn_bonded, R2.id.btn_found})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_bonded) {
            getBondeds();
        } else if (i == R.id.btn_found) {
            requestNeedPermissions(200, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    public void passPermission(@NonNull List<String> permissions, int requestCode) {
        if (requestCode == 200 && permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            findBluetoothDevice();
        }
    }

    /**
     * 发现新设备
     **/
    public void findBluetoothDevice() {
        //其实是启动了一个异步线程，该方法将立即返回一个布尔值，指示发现是否已成功启动。
        // 发现过程通常涉及大约12秒的查询扫描，随后是每个找到的设备的页面扫描以检索其蓝牙名称
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && !mBluetoothAdapter.isDiscovering()) {
            if (mBluetoothAdapter.startDiscovery()) {
                LogUtil.i("=======已成功启动寻找新设备的异步线程=======");
                mBluetoothAdapter.startDiscovery();
            } else {
                LogUtil.i("=======启动寻找新设备的异步线程失败=======");
            }
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device;
            switch (action) {
                case BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED:
                    switch (intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1)) {
                        case BluetoothA2dp.STATE_CONNECTING:
                            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            LogUtil.i("device: " + device.getName() + " connecting");
                            break;
                        case BluetoothA2dp.STATE_CONNECTED:
                            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            LogUtil.i("device: " + device.getName() + " connected");
                            deviceConnected(device);
                            break;
                        case BluetoothA2dp.STATE_DISCONNECTING:
                            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            LogUtil.i("device: " + device.getName() + " disconnecting");
                            break;
                        case BluetoothA2dp.STATE_DISCONNECTED:
                            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            LogUtil.i("device: " + device.getName() + " disconnected");
                            break;
                        default:
                            break;
                    }
                    break;
                case BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED:
                    int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1);
                    switch (state) {
                        case BluetoothA2dp.STATE_PLAYING:
                            LogUtil.i("state: playing.");
                            break;
                        case BluetoothA2dp.STATE_NOT_PLAYING:
                            LogUtil.i("state: not playing");
                            break;
                        default:
                            LogUtil.i("state: unkown");
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    int deviceClassType = device.getBluetoothClass().getDeviceClass();
                    //找到指定的蓝牙设备(蓝牙耳机或音箱)
                    if (deviceClassType == BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET
                            || deviceClassType == BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES) {
                        LogUtil.i("Found device:" + device.getName() + "   address:" + device.getAddress());
                        if (!TextUtils.isEmpty(device.getName())) {
                            //添加到设备列表
                            deviceFound(device);
                            LogUtil.e("zhang",Arrays.toString(device.getUuids()));
                        }
                    } else {//找到可用蓝牙
//                        if (!TextUtils.isEmpty(device.getName())) {
//                            LogUtil.i("=====Found device====11===" + device.getName() + "   address:" + device.getAddress());
//                            //添加到设备列表
//                            deviceFound(device);
//                        }
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (bondState) {
                        case BluetoothDevice.BOND_BONDED:  //配对成功
                            LogUtil.i("Device:" + device.getName() + " bonded.");
                            //取消搜索，连接蓝牙设备
                            deviceBonded(device);
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            LogUtil.i("Device:" + device.getName() + " bonding.");
                            break;
                        case BluetoothDevice.BOND_NONE:
                            LogUtil.i("Device:" + device.getName() + " not bonded.");
                            //不知道是蓝牙耳机的关系还是什么原因，经常配对不成功
                            //配对不成功的话，重新尝试配对
                            deviceBondNone(device);
                            break;
                        default:
                            break;
                    }
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    switch (state) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            LogUtil.i("BluetoothAdapter is turning on.");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            LogUtil.i("BluetoothAdapter is on.");
//                        //蓝牙已打开，开始搜索并连接service
                            requestNeedPermissions(200, Manifest.permission.ACCESS_COARSE_LOCATION);
                            getBluetoothA2DP();

                            blootoothStateOn();
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            LogUtil.i("BluetoothAdapter is turning off.");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            LogUtil.i("BluetoothAdapter is off.");
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static  boolean createBond(BluetoothDevice btDevice){
        boolean result = false;
        try{
            Method m = btDevice.getClass().getDeclaredMethod("createBond",new Class[]{});
            m.setAccessible(true);
            Boolean originalResult = (Boolean) m.invoke(btDevice);
            result = originalResult.booleanValue();
        }catch(Exception ex){
        }
        return result;
    }

    private void getBluetoothA2DP() {
        //获取A2DP代理对象
        mBluetoothAdapter.getProfileProxy(this, mListener, BluetoothProfile.A2DP);
    }

    private BluetoothProfile.ServiceListener mListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.A2DP) {
                mA2dp = null;
            }
        }

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.A2DP) {
                mA2dp = (BluetoothA2dp) proxy; //转换
            }
        }
    };

    private void blootoothStateOn() {

    }

    private void deviceBondNone(BluetoothDevice device) {

    }

    private void deviceBonded(BluetoothDevice device) {
        connectA2dp(device);
    }

    private void deviceConnected(BluetoothDevice device) {

    }

    private void deviceFound(BluetoothDevice device) {
        String deviceName = device.getName();
        String deviceHardwareAddress = device.getAddress(); // MAC address

        BlueTooThBean bean = new BlueTooThBean();
        bean.name = deviceName;
        bean.address = deviceHardwareAddress;
        bean.device = device;
        if (mFound.subItems == null) {
            mFound.subItems = new ArrayList<>();
        }
        if (!mFound.subItems.contains(bean)) {
            mFound.subItems.add(bean);
        }
        adapter.notifyExpandDataChange();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, mA2dp);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, BlueToothActivity.class);
        context.startActivity(starter);
    }
}