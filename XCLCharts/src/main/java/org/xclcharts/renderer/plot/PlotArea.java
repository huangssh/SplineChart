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

package org.xclcharts.renderer.plot;


import org.xclcharts.renderer.XEnum;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Paint.Style;

/**
 * @ClassName PlotArea
 * @Description 主图表区类，用于定制其属性
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * 
 */

public class PlotArea {
	
	//主图表区范围
	protected float mLeft  = 0.0f;
	protected float mTop  = 0.0f;
	protected float mRight   = 0.0f;
	protected float mBottom  = 0.0f;
	
	private float mWidth = 0.0f;
	private float mHeight = 0.0f;
	
	//主图表区背景色,即画X轴与Y轴围成的区域
	private Paint mBackgroundPaint = null;	
	
	//是否画背景色
	private boolean mBackgroundColorVisible = false;
	
	//扩展宽度
	private float mExtWidth = 0.0f;
	
	//是否应用渲染模式
	private boolean mApplayGradient = false;
		
	private int mBeginColor = Color.WHITE;
    private int mEndColor = Color.WHITE;
	private Shader.TileMode mTileMode = Shader.TileMode.MIRROR;
	private XEnum.Direction mDirection = XEnum.Direction.VERTICAL;
		
	
	public PlotArea()
	{

	}
	
	private void initBackgroundPaint()
	{
		if(null == mBackgroundPaint)
		{
			mBackgroundPaint = new Paint();
			mBackgroundPaint.setStyle(Style.FILL);	
			mBackgroundPaint.setColor(Color.WHITE);
		}
	}
	
	
	/**
	 * 开放主图表区背景画笔，即画X轴与Y轴围成的区域的背景画笔。
	 * @return 画笔
	 */
	 public Paint getBackgroundPaint()
	 {
		 initBackgroundPaint();
		 return mBackgroundPaint;
	 }
	 
	 /**
	  * 设置是否显示背景色
	  * @param visible 是否显示背景色
	  */
	public void setBackgroundColorVisible(boolean visible)
	{
		mBackgroundColorVisible = visible;
	}
	
	/**
	 * 返回是否显示背景色
	 * @return 是否显示背景色
	 */
	public boolean getBackgroundColorVisible()
	{
		return mBackgroundColorVisible;
	}
	
	
	/**
	 * 设置是否显示背景色及其背景色的值
	 * @param visible 是否显示背景色
	 * @param color	      背景色
	 */
	public void setBackgroundColor(boolean visible,int color)
	{
		mBackgroundColorVisible = visible;
		getBackgroundPaint().setColor(color);
		
		setBeginColor(color);
		setEndColor(color);		
	}	
	

	/**
	 * 绘图区左边位置X坐标
	 * @return X坐标
	 */
	public float getLeft() {
		return mLeft;
	}
	
	public float getPlotLeft() {
		return mLeft;
	}

	/**
	 * 绘图区上方Y坐标
	 * @return Y坐标
	 */
	public float getTop() {
		return mTop ;
	}
	
	public float getPlotTop() {
		return mTop ;
	}
	
	/**
	 * 绘图区下方Y坐标
	 * @return Y坐标
	 */
	public float getBottom() {
		return mBottom ;
	}
	
	public float getPlotBottom() {
		return mBottom ;
	}
	
	/**
	 * 绘图区右边位置X坐标
	 * @return X坐标
	 */
	public float getRight() {
		return mRight;
	}
	
	public float getPlotRight() {
		return (mRight + mExtWidth);
	}
	
	/**
	 * 绘图区宽度
	 * @return 宽度
	 */
	public float getWidth() {		
		mWidth = Math.abs(mRight - mLeft);		
		return mWidth;
	}
	
	
	public float getPlotWidth() {		
		return Math.abs(mRight + mExtWidth - mLeft);		
	}
	

	/**
	 * 绘图区高度
	 * @return 高度
	 */
	public float getHeight() {		
		mHeight = Math.abs(getBottom() - getTop());
		return mHeight;
	}
	
	public float getPlotHeight() {		
		mHeight = Math.abs(getPlotBottom() - getPlotTop());
		return mHeight;
	}
		
	/*
	public void showRoundRect(Canvas canvas)
	{
		BorderRender border = new BorderRender();
		border.renderBorder(canvas, mLeft  , mTop  , mRight , mBottom  ); 
	}
	*/
	
	/**
	 * 扩展绘图区的实际宽度,用来处理需要很大显示范围的图表
	 * @param width 宽度
	 */
	public void extWidth(float width)
	{
		mExtWidth = width;
	}
	
	public float getExtWidth()
	{
		return mExtWidth;
	}
	
	/**
	 * 设置 是否应用渲染模式
	 */
	public void setApplayGradient(boolean status)
	{
		mApplayGradient = status;
	}
	
	/**
	 * 返回是否应用渲染模式
	 * @return 状态
	 */
	public boolean getApplayGradient()
	{
		return mApplayGradient;
	}
	
	/**
	 * 设置渐变渲染方向	
	 * @param direction 方向
	 */
	public void setGradientDirection(XEnum.Direction direction)
	{
		mDirection = direction;
	}
	
	/**
	 * 返回渐变渲染方向	
	 * @return 方向	
	 */
	public XEnum.Direction getGradientDirection()
	{
		return mDirection;
	}
	
	/**
	 * 设置渲染模式
	 * @param tm	渲染模式
	 */
	 public void setGradientMode(Shader.TileMode tm)
	 {
		 mTileMode = tm;
	 }
	 
	 /**
	  * 返回渲染模式
	  * @return 渲染模式
	  */
	 public Shader.TileMode getGradientMode()
	 {
		 return mTileMode;
	 }
	 
	 /**
	  * 设置起始颜色
	  * @param color	颜色
	  */
	 public void setBeginColor(int color)
	 {
		 mBeginColor = color;
	 }
	 
	 /**
	  * 设置结束颜色
	  * @param color	颜色
	  */
	 public void setEndColor(int color)
	 {
		 mEndColor = color;
	 }
	 
	 
	 /**
	  * 返回起始颜色
	  * @return	颜色
	  */
	 public int getBeginColor()
	 {
		 return mBeginColor;
	 }
	 
	 /**
	  * 返回结束颜色
	  * @return	颜色
	  */
	 public int getEndColor()
	 {
		 return mEndColor;
	 }
	
	
}
