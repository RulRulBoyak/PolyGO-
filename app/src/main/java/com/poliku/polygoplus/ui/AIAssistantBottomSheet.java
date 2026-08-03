package com.poliku.polygoplus.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.poliku.polygoplus.R;

public class AIAssistantBottomSheet extends BottomSheetDialogFragment {

    private TextView tvAiResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_ai_assistant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAiResult = view.findViewById(R.id.tvAiResult);
        Button btnImproveDescription = view.findViewById(R.id.btnImproveDescription);
        Button btnNegotiatePrice = view.findViewById(R.id.btnNegotiatePrice);
        Button btnSuggestFairPrice = view.findViewById(R.id.btnSuggestFairPrice);
        Button btnApply = view.findViewById(R.id.btnApply);

        btnImproveDescription.setOnClickListener(v -> {
            tvAiResult.setText("AI: This vintage camera is in excellent condition, featuring a classic lens and a durable leather case. Perfect for collectors and photography enthusiasts alike.");
        });

        btnNegotiatePrice.setOnClickListener(v -> {
            tvAiResult.setText("AI: 'I'm really interested in this item. Would you be open to considering a slightly lower price, perhaps closer to $45? I can pick it up today!'");
        });

        btnSuggestFairPrice.setOnClickListener(v -> {
            tvAiResult.setText("AI: Based on similar listings, a fair market price for this item would be between $40 and $55.");
        });

        btnApply.setOnClickListener(v -> {
            String result = tvAiResult.getText().toString();
            if (!result.equals(getString(R.string.result_placeholder))) {
                Toast.makeText(getContext(), "Applied: " + result, Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(getContext(), "Please generate a result first", Toast.LENGTH_SHORT).show();
            }
        });
    }
}