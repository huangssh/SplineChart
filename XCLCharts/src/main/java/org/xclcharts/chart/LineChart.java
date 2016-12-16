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
import org.xclcharts.renderer.LnChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.line.PlotDot;
import org.xclcharts.renderer.line.PlotDotRender;
import org.xclcharts.renderer.line.PlotLine;

import android.graphics.Canvas;
import android.util.Log;

/**
 * @ClassName LineChart
 * @Description  线图基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class LineChart extends LnChart{
	
	private  static final  String TAG ="LineChart";
	
	//数据源
	protected List<LineData> mDataSet;
	
	//当线与轴交叉时是否不断开连接
	private boolean mLineAxisIntersectVisible = true;
	
	//图例
	private List<LnData> mLstKey = new ArrayList<LnData>();	
								
	public LineChart()
	{
		
	}
	
	@Override
	public XEnum.ChartType getType()
	{
		return XEnum.ChartType.LINE;
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
		 * @param dataSet 数据源
		 */
		public void setDataSource( List<LineData> dataSet)
		{										
			this.mDataSet = dataSet;		
		}			
		
		/**
		 * 返回数据源
		 * @return 数据源
		 */
		public List<LineData> getDataSource()
		{
			return this.mDataSet;
		}
						
						
		/**
		 *  设置当值与底轴的最小值相等时，线是否与轴连结显示. 默认为连接(true)
		 * @param visible 是否连接
		 */
		public void setLineAxisIntersectVisible( boolean visible)
		{
			mLineAxisIntersectVisible = visible;
		}
		
		/**
		 * 返回当值与底轴的最小值相等时，线是否与轴连结的当前状态
		 * @return 状态
		 */
		public boolean getLineAxisIntersectVisible()
		{
			return mLineAxisIntersectVisible;
		}	
		
//		/**
//		 * 设置柱形居中位置,依刻度线居中或依刻度中间点居中。
//		 * @param style 居中风格
//		 */
//		public void setBarCenterStyle(XEnum.BarCenterStyle style)
//		{
//			mBarCenterStyle = style;
//		}
//		
//		/**
//		 * 返回柱形居中位置,依刻度线居中或依刻度中间点居中。
//		 * @return 居中风格
//		 */
//		public XEnum.BarCenterStyle getBarCenterStyle()
//		{
//			return mBarCenterStyle;
//		}
		
		/**
		 * 设置线图的X坐标开始计算位置，默认false,即从轴上开始,true则表示从第一个刻度线位置开始
		 * @param status 状态
		 */
		public void setXCoordFirstTickmarksBegin(boolean status)
		{
			mXCoordFirstTickmarksBegin = status;
		}

				
		/**
		 * 绘制线
		 * @param canvas	画布
		 * @param bd		数据类
		 * @param type		处理类型
		 */
		private boolean renderLine(Canvas canvas, LineData bd,String type,int dataID)
		{
			if(null == categoryAxis || null == dataAxis) return false;
			
			if(null == bd)
			{
				Log.i(TAG,"传入的线的数据序列参数为空.");
				return false;
			}
			
			float initX =  plotArea.getLeft();
            float initY =  plotArea.getBottom();             
			float lineStartX = initX,lineStartY = initY;         
            float lineStopX = 0.0f,lineStopY = 0.0f;                               
            													
			//得到分类轴数据集
			List<String> dataSet =  categoryAxis.getDataSet();
			if(null == dataSet ||dataSet.size() == 0){
				Log.w(TAG,"分类轴数据为空.");
				return false;
			}		
			//数据序列
			List<Double> chartValues = bd.getLinePoint();
			if(null == chartValues ||chartValues.size() == 0 )
			{
				Log.w(TAG,"当前分类的线数据序列值为空.");
				return false;
			}
				
			//步长	
			float XSteps = 0.0f;	
			int j = 0; //,childID = 0;	
			int tickCount = dataSet.size();		
			if (1 == tickCount)j = 1;  //label仅一个时右移 !mXCoordFirstTickmarksBegin && 
					
			int labeltickCount = getCategoryAxisCount();		
			XSteps = getVerticalXSteps(labeltickCount );
						
			 						
			float itemAngle = bd.getItemLabelRotateAngle();
			PlotLine pLine = bd.getPlotLine();   
			PlotDot pDot = pLine.getPlotDot();	        
			float radius = pDot.getDotRadius();	
			double bv = 0.d;
			
		    //画线
			int count = chartValues.size();
			for(int i=0;i<count;i++)
            {								
				bv = chartValues.get(i);            	
            	lineStopY = getVPValPosition(bv);		            	            	
            	
            	if(mXCoordFirstTickmarksBegin)
				{
            		lineStopX = add(initX , mul((j+1) , XSteps));  
				}else{
					lineStopX = add(initX , mul(j , XSteps));  
				}          
            	//当柱图与线图混合，且柱图柱形为BarCenterStyle.SPACE时
            	if(mXCoordFirstTickmarksBegin && 
            			XEnum.BarCenterStyle.SPACE == getBarCenterStyle())
            					lineStopX = sub(lineStopX ,div(XSteps,2)); 
            	            	            	           
            	if(0 == j)
            	{
            		lineStartX = lineStopX;
            		lineStartY = lineStopY;
            	}
                        	
            	if( getLineAxisIntersectVisible() == false &&
            			Double.compare(bv, dataAxis.getAxisMin()) == 0 )
            	{
            		//如果值与最小值相等，即到了轴上，则忽略掉  
            		lineStartX = lineStopX;
    				lineStartY = lineStopY;

    				j++;
            	}else{
	            	        
	            	if(type.equalsIgnoreCase("LINE"))
	            	{	            		
	            		if(getLineAxisIntersectVisible() == true ||
	            					Float.compare(lineStartY, initY) != 0 )	
	            		{	            		
	            			DrawHelper.getInstance().drawLine(bd.getLineStyle(), 
	            					lineStartX ,lineStartY ,lineStopX ,lineStopY,
	            					canvas,pLine.getLinePaint());		            			
	            		}
	            	}else if(type.equalsIgnoreCase("DOT2LABEL")){
	            		
	            		if(!pLine.getDotStyle().equals(XEnum.DotStyle.HIDE))
	                	{          
	                		PlotDotRender.getInstance().renderDot(canvas,pDot,
	                				lineStopX ,lineStopY,
	                				pLine.getDotPaint()); //标识图形            		
	                			                		
	                		savePointRecord(dataID,i, lineStopX  + mMoveX, lineStopY  + mMoveY,	                		
					                		lineStopX - radius + mMoveX, 
					                		lineStopY - radius + mMoveY,
					    					lineStopX + radius + mMoveX, 
					    					lineStopY + radius + mMoveY);  
	            			lineStopX = add(lineStopX  , radius);           			
	                	}
	            		
	            		//显示批注形状
	    				drawAnchor(getAnchorDataPoint(),dataID,i,canvas,lineStopX - radius,lineStopY,radius);
	            		
	            		if(bd.getLabelVisible()) //标签
	                	{	                	            
	            			bd.getLabelOptions().drawLabel(canvas, pLine.getDotLabelPaint(), 
	            					getFormatterItemLabel(bv), lineStopX  - radius, lineStopY,
	            					itemAngle,bd.getLineColor());
	                	}
	            			            		
	            	}else{
	            		Log.w(TAG,"未知的参数标识。"); //我不认识你，我不认识你。
	            		return false;
	            	}      				
            	
					lineStartX = lineStopX;
					lineStartY = lineStopY;
	
					j++;
            	} //if(bv != dataAxis.getAxisMin())
            } 				
			return true;
		}
					
		/**
		 * 绘制图表
		 */
		private boolean renderVerticalPlot(Canvas canvas)
		{											
			if(null == mDataSet) 
			{
				Log.w(TAG,"数据轴数据为空.");
				return false;
			}			
			
			mLstKey.clear();
			String key = "";
			//开始处 X 轴 即分类轴                  
			int count = mDataSet.size();
			for(int i=0;i<count;i++)
			{								
				if(renderLine(canvas,mDataSet.get(i),"LINE",i) == false) 
					return false;;
				if(renderLine(canvas,mDataSet.get(i),"DOT2LABEL",i) == false) 
					return false;;
				key = mDataSet.get(i).getLineKey();				
				if("" != key && key.length() > 0)
					mLstKey.add(mDataSet.get(i));
			}			
			
			return true;
		}	

		/////////////////////////////////////////////
				
		@Override
		protected void drawClipPlot(Canvas canvas)
		{
			if(renderVerticalPlot(canvas) == true)
			{				
				if(null != mCustomLine) //画横向定制线
				{
					mCustomLine.setVerticalPlot(dataAxis, plotArea, getAxisScreenHeight());
					mCustomLine.renderVerticalCustomlinesDataAxis(canvas);	
				}
				
			}
		}
			
		@Override
		protected void drawClipLegend(Canvas canvas)
		{
			plotLegend.renderLineKey(canvas, mLstKey);
			mLstKey.clear();
		}
		/////////////////////////////////////////////

}
