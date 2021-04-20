package com.bishe.me.fragment;


import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bishe.me.R;
import com.bishe.me.util.SPUtils;
import com.bishe.me.activity.OrderActivity;
import com.bishe.me.adapter.OrderAdapter;
import com.bishe.me.bean.Bill;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import java.util.ArrayList;
import java.util.List;
import org.litepal.LitePal;

/**
 * 订单
 */
public class OrderFragment extends Fragment implements RequestLoadMoreListener,
    OnRefreshListener {

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

    Unbinder unbinder;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        initView();
        initDate();
        initAdapter();

        return rootView;
    }

    private void initView() {
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initDate() {
        mList = new ArrayList<>();
    }

    private void initAdapter() {
        mAdapter = new OrderAdapter(R.layout.order_item, mList);
        mAdapter.setType(1);
        mAdapter.setOnLoadMoreListener(this, mRvList);
        mRvList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(
            (adapter, view, position) -> {
                OrderActivity.startToOrderActivity(getActivity(), mList.get(position),1);
            });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Bill bill = mList.get(position);
            switch (view.getId()){
                case R.id.tv_topay:
                    if (bill.getStatus() == 1){
                        Builder builder = new Builder(this.getActivity());
                        builder.setTitle("支付")
                            .setMessage("是否要支付景点【" + bill.getAttractionName() + "】的门票" + bill.getNum() + "张，总计 " + bill.getShould() +"元？")
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
                        Builder builderCancel = new Builder(this.getActivity());
                        builderCancel.setTitle("取消账单")
                            .setMessage("是否要取消支付景点【" + bill.getAttractionName() + "】的门票" + bill.getNum() + "张，总计 " + bill.getShould() +"元？")
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
        });
    }

    void getDate() {
        if (status != 0){
            items = LitePal.select("*").where("status = ?  and authorID = ? ", String.valueOf(status),
                String.valueOf(SPUtils.getUserID(getActivity()))).order("id desc").offset(offset).limit(ROWS)
                .find(Bill.class);
        }else {
            items = LitePal.select("*").where("authorID = ? ",
                String.valueOf(SPUtils.getUserID(getActivity()))).order("id desc").offset(offset).limit(ROWS).find(Bill.class);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
}
