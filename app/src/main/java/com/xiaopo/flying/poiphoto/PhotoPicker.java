package com.xiaopo.flying.poiphoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import com.xiaopo.flying.poiphoto.ui.PickActivity;

/**
 * outer interface
 * @author wupanjie
 */
public class PhotoPicker {
  private com.xiaopo.flying.poiphoto.Configure mConfigure;

  private PhotoPicker() {
    mConfigure = new com.xiaopo.flying.poiphoto.Configure();
  }

  private PhotoPicker(com.xiaopo.flying.poiphoto.Configure configure) {
    this.mConfigure = configure;
  }

  public static PhotoPicker newInstance() {
    return new PhotoPicker();
  }

  public static PhotoPicker newInstance(com.xiaopo.flying.poiphoto.Configure configure) {
    return new PhotoPicker(configure);
  }

  public PhotoPicker setToolbarColor(@ColorInt int color) {
    this.mConfigure.setToolbarColor(color);
    return this;
  }

  public PhotoPicker setToolbarTitleColor(@ColorInt int color) {
    this.mConfigure.setToolbarTitleColor(color);
    return this;
  }

  public PhotoPicker setAlbumTitle(String title) {
    this.mConfigure.setAlbumTitle(title);
    return this;
  }

  public PhotoPicker setPhotoTitle(String title) {
    this.mConfigure.setPhotoTitle(title);
    return this;
  }

  public PhotoPicker setNavIcon(@DrawableRes int resId) {
    this.mConfigure.setNavIcon(resId);
    return this;
  }

  public PhotoPicker setStatusBarColor(@ColorInt int color) {
    this.mConfigure.setStatusBarColor(color);
    return this;
  }

  public PhotoPicker setMaxNotice(String message) {
    this.mConfigure.setMaxNotice(message);
    return this;
  }

  public PhotoPicker setMaxCount(int count) {
    this.mConfigure.setMaxCount(count);
    return this;
  }

  public void pick(Context context) {
    Intent intent = new Intent(context, PickActivity.class);
    intent.putExtra(com.xiaopo.flying.poiphoto.Define.CONFIGURE, mConfigure);
    if (context instanceof Activity) {
      ((Activity) context).startActivityForResult(intent, Define.DEFAULT_REQUEST_CODE);
    } else {
      throw new IllegalStateException("the context need to use activity");
    }
  }
}
