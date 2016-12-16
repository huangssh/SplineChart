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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

/**
 * @ClassName CustomLineData
 * @Description 定制线基类用于强调标准,底线之类,也可用于显示计算出来的平均线之类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * 
 */

public class CustomLineData {
	
	private String mLabel = "";
	private Double mDesireValue = 0d;
	private int mColor = Color.BLACK;
	private int mLineStroke = 0;
	
	//文字旋转角度
	private float mLabelRotateAngle = 0.0f; //-45f;
	
	//设置Label显示位置(左，中，右)
	private Align  mLabelAlign = Align.RIGHT;
	
	//设置Label显示位置(上，中，下)
	private XEnum.VerticalAlign mLabelPostion = XEnum.VerticalAlign.TOP;
				
	//线的风格(点或线之类)
	private XEnum.LineStyle mLineStyle = XEnum.LineStyle.SOLID;
	//设置线箭头 (三角，方形，棱形....)  
	private XEnum.DotStyle mLineCap = XEnum.DotStyle.HIDE;
	
	//标签偏移距离,注意，如是显示在中间，则会上移动此距离。
	private  int mLabelOffset = 0;
		
	//定制线画笔
	private Paint mPaintCustomLine=null;
	private Paint mPaintLineLabel=null;
	//是否显示线
	private boolean mLineVisible=true;	
	
	private Bitmap bitmap;
	
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public CustomLineData()
	{

	}
		
	/**
	 * 定制线
	 * @param value	区分值
	 * @param color	线颜色
	 */
	public CustomLineData(
				  Double value,
				  int color) 
	{		
	
		setValue(value);
		setColor(color);
	}
	
	/**
	 * 定制线
	 * @param value	区分值
	 * @param 上下文
	 */
	public CustomLineData(
				  Double value,
				  Bitmap  bmp)
	{		
		setBitmap(bmp);
		setValue(value);
	}
			
	/**
	 * 定制线
	 * @param label		标签
	 * @param value		用区分的值
	 * @param color		线颜色
	 * @param stroke	线粗细
	 */
	public CustomLineData(String label,
						  Double value,
						  int color,
						  int stroke) 
	{	
		setLabel(label);
		setValue(value);
		setColor(color);
		setLineStroke(stroke);
	}


	
	/**
	 * 返回标签
	 * @return 标签
	 */
	public String getLabel() {
		return mLabel;
	}

	/**
	 * 设置标签
	 * @param label 标签
	 */
	public void setLabel(String label) {
		this.mLabel = label;
	}

	/**
	 * 取得当前区分值
	 * @return 区分值
	 */
	public Double getValue() {
		return mDesireValue;
	}

	/**
	 * 设置区分值
	 * @param value 区分值
	 */
	public void setValue(Double value) {
		this.mDesireValue = value;
	}

	/**
	 * 返回颜色
	 * @return 颜色
	 */
	public int getColor() {
		return mColor;
	}

	/**
	 * 设置颜色
	 * @param color 颜色
	 */
	public void setColor(int color) {
		this.mColor = color;
	}

	/**
	 * 得到当前线粗细
	 * @return 线粗细
	 */
	public int getLineStroke() {
		return mLineStroke;
	}

	/**
	 * 设置线的粗细
	 * @param stroke 粗细
	 */
	public void setLineStroke(int stroke) {
		this.mLineStroke = stroke;
	}
	
	/**
	 *  返回是否有手工指定线的 粗细
	 * @return 是否有指定
	 */
	public boolean isSetLineStroke()
	{
		return ((0 == mLineStroke)?false:true);
	}
	
	
	// 设置线箭头 (三角，方形，棱形....)  
	public void setCustomLineCap(XEnum.DotStyle style) {
		this.mLineCap = style;
	}
	
	public XEnum.DotStyle getCustomeLineCap()
	{
		return mLineCap;
	}
	
	
	/**
	 * 设置标签显示位置(左，中，右),适合于竖向图，在横向图下设置无效.
	 * @param align 位置
	 */
	public void setLabelHorizontalPostion(Align align) //XEnum.LabelAlign
	{
		mLabelAlign = align;
	}
	
	/**
	 * 返回标签显示在左，中，右哪个位置
	 * @return 位置
	 */
	public Align getLabelHorizontalPostion()
	{
		return mLabelAlign;
	}
	
	
	/**
	 * 设置标签显示位置(上，中，下),适合于横向图，在竖向图下设置无效.
	 * @param postion 显示位置
	 */
	public void setLabelVerticalAlign(XEnum.VerticalAlign postion)
	{
		mLabelPostion = postion;
	}
	
	/**
	 * 返回标签显示在上，中，下哪个位置
	 * @return 位置
	 */
	public XEnum.VerticalAlign getLabelVerticalAlign()
	{
		return mLabelPostion;
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
		
		
	/**
	 * 返回轴标签文字旋转角度
	 * @return 旋转角度
	 */
	public float getLabelRotateAngle() {
		return mLabelRotateAngle;
	}

	/**
	 * 设置轴标签文字旋转角度
	 * @param rotateAngle 旋转角度
	 */
	public void setLabelRotateAngle(float rotateAngle) {
		this.mLabelRotateAngle = rotateAngle;
	}
	
	/**
	 * 设置标签的偏移距离
	 * @param offset 偏移距离
	 */
	public void setLabelOffset(int offset)
	{
		mLabelOffset = offset;
	}
	
	/**
	 * 返回标签的偏移距离
	 * @return 偏移距离
	 */
	public int getLabelOffset()
	{
		return mLabelOffset;
	}
	
	/**
	 * 开放定制线画笔
	 * @return 画笔
	 */
	public Paint getCustomLinePaint()
	{
		if(null == mPaintCustomLine)
		{
			mPaintCustomLine = new Paint();
			mPaintCustomLine.setAntiAlias(true);
			mPaintCustomLine.setStrokeWidth(3);
			mPaintCustomLine.setTextSize(18);
			mPaintCustomLine.setTextAlign(Align.LEFT);
		}
		return mPaintCustomLine;
	}
	
	
	/**
	 * 开放定制线标签画笔
	 * @return 画笔
	 */
	public Paint getLineLabelPaint()
	{		
		if(null == mPaintLineLabel)
		{
			mPaintLineLabel = new Paint();
			mPaintLineLabel.setAntiAlias(true);
			mPaintLineLabel.setStrokeWidth(3);
			mPaintLineLabel.setTextSize(18);
			mPaintLineLabel.setTextAlign(Align.LEFT);
		}
		
		return mPaintLineLabel;
	}
	
	/**
	 * 是否隐藏线不显示
	 * @return 是否显示
	 */
	public boolean isShowLine()
	{
		return mLineVisible;
	}
	
	/**
	 * 隐藏线让其不显示
	 */
	public void hideLine()
	{
		mLineVisible = false;
	}
	
	/**
	 * 显示线
	 */
	public void showLine()
	{
		mLineVisible = true;
	}
	
	
}
