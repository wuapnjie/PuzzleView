package com.xiaopo.flying.poiphoto.datatype;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Photo bean
 * @author wupanjie
 */
public class Photo implements Parcelable {
  private String path;
  private long dataAdded;
  private long dataModified;

  public Photo(String path, long dataAdded, long dataModified) {
    this.path = path;
    this.dataAdded = dataAdded;
    this.dataModified = dataModified;
  }

  protected Photo(Parcel in) {
    path = in.readString();
    dataAdded = in.readLong();
    dataModified = in.readLong();
  }

  public static final Creator<Photo> CREATOR = new Creator<Photo>() {
    @Override
    public Photo createFromParcel(Parcel in) {
      return new Photo(in);
    }

    @Override
    public Photo[] newArray(int size) {
      return new Photo[size];
    }
  };

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public long getDataAdded() {
    return dataAdded;
  }

  public void setDataAdded(long dataAdded) {
    this.dataAdded = dataAdded;
  }

  public long getDataModified() {
    return dataModified;
  }

  public void setDataModified(long dataModified) {
    this.dataModified = dataModified;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

    dest.writeString(path);
    dest.writeLong(dataAdded);
    dest.writeLong(dataModified);
  }
}
