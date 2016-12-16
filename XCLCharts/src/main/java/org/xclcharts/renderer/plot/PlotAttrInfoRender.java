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
 * @version 1.5
 */
package org.xclcharts.renderer.plot;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * @ClassName PlotAttrInfoRender
 * @Description 图的附加信息绘制类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * 
 */
public class PlotAttrInfoRender extends PlotAttrInfo{
	
	PointF mPosPintF = new PointF();
	
	
	public PlotAttrInfoRender()
	{
	}
	
	 
	/**
	 * 绘制附加信息
	 * @param canvas		画布
	 * @param centerX		绘图区中心点X坐标
	 * @param centerY		绘图区中心点Y坐标
	 * @param plotRadius	当前半径
	 */
	public void renderAttrInfo(Canvas canvas,float centerX,float centerY,float plotRadius)
	{		
		if(null == mAttrInfo) return ;
		if(null == mAttrInfoLocation) return ;
		float radius = 0.0f; 
		String info = "";		
		
		for(int i=0;i<mAttrInfo.size();i++)
		{
			info = mAttrInfo.get(i);
			if("" == info) continue;
			
			if(null == mAttrInfoPostion || mAttrInfoPostion.size() < i)continue;	
			if(null == mAttrInfoPaint.get(i) || mAttrInfoPaint.size() < i) continue;
			
			mPosPintF.x =  centerX;
			mPosPintF.y =  centerY;
			
			radius = plotRadius * mAttrInfoPostion.get(i);
			switch(mAttrInfoLocation.get(i))
			{
				case TOP:
					mPosPintF.y =  centerY - radius;
					break;
				case BOTTOM:
					mPosPintF.y =  centerY + radius;
					break;
				case LEFT:
					mPosPintF.x =  centerX - radius;
					break;
				case RIGHT:
					mPosPintF.x =  centerX + radius;
					break;
			}	    							
			canvas.drawText(info, mPosPintF.x, mPosPintF.y, mAttrInfoPaint.get(i));
		}
		
	}

}
