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

package org.xclcharts.renderer.line;

import java.util.List;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.axis.DataAxisRender;
import org.xclcharts.renderer.plot.PlotAreaRender;

import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.util.Log;


/**
 * @ClassName PlotCustomLine   
 * @Description  处理定制线(参考线/分界线)的绘制 <br/>
 * 	
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class PlotCustomLine {
	
	private static final String TAG = "PlotCustomLine";
	
	//定制集合
	private List<CustomLineData> mCustomLineDataset;
			
	private DataAxisRender mDataAxis = null;
	private PlotAreaRender mPlotArea = null;
	private float mAxisScreenHeight = 0.0f;
	private float mAxisScreenWidth = 0.0f;
	
	//默认箭头大小
	private static final int CAPSIZE = 10;
	
	private PlotDot mDot = null;
	
			
	public PlotCustomLine()
	{	
	}		

	public void setVerticalPlot(DataAxisRender dataAxis,
								PlotAreaRender plotArea,float axisScreenHeight)
	{
		setDataAxis(dataAxis);
		setPlotArea(plotArea);
		setAxisScreenHeight(axisScreenHeight);		
	}
	
	public void setHorizontalPlot(DataAxisRender dataAxis,
								 PlotAreaRender plotArea,float axisScreenWidth)
	{
		setDataAxis(dataAxis);
		setPlotArea(plotArea);
		setAxisScreenWidth(axisScreenWidth);		
	}
			
	private boolean validateParams()
	{
		if(null == mDataAxis)
		{
			Log.e(TAG, "数据轴基类没有传过来。");
			return false;
		}
		if(null == mPlotArea)
		{
			Log.e(TAG, "绘图区基类没有传过来。");
			return false;
		}
		
		//数据集没有传过来
		if(null == mCustomLineDataset)return false;
	
		return true;
	}
	
	
	/**
	 * 用来画竖向柱形图，横向的定制线
	 * @param canvas 画布
	 * @return 是否成功
	 */
	public boolean renderVerticalCustomlinesDataAxis(Canvas canvas) {
		
		if(!validateParams())return false;
		if(0.0f == mAxisScreenHeight)
		{
			Log.e(TAG, "轴的屏幕高度值没有传过来。");
			return false;
		}		
		double axisHeight = MathHelper.getInstance().sub( 
									mDataAxis.getAxisMax(), mDataAxis.getAxisMin());
		
		for(CustomLineData line : mCustomLineDataset)
		{			
			line.getCustomLinePaint().setColor(line.getColor());
			line.getCustomLinePaint().setStrokeWidth(line.getLineStroke());		
			
			//double  postion = mAxisScreenHeight * ( 
			//					(line.getValue() - mDataAxis.getAxisMin()) /axisHeight  );
			double per =  MathHelper.getInstance().div(
								MathHelper.getInstance().sub( 
										line.getValue() , mDataAxis.getAxisMin()),
								axisHeight  );			
			float  postion = MathHelper.getInstance().mul(mAxisScreenHeight ,  (float) per); 		
			
			//float currentY = (float) (mPlotArea.getBottom() - postion); 			
			float currentY =  MathHelper.getInstance().sub(mPlotArea.getBottom(),postion);
			
			//绘制线
			if(line.isShowLine())
				DrawHelper.getInstance().drawLine(line.getLineStyle(), 
											 	mPlotArea.getLeft(), currentY,
												mPlotArea.getRight(), currentY,
												canvas, line.getCustomLinePaint(), line.getBitmap());
			
			//绘制标签和箭头
			renderCapLabelVerticalPlot(canvas,line,postion);			
		}
		return true;
	}
	
	/**
	 * 绘制标签和箭头
	 * @param canvas	画布
	 * @param line		线基类
	 * @param chartPostion	位置
	 */
	private void renderCapLabelVerticalPlot(Canvas canvas,
										CustomLineData line,float chartPostion )
	{
		
		if(line.getLabel().length()  > 0)
		{				
			float currentX = 0.0f,currentY = 0.0f;
			float capX = 0.0f;
			
			//显示在哪个高度位置
			currentY =  MathHelper.getInstance().sub(mPlotArea.getBottom() , chartPostion); 		
		
			switch (line.getLabelHorizontalPostion())
			{
			case LEFT:
				currentX =  MathHelper.getInstance().sub(mPlotArea.getLeft() ,line.getLabelOffset());	
				line.getLineLabelPaint().setTextAlign(Align.RIGHT);
				
				capX =  mPlotArea.getRight();	
				break;
			case CENTER:				
				//currentX = mPlotArea.getLeft() + 
				//			(mPlotArea.getRight() -  mPlotArea.getLeft() ) /2  - line.getLabelOffset();		
								
				float w = MathHelper.getInstance().div(
										MathHelper.getInstance().sub(mPlotArea.getRight(),mPlotArea.getLeft()),2);
				currentX = MathHelper.getInstance().add(mPlotArea.getLeft() , w);
				currentX = MathHelper.getInstance().sub(currentX ,line.getLabelOffset());										
				line.getLineLabelPaint().setTextAlign(Align.CENTER);
									
				//capX = mPlotArea.getLeft() + (mPlotArea.getRight() -  mPlotArea.getLeft() ) /2;						
				float w2 = MathHelper.getInstance().div(
							MathHelper.getInstance().sub(mPlotArea.getRight(), mPlotArea.getLeft()),2);
				capX =  MathHelper.getInstance().add(mPlotArea.getLeft(),w2);
				
				break;
			case RIGHT:
				currentX =  MathHelper.getInstance().add(mPlotArea.getRight() , line.getLabelOffset());	
				line.getLineLabelPaint().setTextAlign(Align.LEFT);		
				
				capX =  mPlotArea.getLeft() ;
				break;				
			}			
			
			//绘制箭头			
			renderLineCapVerticalPlot( canvas,  line, capX,currentY);
				
			//绘制标签
			renderLabel(canvas,line,currentX,currentY);		
		}		
	}
	
	
	private void renderLabel(Canvas canvas,
							 CustomLineData line,float currentX,float currentY){
		float txtHeight = DrawHelper.getInstance().getPaintFontHeight(
				line.getLineLabelPaint());	
		switch (line.getLabelHorizontalPostion())
		{
		case LEFT:				
			currentY += txtHeight/3;
			break;
		case CENTER:				
			if(line.isShowLine())
				currentY -= DrawHelper.getInstance().getPaintFontHeight( line.getCustomLinePaint());
															
			break;
		case RIGHT:
			currentY += txtHeight/3;
			break;				
		}		
		
		//绘制标签				
		DrawHelper.getInstance().drawRotateText(line.getLabel(), 
												currentX, currentY,
												line.getLabelRotateAngle(), 
												canvas,line.getLineLabelPaint());		
	}
		
	/**
	 * 用来画横向柱形图，竖向的定制线
	 * @param canvas 画布
	 * @return 是否成功
	 */
	public boolean renderHorizontalCustomlinesDataAxis(Canvas canvas) {
		
		if(!validateParams())return false;
		if(0.0f == mAxisScreenWidth)
		{
			Log.e(TAG, "轴的屏幕宽度值没有传过来。");
			return false;
		}		
		double axisHeight = mDataAxis.getAxisMax() - mDataAxis.getAxisMin();
		
		for(CustomLineData line : mCustomLineDataset)
		{			
			line.getCustomLinePaint().setColor(line.getColor());
			line.getCustomLinePaint().setStrokeWidth(line.getLineStroke());
			
			double  postion = mAxisScreenWidth * ( 
					(line.getValue() - mDataAxis.getAxisMin()) /axisHeight  );
			
			float currentX = (float) (mPlotArea.getLeft() + postion); 
												
			//绘制线	
			if(line.isShowLine())
				DrawHelper.getInstance().drawLine(line.getLineStyle(), 
												currentX, mPlotArea.getBottom(),
												currentX, mPlotArea.getTop(),
												canvas, line.getCustomLinePaint());
			
			//绘制标签和箭头
			renderCapLabelHorizontalPlot(canvas,line,postion);
		}		
		return true;
	}
	
	// PlotAreaRender plotArea,
	
	public boolean renderCategoryAxisCustomlines(Canvas canvas,
						float  plotScreenWidth,
						PlotAreaRender plotArea,
						double maxValue,double minValue) {
		
		setPlotArea(plotArea);
					
		for(CustomLineData line : mCustomLineDataset)
		{			
			line.getCustomLinePaint().setColor(line.getColor());
			line.getCustomLinePaint().setStrokeWidth(line.getLineStroke());
						
			float pos = MathHelper.getInstance().getLnPlotXValPosition(
					plotScreenWidth, plotArea.getLeft(), line.getValue(), maxValue, minValue);
			
			float currentX = MathHelper.getInstance().add(plotArea.getLeft() , pos);
														
			//绘制线	
			if(line.isShowLine())
				DrawHelper.getInstance().drawLine(line.getLineStyle(), 
												currentX, plotArea.getBottom(),
												currentX, plotArea.getTop(),
												canvas, line.getCustomLinePaint());
						
			//绘制标签和箭头
			renderCapLabelHorizontalPlot(canvas,line,pos);
		}		
		return true;
	}
	
	
	/**
	 * 绘制标签和箭头
	 * @param canvas	画布
	 * @param line		线基类
	 * @param chartPostion	位置
	 */
	private void renderCapLabelHorizontalPlot(Canvas canvas,
										  CustomLineData line,double chartPostion )
	{
		if(line.getLabel().length()  > 0)
		{				
			float currentX = 0.0f,currentY = 0.0f;
			float capY = 0.0f;
			
			currentX = (float) (mPlotArea.getLeft() + chartPostion); 						
			switch (line.getLabelVerticalAlign())
			{
			case TOP:
				currentY =  mPlotArea.getTop() - line.getLabelOffset();	
				
				capY = mPlotArea.getBottom();
				
				break;
			case MIDDLE:
				currentY =  mPlotArea.getBottom() - 
							(mPlotArea.getBottom() - mPlotArea.getTop()) / 2 - line.getLabelOffset();
				
				capY = mPlotArea.getBottom() - (mPlotArea.getBottom() -  mPlotArea.getTop() ) / 2 ;
				break;
			case BOTTOM:				
				currentY =  mPlotArea.getBottom() + line.getLabelOffset();		
				
				capY =  mPlotArea.getTop();
				break;				
			}			
			line.getLineLabelPaint().setTextAlign(Align.CENTER);		
			
			//绘制箭头			
			renderLineCapHorizontalPlot( canvas, line, currentX,capY);
						
			//绘制标签		
			DrawHelper.getInstance().drawRotateText(
									line.getLabel(), currentX, currentY,
									line.getLabelRotateAngle(),
									canvas,line.getLineLabelPaint());			
		}
	}
	
	//绘制箭头
	private void renderLineCapHorizontalPlot(Canvas canvas, 
								CustomLineData line, float currentX, float currentY)
	{
		float left = currentX ;
		float top   = currentY ;
		float right  = currentX ;	
		float bottom  = currentY ;
		renderLineCap( canvas,  line,  left,  top,  right, bottom);
	}
	
	
	//绘制箭头
	private void renderLineCapVerticalPlot(Canvas canvas, 
							CustomLineData line, float currentX, float currentY)
	{
		float left = currentX - CAPSIZE * 2;
		float top   = currentY - CAPSIZE * 2;
		float right  = currentX ;		
		float bottom  = currentY ;
		renderLineCap( canvas,  line,  left,  top,  right, bottom);
	}
	
	//绘制箭头
	private void renderLineCap(Canvas canvas, CustomLineData line,
								float left, float top, float right,float bottom)
	{		
		initPlotDot();
		mDot.setDotStyle(line.getCustomeLineCap());		
		PlotDotRender.getInstance().renderDot(canvas,mDot,
				left + (right - left )/2, top + (bottom - top)/2,
				line.getCustomLinePaint()); //标识图形          	
	} 
	
	private void initPlotDot()
	{
		if(null == mDot) mDot = new PlotDot();
	}

		
	/**
	 * 设置定制线值
	 * @param customLineDataSet 线数据集合
	 */
	public void setCustomLines(List<CustomLineData> customLineDataSet)
	{
		//if(null != mCustomLineDataset) mCustomLineDataset.clear();
		mCustomLineDataset = customLineDataSet;
	}
	
	
	/**
	 * 设置当前处理的数据轴
	 * @param dataAxis 数据轴
	 */
	public void setDataAxis(DataAxisRender dataAxis)
	{
		mDataAxis = dataAxis;
	}
	
	/**
	 * 设置绘图区
	 * @param plotArea 绘图区
	 */
	public void setPlotArea(PlotAreaRender plotArea)
	{
		mPlotArea = plotArea;
	}
	
	/**
	 * 设置轴的屏幕高度值
	 * @param height 高度
	 */
	public void setAxisScreenHeight(float height)
	{
		mAxisScreenHeight = height;
	}
	
	/**
	 * 设置轴的屏幕宽度值
	 * @param width 宽度
	 */
	public void setAxisScreenWidth(float width)
	{
		mAxisScreenWidth = width;
	}
	
}
