package com.xiaopo.flying.poiphoto.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xiaopo.flying.anotherlayout.R;
import com.xiaopo.flying.poiphoto.datatype.Album;
import com.xiaopo.flying.poiphoto.ui.custom.SquareImageView;
import java.io.File;
import java.util.List;

/**
 * adapter for AlbumList
 * @author wupanjie
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
  private final String TAG = AlbumAdapter.class.getSimpleName();

  private List<Album> mData;
  private OnItemClickListener mOnItemClickListener;

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  public List<Album> getData() {
    return mData;
  }

  public void refreshData(List<Album> data) {
    mData = data;
    notifyDataSetChanged();
  }

  @Override
  public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.poiphoto_item_album, parent, false);
    return new AlbumViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final AlbumViewHolder holder, final int position) {
    holder.mTvTitle.setText(mData.get(position).getName());
    final String path = mData.get(position).getCoverPath();

    //        System.out.println(path);

    Picasso.with(holder.itemView.getContext())
        .load(new File(path))
        .fit()
        .centerInside()
        .into(holder.mIvPhoto, new Callback() {
          @Override
          public void onSuccess() {

          }

          @Override
          public void onError() {
            Log.e(TAG, "Picasso failed load photo -> " + path);
          }
        });

    holder.mAlbumContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
      }
    });
  }

  public String getBuckedId(int position) {
    if (mData != null && mData.size() >= position) {
      return mData.get(position).getId();
    } else {
      return "null";
    }
  }

  @Override
  public int getItemCount() {
    return mData == null ? 0 : mData.size();
  }

  public static class AlbumViewHolder extends RecyclerView.ViewHolder {
    SquareImageView mIvPhoto;
    TextView mTvTitle;
    LinearLayout mAlbumContainer;

    public AlbumViewHolder(View itemView) {
      super(itemView);

      mIvPhoto = (SquareImageView) itemView.findViewById(R.id.iv_photo);
      mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
      mAlbumContainer = (LinearLayout) itemView.findViewById(R.id.album_container);
    }
  }

  public interface OnItemClickListener {
    void onItemClick(View v, int position);
  }
}
