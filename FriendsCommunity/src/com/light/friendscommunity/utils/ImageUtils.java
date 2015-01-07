package com.light.friendscommunity.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageUtils {
	
	/**
	 * 
	* @Description TODO(Get bitmap through image path) 
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
	public static Bitmap compressPixel(String imgPath, float pixelW, float pixelH) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        float hh = pixelH;
	    float ww = pixelW;
        int inSampleSize = 1;
        if (h > hh || w > ww) {
            final int halfHeight = h / 2;
            final int halfWidth = w / 2;
            while ((halfHeight / inSampleSize) > hh
              && (halfWidth / inSampleSize) > ww) {
            	inSampleSize *= 2;
            }
        }
        newOpts.inSampleSize = inSampleSize;
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        
        Bitmap dst = Bitmap.createScaledBitmap(bitmap, (int)ww, (int)hh, false);
        if (bitmap != dst) { 
        	bitmap.recycle(); 
        }

        return dst;
	}
	
	public static Bitmap compressPixel1(String imgPath, float pixelW, float pixelH) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        float hh = pixelH;
	    float ww = pixelW;
	    int height = h;
	    int width = w;
        int inSampleSize = 1;
        if (h > hh || w > ww) {
            final int halfHeight = h / 2;
            final int halfWidth = w / 2;
            while ((halfHeight / inSampleSize) > hh
              && (halfWidth / inSampleSize) > ww) {
            	inSampleSize *= 2;
            }
            
            if(h>w){
            	height = 1280;
            	width = (1280*w)/h;
            }else{
            	width = 1280;
            	height = (1280*h)/w;
            }
        }
        newOpts.inSampleSize = inSampleSize;
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        	
        Bitmap dst = Bitmap.createScaledBitmap(bitmap, width, height, false);
        if (bitmap != dst) { 
        	bitmap.recycle(); 
        }

        return dst;
	}
	

}



