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
 * @version 1.9
 */
package org.xclcharts.renderer.plot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.xclcharts.chart.ArcLineData;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.LnData;
import org.xclcharts.common.DrawHelper;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.line.PlotDot;
import org.xclcharts.renderer.line.PlotDotRender;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * @ClassName PlotLegendRender
 * @Description 用于绘制图表的图例 
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *
 */
public class PlotLegendRender extends PlotLegend{
	
	//private static final String TAG = "PlotLegendRender";
	
	private PlotArea mPlotArea = null;
	private XChart mXChart = null;

	private float mKeyLabelX = 0.0f;
	private float mKeyLabelY = 0.0f;

	/////////////////////////////////////
	
	private ArrayList<PlotDot> mLstDotStyle = null;
	private ArrayList<String> mLstKey = null;
	private ArrayList<Integer> mLstColor = null;
	
	private float mRectWidth = 0.0f;
	private float mRectHeight = 0.0f;
	
	LinkedHashMap<Integer,Integer> mMapID = new LinkedHashMap<Integer,Integer>();
	
	private boolean mIsLnChart = false;
	private Paint mPaintLine = null;
	
	enum EnumChartType{AXIS,CIR,LN,RD};	
	EnumChartType mType = EnumChartType.AXIS;
	
	//此处的5.f相当于this.getBox().getLinePaint()的高度	
	private final int BOX_LINE_SIZE = 5;	
	
	///////////////////////////////////
	public PlotLegendRender()
	{
	}
		
	public PlotLegendRender(XChart xChart)
	{
		mXChart = xChart;		
	}
		
	public void setXChart(XChart xChart)
	{
		mXChart = xChart;
	}	
	
	private void initEnv()
	{
		mKeyLabelX = mKeyLabelY = 0.0f;
		mRectWidth = mRectHeight = 0.0f;			
	}
					
	private float getLabelTextWidth(String key)
	{
		return  DrawHelper.getInstance().getTextWidth(getPaint(),key );
	}
	
	private float getLabelTextHeight()
	{
		return DrawHelper.getInstance().getPaintFontHeight(getPaint());
	}		
	
	///////////////////////////////
	
	// 1. 转成arraylist,convertArray
	// 2. calcRect
	// 3. calcStartXY
	// 4. drawLegend
	
	public boolean renderBarKey(Canvas canvas,List<BarData> dataSet) {
		if (isShow() == false) return false;	
		refreshLst();
		convertArrayBarKey(dataSet);
		render(canvas);
		
		return true;
	}
	
	public void renderLineKey(Canvas canvas, List<LnData> dataSet) {	
		if (isShow() == false) return;	
		
		setLnChartStatus();
		refreshLst();
		convertArrayLineKey(dataSet);
		render(canvas);
	}
	
	public void renderRoundBarKey(Canvas canvas,List<ArcLineData> dataSet)
	{
		if (isShow() == false) return;	
		refreshLst();
		convertArrayArcLineKey(dataSet);
		render(canvas);
	}
	
	public void renderRangeBarKey(Canvas canvas,String key,int textColor)
	{		
		if (isShow() == false) return;	
		if("" == key||key.length() == 0) return;	
		
		refreshLst();
									
		mLstKey.add(key);
		mLstColor.add(textColor);		
		PlotDot pDot = new PlotDot();
		pDot.setDotStyle(XEnum.DotStyle.RECT);
		mLstDotStyle.add(pDot);
		
		render(canvas);		
	}
	
	
	private void setLnChartStatus()
	{
		if(null == mPaintLine)mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.mPaintLine.setStrokeWidth(2);
		mIsLnChart = true;
	}
	
	
	private void render(Canvas canvas)
	{
		if(null == mXChart) return;	
		if(null == mPlotArea) mPlotArea = mXChart.getPlotArea();
		calcContentRect();		
		getStartXY();
		drawLegend(canvas);		
	}
	
	private float getRectWidth()
	{
		float rectWidth = 0.0f;
		float textHeight = getLabelTextHeight();
		
		if( mIsLnChart)
		{
			rectWidth = 2 * textHeight;
		}else{
			rectWidth = textHeight/2 + textHeight;
		}
		return rectWidth;
	}
	
	private void calcContentRect()
	{
		int countDots = (null != mLstDotStyle)? mLstDotStyle.size():0 ;	
		int countText = (null != mLstKey)?mLstKey.size():0;
		if(0 == countText && 0 == countDots ) return;
		//int count = (countText > countDots)?countText:countDots;
															
		String text = "";
		float textHeight = getLabelTextHeight();
										
		int row = 1;
		mMapID.clear();
		
		float areaWidth = mPlotArea.getWidth() - 2 * mMargin;
		float rectWidth = getRectWidth();
		
		float rowWidth = 0.0f;	
		float rowHeight = textHeight;	
		float maxHeight = rowHeight;
		float maxWidth = 0.0f;					
		
		for(int i=0;i<countText;i++)
		{									
			if(countDots > i) 
			{
				PlotDot plot = mLstDotStyle.get(i);		
				if(mIsLnChart)
				{
					rowWidth += rectWidth + mColSpan;	
				}else{
					if( plot.getDotStyle() !=  XEnum.DotStyle.HIDE )		
										rowWidth += rectWidth + mColSpan;				
				}
			}			
			
			text = mLstKey.get(i);				
			float labelWidth = getLabelTextWidth( text);
			rowWidth += labelWidth;
			
			switch(getType())
			{
				case ROW:					
						if(Float.compare(rowWidth , areaWidth) == 1)
						{	//换行			
							rowWidth = rectWidth + mColSpan +labelWidth;
							maxHeight += rowHeight + mRowSpan;							
							row++;													
						}else{			
							rowWidth += mColSpan;
							if(Float.compare(rowWidth, maxWidth) == 1) maxWidth = rowWidth;														
						}				
					break;
				case COLUMN:						
						if(Float.compare(rowWidth, maxWidth) == 1)maxWidth = rowWidth;		
						maxHeight += rowHeight + mRowSpan;	
						
						rowWidth = 0.0f;
						row++;
						break;
				 default:
					 break;
			}	
						
			mMapID.put(i,row);
		}			
	
		mRectWidth = maxWidth +  2 * mMargin;
		mRectHeight = maxHeight +  2 * mMargin ;	
		
		if(XEnum.LegendType.COLUMN == getType() ) mRectHeight -= 2 * mRowSpan;
	}
	
	
	private void getStartXY()
	{
		float mBoxLineSize = BOX_LINE_SIZE;
		if(!this.mShowBox) mBoxLineSize = 0.0f;
		
		switch(getHorizontalAlign())
		{
			case LEFT:
				if(EnumChartType.CIR == mType)
				{
					mKeyLabelX = mXChart.getLeft() + mOffsetX ;
				}else{
					mKeyLabelX = mPlotArea.getLeft() + mOffsetX ;
				}
				
				mKeyLabelX += mBoxLineSize;
				break;
			case CENTER:				
					mKeyLabelX = this.mXChart.getLeft() + 
							(mXChart.getWidth()- mRectWidth) / 2 + mOffsetX ;					
				break;
			case RIGHT:
				if(EnumChartType.CIR == mType)
				{
					mKeyLabelX = mXChart.getRight() - mOffsetX - mRectWidth;
				}else{
					mKeyLabelX = mPlotArea.getRight() - mOffsetX - mRectWidth;
				}
				mKeyLabelX -= mBoxLineSize;
				break;
			default:
				break;
		}
				
	
		switch(getVerticalAlign())
		{
			case TOP:
				
				if(XEnum.LegendType.COLUMN == getType())
				{
					mKeyLabelY = mPlotArea.getTop()  + mOffsetY;
					mKeyLabelY += mBoxLineSize;
				}else{
					mKeyLabelY = mPlotArea.getTop() -  mRectHeight - mOffsetY;	
					mKeyLabelY -= mBoxLineSize;
				}
				
				break;
			case MIDDLE:						
				mKeyLabelY = mPlotArea.getTop() + ( mPlotArea.getHeight() - mRectHeight) /2;
				break;
			case BOTTOM:
				
				if(XEnum.LegendType.COLUMN == getType())
				{
					mKeyLabelY = mXChart.getBottom() + mOffsetY;				
					mKeyLabelY += mXChart.getBorderWidth();	
					
					mKeyLabelY += mBoxLineSize;					
				}else{
					mKeyLabelY = mXChart.getBottom() - mRectHeight - mOffsetY;				
					mKeyLabelY -= mXChart.getBorderWidth();	
					
					mKeyLabelY -= mBoxLineSize;
				}								
				break;
			default:
				break;
		}	
				
	}
		
	
	private void drawLegend(Canvas canvas)
	{			
		int countDots = (null != mLstDotStyle)? mLstDotStyle.size():0 ;	
		int countText = (null != mLstKey)?mLstKey.size():0;
		if(0 == countText && 0 == countDots ) return;
		int countColor = (null != mLstColor)? mLstColor.size():0 ;
							
		float currDotsX = mKeyLabelX + mMargin ;		
		float currRowX = currDotsX;
		float currRowY = mKeyLabelY + mMargin;	
		
		float textHeight = getLabelTextHeight();
		float rowHeight = textHeight;
		float rectWidth = getRectWidth(); //2 * textHeight;		
		
		int currRowID = 0;
		Iterator iter = this.mMapID.entrySet().iterator();		
		
		//背景
		drawBox(canvas);	
			
		//图例
		while(iter.hasNext()){
			    Entry  entry=(Entry)iter.next();
			
			    Integer id =(Integer) entry.getKey();
			    Integer row =(Integer) entry.getValue();//行号
			    
			    if(row > currRowID) //换行
			    {
			    	if(0 < currRowID)currRowY += rowHeight + mRowSpan;				    		    
			    	currRowX = mKeyLabelX + mMargin;	
			    	currRowID = row;
			    }
			    
			    //颜色
			    if(countColor > id)
			    {
			    	this.getPaint().setColor(mLstColor.get(id));			    	
			    	if( mIsLnChart)mPaintLine.setColor(mLstColor.get(id));
			    }else{
			    	this.getPaint().setColor(Color.BLACK);
			    	if( mIsLnChart)mPaintLine.setColor(Color.BLACK);
			    }			    
			   	
			    if(countDots > id)
				{			    				    	
					PlotDot plot = mLstDotStyle.get(id);		
					
					if( mIsLnChart) //line
					{																			
						canvas.drawLine(currRowX            , currRowY + rowHeight/2, 
								        currRowX + rectWidth, currRowY + rowHeight/2,  
								        this.mPaintLine);
						
						PlotDotRender.getInstance().renderDot(canvas,plot, 
								currRowX + rectWidth/2,
								currRowY + rowHeight/2,
								this.getPaint());
						currRowX += rectWidth + mColSpan;	
						
					}else{
						if( plot.getDotStyle() !=  XEnum.DotStyle.HIDE )
						{		
							PlotDotRender.getInstance().renderDot(canvas,plot, 
									currRowX + rectWidth/2,
									currRowY + rowHeight/2 , 
									this.getPaint());																				
							currRowX += rectWidth + mColSpan;															
						}
					}
				}
			    String label = mLstKey.get(id);
			    if("" != label)
			    	canvas.drawText(label,currRowX , currRowY + rowHeight,this.getPaint());		
			    			    
			    currRowX += this.getLabelTextWidth(label);			
			    currRowX += mColSpan;				  
		}
		
		mMapID.clear();
		clearLst();
	}
	
	private void clearLst()
	{
		if(null != mLstDotStyle)
		{
			mLstDotStyle.clear();
			mLstDotStyle = null;
		}
		if(null != mLstKey)
		{
			mLstKey.clear();
			mLstKey = null;
		}
		if(null != mLstColor)
		{
			mLstColor.clear();
			mLstColor = null;
		}
	}
	

	private void drawBox(Canvas canvas)
	{				
		if(!mShowBox)return;
		
		RectF rect = new RectF();	
		rect.left = mKeyLabelX ;
		rect.right = mKeyLabelX + mRectWidth;
		rect.top = mKeyLabelY;
		rect.bottom = mKeyLabelY + mRectHeight;	
				
		mBorder.renderRect(canvas, rect,mShowBoxBorder,mShowBackground);			
	}
		
	private void refreshLst()
	{
		initEnv();
		
		if(null == mLstKey)
			mLstKey = new ArrayList<String>();
		else
			mLstKey.clear();
			
		if(null == mLstDotStyle)
			mLstDotStyle = new ArrayList<PlotDot>();
		else
			mLstDotStyle.clear();
		
		if(null == mLstColor)
			mLstColor = new ArrayList<Integer>();
		else
			mLstColor.clear();		
	}
	

	////////////////////////////////

	private void convertArrayLineKey(List<LnData> dataSet)
	{
		if(null == dataSet) return;
		
		String key = "";
		for (LnData cData : dataSet) {
			key = cData.getLineKey();				
			if(!isDrawKey(key))continue;
			if("" == key)continue;
			
			mLstKey.add(key);
			mLstColor.add(cData.getLineColor());
			mLstDotStyle.add(cData.getPlotLine().getPlotDot());
		}
	}
	
	private void convertArrayBarKey(List<BarData> dataSet)
	{
		if(null == dataSet) return;
				
		String key = "";
		for (BarData cData : dataSet) {
			key = cData.getKey();				
			if(!isDrawKey(key))continue;
			if("" == key)continue;
			
			mLstKey.add(key);
			mLstColor.add(cData.getColor());
			
			PlotDot dot = new PlotDot();
			dot.setDotStyle(XEnum.DotStyle.RECT);
			mLstDotStyle.add(dot);			
		}
	}
	
	private void convertArrayArcLineKey(List<ArcLineData> dataSet)
	{
		if(null == dataSet) return;
		
		String key = "";
		for (ArcLineData cData : dataSet) {
			key = cData.getKey();	
			if(!isDrawKey(key))continue;
			if("" == key)continue;
			
			mLstKey.add(key);
			mLstColor.add(cData.getBarColor());
			
			PlotDot pDot = new PlotDot();
			pDot.setDotStyle(XEnum.DotStyle.RECT);
			mLstDotStyle.add(pDot);
		}
	}
	
	private boolean isDrawKey(String key)
	{
		if("" == key||key.length() == 0) return false;
		return true;
	}
	
	////////////////////////////////
}

