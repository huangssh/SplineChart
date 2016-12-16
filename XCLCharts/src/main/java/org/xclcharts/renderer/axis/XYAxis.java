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
package org.xclcharts.renderer.axis;

import java.util.List;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;

import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Path;

/**
 * @ClassName XYAxis
 * @Description XY坐标系类，定义了具体的绘制及格式回调函数的处理
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * 
 */

public class XYAxis extends Axis {
	
	// 数据集
	protected List<String> mDataSet = null;

	// 用于格式化标签的回调接口
	private IFormatterTextCallBack mLabelFormatter;
	
	//标签显示位置，分别在轴的左边，中间，右边
	private Align mTickMarksAlign  = Align.RIGHT;
	
	//标签显示位置，分别在轴的上面，中间，底下
	private XEnum.VerticalAlign mTickMarksPosition =  XEnum.VerticalAlign.BOTTOM;
	
	 //默认刻度线所占宽度
	private int mTickMarksLength = 15;	
	
	//刻度标记与轴的间距
	private int mTickLabelMargin = 10;	
		
	protected boolean mShowAxisLineStyle = true;
	
	private XEnum.AxisLineStyle mAxisLineStyle = XEnum.AxisLineStyle.NONE;
	
	private float mAxisLineStyleWidth = 20.f;
	private float mAxisLineStyleHeight = 30.f;
	
	// 标签按哪种风格显示
	protected XEnum.LabelLineFeed mLineFeed = XEnum.LabelLineFeed.NORMAL;

	public XYAxis() {
		
		//设置轴线条粗细
		//this.getAxisPaint().setStrokeWidth(5);
	}
	
	/**
	 *  设置时刻度显示在上，中，下哪个地方,针对横轴
	 * @param position 上方，居中，下方
	 */
	public void setVerticalTickPosition(XEnum.VerticalAlign position)
	{
		mTickMarksPosition = position;
	}
	
	/**
	 * 返回轴上刻度线显示的位置
	 * @return 位置
	 */
	public XEnum.VerticalAlign getVerticalTickPosition()
	{
		return mTickMarksPosition;
	}
	
	
	/**
	 * 设置刻度靠左，中，右哪个位置显示,针对竖轴
	 * @param align 靠左，居中，靠右
	 */
	public void setHorizontalTickAlign(Align align)
	{
		mTickMarksAlign = align;
	}
	
	public Align getHorizontalTickAlign()
	{
		return mTickMarksAlign;
	}
	

	/**
	 * 设置标签的显示格式
	 * @param callBack 回调函数
	 */
	public void setLabelFormatter(IFormatterTextCallBack callBack) {
		this.mLabelFormatter = callBack;
	}
	
	/**
	 * 返回标签显示格式
	 * 
	 * @param value 传入当前值
	 * @return 显示格式
	 */
	protected String getFormatterLabel(String text) {
		String itemLabel = "";
		try {
			itemLabel = mLabelFormatter.textFormatter(text);
		} catch (Exception ex) {
			itemLabel = text;
		}
		return itemLabel;
	}
	
	
	/**
	 * 设置横轴标签换行方式
	 *    NORMAL: 不换行 <br\>
	 *    ODD_EVEN: 奇数位置标签先换行 <br/>
	 *    EVEN_ODD：偶数位置标签先换行
	 * @param lineFeed
	 */
	public void setLabelLineFeed(XEnum.LabelLineFeed lineFeed){
		this.mLineFeed = lineFeed;
	}
	
		
	/**
	 * 竖轴坐标标签，依左，中，右，决定标签横向显示在相对中心点的位置
	 * @param centerX 轴上中点X坐标
	 * @param centerY 轴上中点X坐标
	 * @param text    标签文本
	 */

	// odd-even
	protected void renderHorizontalTick(float chatLeft,float plotLeft,
			Canvas canvas, 
			float centerX, float centerY,
			String text,float labelX, float labelY,
			boolean isTickVisible) {

		if (false == isShow())return;
		float marksStartX = centerX;
		float markeStopX = centerX;
		
		float labelStartX = labelX;
		float labelStartY = labelY;

		switch (getHorizontalTickAlign()) {
		case LEFT: {
			if (isShowTickMarks()) {
				marksStartX = MathHelper.getInstance().sub(centerX,getTickMarksLength()); 
				markeStopX = centerX;				
			}
			
			if(this.isShowAxisLabels())
				labelStartX = MathHelper.getInstance().sub(marksStartX , getTickLabelMargin());															
			break;
		}
		case CENTER: {
			if (isShowTickMarks()) {
				marksStartX = MathHelper.getInstance().sub(centerX , getTickMarksLength() / 2);
				markeStopX = MathHelper.getInstance().add(centerX , getTickMarksLength() / 2);
			}
			break;
		}
		case RIGHT:
			if (isShowTickMarks()) {
				marksStartX = centerX;
				markeStopX = MathHelper.getInstance().add(centerX , getTickMarksLength());				
			}
			if(this.isShowAxisLabels())
				labelStartX = MathHelper.getInstance().add(markeStopX , getTickLabelMargin());
			
			break;
		default:
			break;
		}
		
		//横轴刻度线
		if (isShowTickMarks() && isTickVisible) 
		{				
				canvas.drawLine(marksStartX, centerY, 
						MathHelper.getInstance().add(markeStopX, 
									this.getAxisPaint().getStrokeWidth() / 2),
						centerY,
						getTickMarksPaint());				
		}

		//标签
		//  当标签文本太长时，可以考虑分成多行显示如果实在太长，则开发用...来自己处理
		if (isShowAxisLabels()) {			
			renderHorizontalTickLabels(chatLeft,plotLeft,canvas,
					 	 labelStartX,labelStartY,markeStopX,text );				
		}
				
	}
	
	//画标签
	private void renderHorizontalTickLabels(float chatLeft,float plotLeft,Canvas canvas,
								float labelStartX,float labelStartY,
								float markeStopX,
								String text   )
	{
		float labelHeight = DrawHelper.getInstance().getPaintFontHeight(
				getTickLabelPaint());
		float textHeight  = labelHeight/4;
		if(Align.LEFT == getHorizontalTickAlign()) //处理多行标签
		{
			float width = 0.0f;
			if (isShowTickMarks()) {
				width = markeStopX - chatLeft;
			}else{				
				width = MathHelper.getInstance().sub(plotLeft ,chatLeft);					
			}
			renderLeftAxisTickMaskLabel(canvas,labelStartX, labelStartY + textHeight,text,width );				
		}else{		
			DrawHelper.getInstance().drawRotateText(
					getFormatterLabel(text), labelStartX,labelStartY + textHeight,
					getTickLabelRotateAngle(), canvas,getTickLabelPaint());	
		}
	}
	

	/**
	 * 横轴坐标标签，决定标签显示在相对中心点的上方，中间还是底部位置
	 * @param centerX	轴上中点X坐标
	 * @param centerY	轴上中点Y坐标
	 * @param text		标签文本
	 */
	protected void renderVerticalTick(Canvas canvas, 
									 float centerX, float centerY,  
									 String text,float labelX, float labelY, 
									 boolean isTickVisible,XEnum.ODD_EVEN oe) {
		if (!isShow())return;

		float marksStartY = centerY;
		float marksStopY = centerY;
		float labelsStartY = labelY;				
		
		switch (getVerticalTickPosition()) {
		case TOP: {
			
			if (isShowTickMarks()){
				marksStartY = MathHelper.getInstance().sub(centerY , getTickMarksLength());
				marksStopY = centerY;				
			}			
			marksStartY = MathHelper.getInstance().sub(centerY , getTickMarksLength());
			marksStopY = centerY;				
			break;
		}
		case MIDDLE: {
			if (isShowTickMarks()) {
				marksStartY = MathHelper.getInstance().sub(centerY , getTickMarksLength() / 2);
				marksStopY = MathHelper.getInstance().add(centerY , getTickMarksLength() / 2);				
			}
			break;
		}
		case BOTTOM:

			if (isShowTickMarks()) {
				marksStartY = centerY;
				//marksStopY = Math.round(centerY + getTickMarksLength());		
				marksStopY =  MathHelper.getInstance().add(centerY , getTickMarksLength());
			}
			
			if(this.isShowAxisLabels())
			{
				labelsStartY = marksStopY + getTickLabelMargin()
						+ DrawHelper.getInstance().getPaintFontHeight(getTickLabelPaint())
						/ 3;
			}
			break;
		default:
			break;
		}

		
		if (isShowTickMarks() && isTickVisible) {
			float mstartX = MathHelper.getInstance().sub(marksStartY, 
								getAxisPaint().getStrokeWidth() /2 ) ;
			
			//if( Float.compare(mstartX, xchart.getPlotArea().getLeft()) == -1) ||
			//		Float.compare(mstartX, xchart.getPlotArea().getRight()) == 1 	)
			//{				
			//}else{	
			canvas.drawLine(centerX,					
					mstartX,
				//	marksStartY - this.getAxisPaint().getStrokeWidth() / 2,					
					centerX,
					marksStopY, getTickMarksPaint());
			//}
		}
		
	
		if (isShowAxisLabels()) {
			
			float labelHeight = DrawHelper.getInstance().getPaintFontHeight(
					getTickLabelPaint());
		
			float currY = labelsStartY ;							
			if(this.mLineFeed != XEnum.LabelLineFeed.NORMAL){																					
				switch(this.mLineFeed)
				{
				case ODD_EVEN:
					if(oe == XEnum.ODD_EVEN.ODD)currY +=  labelHeight  ;			
					break;
				case EVEN_ODD:
					if(oe == XEnum.ODD_EVEN.EVEN)currY +=  labelHeight  ;	
					break;
				case NORMAL:
					break;
				}
			}
			
			//定制化显示格式			
			DrawHelper.getInstance().drawRotateText(getFormatterLabel(text),
					labelX, currY, //labelsStartY,
					getTickLabelRotateAngle(), canvas,
					getTickLabelPaint());
			//Log.e("t2","2angle:"+Float.toString(getTickLabelRotateAngle()));
		}		
	}
	
	
	//只针对renderHorizontalTick()，处理标签文字太长，分多行显示的情况,
	// 即只有在竖向图左轴，并且标签靠左显示时，才会处理这个问题
	private void renderLeftAxisTickMaskLabel(Canvas canvas, 
											float centerX, float centerY, String text,float width)
	{
		if(!isShowAxisLabels())return;
			
		//格式化后的标签文本.  
		 String label =  getFormatterLabel(text);
		
		//横向找宽度，竖向找高度，竖向的就不处理了。只搞横向多行的
		double txtLength = DrawHelper.getInstance().getTextWidth(getTickLabelPaint(), label);
		if(txtLength <= width) 
		{
			//标签绘制在一行中
     	   DrawHelper.getInstance().drawRotateText(label,centerX, centerY,
								getTickLabelRotateAngle(), canvas,getTickLabelPaint());
		}else{	//Multi line			
			 float txtHeight = DrawHelper.getInstance().getPaintFontHeight(getTickLabelPaint());						
	         float charWidth =0.0f,totalWidth = 0.0f;
	         float renderY = centerY;
	         String lnString = "";
	         
	         for(int i= 0; i< label.length();i++)
	         {        	 	        	 
	        	 charWidth = DrawHelper.getInstance().getTextWidth(
	        			 						getTickLabelPaint(),label.substring(i, i+1));    
	        			 						
	        	 totalWidth = MathHelper.getInstance().add(totalWidth,charWidth);	        	        
	      		 if( Float.compare(totalWidth , width) == 1 )
	      		 {
		        	   DrawHelper.getInstance().drawRotateText(lnString,centerX, renderY,
							getTickLabelRotateAngle(), canvas,getTickLabelPaint());
		        	   
		        		totalWidth = charWidth;		      		 			   	           	
		        		renderY = MathHelper.getInstance().add(renderY,txtHeight);		      		 		      		 
		      		 	lnString = label.substring(i, i+1) ; 
	      		 }else{
	      			 lnString +=  label.substring(i, i+1) ; 
	      		 }
				 		 
	         } //end for
	         
	         if(lnString.length() > 0 )
	         {
	        	 DrawHelper.getInstance().drawRotateText(lnString,centerX, renderY,
	        			 		getTickLabelRotateAngle(), canvas,getTickLabelPaint());
	         }
	         	         
		} //end width if
	}
	

	/**
	 * 返回轴刻度线长度
	 * @return 刻度线长度
	 */
	public int getTickMarksLength()
	{
		return mTickMarksLength;
	}
	
	
	/**
	 * 设置轴刻度线与标签间的间距
	 * @param margin 间距
	 */
	public void setTickLabelMargin(int margin)
	{
		mTickLabelMargin = margin;
	}

	/**
	 * 返回轴刻度线与标签间的间距
	 * @return 间距
	 */
	public int getTickLabelMargin()
	{
		return mTickLabelMargin;
	}
	
	/**
	 * 设置轴线上三角箭头属性
	 * @param width  箭头宽度
	 * @param hieght 箭头高度
	 */
	public void setAxisLinxCapWH(float width,float hieght)
	{
		mAxisLineStyleWidth = width;
		mAxisLineStyleHeight = hieght;
	}
	
	/**
	 * 设置轴线风格
	 * @param style 风格
	 */
	public void setAxisLineStyle(XEnum.AxisLineStyle style)
	{
		mAxisLineStyle = style;
	}
	
	//绘制轴线
	protected void drawAxisLine(Canvas canvas, float startX,float startY,float stopX,float stopY)
	{				
		if(XEnum.AxisLineStyle.CAP == mAxisLineStyle 
				|| XEnum.AxisLineStyle.FILLCAP == mAxisLineStyle )
		{
			float w = mAxisLineStyleWidth / 2;
			float angleTop = 0.f;
			float axisEnd =mAxisLineStyleHeight/2;
			
			Path path = new Path();
			if(Float.compare(startY, stopY) != 0) //竖轴
			{
				angleTop = stopY - mAxisLineStyleHeight;			
				float angleLeftX = stopX - w;
				float angleRightX = stopX + w;
				
				axisEnd = angleTop + axisEnd;				
				if(XEnum.AxisLineStyle.FILLCAP == mAxisLineStyle)
				{
					path.moveTo(angleLeftX, axisEnd);
					path.lineTo(stopX, angleTop);
					path.lineTo(angleRightX, axisEnd);
					path.close();
					canvas.drawPath(path, getAxisPaint());
					canvas.drawLine(startX, startY, stopX,axisEnd, this.getAxisPaint());
				}else{
					canvas.drawLine(startX, startY, stopX, angleTop, this.getAxisPaint());					
					canvas.drawLine(angleLeftX, axisEnd, stopX, angleTop, this.getAxisPaint());
					canvas.drawLine(angleRightX, axisEnd, stopX, angleTop, this.getAxisPaint());						
				}											
			}else{				
				angleTop = stopX + mAxisLineStyleHeight;
				float angleTopY = stopY - w;
				float angleBottom = stopY + w;		
				
				axisEnd = angleTop - axisEnd;				
				if(XEnum.AxisLineStyle.FILLCAP == mAxisLineStyle)
				{
					path.moveTo(axisEnd, angleTopY);
					path.lineTo(angleTop, stopY);
					path.lineTo(axisEnd, angleBottom);
					path.close();
					canvas.drawPath(path, getAxisPaint());					
					canvas.drawLine(startX, startY, axisEnd,stopY , this.getAxisPaint());					
				}else{			
					canvas.drawLine(startX, startY, angleTop,stopY , this.getAxisPaint());
					canvas.drawLine(axisEnd, angleTopY, angleTop, stopY, this.getAxisPaint());
					canvas.drawLine(axisEnd, angleBottom, angleTop, stopY, this.getAxisPaint());						
				}
			}									
		}else{ //none
			canvas.drawLine(startX, startY, stopX, stopY, this.getAxisPaint());
		}
			
	}
}
