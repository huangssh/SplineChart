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
package org.xclcharts.renderer.line;

/**
 * @ClassName DotInfo
 * @Description  用于暂存点信息的类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class DotInfo {
	
	public Double mValue = 0d;
	
	public Double mXValue = 0d;
	public Double mYValue = 0d;
			
	public float mX = 0.f;
	public float mY = 0.f;
	
	public DotInfo(){}
	
	public DotInfo(Double value,float x,float y)
	{
		 mValue = value ;
		 mX = x ;
		 mY = y ;			
	}
	
	
	public DotInfo(Double xValue,Double yValue,float x,float y)
	{
		 mXValue = xValue ;
		 mYValue = yValue ;
		 mX = x ;
		 mY = y ;			
	}
	
	public String getLabel()
	{
		return Double.toString(mXValue)+","+ Double.toString(mYValue);
	}

}
