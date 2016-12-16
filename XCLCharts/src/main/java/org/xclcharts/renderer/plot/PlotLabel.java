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
 * @version 2.0
 */
package org.xclcharts.renderer.plot;

import org.xclcharts.renderer.XEnum;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


/**
 * @ClassName PlotLabel
 * @Description 用于设定标签显示的属性
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *
 */

public class PlotLabel {
	
	//标签与框的保留间距
	protected float mMargin  = 5f;	
			
	//box
	protected BorderRender mBorder = null;
	//protected boolean mShowBox = false;
	protected boolean mShowBoxBorder = true;
	protected boolean mShowBackground = true;
	
	//protected boolean mShowBoxCap = true;
			
	//标签起始偏移多少距离
	protected float mOffsetX = 0.0f;
	protected float mOffsetY = 0.0f;
	
	protected final int DEF_BOX_BG_ALPHA = 100;
	
	//带箭头的框中，箭头的高度
	protected float mScale = 0.2f;
	//圆框半径
	protected float mRadius = 0.f;
	
	protected XEnum.LabelBoxStyle mLabelBoxStyle = XEnum.LabelBoxStyle.CAPRECT;
				
	public PlotLabel() {
		// TODO Auto-generated constructor stub						
	}
	
	 /**
	  * 开放标签框绘制基类
	  * @return  框绘制类
	  */
	 public Border getBox()
	 {		
		 initBox();
		return mBorder;
	 }
	 
	 protected void initBox()
	 {
		 if(null == mBorder)
		{
			 mBorder = new BorderRender();			
			 mBorder.setBorderRectType(XEnum.RectType.RECT);
			 mBorder.getBackgroundPaint().setColor(Color.rgb(240, 255, 112));			 
//			 mBorder.getBackgroundPaint().setAlpha(DEF_BOX_BG_ALPHA);
		}
	 }

	
	/**
	 * 不显示标签边框		
	 */
	public void hideBorder()
	{
		mShowBoxBorder = false;
	}
	
	/**
	 * 不显示标签背景	
	 */
	public void hideBackground()
	{
		mShowBackground = false;
	}
	

	/**
	 * 显示标签边框		
	 */
	public void showBorder()
	{
		mShowBoxBorder = true;
	}
	
	/**
	 * 显示图背景		
	 */
	public void showBackground()
	{
		mShowBackground = true;
	}	
	
	/**
	 * 带箭头的标识框中，其箭头的高度(占整宽度的比例)
	 * @param scale 比例
	 */
	public void setCapBoxAngleHeight(float scale)
	{
		mScale = scale;
	}
	
	/**
	 * 圆形标识框中，其半径长度
	 * @param radius 半径
	 */
	public void setCircleBoxRadius(float radius)
	{
		mRadius = radius;
	}
			
			
	/**
	 * 显示标签标识风格。默认为带箭头的标识框
	 * @param style 风格
	 */
	public void setLabelBoxStyle(XEnum.LabelBoxStyle style)
	{
		mLabelBoxStyle = style;
		
		if(XEnum.LabelBoxStyle.TEXT ==  mLabelBoxStyle)
		{
			this.hideBorder();
			this.hideBackground();
			return;
		}
		
		if(XEnum.LabelBoxStyle.CIRCLE ==  mLabelBoxStyle)
		{
			this.hideBorder();
			showBackground();
		}else{
			showBorder();
			showBackground();
		}		
	}	
	

	 /**
	  * 标签向X方向偏移多少距离
	  * @param offset 偏移值
	  */
	 public void setOffsetX(float offset)
	 {		 
		 mOffsetX = offset;
	 }
	 
	 /**
	  * 标签向Y方向偏移多少距离
	  * @param offset 偏移值
	  */
	 public void setOffsetY(float offset)
	 {		 
		 mOffsetY = offset;
	 }
	 
		
	 /**
	  * 标签与框的保留间距
	  * @param margin 间距
	  */
	 public void setMargin(float margin)
	 {		 
		 mMargin = margin;
	 }
	 
	 /**
	  * 返回标签与边框的间距
	  * @return 间距
	  */
	 public float getMargin()
	 {
		 return mMargin;
	 }
	 
	 
	public boolean drawLabel(Canvas canvas,Paint paint,String label,
								float cX,float cY,float itemAngle)
	 {
		 return true;
	 }
	
	
	public boolean drawLabel(Canvas canvas,Paint paint,String label,
			float cX,float cY,float itemAngle,int borderColor)
	{
		return true;
	}
	 
}
