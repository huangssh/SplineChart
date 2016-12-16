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

package org.xclcharts.renderer;

import java.util.List;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.common.CurveHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.common.PointHelper;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum.BarCenterStyle;
import org.xclcharts.renderer.info.AnchorDataPoint;
import org.xclcharts.renderer.info.PlotAxisTick;
import org.xclcharts.renderer.line.PlotCustomLine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

/**
 * @ClassName XChart
 * @Description 所有线类，如折线，曲线等图表类的基类,在此主要用于Key及坐标系的绘制。
 * 
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 * 
 */

public class LnChart extends AxesChart {

	private static final String TAG = "LnChart";

	private PointF[] BezierControls;

	// 批注
	private List<AnchorDataPoint> mAnchorSet;

	// 用于绘制定制线(分界线)
	protected PlotCustomLine mCustomLine = null;
	
	//坐标从第一个刻度而不是轴开始
	protected boolean mXCoordFirstTickmarksBegin = false;
	
	//标签和对象依哪种风格居中显示
	protected XEnum.BarCenterStyle mBarCenterStyle = XEnum.BarCenterStyle.TICKMARKS;

	public LnChart() {
		this.mBarCenterStyle = BarCenterStyle.TICKMARKS;
		// 初始化图例
		if (null != plotLegend) {
			plotLegend.show();
			plotLegend.setType(XEnum.LegendType.ROW);
			plotLegend.setHorizontalAlign(XEnum.HorizontalAlign.LEFT);
			plotLegend.setVerticalAlign(XEnum.VerticalAlign.TOP);
			plotLegend.hideBox();
		}

		categoryAxisDefaultSetting();
		dataAxisDefaultSetting();
	}
	
	/**
	 * 依刻度线居中或依刻度中间点居中。
	 * @return
	 */
	public XEnum.BarCenterStyle getBarCenterStyle() {
		return mBarCenterStyle;
	}

	/**
	 * 依刻度线居中或依刻度中间点居中。
	 * @return
	 */
	public void setBarCenterStyle(XEnum.BarCenterStyle mBarCenterStyle) {
		this.mBarCenterStyle = mBarCenterStyle;
	}

	/**
	 * 返回指定数据在图中的竖向坐标位置
	 * 
	 * @param bv
	 *            数
	 * @return 坐标位置
	 */
	public float getVPValPosition(double bv) {
		float vaxlen = (float) MathHelper.getInstance().sub(bv,
				dataAxis.getAxisMin());
		float valuePostion = mul(getPlotScreenHeight(),
				div(vaxlen, dataAxis.getAxisRange()));
		return (sub(plotArea.getBottom(), valuePostion));
	}

	protected float getLnXValPosition(double xValue, double maxValue,
			double minValue) {
		// 对应的X坐标
		double maxminRange = MathHelper.getInstance().sub(maxValue, minValue);
		double xScale = MathHelper.getInstance().div(
				MathHelper.getInstance().sub(xValue, minValue), maxminRange);
		float XvaluePos = mul(getPlotScreenWidth(), (float) xScale);
		return add(plotArea.getLeft(), XvaluePos);
	}

	private float getVPDataAxisStdY() {
		if (dataAxis.getAxisStdStatus()) {
			return getVPValPosition(dataAxis.getAxisStd());
		} else {
			return plotArea.getBottom();
		}
	}

	@Override
	protected float getAxisYPos(XEnum.AxisLocation location) {
		if (dataAxis.getAxisStdStatus() && categoryAxis.getAxisBuildStdStatus()) {
			return getVPDataAxisStdY();
		} else {
			return super.getAxisYPos(location);
		}
	}

	/**
	 * 设置定制线值
	 * 
	 * @param customLineDataset
	 *            定制线数据集合
	 */
	public void setCustomLines(List<CustomLineData> customLineDataset) {
		if (null == mCustomLine)
			mCustomLine = new PlotCustomLine();
		mCustomLine.setCustomLines(customLineDataset);
	}

	/**
	 * 绘制底部标签轴
	 */
	@Override
	protected void drawClipDataAxisGridlines(Canvas canvas) {
		// 与柱形图不同，无须多弄一个
		float XSteps = 0.0f, YSteps = 0.0f;

		// 数据轴数据刻度总个数
		int tickCount = dataAxis.getAixTickCount();
		int labeltickCount = tickCount;

		if (0 == tickCount) {
			Log.e(TAG, "数据源个数为0!");
			return;
		} else if (1 == tickCount) // label仅一个时右移
			labeltickCount = tickCount - 1;

		// 标签轴(X 轴)
		float axisX = 0.0f, axisY = 0.0f, currentX = 0.0f, currentY = 0.0f;
		// 标签
		double currentTickLabel = 0d;
		// 轴位置
		XEnum.AxisLocation pos = getDataAxisLocation();

		// 步长
		switch (pos) {
		case LEFT: // Y
		case RIGHT:
		case VERTICAL_CENTER:
			YSteps = getVerticalYSteps(labeltickCount);

			currentX = axisX = getAxisXPos(pos);
			currentY = axisY = plotArea.getBottom();
			break;
		case TOP: // X
		case BOTTOM:
		case HORIZONTAL_CENTER:
			XSteps = getVerticalXSteps(labeltickCount);
			currentY = axisY = getAxisYPos(pos);
			currentX = axisX = plotArea.getLeft();
			break;
		default:
			Log.e(TAG, "未知的枚举类型 .");
		}

		mLstDataTick.clear();
		// 绘制
		for (int i = 0; i < tickCount + 1; i++) {
			switch (pos) {
			case LEFT: // Y
			case RIGHT:
			case VERTICAL_CENTER:
				// 依起始数据坐标与数据刻度间距算出上移高度
				currentY = sub(plotArea.getBottom(), mul(i , YSteps));

				// 从左到右的横向网格线
				drawHorizontalGridLines(canvas, plotArea.getLeft(),
						plotArea.getRight(), i, tickCount + 1, YSteps, currentY);

				// 这个有点问题，要处理下，
				// 隐藏时应当不需要这个，但目前主明细模式下，会有问题，加 了一个都显示不出来
				// 先省略了
				// if(!dataAxis.isShowAxisLabels())continue;

				// 标签
				currentTickLabel = MathHelper.getInstance().add(
						dataAxis.getAxisMin(), mul(i , (float) dataAxis.getAxisSteps()));

				mLstDataTick.add(new PlotAxisTick(i, axisX, currentY, Double
						.toString(currentTickLabel)));
				break;
			case TOP: // X
			case BOTTOM:
			case HORIZONTAL_CENTER:
				// 依初超始X坐标与标签间距算出当前刻度的X坐标
				currentX = add(plotArea.getLeft(), mul(i , XSteps));

				// 绘制竖向网格线
				drawVerticalGridLines(canvas, plotArea.getTop(),
						plotArea.getBottom(), i, tickCount + 1, XSteps,
						currentX);

				// if(!dataAxis.isShowAxisLabels())continue;

				// 画上标签/刻度线
				currentTickLabel = MathHelper.getInstance().add(
						dataAxis.getAxisMin(), mul(i , (float) dataAxis.getAxisSteps()));

				mLstDataTick.add(new PlotAxisTick(i, currentX, axisY, Double
						.toString(currentTickLabel)));

				break;
			} // switch end
		} // end for
	}
	
	protected int getCategoryAxisCount() {
		int tickCount = categoryAxis.getDataSet().size();
		int labeltickCount = 0;
		if (0 == tickCount) {
			Log.w(TAG, "分类轴数据源为0!");
			return labeltickCount;
		} else if (1 == tickCount){ // label仅一个时右移		
			labeltickCount = tickCount;
		} else {
			if(mXCoordFirstTickmarksBegin)
			{
				if(XEnum.BarCenterStyle.SPACE == mBarCenterStyle)
				{
					labeltickCount = tickCount;
				}else{
					labeltickCount = tickCount + 1;
				}
			}else{
				labeltickCount = tickCount - 1;
			}			
		}
		return labeltickCount;
	}

	/**
	 * 绘制底部标签轴
	 */
	@Override
	protected void drawClipCategoryAxisGridlines(Canvas canvas) {
		// 得到标签轴数据集
		List<String> dataSet = categoryAxis.getDataSet();
		if(null == dataSet) return ;
		// 与柱形图不同，无须多弄一个
		float XSteps = 0.0f, YSteps = 0.0f;
		int j = 0;

		int tickCount = dataSet.size();
		if (0 == tickCount) {
			Log.w(TAG, "分类轴数据源为0!");
			return;
		} else if (1 == tickCount) // label仅一个时右移
		{
			j = 1;
		}
		int labeltickCount = getCategoryAxisCount();

		// 标签轴(X 轴)
		float axisX = 0.0f, axisY = 0.0f, currentX = 0.0f, currentY = 0.0f;
		XEnum.AxisLocation pos = getCategoryAxisLocation();

		if (XEnum.AxisLocation.LEFT == pos || XEnum.AxisLocation.RIGHT == pos
				|| XEnum.AxisLocation.VERTICAL_CENTER == pos) {
			YSteps = getVerticalYSteps(labeltickCount);

			currentX = axisX = getAxisXPos(pos);
			currentY = axisY = plotArea.getBottom();
		} else { // TOP BOTTOM
			XSteps = getVerticalXSteps(labeltickCount);
			currentY = axisY = getAxisYPos(pos);
			currentX = axisX = plotArea.getLeft();
		}

		mLstCateTick.clear();
		
		float labelX,labelY;
		boolean showTicks = true;

		// 绘制
		for (int i = 0; i < tickCount; i++) {
			switch (pos) {
			case LEFT: // Y
			case RIGHT:
			case VERTICAL_CENTER:
				// 依起始数据坐标与数据刻度间距算出上移高度								
				//currentY = sub(plotArea.getBottom(), j * YSteps);
				if(mXCoordFirstTickmarksBegin)
				{
					currentY = sub(plotArea.getBottom(), mul((j+1) , YSteps));
				}else{
					currentY = sub(plotArea.getBottom(), mul(j , YSteps));
				}

				// 从左到右的横向网格线
				drawHorizontalGridLines(canvas, plotArea.getLeft(),
						plotArea.getRight(), i, tickCount, YSteps, currentY);

				if (!categoryAxis.isShowAxisLabels())
					continue;
				//mLstCateTick.add(new PlotAxisTick(axisX, currentY, dataSet.get(i)));
				
				 labelX = axisX;
				 labelY = currentY;
				 if(mXCoordFirstTickmarksBegin && XEnum.BarCenterStyle.SPACE == mBarCenterStyle) 
				 {
					 if(i == tickCount - 1)showTicks = false;
					 labelY = add(currentY,div(YSteps,2));
				 }	
				 
				 mLstCateTick.add(new PlotAxisTick(axisX, currentY, dataSet.get(i),labelX,labelY,showTicks));

				break;
			case TOP: // X
			case BOTTOM:
			case HORIZONTAL_CENTER:
				// 依初超始X坐标与标签间距算出当前刻度的X坐标
				if(mXCoordFirstTickmarksBegin)
				{
					currentX = add(plotArea.getLeft(),mul((j + 1) , XSteps));
				}else{
					currentX = add(plotArea.getLeft(),mul(j , XSteps));
				}
							
				// 绘制竖向网格线
				drawVerticalGridLines(canvas, plotArea.getTop(),
						plotArea.getBottom(), i, tickCount, XSteps, currentX);

				if (!categoryAxis.isShowAxisLabels()) continue;
				
				 labelX = currentX;
				 labelY = axisY;
				 
				 // 横坐标刻度居中
				 if(XEnum.BarCenterStyle.SPACE == mBarCenterStyle) 
				 {
					 if(i == tickCount - 1)showTicks = false;
					 labelX = sub(currentX,div(XSteps,2));
				 }
				 mLstCateTick.add(new PlotAxisTick( currentX, axisY, dataSet.get(i),labelX,labelY,showTicks));
				 
				break;
			} // switch end
			j++;
		} // end for

	}

	@Override
	public boolean isPlotClickArea(float x, float y) {
		if (!getListenItemClickStatus())
			return false;

		if (Float.compare(x, getLeft()) == -1)
			return false;
		if (Float.compare(x, getRight()) == 1)
			return false;

		if (Float.compare(y, getPlotArea().getTop()) == -1)
			return false;
		if (Float.compare(y, getPlotArea().getBottom()) == 1)
			return false;
		return true;
	}

	/**
	 * 返回当前点击点的信息
	 * 
	 * @param x
	 *            点击点X坐标
	 * @param y
	 *            点击点Y坐标
	 * @return 返回对应的位置记录
	 */
	public PointPosition getPositionRecord(float x, float y) {
		return getPointRecord(x, y);
	}

	// 遍历曲线
	protected void renderBezierCurveLine(Canvas canvas, Paint paint,
			Path bezierPath, List<PointF> lstPoints) {
		if (null == BezierControls)
			BezierControls = new PointF[2];
		paint.setStyle(Style.STROKE);

		int count = lstPoints.size();
		if (count <= 2)
			return; // 没有或仅一个点就不需要了

		if (count == 3) {
			if (null == bezierPath)
				bezierPath = new Path();
			bezierPath.moveTo(lstPoints.get(0).x, lstPoints.get(0).y);
			PointF var8 = PointHelper.percent((PointF)lstPoints.get(1), 0.2F, (PointF)lstPoints.get(2), 0.2F);
            bezierPath.quadTo(var8.x, var8.y, ((PointF)lstPoints.get(2)).x, ((PointF)lstPoints.get(2)).y);
            canvas.drawPath(bezierPath, paint);
            bezierPath.reset();
			return;
		}

		float axisMinValue = plotArea.getBottom();

		for (int i = 0; i < count; i++) {
			if (i < 3)
				continue;

			// 连续两个值都为0,控制点有可能会显示在轴以下，则此种情况下，将其处理为直线
			if (lstPoints.get(i - 1).y >= axisMinValue
					&& lstPoints.get(i).y >= axisMinValue) {
			
				if (null == bezierPath){
					bezierPath = new Path();
				}
				bezierPath.reset();
				bezierPath.moveTo(lstPoints.get(i - 2).x,
						lstPoints.get(i - 2).y);
				
				// change by chenqiang
				if (lstPoints.get(i - 2).y>=axisMinValue) {//连续3个点为0
					bezierPath.lineTo(lstPoints.get(i - 1).x,
							lstPoints.get(i - 1).y);
				}else{
					CurveHelper.curve3(lstPoints.get(i - 2), lstPoints.get(i - 1),
							lstPoints.get(i - 3), lstPoints.get(i), BezierControls);
					bezierPath.quadTo(BezierControls[0].x, BezierControls[0].y,
							lstPoints.get(i - 1).x, lstPoints.get(i - 1).y);
					
					// i-2与i-1之间的曲线
					canvas.drawPath(bezierPath, paint);
					bezierPath.reset();
				}

				// i-1与i之间的直线
				canvas.drawLine(lstPoints.get(i - 1).x, lstPoints.get(i - 1).y,
						lstPoints.get(i).x, lstPoints.get(i).y, paint);

				continue;
			}

			CurveHelper.curve3(lstPoints.get(i - 2), lstPoints.get(i - 1),
					lstPoints.get(i - 3), lstPoints.get(i), BezierControls);

			// change by chenqiang
			renderBezierCurvePath(canvas, paint, bezierPath,
					lstPoints.get(i - 2), lstPoints.get(i - 1), BezierControls);

		}

		if (count > 3) {
			PointF stop = lstPoints.get(count - 1);
			// PointF start = lstPoints.get(lstPoints.size()-2);
			CurveHelper.curve3(lstPoints.get(count - 2), stop,
					lstPoints.get(count - 3), stop, BezierControls);

			renderBezierCurvePath(canvas, paint, bezierPath,
					lstPoints.get(count - 2), lstPoints.get(count - 1),
					BezierControls);
		}

	}

	// 绘制曲线
	private void renderBezierCurvePath(Canvas canvas, Paint paint,
			Path bezierPath, PointF start, PointF stop, PointF[] bezierControls) {
		if (null == bezierPath)
			bezierPath = new Path();
		bezierPath.reset();
		bezierPath.moveTo(start.x, start.y);

//		Log.v(TAG, "start.y = " + start.y + " stop.y = " + stop.y);
//		Log.v(TAG, "bezierControls[0].y = " + bezierControls[0].y
//				+ " bezierControls[1].y = " + bezierControls[1].y);

		// change by chenqiang
		bezierCurvePathAxisMinValue(bezierPath,start,stop, bezierControls);
	
		canvas.drawPath(bezierPath, paint);
		bezierPath.reset();
	}
	
	
	// add by chenqiang
		protected void bezierCurvePathAxisMinValue(Path bezierPath, PointF start,
				PointF stop, PointF[] bezierControls) {
			
			// 平滑贝塞尔
			float wt = (start.x + stop.x) / 2.0F;
	        bezierPath.cubicTo(wt, start.y, wt, stop.y, stop.x, stop.y);

	        // 普通贝塞尔
//			float axisMinValue = plotArea.getBottom();
//			if (start.y >= axisMinValue && stop.y >= axisMinValue) {
//				bezierPath.lineTo(stop.x, stop.y);
//			} else {
//				if (bezierControls[0].y >= axisMinValue
//						&& bezierControls[1].y >= axisMinValue) {
//					bezierPath.lineTo(stop.x, stop.y);
//				} else if (bezierControls[0].y >= axisMinValue
//						&& bezierControls[1].y < axisMinValue) {
//					bezierPath.cubicTo(bezierControls[0].x, axisMinValue,
//							bezierControls[1].x, bezierControls[1].y, stop.x,
//							stop.y);
//				} else if (bezierControls[0].y < axisMinValue
//						&& bezierControls[1].y >= axisMinValue) {
//					bezierPath.cubicTo(bezierControls[0].x, bezierControls[0].y,
//							bezierControls[1].x, axisMinValue, stop.x, stop.y);
//				} else {
//					bezierPath.cubicTo(bezierControls[0].x, bezierControls[0].y,
//							bezierControls[1].x, bezierControls[1].y, stop.x,
//							stop.y);
//				}
//			}
		}

	// ///////////////////////////////////////

	/**
	 * 设置批注
	 * 
	 * @param anchor
	 *            批注
	 */
	public void setAnchorDataPoint(List<AnchorDataPoint> anchor) {
		mAnchorSet = anchor;
	}

	/**
	 * 返回批注
	 * 
	 * @return 批注
	 */
	public List<AnchorDataPoint> getAnchorDataPoint() {
		return mAnchorSet;
	}
	// ///////////////////////////////////////
}
