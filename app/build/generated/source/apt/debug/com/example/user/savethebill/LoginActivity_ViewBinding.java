// Generated code from Butter Knife. Do not modify!
package com.example.user.savethebill;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding<T extends LoginActivity> implements Unbinder {
  protected T target;

  private View view2131558530;

  private View view2131558532;

  private View view2131558531;

  private View view2131558529;

  @UiThread
  public LoginActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.email = Utils.findRequiredViewAsType(source, R.id.email, "field 'email'", TextInputLayout.class);
    target.password = Utils.findRequiredViewAsType(source, R.id.password, "field 'password'", TextInputLayout.class);
    view = Utils.findRequiredView(source, R.id.log_in, "field 'logIn' and method 'logIn'");
    target.logIn = Utils.castView(view, R.id.log_in, "field 'logIn'", Button.class);
    view2131558530 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.logIn();
      }
    });
    view = Utils.findRequiredView(source, R.id.sign_up, "method 'signUp'");
    view2131558532 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.signUp();
      }
    });
    view = Utils.findRequiredView(source, R.id.forgot_password, "method 'forgotPassword'");
    view2131558531 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.forgotPassword();
      }
    });
    view = Utils.findRequiredView(source, R.id.password_input, "method 'onEditorAction'");
    view2131558529 = view;
    ((TextView) view).setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView p0, int p1, KeyEvent p2) {
        return target.onEditorAction(p1);
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
    target.logIn = null;

    view2131558530.setOnClickListener(null);
    view2131558530 = null;
    view2131558532.setOnClickListener(null);
    view2131558532 = null;
    view2131558531.setOnClickListener(null);
    view2131558531 = null;
    ((TextView) view2131558529).setOnEditorActionListener(null);
    view2131558529 = null;

    this.target = null;
  }
}
