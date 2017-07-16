package com.xiaopo.flying.poiphoto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import com.xiaopo.flying.anotherlayout.R;

/**
 * some configuration
 * @author wupanjie
 */
public class Configure implements Parcelable {
  private String mAlbumTitle;
  private String mPhotoTitle;
  private String mMaxNotice;
  @ColorInt private int mToolbarColor;
  @ColorInt private int mToolbarTitleColor;
  @ColorInt private int mStatusBarColor;
  @DrawableRes private int mNavIcon;
  private int mMaxCount;

  public Configure() {
    mNavIcon = R.drawable.ic_arrow_back_white_24dp;
    mAlbumTitle = Define.DEFAULT_ALBUM_TITLE;
    mPhotoTitle = Define.DEFAULT_PHOTO_TITLE;
    mToolbarColor = Define.DEFAULT_TOOLBAR_COLOR;
    mToolbarTitleColor = Define.DEFAULT_TITLE_COLOR;
    mStatusBarColor = Define.DEFAULT_STATUS_BAR_COLOR;
    mMaxCount = Define.DEFAULT_MAX_COUNT;
    mMaxNotice = Define.DEFAULT_MAX_NOTICE;
  }

  protected Configure(Parcel in) {
    mToolbarColor = in.readInt();
    mAlbumTitle = in.readString();
    mPhotoTitle = in.readString();
    mToolbarTitleColor = in.readInt();
    mNavIcon = in.readInt();
    mStatusBarColor = in.readInt();
    mMaxCount = in.readInt();
    mMaxNotice = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mToolbarColor);
    dest.writeString(mAlbumTitle);
    dest.writeString(mPhotoTitle);
    dest.writeInt(mToolbarTitleColor);
    dest.writeInt(mNavIcon);
    dest.writeInt(mStatusBarColor);
    dest.writeInt(mMaxCount);
    dest.writeString(mMaxNotice);
  }

  public static final Creator<Configure> CREATOR = new Creator<Configure>() {
    @Override
    public Configure createFromParcel(Parcel in) {
      return new Configure(in);
    }

    @Override
    public Configure[] newArray(int size) {
      return new Configure[size];
    }
  };

  public String getMaxNotice() {
    return mMaxNotice;
  }

  public void setMaxNotice(String maxNotice) {
    mMaxNotice = maxNotice;
  }

  public int getMaxCount() {
    return mMaxCount;
  }

  public void setMaxCount(int maxCount) {
    mMaxCount = maxCount;
  }

  public int getStatusBarColor() {
    return mStatusBarColor;
  }

  public void setStatusBarColor(int statusBarColor) {
    mStatusBarColor = statusBarColor;
  }

  public int getToolbarColor() {
    return mToolbarColor;
  }

  public void setToolbarColor(int toolbarColor) {
    mToolbarColor = toolbarColor;
  }

  public String getAlbumTitle() {
    return mAlbumTitle;
  }

  public void setAlbumTitle(String albumTitle) {
    mAlbumTitle = albumTitle;
  }

  public String getPhotoTitle() {
    return mPhotoTitle;
  }

  public void setPhotoTitle(String photoTitle) {
    mPhotoTitle = photoTitle;
  }

  public int getToolbarTitleColor() {
    return mToolbarTitleColor;
  }

  public void setToolbarTitleColor(int toolbarTitleColor) {
    mToolbarTitleColor = toolbarTitleColor;
  }

  public int getNavIcon() {
    return mNavIcon;
  }

  public void setNavIcon(int navIcon) {
    mNavIcon = navIcon;
  }
}
