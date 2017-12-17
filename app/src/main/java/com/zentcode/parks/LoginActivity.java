package com.zentcode.parks;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.zentcode.parks.app.Messages;
import com.zentcode.parks.network.ObjectRequest;
import com.zentcode.parks.network.Routes;
import com.zentcode.parks.storage.PreferenceManager;
import com.zentcode.parks.storage.Preferences;
import com.zentcode.parks.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private PreferenceManager preferenceManager;
    private AppCompatEditText edtUser, edtPassword;
    private AppCompatButton btnLogin;
    private Map<String, String> body;

    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(this);

        isSessionActive();

        setContentView(R.layout.activity_login);

        edtUser = (AppCompatEditText) findViewById(R.id.edt_user);
        edtPassword = (AppCompatEditText) findViewById(R.id.edt_password);
        btnLogin = (AppCompatButton) findViewById(R.id.btn_login);

        Helper.setEditTextTint(edtUser, Helper.getResourceColor(this, R.color.colorText));
        Helper.setEditTextTint(edtPassword, Helper.getResourceColor(this, R.color.colorText));

        btnLogin.setOnClickListener(this);
    }

    private void isSessionActive() {
        if (preferenceManager.getInt(Preferences.USER_ID) > 0) {
            launchApp();
        }
    }

    private void launchApp() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void validateLogin() {
        String email = edtUser.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (!emailValidator(email)) {
            Toast.makeText(this, Messages.INVALID_EMAIL, Toast.LENGTH_SHORT).show();
        } else if (!passwordValidator(password)) {
            Toast.makeText(this, Messages.INVALID_PASSWORD, Toast.LENGTH_SHORT).show();
        } else {
            body = new HashMap<>();
            body.put("email", email);
            body.put("password", password);
            login();
        }
    }

    private void login() {
        final AlertDialog loading = Helper.showLoading(this);
        ObjectRequest request = new ObjectRequest(this);
        request.sendRequest(Request.Method.POST, Routes.getLoginUrl(), Helper.prepareJson(body), new ObjectRequest.RequestCallback() {
            @Override
            public void success(JSONObject response) {
                Helper.hideLoading(loading);
                try {
                    if (response.has("status")) {
                        Toast.makeText(LoginActivity.this, response.getString("status"), Toast.LENGTH_SHORT).show();
                    } else {
                        preferenceManager.saveInt(Preferences.USER_ID, response.getInt("user_id"));
                        preferenceManager.saveString(Preferences.USER_NAME, response.getString("user_name"));
                        preferenceManager.saveString(Preferences.API_TOKEN, response.getString("api_token"));
                        launchApp();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void error() {
                Helper.hideLoading(loading);
                Toast.makeText(LoginActivity.this, Messages.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean emailValidator(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return !email.matches("") && matcher.matches();
    }

    private boolean passwordValidator(String password) {
        return password.length() > 0 || !password.isEmpty() || !password.matches("");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            Helper.hideSoftKeyboard(this, view);
            validateLogin();
        }
    }
}