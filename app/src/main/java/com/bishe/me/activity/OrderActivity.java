package com.bishe.me.activity;

import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bishe.me.R;
import com.bishe.me.bean.Bill;
import com.bishe.me.util.DrawableUtil;
import java.util.List;
import org.litepal.LitePal;

/**
 * @author luoxin
 * @since 2021-04-11 22:21
 */

public class OrderActivity extends BaseActivity implements OnClickListener {

    @BindView(R.id.tv_page_title)
    TextView tvPageTitle;
    @BindView(R.id.img_operate)
    ImageView imgOperate;
    @BindView(R.id.tv_topay)
    TextView tvTopay;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.ll_topay)
    LinearLayout llTopay;
    @BindView(R.id.tv_paid)
    TextView tvPaid;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.rl_user_info)
    LinearLayout rlUserInfo;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.order_img)
    ImageView orderImg;
    @BindView(R.id.ll_img)
    LinearLayout llImg;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_order_code)
    TextView tvOrderCode;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;

    Bill bill;
    @BindView(R.id.ll_paid)
    LinearLayout llPaid;
    @BindView(R.id.ll_total)
    LinearLayout llTotal;
    @BindView(R.id.rl_pay_status)
    RelativeLayout rlPayStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;

    /**
     * 1-账单界面 2-账单管理界面
     */
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_activity);
        ButterKnife.bind(this);

        // 沉浸式状态栏设置
        if (isSetStatusBar) {
            steepStatusBar();
        }

        tvPageTitle.setText("订单详情");
        imgOperate.setVisibility(View.GONE);

        initData();
        initView();
    }

    private void initView() {

        /**
         * 1-待支付 2-已支付 3-已完成 4-已取消
         */
        if (type == 2) {
//            1-账单界面 2-账单管理界面
            rlUserInfo.setVisibility(View.VISIBLE);
            tvCreateTime.setText(bill.getCreateTime());
            tvUsername.setText(bill.getAuthorName());
            if (bill.getStatus() == 1) {
                llTopay.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel.setText("待支付");
                tvTopay.setVisibility(View.GONE);
                llPaid.setVisibility(View.GONE);
                llTotal.setVisibility(View.GONE);

                tvStatus.setText("待支付");
            } else if (bill.getStatus() == 2) {
                llTopay.setVisibility(View.GONE);
                llPaid.setVisibility(View.VISIBLE);
                llTotal.setVisibility(View.VISIBLE);

                tvStatus.setText("已支付");
            } else if (bill.getStatus() == 4) {
                llTopay.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel.setText("已取消");
                tvTopay.setVisibility(View.GONE);
                llPaid.setVisibility(View.GONE);
                llTotal.setVisibility(View.GONE);

                tvStatus.setText("已取消");
            }
        } else {
            rlUserInfo.setVisibility(View.GONE);
            if (bill.getStatus() == 1) {
                llTopay.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel.setText("取消支付");
                tvTopay.setVisibility(View.VISIBLE);
                llPaid.setVisibility(View.GONE);
                llTotal.setVisibility(View.GONE);

                tvCancel.setOnClickListener(this);
                tvTopay.setOnClickListener(this);

                tvStatus.setText("待支付");
            } else if (bill.getStatus() == 2) {
                llTopay.setVisibility(View.GONE);
                llPaid.setVisibility(View.VISIBLE);
                llTotal.setVisibility(View.VISIBLE);

                tvStatus.setText("已支付");
            } else if (bill.getStatus() == 4) {
                llTopay.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel.setText("已取消");
                tvTopay.setVisibility(View.GONE);
                llPaid.setVisibility(View.GONE);
                llTotal.setVisibility(View.GONE);

                tvStatus.setText("已取消");
            }
        }

        tvTitle.setText(bill.getAttractionName());
        orderImg.setImageResource(DrawableUtil.getDrawableByName(bill.getImg()));
        tvContent.setText(bill.getAttractionContent());
        tvNum.setText("x" + bill.getNum());
        tvPrice.setText("￥" + bill.getPrice());
        tvTotal.setText(String.valueOf(bill.getShould()));
        tvPaid.setText(String.valueOf(bill.getPaid()));
        tvOrderCode.setText(bill.getOrderCode());
        tvCreateTime.setText(bill.getCreateTime());
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bill = (Bill) bundle.getSerializable("data");
            this.type = bundle.getInt("type");
        }
    }

    public static void startToOrderActivity(Context context, Bill order, int type) {
        Intent intent = new Intent(context, OrderActivity.class);
        Bundle bundle = new Bundle();
        if (order != null) {
            bundle.putSerializable("data", order);
        }
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @OnClick({R.id.tv_topay, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_topay:
                if (bill.getStatus() == 1) {
                    Builder builder = new Builder(this);
                    builder.setTitle("支付")
                        .setMessage(
                            "是否要支付景点【" + bill.getAttractionName() + "】的门票" + bill.getNum() + "张，总计 "
                                + bill.getShould() + "元？")
                        .setPositiveButton("支付",
                            (dialog, which) -> {
                                ContentValues cv = new ContentValues();
                                cv.put("status", 2);
                                LitePal.update(Bill.class, cv, bill.getId());
                                onRefresh();
                            }).setNegativeButton("取消", null)
                        .show();
                }
                break;
            case R.id.tv_cancel:
                if (bill.getStatus() == 1) {
                    Builder builderCancel = new Builder(this);
                    builderCancel.setTitle("取消账单")
                        .setMessage("是否要取消支付景点【" + bill.getAttractionName() + "】的门票" + bill.getNum()
                            + "张，总计 " + bill.getShould() + "元？")
                        .setPositiveButton("确定取消",
                            (dialog, which) -> {
                                ContentValues cv = new ContentValues();
                                cv.put("status", 4);
                                LitePal.update(Bill.class, cv, bill.getId());
                                onRefresh();
                            }).setNegativeButton("不取消", null)
                        .show();
                }
                break;
        }
    }

    private void onRefresh() {
        List<Bill> bills = LitePal.select("*").where("id = ?", String.valueOf(bill.getId()))
            .limit(1).find(Bill.class);
        if (bills != null && bills.size() > 0) {
            bill = bills.get(0);
            initView();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_topay:
                if (bill.getStatus() == 1) {
                    Builder builder = new Builder(this);
                    builder.setTitle("支付")
                        .setMessage(
                            "是否要支付景点【" + bill.getAttractionName() + "】的门票" + bill.getNum() + "张，总计 "
                                + bill.getShould() + "元？")
                        .setPositiveButton("支付",
                            (dialog, which) -> {
                                ContentValues cv = new ContentValues();
                                cv.put("status", 2);
                                LitePal.update(Bill.class, cv, bill.getId());
                                onRefresh();
                            }).setNegativeButton("取消", null)
                        .show();
                }
                break;
            case R.id.tv_cancel:
                if (bill.getStatus() == 1) {
                    Builder builderCancel = new Builder(this);
                    builderCancel.setTitle("取消账单")
                        .setMessage("是否要取消支付景点【" + bill.getAttractionName() + "】的门票" + bill.getNum()
                            + "张，总计 " + bill.getShould() + "元？")
                        .setPositiveButton("确定取消",
                            (dialog, which) -> {
                                ContentValues cv = new ContentValues();
                                cv.put("status", 4);
                                LitePal.update(Bill.class, cv, bill.getId());
                                onRefresh();
                            }).setNegativeButton("不取消", null)
                        .show();
                }
                break;
        }
    }
}
