package com.bishe.me.fragment;


import android.app.AlertDialog.Builder;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bishe.me.activity.RaiderActivity;
import com.bishe.me.R;
import com.bishe.me.adapter.RaiderAdapter;
import com.bishe.me.bean.Raider;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import java.util.ArrayList;
import java.util.List;
import org.litepal.LitePal;

/**
 * 攻略
 */
public class RaidersFragment extends Fragment implements RequestLoadMoreListener,
    OnRefreshListener {

    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    Unbinder unbinder;

    List<Raider> mList;
    List<Raider> items;
    RaiderAdapter mAdapter;

    private static final int ROWS = 15;

    private static int REFRESH = 0;
    private static int LOAD_MORE = 1;

    private int offset = 0;

    private int LOAD_TYPE = REFRESH; //  REFRESH : 下拉刷新,LOADMORE :加载更多

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tab3, container, false);
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
        mAdapter = new RaiderAdapter(R.layout.note_item, mList);
        mAdapter.setType(1);
        mAdapter.setOnLoadMoreListener(this, mRvList);
        mRvList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(
            (adapter, view, position) -> {
                RaiderActivity.startToRaiderActivity(getActivity(), mList.get(position),1);
            });

       mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
           // 弹窗显示是否删除
           Builder builder = new Builder(getActivity());
           builder.setTitle("删除")
               .setMessage("是否要删除攻略【" + mList.get(position).getName() + "】？")
               .setPositiveButton("提交",
                   (dialog, which) -> {
                       Raider notepad=  mList.get(position);
                       notepad.delete();
                       mList.remove(position);
                       mAdapter.notifyDataSetChanged();
                   }).setNegativeButton("取消", null)
               .show();
           return true;
       });
    }

    void getDate() {
        items = LitePal.select("*").where("status = 1").order("id desc").offset(offset).limit(ROWS).find(Raider.class);

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
}
