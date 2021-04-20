package com.bishe.me.adapter;

import android.support.annotation.Nullable;
import com.bishe.me.R;
import com.bishe.me.bean.Bill;
import com.bishe.me.util.DrawableUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * @author luoxin
 * @since 2021-04-11 22:20
 */
public class OrderAdapter extends BaseQuickAdapter<Bill, BaseViewHolder> {

    /**
     * 1-账单界面 2-账单管理界面
     */
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public OrderAdapter(int layoutResId,
        @Nullable List<Bill> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bill item) {

        if (type == 2){
            helper.setVisible(R.id.rl_user_info, true);
            helper.setText(R.id.tv_createtime, item.getCreateTime());
            helper.setText(R.id.tv_username, item.getAuthorName());
            if (item.getStatus() == 1){
                helper.setVisible(R.id.ll_topay, true);
                helper.setVisible(R.id.tv_cancel, true);
                helper.setText(R.id.tv_cancel, "待支付");
                helper.setVisible(R.id.tv_topay, false);
                helper.setVisible(R.id.ll_paid, false);
                helper.setVisible(R.id.ll_total, false);
            }else if (item.getStatus() == 2){
                helper.setVisible(R.id.ll_topay, false);
                helper.setVisible(R.id.ll_paid, true);
                helper.setVisible(R.id.ll_total, true);
            }else if (item.getStatus() == 4){
                helper.setVisible(R.id.ll_topay, true);
                helper.setVisible(R.id.tv_cancel, true);
                helper.setText(R.id.tv_cancel, "已取消");
                helper.setVisible(R.id.tv_topay, false);
                helper.setVisible(R.id.ll_paid, false);
                helper.setVisible(R.id.ll_total, false);
            }
        }else {
            helper.setVisible(R.id.rl_user_info, false);
            if (item.getStatus() == 1){
                helper.setVisible(R.id.ll_topay, true);
                helper.setVisible(R.id.tv_cancel, true);
                helper.setText(R.id.tv_cancel, "取消支付");
                helper.setVisible(R.id.tv_topay, true);
                helper.setVisible(R.id.ll_paid, false);
                helper.setVisible(R.id.ll_total, false);

                helper.addOnClickListener(R.id.tv_cancel);
                helper.addOnClickListener(R.id.tv_topay);
            }else if (item.getStatus() == 2){
                helper.setVisible(R.id.ll_topay, false);
                helper.setVisible(R.id.ll_paid, true);
                helper.setVisible(R.id.ll_total, true);
            }else if (item.getStatus() == 4){
                helper.setVisible(R.id.ll_topay, true);
                helper.setVisible(R.id.tv_cancel, true);
                helper.setText(R.id.tv_cancel, "已取消");
                helper.setVisible(R.id.tv_topay, false);
                helper.setVisible(R.id.ll_paid, false);
                helper.setVisible(R.id.ll_total, false);
            }
        }

        helper.setText(R.id.tv_title, item.getAttractionName());
        helper.setImageResource(R.id.order_img, DrawableUtil.getDrawableByName(item.getImg()));
        helper.setText(R.id.tv_content, item.getAttractionContent());
        helper.setText(R.id.tv_num, "x" + item.getNum());
        helper.setText(R.id.tv_price, "￥" + item.getPrice());
        helper.setText(R.id.tv_total, String.valueOf(item.getShould()));
        helper.setText(R.id.tv_paid, String.valueOf(item.getPaid()));
    }
}
