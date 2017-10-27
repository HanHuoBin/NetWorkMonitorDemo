package com.hb.network;

import android.widget.ImageView;
import android.widget.TextView;

import com.hb.network.utils.PackageUtils;

public class MainActivity extends BaseActivity {

    private ImageView logoImg;
    private TextView versionTv;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initToolBar();
        logoImg = (ImageView) findViewById(R.id.img_logo);
        versionTv = (TextView) findViewById(R.id.tv_version);
    }

    @Override
    protected void initData() {
        setActionTitle(getString(R.string.main));
        logoImg.setImageDrawable(getResources().getDrawable(BuildConfig.LOGO));
        versionTv.setText(BuildConfig.APP_NAME + getString(R.string.version) + " " + PackageUtils.getCurrentVersion());
    }

}
