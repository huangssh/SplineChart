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

import java.util.List;

import org.xclcharts.renderer.XEnum;


/**
 * @ClassName SplineData
 * @Description 数据类, 曲线图用这个传数据
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class SplineData extends LnData{

	
			//线上每个点的值
			private List<PointD> mLinePointMap ;	
			
			//标签文字旋转角度
			private float mItemLabelRotateAngle = 0.0f;	
			
			public SplineData(){}
			
			/**
			 * 构成一条完整的线条
			 * @param lineKey 	 对应的键值		
			 * @param dataSeries  对应的数据序列
			 * @param color  线条颜色
			 */
			public SplineData(String lineKey,					
							List<PointD> dataSeries,
							int color ) {
				// TODO Auto-generated constructor stub
			
				setLineKey(lineKey);			
				setLineDataSet(dataSeries);
				setLineColor(color );			
			}	
			
			/**
			 * 
			 * @param lineKey	key值
			 * @param dataSeries 对的数据序列
			 * @param color		线的颜色
			 * @param dotStyle  坐标点绘制类型
			 */
			public SplineData(String lineKey,					
							List<PointD> dataSeries,
							int color,
							XEnum.DotStyle  dotStyle) {
				// TODO Auto-generated constructor stub
			
				setLineKey(lineKey);			
				setLineDataSet(dataSeries);
				
				setLineColor(color );			
				setDotStyle(dotStyle);		
			}
			
			/**
			 * 设置绘制线的数据序列,由x与y坐标构建
			 * @param dataSeries <X坐标值，Y坐标值>
			 */
			public void setLineDataSet( List<PointD>  dataSeries) 
			{
				mLinePointMap = dataSeries;
			}
			
			/**
			 * 返回绘制线的数据序列
			 * @return 线的数据序列<X坐标值，Y坐标值>
			 */
			public List<PointD> getLineDataSet() {
				return mLinePointMap;
			}
			
			/**
			 * 返回标签在显示时的旋转角度
			 * @return 旋转角度
			 */
			public float getItemLabelRotateAngle() {
				return mItemLabelRotateAngle;
			}

			/**
			 * 设置标签在显示时的旋转角度
			 * @param rotateAngle 旋转角度
			 */
			public void setItemLabelRotateAngle(float rotateAngle) {
				this.mItemLabelRotateAngle = rotateAngle;
			}
			
			
}
