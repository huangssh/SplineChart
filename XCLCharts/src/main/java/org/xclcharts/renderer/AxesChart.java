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
package org.xclcharts.renderer;


import java.util.ArrayList;

import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.renderer.axis.CategoryAxis;
import org.xclcharts.renderer.axis.CategoryAxisRender;
import org.xclcharts.renderer.axis.DataAxis;
import org.xclcharts.renderer.axis.DataAxisRender;
import org.xclcharts.renderer.info.PlotAxisTick;
import org.xclcharts.renderer.plot.AxisTitle;
import org.xclcharts.renderer.plot.AxisTitleRender;

import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.util.Log;

/**
 * @ClassName AxesChart
 * @Description 所有用到坐标类的图表的基类,主要用于定义和绘制坐标轴
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class AxesChart extends EventChart {
	
	//private static final String TAG = "AxisChart";
		
	//数据轴
	protected DataAxisRender dataAxis  = null;
	//标签轴
	protected CategoryAxisRender categoryAxis  = null;	
	//轴标题类
	private AxisTitleRender axisTitle = null;
	
	//格式化柱形顶上或线交叉点的标签
	private IFormatterDoubleCallBack mItemLabelFormatter;
	
	// 确定是竖向柱形图(默认)还是横向
	protected XEnum.Direction mDirection = XEnum.Direction.VERTICAL;
	
	//Pan模式下移动距离
	protected float mMoveX = 0.0f;
	protected float mMoveY = 0.0f;
	
	//数据轴显示在左边还是右边
	private XEnum.AxisLocation mDataAxisLocation = 
									XEnum.AxisLocation.LEFT;
	
	private XEnum.AxisLocation mCategoryAxisLocation = 
									XEnum.AxisLocation.BOTTOM;
	
	//是否将轴封闭
	private boolean mAxesClosed = false;	
		
	// 对于轴标签滑动时的显示处理并不是很好，不过已经没精力重构了。
	// 目前可用下面的方式来限定:
	//     可通过指定下面两个参数值来限定x/y轴标签可移出的范围
	//     但注意这两个参数是同时限制上下或左右。
	private float myMargin = -10.0f;
	private float mxMargin = -25.f;// 25.0f; //散点和气泡要注意下
	
	//轴刻度的位置信息
	protected ArrayList<PlotAxisTick> mLstDataTick = null;
	protected ArrayList<PlotAxisTick> mLstCateTick = null;
	
	private ClipExt mClipExt = null;
		
	
	public AxesChart() {
		// TODO Auto-generated constructor stub		
		
		if(null == mLstDataTick)mLstDataTick = new  ArrayList<PlotAxisTick>();
		if(null == mLstCateTick)mLstCateTick = new  ArrayList<PlotAxisTick>();
		
		initChart();		
	}
		
	/**
	 * 初始化设置
	 */
	private void initChart()
	{						
		//数据轴
		if(null == dataAxis)initDataAxis(); 
		
		//标签轴
		if(null == categoryAxis)initCategoryAxis();
						
		//初始化图例
		if(null != plotLegend)
		{
			plotLegend.show();
			plotLegend.setType(XEnum.LegendType.ROW);
			plotLegend.setHorizontalAlign(XEnum.HorizontalAlign.LEFT);
			plotLegend.setVerticalAlign(XEnum.VerticalAlign.TOP);
			plotLegend.hideBox();
		}		
	}		

	 /**
	  * 开放数据轴绘制类
	  * @return 数据轴绘制类
	  */
	public DataAxis getDataAxis() 
	{
		//数据轴
		initDataAxis(); 
		return dataAxis;
	}

	/**
	 * 开放标签轴绘制类
	 * @return 标签轴绘制类
	 */
	public CategoryAxis getCategoryAxis() 
	{
		//标签轴
		initCategoryAxis();
		return categoryAxis;
	}
			
	private void initCategoryAxis()
	{
		if(null == categoryAxis)categoryAxis  = new CategoryAxisRender();
	}
	
	public void initDataAxis() 
	{
		if(null == dataAxis)dataAxis  = new DataAxisRender();	
	}
	
	protected void drawCategoryAxisLabels(Canvas canvas,
										  ArrayList<PlotAxisTick> lstLabels)
	{
		
		if(null == lstLabels) return ;
		
		boolean showTicks = true;
		for(int i=0;i<lstLabels.size();i++) 
		{
			PlotAxisTick t = lstLabels.get(i);
			switch(mCategoryAxisLocation)
			{				 
				case LEFT: //Y
				case RIGHT:			
				case VERTICAL_CENTER:
					if( !t.isShowTickMarks() || !isDrawYAxisTickMarks(t.Y,mMoveY)) showTicks = false;					
					  categoryAxis.renderAxisHorizontalTick(
							  this.getLeft(),
							  this.getPlotArea().getLeft(),
							  canvas,t.X,t.Y, 
							  t.Label,t.labelX,t.labelY,
							  showTicks);					
					break;							
				case TOP: //X
				case BOTTOM:		
				case HORIZONTAL_CENTER:
					XEnum.ODD_EVEN oe = (i%2 != 0)?XEnum.ODD_EVEN.ODD:XEnum.ODD_EVEN.EVEN;
				
					if( !t.isShowTickMarks() || !isDrawXAxisTickMarks(t.X,mMoveX) ) showTicks = false;								
						categoryAxis.renderAxisVerticalTick(
								canvas,t.X,t.Y, 
								t.Label,t.labelX,t.labelY,
								showTicks,oe);	
					break;			
			
			} //switch end
			showTicks = true;
		}
	}
	
	protected void drawDataAxisLabels(Canvas canvas,
									 ArrayList<PlotAxisTick> lstLabels)
	{
		
		if(null == lstLabels) return ;		
		
		for(int i=0;i<lstLabels.size();i++) 
		{
			PlotAxisTick t = lstLabels.get(i);
			XEnum.ODD_EVEN oe = (i%2 != 0)?XEnum.ODD_EVEN.ODD: XEnum.ODD_EVEN.EVEN;
	
			dataAxis.setAxisTickCurrentID(t.ID);
			
			switch(mDataAxisLocation)
			{				 
				case LEFT: //Y
				case RIGHT:			
				case VERTICAL_CENTER:
						dataAxis.renderAxisHorizontalTick(this.getLeft(),this.getPlotArea().getLeft(),
															canvas,t.X,t.Y, t.Label,
															isDrawYAxisTickMarks(t.Y,mMoveY));										
					break;							
				case TOP: //X
				case BOTTOM:	
				case HORIZONTAL_CENTER:
						dataAxis.renderAxisVerticalTick(canvas,t.X,t.Y, t.Label,
															isDrawXAxisTickMarks(t.X,mMoveX),oe);										
					break;			
			} //switch end
		}
	}

	/**
	 * 开放轴标题绘制类
	 * @return 图例绘制类
	 */
	public AxisTitle getAxisTitle() 
	{		
		//轴标题
		if(null == axisTitle)axisTitle = new AxisTitleRender();					
		return axisTitle;
	}

	/**
	 * 轴所占的屏幕宽度
	 * @return  屏幕宽度
	 */
	protected float getAxisScreenWidth()
	{
		if(null == plotArea)return 0.0f;
		return(Math.abs(plotArea.getRight() - plotArea.getLeft()));
	}
	
	protected float getPlotScreenWidth()
	{
		if(null == plotArea)return 0.0f;
		return(Math.abs(plotArea.getPlotRight() - plotArea.getPlotLeft()));
	}
	
	protected float getPlotScreenHeight()
	{
		if(null == plotArea)return 0.0f;
		return( Math.abs(plotArea.getPlotBottom() - plotArea.getPlotTop()));
	}
	

			
	/**
	 * 轴所占的屏幕高度
	 * @return 屏幕高度
	 */
	protected float getAxisScreenHeight()
	{
		if(null == plotArea)return 0.0f;
		return( Math.abs(plotArea.getBottom() - plotArea.getTop()));
	}
	
	/**
	 * 	竖向柱形图
	 *  Y轴的屏幕高度/数据轴的刻度标记总数 = 步长
	 * @return Y轴步长
	 */
	protected float getVerticalYSteps(int tickCount) {		
		return (div(getPlotScreenHeight(),tickCount)); 
	}
	
	/**
	 * 竖向柱形图
	 * 得到X轴的步长
	 * X轴的屏幕宽度 / 刻度标记总数  = 步长
	 * @param num 刻度标记总数 
	 * @return X轴步长
	 */
	public float getVerticalXSteps(int tickCount) {
		//柱形图为了让柱形显示在tick的中间，会多出一个步长即(dataSet.size()+1)	
		return  (div(getPlotScreenWidth() ,tickCount)); 
	}
		
	/**
	 * 设置标签显示格式
	 * 
	 * @param callBack
	 *            回调函数
	 */
	public void setItemLabelFormatter(IFormatterDoubleCallBack callBack) {
		this.mItemLabelFormatter = callBack;
	}

	/**
	 * 返回标签显示格式
	 * 
	 * @param value 传入当前值
	 * @return 显示格式
	 */
	protected String getFormatterItemLabel(double value) {
		String itemLabel = "";
		try {
			itemLabel = mItemLabelFormatter.doubleFormatter(value);
		} catch (Exception ex) {
			itemLabel = Double.toString(value);
			// DecimalFormat df=new DecimalFormat("#0");
			// itemLabel = df.format(value).toString();
		}
		return itemLabel;
	}
		
	
	/**
	 * 检查Y轴的刻度是否显示 
	 * @param currentY	y坐标
	 * @param moveY	y坐标平移值
	 * @return	是否绘制
	 */
	
	protected boolean isDrawYAxisTickMarks(float currentY,float moveY)
	{				
		if(Float.compare(currentY , plotArea.getTop() - moveY) == -1 || 
				Float.compare(currentY, plotArea.getBottom()  - moveY) == 1 )
		{
			return false;
		}
		return true;
	}
	
			
	/**
	 *  检查X轴的刻度是否显示 
	 * @param currentX	x坐标
	 * @param moveX	x坐标平移值
	 * @return 是否绘制
	 */
	
	protected boolean isDrawXAxisTickMarks(float currentX,float moveX)
	{
		if(Float.compare(currentX + moveX , plotArea.getLeft() ) == -1 ) return false;
		if(Float.compare(currentX + moveX , plotArea.getRight() ) ==  1 ) return false;
		return true;
	}
	
	//横向网格线
	protected void drawHorizontalGridLines(Canvas canvas,float plotLeft,float plotRight,
			int tickID ,int tickCount,
			float YSteps,float currentY)
	{
		if(tickID < 0 ) return; //tickID <= 0
		
		// 从左到右的横向网格线
		if(tickID > 0)
		{					
			if (tickID % 2 != 0) {
				plotGrid.renderOddRowsFill(canvas,plotLeft, add(currentY , YSteps),plotRight, currentY);
			} else {
				plotGrid.renderEvenRowsFill(canvas,plotLeft,add(currentY , YSteps),plotRight, currentY);
			}
		}
	
		if (tickID >= 0 && tickID < tickCount){
			plotGrid.setPrimaryTickLine(dataAxis.isPrimaryTick(tickID));
			plotGrid.renderGridLinesHorizontal(canvas,plotLeft, currentY,plotRight, currentY);
		}		
	}
	
	//绘制竖向网格线
	protected void drawVerticalGridLines(Canvas canvas,float plotTop,float plotBottom,
			int tickID ,int tickCount,
			float XSteps,float currentX)
	{
		// 绘制竖向网格线
		if (plotGrid.isShowVerticalLines()) 
		{
			//if (i > 0 && i + 1 < tickCount) //line
				plotGrid.renderGridLinesVertical(canvas,currentX,
												 plotBottom, currentX,plotTop);
		}
	}
	
		
	/**
	 * 设置数据轴显示在哪边,默认是左边
	 * @param position 显示位置
	 */
	public void setDataAxisLocation(XEnum.AxisLocation location)
	{
		mDataAxisLocation = location;	
	}
	
	/**
	 * 返回数据轴显示在哪边
	 * @return 显示位置
	 */
	public XEnum.AxisLocation getDataAxisLocation()
	{
		return mDataAxisLocation;
	}
	
	/**
	 * 设置分类轴显示在哪边,默认是底部
	 * @param position 显示位置
	 */
	public void setCategoryAxisLocation( XEnum.AxisLocation location)
	{
		mCategoryAxisLocation = location;	
	}
	
	/**
	 * 返回分类轴显示在哪边
	 * @return 显示位置
	 */
	public XEnum.AxisLocation getCategoryAxisLocation()
	{
		return mCategoryAxisLocation;
	}
	
	
	protected float getAxisXPos(XEnum.AxisLocation location)
	{
		if( XEnum.AxisLocation.RIGHT  == location)
		{    //显示在右边
			return plotArea.getRight();
		}else if(XEnum.AxisLocation.LEFT  == location){
			//显示在左边
			return plotArea.getLeft();
		}else if(XEnum.AxisLocation.VERTICAL_CENTER  == location){
			//显示在中间
			return plotArea.getCenterX();
		}		
		return 0;
	}
	
	protected float getAxisYPos(XEnum.AxisLocation location)
	{
		if(XEnum.AxisLocation.TOP == location)
		{
			return plotArea.getTop();
		}else if(XEnum.AxisLocation.BOTTOM == location){
			return plotArea.getBottom();
		}else if(XEnum.AxisLocation.HORIZONTAL_CENTER == location){
			return plotArea.getCenterY();
		}
		return 0;
	}
	
	protected void categoryAxisDefaultSetting()
	{		
		if(null == mDataAxisLocation) return;
		
		if(null == categoryAxis) return;
		if(!categoryAxis.isShow())return;
		
		if(null != mDirection)
		{
			switch (mDirection) {
				case HORIZONTAL: {		
					this.setCategoryAxisLocation(XEnum.AxisLocation.LEFT);				
					break;
				}
				case VERTICAL: {
					this.setCategoryAxisLocation(XEnum.AxisLocation.BOTTOM);			
					break;
				}
			}
		}
		
		if(XEnum.AxisLocation.LEFT == mDataAxisLocation)
		{
			categoryAxis.setHorizontalTickAlign(Align.CENTER);
		}
		
		categoryAxis.getAxisPaint().setStrokeWidth(2);
		categoryAxis.getTickMarksPaint().setStrokeWidth(2);
	}
	
	protected void dataAxisDefaultSetting()
	{		
		if(null == mDataAxisLocation) return;
		
		if(null == dataAxis) return;
		if(!dataAxis.isShow())return;
		
		if(null != mDirection)
		{
			switch (mDirection) {
				case HORIZONTAL: {		
					this.setDataAxisLocation(XEnum.AxisLocation.BOTTOM);		
					break;
				}
				case VERTICAL: {
					this.setDataAxisLocation(XEnum.AxisLocation.LEFT);					
					break;
				}
			}
		}
				
		if(XEnum.AxisLocation.LEFT == mDataAxisLocation)
		{
			dataAxis.setHorizontalTickAlign(Align.LEFT);	
		}else{
			dataAxis.setHorizontalTickAlign(Align.RIGHT);
			if(dataAxis.isShowAxisLabels())
					dataAxis.getTickLabelPaint().setTextAlign(Align.LEFT);	
		}
			
		if(dataAxis.isShowAxisLine())
				dataAxis.getAxisPaint().setStrokeWidth(2);
		if(dataAxis.isShowTickMarks())
				dataAxis.getTickMarksPaint().setStrokeWidth(2);
		
	}
	
	/**
	 * 封闭轴
	 * @param status 状态
	 */
	public void setAxesClosed(boolean status)
	{
		mAxesClosed = status;
	}
	
	/**
	 * 是否封闭轴
	 * @return 状态
	 */
	public boolean getAxesClosedStatus()
	{
		return mAxesClosed;
	}
	////////////////////////////
		
	protected void initMoveXY()
	{
		mMoveX = mMoveY = 0.0f;  	
		switch(this.getPlotPanMode())
		{
		case HORIZONTAL:
			mMoveX = mTranslateXY[0]; 			
			break;
		case VERTICAL:
			mMoveY = mTranslateXY[1]; 					
			break;
		default:
			mMoveX = mTranslateXY[0]; 
			mMoveY = mTranslateXY[1]; 
			break;
		}
	}
	
	
	protected void drawClipCategoryAxisGridlines(Canvas canvas)
	{		
	}
	
	protected void drawClipDataAxisGridlines(Canvas canvas)
	{
	}
	
	protected void drawClipPlot(Canvas canvas)
	{		
	}
	
	protected void drawClipAxisClosed(Canvas canvas)
	{
		if(!getAxesClosedStatus())return;
			
		float plotLeft = plotArea.getLeft();
		float plotTop = plotArea.getTop();
		float plotRight = plotArea.getRight();
		float plotBottom = plotArea.getBottom();
		
		switch(mDataAxisLocation)
		{
		case LEFT:
		case RIGHT:		
		case VERTICAL_CENTER:			
			dataAxis.renderAxisLine(canvas,plotLeft, plotTop, plotLeft, plotBottom);	
			dataAxis.renderAxisLine(canvas,plotRight, plotTop, plotRight, plotBottom);				
			break;								
		case TOP:
		case BOTTOM:		
		case HORIZONTAL_CENTER:						
			dataAxis.renderAxisLine(canvas,plotLeft, plotTop, plotRight, plotTop); 
			dataAxis.renderAxisLine(canvas,plotLeft, plotBottom, plotRight, plotBottom);			
			break;
		}		
		
		switch(mCategoryAxisLocation)
		{
		case LEFT:
		case RIGHT:
		case VERTICAL_CENTER:
				categoryAxis.renderAxisLine(canvas,plotLeft, plotBottom, plotLeft, plotTop);
				categoryAxis.renderAxisLine(canvas,plotRight, plotBottom, plotRight, plotTop);
			break;					
		case TOP:
		case BOTTOM:	
		case HORIZONTAL_CENTER:
				categoryAxis.renderAxisLine(canvas,plotLeft, plotTop, plotRight, plotTop);
				categoryAxis.renderAxisLine(canvas,plotLeft, plotBottom, plotRight,plotBottom);		
			break;		
		}		
	}
	
	protected void drawClipDataAxisLine(Canvas canvas)
	{
		float plotLeft = plotArea.getLeft();
		float plotTop = plotArea.getTop();
		float plotRight = plotArea.getRight();
		float plotBottom = plotArea.getBottom();
	
		float vcX = plotLeft + ( plotRight - plotLeft )/2;	
		float hcY = plotTop + ( plotBottom - plotTop )/2;	
		
		switch(mDataAxisLocation)
		{
		case LEFT:
			dataAxis.renderAxis(canvas,plotLeft, plotBottom, plotLeft, plotTop);
			break;
		case RIGHT:
			dataAxis.renderAxis(canvas,plotRight, plotTop, plotRight, plotBottom); 
			break;			
		case VERTICAL_CENTER:			
			dataAxis.renderAxis(canvas,vcX, plotTop, vcX, plotBottom); 		
			break;								
		case TOP:
			dataAxis.renderAxis(canvas,plotLeft, plotTop, plotRight, plotTop); 			
			break;
		case BOTTOM:
			dataAxis.renderAxis(canvas,plotLeft, plotBottom, plotRight, plotBottom); 
			break;		
		case HORIZONTAL_CENTER:								
			dataAxis.renderAxis(canvas,plotLeft, hcY, plotRight, hcY); 
			break;
		}	
	}
	
	protected void drawClipCategoryAxisLine(Canvas canvas)
	{		
		float plotLeft = plotArea.getLeft();
		float plotTop = plotArea.getTop();
		float plotRight = plotArea.getRight();
		float plotBottom = plotArea.getBottom();
	
		float vcX = plotLeft + ( plotRight - plotLeft )/2;	
		float hcY = plotTop + ( plotBottom - plotTop )/2;
		
		switch(mCategoryAxisLocation)
		{
		case LEFT:
			categoryAxis.renderAxis(canvas,plotLeft, plotBottom, plotLeft, plotTop); 		
			break;
		case RIGHT:
			categoryAxis.renderAxis(canvas,plotRight, plotTop, plotRight, plotBottom);		
			break;
		case VERTICAL_CENTER:
			categoryAxis.renderAxis(canvas,vcX, plotTop, vcX, plotBottom); 
			break;					
		case TOP:
			categoryAxis.renderAxis(canvas,plotLeft, plotTop, plotRight, plotTop); 
			break;
		case BOTTOM:
			categoryAxis.renderAxis(canvas,plotLeft, plotBottom, plotRight, plotBottom); 
			break;		
		case HORIZONTAL_CENTER:
			categoryAxis.renderAxis(canvas,plotLeft, hcY, plotRight, hcY);
			break;		
		}
	}
		
	protected void drawClipAxisLine(Canvas canvas)
	{		
		drawClipDataAxisLine(canvas);
		drawClipCategoryAxisLine(canvas);
	}
		
	protected void drawClipDataAxisTickMarks(Canvas canvas)
	{
		drawDataAxisLabels(canvas,mLstDataTick);	
		mLstDataTick.clear();
	}
	
	protected void drawClipCategoryAxisTickMarks(Canvas canvas)
	{
		drawCategoryAxisLabels(canvas,mLstCateTick);	
		mLstCateTick.clear();
	}
	
	protected void drawClipLegend(Canvas canvas)
	{
		
	}
	
	protected boolean drawFixedPlot(Canvas canvas)
	{	
		this.mMoveX = this.mMoveY = 0.0f;
		
		//绘制Y轴tick和marks	
		drawClipDataAxisGridlines(canvas);	
				
		//绘制X轴tick和marks	
		drawClipCategoryAxisGridlines(canvas);
		
		//绘图
		drawClipPlot(canvas);
		
		//轴 线
		drawClipAxisClosed(canvas);
		drawClipAxisLine(canvas);	
		
			
		//轴刻度
		drawClipDataAxisTickMarks(canvas);	
		drawClipCategoryAxisTickMarks(canvas);
				
		//图例
		drawClipLegend(canvas);		
		return true;
	 }
	
	
	/**
	 * X方向的轴刻度平移扩展间距,即控制刻度标签在移绘图区后可多显示范围。
	 * @param margin 间距
	 */
	public void setXTickMarksOffsetMargin(float margin)
	{
		mxMargin = margin;
	}
	
	/**
	 * Y方向的轴刻度平移扩展间距,即控制刻度标签在移绘图区后可多显示范围。
	 * @param margin 间距
	 */
	public void setYTickMarksOffsetMargin(float margin)
	{
		myMargin = margin;
	}
	
		
	protected float getClipYMargin()
	{	
		return (this.myMargin + this.getBorderWidth());
	}
	
	protected float getClipXMargin()
	{
		return (this.mxMargin + this.getBorderWidth());	
	}
	
	
	public ClipExt getClipExt()
	{
		if(null == mClipExt) mClipExt = new ClipExt();
		return mClipExt;
	}
	
	
	protected boolean drawClipVerticalPlot(Canvas canvas)
	{				
		//显示绘图区rect
		float offsetX = mTranslateXY[0]; 
		float offsetY = mTranslateXY[1];  
		initMoveXY();
		
		float yMargin = getClipYMargin(); 
		float xMargin = getClipXMargin();
		float gWidth = 0.0f;	
		
		drawClipAxisClosed(canvas);

		//设置图显示范围
		canvas.save();
		canvas.clipRect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
				
				if( XEnum.PanMode.VERTICAL == this.getPlotPanMode()
						|| XEnum.PanMode.FREE == this.getPlotPanMode() )
				{			
					if(getPlotGrid().isShowVerticalLines())
						gWidth = this.getPlotGrid().getVerticalLinePaint().getStrokeWidth();
					//绘制Y轴tick和marks			
					canvas.save();																	
					canvas.clipRect(plotArea.getLeft() - gWidth , plotArea.getTop() - gWidth, 
									plotArea.getRight() + gWidth, plotArea.getBottom() + gWidth);					
							canvas.translate(0 , offsetY );			
							
							drawClipDataAxisGridlines(canvas);					
					canvas.restore();	
				}else{					
					drawClipDataAxisGridlines(canvas);	
				}
		
				if( XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
						|| XEnum.PanMode.FREE == this.getPlotPanMode() )
				{	
					if(getPlotGrid().isShowHorizontalLines())
							gWidth = this.getPlotGrid().getHorizontalLinePaint().getStrokeWidth();
					//绘制X轴tick和marks			
					canvas.save();		
					
						canvas.clipRect(plotArea.getLeft() - gWidth , plotArea.getTop() - gWidth, 
										plotArea.getRight() + gWidth, plotArea.getBottom() + gWidth);
							canvas.translate(offsetX,0);
							
							drawClipCategoryAxisGridlines(canvas);
					canvas.restore();
				}else{
					drawClipCategoryAxisGridlines(canvas);
				}
				
					//设置绘图区显示范围
					canvas.save();		
										
					getClipExt().calc(getType());					
					canvas.clipRect(plotArea.getLeft()- getClipExt().getExtLeft() , 
									plotArea.getTop() - getClipExt().getExtTop(), 
									plotArea.getRight() + getClipExt().getExtRight(), 
									plotArea.getBottom() + getClipExt().getExtBottom() );
										
					//canvas.clipRect(plotArea.getLeft()+ 0.5f , plotArea.getTop() + 0.5f, 
					//		plotArea.getRight() + 0.5f, plotArea.getBottom() + 0.5f );
								
							canvas.save();					
							canvas.translate(mMoveX, mMoveY);
							//绘图
							drawClipPlot(canvas);
							
							canvas.restore();
					canvas.restore();	
								
		//还原绘图区绘制
		canvas.restore(); //clip	
			
		//轴 线		
		drawClipAxisLine(canvas);				
		/////////////////////////////////////////
	
		//轴刻度
		if( XEnum.PanMode.VERTICAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{			
			//绘制Y轴tick和marks			
			canvas.save();											
				canvas.clipRect(this.getLeft() ,this.getTop() + yMargin,   
								this.getRight(),this.getBottom() - yMargin);
					canvas.translate(0 , offsetY );					
					drawClipDataAxisTickMarks(canvas);	
			canvas.restore();	
		}else{			
			drawClipDataAxisTickMarks(canvas);	
		}
	
					
		if( XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{				
			//绘制X轴tick和marks			
			canvas.save();			
			
				canvas.clipRect(this.getLeft() + xMargin, this.getTop(),  
								this.getRight() - xMargin,this.getBottom()); //this.getRight() + xMargin
			
					canvas.translate(offsetX,0);
					drawClipCategoryAxisTickMarks(canvas);	
			canvas.restore();
		}else{
			drawClipCategoryAxisTickMarks(canvas);
		}
		
		/////////////////////////////////////////
			
		//图例
		drawClipLegend(canvas);
		return true;
	 }
	
	/////////////////////////  
	protected boolean drawClipHorizontalPlot(Canvas canvas)
	 {		
		//显示绘图区rect
		float offsetX = mTranslateXY[0]; 
		float offsetY = mTranslateXY[1]; 					
		initMoveXY();		
				
		float yMargin = getClipYMargin();
		float xMargin = getClipXMargin();
		float gWidth = 0.0f;
		
		drawClipAxisClosed(canvas);
	
		//设置图显示范围
		canvas.save();				
		canvas.clipRect(this.getLeft() , this.getTop() , this.getRight(), this.getBottom());		
							
		if( XEnum.PanMode.VERTICAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{									
			if(getPlotGrid().isShowVerticalLines())
				gWidth = this.getPlotGrid().getVerticalLinePaint().getStrokeWidth();
			//绘制Y轴tick和marks			
			canvas.save();					
			canvas.clipRect(plotArea.getLeft() - gWidth , plotArea.getTop() - gWidth, 
					plotArea.getRight() + gWidth, plotArea.getBottom() + gWidth);
					canvas.translate(0 , offsetY );					
					drawClipCategoryAxisGridlines(canvas);		
			canvas.restore();	
		}else{
			drawClipCategoryAxisGridlines(canvas);
		}
		
		if( XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{								
			
			if(getPlotGrid().isShowHorizontalLines())
				gWidth = this.getPlotGrid().getHorizontalLinePaint().getStrokeWidth();
			
			//绘制X轴tick和marks			
			canvas.save();		
			
			
			canvas.clipRect(plotArea.getLeft() - gWidth , plotArea.getTop() - gWidth, 
					plotArea.getRight() + gWidth, plotArea.getBottom() + gWidth);
					canvas.translate(offsetX,0);
					drawClipDataAxisGridlines(canvas);						
			canvas.restore();	
		}else{
			drawClipDataAxisGridlines(canvas);			
		}
		//////////////////////////////////////////////////
		
			//////////////////////////////////////////////////								
			//设置绘图区显示范围
			canvas.save();
			
			getClipExt().calc(getType());
			canvas.clipRect(plotArea.getLeft()- getClipExt().getExtLeft() , 
							plotArea.getTop() - getClipExt().getExtTop(), 
							plotArea.getRight() + getClipExt().getExtRight(), 
							plotArea.getBottom() + getClipExt().getExtBottom() );
			
			//canvas.clipRect(plotArea.getLeft() , plotArea.getTop() ,
			//				this.getRight(), plotArea.getBottom());			
					canvas.save();
					canvas.translate(mMoveX, mMoveY);	
					//绘图
					drawClipPlot(canvas);
					
					canvas.restore();
			canvas.restore();
		
			
		//还原绘图区绘制
		canvas.restore(); //clip							
		//////////////////////////////////////////////////			
		//轴线		
		drawClipAxisLine(canvas);
					
		if( XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{																	
			//绘制X轴tick和marks			
			canvas.save();	
			     //放开，排除掉border的宽度
				canvas.clipRect(this.getLeft() + xMargin , this.getTop(), 
								this.getRight() - xMargin, this.getBottom());			
					canvas.translate(offsetX,0);
					drawClipDataAxisTickMarks(canvas);
			canvas.restore();	
		}else{
			drawClipDataAxisTickMarks(canvas);
		}
								
		if( XEnum.PanMode.VERTICAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{													
			//绘制Y轴tick和marks			
			canvas.save();													
					canvas.clipRect(this.getLeft() , this.getTop() + yMargin, 
									this.getRight(), this.getBottom() - yMargin); 		
					canvas.translate(0 , offsetY );										
					drawClipCategoryAxisTickMarks(canvas);					
			canvas.restore();	
		}else{	
			drawClipCategoryAxisTickMarks(canvas);
		}		
		//////////////////////////////////////////////////
		
		//图例
		drawClipLegend(canvas);	
		return true;
	 }
	/////////////////////////

	@Override
	protected boolean postRender(Canvas canvas) throws Exception
	{		
		try{			
			super.postRender(canvas);				
			
			boolean ret = true;
			
			//计算主图表区范围
			 calcPlotRange();
			//画Plot Area背景			
			 plotArea.render(canvas);							 			
			
			//绘制图表
			if(getPanModeStatus())
			{				 
				switch (mDirection) 
				{
					case HORIZONTAL: 										
						ret = drawClipHorizontalPlot(canvas);
						break;
					case VERTICAL: 
						ret = drawClipVerticalPlot(canvas);				
						break;
				}	
			}else{
				ret = drawFixedPlot(canvas);
			}						 
			if(!ret) return ret;
			
			//绘制标题
			 renderTitle(canvas);
			//绘制轴标题
			if(null != axisTitle)
			{
				axisTitle.setRange(this);
				axisTitle.render(canvas);
			}			
			
			//显示焦点
			renderFocusShape(canvas);
			//响应提示
			renderToolTip(canvas);
											
			return ret;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	public class ClipExt {
		
		//用于扩展clip绘图区范围				
		private float mClipExtLeft = -1f;
		private float mClipExtTop = -1f;
		private float mClipExtRight = -1f;
		private float mClipExtBottom = -1f;
				
		private float clipExtLeft = 0.5f;
		private float clipExtTop = 0.5f;
		private float clipExtRight = 0.5f;
		private float clipExtBottom = 0.5f;
		
		public ClipExt(){}
		
		/**
		 * 指定绘图区clip时向左扩展范围
		 * @param value 范围值
		 */
		public void setExtLeft(float value)
		{
			mClipExtLeft = value;
		}

		/**
		 * 指定绘图区clip时向上扩展范围
		 * @param value 范围值
		 */
		public void setExtTop(float value)
		{
			mClipExtTop = value;
		}

		/**
		 * 指定绘图区clip时向右扩展范围
		 * @param value 范围值
		 */
		public void setExtRight(float value)
		{
			mClipExtRight = value;
		}

		/**
		 * 指定绘图区clip时向下扩展范围
		 * @param value 范围值
		 */
		public void setExtBottom(float value)
		{
			mClipExtBottom = value;
		}
		
		/**
		 * 用于计算实际扩展值
		 * @param type 图类型
		 */
		public void calc(XEnum.ChartType type)
		{
			switch(type)
			{
				case LINE:
				case SPLINE:
				case AREA:							
					if(Float.compare(mClipExtLeft, -1f) == 0)
					{
						clipExtLeft = 10.f;								
					}else{
						clipExtLeft = mClipExtLeft;
					}
					
					if(Float.compare(mClipExtTop, -1f) == 0)
					{
						clipExtTop = 0.5f;								
					}else{
						clipExtTop = mClipExtTop;
					}
					
					if(Float.compare(mClipExtRight, -1f) == 0)
					{
						clipExtRight = 10.f;								
					}else{
						clipExtRight = mClipExtRight;
					}
					
					if(Float.compare(mClipExtBottom, -1f) == 0)
					{
						clipExtBottom = 10.f;								
					}else{
						clipExtBottom = mClipExtBottom;
					}
					break;
				default:
					break;
			}
		}
		
		/**
		 * 返回绘图区clip时实际向左扩展范围
		 * @return 扩展值
		 */
		public float getExtLeft()
		{
			return clipExtLeft;
		}

		/**
		 * 返回绘图区clip时实际向上扩展范围
		 * @return 扩展值
		 */
		public float getExtTop()
		{
			return clipExtTop;
		}

		/**
		 * 返回绘图区clip时实际向右扩展范围
		 * @return 扩展值
		 */
		public float getExtRight()
		{
			return clipExtRight;
		}

		/**
		 * 返回绘图区clip时实际向下扩展范围
		 * @return 扩展值
		 */
		public float getExtBottom()
		{
			return clipExtBottom;
		}
				
	}
		
}
