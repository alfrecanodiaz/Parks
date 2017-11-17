package com.zentcode.parks.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zentcode.parks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Helper {

    public static int getResourceColor(Context context, int resource) {
        return ResourcesCompat.getColor(context.getResources(), resource, context.getTheme());
    }

    public static AlertDialog showProgressDialog(Context context) {
        AlertDialog dialog = new SpotsDialog(context, R.style.ProgressDialog);
        dialog.show();
        return dialog;
    }

    public static void hideProgressDialog(AlertDialog dialog) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void setEditTextTint(AppCompatEditText edt, int color) {
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        ViewCompat.setBackgroundTintList(edt, colorStateList);
    }

    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static String prepareJson(Map<String, String> body) {
        JSONObject jsonObj = new JSONObject();
        try {
            for (Map.Entry<String, String> pair : body.entrySet()) {
                jsonObj.put(pair.getKey(), pair.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(jsonObj);
    }
}