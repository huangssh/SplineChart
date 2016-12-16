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
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.Log;

/**
 * @ClassName Bar
 * @Description  柱形基类，定义了柱形的一些属性
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class Bar {
	
	private static final String TAG ="Bar";
	
	//确定是横向柱形还是竖向柱形图
	private XEnum.Direction mBarDirection = XEnum.Direction.VERTICAL;
	
	private XEnum.ItemLabelStyle mItemLabelStyle = XEnum.ItemLabelStyle.NORMAL;
	
	//柱形画笔
	private Paint mPaintBar = null;
	//内部柱形
	private Paint mPaintOutlineBar = null;
	
	
	//文字画笔
	private Paint mPaintItemLabel = null;		
	
	//柱形顶上文字偏移量
	private int mItemLabelAnchorOffset = 5;
	//柱形顶上文字旋转角度
	private float mItemLabelRotateAngle = 0.0f;	
	//是否显示柱形顶上文字标签
	private boolean mShowItemLabel = false;		
	
	//柱形间距所占比例
	private double mBarInnerMargin = 0.2f;
	
	//FlatBar类的有效，3D柱形无效
	private XEnum.BarStyle mBarStyle = XEnum.BarStyle.GRADIENT;
	
	//柱形所占总比例
	private float mBarTickSpacePercent = 0.7f;
	
	//柱子最大宽度
	private float mBarMaxPxWidth = 0.0f;
	//柱子最大高度
	private float mBarMaxPxHeight = 0.0f;
	
	//圆柱形的半径
	protected float mBarRoundRaidus = 15.0f;
	protected int mOutlineAlpha = 150;
	protected int mBorderWidth = 0;
	
	/**
	 * 设置角圆弧半径
	 * @param radius 半径
	 */
	public void setBarRoundRadius(int radius)
	{
		mBarRoundRaidus = radius;
	}
	
	/**
	 * 返回角圆弧半径
	 * @return 半径
	 */
	public float getBarRoundRadius()
	{		
		return mBarRoundRaidus;		
	}
	
	
	public Bar()
	{				
		
	}
		
	/**
	 * 返回柱形的显示方向
	 * @return 显示方向
	 */
	public XEnum.Direction getBarDirection() {
		return mBarDirection;
	}

	/**
	 * 设置柱形的显示方向
	 * @param direction 显示方向
	 */
	public void setBarDirection(XEnum.Direction direction) {
		this.mBarDirection = direction;
	}
	
	/**
	 * 设置柱形标签显示位置
	 * @param style 内/外/中间
	 */
	public void setItemLabelStyle(XEnum.ItemLabelStyle style){
		this.mItemLabelStyle = style;
	}
	
	/**
	 * 返回柱形标签显示位置
	 * @return
	 */
	public XEnum.ItemLabelStyle getItemLabelStyle(){
		return this.mItemLabelStyle;
	}

	/**
	 * 开放柱形画笔
	 * @return 画笔
	 */
	public Paint getBarPaint() {
		
		if(null == mPaintBar)
		{
			mPaintBar  = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaintBar.setColor(Color.rgb(252, 210, 9));
			mPaintBar.setStyle(Style.FILL);
		}				
		return mPaintBar;
	}
	
	/**
	 * 柱形风格为outline时,内部柱形的画笔
	 * @return 画笔
	 */
	public Paint getBarOutlinePaint() {
		if(null == mPaintOutlineBar)
		{
			mPaintOutlineBar = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaintOutlineBar.setStyle(Style.FILL);
		}
		return mPaintOutlineBar;
	}
	
	/**
	 * 柱形风格为outline时,内部柱形颜色相对于外围柱形的透明度
	 * @param alpha 透明度
	 */
	public void setOutlineAlpha(int alpha){ 
		mOutlineAlpha = alpha;
	}
	
	/**
	 * 柱形风格为outline时,外部柱形的宽度
	 * @param width 宽度
	 */
	public void setBorderWidth(int width){
		mBorderWidth = width;
	}
	
	/**
	 * 开放柱形顶部标签画笔
	 * @return 画笔
	 */
	public Paint getItemLabelPaint() {
		
		//柱形顶上的文字标签	
		if(null == mPaintItemLabel)
		{
			mPaintItemLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaintItemLabel.setTextSize(12);
			mPaintItemLabel.setColor(Color.BLACK);
			mPaintItemLabel.setTextAlign(Align.CENTER);
		}		
		return mPaintItemLabel;
	}

	/**
	 * 返回柱形顶部标签在显示时的偏移距离
	 * @return 偏移距离
	 */
	public int getItemLabelAnchorOffset() {
		return mItemLabelAnchorOffset;
	}

	/**
	 * 设置柱形顶部标签在显示时的偏移距离
	 * @param offset 偏移距离
	 */
	public void setItemLabelAnchorOffset(int offset) {
		this.mItemLabelAnchorOffset = offset;
	}

	/**
	 * 返回柱形顶部标签在显示时的旋转角度
	 * @return 旋转角度
	 */
	public float getItemLabelRotateAngle() {
		return mItemLabelRotateAngle;
	}

	/**
	 * 设置柱形顶部标签在显示时的旋转角度
	 * @param rotateAngle 旋转角度
	 */
	public void setItemLabelRotateAngle(float rotateAngle) {
		this.mItemLabelRotateAngle = rotateAngle;
	}

	/**
	 * 设置是否显示柱形顶部标签
	 * @param visible 是否显示
	 */
	public void setItemLabelVisible(boolean visible) {
		this.mShowItemLabel = visible;
	}
	
	/**
	 * 设置所有柱形间占刻度间总空间的百分比(默认为0.7即70%)
	 * @param percent 百分比
	 */
	public boolean setBarTickSpacePercent(float percent)
	{		
		if(Float.compare(percent, 0.f) == -1)
		{
			Log.e(TAG, "此比例不能为负数噢!");
			return false;
		}if( Float.compare(percent, 0.f) ==  0  ){  
			Log.e(TAG, "此比例不能等于0!");
			return false;
		}else{
			mBarTickSpacePercent = percent;
		}
		return true;
	}
	
	
	/**
	 * 设置柱形间空白所占的百分比
	 * @param percent 百分比
	 */
	public boolean setBarInnerMargin(float percent)
	{		
		if(Float.compare(percent, 0.f) == -1)
		{
			Log.e(TAG, "此比例不能为负数噢!");
			return false;
		}if( Float.compare(percent, 0.9f) ==  1 
				|| Float.compare(percent, 0.9f) ==  0 ){  
			Log.e(TAG, "此比例不能大于等于0.9,要给柱形留下点显示空间!");
			return false;
		}else{
			this.mBarInnerMargin = percent;
		}
		return true;
	}
	
	/**
	 * 得到柱形间空白所占的百分比
	 * @return 百分比
	 */
	public double getInnerMargin()
	{
		return mBarInnerMargin;
	}
	
	
	/**
	 * 返回是否显示柱形顶部标签
	 * @return 是否显示
	 */
	public boolean getItemLabelsVisible()
	{
		return mShowItemLabel;
	}
	
	/**
	 * 设置柱子的最大宽度范围，仅在竖向柱图中有效
	 * @param width 最大宽度
	 */
	public void setBarMaxPxWidth(float width)
	{
		mBarMaxPxWidth = width;
	}
	
	/**
	 * 返回柱子的最大宽度范围，仅在竖向柱图中有效
	 * @return 最大宽度
	 */
	public float getBarMaxPxWidth()
	{
		return mBarMaxPxWidth;
	}
	
	/**
	 * 设置柱子的最大高度范围，仅在横向柱图中有效
	 * @param height 最大高度
	 */
	public void setBarMaxPxHeight(float height)
	{
		mBarMaxPxHeight = height;
	}
	
	/**
	 * 返回柱子的最大高度范围，仅在横向柱图中有效
	 * @return 最大高度范围
	 */
	public float getBarMaxPxHeight()
	{
		return mBarMaxPxHeight;
	}
	
	
	/**
	 * 计算同标签多柱形时的Y分隔
	 * @param YSteps    Y轴步长
	 * @param barNumber  柱形个数
	 * @return 返回单个柱形的高度及间距
	 */	
	protected  float[] calcBarHeightAndMargin(float YSteps,int barNumber)
	{			
			if(0 == barNumber)
			{
				Log.e(TAG,"柱形个数为零.");
				return null;		
			}					
			float labelBarTotalHeight = MathHelper.getInstance().mul(YSteps , mBarTickSpacePercent);						
			float barTotalInnerMargin = MathHelper.getInstance().mul(
											labelBarTotalHeight ,(float) mBarInnerMargin ); 
			float barInnerMargin = MathHelper.getInstance().div(barTotalInnerMargin , barNumber);
			float barHeight = MathHelper.getInstance().div( 
									MathHelper.getInstance().sub(labelBarTotalHeight ,
																 barTotalInnerMargin) , 
									barNumber);			
			float[] ret = new float[2];
			
			if( Float.compare(mBarMaxPxHeight, 0.0f) ==  1 && 
					Float.compare(barHeight, mBarMaxPxHeight) == 1 )
			{
				barHeight = mBarMaxPxHeight;
			}
			
			ret[0] = barHeight;
			ret[1] = barInnerMargin;
			
			return ret;
	}
	

	/**
	 * 计算同标签多柱形时的X分隔
	 * @param XSteps	X轴步长
	 * @param barNumber 柱形个数
	 * @return 返回单个柱形的宽度及间距
	 */
	protected float[] calcBarWidthAndMargin(float XSteps,int barNumber)
	{			
			if(0 == barNumber)
			{
				Log.e(TAG,"柱形个数为零.");
				return null;		
			}
			float labelBarTotalWidth = MathHelper.getInstance().mul(XSteps , mBarTickSpacePercent); 				
			float barTotalInnerMargin = MathHelper.getInstance().mul(labelBarTotalWidth , 
					(float) mBarInnerMargin);
			
			float barTotalWidth = MathHelper.getInstance().sub(labelBarTotalWidth , barTotalInnerMargin);			
			float barInnerMargin = MathHelper.getInstance().div(barTotalInnerMargin , barNumber);
			float barWidth = MathHelper.getInstance().div(barTotalWidth , barNumber);		
			
			float[] ret = new float[2];
			
			if( Float.compare(mBarMaxPxWidth, 0.0f) ==  1 && 
					Float.compare(barWidth, mBarMaxPxWidth) == 1 )
			{
				barWidth = mBarMaxPxWidth;
			}
			ret[0] = barWidth;
			ret[1] = barInnerMargin;			
			return ret;
	}
	
	/**
	 * 绘制柱形顶部标签
	 * @param text	内容	
	 * @param x		x坐标
	 * @param y		y坐标
	 * @param canvas 画布
	 */
	protected void drawBarItemLabel(String text,float x,float y,Canvas canvas)
	{
		//在柱形的顶端显示上柱形的当前值			
		if(getItemLabelsVisible() && text.length()> 0)
		{							
			//要依横向还是竖向
			//如果是背向式的，还要看是向上还是向下
			
			float cx = x;
			float cy = y;
			
			
			switch(mBarDirection)
			{
			case VERTICAL:
				
				float textHeight = DrawHelper.getInstance().getPaintFontHeight(getItemLabelPaint());
				// NORMAL,INNER,OUTER
				switch(mItemLabelStyle)
				{
				case OUTER:
					cy -= this.mItemLabelAnchorOffset;
					cy -= textHeight;
					break;
				case INNER:
					cy += this.mItemLabelAnchorOffset;
					cy += textHeight;
					break;
				default:
					cy -= this.mItemLabelAnchorOffset;
				}																						
				break;
			case HORIZONTAL:
				float textWidth = DrawHelper.getInstance().getTextWidth(getItemLabelPaint(), text);						
				switch(mItemLabelStyle)
				{
				case OUTER:
					cx += this.mItemLabelAnchorOffset;
					cx += textWidth;
					break;
				case INNER:										
					cx -= this.mItemLabelAnchorOffset;
					cx -= textWidth;
					break;
				default:
					cx += this.mItemLabelAnchorOffset;
				}
				break;	
			default:
				break;
			}
									
			DrawHelper.getInstance().drawRotateText(text,
								cx ,
								cy,
	            			  getItemLabelRotateAngle(),
	            			  canvas, 
	            			  getItemLabelPaint());	
		}
	}
	
	/**
	 * 设置柱形的显示风格，对3D柱形无效
	 * @param style 显示风格
	 */
	public void setBarStyle(XEnum.BarStyle style)
	{
		this.mBarStyle = style;
	}
	
	/**
	 * 返回当前柱形的显示风格,对3D柱形无效
	 * @return 显示风格
	 */
	public XEnum.BarStyle getBarStyle()
	{
		return this.mBarStyle;
	}

}
