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
 * @version v0.1
 */
package org.xclcharts.renderer.axis;

import java.util.List;

import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PointF;

/**
 * @ClassName DialChart
 * @Description  仪表盘基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class RoundAxis extends Axis{
	
	// private static final String TAG ="RoundAxis";
		
	 protected float mCirX = 0.0f;
	 protected float mCirY = 0.0f;
	
	 protected float mOrgRadius = 0.0f;
	 protected float mRadius = 0.0f;
	 protected int mDetailModeSteps = 1;
	 
	 private float mRadiusPercentage = 1f;
	 private float mInnerRadiusPercentage = 0.9f;

 
	// 用于格式化标签的回调接口
	private IFormatterTextCallBack mLabelFormatter;
	
	protected float mTotalAngle = 0.0f;
	protected float mInitAngle = 0.0f;
	private	XEnum.RoundAxisType mAxisType = XEnum.RoundAxisType.ARCLINEAXIS;
			
	protected List<Float> mPercentage = null;
	protected List<Integer> mColor = null;
	protected List<String> mLabels = null;
	  
	//ringaxis/fillaxis
	private  Paint mPaintFillAxis = null;
	
	 protected boolean mLongTickfakeBold = true;
	// protected float mShortTickPercentage = 0.95f;
	 
	 protected XEnum.RoundTickAxisType mRoundTickAxisType = 
			 				XEnum.RoundTickAxisType.INNER_TICKAXIS;
	 
	 
	 public RoundAxis()
	 {		
	 }
	 
	 /**
	  * 设置轴类型,不同类型有不同的显示风格
	  * @param axisType 轴类型
	  */
	 public void setRoundAxisType(XEnum.RoundAxisType axisType)
	 {
		 mAxisType = axisType;
		// TICKAXIS,RINGAXIS,LENAXIS
		 switch(mAxisType)
		 {
		 case TICKAXIS:
			 getTickLabelPaint().setTextAlign(Align.CENTER);
		
			 showAxisLabels();
			 showTickMarks();
			 showAxisLine();
			 this.getAxisPaint().setStyle(Style.STROKE);
			 
			 break;
		 case RINGAXIS:
			 showAxisLabels();
			 hideTickMarks();
			 
			 this.getAxisPaint().setStyle(Style.FILL);
			 this.getAxisPaint().setColor(Color.BLUE);
			 
			 initFillAxisPaint();
			 
			 break;
		 case ARCLINEAXIS:			 
			 hideAxisLabels();
			 hideTickMarks();
			 
			 getAxisPaint().setStyle(Style.STROKE);
			 
		 case CIRCLEAXIS:			 
			 //getAxisPaint().setStyle(Style.FILL);
			 
			 hideAxisLabels();
			 hideTickMarks();
			 
			 break;
		 default:
			 break;
		 }
	 }
	 
	 
	 /**
	  * 设置后，会启用为明细模式，轴刻度线会分长短,背景线会分粗细
	  * @param steps 步长
	  */
	 public void setDetailModeSteps(int steps)
	 {
		 mDetailModeSteps = steps;
	 }
	 
	
	 public void setDetailModeSteps(int steps,
			 						boolean isLongTickfakeBold)
	 {
		 mDetailModeSteps = steps;
		 
		 mLongTickfakeBold = isLongTickfakeBold;
	 }
	 
	  public float getRadius()
	  {
		  return mRadius;
	  }
	  
	  public void setRoundTickAxisType(XEnum.RoundTickAxisType type)
	  {
		  mRoundTickAxisType = type;
	  }

	  /**
	   * 绘制半径比例
	   * @param percentage 占总半径的比例
	   */
	  public void setRadiusPercentage(float percentage)
	  {
		  mRadiusPercentage = percentage;
	  }
	  
	  //ringaxis
	  /**
	   * 绘制内半径比例
	   * @param percentage 占总半径的比例
	   */
	  public void setRingInnerRadiusPercentage(float percentage)
	  {
		  mInnerRadiusPercentage = percentage;
	  }
	 
	  /**
	   * 内部填充画笔
	   * @return 画笔
	   */
	  public Paint getFillAxisPaint()
	  {
		  initFillAxisPaint();
		  return mPaintFillAxis;
	  }
	
	  private void initFillAxisPaint()
	  {
		  if(null == mPaintFillAxis)
		 {
			 mPaintFillAxis = new Paint();
			 mPaintFillAxis.setStyle(Style.FILL);
			 mPaintFillAxis.setColor(Color.WHITE);
			 mPaintFillAxis.setAntiAlias(true);
		 }		
	  }
	  
	  
	  /**
		 * 设置标签的显示格式
		 * @param callBack 回调函数
		 */
		public void setLabelFormatter(IFormatterTextCallBack callBack) {
			this.mLabelFormatter = callBack;
		}
		
		/**
		 * 返回标签显示格式
		 * 
		 * @param value 传入当前值
		 * @return 显示格式
		 */
		protected String getFormatterLabel(String text) {
			String itemLabel = "";
			try {
				itemLabel = mLabelFormatter.textFormatter(text);
			} catch (Exception ex) {
				itemLabel = text;
			}
			return itemLabel;
		}
	
	 
		/**
		 * 返回轴类型
		 * @return 轴类型
		 */
		public XEnum.RoundAxisType getAxisType()
		{
			return mAxisType;
		}

		
		/**
		 * 外环显示在哪个比例位置
		 * @return 比例
		 */
		public float getOuterRadiusPercentage()
		{
			return mRadiusPercentage;
		}
		
		/**
		 * 内环显示在哪个比例位置
		 * @return 比例
		 */
		public float getRingInnerRadiusPercentage()
		{
			return mInnerRadiusPercentage;
		}
		
		
		/**
		 * 外环半径长度
		 * @return	半径长度
		 */
		public float getOuterRadius()
		{
			return  mOrgRadius *  mRadiusPercentage;
		}
		
		/**
		 * 内环半径长度
		 * @return	半径长度
		 */
		public float getRingInnerRadius()
		{
			return  mOrgRadius * mInnerRadiusPercentage;
		}
	  
		/**
		 * 圆心位置 
		 * @return	圆心
		 */
		public PointF getCenterXY()
		{
			return(new PointF(mCirX,mCirY));
		}
	 
}
