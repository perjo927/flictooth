package com.preybird.webtooth;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;


public class AlertServiceFragment extends Fragment {

    public interface ServiceFragmentDelegate {
        void sendNotificationToDevices(BluetoothGattCharacteristic characteristic);
    }

    private static final UUID IMMEDIATE_ALERT_SERVICE_UUID = UUID
            .fromString("00001802-0000-1000-8000-00805f9b34fb");

    private static final UUID IMMEDIATE_ALERT_LEVEL_UUID = UUID
            .fromString("00002A06-0000-1000-8000-00805f9b34fb");
    private static final int INITIAL_IMMEDIATE_ALERT_LEVEL = 1;
    private static final String IMMEDIATE_ALERT_LEVEL_DESCRIPTION =
            "The current alert level of an immediateAlert." +
                    "\tValue 0, meaning “No Alert”\n" +
                    "\n" +
                    "•\tValue 1, meaning “Mild Alert”\n" +
                    "\n" +
                    "•\tValue 2, meaning “High Alert”";
    // Example:
    // The value 0x01 is interpreted as “Mild Alert”

    private ServiceFragmentDelegate mDelegate;


    private final View.OnClickListener mNotifyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDelegate.sendNotificationToDevices(mImmediateAlertLevelCharacteristic);
        }
    };

    // GATT
    private BluetoothGattService mImmediateAlertService;
    private BluetoothGattCharacteristic mImmediateAlertLevelCharacteristic;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AlertServiceFragment() {
        mImmediateAlertLevelCharacteristic =
                new BluetoothGattCharacteristic(IMMEDIATE_ALERT_LEVEL_UUID,
                        BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                        BluetoothGattCharacteristic.PERMISSION_READ);

        mImmediateAlertLevelCharacteristic.addDescriptor(
                MainActivity.getClientCharacteristicConfigurationDescriptor());

        mImmediateAlertLevelCharacteristic.addDescriptor(
                MainActivity.getCharacteristicUserDescriptionDescriptor(IMMEDIATE_ALERT_LEVEL_DESCRIPTION));

        mImmediateAlertService = new BluetoothGattService(IMMEDIATE_ALERT_SERVICE_UUID,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);
        mImmediateAlertService.addCharacteristic(mImmediateAlertLevelCharacteristic);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        Button notifyButton = view.findViewById(R.id.button_immediateAlertLevelNotify);
        notifyButton.setOnClickListener(mNotifyButtonListener);

        setImmediateAlertLevel(INITIAL_IMMEDIATE_ALERT_LEVEL, null);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDelegate = (ServiceFragmentDelegate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ServiceFragmentDelegate");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDelegate = null;
    }

    public BluetoothGattService getBluetoothGattService() {
        return mImmediateAlertService;
    }

    public ParcelUuid getServiceUUID() {
        return new ParcelUuid(IMMEDIATE_ALERT_SERVICE_UUID);
    }

    private void setImmediateAlertLevel(int newImmediateAlertLevel, View source) {
        mImmediateAlertLevelCharacteristic.setValue(newImmediateAlertLevel,
                BluetoothGattCharacteristic.FORMAT_UINT8, /* offset */ 0);
    }

    public int writeCharacteristic(BluetoothGattCharacteristic characteristic, int offset, byte[] value) {
        throw new UnsupportedOperationException("Method writeCharacteristic not implemented");
    }

    public void notificationsEnabled(BluetoothGattCharacteristic characteristic, boolean indicate) {
        if (characteristic.getUuid() != IMMEDIATE_ALERT_LEVEL_UUID) {
            return;
        }
        if (indicate) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.notificationsEnabled, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void notificationsDisabled(BluetoothGattCharacteristic characteristic) {
        if (characteristic.getUuid() != IMMEDIATE_ALERT_LEVEL_UUID) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.notificationsNotEnabled, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
