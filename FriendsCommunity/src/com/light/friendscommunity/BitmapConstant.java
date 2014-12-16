package com.light.friendscommunity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class BitmapConstant {
	
	/**表示拍照*/
	public static final int TAKE_PICTURE = 0;
	/**最大的bitmap数量*/
	public static final int PICTURE_MAX = 9;
	/**单张bitmap的路径*/
	public static String BITMAP_PATH = "";
	/**保存显示的bitmap*/
	public static List<Bitmap> bmpList = new ArrayList<Bitmap>();
	/**保存显示bitmap的路径*/
	public static List<String> pathList = new ArrayList<String>();
}
