package com.demo.boonggdemo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;


public class L {
    private static AlertDialog.Builder builder;
    private static final String MYTAG = "mytag";
    private static ProgressDialog dialog;


    public static void setPreferences(Context act, String key, String value) {
        SharedPreferences settings = act.getSharedPreferences(act.getApplicationInfo().packageName + "_preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);

        // Commit the edits!
        editor.commit();
    }

    public static String getPreferences(Context act, String key, String def) {

        // [[NSUSerDefault standardUserDefault]setValue:firstName.text ForKey:@"keyname"]
        SharedPreferences settings = act.getSharedPreferences(act.getApplicationInfo().packageName + "_preferences", 0);
        return settings.getString(key, def);
    }

    public static void toast_short(String txt) {
        Toast.makeText(AppController.getAppContext(), txt, Toast.LENGTH_SHORT).show();
    }

    public static void toast_long(String txt) {
        Toast.makeText(AppController.getAppContext(), txt, Toast.LENGTH_LONG).show();
    }


    public static void l(Context context, String txt) {
        if (txt.length() > 0) {
            Log.d(context.getClass().getSimpleName(), txt);
        }
    }

    public static void l(String txt) {
        if (txt.length() > 0) {
            Log.d("mytag", txt);
        }
    }

    public static void pd(String msg, Context context) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.show();
    }

    public static void pd(String title, String msg, Context context) {

        dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
    }

    public static void pd(String title, String msg, int icon, Context context) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setIcon(icon);
        dialog.show();
    }

    public static void dismiss_pd() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showMessageDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(AppController.getAppContext().getString(android.R.string.ok), null)
                .show();
    }

    private boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        boolean haveConnectedInternet = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo)
        {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }

        haveConnectedInternet=isInternetAvailable();

        return haveConnectedWifi || haveConnectedMobile || haveConnectedInternet;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }


    public static String getText(EditText editText){
        return editText.getText().toString().trim();
    }
    public static String getText(TextView textView){
        return textView.getText().toString().trim();
    }
    public static String getRbText(RadioButton radioButton){
        return radioButton.getText().toString().trim();
    }

    public static String ReplaceAnd(String str)
    {
       str=str.replace("'","");

        return  "<![CDATA["+str+"]]>";

    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
