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

/**
 * @ClassName XEnum
 * @Description 枚举定义
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class XEnum {
	
		/**
		 * 横向显示位置,靠左，中间，还是靠右(如图标题......)
		 * @author XCL
		 *
		 */
		public enum HorizontalAlign {
			LEFT, CENTER ,RIGHT
		}			
		
		/**
		 * 竖向显示位置,上方，中间，底部 (如坐标轴标签......)
		 * @author XCL
		 *
		 */
		public enum VerticalAlign
		{
			TOP,MIDDLE,BOTTOM
		}
		
		/**
		 * 设置轴线风格
		 * @author XCL
		 *
		 */
		public enum AxisLineStyle
		{
			NONE,CAP,FILLCAP
		}
			
		/**
		 * 对于圆形的图，如饼图之类，用来确定标签的显示位置
		 * @author XCL  
		 *
		 */
		public enum SliceLabelStyle{		
			HIDE,INSIDE,OUTSIDE,BROKENLINE
		}
				
		/**
		 * 是显示全圆还是半圆
		 * @author XCL
		 *
		 */
		public enum CircleType {		
			FULL,HALF
		}
				
		
		/**
		 * 三角形的朝向
		 * @author XCL
		 *
		 */
		public enum TriangleDirection {
			UP,DOWN,LEFT,RIGHT
		}
		 
		/**
		 * 三角形填充风格 
		 * @author XCL
		 *
		 */
		public enum TriangleStyle {
			OUTLINE,FILL 
		}

		
		/**
		 * 线的几种显示风格:Solid、Dot、Dash
		 * @author XCL
		 *
		 */
		public enum LineStyle {
			SOLID,DOT,DASH, BITMAP
		}
		
		/**
		 * 框的类型
		 * @author XCL
		 *
		 */
		public enum RectType {
			RECT,ROUNDRECT
		}
		
		/**
		 * 标签标注框形状
		 * @author XCL
		 *
		 */
		public enum LabelBoxStyle{
			TEXT,RECT,CIRCLE,CAPRECT,ROUNDRECT,CAPROUNDRECT
		}
		
		
		/**
		 * 用于设定柱形的风格，仅对flatbar有效
		 * @author XCL
		 *
		 */
		public enum BarStyle {
			GRADIENT,FILL,STROKE,OUTLINE,TRIANGLE,ROUNDBAR
		}
		
		/**
		 * 柱形居中位置,依刻度线居中或依刻度中间点居中。
		 * @author XCL
		 *
		 */
		public enum BarCenterStyle {
			TICKMARKS, SPACE
		}	
		
		/**
		 * 横向或竖向网格线
		 * @author XCL
		 *
		 */
		public enum Direction {
			HORIZONTAL, VERTICAL
		}
		

		/**
		 * 轴标记类别:刻度线/标签
		 * @author XCL
		 *
		 */
		public enum TickType {
			MARKS,LABELS
		}
		

		
		/**
		 * 图例类型 
		 * @author XCL
		 *
		 */
		public enum LegendType
		{
			COLUMN,ROW
		}
									
		
		
		/**
		 * 线的类型，默认的直线还是贝塞尔曲线(Bézier curve)
		 * @author XCL
		 *
		 */
		public enum CrurveLineStyle
		{			
			BEELINE,BEZIERCURVE
		}			
		
		/**
		 * 点的类型，隐藏，三角形,方形,实心圆,空心圆,棱形
		 * @author XCL
		 * @param HIDE	隐藏，不显示点
		 * @param TRIANGLE	三角形
		 * @param RECT	方形
		 * @param DOT	圆点
		 * @param RING	圆环
		 * @param PRISMATIC	棱形
		 */
		public enum DotStyle {		
			HIDE,TRIANGLE,RECT,DOT,RING,RING2,PRISMATIC,X,CROSS
		}
		
		/**
		 * 批注背景形状
		 * @author XCL
		 *
		 */
		public enum AnchorStyle{
			RECT,CAPRECT,ROUNDRECT,CAPROUNDRECT,CIRCLE,VLINE,HLINE,TOBOTTOM,TOTOP,TOLEFT,TORIGHT
		}
		
		
		/**
		 * 数据区域的填充方式(雷达图/批注)
		 * @author XCL
		 *
		 */
		public enum DataAreaStyle {
			FILL,STROKE
		}
		
		/**
		 * 设置圆形轴的类型
		 * @author XCL
		 *
		 */
		//DialChart RoundAxis
		public enum RoundAxisType {			
			TICKAXIS,RINGAXIS,ARCLINEAXIS,FILLAXIS,CIRCLEAXIS,LINEAXIS
		}
		

		/**
		 * 位置
		 * @author XCL
		 *
		 */
		public enum Location {  
			TOP,BOTTOM,LEFT,RIGHT
		}
		
		/**
		 * 轴位置
		 * @author XCL
		 *
		 */
		public enum AxisLocation {  
			TOP,BOTTOM,LEFT,RIGHT,HORIZONTAL_CENTER, VERTICAL_CENTER
		}
		
		
		/**
		 * 指针类型
		 * @author XCL
		 *
		 */
		public enum PointerStyle {			
			TRIANGLE,LINE
		}
		
		/**
		 * 设置圆形Tick轴的类型
		 * @author XCL
		 *
		 */
		public enum RoundTickAxisType {			
			INNER_TICKAXIS,OUTER_TICKAXIS
		}
		
		/**
		 * 雷达图显示类型(蛛网或圆形)
		 * @author XCL
		 *
		 */
		public enum RadarChartType{
			RADAR,ROUND
		}
		
		/**
		 * 手势平移模式(模向，纵向，自由移动)
		 * @author XCL
		 *
		 */
		public enum PanMode {  
			HORIZONTAL,VERTICAL,FREE
		}
		
		
		/**
		 * 动态边框风格
		 * @author XCL
		 *
		 */
		public enum DyInfoStyle {			
			RECT,ROUNDRECT,CAPRECT,CIRCLE,CAPROUNDRECT //,ELLIPSE
		}
		
		/**
		 * 动态线类型 
		 * Cross 指定交叉的水平线和垂直线。
		 * BackwardDiagonal 从右上到左下的对角线的线条图案。
		 * Vertical		垂直线
		 * Horizontal 水平线
		 * @author XCL
		 *
		 */
		public enum DyLineStyle{
			Cross,BackwardDiagonal,Vertical,Horizontal
		}
		
		/**
		 * 标签线是否显示圆点及显示风格
		 * @author XCL
		 *
		 */
		public enum LabelLinePoint{
			NONE,BEGIN,END,ALL
		}
		
		
		/**
		 * 标签信息的保存类型 
		 * 	ONLYPOSITION : 保存坐标信息，但不显示标签
		 *  ALL : 保存坐标信息，也显示标签
		 * @author XCL
		 *
		 */
		public enum LabelSaveType{
			NONE,ONLYPOSITION,ALL
		}
		
		/**
		 * 轴标题显示几格.
		 * NORMAL 显示在轴中间
		 * ENDPOINT 显示在轴的结束点及交叉点(仅支持左下点)位置
		 * @author XCL
		 *
		 */
		public enum AxisTitleStyle{
			NORMAL,ENDPOINT
		}
		
		/**
		 * 排序类型 
		 * @author XCL
		 *
		 */
		public enum SortType {		
			NORMAL,ASC,DESC
		}
		
		/**
		 * 标签显示方式
		 * @author XCL
		 *
		 */
		public enum LabelLineFeed {		
			NORMAL,ODD_EVEN,EVEN_ODD
		}
		
		public enum ODD_EVEN {		
			ODD,EVEN,NONE
		}
		
		//柱形标签位置
		public enum ItemLabelStyle {			
			NORMAL,INNER,OUTER,BOTTOM
		}
				
		/**
		 * 图的所属类型
		 * @author XCL
		 *
		 */
		public enum ChartType{
			NONE,BAR,BAR3D,STACKBAR,PIE,PIE3D,DOUNT,LINE,SPLINE,AREA,
			ROSE,RADAR,DIAL,RANGEBAR,ARCLINE,CIRCLE,SCATTER,BUBBLE,GAUGE,FUNNEL
		}
		
}
