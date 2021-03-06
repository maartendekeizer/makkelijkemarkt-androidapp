/**
 * Copyright (C) 2016 X Gemeente
 *                    X Amsterdam
 *                    X Onderzoek, Informatie en Statistiek
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/
 */
package com.amsterdam.marktbureau.makkelijkemarkt;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.amsterdam.marktbureau.makkelijkemarkt.api.ApiGetLogout;
import com.amsterdam.marktbureau.makkelijkemarkt.api.ApiGetVersion;
import com.amsterdam.marktbureau.makkelijkemarkt.api.MakkelijkeMarktApiService;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Utility class containing various commonly used static helper functions
 * @author marcolangebeeke
 */
public class Utility {

    /**
     * Log helper to show the package name, component name, and message
     * @param context a context needed for retrieving the app name from the strings resource
     * @param logTag the name of the component
     * @param message the message we want to log
     */
    public static void log(Context context, String logTag, String message) {
        Log.d(context.getString(R.string.log_tag_package), logTag + " " + context.getString(R.string.log_tag_separator) + " " + message);
    }

    /**
     * Show a toast for a short while
     * @param text String
     */
    public static Toast showToast(Context context, Toast toast, String text) {

        // if the toast is already active, cancel it
        if (toast != null) {
            toast.cancel();
        }

        // set the toast text and show it for a short moment
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();

        return toast;
    }

    /**
     * Check if the network is available
     * @return boolean
     */
    public static boolean isNetworkAvailable(Context context) {

        // get the connectivity manager service
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // get info about the network
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // return true if we have networkinfo and are connected
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    /**
     * Check if we are connected to a wifi connection
     * @return boolean indicating if we are connected
     */
    public static boolean isWifiConnected(Context context) {
        boolean isConnected = false;

        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo info = manager.getConnectionInfo();
            if (info.getNetworkId() != -1) {
                isConnected = true;
            }
        }

        return isConnected;
    }

    /**
     * Hide the softkeyboard from an activity
     * @param activity Activity
     */
    public static void hideKeyboard(Activity activity) {

        // get the inputmanager from the activity
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        // find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();

        // if no view currently has focus, create a new one, just so we can grab a window token from it
        if(view == null) {
            view = new View(activity);
        }

        // and hide the keyboard
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Helper to join a list of string items separating them using given separator
     * @param list list containing the strings
     * @param separator the string to separate the listitems
     * @return string containing <separator> separated items
     */
    public static String listToCsv(List<String> list, String separator) {
        List<String> rolesCopy = new ArrayList<>(list);

        StringBuilder builder = new StringBuilder();

        if (rolesCopy.size() > 0) {
            builder.append(rolesCopy.remove(0));
            for (String role : rolesCopy) {
                builder.append(separator);
                builder.append(role);
            }
        }

        return builder.toString();
    }

    /**
     * Map a string resource id to a color resource id based on name
     * @param context the context to get the resources from
     * @param sollicitatieStatus the sollicitatie status string
     * @return the mapped color resource id
     */
    public static int getSollicitatieStatusColor(Context context, String sollicitatieStatus) {
        int sollicitatieStatusColor = R.color.sollicitatie_status_undefined;

        if (sollicitatieStatus.equals(context.getString(R.string.sollicitatie_status_lot))) {
            sollicitatieStatusColor = R.color.sollicitatie_status_lot;
        } else if (sollicitatieStatus.equals(context.getString(R.string.sollicitatie_status_soll))) {
            sollicitatieStatusColor = R.color.sollicitatie_status_soll;
        } else if (sollicitatieStatus.equals(context.getString(R.string.sollicitatie_status_vkk))) {
            sollicitatieStatusColor = R.color.sollicitatie_status_vkk;
        } else if (sollicitatieStatus.equals(context.getString(R.string.sollicitatie_status_vpl))) {
            sollicitatieStatusColor = R.color.sollicitatie_status_vpl;
        }

        return sollicitatieStatusColor;
    }

    /**
     * Open/close meldingen view
     * @param view view that needs to collapse
     * @param collapse collapse or open
     */
    public static void collapseView(View view, boolean collapse) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();

        if (collapse) {
            lp.height = 0;
        } else {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        view.setLayoutParams(lp);
    }

    /**
     * Get a resource id from a resource name and type
     * @param name The name of the resource as a string
     * @param type the type of resource (R.id.class, etc.)
     * @return a unique id as an int
     */
    public static int getResId(String name, Class<?> type) {
        try {
            Field idField = type.getDeclaredField(name);
            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Convert a binary number to a hexadecimal number
     * @param value binary number
     * @return hexadecimal number as string
     */
    public static String binToHex(byte[] value) {
        if (value != null) {
            return String.format("%0" + (value.length * 2) + "X", new BigInteger(1, value));
        } else {
            return "";
        }
    }

    /**
     * Convert a binary number to a decimal number string
     * @param value binary number
     * @return decimal number string
     */
    public static String binToDec(byte[] value) {
        if (value != null) {
            return new BigInteger(1, value).toString();
        } else {
            return "";
        }
    }

    /**
     * Generate a random hex-decimal string of given length
     * @param length length of the hex string
     * @return hex string
     */
    public static String getRandomHexString(int length){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < length){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, length);
    }

    /**
     * Capatilze the first character of a string
     * @param original string to capatilize
     * @return capatilized string
     */
    public static String capitalize(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    /**
     * Check if the timestamp in a given shared preferences key is older than given timeout
     * @param key the shared preferences key
     * @param timeout the timeout in milliseconds
     * @return boolean
     */
    public static boolean isTimedOut(Context context, String key, int timeout) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        long diffInHours = context.getResources().getInteger(timeout);
        if (sharedPreferences.contains(key)) {
            long lastFetchTimestamp = sharedPreferences.getLong(key, 0);
            long differenceMs = new Date().getTime() - lastFetchTimestamp;
            diffInHours = TimeUnit.MILLISECONDS.toHours(differenceMs);
        }

        return diffInHours >= context.getResources().getInteger(timeout);
    }

    /**
     * Log the user out by clearing the authentication details from the sharedpreferences,
     * optionally sending a logout call to the api, and sending the user back to the main activity
     * login screen
     * @param context context we are called from
     * @param callApi should we send a logout request to the api?
     */
    public static void logout(Context context, boolean callApi) {

        // stop the api service
        Intent apiServiceIntent = new Intent(context, MakkelijkeMarktApiService.class);
        context.stopService(apiServiceIntent);

        // call api logout method (async, but without handling the response)
        if (callApi) {
            ApiGetLogout getLogout = new ApiGetLogout(context);
            getLogout.enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                }
                @Override
                public void onFailure(Throwable t) {
                }
            });
        }

        // clear uuid and all selected markt details from shared preferences
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit()
                .remove(context.getString(R.string.sharedpreferences_key_uuid))
                .remove(context.getString(R.string.sharedpreferences_key_markt_id))
                .remove(context.getString(R.string.sharedpreferences_key_markt_naam))
                .remove(context.getString(R.string.sharedpreferences_key_markt_producten))
                .apply();

        // clear activity history stack and open mainactivity home screen in new task
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Helper to get the real readable android version codename
     * @return string with android version codename
     */
    public static String getAndroidVersionCodename() {

        String androidVersionCodename = Build.VERSION.CODENAME;
        int androidVersionSdkLevel = Build.VERSION.SDK_INT;

        if (androidVersionCodename.equals("REL")) {
            Field[] fields = Build.VERSION_CODES.class.getFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                int fieldValue = -1;

                try {
                    fieldValue = field.getInt(new Object());
                } catch (Exception e) {
                    Log.e(Utility.class.getSimpleName(), "Could not find android version codename: " + e.getMessage());
                }

                if (fieldValue == androidVersionSdkLevel) {
                    androidVersionCodename = fieldName;
                }
            }
        }

        return androidVersionCodename;
    }

    /**
     * Get the name of the application and android version
     */
    public static String getAppName(Context context) {

        String appTitle = context.getString(R.string.app_title);
        String androidVersionCodename = Utility.getAndroidVersionCodename();
        String androidVersionNumber = Build.VERSION.RELEASE;
        int androidVersionSdkLevel = Build.VERSION.SDK_INT;

        if (androidVersionCodename != null && androidVersionNumber != null && androidVersionSdkLevel > 0) {
            return appTitle + " for Android (" + androidVersionCodename + " " + androidVersionNumber + " API level " + androidVersionSdkLevel + ")";
        }

        return null;
    }

    /**
     * Get the version code and name of the application package
     */
    public static String getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int versionCode = info.versionCode;
            String versionName = info.versionName;
            if (versionCode > 0 && versionName != null) {
                return versionName + " (" + versionCode + ")";
            }
        } catch (Exception e) {
            Utility.log(context, Utility.class.getSimpleName(), "PackageInfo not found: " + e.getMessage());
        }

        return null;
    }

    /**
     * Get the serial number of the device
     * @return string serial number
     */
    public static String getSerialNumber() {
        String serial = null;

        // try to get the ril.serialnumber from the system properties (samsung devices only(?))
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serial = (String) get.invoke(c, "ril.serialnumber", "unknown");
        } catch (Exception e) {
            Log.e(Utility.class.getSimpleName(), "Could not get ril.serialnumber from SystemProperties: " + e.getMessage());
        }

        // if unknown use the build serial
        if (serial == null || serial.equals("unknown")) {
            serial = Build.SERIAL;
        }

        return serial;
    }

    /**
     * Check if there is a newer build of the app available
     */
    public static void checkForUpdate(final Activity activity, final String fragmentTag, final boolean silent) {

        // get the latest android build number from the api version call
        ApiGetVersion getVersion = new ApiGetVersion(activity);
        getVersion.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Response<JsonObject> response) {
                if (response.isSuccess() && response.body() != null) {
                    long versionCode = 0;
                    long androidBuild = 0;

                    // get androidBuild from api response
                    try {
                        JsonObject versionInfo = response.body().getAsJsonObject();
                        if (versionInfo != null &&
                                versionInfo.get(activity.getString(R.string.makkelijkemarkt_api_version_app_android_build_name)) != null &&
                                !versionInfo.get(activity.getString(R.string.makkelijkemarkt_api_version_app_android_build_name)).isJsonNull()) {
                            androidBuild = versionInfo.get(activity.getString(R.string.makkelijkemarkt_api_version_app_android_build_name)).getAsLong();
                        } else {
                            Log.e(Utility.class.getSimpleName(), "androidBuild not found in VersionInfo");
                        }
                    } catch (IllegalStateException e) {
                        Log.e(Utility.class.getSimpleName(), "VersionInfo not found: " + e.getMessage());
                    }

                    // get the versionCode of the currently installed app
                    final String appPackageName = activity.getPackageName();
                    try {
                        PackageInfo info = activity.getPackageManager().getPackageInfo(appPackageName, 0);
                        versionCode = info.versionCode;
                    } catch (Exception e) {
                        Log.e(Utility.class.getSimpleName(), "PackageInfo not found: " + e.getMessage());
                    }

                    // check if androidBuild is higher than current versionCode
                    if (versionCode > 0 && androidBuild > 0 && androidBuild > versionCode) {

                        // show a modal dialog with an app update notice that will open the play store app
                        DialogFragment updateFragment = UpdateAvailableDialogFragment.newInstance(appPackageName);
                        updateFragment.setCancelable(false);
                        updateFragment.show(activity.getFragmentManager(), fragmentTag);
                    } else if (!silent) {
                        showToast(activity, new Toast(activity), activity.getString(R.string.dialog_app_update_not_available));
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(Utility.class.getSimpleName(), "Failed to retrieve the version info");
            }
        });
    }
}