package in.ateesinfomedia.relief.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;

import static in.ateesinfomedia.relief.configurations.Global.MaxPriceFilterLimit;


public class FilterFragment extends DialogFragment {

    private static final String TAG = FilterFragment.class.getName();
    private MyPreferenceManager manager;

    private RangeSlider rangeSlider;
    private ConstraintLayout mCancel, mSave;
    private TextView minText, maxText;

    public OnFilterListener onFilterListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onFilterListener = (OnFilterListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach Activity: " + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);

        manager = new MyPreferenceManager(getActivity());
        rangeSlider = view.findViewById(R.id.filter_range_slider);
        minText = view.findViewById(R.id.filter_min_txt);
        maxText = view.findViewById(R.id.filter_max_txt);
        mCancel = view.findViewById(R.id.filter_cancel);
        mSave = view.findViewById(R.id.filter_save);

        if (getArguments() != null) {
            int from = getArguments().getInt("from");
            int to = getArguments().getInt("to");

            List<Float> initialSlider = new ArrayList<>();
            initialSlider.add((float) from);
            initialSlider.add((float) to);

            rangeSlider.setValues(
                    initialSlider
            );

            minText.setText("₹ " + from);
            if (to != MaxPriceFilterLimit) {
                maxText.setText("₹ " + to);
            } else {
                maxText.setText("Max");
            }
        }

        rangeSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                int mPrice = (int) value;
                return "₹ " + mPrice;
            }
        });

        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                int minPrice = slider.getValues().get(0).intValue();
                int maxPrice = slider.getValues().get(1).intValue();
                minText.setText("₹ " + minPrice);
                if (maxPrice != MaxPriceFilterLimit) {
                    maxText.setText("₹ " + maxPrice);
                } else {
                    maxText.setText("Max");
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "From: " + rangeSlider.getValues().get(0).intValue() +
                        " To: " + rangeSlider.getValues().get(1).intValue());
                onFilterListener.updateFilter(
                        rangeSlider.getValues().get(0).intValue(),
                        rangeSlider.getValues().get(1).intValue()
                );
                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }

    public interface OnFilterListener {
        void updateFilter(int lower, int upper);
    }

}