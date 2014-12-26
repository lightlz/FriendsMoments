package com.light.friendscommunity.activity;

import java.util.ArrayList;
import java.util.List;

import com.light.friendscommunity.R;
import com.light.friendscommunity.bean.ThumbnailImageBean;
import com.light.friendscommunity.utils.ThumbnailUtil;
import com.light.friendscommunity.widget.ViewHolder;

import android.R.color;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 
* @ClassName PhotoSelectedActivity 
* @Description TODO(照片选择) 
* @date 2014年12月26日
*
 */
public class PhotoSelectedActivity extends Activity implements OnClickListener{
	
	private ActionBar actionBar;
	
	private static final String TAG = "PublishActivity : ";
	
	private static final int DATATHREAD_FINISH = 0;

	private LinearLayout ablumSelectLayout;
	
	private RelativeLayout layout;
	
	private ThumbnailUtil thumbUtil;
	
	private TextView ablumNameTv;
	
	private GridView imageGv;
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch(msg.what){
			case DATATHREAD_FINISH:
				ablumNameTv.setText(thumbUtil.list.get(0).getAlbumName()+"");
				break;
			}
		}
		
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photoselector);
		initView();
	}
	
	private void initView(){
		actionBar = getActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled(true); 
		actionBar.setTitle("Gallery");
		actionBar.setDisplayShowHomeEnabled(false);
		
		layout = (RelativeLayout)findViewById(R.id.relativeLayout);
		
		ablumSelectLayout = (LinearLayout)findViewById(R.id.layout_photosele_album);
		ablumSelectLayout.setOnClickListener(this);
		
		ablumNameTv = (TextView)findViewById(R.id.tv_photosele_album);
		
		imageGv = (GridView)findViewById(R.id.gv_act_photoselect);
		
		
		new Thread(new GetDataThread()).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_photo_selected, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
		  case android.R.id.home:
			    finish();
	            return true;
	        default:
	        	return super.onOptionsItemSelected(item);
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.layout_photosele_album:
			showAlbumPopu();
			break;
		}
	}

	/**
	 * 
	* @Description TODO(显示相册选择的popup) 
	* @param    
	* @return void  
	 */
	private void showAlbumPopu(){

	     View contentView = getLayoutInflater().inflate(R.layout.item_popu_album_seleted, null); 
	        
		 int h = getWindowManager().getDefaultDisplay().getHeight();  
	     int w = getWindowManager().getDefaultDisplay().getWidth(); 
	     
		 PopupWindow pw = new PopupWindow(contentView);
		 pw.setHeight(h/3);
		 pw.setWidth(w);
		 pw.setOutsideTouchable(false);
		 pw.setFocusable(true);
		 pw.setBackgroundDrawable(new BitmapDrawable()); 
		 
		 ListView lv = (ListView)contentView.findViewById(R.id.lv_ablum_seleted);
		 ListViewAdapter adapter = new ListViewAdapter();
		 lv.setAdapter(adapter);
		 
		 pw.showAtLocation(layout, Gravity.BOTTOM, 0,ablumSelectLayout.getHeight());
	}
	
	
	/**图片数据加载*/
	class GetDataThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			thumbUtil = new ThumbnailUtil(PhotoSelectedActivity.this);
			if(!(thumbUtil.hash.size()>0 && thumbUtil.list.size()>0)){
				thumbUtil.getThumbnailPath();
				thumbUtil.getOriginImagePath();
			}
			handler.sendEmptyMessage(DATATHREAD_FINISH);
		}
		
	}
	
	/**
	 * 
	* @ClassName ListViewAdapter 
	* @Description TODO(相簿名称选择的listview) 
	* @date 2014年12月26日
	*
	 */
	class ListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return thumbUtil.list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return thumbUtil.list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView = LayoutInflater.from(PhotoSelectedActivity.this)
						.inflate(R.layout.item_lv_popu_ablum, null);
			}
			String ablumsName = thumbUtil.list.get(position).getAlbumName();
			int imageSum = thumbUtil.list.get(position).getList().size();
			
			TextView nameTv = ViewHolder.get(convertView,R.id.tv_lv_popu_sele);
			nameTv.setText(ablumsName);
			
			TextView sumTv = ViewHolder.get(convertView, R.id.tv_lv_popu_sum);
			sumTv.setText(String.valueOf(imageSum));
			
			return convertView;
		}
	}
	
	
	class ImageGridViewAdapter extends BaseAdapter{

		private int albumIndex = 0;
		
		private List<ThumbnailImageBean> imageList;
		
		public ImageGridViewAdapter(int albumIndex) {
			super();
			this.albumIndex = albumIndex;
			//获取到同个相册下图片list
			imageList = thumbUtil.list.get(albumIndex).getList();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return imageList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView = LayoutInflater.from(PhotoSelectedActivity.this)
						.inflate(R.layout.item_gv_photo_selected, null);
			}
			
			ImageView img = ViewHolder.get(convertView, R.id.item_gv_img_selected);
			
			//用原图图片的id找到对应的缩略图path
			String dispPath = thumbUtil.getDisplayPath(
					imageList.get(position).getImageId(),imageList.get(position).getDisplayPath());
			
			
			
			return null;
		}
		
	}
	
}
