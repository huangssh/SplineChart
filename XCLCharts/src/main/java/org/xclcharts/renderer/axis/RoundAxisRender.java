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
 * @version v0.1
 */

package org.xclcharts.renderer.axis;

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;

import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.util.Log;

/**
 * @ClassName RoundAxisRender
 * @Description  仪表盘绘制类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class RoundAxisRender extends RoundAxis{
	
	private static final String TAG="RoundAxisRender";
	
	private XEnum.Location mLocation = XEnum.Location.BOTTOM;
	//线的风格(点或线之类)
	//private XEnum.LineStyle mLineStyle = XEnum.LineStyle.SOLID;
	//设置线箭头 (三角，方形，棱形....)  
	//private XEnum.DotStyle mLineCap = XEnum.DotStyle.HIDE;
	
	
	public RoundAxisRender()
	{
	}
	
	public void setAxisPercentage(List<Float> angle)
	  {
		  if(null != mPercentage) mPercentage.clear();	
		  if(null == mPercentage) mPercentage = new ArrayList<Float>();			  
		 		  
		  mPercentage = angle;
	  }
	  
	  public void setAxisColor(List<Integer> color)
	  {
		  if(null != mColor) mColor.clear();
		  if(null == mColor) mColor = new ArrayList<Integer>();
		  
		  mColor = color;
	  }
	  
	  public void setAxisLabels(List<String> labels)
	  {
		  if(null != mLabels) mLabels.clear();
		  if(null == mLabels) mLabels = new ArrayList<String>();
		 
		  mLabels = labels;
	  }
	  	  
	  public void setLineAxisLocation( XEnum.Location location)
	  {
		  mLocation = location;
	  }	  	  
	  	 	
	  /**
	   * 绘制标签环形轴标签
	   * @param canvas	画布
	   * @param labels	标签集合
	   * @return	是否绘制成功
	   */
	public boolean renderTicks(Canvas canvas,List<String> labels)
	{		  
		  	float cirX = mCirX;
			float cirY = mCirY; 					
			int count = labels.size();		
			float stepsAngle = 0;
			if(Float.compare(mTotalAngle, 360f) == 0)
			{
				stepsAngle =  MathHelper.getInstance().div(mTotalAngle ,count  ) ;
			}else{
				stepsAngle =  MathHelper.getInstance().div(mTotalAngle ,count -1 ) ;
			}
			float innerRadius1 = mRadius ; 
			float tickRadius = 0.0f,detailRadius  = 0.0f;	
			
			if( XEnum.RoundTickAxisType.INNER_TICKAXIS == mRoundTickAxisType)
			{
				tickRadius = mRadius * 0.95f;
				detailRadius = tickRadius;
							
				//有启用主明细步长设置 (inner)
				if(1 < mDetailModeSteps)
					tickRadius = tickRadius  - mRadius * 0.05f;
				
			}else{
				tickRadius = mRadius + mRadius * 0.05f; 
				detailRadius = tickRadius;
				if(1 < mDetailModeSteps)
					tickRadius = mRadius + mRadius * 0.08f; 
			}
						
			int steps = mDetailModeSteps;
			float Angle = 0.0f;						
			float tickMarkWidth = getTickMarksPaint().getStrokeWidth();
			
			float stopX = 0.0f,stopY = 0.0f;
			float labelX = 0.0f,labelY = 0.0f;
			float startX = 0.0f,startY = 0.0f;
			
			for(int i=0;i<count;i++)
			{					
					if(0 == i)
					{
						Angle = mInitAngle;
					}else{
						//Angle =  MathHelper.getInstance().add(Angle,stepsAngle);
						Angle = MathHelper.getInstance().add(mInitAngle, i * stepsAngle);													
					}									
					
					MathHelper.getInstance().calcArcEndPointXY(cirX, cirY, innerRadius1, Angle); 								
					startX = MathHelper.getInstance().getPosX();
					startY = MathHelper.getInstance().getPosY();
					
					stopX = stopY = 0.0f;
					labelX = labelY = 0.0f;
										
					MathHelper.getInstance().calcArcEndPointXY(cirX, cirY,tickRadius, Angle); 	
					labelX = MathHelper.getInstance().getPosX();
					labelY = MathHelper.getInstance().getPosY();
										
					if(steps == mDetailModeSteps )
					{
						stopX = labelX;
						stopY = labelY;	
						steps = 0;
					}else{
						MathHelper.getInstance().calcArcEndPointXY(cirX, cirY, detailRadius , Angle); 	
						stopX = MathHelper.getInstance().getPosX();
						stopY = MathHelper.getInstance().getPosY();
							
						steps++;						
					}
					
					if (isShowTickMarks()) 
					{						
						if(0 == steps && mLongTickfakeBold )
						{
							getTickMarksPaint().setStrokeWidth( tickMarkWidth + 1);
						}else{
							if(mLongTickfakeBold)getTickMarksPaint().setStrokeWidth(tickMarkWidth);
						}								
						canvas.drawLine(startX, startY, stopX, stopY, getTickMarksPaint());
					}
									
					if (isShowAxisLabels()) 
					{
						//回调函数定制化标签显示格式
						 String label = getFormatterLabel(labels.get(i));							 
						 PointF pLabel = getLabelXY(label,labelX,labelY,cirX,cirY,mTotalAngle,Angle);
						 						
						 //标签显示
						 DrawHelper.getInstance().drawRotateText(label,pLabel.x , pLabel.y,
												getTickLabelRotateAngle(), canvas, getTickLabelPaint());						
					}
										
					
			} //end for
			return true;
	 	}	
	
		 //得到标签显示位置
		 private PointF getLabelXY(String label,float defLabelX,float defLabelY,
				 					float cirX,float cirY,float totalAngle,float Angle)
		 {		
			 PointF pLabel = new PointF(defLabelX,defLabelY);
			 float labelWidth =DrawHelper.getInstance().getTextWidth(getTickLabelPaint(), label);
			 float labelHeight = DrawHelper.getInstance().getPaintFontHeight(getTickLabelPaint());
			 
			 getTickLabelPaint().setTextAlign(Align.CENTER);
			 
			 if( XEnum.RoundTickAxisType.INNER_TICKAXIS == mRoundTickAxisType)
			 {							 
				 if(Float.compare(pLabel.y, cirY) == 0)
				 {
					 if(Float.compare(pLabel.x , cirX) == -1 )
					 {
						 pLabel.x +=  labelWidth/2 ;
					 }else{
						 pLabel.x -=  labelWidth/2 ;
					 }
				 }else if(Float.compare(pLabel.x, cirX) == 0){
					 if(Float.compare(pLabel.y , cirY) == -1 )
					 {
						 pLabel.y += labelHeight/2 ;
					 }else{
						 pLabel.y -=  labelHeight/2 ;
					 }	
						 
				 }else if(Float.compare(totalAngle, Angle) == 0 ){
					 pLabel.y += labelHeight ;								 								 
				 }else if(Float.compare(pLabel.x, cirX) == 1 ){
					 if(Float.compare(totalAngle, 360f) == 0)
					 {
						 getTickLabelPaint().setTextAlign(Align.RIGHT);
					 }else{
						 pLabel.x -=  labelWidth/2 ;
					 }
					 
				 }else if(Float.compare(pLabel.x, cirX) == -1 ){
					 if(Float.compare(totalAngle, 360f) == 0)
					 {
						 getTickLabelPaint().setTextAlign(Align.LEFT);
					 }else{
						 pLabel.x +=  labelWidth/2 ;
					 }
				 } 
			 }else{			 					 
				 if(Float.compare(pLabel.y, cirY) == 0)
				 {
					 if(Float.compare(pLabel.x , cirX) == -1 )
					 {
						 pLabel.x -=  labelWidth/2 ;
					 }else{
						 pLabel.x +=  labelWidth/2 ;
					 }
				 }else if(Float.compare(pLabel.x, cirX) == 0){
					 if(Float.compare(pLabel.y , cirY) == -1 )
					 {
						 pLabel.y -= labelHeight/2 ;
					 }else{
						 pLabel.y +=  labelHeight/2 ;
					 }						 
				 }else if(Float.compare(totalAngle, Angle) == 0 ){
					 pLabel.y -= labelHeight ;								 								 
				 }else if(Float.compare(pLabel.x, cirX) == 1 ){
					 if(Float.compare(totalAngle, 360f) == 0)
					 {
						 getTickLabelPaint().setTextAlign(Align.LEFT);
					 }else{
						 pLabel.x +=  labelWidth/2 ;
					 }
				 }else if(Float.compare(pLabel.x, cirX) == -1 ){
					 if(Float.compare(totalAngle, 360f) == 0)
					 {
						 getTickLabelPaint().setTextAlign(Align.RIGHT);
					 }else{
						 pLabel.x -=  labelWidth/2 ;
					 }
				 } 
				 
			 }	
			 return pLabel;		
		}

	
	 	//fillAxis
	/**
	 * 绘制填充环形轴
	 * @param canvas 画布
	 * @return 是否成功
	 * @throws Exception 例外
	 */
		public boolean renderFillAxis(Canvas canvas) throws Exception
		{			
			if(isShow() && isShowAxisLine())
			{
				if(null != mColor)
					getFillAxisPaint().setColor(mColor.get(0));
				
				DrawHelper.getInstance().drawPercent(canvas, this.getFillAxisPaint(),
								mCirX, mCirY, mRadius, mInitAngle, mTotalAngle, true);		
			}
			return true;
		}
		
		/**
		 * 绘制标签环形轴
		 * @param canvas
		 * @return
		 * @throws Exception
		 */
		public boolean renderTickAxis(Canvas canvas) throws Exception
		{			
			if(!isShow()) return false;			
			if(null == mLabels) return false;
						
			if(isShowAxisLine())
			{
				DrawHelper.getInstance().drawPathArc(canvas, this.getAxisPaint(),
								this.mCirX,this.mCirY,this.mRadius,this.mInitAngle, this.mTotalAngle);							
			}
			
			return renderTicks(canvas,this.mLabels);
		}
		
		//arcline
		/**
		 * 绘制弧形环形轴
		 * @param canvas
		 * @return
		 * @throws Exception
		 */
		public boolean renderArcLineAxis(Canvas canvas) throws Exception
		{			
			if(isShow() && isShowAxisLine())
			{
				DrawHelper.getInstance().drawPathArc(canvas, this.getAxisPaint() 
								,mCirX, mCirY, mRadius,this.mInitAngle, this.mTotalAngle);
			}
			return true;
		}
		
		public boolean renderCircleAxis(Canvas canvas) throws Exception
		{			
			if(isShow() && isShowAxisLine())
			{
				if(null != mColor)
					getAxisPaint().setColor(mColor.get(0));
				
				 canvas.drawCircle(mCirX, mCirY, mRadius,  this.getAxisPaint());
			}
			return true;
		}
		
		
		/**
		 * 绘制颜色块环形轴
		 * @param canvas 画布
		 * @return  结果
		 * @throws Exception 例外
		 */
		public boolean renderRingAxis(Canvas canvas) throws Exception
		{
			if(!isShow()|| !isShowAxisLine()) return true;
			
			if(null == mPercentage) return false;
									
			int angleCount = 0,colorCount = 0,labelsCount = 0;	
			
			 angleCount = this.mPercentage.size();
			 if(null != mColor)colorCount = this.mColor.size();
			 if(null != mLabels)labelsCount = this.mLabels.size();
			
			float offsetAngle = this.mInitAngle;
			int currentColor = -1;
			String currentLabel = "";
			float sweepAngle = 0.0f;
			
			for(int i=0;i<angleCount;i++)
			{				
				if(null != mColor && colorCount > i) currentColor = mColor.get(i);
				if(null != mLabels && labelsCount > i)currentLabel = mLabels.get(i);				
				sweepAngle = MathHelper.getInstance().mul( mTotalAngle , mPercentage.get(i));				
				
				renderPartitions(canvas,offsetAngle,sweepAngle,currentColor,currentLabel) ;
				offsetAngle = MathHelper.getInstance().add(offsetAngle, sweepAngle);
				currentColor = -1;
				currentLabel = "";
			}
			
			if(Float.compare(getRingInnerRadiusPercentage() , 0.0f) != 0 
					&& Float.compare(getRingInnerRadiusPercentage() , 0.0f) == 1)
			{									
				canvas.drawCircle(this.mCirX, mCirY, 
									getRingInnerRadius(), this.getFillAxisPaint());
			}
						
			return true;
		}
						
		
		/**
		 * 绘制颜色轴
		 * @throws Exception
		 */
		private boolean renderPartitions(Canvas canvas,float startAngle,float sweepAngle,
														int color,String label) throws Exception
		{		
				     
			//if(color >= -1) 
				getAxisPaint().setColor(color);		   		     
			
			 if(Float.compare(sweepAngle, 0.0f) < 0){
					Log.e(TAG,"负角度???!!!");
					return false;
			 }else if(Float.compare(sweepAngle, 0.0f) == 0){
				 	Log.w(TAG,"零角度???!!!");
					return true;
			 }			 
			 
			 
			DrawHelper.getInstance().drawPercent(canvas, this.getAxisPaint(),
					 						this.mCirX, this.mCirY,mRadius, 
					 						startAngle, sweepAngle, true);
			 			 
			if (isShowAxisLabels() && ""!= label) 
			{
			 	float Angle = MathHelper.getInstance().add(startAngle , sweepAngle / 2) ; 
			 	MathHelper.getInstance().calcArcEndPointXY(this.mCirX, this.mCirY,
			 														mRadius * 0.5f,Angle ); 	
			 	
			 	float labelX = MathHelper.getInstance().getPosX();
			 	float labelY = MathHelper.getInstance().getPosY();					
					 						
				//定制化显示格式	 Angle* -2
				DrawHelper.getInstance().drawRotateText(getFormatterLabel(label) ,labelX , labelY,
										getTickLabelRotateAngle(), canvas, getTickLabelPaint());		
			}
			 
			 return true;		 
		}
		
		
		/**
		 * 设置线的风格(点或线之类)
		 * @param style 线的风格
		 */
		/*
		public void setLineStyle(XEnum.LineStyle  style)
		{
			mLineStyle = style;
		}
		
		// 设置线箭头 (三角，方形，棱形....)  
		public void setLineCap(XEnum.DotStyle style) {
			this.mLineCap = style;
		}
		*/
		
		/**
		 * 中心点的线轴
		 * @param canvas 画布
		 * @return	结果
		 * @throws Exception 例外
		 */
		public boolean renderLineAxis(Canvas canvas) throws Exception
		{
			 if(!isShow()|| !isShowAxisLine()) return true;		
			 switch(mLocation)
			 {
			 case TOP:					 
				 canvas.drawLine(mCirX, mCirY, mCirX, mCirY - mRadius , this.getAxisPaint());				 
				 break;
			 case BOTTOM:			
				 canvas.drawLine(mCirX, mCirY, mCirX, mCirY + mRadius , this.getAxisPaint());
				 break;
			 case LEFT:		
				 canvas.drawLine(mCirX, mCirY, mCirX  - mRadius , mCirY , this.getAxisPaint());
				 break;
			 case RIGHT:	
				 canvas.drawLine(mCirX, mCirY, mCirX + mRadius , mCirY , this.getAxisPaint());
				 break;
			 default:
				 return false;
			 }
			return true;
		}
		
		/**
		 * 圆心坐标
		 * @param x x坐标
		 * @param y	y坐标
		 */
		public void setCenterXY(float x,float y)
		{
			mCirX = x;
			mCirY = y;
		}
		 
		/**
		 * Ploat范围半径
		 * @param radius
		 */
		public void setOrgRadius(float radius)
		{
			mOrgRadius = radius;
		}
	
		/**
		 * 指定角度及偏移  		  
		 * @param totalAngle	总角度
		 * @param initAngle		偏移
		 */
		public void setAngleInfo(float totalAngle,float initAngle)
		{
			mTotalAngle = totalAngle;
			mInitAngle = initAngle;
		}		  
		 		  
		
		/**
		 * 绘制图表
		 * @param canvas	画布
		 * @return	是否成功
		 * @throws Exception	例外
		 */
		public boolean render(Canvas canvas) throws Exception
		{
				boolean ret = false;
				
				mRadius = getOuterRadius(); 
				
				// TICKAXIS,RINGAXIS,LENAXIS
				 switch(getAxisType())
				 {
				 case TICKAXIS:					
					 ret = renderTickAxis(canvas);
					 break;
				 case RINGAXIS:					
					 ret = renderRingAxis(canvas);
					 break;
				 case ARCLINEAXIS:					 
					 ret = renderArcLineAxis(canvas);					 
					 break;
				 case FILLAXIS:					 
					 ret = renderFillAxis(canvas);					 
					 break;				
				 case CIRCLEAXIS:	
					 ret = renderCircleAxis(canvas);	
					 break;
				 case LINEAXIS:	
					 ret = renderLineAxis(canvas);	
					 break;	 
				 default:
					 break;
				 }
			 return ret;
		}
	
	

}
