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

public class SignUpActivity_ViewBinding<T extends SignUpActivity> implements Unbinder {
  protected T target;

  private View view2131558533;

  @UiThread
  public SignUpActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.email = Utils.findRequiredViewAsType(source, R.id.email, "field 'email'", TextInputLayout.class);
    target.password = Utils.findRequiredViewAsType(source, R.id.password, "field 'password'", TextInputLayout.class);
    target.confirmPassword = Utils.findRequiredViewAsType(source, R.id.confirm_password, "field 'confirmPassword'", TextInputLayout.class);
    view = Utils.findRequiredView(source, R.id.sign_up, "field 'signUp' and method 'signUp'");
    target.signUp = Utils.castView(view, R.id.sign_up, "field 'signUp'", Button.class);
    view2131558533 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.signUp();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.email = null;
    target.password = null;
    target.confirmPassword = null;
    target.signUp = null;

    view2131558533.setOnClickListener(null);
    view2131558533 = null;

    this.target = null;
  }
}
