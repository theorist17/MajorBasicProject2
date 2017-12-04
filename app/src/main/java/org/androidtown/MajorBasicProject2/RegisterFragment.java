package org.androidtown.MajorBasicProject2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    public Button btnBusiness, btnQuest, btnRecruit;
    public Button btnRegister;

    int phase = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ((RegisterActivity) getActivity()).setButtonGone();

        btnBusiness = view.findViewById(R.id.btnRegisterBusiness);
        btnBusiness.setOnClickListener(this);
        btnQuest = view.findViewById(R.id.btnRegisterTask);
        btnQuest.setOnClickListener(this);
        btnRecruit = view.findViewById(R.id.btnRegisterRecruit);
        btnRecruit.setOnClickListener(this);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        switch (phase){
            case 1:
                phaseOne();
                break;
            case 2:
                phaseTwo();
                break;
            case 3:
                phaseThree();
                break;
            case 4:
                phaseFour();
                break;
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegisterBusiness:
                ((RegisterActivity) getActivity()).indexFragment(1);
                break;
            case R.id.btnRegisterTask:
                ((RegisterActivity) getActivity()).indexFragment(7);
                break;
            case R.id.btnRegisterRecruit:
                ((RegisterActivity) getActivity()).indexFragment(11);
                break;
            case R.id.btnRegister:
                ((RegisterActivity) getActivity()).uploadRegister();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((RegisterActivity) getActivity()).setButtonVisible();
    }
    public void phaseOne(){
        //색상변경
        btnBusiness.setBackgroundColor(Color.parseColor("#F6B352"));
        btnBusiness.setEnabled(true);
    }
    public void phaseTwo(){
        //색상변경
        btnBusiness.setBackgroundColor(Color.parseColor("#383A3F"));
        btnBusiness.setText("수정");
        btnQuest.setBackgroundColor(Color.parseColor("#F6B352"));
        btnBusiness.setEnabled(true);
        btnQuest.setEnabled(true);
    }
    public void phaseThree(){
        //색상변경
        btnQuest.setBackgroundColor(Color.parseColor("#383A3F"));
        btnQuest.setText("수정");
        btnRecruit.setBackgroundColor(Color.parseColor("#F6B352"));
        btnBusiness.setEnabled(true);
        btnQuest.setEnabled(true);
        btnRecruit.setEnabled(true);
    }
    public void phaseFour(){
        btnRecruit.setBackgroundColor(Color.parseColor("#383A3F"));
        btnRecruit.setText("수정");
        btnRegister.setBackgroundColor(Color.parseColor("#F6B352"));
        btnRegister.setVisibility(View.VISIBLE);
        btnBusiness.setEnabled(true);
        btnQuest.setEnabled(true);
        btnRecruit.setEnabled(true);
    }
}
