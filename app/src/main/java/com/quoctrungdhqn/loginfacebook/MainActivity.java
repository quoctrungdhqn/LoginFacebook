package com.quoctrungdhqn.loginfacebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private CallbackManager mCallbackManager;
    private static final String REQUEST_FIELDS = "fields";
    private static final String REQUEST_FIELDS_LIST = "id,name,first_name,last_name,email,gender,birthday,location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mCallbackManager = CallbackManager.Factory.create();
        // Logs the user in with the requested read permissions.
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        handleLoginFacebook(mCallbackManager);
    }

    private void handleLoginFacebook(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                            if (response != null) {
                                try {
                                    Gson gson = new Gson();
                                    FacebookLoginResponse facebookLoginResponse = gson.fromJson(object.toString(), FacebookLoginResponse.class);
                                    Log.d(TAG, "Facebook ID: " + facebookLoginResponse.getId() + " - "
                                            + "Name: " + facebookLoginResponse.getName() + " - "
                                            + "Email: " + facebookLoginResponse.getEmail());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString(REQUEST_FIELDS, REQUEST_FIELDS_LIST);
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
