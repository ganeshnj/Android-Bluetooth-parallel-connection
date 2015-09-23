package com.ensisa.ganesh.abluetoothdriver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ensisa.ganesh.abluetoothdriver.common.logger.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ganesh on 5/25/2015.
 */
public class BluetoothService {

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothSecure";
    private static final String NAME_INSECURE = "BluetoothInsecure";

    private static final String TAG = "BluetoothService";

    private List<UUID> mListUUID;
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private ConnectThread mConnectThread;
    //private AcceptThread mAcceptThread;
    private HashMap<String, BluetoothSocket> mSockets;
    private HashMap<String, ConnectedThread> mConnectedThreads;
    private HashMap<String, UUID> mAssignedUuids;
    private ArrayList<String> mAddresses;
    private int mState;

    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;

    public BluetoothService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mSockets = new HashMap<String, BluetoothSocket>();
        mConnectedThreads = new HashMap<String, ConnectedThread>();
        mListUUID = new ArrayList<UUID>();
        mListUUID.add(UUID.fromString("00001101-0000-1000-8000-00805F9B34FA"));
        mListUUID.add(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        mListUUID.add(UUID.fromString("00001101-0000-1000-8000-00805F9B34FC"));
        mAddresses = new ArrayList<String>();
        mAssignedUuids = new HashMap<String, UUID>();
        mState = STATE_NONE;
        mHandler = handler;
    }


    private UUID getFreeUUID() {
        if(!mListUUID.isEmpty()) {
            if(!mAssignedUuids.isEmpty()){
                for(int i=0; i<mListUUID.size(); i++){
                    if(!mAssignedUuids.containsValue(mListUUID.get(i))){
                        return mListUUID.get(i);
                    }
                }
            }
            return mListUUID.get(0);
        }
        return null;
    }

    private synchronized void setState(int state) {
        //Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(MessageType.MESSAGE_STATE_CHANGE.getValue(), state, -1).sendToTarget();
    }

    public synchronized int getState() {
        return mState;
    }

    public synchronized void start() {
        Log.d(TAG, "Start");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        setState(STATE_LISTEN);

        // Start the thread to listen on a BluetoothServerSocket
//        if (mAcceptThread == null) {
//            mAcceptThread = new AcceptThread(false);
//            mAcceptThread.start();
//        }
    }

    public synchronized void connect(BluetoothDevice device, boolean secure) {
        // Send the name of the connecting device back to the UI Activity
        Message msg = mHandler.obtainMessage(MessageType.MESSAGE_STATE_CONNECTING.getValue());
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        bundle.putString(Constants.DEVICE_ADDRESS, device.getAddress());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        Log.d(TAG, "Connecting to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {
        Log.d(TAG, "Connected to:" + device + " Socket Type:" + socketType);

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

//        if (mAcceptThread != null) {
//            mAcceptThread.cancel();
//            mAcceptThread = null;
//        }

        // Start the thread to manage the connection and perform transmissions
        ConnectedThread connectedThread = new ConnectedThread(socket, socketType, device.getAddress());
        connectedThread.start();
        mConnectedThreads.put(device.getAddress(), connectedThread);

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(MessageType.MESSAGE_STATE_CONNECTED.getValue());
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        bundle.putString(Constants.DEVICE_ADDRESS, device.getAddress());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    public synchronized void stop() {
        Log.d(TAG, "Stopped");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

//        if (mAcceptThread != null) {
//            mAcceptThread.cancel();
//            mAcceptThread = null;
//        }

        for(Map.Entry<String, ConnectedThread> connectedThreadEntry: mConnectedThreads.entrySet()){
            connectedThreadEntry.getValue().cancel();
        }

        mConnectedThreads = new HashMap<String, ConnectedThread>();
        setState(STATE_NONE);
    }

    public void write(byte[] out, String address) {
        ConnectedThread r = mConnectedThreads.get(address);
        r.write(out);
//        try {
//            r.sleep(400);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private void connectionFailed(BluetoothDevice device) {
        Log.d(TAG, "Unable to connect " + device.getAddress() );
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MessageType.MESSAGE_TOAST.getValue());
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        // Start the service over to restart listening mode
        BluetoothService.this.start();
    }

    private void connectionLost(String address) {
        //Log.d(TAG, "Connection lost with " + address);
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MessageType.MESSAGE_CONNECTION_LOST.getValue());
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, mSockets.get(address).getRemoteDevice().getName());
        bundle.putString(Constants.DEVICE_ADDRESS, address);
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mAddresses.remove(address);
        mSockets.remove(address);
        mConnectedThreads.remove(address);

        // Start the service over to restart listening mode
        //BluetoothService.this.start();
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;

        public AcceptThread(boolean secure) {
            BluetoothServerSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Create a new listening server socket
            try {
                if (secure) {
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,
                            getFreeUUID());
                } else {
                    tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
                            NAME_INSECURE, getFreeUUID());
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "Socket Type: " + mSocketType +
                    "BEGIN mAcceptThread" + this);
            setName("AcceptThread" + mSocketType);

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothService.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice(),
                                        mSocketType);
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);

        }

        public void cancel() {
            Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;
        private UUID mmUuid;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            try {
                mmUuid = getFreeUUID();
                tmp = device.createRfcommSocketToServiceRecord(mmUuid);
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
                connectionFailed(mmDevice);
                Log.i(TAG, e.getMessage());
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            mAssignedUuids.put(mmDevice.getAddress(), mmUuid);
            mSockets.put(mmDevice.getAddress(), mmSocket);
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private String address;
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket, String socketType, String address) {
            this.setName("ConnectedThread:"+address);
            Log.d(TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            this.address = address;
        }

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d(TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    //int byteAvailalbe = mmInStream.available();
                    bytes = mmInStream.read(buffer);
                    //if(byteAvailalbe > 0) {
                        // Send the obtained bytes to the UI Activity
//                        mHandler.obtainMessage(MessageType.MESSAGE_READ.getValue(), bytes, -1, buffer)
//                                .sendToTarget();

                    Message msg = mHandler.obtainMessage(MessageType.MESSAGE_READ.getValue());
                    msg.arg1 = bytes;
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DEVICE_NAME, mSockets.get(address).getRemoteDevice().getName());
                    bundle.putString(Constants.DEVICE_ADDRESS, address);
                    bundle.putByteArray(Constants.BUFFER, buffer);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                    //}

                } catch (IOException e) {
                    //Log.e(TAG, "disconnected", e);
                    connectionLost(address);
                    cancel();
                    // Start the service over to restart listening mode
                    //BluetoothService.this.start();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                Message msg = mHandler.obtainMessage(MessageType.MESSAGE_WRITE.getValue());
                msg.arg1 = buffer.length;
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DEVICE_NAME, mSockets.get(address).getRemoteDevice().getName());
                bundle.putString(Constants.DEVICE_ADDRESS, address);
                bundle.putByteArray(Constants.BUFFER, buffer);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
               // this.sleep(200);

            } catch (IOException e) {
                Log.e(TAG, "IOException in sendMessage - Dest:" + address + ", Msg:" + buffer, e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}


