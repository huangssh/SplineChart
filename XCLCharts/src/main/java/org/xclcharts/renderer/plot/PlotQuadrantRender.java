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
 * @version 2.1
 */
package org.xclcharts.renderer.plot;

import android.graphics.Canvas;

/**
 * @ClassName PlotQuadrantRender
 * @Description 四象限绘制类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *
 */
public class PlotQuadrantRender extends PlotQuadrant{
	
	public PlotQuadrantRender(){}
	
	public void drawQuadrant(Canvas canvas,
			float centerX,float centerY,
			float left,float top,float right,float bottom)
	{
		
		if(mShowBgColor) 	//绘制bg
		{		
			getBgColorPaint().setColor(mFirstColor);
			canvas.drawRect(centerX, top, right, centerY,  getBgColorPaint());
			
			getBgColorPaint().setColor(mSecondColor);
			canvas.drawRect( centerX, centerY, right, bottom, getBgColorPaint());
			
			getBgColorPaint().setColor(mThirdColor);
			canvas.drawRect(left, centerY,centerX ,bottom ,  getBgColorPaint());
			
			getBgColorPaint().setColor(mFourthColor);
			canvas.drawRect(left, top,centerX, centerY,  getBgColorPaint());
		}
		
		if(mShowVerticalLine)
		{
			canvas.drawLine(centerX, top, centerX, bottom,  getVerticalLinePaint());
		}
		
		if(mShowHorizontalLine)
		{
			canvas.drawLine(left, centerY, right, centerY,  getVerticalLinePaint());
		}				
	}
	

}
