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

package org.xclcharts.common;

import java.util.Random;

import org.xclcharts.chart.R;
import org.xclcharts.renderer.XEnum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;

/**
 * @ClassName DrawHelper
 * @Description 集中了绘制中，相关的一些小函数
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 * 
 */

public class DrawHelper {

	private static DrawHelper instance = null;

	private RectF mRectF = null;
	private Path mPath = null;
	private Paint mPaint = null;

	public DrawHelper() {
	}

	public static synchronized DrawHelper getInstance() {
		if (instance == null) {
			instance = new DrawHelper();
		}
		return instance;
	}

	private void initRectF() {
		if (null == mRectF)
			mRectF = new RectF();
	}

	private void initPath() {
		if (null == mPath) {
			mPath = new Path();
		} else {
			mPath.reset();
		}
	}

	private void initPaint() {
		if (null == mPaint) {
			mPaint = new Paint();
		} else {
			mPaint.reset();
		}
	}

	/**
	 * 得到一个随机颜色
	 * 
	 * @return 随机颜色
	 */
	public int randomColor() {
		Random random = new Random();
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);
		return Color.rgb(red, green, blue);
	}

	/**
	 * 通过透明度，算出对应颜色的浅色应当是什么效果
	 * 
	 * @param color
	 *            颜色
	 * @param alpha
	 *            透明度
	 * @return 浅色
	 */
	public int getLightColor(int color, int alpha) {
		initPaint();
		mPaint.setColor(color);
		// mPaint.setAlpha(alpha);
		return mPaint.getColor();
	}

	/**
	 * 得到深色
	 * 
	 * @param color
	 *            颜色
	 * @return 深色
	 */
	public int getDarkerColor(int color) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[1] = hsv[1] + 0.1f;
		hsv[2] = hsv[2] - 0.1f;
		int darkerColor = Color.HSVToColor(hsv);
		return darkerColor;
	}

	/**
	 * 得到单个字的高度
	 * 
	 * @param paint
	 *            画笔
	 * @return 高度
	 */
	public float getPaintFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return (float) Math.ceil(fm.descent - fm.ascent);
	}

	/**
	 * 得到字符串的宽度
	 * 
	 * @param paint
	 *            画笔
	 * @param str
	 *            字符串
	 * @return 宽度
	 */
	public float getTextWidth(Paint paint, String str) {
		if (str.length() == 0)
			return 0.0f;
		// float width = Math.abs(paint.measureText(str, 0, str.length()));
		return paint.measureText(str, 0, str.length());
	}

	/**
	 * 用于计算文字的竖直累加高度
	 * 
	 * @param paint
	 *            画笔
	 * @param str
	 *            字符串
	 * @return 高度
	 */
	public float calcTextHeight(Paint paint, String str) {
		if (str.length() == 0)
			return 0;
		return getPaintFontHeight(paint) * str.length();
	}

	/**
	 * 绘制旋转了指定角度的文字
	 * 
	 * @param text
	 *            文字
	 * @param x
	 *            X坐标
	 * @param y
	 *            y坐标
	 * @param paint
	 *            画笔
	 * @param angle
	 *            角度
	 */
	public void drawRotateText(String text, float x, float y, float angle,
			Canvas canvas, Paint paint) {
		if ("" == text || text.length() == 0)
			return;

		if (angle != 0) {
			canvas.rotate(angle, x, y);
			// canvas.drawText(text, x, y, paint);
			drawText(canvas, paint, text, x, y);
			canvas.rotate(-1 * angle, x, y);
		} else {
			// canvas.drawText(text, x, y, paint);
			drawText(canvas, paint, text, x, y);
		}
	}

	/**
	 * 绘制等腰三角形
	 * 
	 * @param baseLine
	 *            底线长度
	 * @param baseLnCentX
	 *            底线中心点X坐标
	 * @param baseLnCentY
	 *            底线中心点Y坐标
	 * @param direction
	 *            三角形方向
	 * @param style
	 *            填充风格
	 * @param canvas
	 *            画布
	 * @param paint
	 *            画笔
	 */
	public void drawTrigangle(float baseLine, float baseLnCentX,
			float baseLnCentY, XEnum.TriangleDirection direction,
			XEnum.TriangleStyle style, Canvas canvas, Paint paint) {
		// 计算偏移量
		int offset = (int) (baseLine / 2 * Math.tan(60 * Math.PI / 180));

		initPath();
		// 计算三角形3个顶点的坐标
		switch (direction) {
		case UP: // 向上

			mPath.moveTo(baseLnCentX - baseLine / 2, baseLnCentY);
			mPath.lineTo(baseLnCentX + baseLine / 2, baseLnCentY);
			mPath.lineTo(baseLnCentX, baseLnCentY - offset);
			mPath.close();
			break;

		case DOWN: // 向下

			mPath.moveTo(baseLnCentX - baseLine / 2, baseLnCentY);
			mPath.lineTo(baseLnCentX + baseLine / 2, baseLnCentY);
			mPath.lineTo(baseLnCentX, baseLnCentY + offset);
			mPath.close();

			break;
		case LEFT: // 向左

			mPath.moveTo(baseLnCentX, baseLnCentY - baseLine / 2);
			mPath.lineTo(baseLnCentX, baseLnCentY + baseLine / 2);
			mPath.lineTo(baseLnCentX - offset, baseLnCentY);
			mPath.close();

			break;
		case RIGHT: // 向右

			mPath.moveTo(baseLnCentX, baseLnCentY - baseLine / 2);
			mPath.lineTo(baseLnCentX, baseLnCentY + baseLine / 2);
			mPath.lineTo(baseLnCentX + offset, baseLnCentY);
			mPath.close();
			break;
		}

		// 三角形的填充风格
		switch (style) {
		case OUTLINE: // 空心
			paint.setStyle(Paint.Style.STROKE);
			break;
		case FILL: // FILL
			paint.setStyle(Paint.Style.FILL);
			break;
		}
		canvas.drawPath(mPath, paint);
		mPath.reset();
	}

	public PathEffect getDotLineStyle() {
		return (new DashPathEffect(new float[] { 2, 2, 2, 2 }, 1));
	}

	public PathEffect getDashLineStyle() {
		// 虚实线
		return (new DashPathEffect(new float[] { 4, 8, 5, 10 }, 1));
	}

	/**
	 * 绘制点
	 * 
	 * @param startX
	 *            起始点X坐标
	 * @param startY
	 *            起始点Y坐标
	 * @param stopX
	 *            终止点X坐标
	 * @param stopY
	 *            终止点Y坐标
	 * @param canvas
	 *            画布
	 * @param paint
	 *            画笔
	 */
	public void drawDotLine(float startX, float startY, float stopX,
			float stopY, Canvas canvas, Paint paint) {
		// PathEffect effects = new DashPathEffect(new float[] { 2, 2, 2, 2},
		// 1);
		paint.setPathEffect(getDotLineStyle());
		canvas.drawLine(startX, startY, stopX, stopY, paint);
		paint.setPathEffect(null);
	}

	/**
	 * 绘制中心线
	 * 
	 * @param startX
	 *            起始点X坐标
	 * @param startY
	 *            起始点Y坐标
	 * @param stopX
	 *            终止点X坐标
	 * @param stopY
	 *            终止点Y坐标
	 * @param canvas
	 *            画布
	 * @param paint
	 *            画笔
	 */
	public void drawBitmap(float startX, float startY, float stopX,
			float stopY, Canvas canvas, Paint paint, Bitmap bitmap) {
		canvas.drawBitmap(bitmap, startX, startY, null);
		
//		paint.setPathEffect(getDotLineStyle());
//		canvas.drawLine(startX, startY, stopX, stopY, paint);
//		paint.setPathEffect(null);
	}

	/**
	 * 绘制虚实线
	 * 
	 * @param startX
	 *            起始点X坐标
	 * @param startY
	 *            起始点Y坐标
	 * @param stopX
	 *            终止点X坐标
	 * @param stopY
	 *            终止点Y坐标
	 * @param canvas
	 *            画布
	 * @param paint
	 *            画笔
	 */
	public void drawDashLine(float startX, float startY, float stopX,
			float stopY, Canvas canvas, Paint paint) {
		// 虚实线
		// PathEffect effects = new DashPathEffect(new float[] { 4, 8, 5, 10},
		// 1);
		paint.setPathEffect(getDashLineStyle());
		canvas.drawLine(startX, startY, stopX, stopY, paint);
		paint.setPathEffect(null);
	}

	// 下次应当做的:虚实线 比例的灵活定制,线的阴影渲染
	public void drawLine(XEnum.LineStyle style, float startX, float startY,
			float stopX, float stopY, Canvas canvas, Paint paint, Bitmap bitmap) {

		switch (style) {
		case SOLID:
			canvas.drawLine(startX, startY, stopX, stopY, paint);
			break;
		case DOT:
			drawDotLine(startX, startY, stopX, stopY, canvas, paint);
			break;
		case DASH:
			// 虚实线
			drawDashLine(startX, startY, stopX, stopY, canvas, paint);
			break;
		case BITMAP:
			// 图片线
			drawBitmap(startX, startY, stopX, stopY, canvas, paint, bitmap);
			break;
		}
	}
	
	public void drawLine(XEnum.LineStyle style, float startX, float startY,
			float stopX, float stopY, Canvas canvas, Paint paint) {

		switch (style) {
		case SOLID:
			canvas.drawLine(startX, startY, stopX, stopY, paint);
			break;
		case DOT:
			drawDotLine(startX, startY, stopX, stopY, canvas, paint);
			break;
		case DASH:
			// 虚实线
			drawDashLine(startX, startY, stopX, stopY, canvas, paint);
			
		}
	}

	/**
	 * 绘制图中显示所占比例 的扇区
	 * 
	 * @param paintArc
	 *            画笔
	 * @param cirX
	 *            x坐标
	 * @param cirY
	 *            y坐标
	 * @param radius
	 *            半径
	 * @param startAngle
	 *            偏移角度
	 * @param sweepAngle
	 *            当前角度
	 * @throws Exception
	 *             例外
	 */
	public void drawPercent(Canvas canvas, Paint paintArc, final float cirX,
			final float cirY, final float radius, final float startAngle,
			final float sweepAngle, boolean useCenter) throws Exception {
		try {
			initRectF();
			mRectF.left = cirX - radius;
			mRectF.top = cirY - radius;
			mRectF.right = cirX + radius;
			mRectF.bottom = cirY + radius;
			// 在饼图中显示所占比例
			canvas.drawArc(mRectF, startAngle, sweepAngle, useCenter, paintArc);
			mRectF.setEmpty();
		} catch (Exception e) {
			throw e;
		}
	}

	public void drawPathArc(Canvas canvas, Paint paintArc, final float cirX,
			final float cirY, final float radius, final float startAngle,
			final float sweepAngle) throws Exception {
		try {
			initRectF();
			mRectF.left = cirX - radius;
			mRectF.top = cirY - radius;
			mRectF.right = cirX + radius;
			mRectF.bottom = cirY + radius;
			// 弧形
			initPath();
			mPath.addArc(mRectF, startAngle, sweepAngle);
			canvas.drawPath(mPath, paintArc);
			mRectF.setEmpty();
			mPath.reset();
		} catch (Exception e) {
			throw e;
		}
	}

	// 绘制有换行的文本
	public float drawText(Canvas canvas, Paint paint, String text, float x,
			float y) {
		if (text.length() > 0) {
			if (text.indexOf("\n") > 0) {
				float height = getPaintFontHeight(paint);
				String[] arr = text.split("\n");
				for (int i = 0; i < arr.length; i++) {
					canvas.drawText(arr[i], x, y, paint);
					y += height;
				}
			} else {
				canvas.drawText(text, x, y, paint);
			}
		}
		return y;
	}
}
