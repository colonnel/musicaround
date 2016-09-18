package ua.asd.musicaround.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import ua.asd.musicaround.R;


public class NetworkUtil {

    public interface ActionCallback {
        void onAction();
    }

    public static boolean checkInternetConnectivityWithErrorToast(Context context) {
        if (!hasInternetConnectivity(context)) {
            Toast.makeText(context, R.string.message_no_internet, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean hasInternetConnectivity(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void checkNetworkWithAlertDialogAndRetry(final Context context, final ActionCallback actionCallback) {
        if (!hasInternetConnectivity(context)) {
            AlertDialog networkConnectionAlert = new AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.message_no_internet))
                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkNetworkWithAlertDialogAndRetry(context, actionCallback);
                        }
                    })
                    .setCancelable(false)
                    .create();
            networkConnectionAlert.show();
        } else {
            actionCallback.onAction();
        }
    }

    public static void checkNetworkWithSnackbar(final View rootView, final ActionCallback actionCallback) {
        final Context context = rootView.getContext();
        if (!hasInternetConnectivity(context)) {
            UIUtils.showSnackBar(rootView, R.string.message_no_internet, Snackbar.LENGTH_INDEFINITE, R.string.retry, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkNetworkWithSnackbar(rootView, actionCallback);
                }
            });
        } else {
            actionCallback.onAction();
        }
    }
}
