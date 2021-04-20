package com.bishe.me.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.baidu.mapapi.SDKInitializer;
import com.bishe.me.user.LoginActivity;
import com.bishe.me.user.ModifyPasswordActivity;
import com.bishe.me.activity.OrderManagerActivity;
import com.bishe.me.R;
import com.bishe.me.activity.RaiderCheckActivity;
import com.bishe.me.util.SPUtils;
import com.bishe.me.bean.User;
import com.bishe.me.MyApplication;
import com.bishe.me.view.RoundImageView;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.litepal.LitePal;

/**
 * 我的
 */
public class MeFragment extends Fragment {

    @BindView(R.id.iv_person_info)
    RoundImageView ivPersonInfo;
    @BindView(R.id.tv_login_state)
    TextView tvLoginState;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    Unbinder unbinder;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    private Activity activity;   //主activity

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity());

        View rootView = inflater.inflate(R.layout.me_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取数据
        boolean loginSuccessful = SPUtils.getLoginSuccessful(this.activity);
        if (loginSuccessful) {
            // 显示名称
            String userName = SPUtils.getUserName(this.activity);
            tvUserName.setText(userName);
            tvLoginState.setVisibility(View.GONE);
            List<User> users = LitePal.where("userName=?", userName).find(User.class);
            tvTel.setText(String.format(Locale.CHINA,"信用分：%d",users.get(0).getScore()));
        }else {
            tvUserName.setVisibility(View.GONE);
            tvLoginState.setVisibility(View.VISIBLE);
            tvTel.setVisibility(View.GONE);
        }

        tvVersion.setText(String.format("版本%s", MyApplication.getVersionName(this.activity)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_login_state, R.id.rl_order_manager, R.id.rl_modify_password, R.id.rl_about,
        R.id.btn_login_out, R.id.rl_checkupdate,R.id.rl_raider_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_state:
                if (!SPUtils.getLoginSuccessful(this.activity)) {
                    LoginActivity.startToLoginActivity(this.activity);
                    this.activity.finish();
                }
                break;
            case R.id.rl_raider_check:
                RaiderCheckActivity.startToRaiderCheckActivity(getActivity());
                break;
            case R.id.rl_order_manager:
                OrderManagerActivity.startToOrderManagerActivity(getActivity());
                break;
            case R.id.rl_modify_password:
                ModifyPasswordActivity.startToModifyPasswordActivity(getActivity());
                break;
            case R.id.rl_about:
                break;
            case R.id.rl_checkupdate:
                // 检查更新
                updateApk(view);
                break;
            case R.id.btn_login_out:
                LoginActivity.startToLoginActivity(getActivity());
                SPUtils.put(this.activity, SPUtils.LOGINSUCCESSFUL, false);
                this.activity.finish();
                break;
        }
    }

    public void updateApk (View view) {

        Random random = new Random();
        int num = random.nextInt(100);
        if (num%2==0){

            new AlertDialog.Builder(this.activity).setTitle("无新版本").setMessage("您已安装最新版本").setPositiveButton("确定",
                (dialog, which) -> dialog.dismiss()).show();
        }else {
            processVersion();
        }
    }

    protected void processVersion () {

        String content = String.format("\n最新版本：%1$s\n\n更新内容\n%2$s", "2.7.2", "这里是随机数点击更新");

        float density = this.activity.getResources().getDisplayMetrics().density;
        TextView tv = new TextView(this.activity);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setVerticalScrollBarEnabled(true);
        tv.setTextSize(14);
        tv.setMaxHeight((int) (250 * density));
        tv.setPadding((int) (25 * density), (int) (15 * density), (int) (25 * density), 0);

        new AlertDialog.Builder(this.activity).setTitle("应用更新").setMessage(content).setView(tv)
            .setPositiveButton("下载", (dialog, which) -> {
            }).setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
    }

    @OnClick(R.id.rl_raider_check)
    public void onViewClicked() {
    }
}
