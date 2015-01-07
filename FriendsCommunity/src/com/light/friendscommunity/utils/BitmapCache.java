package com.light.friendscommunity.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

public class BitmapCache {

	private HashMap<String, SoftReference<Bitmap>> hash = new HashMap
			<String, SoftReference<Bitmap>>();
	
	private Handler handler = new Handler();
	
	public interface ImageCallback {
		public void imageLoad(ImageView img, Bitmap bitmap,
				Object... params);
	}
	
	
	/**
	 * @descr put bitmap into map
	 * @param path
	 * @param bmp
	 */
	public void put(String path,Bitmap bmp){
		if(!StringUtil.isEmpty(path) && bmp != null){
			hash.put(path, new SoftReference<Bitmap>(bmp));
		}
	}
	
	public void display(ImageView img ,String dispPath,String absolutePath,ImageCallback callback){
		
		String path ;
		boolean hasThumPath;
		if(!StringUtil.isEmpty(dispPath)){
			path = dispPath;
			hasThumPath = true;
		}else if(!StringUtil.isEmpty(absolutePath)){
			path = absolutePath;
			hasThumPath = false;
		}else{
			return;
		}
		
		if(hash.containsKey(path)){
			SoftReference<Bitmap> sr = hash.get(path);
			Bitmap bmp = sr.get();
			if(bmp != null&&callback != null){
				callback.imageLoad(img,bmp, path);
			}
		}else{
			new Thread(new DealImageThread(path, hasThumPath, callback,img)).start();
		}
	}
	
	class DealImageThread extends Thread{

		private String path;
		
		private boolean isThumbnail;
		
		private ImageCallback callback;
		
		private ImageView img;
		
		Bitmap bmp;
		
		public DealImageThread(String path, boolean isThumbnail,
				ImageCallback callback,ImageView img) {
			super();
			this.path = path;
			this.isThumbnail = isThumbnail;
			this.callback = callback;
			this.img = img;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			if(isThumbnail){
				bmp = BitmapFactory.decodeFile(path);
				if(bmp == null){
					//bmp = ImageUtils.compressPixel(path, 256f, 256f);
					try {
						bmp = revitionImageSize(path);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				//bmp = ImageUtils.compressPixel(path, 256f, 256f);
				try {
					bmp = revitionImageSize(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(bmp != null){
				put(path,bmp);
				if(callback != null){
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							callback.imageLoad(img,bmp, path);
						}
					});
				}
			}
			super.run();
		}
		
	}
	
	public Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 256)
					&& (options.outHeight >> i <= 256)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
}
