/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ramkt.example.R;


/**
 * Alerts class handles all message which should be displayed
 * to the user and it uses {@link android.app.AlertDialog}
 */
public class Alerts {
    private static final String TAG = Alerts.class.getSimpleName();


    public void displayAlert(final Context context, final String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_alert);
                TextView text = (TextView) dialog.findViewById(R.id.txt_dialog);
                text.setText(message);
                dialog.show();
                dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }
}
