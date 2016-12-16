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

/**
 *  version	 	date
 *  0.1 		2014-06-12
 *  1.0 		2014-07-04
 *  1.2			2014-07-13
 */

package org.xclcharts.renderer;

/**
 * @ClassName XChart
 * @Description 所有图表类的基类,定义了图表区，标题，背景等
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * 
 */

import java.util.List;

import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.info.AnchorDataPoint;
import org.xclcharts.renderer.info.AnchorRender;
import org.xclcharts.renderer.info.DyLine;
import org.xclcharts.renderer.info.DyLineRender;
import org.xclcharts.renderer.info.Legend;
import org.xclcharts.renderer.info.LegendRender;
import org.xclcharts.renderer.plot.Border;
import org.xclcharts.renderer.plot.BorderRender;
import org.xclcharts.renderer.plot.PlotArea;
import org.xclcharts.renderer.plot.PlotAreaRender;
import org.xclcharts.renderer.plot.PlotGrid;
import org.xclcharts.renderer.plot.PlotGridRender;
import org.xclcharts.renderer.plot.PlotLegend;
import org.xclcharts.renderer.plot.PlotLegendRender;
import org.xclcharts.renderer.plot.PlotTitle;
import org.xclcharts.renderer.plot.PlotTitleRender;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;

public class XChart implements IRender {
		
	//private static final String TAG = "XChart";
	// 开放主图表区
	protected PlotAreaRender plotArea = null;
	// 开放主图表区网格
	protected PlotGridRender plotGrid = null;
	// 标题栏
	private PlotTitleRender plotTitle = null;
	// 图大小范围
	private float mLeft = 0.0f;
	private float mTop = 0.0f;
	private float mRight = 0.0f;
	private float mBottom = 0.0f; //5f;
	// 图宽高
	private float mWidth = 0.0f;
	private float mHeight = 0.0f;

	// 图的内边距属性
	private float mPaddingTop = 0.f;
	private float mPaddingBottom = 0.f;
	private float mPaddingLeft = 0.f;
	private float mPaddingRight = 0.f;
	// 是否画背景色
	private boolean mBackgroundColorVisible = false;
	
	//坐标系原点坐标
	protected float[] mTranslateXY = new float[2];		
		
	//是否显示边框
	private boolean mShowBorder = false;
	private BorderRender mBorder = null;
	
	//图例类
	protected PlotLegendRender plotLegend = null;
	
	//动态图例
	private LegendRender mDyLegend = null;
	
	private boolean mEnableScale = true;
	private float mXScale = 0.0f, mYScale = 0.0f;
	private float mCenterX  = 0.0f, mCenterY  = 0.0f;
		
	//是否显示十字交叉线
	private boolean mDyLineVisible = false;
	private DyLineRender mDyLine = null;
	
	//是否平移
	protected boolean mEnablePanMode = true;
	//平移模式下的可移动方向
	private XEnum.PanMode mPlotPanMode = XEnum.PanMode.FREE;
	//限制图表滑动范围
	private boolean mControlPanRange = true;
		
		
	public XChart() {
		initChart();		
	}

	private void initChart() {
		//默认的原点坐标
		mTranslateXY[0] = 0.0f;
		mTranslateXY[1] = 0.0f;
				
		//图例
		if(null == plotLegend)plotLegend = new PlotLegendRender(this);	
		
		// 图表
		if(null == plotArea)plotArea = new PlotAreaRender();
		
		if(null == plotGrid)plotGrid = new PlotGridRender();
		
		if(null == plotTitle)plotTitle = new PlotTitleRender();						
	}
	
	/**
	 * 返回当前绘制的是什么类型的图
	 * @return 类型
	 */
	public XEnum.ChartType getType()
	{
		return XEnum.ChartType.NONE;
	}

	
	/**
	 * 开放图例基类
	 * @return	基类
	 */
	public PlotLegend getPlotLegend()
	{
		//图例
		if(null == plotLegend)plotLegend = new PlotLegendRender(this);
		return plotLegend;
	}	

	/**
	 * 用于指定绘图区与图范围的内边距。单位为PX值. 即用于确定plotArea范围
	 * 
	 * @param left
	 *            绘图区与图左边的保留宽度，用于显示左边轴及轴标题之类
	 * @param top
	 *            绘图区与图顶部的保留距离，用于显示标题及图例之类
	 * @param right
	 *            绘图区与图右边的保留宽度，用于显示右边轴及轴标题之类
	 * @param bottom
	 *            绘图区与图底部的保留距离，用于显示底轴及轴标题之类	
	 */
	public void setPadding(float left, float top, float right,float bottom  ) {
		if (top > 0)
			mPaddingTop = top;
		if (bottom > 0)
			mPaddingBottom = bottom ;
		if (left > 0)
			mPaddingLeft = left;
		if (right > 0)
			mPaddingRight = right ;
	}
			

	/**
	 * 返回主图表区基类
	 * 
	 * @return 主图表区基类
	 */
	public PlotArea getPlotArea() {
		if(null == plotArea)plotArea = new PlotAreaRender();
		return plotArea;
	}

	/**
	 * 返回主图表区网格基类
	 * 
	 * @return 网格基类
	 */
	public PlotGrid getPlotGrid() {
		if(null == plotGrid)plotGrid = new PlotGridRender();
		return plotGrid;
	}

	/**
	 * 返回图的标题基类
	 * 
	 * @return 标题基类
	 */
	public PlotTitle getPlotTitle() {
		if(null == plotTitle)plotTitle = new PlotTitleRender();
		return plotTitle;
	}
	
	/**
	 * 设置图表绘制范围.
	 * @param width
	 *            图表宽度
	 * @param height
	 *            图表高度
	 */
	public void setChartRange( float width,float height) {	
		setChartRange(0.0f,0.0f,width,height);
	}
	

	/**
	 * 设置图表绘制范围,以指定起始点及长度方式确定图表大小.
	 * 
	 * @param startX
	 *            图表起点X坐标
	 * @param startY
	 *            图表起点Y坐标
	 * @param width
	 *            图表宽度
	 * @param height
	 *            图表高度
	 */
	public void setChartRange(float startX, float startY, float width,
			float height) {
		
		if (startX > 0)
			mLeft = startX;
		if (startY > 0)
			mTop = startY;
				
		mRight = add(startX , width);
		mBottom = add(startY , height);

		if (Float.compare(width, 0.0f) > 0)mWidth = width;
		if (Float.compare(height, 0.0f) > 0)mHeight = height;
	}

	/**
	 * 设置标题
	 * 
	 * @param title 标题
	 */
	public void setTitle(String title) {
		if(null!= plotTitle)plotTitle.setTitle(title);
	}

	/**
	 * 设置子标题
	 * 
	 * @param subtitle 子标题
	 */
	public void addSubtitle(String subtitle) {
		if(null!= plotTitle)plotTitle.setSubtitle(subtitle);
	}

	/**
	 * 设置标题上下显示位置,即图上边距与绘图区间哪个位置(靠上，居中，靠下).
	 * @param position 显示位置
	 */
	public void setTitleVerticalAlign(XEnum.VerticalAlign position) {
		if(null!= plotTitle)plotTitle.setVerticalAlign(position);
	}

	/**
	 * 设置标题横向显示位置(靠左，居中，靠右)
	 * 
	 * @param align 显示位置
	 */
	public void setTitleAlign(XEnum.HorizontalAlign align) {
		if(null!= plotTitle)plotTitle.setTitleAlign(align);
	}
	

	/**
	 * 返回图表左边X坐标
	 * 
	 * @return 左边X坐标
	 */
	public float getLeft() {		
		return mLeft;
	}

	/**
	 * 返回图表上方Y坐标
	 * 
	 * @return 上方Y坐标
	 */
	public float getTop() {
		return mTop;
	}

	/**
	 * 返回图表右边X坐标
	 * 
	 * @return 右边X坐标
	 */
	public float getRight() {
		return mRight;
	}

	/**
	 * 返回图表底部Y坐标
	 * 
	 * @return 底部Y坐标
	 */
	public float getBottom() {
		return mBottom;
	}

	/**
	 * 返回图表宽度
	 * 
	 * @return 宽度
	 */
	public float getWidth() {
		
		return mWidth;
	}

	/**
	 * 返回图表高度
	 * 
	 * @return 高度
	 */
	public float getHeight() {
		return mHeight;
	}

	/**
	 * 返回图绘制区相对图顶部边距的高度
	 * 
	 * @return 绘图区与图边距间的PX值
	 */
	public float getPaddingTop() {
		return this.mPaddingTop;
	}
	
	/**
	 * 返回图绘制区相对图底部边距的高度
	 * 
	 * @return 绘图区与图边距间的PX值
	 */
	public float getPaddingBottom() {
		return mPaddingBottom;
	}

	/**
	 * 图绘制区相对图左边边距的宽度
	 * 
	 * @return 绘图区与图边距间的PX值
	 */
	public float getPaddingLeft() {
		return mPaddingLeft;
	}

	/**
	 * 图绘制区相对图右边边距的宽度
	 * 
	 * @return 绘图区与图边距间的PX值
	 */
	public float getPaddingRight() {
		return mPaddingRight;
	}
	
	/**
	 * 返回图中心点坐标
	 * @return 坐标
	 */	
	public PointF getCenterXY()
	{
		PointF point = new PointF();
		point.x = this.getLeft() + div(this.getWidth() , 2f) ;
		point.y = this.getTop() + div(this.getHeight() , 2f) ;		
		return point;
	}
	
	
	/**
	 * 设置绘画时的坐标系原点位置
	 * @param x 原点x位置
	 * @param y 原点y位置
	 */
	public void setTranslateXY(float x,float y)
	{
		if(!mEnablePanMode)return; 
		if(null == mTranslateXY) mTranslateXY = new float[2];	
		mTranslateXY[0] = x;
		mTranslateXY[1] = y;
	}
	
	/**
	 * 返回坐标系原点坐标
	 * @return 原点坐标
	 */
	public float[] getTranslateXY()
	{
		return mTranslateXY;
	}
		
	/**
	 * 计算图的显示范围,依屏幕px值来计算.
	 */
	protected void calcPlotRange() {	
		
		int borderWidth = getBorderWidth();
		if(null == plotArea) return;
		plotArea.setBottom(sub(this.getBottom() - borderWidth/2 , mPaddingBottom) );
		plotArea.setLeft(add(this.getLeft() + borderWidth/2 , mPaddingLeft));
		plotArea.setRight(sub(this.getRight() - borderWidth/2 , mPaddingRight));		
		plotArea.setTop(add(this.getTop() + borderWidth/2 , mPaddingTop));
	}
	
	/**
	 * 绘制标题
	 */
	protected void renderTitle(Canvas canvas) {				
		int borderWidth = getBorderWidth();
		if(null == plotTitle) return;
		this.plotTitle.renderTitle(
				mLeft + borderWidth, mRight - borderWidth, mTop + borderWidth,
				mWidth, this.plotArea.getTop(), canvas);
	}
	
	
	/**
	 * 绘制批注
	 * @param anchorSet 批注集合
	 * @param dataID    主数据集id
	 * @param childID   子数据集id
	 * @param canvas	画布
	 * @param x			X坐标点
	 * @param y			y坐标点
	 * @return 是否有绘制
	 */
	protected boolean drawAnchor(List<AnchorDataPoint> anchorSet,
								int dataID,int childID,Canvas canvas,float x,float y,float radius)
	{
		if(null == anchorSet || -1 == dataID)return false;
		int count = anchorSet.size();
		
		float left = getPlotArea().getLeft();
		float right = getPlotArea().getRight();
		float top = getPlotArea().getTop();
		float bottom = getPlotArea().getBottom();
		
		for(int i=0;i<count;i++)
		{
			AnchorDataPoint an = anchorSet.get(i);
			if(an.getDataSeriesID() == dataID )
			{
				if( (-1 == childID || -1 == an.getDataChildID() ) 
					|| ( -1 != childID && an.getDataChildID() == childID))
				{
					AnchorRender.getInstance().renderAnchor(canvas, an, x, y,radius,
															left,top,right,bottom);
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 设置是否绘制背景
	 * 
	 * @param visible 是否绘制背景
	 */
	public void setApplyBackgroundColor(boolean visible) {
		mBackgroundColorVisible = visible;
	}

	/**
	 * 设置图的背景色
	 * 
	 * @param color   背景色
	 */
	public void setBackgroundColor(int color) {
		
		getBackgroundPaint().setColor(color);
		getPlotArea().getBackgroundPaint().setColor(color);
		
		if(null == mBorder)mBorder = new BorderRender();
		mBorder.getBackgroundPaint().setColor(color);		
	}
	
	/**
	 * 设置图的渐变背景色
	 * @param direction	渐变方向
	 * @param beginColor 起始颜色
	 * @param endColor	结束颜色
	 */
	public void setBackgroundColor(XEnum.Direction direction, int beginColor,int endColor) {
		
		//getPlotArea().getBackgroundPaint().setColor(areaColor);
		
		if(beginColor == endColor)
		{
			getBackgroundPaint().setColor(beginColor);
		}else{
						
			LinearGradient  linearGradient ;
			if(direction == XEnum.Direction.VERTICAL)
			{
				linearGradient = new LinearGradient(
						0, 0, 0, getBottom() - getTop(),							
						beginColor,endColor, 				 
						 Shader.TileMode.MIRROR);
			}else{
				linearGradient = new LinearGradient(
						 getLeft(),getBottom(),getRight(),getTop(),
						 beginColor,endColor, 				 
						 Shader.TileMode.CLAMP);
			}
			getBackgroundPaint().setShader(linearGradient);    
		}
		
		if(null == mBorder)mBorder = new BorderRender();
		mBorder.getBackgroundPaint().setColor(endColor);		
	}

	
	/**
	 * 开放背景画笔
	 * 
	 * @return 画笔
	 */
	public Paint getBackgroundPaint() {
		if(null == mBorder)mBorder = new BorderRender();
		return mBorder.getBackgroundPaint();			
	}
	
	/**
	 * 显示矩形边框
	 */
	public void showBorder()
	{
		 mShowBorder = true;
		 if(null == mBorder)mBorder = new BorderRender();
		 mBorder.setBorderRectType(XEnum.RectType.RECT);
	}
	
	/**
	 * 显示圆矩形边框
	 */
	public void showRoundBorder()
	{
		mShowBorder = true;
		if(null == mBorder)mBorder = new BorderRender();
		mBorder.setBorderRectType(XEnum.RectType.ROUNDRECT);
	}
	
	/**
	 * 隐藏边框
	 */
	public void hideBorder()
	{
		 mShowBorder = false;
		 if(null != mBorder)mBorder = null;
	}	
	
	/**
	 * 开放边框绘制类
	 * @return 边框绘制类
	 */
	public Border getBorder()
	{
		if(null == mBorder)mBorder = new BorderRender();
		return mBorder; 
	}
	
	/**
	 * 是否显示边框
	 * @return 是否显示
	 */
	public boolean isShowBorder()
	{
		return mShowBorder;
	}
	
	/**
	 * 得到边框宽度,默认为5px
	 * @return 边框宽度
	 */
	public int getBorderWidth()
	{
		int borderWidth = 0;
		if(mShowBorder)
		{
			 if(null == mBorder)mBorder = new BorderRender();
			 borderWidth = mBorder.getBorderWidth();
		}
		return borderWidth;
	}
	
	/**
	 * 设置边框宽度
	 * @param width 边框宽度
	 */
	public void setBorderWidth(int width)
	{
		 if(0 >= width) return;
		 if(null == mBorder)mBorder = new BorderRender();
		 mBorder.setRoundRadius(width);
	}
	
	/**
	 * 绘制边框
	 * @param canvas 画布
	 */
	protected void renderBorder(Canvas canvas)
	{
		if(mShowBorder)
		{
			if(null == mBorder) mBorder = new BorderRender();				
			mBorder.renderBorder("BORDER",canvas, 
								 mLeft  , mTop  , mRight , mBottom  ); 
		}
	}
	
	/**
	 * 绘制图的背景
	 */
	protected void renderChartBackground(Canvas canvas) {
		
		if(this.mBackgroundColorVisible)
		{		
			if(null == mBorder) mBorder = new BorderRender();				
			if(mShowBorder)
			{
				mBorder.renderBorder("CHART",canvas, 
									 mLeft  , mTop  , mRight , mBottom  ); 		
			}else{ //要清掉 border的默认间距				
				int borderSpadding = mBorder.getBorderSpadding();				 
				mBorder.renderBorder("CHART",canvas, 
						mLeft - borderSpadding , mTop - borderSpadding , 
						mRight + borderSpadding, mBottom + borderSpadding  ); 
			}
		}
	}
	
	
	
	/**
	 * 设置缩放参数
	 * @param xScale	x方向缩放比例
	 * @param yScale	y方向缩放比例
	 * @param centerX	缩放中心点x坐标
	 * @param centerY	缩放中心点y坐标
	 */
	public void setScale(float xScale,float yScale,
						 float centerX,float centerY)
	{
		mXScale = xScale;
		mYScale  = yScale;
		mCenterX  = centerX;
		mCenterY  = centerY;
	}
		
	protected boolean getClikedScaleStatus()
	{		
		if(!mEnableScale)return true;	
		if( Float.compare(mXScale, 0.0f) == 0)return true;
		
		//如果在这范围内，则可以处理点击
		if(Float.compare(mXScale , 0.95f) == 1 &&
				Float.compare(mXScale , 1.1f) == -1)
		{
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 缩放图表
	 * @param canvas	画布
	 */
	private void scaleChart(Canvas canvas)
	{
		if(!mEnableScale)return;
		
		if( Float.compare(mCenterX, 0.0f) == 1 ||
				Float.compare(mCenterY, 0.0f) == 1	)
			{							
				canvas.scale(mXScale, mYScale,mCenterX,mCenterY);				
				//}else{
				//canvas.scale(mScale, mScale,plotArea.getCenterX(),plotArea.getCenterY());					
			}
	}
	
	/**
	 * 激活图表缩放(但注意，图表缩放后，如果有同时激活click事件，
	 * 	则缩放状态下，点击处理无效。
	 * 	
	 */
	public void enableScale()
	{
		mEnableScale = true;
	}
	
	/**
	 * 禁用图表缩放
	 */
	public void disableScale()
	{
		mEnableScale = false;
	}
	
	/**
	 * 返回图表缩放状态 
	 * @return 缩放状态 
	 */
	public boolean getScaleStatus()
	{
		return mEnableScale;
	}
	
	
	/**
	 * 设置手势平移模式
	 * @param mode	平移模式
	 */
	public void setPlotPanMode(XEnum.PanMode mode)
	{
		 mPlotPanMode = mode;
	}
	
	/**
	 * 返回当前图表平移模式
	 * @return 平移模式
	 */
	public XEnum.PanMode getPlotPanMode()
	{
		return mPlotPanMode;
	}
	
	/**
	 * 激活平移模式
	 */
	public void enablePanMode()
	{
		mEnablePanMode = true;		
	}
	
	/**
	 * 禁用平移模式
	 */
	public void disablePanMode()
	{
		mEnablePanMode = false;		
	}
	
	 /**
	  * 限制图表滑动范围,开启则图表不能滑动出可见范围
	  */
	 public void enabledCtlPanRange()
	 {
		 mControlPanRange = true;
	 }
	 
	 /**
	  * 不限制图表滑动范围
	  */
	 public void disabledCtlPanRange()
	 {
		 mControlPanRange = false;
	 }
	 
	 /**
	  * '是否有限制图表滑动范围
	  * @return 状态
	  */
	 public boolean getCtlPanRangeStatus()
	 {
		 return mControlPanRange;
	 }	 
	
	/**
	 * 返回当前图表的平移状态
	 * @return  平移状态
	 */
	public boolean getPanModeStatus()
	{
		return mEnablePanMode;
	}
			
	/**
	 * 返回动态图例类，当默认的图例不合需求时，可以用来应付一些特殊格式
	 * @return 动态图例
	 */
	public Legend getDyLegend()
	{
		if(null == mDyLegend)mDyLegend = new LegendRender();
		
		return mDyLegend;
	}
			
		
	/**
	 * 绘制十字交叉线
	 */
	public void showDyLine()
	{
		mDyLineVisible = true;
	}
	
	/**
	 * 不绘制十字交叉线
	 */
	public void hideDyLine()
	{
		mDyLineVisible = false;
	}
	
	/**
	 * 返回是否显示十字交叉线
	 * @return 是否显示
	 */
	public boolean getDyLineVisible()
	{
		return mDyLineVisible;
	}
	
	/**
	 * 开放十字交叉线绘制基类
	 * @return 交叉线绘制基类
	 */
	public DyLine getDyLine()
	{
		if(null == mDyLine) mDyLine = new DyLineRender();
		return mDyLine;
	}
	
	//交叉线
	private void drawDyLine(Canvas canvas)
	{
		if(!mDyLineVisible)return;		
		if(null == mDyLine)mDyLine = new DyLineRender(); 
		mDyLine.renderLine(canvas,plotArea.getLeft(),plotArea.getTop(),
								  plotArea.getRight(),plotArea.getBottom());
	}
	
	private void drawDyLegend(Canvas canvas)
	{
		//动态图例
		if(null != mDyLegend)
		{
			mDyLegend.setPlotWH(this.getWidth(), this.getHeight());
			mDyLegend.renderInfo(canvas);
		}
	}
		
	/**
	 * 用于延迟绘制
	 * @param canvas	画布
	 * @return	是否成功
	 * @throws Exception 例外
	 */
	protected boolean postRender(Canvas canvas)  throws Exception
	{
		try{	
			// 绘制图背景
			renderChartBackground(canvas);		
			
		} catch (Exception e) {
			throw e;
		}
		return true;
	}
	

	@Override
	public boolean render(Canvas canvas) throws Exception {
		// TODO Auto-generated method stubcalcPlotRange
		boolean ret = true;
		try {
			
				if (null == canvas)
						return false;
				
				
				canvas.save();
				
					//缩放图表
					scaleChart(canvas);	
					
					//绘制图表	
					ret = postRender(canvas);	
					
					//绘制边框
					renderBorder(canvas);	
															
					//动态图例
					drawDyLegend(canvas);	
					
					//十字交叉线
					drawDyLine(canvas);
										
				canvas.restore();
			
				
				return ret;					
		} catch (Exception e) {
			throw e;
		}
	}

	
	//math计算类函数 ----------------------------------------------------------------
	
	/**
	 * Java是无法精确计算小数后面的，激活后会
	 * 忽略Java数据计算时的误差。能提高绘制性能，(但饼图类图表慎用)
	 */
	public void disableHighPrecision()
    {
		 MathHelper.getInstance().disableHighPrecision();
    }
    
	/**
	 * 激活Java数据精确计算，考虑计算的误差。
	 */
    public void enabledHighPrecision()
    {
    	 MathHelper.getInstance().enabledHighPrecision();
    }
    
	/**
	 * 加法运算
	 * @param v1 参数1
	 * @param v2 参数2
	 * @return 结果
	 */
	 protected float add(float v1, float v2) 
	 {
		 return MathHelper.getInstance().add(v1, v2);
	 }
		 
	 /**
	  * 减法运算
	  * @param v1 参数1
	  * @param v2 参数2
	  * @return 运算结果
	  */
	 protected float sub(float v1, float v2) 
	 {
		 return MathHelper.getInstance().sub(v1, v2);
	 }
		 
	 /**
	  * 乘法运算
	  * @param v1 参数1
	  * @param v2 参数2
	  * @return 运算结果
	  */
	 protected float mul(float v1, float v2) 
	 {
		return MathHelper.getInstance().mul(v1, v2);
	 }
		 
	 /**
	  * 除法运算,当除不尽时，精确到小数点后10位
	  * @param v1 参数1
	  * @param v2 参数2
	  * @return 运算结果
	  */
	 protected float div(float v1, float v2)
	 {
		 return MathHelper.getInstance().div(v1, v2);
	 }
	 
	//math计算类函数 ----------------------------------------------------------------
	 
}
