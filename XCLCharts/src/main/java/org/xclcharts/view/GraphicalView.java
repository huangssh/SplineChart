/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.7
 */
package org.xclcharts.view;

import org.xclcharts.common.SysinfoHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * @ClassName GraphicalView
 * @Description  XCL-Charts图表的View基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com) QQ群: 374780627
 */
@SuppressLint("NewApi")
public abstract class GraphicalView extends View {
	
	 private String TAG = "GraphicalView";	


	 public GraphicalView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initChartView();
		 
	 }

	 public GraphicalView(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initChartView();
	 }

	 
	 public GraphicalView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initChartView();
	 }
	 
	 protected void initChartView()
	 {
		//禁用硬件加速
		disableHardwareAccelerated();	
	 }
	 
	 /**
	  * 刷新图表
	  */
	 public void refreshChart(){
		 this.invalidate();
	 }	 
	 
	 /**
	  * 绘制图表
	  * @param canvas	画布
	  */
	 public abstract void render(Canvas canvas);
	 			 
	 @Override
	public void onDraw(Canvas canvas)
	  {		 
		  try {								  			  
				render(canvas);								
		  } catch (Exception e) {
				// TODO Auto-generated catch block
			  Log.e(TAG, e.toString());
		  }	
	   }
	 	 
	 @Override  
     protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
    
     }  
	 
	 @Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			setMeasuredDimension(measureWidth(widthMeasureSpec),
								 measureHeight(heightMeasureSpec));
		}
		
		private int measureWidth(int measureSpec) {
			int result = 100;
			int specMode = MeasureSpec.getMode(measureSpec);
			int specSize = MeasureSpec.getSize(measureSpec);

			if (specMode == MeasureSpec.EXACTLY) { //fill_parent
				result = specSize;
			} else if (specMode == MeasureSpec.AT_MOST) { //wrap_content
				result = Math.min(result, specSize);
			}			
			return result;
		}

		private int measureHeight(int measureSpec) {
			int result = 100;
			int specMode = MeasureSpec.getMode(measureSpec);
			int specSize = MeasureSpec.getSize(measureSpec);

			if (specMode == MeasureSpec.EXACTLY) { //fill_parent
				result = specSize;
			} else if (specMode == MeasureSpec.AT_MOST) { //wrap_content
				result = Math.min(result, specSize);
			}			
			return result;
		}
		
		/**
		 * 禁用硬件加速.
		 * 原因:android自3.0引入了硬件加速，即使用GPU进行绘图,但它并不能完善的支持所有的绘图，
		 * 通常表现为内容(如Rect或Path)不可见，异常或渲染错误。所以类了保证图表的正常显示，强制禁用掉.
		 */		
		protected void disableHardwareAccelerated()
		{			
			//View.isHardwareAccelerated() 
			//Canvas.isHardwareAccelerated()
			
			if(SysinfoHelper.getInstance().supportHardwareAccelerated())
			{		
				//是否开启了硬件加速,如开启将其禁掉
				if(!isHardwareAccelerated())
				{
					//setLayerType(View.LAYER_TYPE_NONE,null);  //LAYER_TYPE_SOFTWARE
					setLayerType(View.LAYER_TYPE_SOFTWARE,null);
				}
			}
		}					 
}
