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
 * @ClassName RectPosition
 * @Description  rect类型的位置记录信息基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class RectPosition extends PositionRecord{
	
	protected RectF mRectF = null;
	protected RectF mRectFRange = null;
	
	//放大值
	protected int mExtValue = 0;
	
	public RectPosition()
	{	
	}	
	
	public void extPointClickRange(int value)
	{
		mExtValue = value;
	}	
	
	
	protected void saveRectF(float left,float top,float right,float bottom)
	{
		if(null == mRectF)mRectF = new RectF();			
		mRectF.set(left, top, right, bottom);
	}
	
	protected void saveRectF(RectF r)
	{
		mRectF = r;
	}
	
	public float getRadius()
	{
		float radius = 0.0f;		
		radius = (mRectF.bottom -  mExtValue) - (mRectF.top +  mExtValue) ;
		return radius; //MathHelper.getInstance().div(radius, 2.f);		
	}
			
	public String getRectInfo()
	{	
		if(null == mRectF)return "";
		
		float left = mRectF.left +  mExtValue;
		float top = mRectF.top +  mExtValue;
		float right = mRectF.right -  mExtValue;
		float bottom = mRectF.bottom -  mExtValue;
		
		String info =" left:"+Float.toString(left)+" top:"+Float.toString(top) +
				 	 " right:"+Float.toString(right)+" bottom:"+Float.toString(bottom);
		return info;
	}
	
	public RectF getRectF()
	{
		return mRectF;
	}

	@Override
	protected boolean compareRange(float x, float y) {
		// TODO Auto-generated method stub
		
		if(null == mRectF)return false;		
		if(null == mRectFRange)  mRectFRange = new RectF();
	
		mRectFRange.setEmpty();
		mRectFRange.set(mRectF);
		
		mRectFRange.left -=  mExtValue;
		mRectFRange.top -=  mExtValue;
		mRectFRange.right +=  mExtValue;
		mRectFRange.bottom +=  mExtValue;			
			
		//contains 在范围比较小时很不好使.
		if( mRectFRange.contains(x, y)) return true;
		
		//再加层手工检查
		if( (Float.compare(x, mRectFRange.left) == 1 || Float.compare(x, mRectFRange.left) == 0 )
				&& (Float.compare(x, mRectFRange.right) == -1 || Float.compare(x, mRectFRange.right) == 0 ) 
			&& (Float.compare(y, mRectFRange.bottom) == 1 || Float.compare(y, mRectFRange.bottom) == 0 )
			&& (Float.compare(y, mRectFRange.top) == -1 || Float.compare(y, mRectFRange.top) == 0 ) ) 
			{
				return true;
			}

		return false;
	}	
	

}
