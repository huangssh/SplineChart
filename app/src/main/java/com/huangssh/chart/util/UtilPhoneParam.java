package com.huangssh.chart.util;

import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** 
 * 手机参数 
 * 
 * @ClassName: UtilPhoneParam 
 * @author linwen@ffcs.cn 
 * @date 2015年7月30日 上午11:12:09 
 *  
 */
public class UtilPhoneParam {
	
	private final String TAG = UtilPhoneParam.class.getName();
	
	public final static int SCREEN_ORIENTATION_VERTICAL = 1; // 屏幕状态：横屏
	public final static int SCREEN_ORIENTATION_HORIZONTAL = 2; // 屏幕状态：竖屏
	
    public static int screenWidth;// 屏幕宽度，单位为px
    public static int screenHeight;// 屏幕高度，单位为px
    public static int densityDpi;// 屏幕密度，单位为dpi
    public static float scale;// 缩放系数，值为 densityDpi/160
    public static float fontScale;// 文字缩放系数，同scale
    public static int screenOrientation = SCREEN_ORIENTATION_VERTICAL;// 当前屏幕状态，默认为竖屏

    private static UtilPhoneParam instance;

    private UtilPhoneParam() {

    }
    
    private static synchronized void initSync() {
    	if (instance == null) {
    		instance = new UtilPhoneParam();
		}
    }

    /**
     * 获取实例
     * 
     * @return	UtilPhoneParam
     */
    public static UtilPhoneParam getInstance() {
        if (instance == null) {
        	initSync();
        }
        return instance;
    }

    /**
     * 初始化
     * 1、获取手机屏幕参数
     * 
	 * @author zhuofq  
     * @param context 
     */
    public static void init(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        densityDpi = dm.densityDpi;
        scale = dm.density;
        fontScale = dm.scaledDensity;
        screenOrientation = screenHeight > screenWidth ? SCREEN_ORIENTATION_VERTICAL
                : SCREEN_ORIENTATION_HORIZONTAL;       
    }
    
    /**
     * 获取屏幕高度
     * 
	 * @author zhuofq  
     * @param context
     * @return	int
     */
	public static int getScreenHeight(Context context) {
//		WindowManager wm = (WindowManager) context
//				.getSystemService(Context.WINDOW_SERVICE);
//		int height = wm.getDefaultDisplay().getHeight();
//		return height;
		return screenHeight;
	}

	/**
	 * 屏幕是否亮起
	 * 
	 * @author zhuofq  
	 * @param context
	 * @return	boolean
	 */
	public static boolean isScreenOn(Context context) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	/**
	 * sim卡是否准备好
	 * 
	 * @author zhuofq  
	 * @param context
	 * @return	boolean
	 */
	public static boolean isSimReady(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telMgr.getSimState() == TelephonyManager.SIM_STATE_READY)
			return true;
		else
			return false;
	}

	/**
	 * 获取手机IMSI,获取不了则以0替代
	 * 
	 * @author zhuofq  
	 * @param context
	 * @return	String
	 */
	public static String getIMSI(Context context) {
		try {
			TelephonyManager telMgr;
			telMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String imsi = telMgr.getSubscriberId();
			if (imsi == null) {
				imsi = "0";
			}
			return imsi;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	
	/**
	 * 获取手机MEID
	 * 
	 * @author zhuofq  
	 * @param context
	 * @return	String
	 */
	public static String getMEID(Context context) {
		try {
			TelephonyManager telMgr;
			telMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String meid = telMgr.getDeviceId();
			if (meid == null) {
				meid = "";
			}
			return meid;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取SIM所属运营商
	 * 
	 * @author zhuofq  
	 * @param context
	 * @return 0 异常 1 移动 2 联通 3 电信
	 */
	public static int getTelCnpny(Context context) {
		try {
			int telCnpny = 0;
			TelephonyManager telMgr;
			telMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String imsi = telMgr.getSubscriberId();
			if (imsi != null) {
				if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
					telCnpny = 1;
				} else if (imsi.startsWith("46001")) {
					telCnpny = 2;
				} else if (imsi.startsWith("46003") || imsi.startsWith("46011")) {
					telCnpny = 3;
				}
			}
			return telCnpny;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 是否为电信用户
	 * 
	 * @author zhuofq  
	 * @param context
	 * @return	boolean
	 */
	public static boolean isCtImsi(Context context) {
		return (getTelCnpny(context) == 3);
	}

//	public static final String DEFAULT_SID = "13824";// 默认返回北京的SID

	/**
	 * 获取手机SID
	 * 
	 * @author zhuofq  
	 * @param context
	 * @return
	 */
	public static String getSID(Context context) {
		try {
			TelephonyManager telMgr;
			telMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			CdmaCellLocation celllocation = (CdmaCellLocation) telMgr
					.getCellLocation();
			int sid = celllocation.getSystemId();
			if (sid == -1)
				return null;

			return "" + sid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 是否为 CDMA 手机
	 * 
	 * @author zhuofq  
	 * @param context
	 * @return boolean
	 */
	public static boolean isCTUser(Context context) {
		boolean ret = false;
		try {
			String sid = getSID(context);
			if (sid == null)
				return false;

			int intSid = Integer.valueOf(sid);
			if (intSid > 0)
				ret = true;
		} catch (Exception e) {
		}
		return ret;
	}
	
	/**
	 * 是否为 CDMA 手机
	 * 
	 * @author zhuofq  
	 * @param context
	 * @return	boolean
	 */
	public static boolean isCDMAPhone(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null && tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取内置存储剩余总大小
	 * 
	 * @author zhuofq  
	 * @return long
	 */
	public static long getInternalStorgeLeft() {
		String internalPath = Environment.getDataDirectory().getPath();
		StatFs statInternal = new StatFs(internalPath);
		long blockSize = statInternal.getBlockSize();
		long totalBlocks = statInternal.getBlockCount();
		long availableBlocks = statInternal.getAvailableBlocks();

		long leftSize = availableBlocks * blockSize;// 内置存储总大小剩余 单位B
		return leftSize;
	}

	/**
	 * 获取android 手机版本
	 * 
	 * The user-visible SDK version of the framework in its raw String
	 * representation; use SDK_INT instead.
	 * 
	 * @author zhuofq  
	 * @return String
	 */
	public static String getPhoneSDKVersion() {
		return android.os.Build.VERSION.SDK;
	}

	/**
	 * The user-visible version string. E.g., "1.0" or "3.4b5". 
	 * 
	 * @author zhuofq  
	 * @return	String
	 */
	public static String getPhoneSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	
	/**
	 * The end-user-visible name for the end product. 
	 * 
	 * @author zhuofq  
	 * @return	String
	 */
	public static String getPhoneModel() {
		String phonemodel = android.os.Build.MODEL;
		phonemodel = phonemodel.replace("&", "_");// 兼容<"&" 字符在xml中是特殊字符,必须转化>
		return phonemodel;
	}

	/**
	 * The manufacturer of the product/hardware
	 * 
	 * @author zhuofq  
	 * @return boolean
	 */
	public static String getPhoneModelLong() {
		String phonemodel = android.os.Build.MANUFACTURER + " "
				+ android.os.Build.MODEL;
		phonemodel = phonemodel.replace("&", "_");// 兼容<"&" 字符在xml中是特殊字符,必须转化>
		return phonemodel;
	}

	/**
	 * 判断手机是否有插入SD卡
	 * 
	 * @author zhuofq  
	 * @return boolean
	 */
	public static boolean isSDCardMounted() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
     * 获取手机内存信息
     * @return
     */
    public static String getMemoryInfo(){
    	return "总大小:"+formatSize(getTotalInternalMemorySize());
    }
    

    /** 
     * 获取手机内存的总空间大小
     * */
    private static long getTotalInternalMemorySize()
    {
    	String memInfoPath = "/proc/meminfo";
		String readTemp = "";
		String memTotal = "";
		long memory = 0;
		try {
			FileReader fr = new FileReader(memInfoPath);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((readTemp = localBufferedReader.readLine()) != null) {
				if (readTemp.contains("MemTotal")) {
					String[] total = readTemp.split(":");
					memTotal = total[1].trim();
				}
			}
			localBufferedReader.close();
			String[] memKb = memTotal.split(" ");
			memTotal = memKb[0].trim();
			memory = Long.parseLong(memTotal);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return memory;
    }
    
    private static String formatSize(long size) // 文件大小单位转换
	{
		long mb = 1024;
		long gb = (mb * 1024);
		if (size < mb) {
			return String.format("%.2f KB", (int)size); // 保留两位小数
		} else if (size < gb) {
			return String.format("%.2f MB", (float)size / mb);
		} else {
			return String.format("%.2f GB", (float)size / gb);
		}
	}

}
