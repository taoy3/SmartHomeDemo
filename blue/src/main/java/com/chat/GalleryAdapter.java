package com.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {
	private int[] mfacesId;
	private Context mContext;

	public GalleryAdapter(Context context,int[] facesid) {
		mContext=context;
		mfacesId=facesid;
	}
	@Override
	public int getCount() {
		return mfacesId.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView img = new ImageView(mContext);
		img.setImageResource(mfacesId[position]);
		img.setScaleType(ImageView.ScaleType.FIT_XY);
		img.setLayoutParams(new GridView.LayoutParams(50, 50));
		img.setScaleType(ImageView.ScaleType.FIT_CENTER);

		return img;
	}

}
