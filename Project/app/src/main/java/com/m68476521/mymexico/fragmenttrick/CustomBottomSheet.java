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
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.m68476521.mymexico.R;

import java.util.Random;

/**
 * BottomSheetDialogFragment to show the trick.
 */

public class CustomBottomSheet extends BottomSheetDialogFragment {
    private static final String EXTRA_BODY_QUESTION = "bodyQuestion";
    private static final String EXTRA_BODY_ANSWER = "bodyAnswer";
    private static final String EXTRA_BODY_FAKE_A = "bodyFakeA";
    private static final String EXTRA_BODY_FAKE_B = "bodyFakeB";

    private String bodyQuestion;
    private String bodyAnswer;
    private String bodyFakeA;
    private String bodyFakeB;

    private RadioButton radioButtonA;
    private RadioButton radioButtonB;
    private RadioButton radioButtonC;

    private int realAnswer;

    private final BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

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
        } else {
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
        bodyQuestion = getArguments().getString(EXTRA_BODY_QUESTION);
        bodyAnswer = getArguments().getString(EXTRA_BODY_ANSWER);
        bodyFakeA = getArguments().getString(EXTRA_BODY_FAKE_A);
        bodyFakeB = getArguments().getString(EXTRA_BODY_FAKE_B);
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        String message;
        String titleMessage;
        if (radioButtonA.isChecked() && realAnswer == 1) {
            titleMessage = getString(R.string.great);
            message = getString(R.string.yes_1);
        } else if (radioButtonB.isChecked() && realAnswer == 2) {
            titleMessage = getString(R.string.title_2);
            message = getString(R.string.message_2);
        } else if (radioButtonC.isChecked() && realAnswer == 3) {
            titleMessage = getString(R.string.title_3);
            message = getString(R.string.message_3);
        } else if (!radioButtonA.isChecked() && !radioButtonB.isChecked() && !radioButtonC.isChecked()) {
            titleMessage = getString(R.string.title_4);
            message = getString(R.string.message_4);
        } else {
            titleMessage = getString(R.string.title_5);
            message = getString(R.string.message_5);
        }

        VerifyDialogFragment editNameDialogFragment = VerifyDialogFragment.newInstance(titleMessage, message);
        editNameDialogFragment.show(fm, getString(R.string.tag_fragment));
    }

    private int randomNumber() {
        int max = 3;
        int min = 1;
        Random randomNum = new Random();
        return min + randomNum.nextInt(max);
    }
}