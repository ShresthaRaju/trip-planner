package com.raju.tripplanner.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.raju.tripplanner.R;

public class DialogProgress extends AppCompatDialogFragment {

    private String dialogText;

    public DialogProgress(String dialogText) {
        this.dialogText = dialogText;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder loader = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.loader, null);
        loader.setView(view);

        TextView tvLoading = view.findViewById(R.id.tv_loading);
        tvLoading.setText(dialogText);

        return loader.create();
    }
}
