package com.bishe.me.adapter;

import android.support.annotation.Nullable;
import com.bishe.me.R;
import com.bishe.me.bean.Attraction;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * @author luoxin
 * @since 2021-04-19 23:49
 */

public class MyAdapter extends BaseQuickAdapter<Attraction, BaseViewHolder> {

    public MyAdapter(int layoutResId, @Nullable List<Attraction> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Attraction item) {
        helper.setText(R.id.tv_name, item.getTitle());

        helper.addOnClickListener(R.id.tv_name);
    }
}
