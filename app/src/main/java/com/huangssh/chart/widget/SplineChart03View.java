package com.huangssh.chart.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import com.huangssh.chart.util.UtilPhoneParam;
import com.huangssh.chart.util.UtilUnitConversion;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.BarPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.XEnum.BarStyle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import chart.huangssh.com.splinechart.BuildConfig;
import chart.huangssh.com.splinechart.R;

public class SplineChart03View extends BaseChartView {

    private String TAG = "SplineChart03View";

    // 柱形图
    private BarChart barChart = new BarChart();
    private LinkedList<String> barLabels = new LinkedList<String>();
    private List<BarData> barChartDataset = new LinkedList<BarData>();

    private SplineChart splineChart = new SplineChart();

    // 分类轴标签集合
    private LinkedList<String> labels = new LinkedList<String>();
    private List<Integer> months = new ArrayList<Integer>();
    private LinkedList<SplineData> chartData = new LinkedList<SplineData>();

    // splinechart支持横向和竖向定制线
    private List<CustomLineData> mXCustomLineDataset = new ArrayList<CustomLineData>();
    private List<CustomLineData> mYCustomLineDataset = new ArrayList<CustomLineData>();
    private CustomLineData cdy1;

    // 曲线图数据集合
    private List<PointD> linePoint1;
    private SplineData dataSeries1;
    // 标签对应的柱形数据集
    List<Double> dataSeries2 = new LinkedList<Double>();
    // 平均值
    private Double average;
    // 最大值
    double newMaxNum;

    // 点击的第一点
    int firstX = 0;
    // 第二点
    int secondX;
    // 曲线标签画笔
    Paint paintLable = new Paint();
    // 均值文字画笔
    Paint paintText = new Paint();
    // 是否第一次进入
    private boolean isFirst = true;

    public SplineChart03View(Context context) {
        super(context);
        initView();
    }

    public SplineChart03View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SplineChart03View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        // 横坐标数据集
        chartLabels();
        barChartLabels();
        chartCustomeLines();

        // 柱形图数据集
        barChartDataSet();
        // 曲线图数据集
        splineChartDataSet();

        // 绘制图
        barChartRender();
        splineChartRender();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 柱形图
        barChart.setChartRange(w, h);
        // 图所占范围大小
        splineChart.setChartRange(w, h);
    }

    private void barChartRender() {
        try {
            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            barChart.setPadding(ltrb[0], ltrb[1],
                    ltrb[2], ltrb[3]);

            barChart.setChartDirection(XEnum.Direction.VERTICAL);
            // 轴
            renderBarAxis();
            // 隐藏圆柱标签
            barChart.getBar().setItemLabelVisible(false);
            //激活点击监听
            barChart.ActiveListenItemClick();
            barChart.showClikedFocus();

            // 隐蔽图例
            barChart.getPlotLegend().hide();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 柱形图轴相关
     */
    private void renderBarAxis() {
        // 标签轴
        barChart.setCategories(barLabels);
        // 数据轴
        barChart.setDataSource(barChartDataset);
        barChart.getDataAxis().setAxisMax(100);
        barChart.getDataAxis().setAxisSteps(10);

        // 隐藏柱形图的横坐标
        barChart.getDataAxis().hide();
        barChart.getCategoryAxis().hide();


        barChart.getBar().setBarTickSpacePercent(1);
        barChart.getBar().setBarInnerMargin(0);

        barChart.getPlotGrid().hideEvenRowBgColor();
        barChart.getPlotGrid().hideHorizontalLines();
        barChart.getPlotGrid().hideOddRowBgColor();
        barChart.getPlotGrid().hideVerticalLines();

        barChart.getBar().setBarStyle(BarStyle.STROKE);
        barChart.setApplyBackgroundColor(true);
        barChart.setBackgroundColor(Color.WHITE);
    }

    /**
     * 曲线图轴相关
     */
    private void splineChartRender() {
        try {
            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            splineChart.setPadding(ltrb[0],
                    ltrb[1], ltrb[2], ltrb[3]);

            // 显示边框
            // splineChart.showRoundBorder();

            // 数据源
            splineChart.setCategories(labels);
            splineChart.setDataSource(chartData);

            // 坐标系
            // y轴最大值
            splineChart.getDataAxis().setAxisMax(100);
            // chart.getDataAxis().setAxisMin(0);
            // y轴刻度间隔
            splineChart.getDataAxis().setAxisSteps(10);
            // 隐藏y轴坐标刻度
            splineChart.getDataAxis().hide();
            splineChart.setCustomLines(mYCustomLineDataset); // y轴

            // x轴最大值
            splineChart.setCategoryAxisMax(100);
            // x轴最小值
            splineChart.setCategoryAxisMin(0);
            // chart.setCustomLines(mXCustomLineDataset); //y轴
            splineChart.setCategoryAxisCustomLines(mXCustomLineDataset); // x轴

            // 设置图的背景色
//			splineChart.setApplyBackgroundColor(true);
//			 splineChart.setBackgroundColor(Color.WHITE);
            splineChart.getBorder()
                    .setBorderLineColor(Color.WHITE);

            // 调轴线与网络线风格
            splineChart.getCategoryAxis().hideTickMarks();
            splineChart.getDataAxis().hideAxisLine();
            splineChart.getDataAxis().hideTickMarks();

            // 横坐标风格
            splineChart.getCategoryAxis().getTickLabelPaint().setTextSize(UtilUnitConversion.dip2px(getContext(), 12));
            splineChart.getCategoryAxis().getTickLabelPaint().setColor(this.getContext().getResources().getColor(R.color.bills_textcolor));
            splineChart.getCategoryAxis().setTickLabelMargin(30);

            // 显示水平网格线
            splineChart.getPlotGrid().showHorizontalLines();
            splineChart.getPlotGrid().showVerticalLines();
            // chart.hideTopAxis();
            // chart.hideRightAxis();

            // 垂直网格线
            splineChart.getPlotGrid().getVerticalLinePaint()
                    .setColor(Color.rgb(233, 233, 233));
            splineChart.getPlotGrid().getVerticalLinePaint()
                    .setStrokeWidth(0.1f);
            // 水平网格线
            splineChart.getPlotGrid().getHorizontalLinePaint()
                    .setStrokeWidth(0.1f);
            splineChart.getPlotGrid().getHorizontalLinePaint()
                    .setColor(Color.rgb(233, 233, 233));

            // 轴线
            splineChart
                    .getCategoryAxis()
                    .getAxisPaint()
                    .setColor(
                            splineChart.getPlotGrid().getHorizontalLinePaint()
                                    .getColor());
            splineChart
                    .getCategoryAxis()
                    .getAxisPaint()
                    .setStrokeWidth(
                            splineChart.getPlotGrid().getHorizontalLinePaint()
                                    .getStrokeWidth());

            // 设置曲线图横坐标居中显示
            splineChart.setBarCenterStyle(XEnum.BarCenterStyle.SPACE);

            // 定义交叉点标签显示格式,特别备注,因曲线图的特殊性，所以返回格式为: x值,y值
            // 请自行分析定制
            splineChart.setDotLabelFormatter(new IFormatterTextCallBack() {

                @Override
                public String textFormatter(String value) {
                    String label = value.substring(value.indexOf(",") + 1,
                            value.length());
                    DecimalFormat df = new DecimalFormat("0.00");
                    label = df.format(Double.parseDouble(label));
                    return (label);
                }
            });

            // 标题
            // chart.setTitle("登录号码历史话费");
            // 副标题
            // chart.addSubtitle("(XCL-Charts Demo)");

            // 扩展绘图区左右边分割的范围，让定制线的说明文字能显示出来
            splineChart.getClipExt().setExtLeft(250.f);
            splineChart.getClipExt().setExtRight(250.f);
            splineChart.getClipExt().setExtTop(250.f);

            // 激活点击监听
            splineChart.ActiveListenItemClick();
            // 为了让触发更灵敏，可以扩大5px的点击监听范围
            splineChart.extPointClickRange(30);
            splineChart.showClikedFocus();

            // 显示折线/平滑曲线
            splineChart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);

            // 图例显示在正下方
            splineChart.getPlotLegend().setVerticalAlign(
                    XEnum.VerticalAlign.BOTTOM);
            splineChart.getPlotLegend().setHorizontalAlign(
                    XEnum.HorizontalAlign.CENTER);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 柱形图数据集合
     */
    private void barChartDataSet() {
        dataSeries2.add(100d);
        BarData barDataA = new BarData("Virtual OPM", dataSeries2, Color.rgb(255, 255, 255));
        barChartDataset.add(barDataA);
    }

    /**
     * 刷新数据
     *
     * @param linePoint1
     */
    public void refreshChart(List<PointD> linePoint1, Double average, double maxNum) {
        this.linePoint1.clear();
        this.linePoint1.addAll(linePoint1);
        dataSeries1.setLineDataSet(linePoint1);
        // 设置平均值
        cdy1.setValue(average);

        newMaxNum = maxNum + maxNum / 9 * 2;
        splineChart.getDataAxis().setAxisMax(newMaxNum);
        // y轴六个刻度值
        splineChart.getDataAxis().setAxisSteps(newMaxNum / 11);

        // 设置按下效果和步进
        barChart.getDataAxis().setAxisMax(newMaxNum);
        barChart.getDataAxis().setAxisSteps(newMaxNum / 6);

        // 柱形图数据
        dataSeries2.clear();
        for (int i = 0; i < 5; i++) {
            dataSeries2.add(newMaxNum);
        }
        BarData barDataA = new BarData("Virtual OPM", dataSeries2, Color.rgb(255, 255, 255));
        barChartDataset = new LinkedList<BarData>();
        barChartDataset.add(barDataA);
        barChart.setDataSource(barChartDataset);
        this.average = average;
        // 重置首次进入为true
        this.isFirst = true;
        this.invalidate();
    }

    /**
     * 曲线图数据集合
     */
    private void splineChartDataSet() {
        // 线1的数据集
        linePoint1 = new ArrayList<PointD>();

        // linekey
        dataSeries1 = new SplineData("", linePoint1, Color.WHITE);

        // 设置点的风格
        dataSeries1.setDotStyle(XEnum.DotStyle.HIDE);
        // 点的颜色
        dataSeries1.getDotPaint().setColor(getContext().getResources().getColor(R.color.spline_color));
        dataSeries1.getPlotLine().getPlotDot()
                .setRingInnerColor(Color.rgb(54, 141, 238));
        // 曲线的粗细
        dataSeries1.getLinePaint().setStrokeWidth(UtilUnitConversion.dip2px(getContext(), 4.5f));
        dataSeries1.getLinePaint().setStrokeCap(Paint.Cap.ROUND);
        dataSeries1.getLinePaint().setColor(getContext().getResources().getColor(R.color.orange1));

        // 是否显示标签
        dataSeries1.setLabelVisible(false);

        // 设置round风格的标签
        dataSeries1.getLabelOptions()
                .setLabelBoxStyle(XEnum.LabelBoxStyle.TEXT);
        dataSeries1.getLabelOptions().setOffsetY(50);
        dataSeries1.getDotLabelPaint().setTextSize(UtilUnitConversion.dip2px(getContext(), 15));
        dataSeries1.getDotLabelPaint().setColor(Color.rgb(241, 97, 63));

        // 设定数据源
        chartData.add(dataSeries1);
    }

    /**
     * 曲线图横坐标
     */
    private void chartLabels() {
        int month = getMonthNow() - 5;
        if (month <= 0) {
            month = month + 12;
        }
        labels.add("");
        labels.add(month + "月");
        months.clear();
        months.add(month);
        for (int i = 0; i < 4; i++) {
            month = getMonthNext(month);
            labels.add(month + "月");
            months.add(month);
        }
    }

    // 柱状图横坐标（必须保留，否则监听事件不起作用）
    private void barChartLabels() {
        int month = getMonthNow() - 5;
        if (month <= 0) {
            month = month + 12;
        }
        barLabels.add(month + "月");
        for (int i = 0; i < 4; i++) {
            month = getMonthNext(month);
            barLabels.add(month + "月");
        }
    }

    private int getMonthNow() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    private int getMonthNext(int month) {
        if (month == 12)
            return 1;
        else {
            return month + 1;
        }
    }

    /**
     * 均值线
     */
    private void chartCustomeLines() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.line_chart);
        // 适配表格
        Matrix matrix = new Matrix();
        // 高度不变，缩放宽度
        matrix.postScale(((float)(UtilPhoneParam.screenWidth - UtilUnitConversion.dip2px(getContext(),30))) / bmp.getWidth(), 1);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        cdy1 = new CustomLineData(0.0, newbm);
        cdy1.setLineStyle(XEnum.LineStyle.BITMAP);
        mYCustomLineDataset.add(cdy1);
    }

    @Override
    public void render(Canvas canvas) {
        try {
            barChart.render(canvas);
            splineChart.render(canvas);

            // 平均值标签文字设置
            paintLable.setAntiAlias(true);
            paintLable.setColor(Color.WHITE);
            paintLable.setTextSize(UtilUnitConversion.dip2px(getContext(), 12));
            // 获取标签背景
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_sp_lable);

            // 平均值字体缩放
            float textWidth = paintLable.measureText(UtilUnitConversion.keepTwoDecimalPlaces(average)) + UtilUnitConversion.dip2px(getContext(), 10);
            Matrix matrix = new Matrix();
            matrix.postScale(textWidth / bmp.getWidth(), 1);
            // 得到新的图片
            Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            // 平均值线标签背景 top 高度为(1-平均值/100)
            canvas.drawBitmap(
                    newbm,
                    splineChart.getPlotArea().getLeft(),
                    (splineChart.getPlotArea().getBottom() - splineChart.getPlotArea().getTop())
                            * (Float.parseFloat(newMaxNum + "") - Float.parseFloat(average + ""))
                            / Float.parseFloat(newMaxNum + "") + splineChart.getPlotArea().getTop()
                            - newbm.getHeight() - UtilUnitConversion.dip2px(getContext(), 6), null);
            // 设置平均值
            canvas.drawText(
                    UtilUnitConversion.keepTwoDecimalPlaces(average),
                    splineChart.getPlotArea().getLeft() + UtilUnitConversion.dip2px(getContext(), 5),
                    (splineChart.getPlotArea().getBottom() - splineChart.getPlotArea().getTop())
                            * (Float.parseFloat(newMaxNum + "") - Float.parseFloat(average + ""))
                            / Float.parseFloat(newMaxNum + "") + splineChart.getPlotArea().getTop()
                            - bmp.getHeight() * 10 / 16, paintLable);

            // 设置曲线上的文字
            paintText.setAntiAlias(true);
            paintText.setColor(Color.rgb(241, 97, 63));
            paintText.setTextSize(UtilUnitConversion.dip2px(getContext(), 12));

            // 设置曲线标签文字的显示位置
            for (int i = 0; i < linePoint1.size(); i++) {

                String text = UtilUnitConversion.keepTwoDecimalPlaces(linePoint1.get(i).y);
                // 文字长度的一半设为偏移量
                float offX = paintText.measureText(text) / 2;
                // 高度偏移量
                int offy = 35;
                if ((int) linePoint1.get(i).y == 0) {
                    canvas.drawText(text, splineChart.getmLstDotInfo().get(i).mX - offX, splineChart.getmLstDotInfo().get(i).mY - offy, paintText);
                    continue;
                }

                if (i == 0) {
                    if (linePoint1.get(i).y < linePoint1.get(i + 1).y) {
                        canvas.drawText(text, splineChart.getmLstDotInfo().get(i).mX - offX, splineChart.getmLstDotInfo().get(i).mY + offy + 15, paintText);
                    } else {
                        canvas.drawText(text, splineChart.getmLstDotInfo().get(i).mX - offX, splineChart.getmLstDotInfo().get(i).mY - offy, paintText);
                    }
                } else if (i == linePoint1.size() - 1) {
                    if (linePoint1.get(i).y < linePoint1.get(i - 1).y) {
                        canvas.drawText(text, splineChart.getmLstDotInfo().get(i).mX - offX, splineChart.getmLstDotInfo().get(i).mY + offy + 15, paintText);
                    } else {
                        canvas.drawText(text, splineChart.getmLstDotInfo().get(i).mX - offX, splineChart.getmLstDotInfo().get(i).mY - offy, paintText);
                    }
                } else {
                    if (linePoint1.get(i).y < linePoint1.get(i + 1).y && linePoint1.get(i).y < linePoint1.get(i - 1).y) {
                        canvas.drawText(text, splineChart.getmLstDotInfo().get(i).mX - offX, splineChart.getmLstDotInfo().get(i).mY + offy + 15, paintText);
                    } else {
                        canvas.drawText(text, splineChart.getmLstDotInfo().get(i).mX - offX, splineChart.getmLstDotInfo().get(i).mY - offy, paintText);
                    }
                }
            }

            // 渐变
			LinearGradient gradient = new LinearGradient(splineChart.getmLstDotInfo().get(0).mX, splineChart.getmLstDotInfo().get(0).mY,
					splineChart.getmLstDotInfo().get(4).mX, splineChart.getmLstDotInfo().get(4).mY, Color.rgb(241, 97, 63), Color.rgb(
					247, 233, 0),  Shader.TileMode.MIRROR);
			dataSeries1.getLinePaint().setShader(gradient);
            // 只有第一次进入和切换用户级的时候才刷新
            if (isFirst) {
                this.invalidate();
                isFirst = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                triggerClick(event.getX(), event.getY());
                firstX = (int) event.getX();
                break;
            }
            // 抬起后跳转
            case MotionEvent.ACTION_UP: {
                secondX = (int) event.getX();
                int distance = secondX - firstX;

                if (Math.abs(distance) < 50) {
//					intentClick(event.getX(), event.getY());

                    // 事件监听
                    if (mClickListener != null) {
                        mClickListener.onClickListener(event.getX(), event.getY(), barChart, months);
                    }
                }
                barChart.getFocusPaint().setColor(Color.WHITE);
                this.invalidate();

                break;
            }
            // 取消后还原背景
            case MotionEvent.ACTION_CANCEL: {
                barChart.getFocusPaint().setColor(Color.WHITE);
                this.invalidate();
                break;
            }
        }
        return true;
    }

    // 设置按下颜色
    private void triggerClick(float x, float y) {
        BarPosition record = barChart.getPositionRecord(x, y);
        if (null == record)
            return;

        barChart.showFocusRectF(record.getRectF());
        barChart.getFocusPaint().setStyle(Style.FILL);
        barChart.getFocusPaint().setStrokeWidth(3);
        barChart.getFocusPaint().setColor(Color.rgb(250, 250, 250));
        this.invalidate();
    }

    // 回调接口
    public interface OnClickListener {
        public void onClickListener(float x, float y, BarChart barChart, List<Integer> months);
    }

    private OnClickListener mClickListener;

    public void setOnClickListener(OnClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            // 滑动事件交给父view
            case MotionEvent.ACTION_MOVE: {
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
