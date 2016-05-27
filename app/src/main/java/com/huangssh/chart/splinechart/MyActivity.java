package com.huangssh.chart.splinechart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MyActivity extends Activity {
    protected String TAG = "MyActivity";
    protected MyActivity mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String ACTIVITY_NAME = this.getClass().getSimpleName() + ":";

        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    /**
     * UI绑定
     * 
     * @作者 huangssh
     * @创建时间 2015-8-6 上午11:20:48  
     * @param id
     * @return
     */
    public <T extends View> T findView(int id) {
        T view = null;
        View genericView = findViewById(id);
        try {
            view = (T) (genericView);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return view;
    }
    
    /**
     * UI绑定(通过父级view)
     * 
     * @作者 huangssh
     * @创建时间 2015-8-6 上午11:23:21  
     * @param parentView 父级view
     * @param id
     * @return
     */
    public <T extends View> T findView(View parentView, int id) {
        T view = null;
        View genericView = parentView.findViewById(id);
        try {
            view = (T) (genericView);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return view;
    }
    /**
     * 通过Class跳转界面
     * 
     * @作者 huangssh
     * @创建时间 2015-8-6 上午9:19:29  
     * @param cls 跳转到的class
     */
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/**
	 * 含有Bundle通过Class跳转界面
	 * 
	 * @作者 huangssh
	 * @创建时间 2015-8-6 上午9:21:52  
	 * @param cls 跳转到的class
	 * @param bundle
	 */
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
	
	/**
	 * 通过action跳转界面
	 * 
	 * @作者 huangssh
	 * @创建时间 2015-8-6 上午9:38:09  
	 * @param action
	 */
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/**
	 * 含有bundle通过action跳转界面
	 * 
	 * @作者 huangssh
	 * @创建时间 2015-8-6 上午9:38:36  
	 * @param action
	 * @param bundle
	 */
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
	
	/**
	 * 带返回值和结果码的 关闭Activity
	 * 
	 * @作者 huangssh
	 * @创建时间 2015-8-6 上午11:37:30  
	 * @param data
	 * @param resultCode
	 */
	protected void finishActivity(Intent data, int resultCode) {
		setResult(resultCode, data);
		this.finish();
	}

}
