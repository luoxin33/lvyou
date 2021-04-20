package com.bishe.me.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bishe.me.R;
import com.bishe.me.util.SPUtils;
import com.bishe.me.bean.Attraction;
import com.bishe.me.bean.Bill;
import com.bishe.me.bean.User;
import com.bishe.me.util.DrawableUtil;
import com.bishe.me.util.GlideImageLoader;
import com.bishe.me.util.StringUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.litepal.LitePal;

/**
 * @author luoxin
 * @since 2021-04-12 22:06
 */

public class AttractionActivity extends BaseActivity {

    @BindView(R.id.tv_page_title)
    TextView tvPageTitle;
    @BindView(R.id.img_operate)
    ImageView imgOperate;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_attraction_content)
    TextView tvAttractionContent;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.et_send_num)
    EditText etSendNum;

    Attraction attraction;

    int num = 1;

    List<Integer> imgs = new ArrayList<>();


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.attraction_activity);
        ButterKnife.bind(this);

        // 沉浸式状态栏设置
        if (isSetStatusBar) {
            steepStatusBar();
        }

        tvPageTitle.setText("添加攻略");
        imgOperate.setVisibility(View.GONE);

        etSendNum.setEnabled(false);

        initData();
        initView();
    }

    private void initView() {
        if (attraction != null) {
            tvPageTitle.setText(attraction.getTitle());

            tvTitle.setText(attraction.getTitle());
            tvAttractionContent.setText(attraction.getContent());
            tvPrice.setText(attraction.getPrice() == 0 ? "0元/人" : (attraction.getPrice() + "元/人"));
            tvTotal.setText(attraction.getPrice() == 0 ? "0元" : (attraction.getPrice() * num + "元"));

            String[] imgList = attraction.getImgRes().split(",");
            for (String s : imgList) {
                int drawableByName = DrawableUtil.getDrawableByName(s);
                imgs.add(drawableByName);
            }

            etSendNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // xml 里面设置了最大长度是9位
                    if (count >= before && start == 5) {
                        showTost("最大购买数量不超过9 张，如还需继续购买，请分多次进行");
                    }

                    etSendNum.setSelection(s.length());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imgs);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            attraction = (Attraction) bundle.getSerializable("data");
        }
    }

    public static void startToAttractionActivity(Context context, Attraction attraction) {
        Intent intent = new Intent(context, AttractionActivity.class);
        Bundle bundle = new Bundle();
        if (attraction != null) {
            bundle.putSerializable("data", attraction);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @OnClick({R.id.btn_add_attraction,R.id.img_num_reduce, R.id.img_num_add})
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.img_num_add:
                updateCount(1);
                break;
            case R.id.img_num_reduce:
                updateCount(-1);
                break;
            case R.id.btn_add_attraction:
                Builder builder = new Builder(this);
                builder.setTitle("购买景点门票")
                    .setMessage("是否要购买景点【" + attraction.getTitle() + "】的门票" + num + "张，总计 " + tvTotal.getText().toString() +"？")
                    .setPositiveButton("支付",
                        (dialog, which) -> {
                            Bill order = new Bill();
                            List<User> userList = LitePal.where("userName = ?", SPUtils.getUserName(AttractionActivity.this)).limit(1)
                                .find(User.class);
                            order.setAuthorID(userList.get(0).getId());
                            order.setAuthorName(userList.get(0).getUserName());
                            order.setOrderCode(StringUtil.getOrderCode());
                            order.setAttractionName(attraction.getTitle());
                            order.setImg(attraction.getFirstImg());
                            order.setAttractionID(attraction.getId());
                            order.setAttractionContent(attraction.getContent());
                            order.setPrice(attraction.getPrice());
                            order.setStatus(2);
                            order.setNum(num);
                            order.setShould(attraction.getPrice() * num);
                            order.setPaid(attraction.getPrice() * num);
                            order.setCreateTime(simpleDateFormat.format(new Date()));
                            order.save();
                        }).setNegativeButton("取消", null).setNeutralButton("稍后支付",
                    (dialog, which) -> {
                        Bill order = new Bill();
                        List<User> userList = LitePal.where("userName = ?", SPUtils.getUserName(AttractionActivity.this)).limit(1)
                            .find(User.class);
                        order.setAuthorID(userList.get(0).getId());
                        order.setOrderCode(StringUtil.getOrderCode());
                        order.setAuthorName(userList.get(0).getUserName());
                        order.setAttractionName(attraction.getTitle());
                        order.setImg(attraction.getFirstImg());
                        order.setAttractionID(attraction.getId());
                        order.setStatus(1);
                        order.setNum(num);
                        order.setShould(attraction.getPrice() * num);
                        order.setCreateTime(simpleDateFormat.format(new Date()));
                        order.setPaid(0);
                        order.save();
                    })
                    .show();
                break;
        }
    }

    private void updateCount(int step) {
        num = Integer.parseInt(etSendNum.getText().toString().trim());
        num += step;
        if (num < 1) {
            showTost("购买数量不能小于1");
            return;
        }if (num > 9){
            showTost("最大购买数量不超过9张，如还需继续购买，请分多次进行");
            return;
        }
        etSendNum.setText(String.format("%d", num));

        tvTotal.setText(attraction.getPrice() == 0 ? "0元" : (attraction.getPrice() * num + "元"));
    }
}
