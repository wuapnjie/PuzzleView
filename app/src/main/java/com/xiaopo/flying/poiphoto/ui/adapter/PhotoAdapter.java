package com.xiaopo.flying.poiphoto.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xiaopo.flying.anotherlayout.R;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.datatype.Photo;
import com.xiaopo.flying.poiphoto.ui.custom.SquareImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * adapter for Photo Adapter
 * @author wupanjie
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
  private final String TAG = PhotoAdapter.class.getSimpleName();

  private List<Photo> mData;
  private ArrayList<Photo> mSelectedPhotos;
  private Set<Integer> mSelectedPhotoPositions;

  private OnPhotoSelectedListener mOnPhotoSelectedListener;
  private OnPhotoUnSelectedListener mOnPhotoUnSelectedListener;
  private OnSelectedMaxListener mOnSelectedMaxListener;

  private int shadowResId = -1;

  private int mMaxCount = Define.DEFAULT_MAX_COUNT;

  public PhotoAdapter() {
    mSelectedPhotos = new ArrayList<>();
    mSelectedPhotoPositions = new TreeSet<>();
  }

  public int getMaxCount() {
    return mMaxCount;
  }

  public void setMaxCount(int maxCount) {
    mMaxCount = maxCount;
  }

  public void setOnSelectedMaxListener(OnSelectedMaxListener onSelectedMaxListener) {
    mOnSelectedMaxListener = onSelectedMaxListener;
  }

  public void setOnPhotoSelectedListener(OnPhotoSelectedListener onPhotoSelectedListener) {
    mOnPhotoSelectedListener = onPhotoSelectedListener;
  }

  public void setOnPhotoUnSelectedListener(OnPhotoUnSelectedListener onPhotoUnSelectedListener) {
    mOnPhotoUnSelectedListener = onPhotoUnSelectedListener;
  }

  public ArrayList<Photo> getSelectedPhotos() {
    return mSelectedPhotos;
  }

  public ArrayList<String> getSelectedPhotoPaths() {
    ArrayList<String> paths = new ArrayList<>();
    for (Photo photo : mSelectedPhotos) {
      paths.add(photo.getPath());
    }

    return paths;
  }

  public void refreshData(List<Photo> dataNew) {
    mData = dataNew;
    mSelectedPhotos.clear();
    notifyDataSetChanged();
  }

  @Override
  public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.poiphoto_item_photo, parent, false);
    return new PhotoViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
    //解决View复用时的问题
    if (!mSelectedPhotoPositions.contains(position) && holder.mShadow.getVisibility() == View.VISIBLE) {
      holder.mShadow.setVisibility(View.GONE);
    } else if (mSelectedPhotoPositions.contains(position) && holder.mShadow.getVisibility() != View.VISIBLE) {
      holder.mShadow.setVisibility(View.VISIBLE);
    }

    if (shadowResId > 0) {
      holder.mShadow.setBackgroundResource(shadowResId);
    }

    final String path = mData.get(position).getPath();

    Picasso.with(holder.itemView.getContext())
        .load("file:///" + path)
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

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int pos = holder.getAdapterPosition();
        if (mSelectedPhotoPositions.contains(pos)) {

          mSelectedPhotoPositions.remove(pos);
          mSelectedPhotos.remove(mData.get(pos));
          if (mOnPhotoUnSelectedListener != null) {
            mOnPhotoUnSelectedListener.onPhotoUnSelected(mData.get(pos), pos);
          }

          holder.mShadow.setVisibility(View.GONE);
        } else {
          if (mSelectedPhotoPositions.size() >= mMaxCount) {
            if (mOnSelectedMaxListener != null) mOnSelectedMaxListener.onSelectedMax();
          } else {
            mSelectedPhotoPositions.add(pos);
            mSelectedPhotos.add(mData.get(pos));
            if (mOnPhotoSelectedListener != null) {
              mOnPhotoSelectedListener.onPhotoSelected(mData.get(pos), pos);
            }
            holder.mShadow.setVisibility(View.VISIBLE);
          }
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return mData == null ? 0 : mData.size();
  }

  public void setSelectedResId(int shadowResId) {
    this.shadowResId = shadowResId;
  }

  public void reset() {
    mSelectedPhotos.clear();
    mSelectedPhotoPositions.clear();
    notifyDataSetChanged();
  }

  public static class PhotoViewHolder extends RecyclerView.ViewHolder {
    SquareImageView mIvPhoto;
    View mShadow;

    public PhotoViewHolder(View itemView) {
      super(itemView);

      mIvPhoto = (SquareImageView) itemView.findViewById(R.id.iv_photo);
      mShadow = itemView.findViewById(R.id.shadow);
    }
  }

  public interface OnPhotoSelectedListener {
    void onPhotoSelected(Photo photo, int position);
  }

  public interface OnPhotoUnSelectedListener {
    void onPhotoUnSelected(Photo photo, int position);
  }

  public interface OnSelectedMaxListener {
    void onSelectedMax();
  }
}
