package com.bishe.me.adapter;

import android.support.annotation.Nullable;
import com.bishe.me.R;
import com.bishe.me.bean.Raider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * @author luoxin
 * @since 2021-04-08 23:20
 */

public class RaiderAdapter extends BaseQuickAdapter<Raider, BaseViewHolder> {

    /**
     * 1-攻略界面 2-攻略审核界面
     */
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public RaiderAdapter(int layoutResId, @Nullable List<Raider> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Raider item) {
        helper.setText(R.id.tv_author, item.getAuthor());
        helper.setText(R.id.tv_content, item.getContent());
        helper.setText(R.id.tv_title, item.getName());
        helper.setText(R.id.tv_publish_time_time, item.getTime());

//        0-待审核 1-审核通过 2-审核不通过
        String status;
        if (type == 2) {
            helper.setVisible(R.id.tv_status, true);
            switch (item.getStatus()) {
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
            helper.setText(R.id.tv_status, status);
        }else {
            helper.setVisible(R.id.tv_status, false);
        }
    }
}
