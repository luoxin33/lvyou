package views;

import com.bishe.me.R;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by ex-liyongqiang001 on 16/7/1.
 */
public class TitleView extends FrameLayout implements View.OnClickListener{

    private TextView tvTitle,tvRight,tvLeft;
    private LinearLayout layoutLeft,layoutRight;
    private ImageView imgRight,imgLeft;
    private OnClickListener onLeftClickListener;

    private Context mContext;

    public TitleView(Context context) {
        super(context);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext=context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_title,this);
        tvTitle=(TextView)findViewById(R.id.tv_title);
        layoutLeft=(LinearLayout)findViewById(R.id.layout_back);
        layoutRight=(LinearLayout)findViewById(R.id.layout_right);
        tvRight=(TextView)layoutRight.findViewById(R.id.tv_right);
        imgRight=(ImageView)layoutRight.findViewById(R.id.img_right);
        imgLeft=(ImageView)layoutLeft.findViewById(R.id.img_back);
        tvLeft=(TextView)layoutLeft.findViewById(R.id.tv_left);
        layoutLeft.setOnClickListener(this);
    }

    public void setTitle(String text){
        tvTitle.setText(text);
    }

    public String getTitle(){
        return TextUtils.isEmpty(tvTitle.getText())?"":tvTitle.getText().toString();
    }

    public void setTitle(int textResource){
        tvTitle.setText(textResource);
    }

    public void setRightText(String text,OnClickListener onClickListener){
        if(onClickListener!=null){
            layoutRight.setOnClickListener(onClickListener);
        }
        tvRight.setText(text);
        tvRight.setVisibility(VISIBLE);
        imgRight.setVisibility(GONE);
    }

    public void resetRight(){
        layoutRight.setOnClickListener(null);
        tvRight.setText("");
        tvRight.setVisibility(GONE);
        imgRight.setVisibility(GONE);
    }

    public void setLeftText(int textId,OnClickListener onClickListener){
        if(onClickListener!=null){
            layoutLeft.setOnClickListener(onClickListener);
        }
        tvLeft.setText(textId);
        tvLeft.setVisibility(VISIBLE);
        imgLeft.setVisibility(GONE);
    }

    public void resetLeft(){
        tvLeft.setVisibility(GONE);
        imgLeft.setVisibility(VISIBLE);
        layoutLeft.setOnClickListener(this);
    }

    public void removerRight(){
        layoutRight.setVisibility(GONE);
        layoutRight.removeAllViews();
    }

    public void hideLeftLayout(){
        layoutLeft.setVisibility(GONE);
    }

    public void setRightText(int text,OnClickListener onClickListener){
        setRightText(mContext.getText(text).toString(), onClickListener);
    }

    public void setRightImg(int imgResources,OnClickListener onClickListener){
        if(onClickListener!=null){
            layoutRight.setOnClickListener(onClickListener);
        }
        imgRight.setImageResource(imgResources);
        imgRight.setVisibility(VISIBLE);
        tvRight.setVisibility(GONE);
    }

    public void setRightView(View view){
        layoutRight.removeAllViews();
        layoutRight.setPadding(0,0,0,0);
        layoutRight.addView(view);
    }

    public void setRightImg(int imgResources,OnClickListener onClickListener,int width,int height){
        if(onClickListener!=null){
            layoutRight.setOnClickListener(onClickListener);
        }
        imgRight.setImageResource(imgResources);
        LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams) imgRight.getLayoutParams();
        lp.width=width;
        lp.height=height;
        imgRight.setVisibility(VISIBLE);
        tvRight.setVisibility(GONE);
    }

    public void setLeftClickListener(OnClickListener onClickListener){
        onLeftClickListener=onClickListener;
    }

    public void setDarkColor(View view){
        layoutRight.removeAllViews();
        layoutRight.setPadding(0,0,0,0);
        layoutRight.addView(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_back:
                if(onLeftClickListener==null){
                    if(mContext instanceof Activity){
                        ((Activity)mContext).onBackPressed();
                    }
                }else{
                    onLeftClickListener.onClick(view);
                }
                break;
        }
    }
}

