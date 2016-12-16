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
package org.xclcharts.renderer.plot;


import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.IRender;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import android.graphics.Canvas;
import android.graphics.Paint.Align;

/**
 * @ClassName axisTitle
 * @Description  轴标题(axisTitle)绘制类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * 
 */

public class AxisTitleRender extends AxisTitle implements IRender{
			
	private XChart mChart = null;
	 	 	
	public AxisTitleRender()
	{

	}
	
	/**
	 * 传入chart给轴标题类
	 * @param chart 图基类
	 */
	public void setRange(XChart chart)
	{
		mChart = chart;
	}


	@Override
	public boolean render(Canvas canvas) {
		// TODO Auto-generated method stub
		
		if(null == mChart) return false;
		
		float left = 0.0f,top = 0.0f,right =0.0f,bottom = 0.0f;
		
		if(mAxisTitleStyle == XEnum.AxisTitleStyle.ENDPOINT)
		{
			left = mChart.getLeft();
			top = mChart.getPlotArea().getTop();
			right = mChart.getPlotArea().getRight();
			bottom = mChart.getPlotArea().getBottom();
		}else{
			left = mChart.getLeft();
			top = mChart.getTop();
			right = mChart.getRight();
			bottom = mChart.getBottom();
		}
								
		if(this.getLeftTitle().length() > 0)
		{
			drawLeftAxisTitle(canvas,getLeftTitle(),left,top,right,bottom);
		}
		
		if(this.getLowerTitle().length() > 0 )
		{						
			drawLowerAxisTitle(canvas,getLowerTitle(),left,top,right,bottom);
		}
		
		if(this.getRightTitle().length() > 0)
		{
			drawRightAxisTitle(canvas,getRightTitle(),left,top,right,bottom);
		}
		
		
		/*
		if(mAxisTitleStyle == XEnum.AxisTitleStyle.ENDPOINT && mCrossPointTitle.length() > 0 )
		{						
			drawCrossAxisTitle(canvas,mCrossPointTitle,mChart.getLeft(),mChart.getTop(),
					mChart.getRight(),mChart.getBottom());
		}
		*/
										
		return true;
	}
	
			
	 /**
	  * 绘制左边轴标题
	  * @param axisTitle	内容
	  * @param left		左边X坐标
	  * @param top		上方Y坐标
	  * @param right	右边X坐标
	  * @param bottom	下方Y坐标
	  */
	public void drawLeftAxisTitle(Canvas canvas, String axisTitle,double left,double top,
											 double right,double bottom) 
	{							
		if(null == canvas) return ;
		
		//是否需要绘制轴标题
		if( 0 == axisTitle.length() || "" == axisTitle)return;
		
		 //计算图列宽度		 
		 double axisTitleTextHeight = DrawHelper.getInstance().getTextWidth(
				 										getLeftTitlePaint(),axisTitle);
         
		 //画布与图表1/3的地方显示
		 float axisTitleTextStartX = Math.round(left+ mLeftAxisTitleOffsetX  + getLeftTitlePaint().getTextSize()) ; 		 
		 
         //轴标题Y坐标
		 float axisTitleTextStartY = 0.0f;
		 if(mAxisTitleStyle == XEnum.AxisTitleStyle.ENDPOINT)
		 {
			 axisTitleTextStartY = Math.round(top +  axisTitleTextHeight);
		 }else{
			 axisTitleTextStartY = Math.round(top + (bottom - top ) /2 + axisTitleTextHeight/2);
		 }
		// float axisTitleTextStartY = Math.round(top + (bottom - top ) /2 + axisTitleTextHeight/2);
         
         //得到单个轴标题文字高度     		
         double axisTitleCharHeight =0d;
         
         for(int i= 0; i< axisTitle.length();i++)
         {        	 
        	 axisTitleCharHeight = DrawHelper.getInstance().getTextWidth(
        			 						getLeftTitlePaint(),axisTitle.substring(i, i+1));        	 
        	 //绘制文字，旋转-90得到横向效果
        	 DrawHelper.getInstance().drawRotateText(axisTitle.substring(i, i+1),
					        			 axisTitleTextStartX,axisTitleTextStartY,
					        			 -90,
					        			 canvas, getLeftTitlePaint() );
        	 axisTitleTextStartY -= axisTitleCharHeight;
         }
	}
	
	/**
	 * 绘制底部轴标题
	 * @param axisTitle	内容
	 * @param left		左边X坐标
	 * @param top		上方Y坐标
	 * @param right		右边X坐标
	 * @param bottom	下方Y坐标
	 */
	public void drawLowerAxisTitle(Canvas canvas, String axisTitle,
								double left,double top,double right,double bottom)
	{         
         if(null == canvas) return ;
 		 //是否需要绘制轴标题
 		 if(""==axisTitle || 0 == axisTitle.length() )return;
 	
 		 //计算轴标题文字宽度		 
 		 double axisTitleTextHeight = DrawHelper.getInstance().getPaintFontHeight(
 				 												getLowerTitlePaint());     
 		  	
 		 float axisTitleTextStartX = 0.0f;
 		 float axisTitleY = (float) MathHelper.getInstance().sub(mChart.getBottom(),axisTitleTextHeight/2); 			 	
		 if(mAxisTitleStyle == XEnum.AxisTitleStyle.ENDPOINT)
		 {
			 axisTitleTextStartX =  (float) right ; 
			 
			 //左下角交叉点绘制
			 if(mCrossPointTitle.length() > 0) 
			 {
				 getLowerTitlePaint().setTextAlign(Align.LEFT);
				 DrawHelper.getInstance().drawRotateText(mCrossPointTitle,
						 		(float) left,axisTitleY,0,canvas, getLowerTitlePaint());					 
			 }
			 getLowerTitlePaint().setTextAlign(Align.RIGHT);
		 }else{
			 axisTitleTextStartX =  Math.round(left +  (right - left) / 2) ;  
		 }	  		
 		DrawHelper.getInstance().drawRotateText(axisTitle,
 	       		 axisTitleTextStartX - mLowerAxisTitleOffsetY ,axisTitleY,0,canvas, getLowerTitlePaint());
		
	}

	
	
	/**
	 * 绘制右边轴标题
	 * @param axisTitle	内容
	 * @param left		左边X坐标
	 * @param top		上方Y坐标
	 * @param right		右边X坐标
	 * @param bottom	下方Y坐标
	 */
	public void drawRightAxisTitle(Canvas canvas,String axisTitle,
								double left,double top,double right,double bottom)
	{			
		if(null == canvas) return ;
		
		//是否需要绘制轴标题
		if( 0 == axisTitle.length() || "" == axisTitle)return;
		
		//计算图列高度，超过最大高度要用...表示,这个后面再加		 
		 float axisTitleTextHeight = DrawHelper.getInstance().getTextWidth(
				 										getRightTitlePaint(),axisTitle);         
		 //画布与图表1/3的地方显示
		 float axisTitleTextStartX =  Math.round(right - mRightAxisTitleOffsetX - getRightTitlePaint().getTextSize()) ;         
         //轴标题Y坐标
		 float axisTitleTextStartY = Math.round(top + (bottom - top -  axisTitleTextHeight) /2 );	          
         //得到单个轴标题文字高度     		
 		 float axisTitleCharHeight = 0.0f ;
 		  		 		
         for(int i= 0; i< axisTitle.length();i++)
         {        	 
        	 axisTitleCharHeight = DrawHelper.getInstance().getTextWidth(
        			 						getRightTitlePaint(),axisTitle.substring(i, i+1));        	 
        	 //绘制文字，旋转-90得到横向效果
        	 DrawHelper.getInstance().drawRotateText(axisTitle.substring(i, i+1),
        			 axisTitleTextStartX,axisTitleTextStartY,90,canvas, getRightTitlePaint() );
        	 axisTitleTextStartY += axisTitleCharHeight;
         }
	
	}


}
