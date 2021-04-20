package com.bishe.me.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bishe.me.R;
import com.bishe.me.adapter.OrderAdapter;
import com.bishe.me.bean.Bill;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import java.util.ArrayList;
import java.util.List;
import org.litepal.LitePal;

/**
 * @author luoxin
 * @since 2021-04-07 21:22
 */

public class OrderManagerActivity extends BaseActivity implements RequestLoadMoreListener,
    OnRefreshListener {

    @BindView(R.id.tv_page_title)
    TextView tvPageTitle;
    @BindView(R.id.img_operate)
    ImageView imgOperate;

    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.tv_status_all)
    TextView tvStatusAll;
    @BindView(R.id.tv_status_topay)
    TextView tvStatusToPay;
    @BindView(R.id.tv_status_paid)
    TextView tvStatusPaid;
    @BindView(R.id.tv_status_complete)
    TextView tvStatusComplete;

    List<Bill> mList;
    List<Bill> items;
    OrderAdapter mAdapter;

    private static final int ROWS = 15;

    private static int REFRESH = 0;
    private static int LOAD_MORE = 1;

    private int offset = 0;

    private int status;

    private int LOAD_TYPE = REFRESH; //  REFRESH : 下拉刷新,LOADMORE :加载更多

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_manager_activity);
        ButterKnife.bind(this);

        tvPageTitle.setText("订单管理");
        imgOperate.setVisibility(View.GONE);

        initView();
        initDate();
        initAdapter();
    }


    private void initView() {
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRvList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initDate() {
        mList = new ArrayList<>();
    }

    private void initAdapter() {
        mAdapter = new OrderAdapter(R.layout.order_item, mList);
        mAdapter.setType(2);
        mAdapter.setOnLoadMoreListener(this, mRvList);
        mRvList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(
            (adapter, view, position) -> {
                OrderActivity.startToOrderActivity(this, mList.get(position), 2);
            });
    }

    void getDate() {
        if (status != 0){
            items = LitePal.select("*").where("status = ?", String.valueOf(status)).order("id desc").offset(offset).limit(ROWS)
                .find(Bill.class);
        }else {
            items = LitePal.select("*").order("id desc").offset(offset).limit(ROWS).find(Bill.class);
        }

        // 判断加载类型
        if (LOAD_TYPE == REFRESH) {
            // 下拉刷新
            mSwipeLayout.setRefreshing(false); // 收起刷新图标
            mAdapter.setEnableLoadMore(true); // 设置可以加载更多
            mList.clear();
        } else if (LOAD_TYPE == LOAD_MORE) {
            // 加载更多时
            mAdapter.loadMoreEnd(false);// 不显示加载更多
            mAdapter.setEnableLoadMore(true); // 设置可以加载更多
            mAdapter.loadMoreComplete();
            mSwipeLayout.setEnabled(true);
        }

        if (items.size() == 0) {
            mAdapter.setEnableLoadMore(false); // 设置可以加载更多
        }

        mList.addAll(items);
        offset = mList.size();
        mAdapter.notifyDataSetChanged();
        mAdapter.disableLoadMoreIfNotFullPage(mRvList);
    }

    @Override
    public void onResume() {
        super.onResume();

        onRefresh();
    }

    @Override
    public void onRefresh() {
        offset = 0;
        LOAD_TYPE = REFRESH;
        mAdapter.setEnableLoadMore(false);
        getDate();
    }

    @Override
    public void onLoadMoreRequested() {
        LOAD_TYPE = LOAD_MORE;
        mSwipeLayout.setEnabled(false);
        getDate();
    }

    @OnClick({R.id.tv_status_all, R.id.tv_status_topay, R.id.tv_status_paid,
        R.id.tv_status_complete})
    public void onViewClicked(View view) {
        tvStatusAll.setTextColor(getResources().getColor(R.color.black));
        tvStatusToPay.setTextColor(getResources().getColor(R.color.black));
        tvStatusPaid.setTextColor(getResources().getColor(R.color.black));
        tvStatusComplete.setTextColor(getResources().getColor(R.color.black));
        tvStatusAll.setTextSize(16);
        tvStatusToPay.setTextSize(16);
        tvStatusPaid.setTextSize(16);
        tvStatusComplete.setTextSize(16);
        switch (view.getId()) {
            case R.id.tv_status_all:
                status = 0;
                tvStatusAll.setTextColor(getResources().getColor(R.color.red));
                tvStatusAll.setTextSize(20);
                break;
            case R.id.tv_status_topay:
                tvStatusToPay.setTextColor(getResources().getColor(R.color.red));
                tvStatusToPay.setTextSize(20);
                status = 1;
                break;
            case R.id.tv_status_paid:
                tvStatusPaid.setTextColor(getResources().getColor(R.color.red));
                tvStatusPaid.setTextSize(20);
                status = 2;
                break;
            case R.id.tv_status_complete:
                tvStatusComplete.setTextColor(getResources().getColor(R.color.red));
                tvStatusComplete.setTextSize(20);
                status = 3;
                break;
        }

        onRefresh();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    public static void startToOrderManagerActivity(Context context) {
        Intent intent = new Intent(context, OrderManagerActivity.class);
        context.startActivity(intent);
    }
}
