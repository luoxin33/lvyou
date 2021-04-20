package com.bishe.me.activity;

import android.os.Bundle;
import android.view.Window;
import butterknife.ButterKnife;
import com.bishe.me.R;
import com.bishe.me.util.SPUtils;
import com.bishe.me.user.LoginActivity;

/**
 * @author luoxin
 * @since 2021-04-15 00:50
 */
public class GuideActivity  extends BaseActivity {

    private boolean isRunning = true;//线程是否运行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guide_activity);
        ButterKnife.bind(this);

        // 沉浸式状态栏设置
        if (isSetStatusBar) {
            steepStatusBar();
        }

        Thread t = new Thread() {

            @Override
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (SPUtils.getLoginSuccessful(GuideActivity.this)) {
                        MainActivity.startToMainActivity(GuideActivity.this);
                    }else {
                        LoginActivity.startToLoginActivity(GuideActivity.this);
                    }

                    finish();
                }
            }
        };
        t.start();
    }

    @Override
    protected void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }
}
