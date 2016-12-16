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

package org.xclcharts.renderer.axis;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;


/**
 * @ClassName Axis
 * @Description  轴(axis)基类，定义了刻度，标签，等的属性
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */
public class Axis {
	
	//轴线画笔
	private Paint mPaintAxis = null;
	
	//是否显示轴线
	private boolean mAxisLineVisible = true;	
	
	//数据轴刻度线与边上的标注画笔
	private Paint mPaintTickMarks = null;
	private Paint mPaintTickLabels = null;	 
	 
	//数据轴刻度线与边上的标注是否显示
	private boolean mTickMarksVisible = true;
	private boolean mAxisLabelsVisible = true;
	 
	//刻度标记文字旋转角度
	private float mTickLabelRotateAngle = 0.0f; //-45f;
		
	//是否显示轴(包含轴线，刻度线和标签)
	private boolean mAxisVisible = true;	

	public Axis()
	{				
	}
	
	private void initAxisPaint()
	{
		if(null == mPaintAxis)
		{
			mPaintAxis = new Paint();
			mPaintAxis.setColor(Color.BLACK);		
			//mPaintAxis.setStrokeWidth(mAxisLineWidth); //设置轴线条粗细
			mPaintAxis.setAntiAlias(true);	
			mPaintAxis.setStrokeWidth(5);
		}
	}
	
	private void initTickMarksPaint()
	{
		if(null == mPaintTickMarks)
		{
			mPaintTickMarks = new Paint();
			mPaintTickMarks.setColor(Color.BLACK);	
			mPaintTickMarks.setStrokeWidth(3);
			mPaintTickMarks.setAntiAlias(true);		
		}
	}
	
	private void initTickLabelPaint()
	{
		if(null == mPaintTickLabels)
		{
			mPaintTickLabels = new Paint();		
			mPaintTickLabels.setColor(Color.BLACK);	
			mPaintTickLabels.setTextAlign(Align.RIGHT);
			mPaintTickLabels.setTextSize(18);
			mPaintTickLabels.setAntiAlias(true);
		}
	}
	
	/**
	 * 显示轴(包含轴线，刻度线和标签)
	 */
	public void show()
	{
		mAxisVisible = true;
	}
	
	/**
	 * 隐藏轴(包含轴线，刻度线和标签)
	 */
	public void hide()
	{
		mAxisVisible = false;
	}
	
	/**
	 * 是否显示轴线
	 * @return	是否显示
	 */
	public boolean isShow()
	{
		return mAxisVisible;
	}

	/**
	 * 显示轴线
	 */
	public void showAxisLine()
	{
		mAxisLineVisible = true;
	}
	
	/**
	 * 隐藏轴线
	 */
	public void hideAxisLine()
	{
		mAxisLineVisible = false;
	}
	
	/**
	 * 返回是否显示轴线
	 * @return 是否显示
	 */
	public boolean isShowAxisLine() 
	{
		return mAxisLineVisible;
	}
	
	/**
	 * 开放轴线画笔
	 * @return 画笔
	 */
	public Paint getAxisPaint() {
		initAxisPaint();
		return mPaintAxis;
	}
	
	/**
	 * 开放轴刻度线画笔
	 * @return 画笔
	 */
	public Paint getTickMarksPaint() {
		initTickMarksPaint();
		return mPaintTickMarks;
	}

	/**
	 * 开放轴标签画笔
	 * @return	画笔
	 */
	public Paint getTickLabelPaint() {
		initTickLabelPaint();
		return mPaintTickLabels;
	}

	
	/**
	 * 显示轴刻度线
	 */
	public void showTickMarks() {
		this.mTickMarksVisible = true;
	}
	
	/**
	 * 隐藏轴刻度线
	 */
	public void hideTickMarks() {
		this.mTickMarksVisible = false;
	}
	
	/**
	 * 返回是否显示轴刻度线
	 * @return 是否显示
	 */
	public boolean  isShowTickMarks() 
	{
		return mTickMarksVisible;
	}
	
	/**
	 * 显示轴标签
	 */
	public void showAxisLabels()
	{
		mAxisLabelsVisible = true;
	}
	
	/**
	 * 隐藏轴标签
	 */
	public void hideAxisLabels()
	{
		mAxisLabelsVisible = false;		
	}

	/**
	 * 返回是否显示轴标签
	 * @return 是否显示
	 */
	public boolean isShowAxisLabels(){ 
		return this.mAxisLabelsVisible ;
	}
	
	/**
	 * 返回轴标签文字旋转角度
	 * @return 旋转角度
	 */
	public float getTickLabelRotateAngle() {
		return mTickLabelRotateAngle;
	}

	/**
	 * 设置轴标签文字旋转角度
	 * @param rotateAngle 旋转角度
	 */
	public void setTickLabelRotateAngle(float rotateAngle) {
		this.mTickLabelRotateAngle = rotateAngle;
	}

	
}
