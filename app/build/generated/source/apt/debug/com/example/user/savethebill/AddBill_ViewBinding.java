// Generated code from Butter Knife. Do not modify!
package com.example.user.savethebill;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddBill_ViewBinding<T extends AddBill> implements Unbinder {
  protected T target;

  private View view2131558541;

  private View view2131558539;

  private View view2131558540;

  @UiThread
  public AddBill_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.gotoGallery, "method 'openGalleryImages'");
    view2131558541 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.openGalleryImages();
      }
    });
    view = Utils.findRequiredView(source, R.id.imageView, "method 'zoomImages'");
    view2131558539 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.zoomImages();
      }
    });
    view = Utils.findRequiredView(source, R.id.bu, "method 'openCameraImages'");
    view2131558540 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.openCameraImages();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (this.target == null) throw new IllegalStateException("Bindings already cleared.");

    view2131558541.setOnClickListener(null);
    view2131558541 = null;
    view2131558539.setOnClickListener(null);
    view2131558539 = null;
    view2131558540.setOnClickListener(null);
    view2131558540 = null;

    this.target = null;
  }
}
