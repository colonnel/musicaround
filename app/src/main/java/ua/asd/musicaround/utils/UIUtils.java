package ua.asd.musicaround.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

public class UIUtils {

    /**
     * Show snackbar. Use instead of {@link android.widget.Toast}.
     *
     * @param rootView root view, or coordinatorLayout
     * @param message primary message for snackbar
     * @param length length of snackbar {@link Snackbar#LENGTH_INDEFINITE}, {@link Snackbar#LENGTH_LONG}, {@link Snackbar#LENGTH_SHORT}
     */
    public static void showSnackBar(final View rootView, String message, int length) {
        if (rootView != null) {
            Snackbar snackbar = Snackbar.make(rootView, message, length);
            snackbar.show();
        }
    }

    /**
     * Show snackbar with button. Use instead of {@link android.widget.Toast}.
     *
     * @param rootView root view, or coordinatorLayout
     * @param message primary message for snackbar. Resource string ID.
     * @param length length of snackbar {@link Snackbar#LENGTH_INDEFINITE}, {@link Snackbar#LENGTH_LONG}, {@link Snackbar#LENGTH_SHORT}
     * @param button text in button. Resource string ID.
     * @param onClickListener click listener on button click
     */
    public static void showSnackBar(final View rootView, @StringRes int message, int length, @NonNull Integer button, @NonNull View.OnClickListener onClickListener) {
        Context context = rootView.getContext();
        showSnackBar(rootView, context.getString(message), length, context.getString(button).toUpperCase(), onClickListener);
    }

    /**
     * Show snackbar with button. Use instead of {@link android.widget.Toast}.
     *
     * @param rootView root view, or coordinatorLayout
     * @param message primary message for snackbar
     * @param length length of snackbar {@link Snackbar#LENGTH_INDEFINITE}, {@link Snackbar#LENGTH_LONG}, {@link Snackbar#LENGTH_SHORT}
     * @param button text in button
     * @param onClickListener click listener on button click
     */
    public static void showSnackBar(final View rootView, String message, int length, @NonNull String button, @NonNull View.OnClickListener onClickListener) {
        if (rootView != null) {
            Snackbar snackbar = Snackbar.make(rootView, message, length);
            snackbar.setAction(button.toUpperCase(), onClickListener);
            snackbar.show();
        }
    }
}
