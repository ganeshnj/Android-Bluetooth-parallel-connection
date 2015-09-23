package com.ensisa.ganesh.abluetoothdriver;

import android.bluetooth.BluetoothDevice;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ensisa.ganesh.abluetoothdriver.common.activities.LoggerActivity;
import com.ensisa.ganesh.abluetoothdriver.common.fragments.BluetoothFragment;
import com.ensisa.ganesh.abluetoothdriver.common.logger.Log;
import com.ensisa.ganesh.abluetoothdriver.common.logger.LogFragment;
import com.ensisa.ganesh.abluetoothdriver.common.logger.LogWrapper;
import com.ensisa.ganesh.abluetoothdriver.common.logger.MessageOnlyLogFilter;
import com.ensisa.ganesh.abluetoothdriver.common.SlidingTabLayout;
import com.ensisa.ganesh.abluetoothdriver.fora.balance.BalanceConstant;
import com.ensisa.ganesh.abluetoothdriver.fora.balance.BalanceFragment;
import com.ensisa.ganesh.abluetoothdriver.fora.pressure.PressureConstant;
import com.ensisa.ganesh.abluetoothdriver.fora.pressure.PressureFragment;
import com.ensisa.ganesh.abluetoothdriver.fora.temperature.TemperatureConstant;
import com.ensisa.ganesh.abluetoothdriver.fora.temperature.TemperatureFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends LoggerActivity implements ActivityCommunicator {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private BluetoothAdapter mBluetoothAdapter = null;
    public BluetoothService mBluetoothService = null;
    private BluetoothHandler mBluetoothHandler = null;

    private List<BluetoothFragment> mFragments;

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTabs;
        }

        @Override
        public Fragment getItem(int position) {
            if(mFragments.size()>0){
                return mFragments.get(position);
            }
            return new Fragment();
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getArguments().getString(TemperatureFragment.ARG_NAME);
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

    }


    Toolbar mToolbar;
    private int mTabs = 0;
    ViewPagerAdapter mFragmentPagerAdapter;
    ViewPager mViewPager;
    SlidingTabLayout mSlidingTabLayout;

    boolean mLogShown = true;
    LogFragment mLogFragment;
    MenuItem logToggle;

    public FragmentCommunicator fragmentCommunicator1;
    public FragmentCommunicator fragmentCommunicator2;
    public FragmentCommunicator fragmentCommunicator3;

    public HashMap<String, FragmentCommunicator> mFragmentCommunicators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Bluetooth driver for e-care");

//        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(mToolbar);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mFragmentCommunicators = new HashMap<>();

        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

        mBluetoothHandler = new BluetoothHandler();
        mBluetoothHandler.setBluetoothListener(new IBluetoothListener() {
            @Override
            public void onWrite(Message msg, int length, byte[] buffer) {
                String deviceName = msg.getData().getString(Constants.DEVICE_NAME);
                String deviceAddress = msg.getData().getString(Constants.DEVICE_ADDRESS);

                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < length; i++) {
                    stringBuffer.append(Integer.toHexString(buffer[i]));
                    stringBuffer.append(" ");
                }
                Log.d(TAG, "Written: " + stringBuffer + " to " + deviceName + " " + deviceAddress);
            }

            @Override
            public void onRead(Message msg, int length, byte[] buffer) {
                String deviceName = msg.getData().getString(Constants.DEVICE_NAME);
                String deviceAddress = msg.getData().getString(Constants.DEVICE_ADDRESS);

                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < length; i++) {
                    stringBuffer.append(Integer.toHexString(buffer[i]));
                    stringBuffer.append(" ");
                }

                Log.d(TAG, "Read: " + stringBuffer + " from " + deviceName + " " + deviceAddress);

                FragmentCommunicator fragmentCommunicator = mFragmentCommunicators.get(deviceAddress);
                if(fragmentCommunicator!=  null){
                    fragmentCommunicator.passDataToFragment(buffer, length, deviceAddress);
                }
            }

            @Override
            public void onLost(Message msg) {
                String deviceName = msg.getData().getString(Constants.DEVICE_NAME);
                String deviceAddress = msg.getData().getString(Constants.DEVICE_ADDRESS);
                Log.d(TAG, "Connection lost with " + deviceName + " " + deviceAddress);
                Toast.makeText(getApplicationContext(), "Connection lost with " + deviceName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Message msg) {
                String deviceName = msg.getData().getString(Constants.DEVICE_NAME);
                String deviceAddress = msg.getData().getString(Constants.DEVICE_ADDRESS);
                Toast.makeText(getApplicationContext(), "Failed to connect with " + deviceName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnected(Message msg, Bundle bundle) {
                String deviceName = bundle.getString(Constants.DEVICE_NAME);
                String deviceAddress = bundle.getString(Constants.DEVICE_ADDRESS);
                Log.d(TAG, "Connected to " + deviceName + " " + deviceAddress);

                Bundle fragmentBundle = new Bundle();
                fragmentBundle.putString(TemperatureFragment.ARG_NAME, deviceName);
                fragmentBundle.putString(TemperatureFragment.ARG_ADDRESS, deviceAddress);

                if (deviceName.equals(TemperatureConstant.DEVICE_NAME)) {
                    TemperatureFragment temperatureFragment = new TemperatureFragment();
                    temperatureFragment.setArguments(fragmentBundle);
                    addFragment(temperatureFragment, deviceAddress);
                }
                else if (deviceName.equals(PressureConstant.DEVICE_NAME)) {
                    PressureFragment pressureFragment = new PressureFragment();
                    pressureFragment.setArguments(fragmentBundle);
                    addFragment(pressureFragment, deviceAddress);
                }
                else if(deviceName.equals(BalanceConstant.DEVICE_NAME)) {
                    BalanceFragment balanceFragment = BalanceFragment.newInstance(deviceName, deviceAddress);
                    addFragment(balanceFragment, deviceAddress);
                }
//                FragmentCommunicator fragmentCommunicator = null;
//                mFragmentCommunicators.put(deviceAddress,fragmentCommunicator);
                Toast.makeText(getApplicationContext(), "Connected with " + deviceName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnecting(Message msg, Bundle bundle) {
                Log.d(TAG, "Connecing to " + bundle.getString(Constants.DEVICE_NAME) + " "
                        + (bundle.getString(Constants.DEVICE_ADDRESS)));
            }

            @Override
            public void onListen(Message msg) {

            }

            @Override
            public void onNone(Message msg) {

            }

            @Override
            public void onError(Message msg) {

            }
        });

        mFragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.vpPager);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        mSlidingTabLayout.setViewPager(mViewPager);

        mFragments = new ArrayList<BluetoothFragment>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mBluetoothService != null){
            mBluetoothService.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth  services
                mBluetoothService.start();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If BT is not on, request that it be enabled.
        // setup() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the  session
        } else if (mBluetoothService == null) {
            setupCommunication();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        logToggle = menu.findItem(R.id.menu_toggle_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if( id==R.id.secure_connect_scan){
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        }
        else if(id == R.id.menu_toggle_log){
            mLogShown = !mLogShown;
            logToggle.setTitle(mLogShown ? "Show log" : "Hide log");
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_log_fragment);
            if (mLogShown) {
                linearLayout.setVisibility(View.INVISIBLE);
            }
            else
            {
                linearLayout.setVisibility(View.VISIBLE);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
         mLogFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());

        Log.i(TAG, "Ready");
    }

    private void setupCommunication() {
        Log.d(TAG, "setupCommnunication");

        mBluetoothService = new BluetoothService(getApplicationContext(), mBluetoothHandler);
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    private void setStatus(int resId) {
        final ActionBar actionBar = getSupportActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    private void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getSupportActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    private void connectDevice(String address, boolean secure) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService.connect(device, secure);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data.getStringExtra(Constants.DEVICE_ADDRESS), true);

                }
                break;
        }
    }

    public void addFragment(BluetoothFragment f, String deviceAddress){
        BluetoothFragment temp = isFragmentExists(deviceAddress);
        if(temp != null){
            mFragments.remove(temp);
            mTabs--;
            mFragmentPagerAdapter.notifyDataSetChanged();
            mSlidingTabLayout.setViewPager(mViewPager);
        }
        mFragments.add(f);
        mTabs++;
        mFragmentPagerAdapter.notifyDataSetChanged();
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    public BluetoothFragment isFragmentExists(String deviceAddress){
        for(BluetoothFragment bluetoothFragment: mFragments){
            if(bluetoothFragment.getDeviceAddress().equals(deviceAddress))
                return bluetoothFragment;
        }
        return null;
    }

    @Override
    public void passDataToActivity(Fragment fragment) {

    }
}
