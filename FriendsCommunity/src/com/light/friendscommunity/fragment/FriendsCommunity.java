package com.light.friendscommunity.fragment;


import java.io.File;

import com.light.friendscommunity.BitmapConstant;
import com.light.friendscommunity.Constant;
import com.light.friendscommunity.R;
import com.light.friendscommunity.widget.ViewHolder;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
	
	private GridView bitmapGv;
	
	private GridViewBitmapAdapter bitmapAdapter;
	
	private static final String TAG = "FriendsCommunity : ";
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		super.onActivityCreated(savedInstanceState);
	}

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
		bitmapGv = (GridView)rootView.findViewById(R.id.gv_community);
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
			}
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

}
