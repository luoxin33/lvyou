package com.bishe.me.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import com.bishe.me.R;
import com.bishe.me.bean.Attraction;
import com.bishe.me.util.DrawableUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * @author luoxin
 * @since 2021-04-09 23:53
 */
public class AttractionAdapter extends BaseQuickAdapter<Attraction, BaseViewHolder> {

    private Context context;

    public AttractionAdapter(int layoutResId, @Nullable List<Attraction> data) {
        super(layoutResId, data);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Attraction item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_content, item.getContent());
        if (item.getPrice() == 0){
            helper.setText(R.id.tv_price, "免费");
        }else {
            helper.setText(R.id.tv_price, item.getPrice() + "元/人");
        }

        helper.addOnClickListener(R.id.btn_order);

        helper.setImageResource(R.id.attraction_img, DrawableUtil.getDrawableByName(item.getFirstImg()));
    }
}
