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
 * @ClassName PlotDataInfo
 * @Description 用于保存图表一些数据信息的基类
 * 
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com) 
 *         
 */

public class PlotDataInfo {
		
		//坐标
		public float X = 0.0f;
		public float Y = 0.0f;
				
		//标签
		public String Label = "";
		
		//将当前为第几个tick传递轴，用以区分是否为主明tick
		public int ID = -1;
				
		
		public float labelX = 0.0f;
		public float labelY = 0.0f;
		
		
		public PlotDataInfo(){};
		
		public PlotDataInfo(float x,float y,String label)
		{
			 X = x;
			 Y = y;
			 Label = label;
			 labelX = x;
			 labelY = y;
		};
		
		public PlotDataInfo(int id,float x,float y,String label)
		{
			 ID = id;
			 X = x;
			 Y = y;
			 Label = label;
			 
			 labelX = x;
			 labelY = y;
		}
		
		public PlotDataInfo(int id,float x,float y,String label,float lx,float ly)
		{
			 ID = id;
			 X = x;
			 Y = y;
			 Label = label;
			 
			 labelX = lx;
			 labelY = ly;
		}

		public float getX() {
			return X;
		}

		public void setX(float x) {
			X = x;
		}

		public float getY() {
			return Y;
		}

		public void setY(float y) {
			Y = y;
		}

		public String getLabel() {
			return Label;
		}

		public void setLabel(String label) {
			Label = label;
		}

		public int getID() {
			return ID;
		}

		public void setID(int iD) {
			ID = iD;
		};
		
		

}
