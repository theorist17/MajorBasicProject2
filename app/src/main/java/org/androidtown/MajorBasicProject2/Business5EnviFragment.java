package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;


public class Business5EnviFragment extends Fragment {
    RegisterActivity registerActivity;
    View view;

    String space = "사무실";
    String[] spaces = {"사무실", "건설장", "식당", "모텔", "호텔", "펜션", "리조트", "아파트", "주택", "창고", "배", "비행기", "산", "바다", "호수"};
    boolean[] accesses = {false, false, false, false};
    boolean[] safeties = {false, false};

    NumberPicker capacity_picker;
    Switch switch1, switch2, switch3, switch4, switch5, switch6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rb5_envi, container, false);
        registerActivity = (RegisterActivity) getActivity();
        if(registerActivity.isVisibleClosePrevNext() == false)
            registerActivity.reverseClosePrevNext();

        capacity_picker = view.findViewById(R.id.numberPicker);
        capacity_picker.setMinValue(0);
        capacity_picker.setMaxValue(spaces.length-1);
        capacity_picker.setDisplayedValues(spaces);
        capacity_picker.setWrapSelectorWheel(false);
        capacity_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                space = spaces[newVal];
            }
        });

        switch1 = view.findViewById(R.id.switch1);
        switch2 = view.findViewById(R.id.switch2);
        switch3 = view.findViewById(R.id.switch3);
        switch4 = view.findViewById(R.id.switch4);
        switch5 = view.findViewById(R.id.switch5);
        switch6 = view.findViewById(R.id.switch6);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                accesses[0] = b;
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                accesses[1] = b;
            }
        });
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                accesses[2] = b;
            }
        });
        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                accesses[3] = b;
            }
        });
        switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                safeties[0] = b;
            }
        });
        switch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                safeties[1] = b;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        registerActivity.space = space;
        for(int i = 0; i< accesses.length;i++){
            if(accesses[i])
                registerActivity.accesses[i] = "true";
            else
                registerActivity.accesses[i] = "false";
        }
        for(int i = 0; i< safeties.length;i++){
            if(safeties[i])
                registerActivity.safeties[i] = "true";
            else
                registerActivity.safeties[i] = "false";
        }
    }
}
