package com.light.friendscommunity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class ImageUtils {
	
	/**
	 * 
	* @Description TODO(Get bitmap through image path  ) 
	* @param @param imgPath
	* @param @return   
	* @return Bitmap  
	 */
	public Bitmap getBitmap(String imgPath) {  
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	        newOpts.inJustDecodeBounds = false;  
	        newOpts.inPurgeable = true;  
	        newOpts.inInputShareable = true;  
	        newOpts.inSampleSize = 1;  
	        newOpts.inPreferredConfig = Config.RGB_565;  
	        return BitmapFactory.decodeFile(imgPath, newOpts);  
	}  
	
	
	/**
	 * 
	* @Description TODO(Compress image by pixel ) 
	* @param @param imgPath
	* @param @param pixelW
	* @param @param pixelH
	* @param @return   
	* @return Bitmap  
	 */
	public Bitmap compressPixel(String imgPath, float pixelW, float pixelH) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        float hh = pixelH;
	    float ww = pixelW;
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0) be = 1;  
        newOpts.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        return bitmap;
	}
	


}



