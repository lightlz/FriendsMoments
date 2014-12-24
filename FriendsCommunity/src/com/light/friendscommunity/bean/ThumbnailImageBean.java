package com.light.friendscommunity.bean;

/**
 * 
* @ClassName ThumbnailImageBean 
* @Description TODO(缩略图的信息) 
* @date 2014年12月24日
*
 */
public class ThumbnailImageBean {

	/**id*/
	private int ImageId;
	
	/**绝对路径*/
	private String absolutePath;
	
	/**图片显示的路径*/
	private String displayPath;
	
	/**图片是否被选中*/
	private boolean isChose;

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

	public boolean isChose() {
		return isChose;
	}

	public void setChose(boolean isChose) {
		this.isChose = isChose;
	}
	
	
}
