package com.example.qloc.controller.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.qloc.R;

/**
 * Created by michael on 13.05.15.
 */
public class AlertDialogUtility {

    public static void showAlertDialog(Activity activity, DialogInterface.OnClickListener positive){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(R.string.alert_okay, positive);
        builder.setMessage(R.string.alert_dialog_message)
                .setTitle(R.string.alert_dialog_title);

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
