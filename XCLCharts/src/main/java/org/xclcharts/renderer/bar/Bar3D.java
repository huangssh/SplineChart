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

package org.xclcharts.renderer.bar;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;

/**
 * @ClassName Bar3D
 * @Description  3d柱形类，增加柱形的一些3D效果处理
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class Bar3D extends Bar{
	
	//3D效果厚度
	private int mThickness = 20;
	//偏移角色度
	private int mAngle = 45;	
	//透明度
	private int mAlpha = 200; 
	
	//3D浅色画笔
	private Paint mPaint3D = null;	
	private Paint mPaintBase = null;
	private Paint mPaintBase3D = null;
	//3D效果厚度
	private int mAxisBaseThickness = 20;
	//底盘颜色
	private int mAxisBaseColor = Color.rgb(73, 172, 72);
	
	private Path mPathRectangle2D = new Path();
	private Paint mPaintLine = new Paint();
	
	
	public Bar3D()
	{		
		if(null == mPaint3D)mPaint3D = new Paint();		
		if(null== mPaintBase)mPaintBase = new Paint();
		if(null == mPaintBase3D)mPaintBase3D = new Paint();		
	}

	/**
	 * 得到水平偏移量
	 * @param thickness	厚度
	 * @param angle		角度
	 * @return	水平偏移量
	 */
	public double getOffsetX(double thickness, double angle)
	{
	    return  thickness * Math.cos(angle * Math.PI / 180);
	}


	/**
	 * 得到垂直偏移量
	 * @param thickness	厚度
	 * @param angle		角度
	 * @return	垂直偏移量
	 */
	public double getOffsetY(double thickness, double angle)
	{
	    return  thickness * Math.sin(angle * Math.PI / 180);
	}


	/**
	 * 返回水平偏移量
	 * @return 偏移量
	 */
	public double getOffsetX()
	{
		return getOffsetX(mAxisBaseThickness,mAngle);
	}
	

	/**
	 * 返回垂直偏移量
	 * @return 偏移量
	 */
	public double getOffsetY()
	{
		return getOffsetY(mAxisBaseThickness,mAngle);
	}

	/**
	 * 计算同标签多柱形时的Y分隔
	 * @param YSteps    Y轴步长
	 * @param barNumber  柱形个数
	 * @return 返回单个柱形的高度及间距
	 */
	public float[] getBarHeightAndMargin(float YSteps,int barNumber)
	{
		return calcBarHeightAndMargin( YSteps, barNumber);
	}
	
	/**
	 * 计算同标签多柱形时的X分隔
	 * @param XSteps	X轴步长
	 * @param barNumber 柱形个数
	 * @return 返回单个柱形的宽度及间距
	 */
	public float[] getBarWidthAndMargin(float XSteps,int barNumber)
	{
		return calcBarWidthAndMargin(XSteps,barNumber);
	}
	

	/**
	 * 竖向柱形的3D效果
	 * @param barLeft	左边X坐标
	 * @param barTop	顶部Y坐标
	 * @param barRight	右边X坐标
	 * @param barBottom	底部Y坐标
	 * @param color		柱形颜色
	 * @param canvas	画布
	 */
	public void renderVertical3DBar(float barLeft,float barTop,
			  float barRight,float barBottom,
			  int color,
			  Canvas canvas)
	{
		//浅色	
		int lightColor = DrawHelper.getInstance().getLightColor(color,mAlpha);
		
		getBarPaint().setColor(color);		
		mPaint3D.setColor(lightColor);
		
		//水平偏移量
		double offsetX = getOffsetX();
		//垂直偏移量
		double offsetY= getOffsetY();
		
		//Shadow
		float barLeft2 = Math.round(barLeft -offsetX);
		float barTop2 = Math.round(barTop + offsetY);
		float barRight2 = Math.round(barRight - offsetX) ;
		float barBottom2 = Math.round(barBottom + offsetY) ;
		
		//顶		
		mPathRectangle2D.reset();
		mPathRectangle2D.moveTo(barLeft, barTop); 
		mPathRectangle2D.lineTo(barLeft2, barTop2);    			
		mPathRectangle2D.lineTo(barRight2, barTop2); 
		mPathRectangle2D.lineTo(barRight, barTop); 			
		mPathRectangle2D.close();
		canvas.drawPath(mPathRectangle2D,getBarPaint());
		
		//右侧边
		mPathRectangle2D.reset();
		mPathRectangle2D.moveTo(barRight, barTop); 
		mPathRectangle2D.lineTo(barRight2, barTop2); 
		mPathRectangle2D.lineTo(barRight2, barBottom2); 
		mPathRectangle2D.lineTo(barRight, barBottom); 
		mPathRectangle2D.close();
		canvas.drawPath(mPathRectangle2D,getBarPaint()); 		
		
		mPathRectangle2D.reset();
	    mPathRectangle2D.moveTo(barRight2, barTop2); 
	    mPathRectangle2D.lineTo(barRight2, barBottom2); 
	    mPathRectangle2D.lineTo(barLeft2, barBottom2); 
	    mPathRectangle2D.lineTo(barLeft2, barTop2 );
	    mPathRectangle2D.close();
		
		//正面 浅色	
		 LinearGradient linearGradient = new LinearGradient(
					 	barLeft2,barBottom2, barRight2 ,barBottom2,  
					 	new int[]{color,lightColor},  
					 	null,Shader.TileMode.REPEAT);  		   
		 mPaint3D.setShader(linearGradient);
		 mPaint3D.setStyle(Style.FILL);		 
		 canvas.drawPath(mPathRectangle2D,mPaint3D); 
	
		//柱形顶上用白画一个RECT,强化3D效果		 
		mPaintLine.reset();	
		mPaintLine.setStyle(Style.STROKE);
		mPaintLine.setColor(Color.WHITE); 
			
		mPathRectangle2D.reset();
		mPathRectangle2D.moveTo(barLeft2, barTop2);    			
		mPathRectangle2D.lineTo(barRight2, barTop2); 
		mPathRectangle2D.lineTo(barRight, barTop); 			 	   
		canvas.drawPath(mPathRectangle2D,mPaintLine); 
		
		//柱形正面画一根白色竖线,强化3D效果
		canvas.drawLine(barRight2, barTop2, barRight2, barBottom2, mPaintLine);
	}
	
	
	/**
	 * 竖向3D柱形图的底 座
	 * @param baseLeft	左边X坐标
	 * @param baseTop	顶部Y坐标
	 * @param baseRight	右边X坐标
	 * @param baseBottom	底部Y坐标
	 * @param canvas	画布
	 */
	 public void render3DXAxis(float baseLeft,float baseTop,
			  float baseRight,float baseBottom,		
			  Canvas canvas) //	  int color,
	{
		//浅色	
		int baseLightColor = DrawHelper.getInstance().getLightColor(getAxis3DBaseColor(), mAlpha);		
		mPaintBase.setColor(getAxis3DBaseColor());		
		mPaintBase3D.setColor(baseLightColor);
		
		//水平偏移量
		float offsetX = (float) getOffsetX();
		//垂直偏移量
		float offsetY= (float) getOffsetY();
		
		//Shadow
		float baseLeft2 = MathHelper.getInstance().sub(baseLeft ,offsetX);
		float baseTop2 = MathHelper.getInstance().add(baseTop , offsetY);
		float baseRight2 = MathHelper.getInstance().sub(baseRight , offsetX) ;
		float baseBottom2 = MathHelper.getInstance().add(baseBottom , offsetY) ;
					
		//顶 用浅色
		mPathRectangle2D.reset();	
		mPathRectangle2D.moveTo(baseLeft, baseBottom); 
		mPathRectangle2D.lineTo(baseLeft2, baseBottom2);      
		mPathRectangle2D.lineTo(baseRight2, baseBottom2); 
		mPathRectangle2D.lineTo(baseRight, baseBottom); 
		mPathRectangle2D.close();
		canvas.drawPath(mPathRectangle2D,mPaintBase3D); 	
		
		//右侧边 深色
		mPathRectangle2D.reset();
		mPathRectangle2D.moveTo(baseRight, baseTop); 
		mPathRectangle2D.lineTo(baseRight2, baseTop2); 
		mPathRectangle2D.lineTo(baseRight2, baseBottom2); 
		mPathRectangle2D.lineTo(baseRight, baseBottom); 
		mPathRectangle2D.close();
		canvas.drawPath(mPathRectangle2D,mPaintBase); 
		
		//正面 深色	 
		mPathRectangle2D.reset();
	    mPathRectangle2D.moveTo(baseRight2, baseTop2); 
	    mPathRectangle2D.lineTo(baseRight2, baseBottom2); 
	    mPathRectangle2D.lineTo(baseLeft2, baseBottom2); 
	    mPathRectangle2D.lineTo(baseLeft2, baseTop2 );		   
	    mPathRectangle2D.close();		
		canvas.drawPath(mPathRectangle2D,mPaintBase); 				
		
		//水平偏移量		
		//Paint paint = new Paint();
		mPaintLine.reset();
		mPaintLine.setColor(getAxis3DBaseColor());
		mPaintLine.setStyle(Style.FILL);			
	
		mPathRectangle2D.reset();
		mPathRectangle2D.moveTo(baseRight2, baseBottom2); 				
		mPathRectangle2D.lineTo(baseRight, baseBottom); 
		mPathRectangle2D.lineTo(baseRight,MathHelper.getInstance().add( baseBottom , mAxisBaseThickness)); 
		mPathRectangle2D.lineTo(baseRight2, baseBottom2 + mAxisBaseThickness);   
		mPathRectangle2D.close();				
		canvas.drawPath(mPathRectangle2D,mPaintLine); 				
		mPaintLine.setColor(baseLightColor);
		canvas.drawRect(baseLeft2, baseBottom2, baseRight2, 
						baseBottom2 + mAxisBaseThickness, mPaintLine);		
	}
	 
	 /**
	  * 横向3D柱形图
	  * @param barLeft	左边X坐标
	  * @param barTop	顶部Y坐标
	  * @param barRight	右边X坐标
	  * @param barBottom	底部Y坐标
	  * @param color	柱形颜色
	  * @param canvas	画布
	  */
	 public void renderHorizontal3DBar(float barLeft,float barTop,
			  float barRight,float barBottom,
			  int color,
			  Canvas canvas)
	{
		 if(Float.compare(barTop, barBottom) == 0)return;		 
		 
		//浅色
		int lightColor =  DrawHelper.getInstance().getLightColor(color,mAlpha);
		
		getBarPaint().setColor(color);		
		mPaint3D.setColor(lightColor);
		
		//水平偏移量
		float offsetX = (float) getOffsetX();
		//垂直偏移量
		float offsetY= (float) getOffsetY();
		
		//Shadow
		float barLeft2 = MathHelper.getInstance().sub(barLeft , offsetX);
		float barTop2 = MathHelper.getInstance().add(barTop , offsetY);
		float barRight2 = MathHelper.getInstance().sub(barRight , offsetX) ;
		float barBottom2 = MathHelper.getInstance().add(barBottom, offsetY) ;	
		
		//右侧边 浅色
		mPathRectangle2D.reset();
		mPathRectangle2D.moveTo(barRight, barTop); 
		mPathRectangle2D.lineTo(barRight, barBottom); 
		mPathRectangle2D.lineTo(barRight2, barBottom2); 
		mPathRectangle2D.lineTo(barRight2, barTop2); 					
		mPathRectangle2D.close();		
		canvas.drawPath(mPathRectangle2D,mPaint3D); 	
		
		//正面	
		canvas.drawRect(barLeft2, barTop2, barRight2, barBottom2, mPaint3D);
		
		//顶
		mPathRectangle2D.reset();		
		mPathRectangle2D.moveTo(barLeft, barTop); 
		mPathRectangle2D.lineTo(barLeft2, barTop2); 
		mPathRectangle2D.lineTo(barRight2, barTop2 );
		mPathRectangle2D.lineTo(barRight, barTop );
		mPathRectangle2D.close();
		canvas.drawPath(mPathRectangle2D,getBarPaint()); 		
	
		//轮廓线
		mPaintLine.reset();
		mPaintLine.setColor(Color.WHITE);
		mPaintLine.setStyle(Style.STROKE);		
		canvas.drawLine( barLeft2,  barTop2, barRight2, barTop2, mPaintLine);
		canvas.drawLine( barRight2, barTop2, barRight2, barBottom2,mPaintLine);		
		canvas.drawLine( barRight, barTop, barRight2,barTop2,mPaintLine);   	
	}
	 

	/**
	 * 横向3D柱形图的底 座
	 * @param baseLeft	左边X坐标
	 * @param baseTop	顶部Y坐标
	 * @param baseRight	右边X坐标
	 * @param baseBottom	底部Y坐标
	 * @param canvas	画布
	 */
	 public void render3DYAxis(float baseLeft,float baseTop,
			 					float baseRight,float baseBottom,						
			  					Canvas canvas) 
	{
		//浅色
		int baseLightColor = DrawHelper.getInstance().getLightColor(getAxis3DBaseColor(), mAlpha);						
		mPaintBase.setColor(getAxis3DBaseColor());		
		mPaintBase3D.setColor(baseLightColor);
		
		//水平偏移量
		float offsetX = (float) (getOffsetX());
		//垂直偏移量
		float offsetY= (float) (getOffsetY()) ;		
	
		//Shadow
		float baseLeft2 = MathHelper.getInstance().sub(baseLeft , offsetX);
		float baseTop2 = MathHelper.getInstance().add(baseTop , offsetY);
		float baseBottom2 = MathHelper.getInstance().add(baseBottom , offsetY) ;			
		
		//左侧面
		mPathRectangle2D.reset();
		mPathRectangle2D.moveTo(baseLeft, baseTop); 
		mPathRectangle2D.lineTo(baseLeft2, baseTop2); 
		mPathRectangle2D.lineTo(baseLeft2, baseBottom2); 
		mPathRectangle2D.lineTo(baseLeft, baseBottom); 
		mPathRectangle2D.close();
		canvas.drawPath(mPathRectangle2D,mPaintBase); 	
		
		//正面
		canvas.drawRect(baseLeft2, baseTop2, 
				MathHelper.getInstance().sub(baseLeft2 , offsetX), baseBottom2, mPaintBase3D);
				
		//侧面顶		
		mPathRectangle2D.reset();	
		mPathRectangle2D.moveTo(baseLeft, baseTop); 
		mPathRectangle2D.lineTo(baseLeft2, baseTop2); 
		mPathRectangle2D.lineTo(MathHelper.getInstance().sub(baseLeft2 ,offsetX),baseTop2); 
		mPathRectangle2D.lineTo(MathHelper.getInstance().sub(baseLeft ,offsetX),baseTop); 
		mPathRectangle2D.close();
		canvas.drawPath(mPathRectangle2D,mPaintBase3D); 			
	}
	 
	 /**
	  * 返回柱形3D厚度
	  * @return 厚度
	  */
		public int getThickness() {
			return mThickness;
		}

		/**
		 * 设置柱形3D厚度
		 * @param thickness 厚度
		 */
		public void setThickness(int thickness) {
			this.mThickness =  thickness;
		}

		/**
		 * 设置3D偏转角度
		 * @return 偏转角度
		 */
		public int getAngle() {
			return mAngle;
		}

		/**
		 * 设置3D偏转角度
		 * @param angle 角度
		 */
		public void setAngle(int angle) {
			this.mAngle = angle;
		}
		
		/**
		 * 返回透明度
		 * @return 透明度
		 */
		public int getAlpha() {
			return mAlpha;
		}

		/**
		 * 设置透明度
		 * @param alpha 透明度
		 */
		public void setAlpha(int alpha) {
//			this.mAlpha = alpha;
		}
		
		/**
		 * 返回坐标系底座厚度 
		 * @return 底座厚度 
		 */
		public int getAxis3DBaseThickness() {
			return mAxisBaseThickness;
		}

		/**
		 * 设置坐标系底座厚度 
		 * @param baseThickness 底座厚度
		 */
		public void setAxis3DBaseThickness(int baseThickness) {
			this.mAxisBaseThickness = baseThickness;
		}
		
		/**
		 * 绘制柱形标签
		 * @param text	文本内容
		 * @param x		x坐标
		 * @param y		y坐标
		 * @param canvas	画布
		 */
		public void renderBarItemLabel(String text,float x,float y,Canvas canvas)
		{
			drawBarItemLabel(text,x,y,canvas);
		}
		
		/**
		 * 设置3D底座颜色
		 * @param color 颜色
		 */
		public void setAxis3DBaseColor(int color)
		{
			mAxisBaseColor =  color;
		}
		
		/**
		 * 返回3D底座颜色
		 * @return 颜色
		 */
		public int getAxis3DBaseColor()
		{
			return mAxisBaseColor;
		}
}
