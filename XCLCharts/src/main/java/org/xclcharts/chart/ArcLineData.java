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
package org.xclcharts.chart;

import org.xclcharts.common.MathHelper;

import android.util.Log;


/**
 * @ClassName ArcLineData
 * @Description  弧线比较图数据类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class ArcLineData {

	private final String TAG ="RoundBarData";

	private String mKey = "";
	private String mLabel = "";
	private double mValue = 0.0f;
	private int mColor = 0 ;

	
	public ArcLineData() {
		// TODO Auto-generated constructor stub
		//super();
	}
		
	/**
	 * 
	 * @param label		标签
	 * @param percent	百分比
	 * @param color		显示颜色
	 */
	public ArcLineData(String label,double percent,int color)
	{
		setLabel(label);
		setPercentage(percent);
		setBarColor(color);
	}
	
	/**
	 * 
	 * @param key		键值
	 * @param label		标签
	 * @param percent	百分比
	 * @param color		显示颜色
	 */
	public ArcLineData(String key,String label,double percent,int color)
	{
		setLabel(label);
		setPercentage(percent);
		setBarColor(color);
		setKey(key);
	}
	
	
	/**
	 * 设置Key值
	 * @param key Key值
	 */
	public void setKey(String key)
	{
		mKey = key;
	}	
	
	/**
	 * 返回Key值
	 * @return Key值
	 */
	public String getKey()
	{
		return mKey;
	}
	
	/**
	 * 设置标签
	 * @param label 标签
	 */
	public void setLabel(String label)
	{
		mLabel = label;
	}	
		
	/**
	 * 设置百分比,绘制时，会将其转换为对应的圆心角
	 * @param value 百分比
	 */
	public void setPercentage(double value)
	{
		mValue = value;
	}
	
	/**
	 * 设置扇区颜色
	 * @param color 颜色
	 */
	public void setBarColor(int color)
	{
		mColor = color;
	}
	
	/**
	 * 返回标签
	 * @return 标签
	 */
	public String getLabel()
	{
		return mLabel;
	}
	
	/**
	 * 返回当前百分比
	 * @return 百分比
	 */
	public double getPercentage()
	{
		return mValue;
	}
	
	/**
	 * 返回扇区颜色
	 * @return 颜色
	 */
	public int getBarColor()
	{
		return mColor;
	}
	
	/**
	 * 将百分比转换为图显示角度
	 * @return 圆心角度
	 */
	public float getSliceAngle() 
	{			
		float Angle = 0.0f;
		try{
			float currentValue = (float) this.getPercentage();
			if(currentValue >= 101f || currentValue < 0.0f)
			{
				Log.e(TAG,"输入的百分比不合规范.须在0~100之间.");			
			}else{		
				//Angle = (float) Math.rint( 360f *  (currentValue / 100f) );
				Angle =  MathHelper.getInstance().round(360f *  (currentValue / 100f),2) ;
			}
		}catch(Exception ex)
		{
			Angle = -1f;
		}finally{
			
		}
		return  Angle;
	}
}
