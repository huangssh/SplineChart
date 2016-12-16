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
package org.xclcharts.renderer.info;

import org.xclcharts.renderer.XEnum;

import android.graphics.Color;


/**
 * @ClassName AnchorDataPoint
 * @Description 数据批注基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class AnchorDataPoint {
	
	private int mDataSeriesID = -1; //
	private int mDataChildID = -1; //Points
	
	private XEnum.AnchorStyle mAnchorStyle = XEnum.AnchorStyle.RECT;
	private String mAnchor = "";
	private int mAnchorTextSize = 22;
	private int mAnchorTextColor = Color.BLUE;
	
	private int mBgColor = Color.BLACK;
	private int mAlpha = 100;
	private XEnum.DataAreaStyle mDataAreaStyle = XEnum.DataAreaStyle.STROKE;
	
	private float mRadius = 30.f;
	private float mRoundRaidus = 15.f;
	
	private int mLineWidth = -1;
	//private XEnum.LineStyle  mLineStyle = XEnum.LineStyle.SOLID;
	
	private float mCapRectW = 20.f;
	private float mCapRectH = 10.f;
	private float mCapRectHeight = 30.f;
	
	//线的风格(点或线之类)
	protected XEnum.LineStyle mLineStyle = XEnum.LineStyle.SOLID;
	
	
	public AnchorDataPoint()
	{
		
	}

	public AnchorDataPoint(int dataSeriesID,int dataChildID,XEnum.AnchorStyle anchorStyle)
	{
		mDataSeriesID = dataSeriesID;
		mDataChildID = dataChildID;
		mAnchorStyle = anchorStyle;
	}
	
	public AnchorDataPoint(int dataSeriesID,XEnum.AnchorStyle anchorStyle)
	{
		mDataSeriesID = dataSeriesID;
		mAnchorStyle = anchorStyle;
	}
	
	
	public XEnum.AnchorStyle getAnchorStyle() {
		return mAnchorStyle;
	}

	public void setAnchorStyle(XEnum.AnchorStyle style) {
		this.mAnchorStyle = style;
	}


	public int getDataSeriesID() {
		return mDataSeriesID;
	}



	public void setDataSeriesID(int mDataSeriesID) {
		this.mDataSeriesID = mDataSeriesID;
	}



	public int getDataChildID() {
		return mDataChildID;
	}



	public void setDataChildID(int mDataChildID) {
		this.mDataChildID = mDataChildID;
	}
	
	public void setAnchor(String anchor)
	{
		mAnchor = anchor;
	}
	
	public String getAnchor()
	{
		return mAnchor;
	}
	
	
	public void setRadius(float radius)
	{
		mRadius = radius;
	}
	
	public float getRadius()
	{
		return mRadius;
	}
	
	public void setTextSize(int size)
	{
		mAnchorTextSize = size;
	}
	
	public float getTextSize()
	{
		return mAnchorTextSize;
	}
	
	public void setTextColor(int color)
	{
		mAnchorTextColor = color;
	}
	
	public int getTextColor()
	{
		return mAnchorTextColor;
	}
	
	public void setLineWidth(int size)
	{
		mLineWidth = size;
	}
	
	public int getLineWidth()
	{
		return mLineWidth;
	}	
	
	//public XEnum.LineStyle getLineStyle()
	//{
	//	return mLineStyle;
	//}

	//public void setLineStyle(XEnum.LineStyle style)
	//{
	//	 mLineStyle = style;
	//}			
	
	public void setAlpha(int alpha)
	{
//		mAlpha = alpha;
	}
	
	public int getAlpha()
	{
		return mAlpha;
	}
	
	public void setBgColor(int color)
	{
		mBgColor = color;
	}
	
	public int getBgColor()
	{
		return mBgColor;
	}
	
	public void setAreaStyle(XEnum.DataAreaStyle style)
	{
		mDataAreaStyle = style;
	}
	
	public XEnum.DataAreaStyle getAreaStyle()
	{
		return mDataAreaStyle;
	}
	
	/**
	 * 当风格为CAPRECT时，可用此函数来设置三角形的宽/高
	 * @param width  三角形的宽
	 * @param height 三角形的高
	 */
	public void setCapRectAngleWH(float capWidth,float capHeight)
	{		
		mCapRectW = capWidth;
		mCapRectH = capHeight;
	}
		
	/**
	 * 当风格为CAPRECT时，设置Rect的高度
	 * @param rectHeight
	 */
	public void setCapRectHeight(float rectHeight)
	{
		mCapRectHeight = rectHeight;
	}
	
	
	public float getCapRectW()
	{
		return mCapRectW;
	}
	
	public float getCapRectH()
	{
		return mCapRectH;
	}
	
	public float getCapRectHeight()
	{
		return mCapRectHeight;
	}
	
	
	/**
	 * 设置角圆弧半径
	 * @param radius 半径
	 */
	public void setRoundRadius(int radius)
	{
		mRoundRaidus = radius;
	}
	
	/**
	 * 返回角圆弧半径
	 * @return 半径
	 */
	public float getRoundRadius()
	{		
		return mRoundRaidus;		
	}
	
	
	/**
	 * 设置线的风格(点或线之类)
	 * @param style 线的风格
	 */
	public void setLineStyle(XEnum.LineStyle  style)
	{
		mLineStyle = style;
	}
	
	
	/**
	 * 返回线的风格
	 * @return 线的风格
	 */
	public XEnum.LineStyle getLineStyle()
	{
		return mLineStyle;		
	}

}
