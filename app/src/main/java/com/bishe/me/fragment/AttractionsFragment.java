package com.bishe.me.fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bishe.me.R;
import com.bishe.me.activity.AttractionActivity;
import com.bishe.me.adapter.AttractionAdapter;
import com.bishe.me.adapter.MyAdapter;
import com.bishe.me.bean.Attraction;
import com.bishe.me.util.DrawableUtil;
import com.bishe.me.util.GlideImageLoader;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.litepal.LitePal;

/**
 * 景点推荐
 */
public class AttractionsFragment extends Fragment implements RequestLoadMoreListener,
    OnRefreshListener {

    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.banner)
    Banner banner;

    Unbinder unbinder;

    List<Attraction> mList;
    List<Attraction> items;

    List<Attraction> mNameList = new ArrayList<>();
    List<Attraction> nameItems;

    AttractionAdapter mAdapter;
    MyAdapter myAdapter;

    private static final int ROWS = 15;

    private static int REFRESH = 0;
    private static int LOAD_MORE = 1;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.ll_title)
    FrameLayout llTitle;
    @BindView(R.id.ll_all)
    LinearLayout llAll;

    private int offset = 0;

    private int LOAD_TYPE = REFRESH; //  REFRESH : 下拉刷新,LOADMORE :加载更多

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private List<Integer> imgs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.attraction_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        getActivity().getWindow()
            .setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initView();
        initDate();
        initAdapter();

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

        return rootView;
    }

    private void initAdapter() {
        mAdapter = new AttractionAdapter(R.layout.item_attraction, mList);
        mAdapter.setContext(getActivity());
        mAdapter.setOnLoadMoreListener(this, mRvList);
        mRvList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(
            (adapter, view, position) -> AttractionActivity
                .startToAttractionActivity(getActivity(), mList.get(position)));

        mAdapter.setOnItemChildClickListener(
            (adapter, view, position) -> AttractionActivity
                .startToAttractionActivity(getActivity(), mList.get(position)));

        myAdapter = new MyAdapter(R.layout.item_string, mNameList);
        list.setAdapter(myAdapter);
        myAdapter.setOnItemChildClickListener((adapter, view, position) -> AttractionActivity
            .startToAttractionActivity(getActivity(), mNameList.get(position)));
    }

    private void initDate() {
        mList = new ArrayList<>();

        String[] imgList = new String[]{"R.drawable.res1", "R.drawable.res6", "R.drawable.res12",
            "R.drawable.res16", "R.drawable.res21"};
        for (String s : imgList) {
            int drawableByName = DrawableUtil.getDrawableByName(s);
            imgs.add(drawableByName);
        }
    }

    private void initView() {
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));

        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                nameItems = LitePal.select("*").where("title like ?", "%" + query + "%")
                    .find(Attraction.class);

                mNameList.clear();
                if (nameItems.size() > 0){
                    mNameList.addAll(nameItems);
                }

                if (nameItems.size() > 0) {
                    llTitle.setVisibility(View.VISIBLE);
                    llAll.setVisibility(View.GONE);
                }else {
                    llTitle.setVisibility(View.GONE);
                    llAll.setVisibility(View.VISIBLE);
                }
                myAdapter.notifyDataSetChanged();

                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    llTitle.setVisibility(View.GONE);
                    llAll.setVisibility(View.VISIBLE);

                    mNameList.clear();
                    myAdapter.notifyDataSetChanged();
                    searchView.clearFocus();
                }
                return false;
            }
        });

        //下拉列表选择监听
        searchView.setOnSuggestionListener(new OnSuggestionListener() {
            //当列表被选择兵添加到搜索框中调用
            @Override
            public boolean onSuggestionSelect(int position) {
                Toast.makeText(getActivity(), "选择的列表是位置" + position, Toast.LENGTH_LONG).show();
                return true;
            }

            //点击一个选项时调用
            @Override
            public boolean onSuggestionClick(int position) {
                Toast.makeText(getActivity(), "点击列表是位置" + position, Toast.LENGTH_LONG).show();
                searchView.setQuery("123", true);
                return true;
            }
        });
    }

    void getDate() {
        items = LitePal.select("*").offset(offset).limit(ROWS).find(Attraction.class);

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

        llTitle.setVisibility(View.GONE);
        llAll.setVisibility(View.VISIBLE);

        searchView.setQuery("",false);

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
