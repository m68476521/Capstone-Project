package com.m68476521.mymexico.fragmenttrick;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.m68476521.mymexico.R;

import java.util.Random;

/**
 * BottomSheetDialogFragment to show the trick.
 */

public class CustomBottomSheet extends BottomSheetDialogFragment {
    private String bodyQuestion;
    private String bodyAnswer;
    private String bodyFakeA;
    private String bodyFakeB;

    private RadioButton radioButtonA;
    private RadioButton radioButtonB;
    private RadioButton radioButtonC;

    int realAnswer;

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

        radioButtonA = contentView.findViewById(R.id.radio_answer1);
        radioButtonB = contentView.findViewById(R.id.radio_answer2);
        radioButtonC = contentView.findViewById(R.id.radio_answer3);

        realAnswer = randomNumber();

        if (realAnswer == 1) {
            radioButtonA.setText(bodyAnswer);
            radioButtonB.setText(bodyFakeA);
            radioButtonC.setText(bodyFakeB);
        } else if (realAnswer == 2) {
            radioButtonA.setText(bodyFakeA);
            radioButtonB.setText(bodyAnswer);
            radioButtonC.setText(bodyFakeB);
        } else  {
            radioButtonA.setText(bodyFakeA);
            radioButtonB.setText(bodyFakeB);
            radioButtonC.setText(bodyAnswer);
        }

        Button checkButton = contentView.findViewById(R.id.button_check);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bodyQuestion = getArguments().getString("bodyQuestion");
        bodyAnswer = getArguments().getString("bodyAnswer");
        bodyFakeA = getArguments().getString("bodyFakeA");
        bodyFakeB = getArguments().getString("bodyFakeB");
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        String message;
        String titleMessage = "";
        if (radioButtonA.isChecked() && realAnswer == 1) {
            titleMessage = "Great!";
            message = "yes, the correct answer is A";
        }  else if (radioButtonB.isChecked() && realAnswer == 2) {
            Log.d("MIKE checkB", "MIKE:");
            titleMessage = "That's right";
            message = "yes, the correct answer is B";
        }  else if (radioButtonC.isChecked() && realAnswer == 3) {
            titleMessage = "Nice!";
            message = "yes, the correct answer is C";
        } else if (!radioButtonA.isChecked() && !radioButtonB.isChecked() && !radioButtonC.isChecked()) {
            titleMessage = "Something went wrong ...";
            message = "Please select one option";
        } else {
            titleMessage = "Ups!";
            message = "wrong answer try again";
        }

        VerifyDialogFragment editNameDialogFragment = VerifyDialogFragment.newInstance(titleMessage, message);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    private int randomNumber() {
        int max = 3;
        int min = 1;
        Random randomNum = new Random();
        return min + randomNum.nextInt(max);
    }
}