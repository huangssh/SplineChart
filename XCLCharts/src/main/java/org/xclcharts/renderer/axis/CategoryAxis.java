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


/**
 * @ClassName CategoryAxisRender
 * @Description 分类轴(Category Axis)类，设置步长
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class CategoryAxis extends XYAxis {
	
	//分类轴分隔值	
	private double  mAxisSteps = 0.0d;
	
	private boolean mAxisBindStd = false;
	
	public CategoryAxis()
	{
		
	}
	
	/**
	 * 设置分类轴步长
	 * @param steps 步长
	 */
	public void setAxisSteps(double steps)
	{
		 mAxisSteps = steps;
	}
	
	/**
	 * 返回分类轴步长
	 * @return 步长
	 */
	public double getAxisSteps()
	{
		return mAxisSteps;
	}
	
	/**
	 * 是否将分类轴与数据轴的正负标准值绑定, 
	 * 如绑定，则轴会显示在标准值所在位置
	 * @param status 设置状态
	 */
	public void setAxisBuildStd(boolean status)
	{
		mAxisBindStd = status;
	}
	
	/**
	 * 分类轴与数据轴的正负标准值绑定状态 
	 * @return 状态
	 */
	public boolean getAxisBuildStdStatus()
	{
		return mAxisBindStd;
	}
	

}
