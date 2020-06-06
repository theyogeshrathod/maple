package com.coolapps.yo.maple;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

/**
 * Utility class to create all alerts in the app.
 */
public class MapleAlerts {

    // Do not instantiate, utility class.
    private MapleAlerts() { }

    public static AlertDialog.Builder createSomethingWentWrongAlert(
            @NonNull Context context, @Nullable DialogInterface.OnClickListener okButtonListener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.something_went_wrong_text);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.ok_text, okButtonListener);
        return dialog;
    }

    public static AlertDialog.Builder createWrongAccountTypeAlert(
            @NonNull Context context, @Nullable DialogInterface.OnClickListener okButtonListener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.account_type_mismatch_text);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.ok_text, okButtonListener);
        return dialog;
    }

    public static AlertDialog.Builder createSuccessfullySubmittedAlert(
            @NonNull Context context, @Nullable DialogInterface.OnClickListener okButtonListener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.successfully_submitted_text);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.ok_text, okButtonListener);
        return dialog;
    }

    public static AlertDialog.Builder createSuccessfullyUploadedAlert(
            @NonNull Context context, @Nullable DialogInterface.OnClickListener okButtonListener) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.successfully_uploaded_image_text);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.ok_text, okButtonListener);
        return dialog;
    }
}
