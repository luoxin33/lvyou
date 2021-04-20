package com.bishe.me.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity{
	
	String new1;
	String new2;
	public Dialog loadDialog;
	private ProgressDialog progressDialog;
	  
    protected Context mContext;

    /** 是否沉浸状态栏 **/
    protected boolean isSetStatusBar = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 处理返回键点击
     *
     * @param view view
     *
     */
    public void goBack(View view) {
        finish();
    }
    
    
    public void showTost(int textResources){
        Toast toast= Toast.makeText(mContext,textResources,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showTost(String text){
        Toast toast= Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    
    public static void into(Context context,String news1,String news2){
        Intent intent =new Intent(context,BaseActivity.class);
        intent.putExtra("news1",news1);
        intent.putExtra("news2",news2);
        context.startActivity(intent);
    }
    
    
    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {

          progressDialog = ProgressDialog.show(this, title,
              message, true, false);
        } else if (progressDialog.isShowing()) {
          progressDialog.setTitle(title);
          progressDialog.setMessage(message);
        }

        progressDialog.show();

      }

      /*
       * 隐藏提示加载
       */
      public void dissProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
          progressDialog.dismiss();
        }

      }

    /**
     * [沉浸状态栏]
     */
    protected void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    

}
