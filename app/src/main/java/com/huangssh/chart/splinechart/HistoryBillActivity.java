package com.huangssh.chart.splinechart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huangssh.chart.util.UtilPhoneParam;
import com.huangssh.chart.util.UtilUnitConversion;
import com.huangssh.chart.widget.SplineChart03View;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.PointD;
import org.xclcharts.event.click.BarPosition;

import java.util.ArrayList;
import java.util.List;

import chart.huangssh.com.splinechart.R;

/**
 * 历史话费
 */
public class HistoryBillActivity extends MyActivity {
    private SplineChart03View splineChart03View;

    private TextView tvAverage;
    private TextView tvMax;
    private TextView tvMin, tvTitle;
    private Double minNum = 0.00, maxNum = 0.00, avgNum;
    private boolean isUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilPhoneParam.init(mContext);

        setContentView(R.layout.activity_history_bill);
        initViews();
        getHisBillDate();
    }

    private void initViews() {
        splineChart03View = findView(R.id.spChart);
        tvAverage = findView(R.id.tvAverage);
        tvMax = findView(R.id.tvMax);
        tvMin = findView(R.id.tvMin);
        tvTitle = findView(R.id.tvTitle);
        splineChart03View.setOnClickListener(new SplineChart03View.OnClickListener() {
            @Override
            public void onClickListener(float x, float y, BarChart barChart, List<Integer> months) {
                intentClick(x, y, barChart, months);
            }
        });
    }

    // 跳转
    private void intentClick(float x, float y, BarChart barChart, List<Integer> months) {
        BarPosition record = barChart.getPositionRecord(x, y);
        if (null == record)
            return;

        String month = months.get(record.getDataChildID()) + "";
        Toast.makeText(mContext,  month + "月", Toast.LENGTH_SHORT).show();
    }

    private void initViews(double arrBill[]) {
        // 瞄点值 BillAmount1代表最近的月份，应显示在曲线的最右边，BillAmount5代表最远的月份，应显示在曲线的最左边
        List<PointD> linePoint1 = new ArrayList<PointD>();
        linePoint1.add(new PointD(10d, arrBill[4]));
        linePoint1.add(new PointD(30d, arrBill[3]));
        linePoint1.add(new PointD(50d, arrBill[2]));
        linePoint1.add(new PointD(70d, arrBill[1]));
        linePoint1.add(new PointD(90d, arrBill[0]));

        Double sum = 0.00;
        minNum = arrBill[0];
        for (int i = 0; i < arrBill.length; i++) {
            sum += arrBill[i];
            if (arrBill[i] > maxNum) {
                maxNum = arrBill[i];
            }
            if (arrBill[i] < minNum) {
                minNum = arrBill[i];
            }
        }
        avgNum = sum / arrBill.length;
        splineChart03View.refreshChart(linePoint1, avgNum, maxNum);

        tvAverage.setText(UtilUnitConversion.keepTwoDecimalPlaces(avgNum));
        tvMin.setText(UtilUnitConversion.keepTwoDecimalPlaces(minNum));
        tvMax.setText(UtilUnitConversion.keepTwoDecimalPlaces(maxNum));
    }

    private void getHisBillDate() {
        double arrBill[] = new double[]{
                75.0,
                40.0,
                70.0,
                50.0,
                90.0,
        };
        initViews(arrBill);
    }


}
