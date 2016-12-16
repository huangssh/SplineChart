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
package org.xclcharts.event.click;

import android.graphics.RectF;

/**
 * @ClassName PlotBarPosition
 * @Description  bar位置记录信息类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */


public class PlotBarPosition extends BarPosition{
	
	public PlotBarPosition()
	{	
	}	
	
	//当前记录在数据源中行号
	public void savePlotDataID(int num)
	{
		saveDataID(num);
	}

	//当前记录所属数据集的行号
	public void savePlotDataChildID(int num)
	{
		saveDataChildID(num);
	}	
		
	
	public void savePlotRectF(float left,float top,float right,float bottom)
	{		
		saveRectF(left, top, right, bottom);
	}
	
	public void savePlotRectF(RectF r)
	{
		saveRectF(r);
	}
	
	public  boolean compareF(float x, float y) 
	{
		// TODO Auto-generated method stub
		
		return compareRange(x,y);	
	}			
	
	

}
