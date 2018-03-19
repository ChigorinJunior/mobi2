package com.exsample.maria.mobi2.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.mvp.present.AuthPresenter;
import com.exsample.maria.mobi2.mvp.view.AuthView;

public class AuthActivity extends MvpAppCompatActivity implements AuthView {

    private EditText emailEd;
    private EditText passEd;
    private Button regBtn;
    private Button loginBtn;

    @InjectPresenter
    AuthPresenter presenter;

    public static Intent start(Context context) {
        return new Intent(context, AuthActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        configViews();
        fillScroll(); // позволяет растянуть scrollView на высоту экрана

        emailEd.setText(getString(R.string.email));
        passEd.setText(getString(R.string.pass));
    }

    private void configViews() {
        emailEd = findViewById(R.id.enterEmail);
        passEd = findViewById(R.id.enterPass);
        regBtn = findViewById(R.id.regBtn);
        loginBtn = findViewById(R.id.loginBtn);

        emailEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.textChanged(edGetTextLength(emailEd), edGetTextLength(passEd));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.textChanged(edGetTextLength(emailEd), edGetTextLength(passEd));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.regBtnPressed(AuthActivity.this, edGetText(emailEd), edGetText(passEd));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginBtnPressed(AuthActivity.this, edGetText(emailEd), edGetText(passEd));
            }
        });
    }

    private String edGetText(EditText ed) {
        return ed.getText().toString();
    }

    private int edGetTextLength(EditText ed) {
        return ed.getText().length();
    }

    // Растягивает view на весь экран
    private void fillScroll() {
        ImageView scrolling_layout = findViewById(R.id.scrolling_layout);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) scrolling_layout.getLayoutParams();

        // Высота экрана
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Высота ActionBar
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        // Высота строки состояния
        int stateBarHeight = 0;
        int resourceId = getResources().getIdentifier(
                getString(R.string.status_bar_height), getString(R.string.dimen), getString(R.string.android));
        if (resourceId > 0) {
            stateBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        params.height = size.y - actionBarHeight - stateBarHeight;

        scrolling_layout.requestLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // как называется эта кнопка?
        if (item.getItemId() == 16908332) {
            presenter.backBtnPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUpLoginBtn(int resText, int resColor, boolean enabled) {
        loginBtn.setText(resText);
        loginBtn.setBackgroundColor(this.getResources().getColor(resColor));
        loginBtn.setEnabled(enabled);
        regBtn.setEnabled(enabled);
    }

    @Override
    public void say(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void close(int result) {
        setResult(result, new Intent());
        finish();
    }
}
