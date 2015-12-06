package com.consultica.techapalooza.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.consultica.techapalooza.R;

/**
 * Created by dimadruchinin on 30.11.15.
 */
public class DialogFactory {
    private static ProgressDialog progressDialog;

    public static Dialog showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar);
        progressDialog.show();
        return progressDialog;
    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    public static AlertDialog.Builder getDialogBuilder(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        return builder;
    }

    public static Dialog createConfirmDialog(final Activity activity, int messageId,
                                             DialogInterface.OnClickListener yesClickListener) {

        DialogInterface.OnClickListener noClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        };
        return createDialog(activity, messageId, yesClickListener, noClickListener);
    }

    public static Dialog createDialog(final Activity activity, int messageId,
                                      DialogInterface.OnClickListener yesClickListener,
                                      DialogInterface.OnClickListener noClickListener) {

        AlertDialog.Builder builder = DialogFactory.getDialogBuilder(activity);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(messageId);
        if (yesClickListener != null)
            builder.setPositiveButton(android.R.string.ok, yesClickListener);
        if (noClickListener != null)
            builder.setNegativeButton(android.R.string.no, noClickListener);

        return builder.create();
    }

    public static Dialog createDialog(final Activity activity, String message,
                                      DialogInterface.OnClickListener yesClickListener,
                                      DialogInterface.OnClickListener noClickListener) {
        AlertDialog.Builder builder = DialogFactory.getDialogBuilder(activity);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(message);
        if (yesClickListener != null)
            builder.setPositiveButton(android.R.string.ok, yesClickListener);
        if (noClickListener != null)
            builder.setNegativeButton(android.R.string.no, noClickListener);

        return builder.create();
    }

    public static Dialog showAlert(Activity activity, int messageId) {
        return showAlert(activity, activity.getString(messageId), activity.getString(R.string.alert));
    }

    public static Dialog showAlert(Activity activity, String message) {
        return showAlert(activity, message, activity.getString(R.string.alert));
    }

    public static Dialog showAlert(Activity activity, int messageId, String title) {
        return showAlert(activity, activity.getString(messageId), title);
    }

    public static Dialog showAlert(Activity activity, String message, String title) {
        AlertDialog.Builder builder = getDialogBuilder(activity);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);

        Dialog dialog = builder.create();
        dialog.show();

        return dialog;
    }
}
