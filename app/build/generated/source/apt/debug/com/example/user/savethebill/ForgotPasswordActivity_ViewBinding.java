// Generated code from Butter Knife. Do not modify!
package com.example.user.savethebill;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ForgotPasswordActivity_ViewBinding<T extends ForgotPasswordActivity> implements Unbinder {
  protected T target;

  private View view2131558528;

  private View view2131558529;

  @UiThread
  public ForgotPasswordActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.inputEmail = Utils.findRequiredViewAsType(source, R.id.email, "field 'inputEmail'", TextInputLayout.class);
    view = Utils.findRequiredView(source, R.id.btn_reset_password, "field 'btnReset' and method 'reset'");
    target.btnReset = Utils.castView(view, R.id.btn_reset_password, "field 'btnReset'", Button.class);
    view2131558528 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.reset();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_back, "field 'btnBack' and method 'back'");
    target.btnBack = Utils.castView(view, R.id.btn_back, "field 'btnBack'", Button.class);
    view2131558529 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.back();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.inputEmail = null;
    target.btnReset = null;
    target.btnBack = null;

    view2131558528.setOnClickListener(null);
    view2131558528 = null;
    view2131558529.setOnClickListener(null);
    view2131558529 = null;

    this.target = null;
  }
}
