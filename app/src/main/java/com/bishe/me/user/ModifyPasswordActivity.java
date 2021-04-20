package com.bishe.me.user;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bishe.me.activity.BaseActivity;
import com.bishe.me.R;
import com.bishe.me.util.SPUtils;
import com.bishe.me.bean.User;
import com.bishe.me.util.MD5;
import java.util.List;
import org.litepal.LitePal;

public class ModifyPasswordActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_page_title)
    TextView tvPageTitle;
    @BindView(R.id.img_operate)
    ImageView imgOperate;
    @BindView(R.id.et_raider_title)
    EditText etOriginPsd;
    @BindView(R.id.et_psd)
    EditText etPsd;
    @BindView(R.id.et_re_psd)
    EditText etRePsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);

        // 沉浸式状态栏设置
        if (isSetStatusBar) {
            steepStatusBar();
        }

        tvPageTitle.setText("修改密码");
        imgOperate.setVisibility(View.GONE);
    }

    public static void startToModifyPasswordActivity(Context context) {
        Intent intent = new Intent(context, ModifyPasswordActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.btn_modify_password)
    public void onViewClicked() {

        if (TextUtils.isEmpty(etPsd.getText().toString().trim())){
            showTost("请输入密码");
            return;
        }

        if (TextUtils.isEmpty(etRePsd.getText().toString().trim())){
            showTost("请输入确认密码");
            return;
        }

        if (!etPsd.getText().toString().trim().equals(etRePsd.getText().toString().trim())){
            showTost("两次密码不一致");
            return;
        }

        List<User> customerList = LitePal
            .where("userName=?", SPUtils.getUserName(ModifyPasswordActivity.this))
            .find(User.class);

        if (customerList.size() > 0) {

            if (!customerList.get(0).getPassword().equals(MD5.encryptStr(etOriginPsd.getText().toString().trim()))) {
                showTost("旧密码错误");
                return;
            }

            ContentValues values = new ContentValues();
            values.put("password", MD5.encryptStr(etPsd.getText().toString()));
            LitePal.updateAll(User.class, values, "userName = ?", SPUtils.getUserName(ModifyPasswordActivity.this));

            Toast.makeText(ModifyPasswordActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
