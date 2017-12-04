package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static org.androidtown.MajorBasicProject2.R.id.tpEndTime;
import static org.androidtown.MajorBasicProject2.R.id.tpStartTime;

public class Recruit4TimeFragment extends Fragment implements View.OnClickListener{
    final int REQ_CODE_DATE_ACTIVITY = 1;

    TimePicker start, end;
    Button btnNext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rt3_time, container, false);
        ((RegisterActivity) getActivity()).setButtonGone();

        start = view.findViewById(tpStartTime);
        end = view.findViewById(tpEndTime);

        btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNext:
                Intent intent = new Intent(getActivity(), PageDateActivity.class);
                intent.putExtra("stage", "init");
                intent.putExtra("type", ((RegisterActivity) getActivity()).type);
                startActivityForResult(intent, REQ_CODE_DATE_ACTIVITY);
                break;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((RegisterActivity) getActivity()).setButtonGone();
        ((RegisterActivity) getActivity()).start = start.getHour() + ":" + start.getMinute() + ":00";
        ((RegisterActivity) getActivity()).end = end.getHour() + ":" + end.getMinute() + ":00";
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_DATE_ACTIVITY:
                if(resultCode == RESULT_OK){
                    ((RegisterActivity) getActivity()).start = start.getHour() + ":" + start.getMinute() + ":00";
                    ((RegisterActivity) getActivity()).end = end.getHour() + ":" + end.getMinute() + ":00";
                    ((RegisterActivity) getActivity()).id_date_list = (ArrayList<String>) data.getExtras().get("date");
                    ((RegisterFragment)((RegisterActivity) getActivity()).fragments[0]).phase = 4;
                    ((RegisterActivity) getActivity()).uploadRecruit();
                    ((RegisterActivity) getActivity()).initFragment();
                }
                break;
        }
    }
}
