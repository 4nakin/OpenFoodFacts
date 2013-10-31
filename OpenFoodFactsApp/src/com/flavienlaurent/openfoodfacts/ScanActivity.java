package com.flavienlaurent.openfoodfacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

public class ScanActivity extends Activity implements ScanditSDKListener {

    public static final String EXTRA_BARCODE = "EXTRA_BARCODE";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";

    private static final String sScanditSdkAppKey = "38zmIkJGEeOKtg4A7aHuVjU5JK/fHIx17gYLyQRezYc";

    private ScanditSDKAutoAdjustingBarcodePicker mBarcodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();

        initializeAndStartBarcodeScanning();
    }

    private void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onPause() {
        mBarcodePicker.stopScanning();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        mBarcodePicker.startScanning();
        super.onResume();
    }

    public void initializeAndStartBarcodeScanning() {
        mBarcodePicker = new ScanditSDKAutoAdjustingBarcodePicker(this, sScanditSdkAppKey, ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);
        setContentView(mBarcodePicker);

        mBarcodePicker.getOverlayView().addListener(this);
        mBarcodePicker.getOverlayView().showSearchBar(false);
    }

    public void didScanBarcode(String barcode, String type) {
        Intent data = new Intent();
        data.putExtra(EXTRA_BARCODE, barcode);
        data.putExtra(EXTRA_TYPE, type);
        setResult(RESULT_OK, data);
        finish();
    }

    public void didManualSearch(String entry) {
        //DO NOTHING
    }

    @Override
    public void didCancel() {
        finish();
    }
}