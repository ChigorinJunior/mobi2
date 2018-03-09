package com.exsample.maria.mobi2.present;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.ui.AuthActivity;
import com.exsample.maria.mobi2.ui.MapActivity;
import com.exsample.maria.mobi2.ui.ProfileActivity;
import com.exsample.maria.mobi2.view.MapView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by maria on 28.02.2018
 */

@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> {

    private static final int SIGN_IN = 11;

    private void changeText(String greeting) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getViewState().sayHi(String.format(greeting, user.getEmail()));
        }
    }

    public void signOutBtnPressed() {
        signOut();
    }

    private void signOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            AuthUI.getInstance()
                    .signOut(activity)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                    activity.sayHi(R.string.hello_world);
                                }
                            } else {
                                if (task.getException() != null) {
                                    activity.showError(task.getException().getMessage());
                                }
                            }
                        }
                    });
        }
    }

    public void signInBtnPressed() {
        signOut(activity);
        activity.startActivityForResult(AuthActivity.start(activity), SIGN_IN);
    }

    @SuppressLint("RestrictedApi")
    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                changeText(activity);
            } else {
                if (response != null) {
                    if (response.getException() != null) {
                        activity.showError(response.getException().getMessage());
                    }
                }
            }
        }
    }

    public void onCreateActivity(String greeting) {
        changeText(greeting);
    }

    public void profileBtnPressed(MapActivity activity) {
        activity.startActivity(ProfileActivity.start(activity));
    }
}
