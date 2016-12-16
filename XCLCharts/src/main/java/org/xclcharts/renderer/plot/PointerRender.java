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
 * @version 1.3
 */
package org.xclcharts.renderer.plot;

import org.xclcharts.common.MathHelper;

import android.graphics.Canvas;
import android.graphics.Path;


/**
 * @ClassName PointerRender
 * @Description 指针绘制类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * 
 */

public class PointerRender extends Pointer{
	
	private static final int FIX_ANGLE = 90;
	
	private float mStartAngle = 0.0f;
	private float mTotalAngle = 0.0f;
	private float mPointerAngle = 0.0f;
	
	private float mParentRadius = 0.0f;
	private float mPointerRadius = 0.0f;
	private float mPointerTailRadius = 0.0f;
	
	private float mEndX = 0.0f;
	private float mEndY = 0.0f;
	
	private float mTailX = 0.0f;
	private float mTailY = 0.0f;
	
	private Path mPath = null;
	

	public PointerRender()
	{		
	}
	
	/**
	 * 设置指针的绘制的起始坐标位置
	 * @param x
	 * @param y
	 */
	public void setStartXY(float x,float y)
	{
		mCenterX = x;
		mCenterY = y;
	}
	
	public void setCurrentAngle(float currentAngle)
	{	
		mPointerAngle = currentAngle;
	}
	
	public void setStartAngle(float startAngle)
	{	
		mStartAngle = startAngle;
	}
	
	public void setTotalAngle(float totalAngle)
	{		
		mTotalAngle = totalAngle;
	}
	
	public void setParentRadius(float radius)
	{
		mParentRadius = radius;
	}
		
	private void calcRadius()
	{
		if(Float.compare(mPointerRadiusPercentage, 0.0f) == 1)
		{
			mPointerRadius = MathHelper.getInstance().mul(
											mParentRadius ,mPointerRadiusPercentage); 
		}
				
		if(Float.compare(mPointerTailRadiusPercentage, 0.0f) == 1)
		{
			mPointerTailRadius = MathHelper.getInstance().mul(
											mParentRadius ,mPointerTailRadiusPercentage); 
		}		
	}
	
	public void setPointEndXY(float x,float y)
	{
		mEndX = x;
		mEndY = y;
	}
			
	public float getCurrentPointerAngle()
	{	
		mPointerAngle = MathHelper.getInstance().mul( mTotalAngle , mPercentage);
		return mPointerAngle;
	}
	
	private void calcEndXY()
	{			
		MathHelper.getInstance().calcArcEndPointXY(mCenterX, mCenterY, mPointerRadius,
								MathHelper.getInstance().add(getCurrentPointerAngle() , mStartAngle));	
		mEndX = MathHelper.getInstance().getPosX();
		mEndY = MathHelper.getInstance().getPosY();	
						
	    if(Float.compare(mPointerTailRadiusPercentage, 0.0f) == 1)
	    {
	    	float tailAgent = mPointerAngle + mStartAngle - 180 ;
	    	MathHelper.getInstance().calcArcEndPointXY(mCenterX, mCenterY, mPointerTailRadius,tailAgent);	
	    	mTailX =  MathHelper.getInstance().getPosX();
	    	mTailY =  MathHelper.getInstance().getPosY();	
	    }else{
	    	mTailX = mCenterX;
	    	mTailY = mCenterY;
	    }
	}
	
	
	
	public void renerLine(Canvas canvas)
	{
		canvas.drawLine(mCenterX, mCenterY, mEndX,mEndY, getPointerPaint());   
	}
	
	public void renderTriangle(Canvas canvas)
	{
	    float currentAgent1 = MathHelper.getInstance().add(mPointerAngle - FIX_ANGLE, mStartAngle);
	    float currentAgent2 = MathHelper.getInstance().add(mPointerAngle + FIX_ANGLE, mStartAngle);
	    	  	    	        
        float bX = 0.0f,bY = 0.0f,eX = 0.0f,eY=0.0f;
		MathHelper.getInstance().calcArcEndPointXY(mTailX, mTailY, mBaseRadius,currentAgent1);	
		bX = MathHelper.getInstance().getPosX();
		bY = MathHelper.getInstance().getPosY();	
    
        MathHelper.getInstance().calcArcEndPointXY(mTailX, mTailY, mBaseRadius,currentAgent2);	
        eX = MathHelper.getInstance().getPosX();
        eY = MathHelper.getInstance().getPosY();	
    
     
        if(null == mPath)
        {
        	mPath = new Path();
        }else{
        	mPath.reset();
        }
        mPath.moveTo(mEndX, mEndY);
        mPath.lineTo(bX, bY);
        mPath.lineTo(eX, eY);
        mPath.close();
        canvas.drawPath(mPath, getPointerPaint());	  	  
      
	}
	
	public void renderCircle(Canvas canvas)
	{
		canvas.drawCircle(mCenterX, mCenterY, mBaseRadius, getBaseCirclePaint());
	}
	
	public void render(Canvas canvas)
	{
		calcRadius();
		calcEndXY();
		switch(getPointerStyle())
		{
			case TRIANGLE:
				renderTriangle(canvas);
				if(isShowBaseCircle())renderCircle(canvas);
				break;
			case LINE:
				renerLine(canvas);
				if(isShowBaseCircle())renderCircle(canvas);
				break;
			default:
				break;
		}
	}
}
