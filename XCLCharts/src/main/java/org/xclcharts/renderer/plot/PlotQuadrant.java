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
 * @version 2.1
 */
package org.xclcharts.renderer.plot;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * @ClassName PlotQuadrant
 * @Description 四象限基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *
 */
public class PlotQuadrant {
	
	protected int mFirstColor = Color.WHITE;
	protected int mSecondColor= Color.WHITE;
	protected int mThirdColor = Color.WHITE;
	protected int mFourthColor = Color.WHITE;
	
	protected boolean mShow = false;
	protected boolean mShowBgColor = true;
	protected boolean mShowVerticalLine = true;
	protected boolean mShowHorizontalLine = true;
	
	private Paint mPaintBgColor = null;
	private Paint mPaintVerticalLine = null;
	private Paint mPaintHorizontalLine = null;
		
	private double mQuadrantXValue  = 0d;
	private double mQuadrantYValue  = 0d;
	

	public PlotQuadrant()
	{
	}
	
	/**
	 * 显示象限
	 * @param xValue	x轴值
	 * @param yValue	y轴值
	 */
	public void show(double xValue,double yValue)
	{
		setQuadrantXYValue(xValue,yValue);
		mShow = true;
	}
	
	/**
	 * 隐藏象限
	 */
	public void hide()
	{
		mShow = false;
	}
	
	/**
	 * 是否显示象限
	 * @return
	 */
	public boolean isShow()
	{		
		return mShow;
	}
	
	/**
	 * 显示背景色
	 */
	public void showBgColor()
	{
		mShowBgColor = true;
	}
	
	/**
	 * 隐藏背景色
	 */
	public void hideBgColor()
	{
		mShowBgColor = false;
	}
	
	/**
	 * 显示竖线
	 */
	public void showVerticalLine()
	{
		mShowVerticalLine = true;
	}
	
	/**
	 * 隐藏竖线
	 */
	public void hideVerticalLine()
	{
		mShowVerticalLine = false;
	}
	
	/**
	 * 显示横线
	 */
	public void showHorizontalLine()
	{
		mShowHorizontalLine = true;
	}
	
	/**
	 * 隐藏横线
	 */
	public void hideHorizontalLine()
	{
		mShowHorizontalLine = false;
	}
	
	/**
	 * 设置各个象限的颜色
	 * @param first		第一象限
	 * @param second	第二象限
	 * @param third		第三象限
	 * @param fourth	第四象限
	 */
	public void setBgColor(int first,int second,int third,int fourth)
	{
		mFirstColor = first;
		mSecondColor = second;
		mThirdColor = third;
		mFourthColor = fourth;
	}
		
	/**
	 * 开放竖线画笔
	 * @return 画笔
	 */
	public Paint  getVerticalLinePaint()
	{
		if(null == mPaintVerticalLine)
			mPaintVerticalLine = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		return mPaintVerticalLine;
	}
	
	/**
	 * 开放横线画笔
	 * @return 画笔
	 */
	public Paint  getHorizontalLinePaint()
	{
		if(null == mPaintHorizontalLine)
			mPaintHorizontalLine = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		return mPaintHorizontalLine;		
	}
	
	/**
	 * 开放背景色画笔
	 * @return 画笔
	 */
	public Paint  getBgColorPaint()
	{
		if(null == mPaintBgColor)	
			mPaintBgColor = new Paint(Paint.ANTI_ALIAS_FLAG);
					
		return mPaintBgColor;		
	}
	
	/**
	 * 设置象限中心点的值
	 * @param xValue	x方向实际值
	 * @param yValue	y方向实际值
	 */
	public void setQuadrantXYValue(double xValue,double yValue)
	{
		mQuadrantXValue = xValue;
		mQuadrantYValue = yValue;
	}
	
	/**
	 * 返回x方向实际值
	 * @return x值
	 */
	public double getQuadrantXValue()
	{
		return mQuadrantXValue;
	}
	
	/**
	 * 返回y方向实际值
	 * @return y值
	 */
	public double getQuadrantYValue()
	{		
		return mQuadrantYValue;
	}
		
}
