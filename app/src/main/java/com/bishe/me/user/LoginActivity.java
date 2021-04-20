package com.bishe.me.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bishe.me.activity.BaseActivity;
import com.bishe.me.activity.MainActivity;
import com.bishe.me.R;
import com.bishe.me.util.SPUtils;
import com.bishe.me.bean.User;
import com.bishe.me.util.MD5;
import java.util.List;
import org.litepal.LitePal;

public class LoginActivity extends BaseActivity {

    public static String user;

    @BindView(R.id.et_username)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassWord;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    Button register;

    @SuppressWarnings("rawtypes")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.longin_activity);
        ButterKnife.bind(this);
    }

    public void login() {

        if (!TextUtils.isEmpty(etUserName.getText().toString().trim()) && !TextUtils.isEmpty(etPassWord.getText().toString().trim())) {

            List<User> customerList = LitePal.where("userName=?", etUserName.getText().toString()).find(User.class);

            if (customerList.size() > 0) {
                User user = customerList.get(0);
                if (etUserName.getText().toString().equals(user.getUserName()) && MD5.encryptStr(etPassWord.getText().toString()).equals(user.getPassword())) {
                    MainActivity.startToMainActivity(this);
                    finish();

                    SPUtils.put(this, SPUtils.USERNAME, etUserName.getText().toString());
                    SPUtils.put(this, SPUtils.USERID, user.getId());
                    SPUtils.put(this, SPUtils.LOGINSUCCESSFUL, true);
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "该用户未注册", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "请输入用户名或密码", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.login, R.id.register,R.id.iv_logo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.register:
                register();
                break;
            case R.id.iv_logo:
                break;
        }
    }

    public static void startToLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void register() {
        RegisterActivity.startToRegisterActivity(this);
    }
}
