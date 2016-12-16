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

import android.graphics.PointF;

/**
 * @ClassName PlotArcLabelInfo
 * @Description 用于保存标签数据信息的类
 * 
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com) 
 *         
 */

public class PlotArcLabelInfo extends PlotDataInfo{
		
		public float Radius = 0.0f;
				
		public float OffsetAngle = 0.0f;
		public float CurrentAngle = 0.0f;
		
		private PointF mLabelPointF = null;
						
		public PlotArcLabelInfo(){};
		
		
		public PlotArcLabelInfo(int id,float x,float y,
								float radius,float offsetAngle,float currentAngle)
		{
			 ID = id;
			 X = x;
			 Y = y;
			 Radius = radius;
			 OffsetAngle = offsetAngle;
			 CurrentAngle = currentAngle;
		}


		public float getRadius() {
			return Radius;
		}


		public void setRadius(float radius) {
			Radius = radius;
		}


		public float getOffsetAngle() {
			return OffsetAngle;
		}


		public void setOffsetAngle(float offsetAngle) {
			OffsetAngle = offsetAngle;
		}


		public float getCurrentAngle() {
			return CurrentAngle;
		}


		public void setCurrentAngle(float currentAngle) {
			CurrentAngle = currentAngle;
		}


		public PointF getLabelPointF() {
			return mLabelPointF;
		}


		public void setLabelPointF(PointF point) {
			this.mLabelPointF = point;
		};
		
		

}
