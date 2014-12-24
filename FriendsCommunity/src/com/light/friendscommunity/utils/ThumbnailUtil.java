package com.light.friendscommunity.utils;

import java.util.HashMap;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore.Images.Thumbnails;


public class ThumbnailUtil {
	
	private ContentResolver cr; 
	
	/** 保存缩略图的path，key为imageId */
	private static HashMap<Integer,String> hash = new HashMap<Integer, String>();
	
	/**
	 * 
	* @Description TODO(获取缩略图的路径，并保存) 
	* @param    
	* @return void  
	 */
	private void getThumbnailPath(){
		
		String[] thumbnailsInfo = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
		Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, thumbnailsInfo, null, null, null);

		if (cur!=null&&cur.moveToFirst()) {
			int imageId;
			String imagePath;
			int imageIdColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
			do {
				imageId = cur.getInt(imageIdColumn);
				imagePath = cur.getString(dataColumn);
				hash.put(imageId, "file://"+imagePath);
			} while (cur.moveToNext());
		}
	}

}
