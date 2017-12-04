package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


public class Business6ServFragment extends Fragment {
    View view;

    boolean[] facilities = {false, false, false, false, false, false, false, false};
    boolean[] welfares = {false, false, false, false, false, false, false, false, false, false, false};

    Switch switch1, switch2, switch3, switch4, switch5, switch6, switch7, switch8;
    Switch switch9, switch10, switch11, switch12, switch13, switch14, switch15, switch16, switch17, switch18, switch19;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rb6_serv, container, false);

        ((RegisterActivity)getActivity()).btnNext.setText("저장 >");

        switch1 = view.findViewById(R.id.switch1);
        switch2 = view.findViewById(R.id.switch2);
        switch3 = view.findViewById(R.id.switch3);
        switch4 = view.findViewById(R.id.switch4);
        switch5 = view.findViewById(R.id.switch5);
        switch6 = view.findViewById(R.id.switch6);
        switch7 = view.findViewById(R.id.switch7);
        switch8 = view.findViewById(R.id.switch8);
        switch9 = view.findViewById(R.id.switch9);
        switch10 = view.findViewById(R.id.switch10);
        switch11 = view.findViewById(R.id.switch11);
        switch12 = view.findViewById(R.id.switch12);
        switch13 = view.findViewById(R.id.switch13);
        switch14 = view.findViewById(R.id.switch14);
        switch15 = view.findViewById(R.id.switch15);
        switch16 = view.findViewById(R.id.switch16);
        switch17 = view.findViewById(R.id.switch17);
        switch18 = view.findViewById(R.id.switch18);
        switch19 = view.findViewById(R.id.switch19);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                facilities[0] = b;
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                facilities[1] = b;
            }
        });
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                facilities[2] = b;
            }
        });
        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                facilities[3] = b;
            }
        });
        switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                facilities[4] = b;
            }
        });
        switch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                facilities[5] = b;
            }
        });
        switch7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                facilities[6] = b;
            }
        });
        switch8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                facilities[7] = b;
            }
        });

        switch9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[0] = b;
            }
        });
        switch10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[1] = b;
            }
        });
        switch11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[2] = b;
            }
        });
        switch12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[3] = b;
            }
        });
        switch13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[4] = b;
            }
        });
        switch14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[5] = b;
            }
        });
        switch15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[6] = b;
            }
        });
        switch16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[7] = b;
            }
        });
        switch17.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[8] = b;
            }
        });
        switch18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[9] = b;
            }
        });
        switch19.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                welfares[10] = b;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        for(int i = 0; i< facilities.length;i++){
            if(facilities[i])
                ((RegisterActivity)getActivity()).facilities[i] = "true";
            else
                ((RegisterActivity)getActivity()).facilities[i] = "false";
        }
        for(int i = 0; i< welfares.length;i++){
            if(welfares[i])
                ((RegisterActivity)getActivity()).welfares[i] = "true";
            else
                ((RegisterActivity)getActivity()).welfares[i] = "false";
        }

        ((RegisterActivity)getActivity()).btnNext.setText("다음 >");
    }
}