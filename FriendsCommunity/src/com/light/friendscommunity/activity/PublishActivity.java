package com.light.friendscommunity.activity;

import java.io.File;

import com.light.friendscommunity.R;
import com.light.friendscommunity.utils.BitmapConstant;
import com.light.friendscommunity.utils.Constant;
import com.light.friendscommunity.utils.ImageUtils;
import com.light.friendscommunity.widget.MyGridView;
import com.light.friendscommunity.widget.ViewHolder;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PublishActivity extends Activity{

	private MyGridView bitmapGv;
	
	private GridViewBitmapAdapter bitmapAdapter;
	
	private ActionBar actionBar;
	
	private static final String TAG = "PublishActivity : ";
	
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		initView();
	}
	
	
	private void initView(){
		
		actionBar = getActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled(true); 
		actionBar.setTitle("Moments");
		actionBar.setDisplayShowHomeEnabled(false);
		
		bitmapGv = (MyGridView)findViewById(R.id.gv_community);
		bitmapAdapter = new GridViewBitmapAdapter();
		bitmapGv.setAdapter(bitmapAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_friends_community, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
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
				convertView = LayoutInflater.from(PublishActivity.this).
						inflate(R.layout.item_gv_community, null);
			}
			
			ImageView img = ViewHolder.get(convertView, R.id.item_gv_img_bitmap);
			
			if(position == BitmapConstant.bmpList.size()){
				img.setImageDrawable(PublishActivity.this.getResources()
						.getDrawable(R.drawable.ic_diary_publish_add));
			}else{
				img.setImageBitmap(BitmapConstant.bmpList.get(position));
			}
			
			img.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					if(BitmapConstant.bmpList.size()<BitmapConstant.PICTURE_MAX){
						openCamera();
					}
					return true;
				}
			});
			img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(PublishActivity.this,PhotoSelectedActivity.class));
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
					&& resultCode == this.RESULT_OK){
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
