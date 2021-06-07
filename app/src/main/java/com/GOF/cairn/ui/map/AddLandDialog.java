package com.GOF.cairn.ui.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.GOF.cairn.AuthHold;
import com.GOF.cairn.R;

import org.jetbrains.annotations.NotNull;

public class AddLandDialog extends AppCompatDialogFragment {

    private EditText editText;
    private ExampleDialogListener listener;
    public Button lbtnAdd, lbtnCancel;
    public EditText ledtLandmarkHe;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.customdialog, null);

        builder.setView(view).setTitle("Add New Place").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               AuthHold.getInstance().newLand = ledtLandmarkHe.getText().toString();
               listener.applyTexts(AuthHold.getInstance().newLand);
            }
        });


        ledtLandmarkHe = view.findViewById(R.id.edtNewLandmarkName);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement the tings (listener)");
        }
    }
    public interface ExampleDialogListener {
        void applyTexts(String text);
    }


}