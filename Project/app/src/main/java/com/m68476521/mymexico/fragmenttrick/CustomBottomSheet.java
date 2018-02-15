package com.m68476521.mymexico.fragmenttrick;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.m68476521.mymexico.R;

/**
 * BottomSheetDialogFragment to show the trick.
 */

public class CustomBottomSheet extends BottomSheetDialogFragment {

    private String bodyQuestion;
    private String bodyAnswer;
    private String bodyFakeA;
    private String bodyFakeB;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_dialog, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        TextView textView = contentView.findViewById(R.id.body_question);
        textView.setText(bodyQuestion);

        RadioButton radioButtonA = contentView.findViewById(R.id.radio_answer1);
        radioButtonA.setText(bodyAnswer);

        RadioButton radioButtonB = contentView.findViewById(R.id.radio_answer2);
        radioButtonB.setText(bodyFakeA);

        RadioButton radioButtonC = contentView.findViewById(R.id.radio_answer3);
        radioButtonC.setText(bodyFakeB);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bodyQuestion = getArguments().getString("bodyQuestion");
        bodyAnswer = getArguments().getString("bodyAnswer");
        bodyFakeA = getArguments().getString("bodyFakeA");
        bodyFakeB = getArguments().getString("bodyFakeB");
    }
}