package com.bishe.me.activity;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bishe.me.R;
import com.bishe.me.fragment.AttractionsFragment;
import com.bishe.me.fragment.MeFragment;
import com.bishe.me.fragment.OrderFragment;
import com.bishe.me.fragment.RaidersFragment;
import com.bishe.me.util.ChangeColorIconWithTextView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements OnPageChangeListener, OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    @BindView(R.id.tv_page_title)
    TextView tvPageTitle;
    @BindView(R.id.img_operate)
    ImageView imgOperate;

    private String[] mTitles = new String[]{"景点推荐",
        "攻略", "订单", "我的"};

    private List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getPermissions();
        mViewPager = findViewById(R.id.id_viewpager);

        initData();

        // 沉浸式状态栏设置
        if (isSetStatusBar) {
            steepStatusBar();
        }

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        // 默认选中第一个
        onPageSelected(0);

        imgOperate.setVisibility(View.GONE);
        ivBack.setVisibility(View.GONE);
    }

    private void initData() {

        AttractionsFragment attractionsFragment = new AttractionsFragment();
        RaidersFragment raidersFragment = new RaidersFragment();
        OrderFragment orderFragment = new OrderFragment();
        MeFragment meFragment = new MeFragment();

        Bundle args = new Bundle();
        args.putString("title", "22");
        Bundle args3 = new Bundle();
        args.putString("title", "22");
        Bundle args4 = new Bundle();
        args.putString("title", "22");

        Bundle args5 = new Bundle();
        args.putString("title", "22");
        attractionsFragment.setArguments(args);
//			tabFragment.setArguments(args2);
        attractionsFragment.setArguments(args3);
        attractionsFragment.setArguments(args4);
        attractionsFragment.setArguments(args5);
        mTabs.add(attractionsFragment);
//			mTabs.add(tabFragment2);
        mTabs.add(raidersFragment);
        mTabs.add(orderFragment);
        mTabs.add(meFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };

        initTabIndicator();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initTabIndicator() {
        ChangeColorIconWithTextView one = findViewById(R.id.id_attractions);
        ChangeColorIconWithTextView three = findViewById(R.id.id_raiders);
        ChangeColorIconWithTextView four = findViewById(R.id.id_order);
        ChangeColorIconWithTextView five = findViewById(R.id.id_me);

        mTabIndicator.add(one);
        mTabIndicator.add(three);
        mTabIndicator.add(four);
        mTabIndicator.add(five);

        one.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);

        one.setIconAlpha(1.0f);
    }

    @Override
    public void onPageSelected(int index) {
        if (index == 1) {
            imgOperate.setVisibility(View.VISIBLE);
        } else {
            imgOperate.setVisibility(View.GONE);
        }
        resetOtherTabs(index);

        tvPageTitle.setText(mTitles[index]);
        mTabIndicator.get(index).setIconAlpha(1.0f);
        mViewPager.setCurrentItem(index, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
        int positionOffsetPixels) {

        if (positionOffset > 0) {
            ChangeColorIconWithTextView left = mTabIndicator.get(position);
            ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);

            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

        int index = 0;
        switch (v.getId()) {
            case R.id.id_attractions:
                index = 0;
                break;
            case R.id.id_raiders:
                index = 1;
                break;
            case R.id.id_order:
                index = 2;
                break;
            case R.id.id_me:
                index = 3;
                break;
        }

        resetOtherTabs(index);

        tvPageTitle.setText(mTitles[index]);
        mTabIndicator.get(index).setIconAlpha(1.0f);
        mViewPager.setCurrentItem(index, false);
    }

    /**
     * 重置其他的Tab
     */
    private void resetOtherTabs(int index) {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            if (i != index) {
                mTabIndicator.get(i).setIconAlpha(0);
            }
        }
    }

    private void getPermissions() {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[0]), 22);
            }
        }
    }

    public static void startToMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @OnClick(R.id.img_operate)
    public void onViewClicked() {
        RaiderActivity.startToRaiderActivity(this, null,0);
    }
}
