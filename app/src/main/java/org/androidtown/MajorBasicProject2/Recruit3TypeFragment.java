package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class Recruit3TypeFragment extends Fragment implements View.OnClickListener{
    View view;
    NumberPicker capacity_picker;
    Button btnNext;
    RadioGroup rg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rt2_type, container, false);

        ((RegisterActivity) getActivity()).btnNext.setVisibility(View.GONE);

        capacity_picker = view.findViewById(R.id.numberPicker);
        capacity_picker.setMinValue(0);
        capacity_picker.setMaxValue(100);
        capacity_picker.setWrapSelectorWheel(false);

        rg = view.findViewById(R.id.radioGroup);

        btnNext = view.findViewById(R.id.btnNext);
        btnNext.setEnabled(false);
        btnNext.setOnClickListener(this);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                btnNext.setEnabled(true);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNext:
                ((RegisterActivity)getActivity()).nextFragment();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((RegisterActivity) getActivity()).btnNext.setVisibility(View.VISIBLE);
        ((RegisterActivity) getActivity()).capacity = capacity_picker.getValue();

        int id = rg.getCheckedRadioButtonId();
        if(id==-1) return;
        RadioButton rb = view.findViewById(id);
        ((RegisterActivity) getActivity()).type = rb.getText().toString();
    }
}
