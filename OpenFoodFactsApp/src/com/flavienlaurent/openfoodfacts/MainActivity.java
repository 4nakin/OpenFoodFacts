package com.flavienlaurent.openfoodfacts;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_CODE_SCAN = 1;

    private View mLoading;
    private TextView mText;
    private ImageView mImage;
    private TextView mBarCode;
    private TextView mTitle;

    private WebService mWebService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebService();
        setContentView(R.layout.activity_main);
        bindViews();
        setupView();
    }

    private void bindViews() {
        mLoading = findViewById(R.id.loading);
        mText = (TextView) findViewById(R.id.text);
        mImage = (ImageView) findViewById(R.id.image);
        mBarCode = (TextView) findViewById(R.id.barcode);
        mTitle = (TextView) findViewById(R.id.title);
    }

    private void initWebService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer("http://fr.openfoodfacts.org/api/v0")
                .build();

        mWebService = restAdapter.create(WebService.class);
    }

    private void setupView() {

    }

    private void clearImage() {
        mImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mImage.setImageResource(R.drawable.ic_stub_image);
    }

    private void setBarCodeSearching() {
        mBarCode.setText(R.string.searching);
    }

    private void clearBarCode() {
        mBarCode.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
            case R.id.action_scan:
                onMenuScan();
                return true;
        }
        return false;
    }

    private void onMenuScan() {
        startActivityForResult(new Intent(this, ScanActivity.class), REQUEST_CODE_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            String barcode = data.getStringExtra(ScanActivity.EXTRA_BARCODE);
            String type = data.getStringExtra(ScanActivity.EXTRA_TYPE);
            mBarCode.setText(barcode + " (" + type + ")");
            if("EAN13".equals(type)) {
                getProduct(barcode);
            } else {
                //TODO show crouton
            }
        }
    }

    private void getProduct(String barcode) {
       mWebService.getProduct(barcode, new Callback<OFFResponse>() {

            @Override
            public void success(OFFResponse offResponse, Response response) {
                if(offResponse != null) {
                    OFFProduct product = offResponse.product;
                    if(product != null) {
                        Picasso.with(MainActivity.this)
                                .load(offResponse.product.image_small_url)
                                .placeholder(R.drawable.ic_stub_image)
                                .error(R.drawable.ic_stub_image)
                                .centerCrop().fit()
                                .into(mImage);

                    }
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                //TODO show crouton
            }
        });
    }
}
