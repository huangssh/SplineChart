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

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.LnChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.line.DotInfo;
import org.xclcharts.renderer.line.PlotCustomLine;
import org.xclcharts.renderer.line.PlotDot;
import org.xclcharts.renderer.line.PlotDotRender;
import org.xclcharts.renderer.line.PlotLine;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.util.Log;


/**
 * @ClassName SplineChart
 * @Description  曲线图基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SplineChart extends LnChart{
	
	private static  String TAG="SplineChart";
	
	//数据源
	private List<SplineData> mDataSet;
	
	//分类轴的最大，最小值
	private double mMaxValue = 0d;
	private double mMinValue = 0d;
		
	//用于格式化标签的回调接口
	private IFormatterTextCallBack mLabelFormatter;	
	
	//平滑曲线
	private List<PointF> mLstPoints = new ArrayList<PointF>(); 
	private Path mBezierPath = new Path();
			
	//key
	private List<LnData> mLstKey = new ArrayList<LnData>();	
	
	private List<DotInfo> mLstDotInfo = new ArrayList<DotInfo>();	
	private List<DotInfo> mLstDotInfoForLabel = new ArrayList<DotInfo>();	
	
	//平滑曲线
  	private XEnum.CrurveLineStyle mCrurveLineStyle = XEnum.CrurveLineStyle.BEZIERCURVE;	
  	
 // 用于绘制定制线(分界线)
 	private PlotCustomLine mXAxisCustomLine = null;

		
	public SplineChart()
	{
		categoryAxisDefaultSetting();	
		dataAxisDefaultSetting();
	}
	
	@Override
	public XEnum.ChartType getType()
	{
		return XEnum.ChartType.SPLINE;
	}

	@Override
	protected void categoryAxisDefaultSetting()
	{		
		if(null != categoryAxis)
			categoryAxis.setHorizontalTickAlign(Align.CENTER);
	}
	
	@Override
	protected void dataAxisDefaultSetting()
	{		
		if(null != dataAxis)
			dataAxis.setHorizontalTickAlign(Align.LEFT);
	}
	
	/**
	 * 分类轴的数据源
	 * @param categories 标签集
	 */
	public void setCategories( List<String> categories)
	{
		if(null != categoryAxis)categoryAxis.setDataBuilding(categories);
	}
	
	/**
	 *  设置数据轴的数据源
	 * @param dataSeries 数据序列
	 */
	public void setDataSource( List<SplineData> dataSeries)
	{		
		this.mDataSet = dataSeries;		
	}	
	
	public List<SplineData>  getDataSource()
	{
		return this.mDataSet;
	}
	
	/**
	 *  显示数据的数据轴最大值
	 * @param value 数据轴最大值
	 */
	public void setCategoryAxisMax( double value)
	{
		mMaxValue = value;
	}	
	
	/**
	 * 设置分类轴最小值
	 * @param value 最小值
	 */
	public void setCategoryAxisMin( double value)
	{
		mMinValue = value;
	}	
		
	/**
	 * 设置分类轴的竖向定制线值
	 * 
	 * @param customLineDataset
	 *            定制线数据集合
	 */
	public void setCategoryAxisCustomLines(List<CustomLineData> customLineDataset) {
		if (null == mXAxisCustomLine) mXAxisCustomLine = new PlotCustomLine();
		mXAxisCustomLine.setCustomLines(customLineDataset);
	}		
	
	/**
	 * 设置标签的显示格式
	 * @param callBack 回调函数
	 */
	public void setDotLabelFormatter(IFormatterTextCallBack callBack) {
		this.mLabelFormatter = callBack;
	}
	
	/**
	 * 返回标签显示格式
	 * 
	 * @return 显示格式
	 */
	protected String getFormatterDotLabel(String text) {
		String itemLabel = "";
		try {
			itemLabel = mLabelFormatter.textFormatter(text);
		} catch (Exception ex) {
			itemLabel = text;
		}
		return itemLabel;
	}
		
	
	/**
	 * 设置曲线显示风格:直线(NORMAL)或平滑曲线(BEZIERCURVE)
	 * @param style 曲线显示风格
	 */
	public void setCrurveLineStyle(XEnum.CrurveLineStyle style)
	{
		mCrurveLineStyle = style;
	}
	
	/**
	 * 返回曲线显示风格
	 * @return 曲线显示风格
	 */
	public XEnum.CrurveLineStyle getCrurveLineStyle()
	{
		return mCrurveLineStyle;
	}
							
	private void calcAllPoints( SplineData bd,List<PointF> lstPoints,List<DotInfo> lstDotInfo)
	{
		
		if(null == bd)
		{
			Log.w(TAG,"传入的数据序列参数为空.");
			return;
		}
		if( Double.compare(mMaxValue, mMinValue) == -1)
		{
			Log.w(TAG,"轴最大值小于最小值.");
			return ;
		}
		
		if( Double.compare(mMaxValue, mMinValue) == 0)
		{
			Log.w(TAG,"轴最大值与最小值相等.");
			return ;
		}
				
		float initX =  plotArea.getLeft();
        float initY =  plotArea.getBottom();
		float lineStartX = initX,lineStartY = initY; 
        float lineStopX = 0.0f,lineStopY = 0.0f;       
        
		//得到标签对应的值数据集		
		List<PointD> chartValues = bd.getLineDataSet();	
		if(null == chartValues) return ;
											
	    //画出数据集对应的线条						
		int count = chartValues.size();
		for(int i=0;i<count;i++)
		{
				PointD  entry = chartValues.get(i);
            	lineStopX = getLnXValPosition(entry.x,mMaxValue,mMinValue);
            	lineStopY = getVPValPosition(entry.y);
            	            	
            	if(0 == i )
        		{
            		lineStartX = lineStopX;
					lineStartY = lineStopY;
					
            		//line
            		lstPoints.add( new PointF(lineStartX,lineStartY));
            		lstPoints.add( new PointF(lineStopX,lineStopY));        	
            		lstPoints.add( new PointF(lineStopX,lineStopY));        
        		}else{     
        			//line
        			lstPoints.add( new PointF(lineStopX,lineStopY));
        		}            		
        
            	//dot
            	lstDotInfo.add(new DotInfo(entry.x,entry.y,lineStopX,lineStopY));
                         					
				lineStartX = lineStopX;
				lineStartY = lineStopY;       								
		}								
	}
	
	
	private boolean renderLine(Canvas canvas, SplineData spData,
												List<PointF> lstPoints)
	{		        
		int count = lstPoints.size();
		for(int i=0;i<count;i++)
		{	        	
			if(0 == i)continue;
			PointF pointStart = lstPoints.get(i - 1);
			PointF pointStop = lstPoints.get(i);
			    
			DrawHelper.getInstance().drawLine(spData.getLineStyle(),
										pointStart.x ,pointStart.y ,pointStop.x ,pointStop.y,
										canvas,spData.getLinePaint());				
		}
		return true;
	}
		
	private boolean renderBezierCurveLine(Canvas canvas,Path bezierPath,
			SplineData spData,List<PointF> lstPoints)
	{		        		
		renderBezierCurveLine(canvas,spData.getLinePaint(),bezierPath,lstPoints); 		 
		return true;
	}
	
	private boolean renderDotAndLabel(Canvas canvas, SplineData spData,int dataID,
										List<PointF> lstPoints)
	{	
		PlotLine pLine = spData.getPlotLine();
		if(pLine.getDotStyle().equals(XEnum.DotStyle.HIDE) == true 
					&& spData.getLabelVisible() == false )
		{
			return true;
		}
		
		float itemAngle = spData.getItemLabelRotateAngle();
		
		PlotDot pDot = pLine.getPlotDot();	
		float radius = pDot.getDotRadius();
		int count =  mLstDotInfo.size();		
		for(int i=0;i < count;i++)
		{
			DotInfo dotInfo = mLstDotInfo.get(i);						   
		    if(!pLine.getDotStyle().equals(XEnum.DotStyle.HIDE))
        	{		    	
		    	PlotDotRender.getInstance().renderDot(canvas,pDot,
		    			dotInfo.mX ,dotInfo.mY,pLine.getDotPaint()); //标识图形            			                	
        			    			    	
		    	savePointRecord(dataID,i, 
		    			dotInfo.mX + mMoveX, dotInfo.mY + mMoveY,		    			
		    			dotInfo.mX - radius + mMoveX, 
		    			dotInfo.mY - radius + mMoveY,
		    			dotInfo.mX + radius + mMoveX, 
		    			dotInfo.mY + radius + mMoveY);		 		    			    			    
		    	//childID++;
        	}
		    
		    //Log.e("spchart","dataID:"+Float.toString(dataID));
		    //Log.e("spchart","  I:"+Integer.toString(i));
		   //显示批注形状
			drawAnchor(getAnchorDataPoint(),dataID,i,canvas,dotInfo.mX,dotInfo.mY,radius);
		    
		    if(spData.getLabelVisible())
        	{            			
        		//请自行在回调函数中处理显示格式
    			spData.getLabelOptions().drawLabel(canvas, pLine.getDotLabelPaint(), 
    					getFormatterDotLabel(dotInfo.getLabel()),
    					dotInfo.mX ,dotInfo.mY,itemAngle,spData.getLineColor());
        	}   			
		}
				
		return true;
	}
		

	/**
	 * 绘制图
	 */
	private boolean renderPlot(Canvas canvas)
	{
		//检查是否有设置分类轴的最大最小值		
		if(Double.compare(mMaxValue, mMinValue) == 0 && Double.compare(0d, mMaxValue) == 0)
		{
			Log.e(TAG,"请检查是否有设置分类轴的最大最小值。");
			return false;
		}
		if(null == mDataSet)
		{
			Log.e(TAG,"数据源为空.");
			return false;
		}
				
		//开始处 X 轴 即分类轴              	
		int count = mDataSet.size();
		for(int i=0;i<count;i++)
		{															
			SplineData spData = mDataSet.get(i);			
			calcAllPoints( spData,mLstPoints,mLstDotInfo);					
			
			switch(getCrurveLineStyle())
			{
				case BEZIERCURVE:								
					renderBezierCurveLine(canvas,mBezierPath,spData,mLstPoints);
					break;
				case BEELINE:				
					renderLine(canvas,spData,mLstPoints);	
					break;
				default:
					Log.e(TAG,"未知的枚举类型.");
					continue;				
			}	
			renderDotAndLabel(canvas,spData,i,mLstPoints);
			mLstKey.add(mDataSet.get(i));
			
			mLstDotInfoForLabel.clear();
			mLstDotInfoForLabel.addAll(mLstDotInfo);
			mLstDotInfo.clear();
			mLstPoints.clear();
			mBezierPath.reset();			
		}	
		
		return true;
	}
	
	public List<DotInfo> getmLstDotInfo() {
		return mLstDotInfoForLabel;
	}

	public void setmLstDotInfo(List<DotInfo> mLstDotInfo) {
		this.mLstDotInfo = mLstDotInfo;
	}

	@Override
	protected void drawClipPlot(Canvas canvas)
	{
		if(renderPlot(canvas) == true)
		{				
			if(null != mCustomLine) //画横向定制线
			{
				mCustomLine.setVerticalPlot(dataAxis, plotArea, getPlotScreenHeight());
				mCustomLine.renderVerticalCustomlinesDataAxis(canvas);										
			}
			
			if(null != mXAxisCustomLine) //画x轴上的竖向定制线
			{				
				mXAxisCustomLine.renderCategoryAxisCustomlines(
								canvas,
								this.getPlotScreenWidth(),this.plotArea,
								mMaxValue,mMinValue);	
			}
			
		}
	}

	
	@Override
	protected void drawClipLegend(Canvas canvas)
	{
		//图例
		plotLegend.renderLineKey(canvas,mLstKey);
		mLstKey.clear();
	}
}
