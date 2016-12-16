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
 * @version 1.0
 */

package org.xclcharts.renderer.bar;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.renderer.XEnum;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

/**
 * @ClassName FlatBar
 * @Description  平面柱形类，对柱形加了一点渲染效果
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class FlatBar extends Bar{
	
	private static final String TAG = "FlatBar";
	
	//柱形填充色透明度
	private int mFillAlpha = 255;
	
	private LinearGradient linearGradient = null;
	private Path mPath = null;
	
	private final int radius = 5; //角半径
	
	public FlatBar()
	{
	}

	/**
	 * 返回填充透明度
	 * @return 透明度
	 */
	public int getFillAlpha() {
		return mFillAlpha;
	}

	/**
	 * 设置填充透明度
	 * @param alpha 透明度
	 */
	public void setFillAlpha(int alpha) {
		this.mFillAlpha = alpha;
	}
	
	/**
	 * 计算同标签多柱形时的Y分隔
	 * @param YSteps    Y轴步长
	 * @param barNumber  柱形个数
	 * @return 返回单个柱形的高度及间距
	 */
	public float[] getBarHeightAndMargin(float YSteps,int barNumber)
	{
		return calcBarHeightAndMargin( YSteps, barNumber);
	}
	/**
	 * 计算同标签多柱形时的X分隔
	 * @param XSteps	X轴步长
	 * @param barNumber 柱形个数
	 * @return 返回单个柱形的宽度及间距
	 */
	public float[] getBarWidthAndMargin(float XSteps,int barNumber)
	{
		return calcBarWidthAndMargin(XSteps,barNumber);
	}
	
	/**
	 * 绘制柱形渲染效果
	 * @param left	左边X坐标
	 * @param top	顶部Y坐标
	 * @param right	右边X坐标
	 * @param bottom	底部Y坐标
	 */
	private void setBarGradient(float left,float top,float right ,float bottom)
	{
		int barColor = getBarPaint().getColor();						
		int lightColor = DrawHelper.getInstance().getLightColor(barColor,150);
		float width = Math.abs(right - left);
		float height = Math.abs(bottom - top);
			
		Shader.TileMode tm = Shader.TileMode.MIRROR;
		if(width > height) //横向柱形
		{
			 linearGradient = new LinearGradient(right,bottom, right ,top,  
			           new int[]{lightColor,barColor},  
			           null,tm);  		   				 
				 
		}else{
			  linearGradient = new LinearGradient(left,bottom, right ,bottom,  
			           new int[]{lightColor,barColor},  
			           null,tm);  					  			  
		}
		getBarPaint().setShader(linearGradient);		
	}
	
	
	/**
	 * 绘制柱形
	 * @param left	左边X坐标
	 * @param top	顶部Y坐标
	 * @param right	右边X坐标
	 * @param bottom	底部Y坐标
	 * @param canvas	画布
	 */
	public boolean renderBar(float left,float top,float right ,float bottom,Canvas canvas)
	{				
		 XEnum.BarStyle style = getBarStyle();		 
		 if(Float.compare(top, bottom)==0) return true;
		 
		 if( XEnum.BarStyle.ROUNDBAR == style)
		 {
				canvas.drawRoundRect(new RectF(left ,bottom,right,top ),						
						getBarRoundRadius(), getBarRoundRadius(),getBarPaint());	
				return true;
		 }
		 
		if(null == mPath)mPath = new Path();			
		if( XEnum.BarStyle.OUTLINE == style)
		{
			int barColor = getBarPaint().getColor();
			int lightColor = DrawHelper.getInstance().getLightColor(barColor,mOutlineAlpha);		
			getBarOutlinePaint().setColor(lightColor); 
			canvas.drawRect( left ,bottom,right,top  ,getBarOutlinePaint());
			
			getBarPaint().setStyle(Style.STROKE);			
			//getBarPaint().setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));		
			getBarPaint().setStrokeWidth(mBorderWidth);
			drawPathBar(left,top,right,bottom,canvas);			
			getBarPaint().setStrokeWidth(mBorderWidth); //pWidth);
			return true;
		}else if( XEnum.BarStyle.TRIANGLE == style){	
			
			float mid = 0.0f;												
			switch(this.getBarDirection())
			{
			case HORIZONTAL:
				mid = top + (bottom - top)/2;	
				mPath.moveTo(left, top);
				mPath.lineTo(right, mid);
				mPath.lineTo(left, bottom);
				mPath.close();		
				canvas.drawPath(mPath, getBarPaint());
				canvas.drawCircle(right, mid, radius, getBarPaint());
				break;
			default:
				mid = left + (right - left)/2;	
				mPath.moveTo(left, bottom);
				mPath.lineTo(mid, top);
				mPath.lineTo(right, bottom);
				mPath.close();		
				canvas.drawPath(mPath, getBarPaint());
				canvas.drawCircle(mid, top, radius, getBarPaint());
				break;
			}			
			mPath.reset();			
			
			return true;			
		}else{				
			//GRADIENT,FILL,STROKE
			switch(style)
			{
			case GRADIENT:
				setBarGradient(left,top,right,bottom);
				break;
			case FILL:
				getBarPaint().setStyle(Style.FILL);
				break;
			case STROKE:						
				if(Float.compare(1f,  getBarPaint().getStrokeWidth() ) == 0)
											getBarPaint().setStrokeWidth(3);
				getBarPaint().setStyle(Style.STROKE);
				break;
			case TRIANGLE:		
			case OUTLINE:
				break;
			default:
				Log.e(TAG,"不认识的柱形风格参数.");
				return false;
			} 
			
			if(getBarStyle() != XEnum.BarStyle.FILL)
			{
				setBarGradient(left,top,right,bottom);
			}					
			drawPathBar(left,top,right,bottom,canvas);
		}
		return true;
	}
	
	/**
	 * 绘制柱形标签
	 * @param text	文本内容
	 * @param x	x坐标
	 * @param y	y坐标
	 * @param canvas	画布
	 */
	public void renderBarItemLabel(String text,float x,float y,Canvas canvas)
	{		    	 
		drawBarItemLabel(text,x,y,canvas);
	}
	
	private void drawPathBar(float left,float top,float right,float bottom,Canvas canvas)
	{
		mPath.moveTo(left, bottom);
		mPath.lineTo(left, top);
		mPath.lineTo(right,top);
		mPath.lineTo(right, bottom);
		mPath.close();				
		canvas.drawPath(mPath, getBarPaint());			
		mPath.reset();
	}
	
}
