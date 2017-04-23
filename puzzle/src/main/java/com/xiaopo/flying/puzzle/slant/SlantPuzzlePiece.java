package com.xiaopo.flying.puzzle.slant;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * @author wupanjie
 */

public class SlantPuzzlePiece {
  private Drawable drawable;
  private Matrix matrix;
  private Matrix previousMatrix;
  private Area area;
  private Rect drawableBounds;

  public SlantPuzzlePiece(Drawable drawable,  Area area, Matrix matrix){
    this.drawable = drawable;
    this.area = area;
    this.matrix = matrix;
    this.drawableBounds = new Rect(0,0,getWidth(),getHeight());
  }

  public void draw(Canvas canvas){
    draw(canvas , 255);
  }

  public void draw(Canvas canvas, int alpha){
    canvas.save();

    canvas.clipPath(area.getAreaPath());
    canvas.concat(matrix);
    drawable.setBounds(drawableBounds);
    drawable.setAlpha(alpha);
    drawable.draw(canvas);

    canvas.restore();
  }

  public int getWidth(){
    return drawable.getIntrinsicWidth();
  }

  public int getHeight(){
    return drawable.getIntrinsicHeight();
  }
}
