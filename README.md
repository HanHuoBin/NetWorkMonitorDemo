# NetWorkMonitorDemo

[本地网络变化检测](#本地网络变化检测)

[gradle多厂商配置，动态配置icon，appname，主题色等等，一次性搞定](doc/gradle.md#gradle多厂商配置，动态配置icon，appname，主题色等等，一次性搞定)

# 本地网络变化检测

<img src="file/network.gif" width="360" height="640" alt=""/>

<img src="file/network1.gif" width="360" height="640" alt=""/>

## 编写判断网络帮助类 NetUtil

```
public static int getNetWorkState(Context context) {
    //得到连接管理器对象
    ConnectivityManager connectivityManager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetworkInfo = connectivityManager
            .getActiveNetworkInfo();
    //如果网络连接，判断该网络类型
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
        if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
            return NETWORK_WIFI;//wifi
        } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
            return NETWORK_MOBILE;//mobile
        }
    } else {
        //网络异常
        return NETWORK_NONE;
    }
    return NETWORK_NONE;
}
```

## 编写检测网络变化广播类

```
public class NetBroadcastReceiver extends BroadcastReceiver {

    public NetChangeListener listener = BaseActivity.listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        Log.i("NetBroadcastReceiver", "NetBroadcastReceiver changed");
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 当网络发生变化，判断当前网络状态，并通过NetEvent回调当前网络状态
            if (listener != null) {
                listener.onChangeListener(netWorkState);
            }
        }
    }

    // 自定义接口
    public interface NetChangeListener {
        void onChangeListener(int status);
    }

}
```

## 注册广播

```
<receiver android:name=".network.NetBroadcastReceiver">
    <intent-filter>
        <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
    </intent-filter>
</receiver>
```

这里有一个问题，在Android 7.0之静态注册广播的方式被取消了，所以我们这里采用动态注册的方式

```
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    //实例化IntentFilter对象
    IntentFilter filter = new IntentFilter();
    filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    netBroadcastReceiver = new NetBroadcastReceiver();
    //注册广播接收
    registerReceiver(netBroadcastReceiver, filter);
}
```

## 创建一个回调当前网络状态值的接口类

```
// 自定义接口
public interface NetChangeListener {
    void onChangeListener(int status);
}
```

并在BaseActivity中实现该接口，广播检测到网络变化时，通过onChangeListener回调当前网络状态至BaseActivity

```
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
        T.showShort("网络异常，请检查网络");
    } else {
        hideNetDialog();
        T.showShort("网络恢复正常");
    }
}
```

这里写了个dialog用来提示网络的变化 showNetDialog() 和 hideNetDialog()分别用来显示和隐藏dialog

```
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
```

通过Intent跳转到网络设置页面

```
Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
startActivity(intent);
```