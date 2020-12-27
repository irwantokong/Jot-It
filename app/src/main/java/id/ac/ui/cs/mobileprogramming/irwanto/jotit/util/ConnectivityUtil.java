package id.ac.ui.cs.mobileprogramming.irwanto.jotit.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectivityUtil {
    ConnectivityManager cm;

    public ConnectivityUtil(Context context) {
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isNetworkConnected() {
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
