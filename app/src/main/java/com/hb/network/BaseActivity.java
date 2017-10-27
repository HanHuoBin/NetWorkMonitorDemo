package com.hb.network;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.network.dialog.MyAlertDialog;
import com.hb.network.network.NetBroadcastReceiver;
import com.hb.network.network.NetUtil;

import java.util.Date;

/**
 * Created by hb on 16/3/29.
 */
public abstract class BaseActivity
        extends AppCompatActivity implements NetBroadcastReceiver.NetChangeListener {
    //双击退出
    private long mLastBackTime = 0;
    private long TIME_DIFF = 2 * 1000;

    public static NetBroadcastReceiver.NetChangeListener listener;
    private MyAlertDialog alertDialog = null;
    private Toolbar mToolbar;

    /**
     * 网络类型
     */
    private int netType;

    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract void initData();

    private NetBroadcastReceiver netBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //全部禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        listener = this;
        //Android 7.0以上需要动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            netBroadcastReceiver = new NetBroadcastReceiver();
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }
        checkNet();
        initView();
        initData();
    }

    public void initToolBar() {
        mToolbar = findView(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setTooBarBackBtn() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置导航栏标题
     *
     * @param title
     */
    protected final void setActionTitle(String title) {
        TextView titleTv = findView(R.id.tv_action_title);
        if (titleTv != null && !TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
    }

    /**
     * 初始化时判断有没有网络
     */
    public boolean checkNet() {
        this.netType = NetUtil.getNetWorkState(BaseActivity.this);
        if (!isNetConnect()) {
            //网络异常，请检查网络
            showNetDialog();
            T.showShort("网络异常，请检查网络，哈哈");
        }
        return isNetConnect();
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onChangeListener(int netMobile) {
        // TODO Auto-generated method stub
        this.netType = netMobile;
        Log.i("netType", "netType:" + netMobile);
        if (!isNetConnect()) {
            showNetDialog();
            T.showShort("网络异常，请检查网络，哈哈");
        } else {
            hideNetDialog();
            T.showShort("网络恢复正常");
        }
    }

    /**
     * 隐藏设置网络框
     */
    private void hideNetDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        alertDialog = null;
    }

    /**
     * 弹出设置网络框
     */
    private void showNetDialog() {
        if (alertDialog == null) {
            alertDialog = new MyAlertDialog(this).builder().setTitle("网络异常")
                    .setNegativeButton("取消", new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).setPositiveButton("设置", new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    }).setCancelable(false);
        }
        alertDialog.show();
        showMsg("网络异常，请检查网络");
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netType == 1) {
            return true;
        } else if (netType == 0) {
            return true;
        } else if (netType == -1) {
            return false;
        }
        return false;
    }

    /**
     * 通过控件的Id获取对应的控件
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    protected final <U extends View> U findView(int viewId) {
        View view = findViewById(viewId);
        return (U) view;
    }

    /**
     * 给id设置监听
     *
     * @param ids
     */
    protected final void setOnClick(int... ids) {
        if (ids == null) {
            return;
        }
        for (int i : ids) {
            setOnClick(this.findView(i));
        }
    }

    /**
     * 给view设置监听
     *
     * @param params
     */
    protected final void setOnClick(View... params) {
        if (params == null) {
            return;
        }

        for (View view : params) {
            if (view != null && this instanceof OnClickListener) {
                view.setOnClickListener((OnClickListener) this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void finishActivity() {
        finish();
    }

    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isTaskRoot()) {
                long now = new Date().getTime();
                if (now - mLastBackTime < TIME_DIFF) {
                    return super.onKeyDown(keyCode, event);
                } else {
                    mLastBackTime = now;
                    showMsg("再按一次返回键退出");
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
