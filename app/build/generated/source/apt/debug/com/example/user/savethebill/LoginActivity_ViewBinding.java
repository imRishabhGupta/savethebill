// Generated code from Butter Knife. Do not modify!
package com.example.user.savethebill;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding<T extends LoginActivity> implements Unbinder {
  protected T target;

  private View view2131558535;

  private View view2131558537;

  private View view2131558536;

  private View view2131558534;

  @UiThread
  public LoginActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.email = Utils.findRequiredViewAsType(source, R.id.email, "field 'email'", TextInputLayout.class);
    target.password = Utils.findRequiredViewAsType(source, R.id.password, "field 'password'", TextInputLayout.class);
    target.autocompleteEmail = Utils.findRequiredViewAsType(source, R.id.email_input, "field 'autocompleteEmail'", AutoCompleteTextView.class);
    view = Utils.findRequiredView(source, R.id.log_in, "field 'logIn' and method 'logIn'");
    target.logIn = Utils.castView(view, R.id.log_in, "field 'logIn'", Button.class);
    view2131558535 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.logIn();
      }
    });
    view = Utils.findRequiredView(source, R.id.sign_up, "method 'signUp'");
    view2131558537 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.signUp();
      }
    });
    view = Utils.findRequiredView(source, R.id.forgot_password, "method 'forgotPassword'");
    view2131558536 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.forgotPassword();
      }
    });
    view = Utils.findRequiredView(source, R.id.password_input, "method 'onEditorAction'");
    view2131558534 = view;
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
    target.autocompleteEmail = null;
    target.logIn = null;

    view2131558535.setOnClickListener(null);
    view2131558535 = null;
    view2131558537.setOnClickListener(null);
    view2131558537 = null;
    view2131558536.setOnClickListener(null);
    view2131558536 = null;
    ((TextView) view2131558534).setOnEditorActionListener(null);
    view2131558534 = null;

    this.target = null;
  }
}
