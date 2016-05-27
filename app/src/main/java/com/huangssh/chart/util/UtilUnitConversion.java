package com.huangssh.chart.util;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 单位转换工具类
 * 
 * @作者 linwen@ffcs.cn
 * @创建时间 2015年8月12日 上午9:47:03
 */
public class UtilUnitConversion {

	/**
	 * 将字节转成 G 或 MB
	 * 
	 * @作者 
	 * @创建时间   
	 * @param size
	 * @return Charsequence
	 */
	public static CharSequence convertFileSize(long size) {
		long mb = 1024;
		long gb = mb * 1024;

		if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f G" : "%.1f G", f);
		} else {
			return String.valueOf(size) + "MB";
		}
	}

	public static String keepTwoDecimalPlaces(String value){
		DecimalFormat df = new DecimalFormat("#0.00");
		return  df.format((Double.valueOf(value)));
	}

	public static String keepTwoDecimalPlaces(Double value){
		DecimalFormat df = new DecimalFormat("#0.00");
		return  df.format((value));
	}


	/**
	 * dip 装换成 px
	 * 
	 * @作者 zhuofq
	 * @创建时间   
	 * @param context
	 * @param dipValue
	 * @return int
	 */
	public static int dip2px(Context context, float dipValue) {
		int num = 0;
		if (context != null) {
			final float scale = context.getResources().getDisplayMetrics().density;
			num = (int) (dipValue * scale + 0.5f);
		}
		return num;
	}

	/**
	 * px 转换成 dip
	 * 
	 * @作者 zhuofq
	 * @创建时间   
	 * @param context
	 * @param pxValue
	 * @return int
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * unit G
	 */
	public static String FLOW_UNIT_G = "G";
	/**
	 * unit M
	 */
	public static String FLOW_UNIT_M = "M";
	/**
	 * unit K
	 */
	public static String FLOW_UNIT_K = "K";
	
	/**
	 * unit GB
	 */
	public static String FLOW_UNIT_GB = "GB";
	/**
	 * unit MB
	 */
	public static String FLOW_UNIT_MB = "MB";
	/**
	 * unit KB
	 */
	public static String FLOW_UNIT_KB = "KB";
	
	private static String FLOW_UNITS[] = {FLOW_UNIT_K, FLOW_UNIT_M, FLOW_UNIT_G};
	private static String FLOW_UNITS_B[] = {FLOW_UNIT_KB, FLOW_UNIT_MB,FLOW_UNIT_GB};
	private static int FLOW_KEEP[] = {1, 1, 10};//总流量、已用流量、剩余流量对应保留位数
	private static int FLOW_AVG_KEEP[] = {10, 10, 100};//日均等计算值保留位数
	
	/**
	 * 
	 * 根据传入流量的参数，进行单位换算，并返回合适的单位
	 * @author zhangyi
	 * @createTime 2014-7-12 下午3:10:50  
	 * @param flow  流量值
	 * @param isMB   该流量的单位是否是MB
	 * @param decimal  保留小数点后几位
	 * @return	String
	 */
	public static String getFlowFromByte(double flow, boolean isMB, int decimal){
		String strFlow = null;
		if(isMB){
			if(flow>=1024){
				double flowGB;
				flowGB=flow/1024;
				NumberFormat nf = NumberFormat.getNumberInstance();
		        nf.setMaximumFractionDigits(decimal);
		        strFlow=nf.format(flowGB)+"G";
			}else{
				strFlow=(int)flow+"M";
			}
		}else{
	        strFlow=(int)flow+"G";
		}
		return strFlow;
	}
	
	/**
	 * 流量单位转换
	 * 
	 * @author jiangwenxin
	 * @createTime 2014年9月15日 下午4:32:40  
	 * @param flowByte 流量值 单位KB
	 * @param decimal 小数最大位数(自动去除小数末尾0)
	 * @return String	合适单位值 如3.5G, 3.5M, 3.5K
	 */
	public static String getFlowFromByte(double flowByte, int decimal){
		String strFlow = null;
		String ceils[] = {"K","M","G","T"};
		int i=0;
		for(;flowByte>=1024 && i<ceils.length-1;i++){
			flowByte = flowByte/1024.0;
		}
		
		NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(decimal);
        strFlow=nf.format(flowByte)+ceils[i];
        
		return strFlow;
	}
	
	/**
	 * 获得流量值 （与getFlowFromByte方法相反）
	 * 
	 * @author jiangwenxin
	 * @createTime 2015年3月18日 上午9:55:53  
	 * @param flow 如3.5G, 3.5M, 3.5K
	 * @return double	 流量值 单位KB
	 */
	public static double getFlowKByte(String flow){
		try{
			String ceils[] = {"K","M","G","T"};
			flow = flow.replaceAll(",", "");
			String num = flow.substring(0,flow.length()-1);
			double flowkb = Double.parseDouble(num);
			for(int i=0;i<ceils.length; i++){
				String a = ceils[i];
				String s = flow.substring(flow.length()-1);
				if(a.equals(s)){
					return flowkb;
				}else{
					flowkb*=1024;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 流量单位转换(返回单位MB)
	 * 
	 * @author zhangyi
	 * @createTime 2014年9月22日 下午3:32:40  
	 * @param flowByte 流量值 单位KB
	 * @param decimal 小数最大位数(自动去除小数末尾0)
	 * @return double	返回单位统一MB，而且只返回值不带单位 如3.5M就返回3
	 */
	public static String getFlowFromByteWithoutMB(double flowByte, int decimal){
		String strFlow = null;
		double flowByteDouble = 0;
		flowByteDouble = flowByte/1024;
		strFlow= String.format("%." + decimal + "f", flowByteDouble);
		return strFlow;
	}

}
