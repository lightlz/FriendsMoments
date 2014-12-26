package com.light.friendscommunity.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.light.friendscommunity.bean.AlbumsBean;
import com.light.friendscommunity.bean.ThumbnailImageBean;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;


public class ThumbnailUtil {
	
	private ContentResolver cr; 
	
	/** 保存缩略图的path，key为imageId */
	public  static HashMap<Integer,String> hash = new HashMap<Integer, String>();
	
	private Context context;
	
	/**保存相簿列表*/
	public static List<AlbumsBean> list = new ArrayList<AlbumsBean>();
	

	public ThumbnailUtil(Context context) {
		super();
		this.context = context;
		
		cr = context.getContentResolver();
		
	}


	/**
	 * 
	* @Description TODO(获取缩略图的路径，并保存) 
	* @param    
	* @return void  
	 */
	public void getThumbnailPath(){
		
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
	
	
	/**
	 * 
	* @Description TODO(获取原图，并保存) 
	* @param    
	* @return void  
	 */
	public List<AlbumsBean> getOriginImagePath(){
		
		
		final String id = "_id";
		final String dataPath = "_data";
		final String albumName = "bucket_display_name";
		final String limit = "date_modified DESC";
		
		
		Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, limit);  
		/**保存相簿 key为相簿名*/
		Map<String,AlbumsBean> albumsMap = new HashMap<String, AlbumsBean>();
		
		AlbumsBean albusBean = null;
		ThumbnailImageBean thumbnailBean = null;
		
		
		if (cursor!=null&&cursor.moveToFirst())
		{
			do{  
				int index = 0;
				//获取到图片信息
				int imgId = cursor.getInt(cursor.getColumnIndex(id)); 
				String imgPath = cursor.getString(cursor.getColumnIndex(dataPath));
				String imgAlbumName = cursor.getString(cursor.getColumnIndex(albumName));
				
				//存放各个相簿里面的图
				List<ThumbnailImageBean> thumbnailList = new ArrayList<ThumbnailImageBean>();
				thumbnailBean = new ThumbnailImageBean();
				
				thumbnailBean.setImageId(imgId);
				thumbnailBean.setAbsolutePath(imgPath);
				thumbnailBean.setDisplayPath("file://"+imgPath);
				
				//判断当前是否已经保存有这个相册的名称
				if(albumsMap.containsKey(imgAlbumName)){
					albusBean = albumsMap.remove(imgAlbumName);
					if(list.contains(albusBean)){
						index = list.indexOf(albusBean);
					}
					albusBean.getList().add(thumbnailBean);
					albumsMap.put(imgAlbumName, albusBean);
					list.set(index, albusBean);
					
				}else{
					thumbnailList.clear();
					albusBean = new AlbumsBean();
					thumbnailList.add(thumbnailBean);
					
					albusBean.setAbsolutePath(imgPath);
					albusBean.setAlbumName(imgAlbumName);
					albusBean.setImageId(imgId);
					albusBean.setDisplayPath("file://"+imgPath);
					albusBean.setList(thumbnailList);
					albumsMap.put(imgAlbumName, albusBean);
					list.add(albusBean);
				}
				
			}while (cursor.moveToNext());
		}
		return list;
	}

	
}
