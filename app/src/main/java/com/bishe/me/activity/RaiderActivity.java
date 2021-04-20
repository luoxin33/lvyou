package com.bishe.me.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bishe.me.R;
import com.bishe.me.bean.Raider;
import com.bishe.me.util.SPUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.litepal.LitePal;

/**
 * @author luoxin
 * @since 2021-04-08 22:37
 */

public class RaiderActivity extends BaseActivity {

    @BindView(R.id.tv_page_title)
    TextView tvPageTitle;
    @BindView(R.id.img_operate)
    ImageView imgOperate;
    @BindView(R.id.et_raider_title)
    EditText etRaiderTitle;
    @BindView(R.id.et_raider_content)
    EditText etRaiderContent;
    @BindView(R.id.btn_add_raider)
    Button btnAddRaider;
    @BindView(R.id.ll_operator_check)
    LinearLayout llOperatorCheck;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    Raider raider;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.ll_status)
    LinearLayout llStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.raider_add_activity);
        ButterKnife.bind(this);

        // 沉浸式状态栏设置
        if (isSetStatusBar) {
            steepStatusBar();
        }

        tvPageTitle.setText("添加攻略");
        imgOperate.setVisibility(View.GONE);

        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            raider = (Raider) bundle.getSerializable("data");
            int type = bundle.getInt("type");
            if (type == 0) {
                // 新增
                llOperatorCheck.setVisibility(View.GONE);
            } else if (type == 1) {
                // 详情
                tvPageTitle.setText("攻略详情");
                btnAddRaider.setVisibility(View.GONE);
                llOperatorCheck.setVisibility(View.GONE);
                etRaiderTitle.setEnabled(false);
                etRaiderContent.setEnabled(false);
            } else {
                // 审核
                tvPageTitle.setText("攻略审核");
                llStatus.setVisibility(View.VISIBLE);

                if (raider != null && raider.getStatus() == 0) {
                    // 待审核的时候才显示审核操作
                    llOperatorCheck.setVisibility(View.VISIBLE);
                    tvStatus.setText("待审核");
                } else {
                    llOperatorCheck.setVisibility(View.GONE);
                }

                btnAddRaider.setVisibility(View.GONE);

                etRaiderTitle.setEnabled(false);
                etRaiderContent.setEnabled(false);
            }

            if (raider != null) {
                etRaiderTitle.setText(raider.getName());
                etRaiderContent.setText(raider.getContent());

                String status;
                switch (raider.getStatus()) {
                    case 1:
                        status = "审核通过";
                        break;
                    case 2:
                        status = "审核不通过";
                        break;
                    default:
                        status = "待审核";
                        break;
                }

                tvStatus.setText(status);

                this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        }
    }

    /**
     * @param context
     * @param raider
     * @param type 0-新增 1-详情 2-审核
     */
    public static void startToRaiderActivity(Context context, Raider raider, int type) {
        Intent intent = new Intent(context, RaiderActivity.class);
        Bundle bundle = new Bundle();
        if (raider != null) {
            bundle.putSerializable("data", raider);
        }
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @OnClick(R.id.btn_add_raider)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etRaiderTitle.getText().toString().trim())) {
            showTost("请输入攻略标题");
            return;
        }

        if (TextUtils.isEmpty(etRaiderContent.getText().toString().trim())) {
            showTost("请输入攻略内容");
            return;
        }

        Raider notepad = new Raider();
        notepad.setName(etRaiderTitle.getText().toString());
        notepad.setContent(etRaiderContent.getText().toString());
        notepad.setTime(simpleDateFormat.format(new Date()));
        notepad.setAuthor(SPUtils.getUserName(this));

        if (notepad.save()) {
            finish();
            Toast.makeText(RaiderActivity.this, "保存成功", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(RaiderActivity.this, "保存失败", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.btn_ok, R.id.btn_refuse})
    public void onViewClicked(View view) {
        int status = 0;
        String statusStr = null;
        switch (view.getId()) {
            case R.id.btn_ok:
                status = 1;
                statusStr = "通过";
                break;
            case R.id.btn_refuse:
                status = 2;
                statusStr = "不通过";
                break;
        }

        raider.setStatus(status);

        Builder builder = new Builder(this);
        int finalStatus = status;
        builder.setTitle("攻略审核")
            .setMessage("是否确定要将攻略【" + raider.getName() + "】审核为" + statusStr)
            .setPositiveButton("确定",
                (dialog, which) -> {
                    ContentValues cv = new ContentValues();
                    cv.put("status", finalStatus);
                    LitePal.update(Raider.class, cv, raider.getId());
                    // 不显示
                    llOperatorCheck.setVisibility(View.GONE);

                    String raiderStatus;
                    switch (raider.getStatus()) {
                        case 1:
                            raiderStatus = "审核通过";
                            break;
                        case 2:
                            raiderStatus = "审核不通过";
                            break;
                        default:
                            raiderStatus = "待审核";
                            break;
                    }

                    tvStatus.setText(raiderStatus);

                    Toast.makeText(this, "操作成功", Toast.LENGTH_LONG).show();
                }).setNegativeButton("取消", null)
            .show();
    }
}
