package com.example.rexiufudemo;

import android.app.Application;
import android.app.Person;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Keep;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

public class MyApplication extends SophixApplication {

    private final String TAG = "SophixStubApplication";

    private final String hotfixId = "30924308";
    private final String hotfixappKey = "35192603c513e9d54ba73abc0a1dd609";
    private final String rsa = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCRjoc/+NR6faL0+I/4mexZBz6Tjdb16t4R15HkJKxzCNokzs26efVU2gRzfRSl3NsVfb2TTN8KoiKokOgef2gSMQ536yVlhZGgpGwRf6lQkdtwazIP8xLMdWKEA2LhcHZI5sNU9s/1kMciZmE865d1oosRSPdbk3geXFdnzIJ41eMaf1fkenP9mxwodlvQQAPbnOdSPl5T+5sMx1Sibc4ZQ/qFrUrduwKoQHeDP+3hK9iBbxxIr2AVzyOpsrp7rpbDLd1hp7rkAvekj8SWQlRt5j0zLM+s+kMDJURdbOTJrnNAji04Uyy+fdvY27Ph3Cm9WPnGA3zE5ZZJkg4GNDvlAgMBAAECggEAJ9FdnlprhbaoxWrZt6Ajz5bzv1ml3FALv8XA2zBNHIrnYfpQvRpvym55mT42T8lDBXYITitROI9x+7roRK+FwLQ6yQNCj6siU1enWIIbxzafWPl6EHLmSZgzAvoKKrGhKgA2nx+FdB/O2dI393pbTIrSkD/tqteEnImWP4bqijNO7Fc1YyK8D1oPPJwsipgITqrLkXf+jv8sJnqwMA+a6LToIy0SEPGJlS791bUQZSIdSAokjXxI9tfxZ8WCpky7qxwW7JTjuIPzIuQ1Ia9T4KZaJbVPQTj/ulOgwSAAoOQI18y7BCclID9bYNi0k4uG0+lLs7ufQlDBx4EqN0tEAQKBgQDGzcFyFum0Ni/dvq41N5Kx0q9p+kRTNhmO/ttdj7nIpBd31sSdO2Ge9dg+WUqbgaWJUlOLoMkcQ7AFvNxO5Jv/Lq72keNUoYeMVXn1dPAbMIyq6QsyWuEyGi5MVsWk7KE5LuJHTodI7vYcmhRgMHhrIIiWbaNkU7AStGwWwEewIQKBgQC7bwn0DIHLvmvVyw+lL6FIzOkApxcsQls0humzEGvllO8KR1Z/SIJdFuzrwojTbU+rrvHNxthZtYzHVtzlpxMFXKNtfBfJVpeVP+a+e2OCAep+z8supfFMfji5W4TW+7bXM4UEoTn+b5+SAWBS8mNA9l9NHKPofjtIEAXXZSVjRQKBgDMSQJPSlw8YrItGpPI+6IyOF0oeZNcLmt1FU41hktSccvryTWgnDRkNBxRdU/FHCQOSBqxkDcYZDWsr7VzHFdwkXgYxzDi9lljSzJXmCJizjC+L1riE6D06xJIP6I5Jy9Eo3jB7V/3AhbErU/ix+v1qSix61fK8HQtxFiNyGfiBAoGASM5kDmGBVZQ9LQm4pRCwVKgL5g4BqVaJOQki5ER/tOk6RULarYyL3v8VvqqNHZt7dBP7p1QTHB2ydogePDK+57lJXjDyyGmTbg0pY1pol8qIvQOYSCF7bpwlCpMrMZsLNeK0S80YyuWvFTblpLK0rTpGYJy8aoYGHwLiUf3fHQUCgYBZ+TyCL0DLIbBx4kB3qymWxeJ0OKvwXO5z5on5n0R147p1s90swMBkW1WGjJw+tllJFYLQ3vVKoM3GOM4KUVXmHN353I14ipjgklRfR6KcJylcWxcEQSeBKS3IhF4Xm2y3RjgN6/cDHDytA2bSgy0KepPTXeg+29euStmOllPYhw==";



    /**
     * 此处SophixEntry应指定真正的Application，
     * 并且保证RealApplicationStub类名不被混淆。
     *
     * @keep 注解已经做好了，无需在混淆文件做处理
     */
    @Keep
    @SophixEntry(MyRealApplication.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //如果需要使用MultiDex，需要在此处调用。
        // MultiDex.install(this);
        initSophix();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //联网下载新的插件
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    /**
     * initialize最好放在attachBaseContext最前面
     */
    private void initSophix() {
        SophixManager.getInstance().setContext(this)
                .setAppVersion(BuildConfig.VERSION_NAME)
                .setAesKey(null)
                .setSecretMetaData(hotfixId,hotfixappKey,rsa)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {

                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Log.e(TAG, "表明补丁加载成功");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                            Log.e(TAG, "表明新补丁生效需要重启. 开发者可提示用户或者强制重启");
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            // SophixManager.getInstance().cleanPatches();
                            Log.e(TAG, "内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载");
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Log.e(TAG, "其它错误信息, 查看PatchStatus类说明");
                        }
                    }
                }).initialize();
    }


    //判断当前应用是否是debug状态
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }


}
