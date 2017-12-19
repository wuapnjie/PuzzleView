package com.xiaopo.flying.puzzle;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */
public interface PuzzleLayout {
  void setOuterBounds(RectF bounds);

  void layout();

  int getAreaCount();

  List<Line> getOuterLines();

  List<Line> getLines();

  Area getOuterArea();

  void update();

  void reset();

  Area getArea(int position);

  float width();

  float height();

  void setPadding(float padding);

  float getPadding();

  float getRadian();

  void setRadian(float radian);

  Info generateInfo();

  void setColor(int color);

  int getColor();

  void sortAreas();

  class Info implements Parcelable{
    public static final int TYPE_STRAIGHT = 0;
    public static final int TYPE_SLANT = 1;

    public int type;
    public ArrayList<Step> steps;
    public ArrayList<LineInfo> lineInfos;
    public float padding;
    public float radian;
    public int color;

    public float left;
    public float top;
    public float right;
    public float bottom;

    public Info(){

    }

    protected Info(Parcel in) {
      type = in.readInt();
      steps = in.createTypedArrayList(Step.CREATOR);
      lineInfos = in.createTypedArrayList(LineInfo.CREATOR);
      padding = in.readFloat();
      radian = in.readFloat();
      color = in.readInt();
      left = in.readFloat();
      top = in.readFloat();
      right = in.readFloat();
      bottom = in.readFloat();
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
      @Override
      public Info createFromParcel(Parcel in) {
        return new Info(in);
      }

      @Override
      public Info[] newArray(int size) {
        return new Info[size];
      }
    };

    public float width(){
      return right - left;
    }

    public float height(){
      return bottom - top;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      parcel.writeInt(type);
      parcel.writeTypedList(steps);
      parcel.writeTypedList(lineInfos);
      parcel.writeFloat(padding);
      parcel.writeFloat(radian);
      parcel.writeInt(color);
      parcel.writeFloat(left);
      parcel.writeFloat(top);
      parcel.writeFloat(right);
      parcel.writeFloat(bottom);
    }
  }

  class Step implements Parcelable{
    public static final int ADD_LINE = 0;
    public static final int ADD_CROSS = 1;
    public static final int CUT_EQUAL_PART_ONE = 2;
    public static final int CUT_EQUAL_PART_TWO = 3;
    public static final int CUT_SPIRAL = 4;

    public int type;
    public int direction;
    public int position;
    public int part;
    public int hSize;
    public int vSize;

    public Step(){

    }

    protected Step(Parcel in) {
      type = in.readInt();
      direction = in.readInt();
      position = in.readInt();
      part = in.readInt();
      hSize = in.readInt();
      vSize = in.readInt();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
      @Override
      public Step createFromParcel(Parcel in) {
        return new Step(in);
      }

      @Override
      public Step[] newArray(int size) {
        return new Step[size];
      }
    };

    public Line.Direction lineDirection() {
      return direction == 0 ? Line.Direction.HORIZONTAL : Line.Direction.VERTICAL;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      parcel.writeInt(type);
      parcel.writeInt(direction);
      parcel.writeInt(position);
      parcel.writeInt(part);
      parcel.writeInt(hSize);
      parcel.writeInt(vSize);
    }
  }

  class LineInfo implements Parcelable{
    public float startX;
    public float startY;
    public float endX;
    public float endY;

    public LineInfo(Line line) {
      startX = line.startPoint().x;
      startY = line.startPoint().y;
      endX = line.endPoint().x;
      endY = line.endPoint().y;
    }

    protected LineInfo(Parcel in) {
      startX = in.readFloat();
      startY = in.readFloat();
      endX = in.readFloat();
      endY = in.readFloat();
    }

    public static final Creator<LineInfo> CREATOR = new Creator<LineInfo>() {
      @Override
      public LineInfo createFromParcel(Parcel in) {
        return new LineInfo(in);
      }

      @Override
      public LineInfo[] newArray(int size) {
        return new LineInfo[size];
      }
    };

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      parcel.writeFloat(startX);
      parcel.writeFloat(startY);
      parcel.writeFloat(endX);
      parcel.writeFloat(endY);
    }
  }
}
