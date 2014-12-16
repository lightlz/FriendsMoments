package com.light.friendscommunity.fragment;


import java.io.File;

import com.light.friendscommunity.BitmapConstant;
import com.light.friendscommunity.Constant;
import com.light.friendscommunity.ImageUtils;
import com.light.friendscommunity.R;
import com.light.friendscommunity.widget.MyGridView;
import com.light.friendscommunity.widget.ViewHolder;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Contactables;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class FriendsCommunity extends Fragment{

	private View rootView;
	
	private MyGridView bitmapGv;
	
	private GridViewBitmapAdapter bitmapAdapter;
	
	private static final String TAG = "FriendsCommunity : ";
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		super.onActivityCreated(savedInstanceState);
	}
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case Constant.COMPRESS_IMAGE_FINISH:
				Bundle bd = new Bundle();
				bd = msg.getData();
				Bitmap bmp = (Bitmap) bd.get("bmp");
				
				if(bmp != null){
					BitmapConstant.bmpList.add(bmp);
					Log.v(TAG, BitmapConstant.bmpList.size() + "");
					bitmapAdapter.notifyDataSetChanged();
				}
				
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_friendscommunity, null);
	    }
	    ViewGroup parent = (ViewGroup) rootView.getParent();
	    if (parent != null) {
	        parent.removeView(rootView);
	    }
		return rootView;
	}

	private void initView(){
		bitmapGv = (MyGridView)rootView.findViewById(R.id.gv_community);
		bitmapAdapter = new GridViewBitmapAdapter();
		bitmapGv.setAdapter(bitmapAdapter);
		
		
	}
	
	class GridViewBitmapAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return (BitmapConstant.bmpList.size() + 1);
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null){
				convertView = LayoutInflater.from(getActivity()).
						inflate(R.layout.item_gv_community, null);
			}
			
			ImageView img = ViewHolder.get(convertView, R.id.item_gv_img_bitmap);
			
			if(position == BitmapConstant.bmpList.size()){
				img.setImageDrawable(getActivity().getResources()
						.getDrawable(R.drawable.ic_diary_publish_add));
			}else{
				img.setImageBitmap(BitmapConstant.bmpList.get(position));
			}
			
			img.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					openCamera();
					return true;
				}
			});
			
			return convertView;
		}
		
	}
	
	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		BitmapConstant.pathList.clear();
		BitmapConstant.bmpList.clear();
		
		super.onDestroy();
	}
	
	
	//临时保存图片的dir
	private File getTempBmpDir(){
		File dir = new File(Constant.TempBitmapDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	/**
	 * 
	* @Description TODO(打开照相机) 
	* @param    
	* @return void  
	* @author lailiangzhong@long.tv
	 */
	public void openCamera(){
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(getTempBmpDir()+
				String.valueOf(System.currentTimeMillis()+ ".jpg"));
		Uri uri = Uri.fromFile(file);
		BitmapConstant.BITMAP_PATH = file.getPath();
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		this.startActivityForResult(openCameraIntent, BitmapConstant.TAKE_PICTURE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode){
		case BitmapConstant.TAKE_PICTURE:
			 
			if(BitmapConstant.pathList.size()<BitmapConstant.PICTURE_MAX
					&& resultCode == getActivity().RESULT_OK){
				BitmapConstant.pathList.add(BitmapConstant.BITMAP_PATH);
				Log.v(TAG+" bitmap", BitmapConstant.BITMAP_PATH);
				//进行压缩，然后显示
				new Thread(new CompressImageThread(BitmapConstant.BITMAP_PATH)).start();
			}
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	/**
	 * 
	* @ClassName CompressImageThread 
	* @Description TODO(compress bitmaps from camera) 
	* @author lailiangzhong@long.tv
	* @date 2014年12月16日
	*
	 */
	class CompressImageThread extends Thread{

		private String ImagePath;
		
		public CompressImageThread(String imagePath) {
			super();
			ImagePath = imagePath;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Bitmap bmp = null ;
			//压缩图片
			if(!ImagePath.equals("")){
				bmp = ImageUtils.compressPixel(ImagePath, 240f, 480f);
			}
			
			Message msg = new Message();
			Bundle bd = new Bundle();
			bd.putParcelable("bmp", bmp);
			msg.what = Constant.COMPRESS_IMAGE_FINISH;
			msg.setData(bd);
			handler.sendMessage(msg);
			
			super.run();
		}
		
	}

}
