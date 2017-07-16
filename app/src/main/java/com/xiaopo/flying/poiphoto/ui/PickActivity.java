package com.xiaopo.flying.poiphoto.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.xiaopo.flying.anotherlayout.R;
import com.xiaopo.flying.poiphoto.Configure;
import com.xiaopo.flying.poiphoto.Define;

/**
 * @author wupanjie
 */
public class PickActivity extends AppCompatActivity {

  private Configure mConfigure;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.poiphoto_activity_pick);

    Intent intent = getIntent();
    mConfigure = intent.getParcelableExtra(Define.CONFIGURE);

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      changeStatusBar(mConfigure.getStatusBarColor());
    }

    getSupportFragmentManager().beginTransaction().replace(R.id.container, new AlbumFragment()).commit();
  }

  @TargetApi(21)
  private void changeStatusBar(int statusBarColor) {
    getWindow().setStatusBarColor(statusBarColor);
  }

  public void setConfigure(Configure configure) {
    mConfigure = configure;
  }

  public Configure getConfigure() {
    return mConfigure;
  }
}
