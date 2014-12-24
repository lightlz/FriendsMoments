package com.light.friendscommunity.bean;

import java.util.List;

/**
 * 
* @ClassName AlbumsBean 
* @Description TODO(相簿信息) 
* @date 2014年12月24日
*
 */
public class AlbumsBean {
	
	/**id*/
	private int ImageId;
	
	/**绝对路径*/
	private String absolutePath;
	
	/**第一张显示的路径*/
	private String displayPath;
	
	/**album名称*/
	private String albumName;
	
	/**保存同个albumName下的Image*/
	private List<ThumbnailImageBean> list;
	
	public int getImageId() {
		return ImageId;
	}
	public void setImageId(int imageId) {
		ImageId = imageId;
	}
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	public String getDisplayPath() {
		return displayPath;
	}
	public void setDisplayPath(String displayPath) {
		this.displayPath = displayPath;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public List<ThumbnailImageBean> getList() {
		return list;
	}
	public void setList(List<ThumbnailImageBean> list) {
		this.list = list;
	}
	
	
	
}
