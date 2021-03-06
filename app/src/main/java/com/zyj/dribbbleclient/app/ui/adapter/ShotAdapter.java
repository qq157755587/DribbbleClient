package com.zyj.dribbbleclient.app.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zyj.dribbbleclient.app.R;
import com.zyj.dribbbleclient.app.model.Shot;
import com.zyj.dribbbleclient.app.util.Device;

public class ShotAdapter extends BaseAdapter {
    private Context mContext;
    private Shot[] mShots = new Shot[0];

    public ShotAdapter(Context context) {
        mContext = context;
    }

    public void setShots(Shot[] shots) {
        mShots = shots;
    }

    @Override
    public int getCount() {
        return mShots.length;
    }

    @Override
    public Object getItem(int position) {
        return mShots[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.shot_item, null);
            holder = new ViewHolder();
            holder.shot = (ImageView) convertView.findViewById(R.id.shot);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.viewsCount = (TextView) convertView.findViewById(R.id.views_count);
            holder.likesCount = (TextView) convertView.findViewById(R.id.likes_count);
            holder.commentsCount = (TextView) convertView.findViewById(R.id.comments_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Shot shot = mShots[position];

        float scale = (float) shot.getHeight() / shot.getWidth();
        ViewGroup.LayoutParams params = holder.shot.getLayoutParams();
        params.width = Device.width;
        params.height = (int) (params.width * scale);
        holder.shot.setLayoutParams(params);

        ImageLoader.getInstance().displayImage(shot.getImages().getNormal(), holder.shot);
        holder.title.setText(shot.getTitle());
        holder.viewsCount.setText(String.valueOf(shot.getViews_count()));
        holder.likesCount.setText(String.valueOf(shot.getLikes_count()));
        holder.commentsCount.setText(String.valueOf(shot.getComments_count()));
        return convertView;
    }

    static class ViewHolder {
        ImageView shot;
        TextView title;
        TextView viewsCount;
        TextView likesCount;
        TextView commentsCount;
    }
}
