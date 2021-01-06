package com.illa.joliveapp.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class dialogtest extends Dialog {
    public dialogtest(@NonNull Context context) {
        super(context);
    }

    public dialogtest(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected dialogtest(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
