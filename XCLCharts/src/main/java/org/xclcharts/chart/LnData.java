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
package org.xclcharts.chart;

import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.line.PlotLine;
import org.xclcharts.renderer.plot.PlotLabel;
import org.xclcharts.renderer.plot.PlotLabelRender;

import android.graphics.Paint;


/**
 * @ClassName LnData
 * @Description 线图(曲线图  折线图 面积图)数据基类 
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class LnData {
	//标签轴用的到值
	private String mLabel;
	//是否在点上显示标签
	private boolean mLabelVisible = false;

	//线的基类
	private PlotLine mPlotLine = null;
	
	//线的类型
	private XEnum.LineStyle mLineStyle = XEnum.LineStyle.SOLID;
	
	//用于设置标签特性
	private PlotLabelRender mPlotLabel = null;
	
	public LnData()
	{
		mPlotLine = new PlotLine(); 
	}
	
	/**
	 * 设置是否在线上显示标签
	 * @param visible 是否显示
	 */
	public void setLabelVisible(boolean visible) 
	{
		mLabelVisible = visible;
		
		getLabelOptions().setOffsetY(15.f);
		//getLabelOptions().showBox();
	}
	
	/**
	 * 返回是否在线上显示标签
	 * @return 是否显示
	 */
	public boolean getLabelVisible()
	{
		return mLabelVisible;
	}
	
	/**
	 * 设置标签
	 * @param value 标签内容
	 */
	public void setLabel(String value) 
	{
		mLabel = value;
	}
	
	/**
	 * 返回标签
	 * @return 标签
	 */
	public String getLabel() {
		return mLabel;
	}
	
	/**
	 * 返回线的基类
	 * @return 线的基类
	 */
	public PlotLine getPlotLine()
	{
		return mPlotLine;
	}			
				
	/**
	 * 设置线的颜色	
	 * @param color 线的颜色
	 */
	public void setLineColor(int color) 
	{				
		mPlotLine.getLinePaint().setColor(color );
		mPlotLine.getDotLabelPaint().setColor(color );	
		mPlotLine.getDotPaint().setColor(color );	
	}
	
	/**
	 * 返回线的颜色
	 * @return 线的颜色
	 */
	public int getLineColor() {
		return mPlotLine.getLinePaint().getColor();
	}
	
	/**
	 * 设置点的显示风格
	 * @param style 显示风格
	 */
	public void setDotStyle(XEnum.DotStyle style)
	{
		mPlotLine.setDotStyle(style);
	}
	
	/**
	 * 返回点的显示风格
	 * @return 显示风格
	 */
	public XEnum.DotStyle getDotStyle()
	{
		return mPlotLine.getDotStyle();
	}
	
	/**
	 * 设置当前记录的Key值
	 * @param value Key值
	 */
	public void setLineKey(String value) 
	{
		mLabel = value;
	}
	/**
	 * 返回Key值
	 * @return Key值
	 */
	public String getLineKey() {
		return mLabel;
	}
	
	/**
	 * 开放标签画笔
	 * @return 画笔
	 */
	public Paint getDotLabelPaint()
	{
		return mPlotLine.getDotLabelPaint();
	}
	
	/**
	 * 开放线的画笔
	 * @return 画笔
	 */
	public Paint getLinePaint()
	{
		return mPlotLine.getLinePaint();
	}
	
	/**
	 * 开放交叉点的画笔
	 * @return 画笔
	 */
	public Paint getDotPaint()
	{
		return mPlotLine.getDotPaint();
	}
	
	/**
	 * 开放交叉点的半径,用来决定绘制的点的图形的大小
	 * @param radius 半径
	 */
	public void setDotRadius(int radius)
	{
		 mPlotLine.getPlotDot().setDotRadius(radius);
	}
	
	
	/**
	 * 返回线的绘制类型(此设置对平滑曲线无效)
	 * @return 线类型
	 */
	public XEnum.LineStyle getLineStyle()
	{
		return mLineStyle; 
	}
		
	/**
	 * 设置线的绘制类型(此设置对平滑曲线无效)
	 * @param style 线类型
	 */
	public void setLineStyle(XEnum.LineStyle style)
	{
		mLineStyle = style;
	}
	
	/**
	 * 用于设置标签显示属性
	 * @return 标签属性类
	 */
	public PlotLabel getLabelOptions()
	{
		if(null == mPlotLabel)
		{
			mPlotLabel = new PlotLabelRender();
		}
		return mPlotLabel;
	}
}
