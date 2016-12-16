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
package org.xclcharts.renderer.info;


/**
 * @ClassName PlotAxisTick
 * @Description  用于记录轴刻度的位置信息
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class PlotAxisTick extends PlotDataInfo{
	
	private boolean mShowTickMarks = true;
	
	public PlotAxisTick(){}
	
	
	public PlotAxisTick(float x,float y,String label)
	{
		 X = x;
		 Y = y;
		 Label = label;
		 labelX = x;
		 labelY = y;
	};
	
	public PlotAxisTick(int id,float x,float y,String label)
	{
		 ID = id;
		 X = x;
		 Y = y;
		 Label = label;
		 
		 labelX = x;
		 labelY = y;
	}
	
	public PlotAxisTick(float x,float y,String label,float lx,float ly)
	{
		// ID = id;
		 X = x;
		 Y = y;
		 Label = label;
		 
		 labelX = lx;
		 labelY = ly;
	}
	
	public PlotAxisTick(float x,float y,String label,float lx,float ly,boolean tickMarks)
	{
		// ID = id;
		 X = x;
		 Y = y;
		 Label = label;
		 
		 labelX = lx;
		 labelY = ly;
		 
		 mShowTickMarks = tickMarks;
	}
	
	
	
	public float getLabelX() 
	{
		return labelX;
	}

	public void setLabelX(float x) 
	{
		labelX = x;
	}

	public float getLabelY() 
	{
		return labelY;
	}

	public void setLabelY(float y) 
	{
		labelY = y;
	}
	
	public boolean isShowTickMarks()
	{
		return mShowTickMarks;
	}

}
