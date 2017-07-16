package com.xiaopo.flying.kits;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

/**
 * @author wupanjie
 */
public class WeakHandler extends Handler {
  private WeakReference<IHandler> ref;

  public WeakHandler(WeakHandler.IHandler handler) {
    this.ref = new WeakReference<>(handler);
  }

  public WeakHandler(Looper looper, WeakHandler.IHandler handler) {
    super(looper);
    this.ref = new WeakReference<>(handler);
  }

  public void handleMessage(Message msg) {
    WeakHandler.IHandler handler = (WeakHandler.IHandler)this.ref.get();
    if(handler != null && msg != null) {
      handler.handleMsg(msg);
    }

  }

  public interface IHandler {
    void handleMsg(Message msg);
  }
}
