package com.microsoft.xbox.toolkit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;


public class XLEManagedAlertDialog extends AlertDialog implements IXLEManagedDialog {
    private DialogType dialogType = DialogType.NORMAL;

    protected XLEManagedAlertDialog(Context context) {
        super(context);
    }

    public Dialog getDialog() {
        return this;
    }

    public DialogType getDialogType() {
        return this.dialogType;
    }

    public void setDialogType(DialogType dialogType2) {
        this.dialogType = dialogType2;
    }

    public void safeDismiss() {
        DialogManager.getInstance().dismissManagedDialog(this);
    }

    public void quickDismiss() {
        super.dismiss();
    }

    public void onStop() {
        super.onStop();
        DialogManager.getInstance().onDialogStopped(this);
    }
}
