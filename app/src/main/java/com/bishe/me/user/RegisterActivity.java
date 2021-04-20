package com.bishe.me.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bishe.me.activity.BaseActivity;
import com.bishe.me.R;
import com.bishe.me.bean.User;
import com.bishe.me.util.MD5;
import java.util.List;
import java.util.Random;
import org.litepal.LitePal;

/**
 * @author luoxin
 * @since 2021-04-06 23:27
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password2)
    EditText etPassword2;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_register)
    Button etRegister;
    @BindView(R.id.tv_page_title)
    TextView tvPageTitle;
    @BindView(R.id.img_operate)
    ImageView imgOperate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);

        tvPageTitle.setText("注册");
        imgOperate.setVisibility(View.GONE);
    }

    @OnClick(R.id.et_register)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(etPassword2.getText().toString().trim())) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            Toast.makeText(this, "电子邮件不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (!etPassword.getText().toString().trim()
            .equals(etPassword2.getText().toString().trim())) {
            Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_LONG).show();
            return;
        }

        List<User> customerList = LitePal.where("userName=?", etUsername.getText().toString())
            .find(User.class);

        if (customerList.size() > 0) {
            Toast.makeText(this, "该用户名已经注册", Toast.LENGTH_LONG).show();
            return;
        }

        User user = new User();
        user.setPassword(MD5.encryptStr(etPassword.getText().toString()));
        user.setUserName(etUsername.getText().toString());
        int score = 0;
        while (score < 700){
            score = new Random().nextInt(900);
        }
        user.setScore(score);
        user.save();

        if (user.save()) {
            finish();
            Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "注册失败", Toast.LENGTH_LONG).show();
        }
    }

    public static void startToRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @OnClick({R.id.iv_back, R.id.img_operate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.img_operate:
                break;
        }
    }
}
