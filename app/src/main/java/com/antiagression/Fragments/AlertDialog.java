package com.antiagression.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;

import com.antiagression.R;

public class AlertDialog extends DialogFragment {

    private CountDownTimer countDownTimer;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("00:10")
                .setMessage(R.string.alert_dialog_msg)
                .setNegativeButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        countDownTimer.cancel();
                    }
                });

        //On crée la fenêtre de confirmation d'alerte et on la retourne
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        countDownTimer.cancel();
    }

    public void setCountDownToAlertDialog() {
        countDownTimer = new CountDownTimer(10000, 100){
            @Override
            public void onTick(long millisUntilFinished) {
                getDialog().setTitle("00:" + Math.round(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                AlertFragment alertFragment = (AlertFragment) getTargetFragment();
                alertFragment.fetchAdress();
                dismiss();
            }

        }.start();
    }
}
