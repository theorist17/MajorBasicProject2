package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ProfileHeaderFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    Button btnAlarm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_header, container, false);

        btnAlarm = view.findViewById(R.id.btnHeaderAlarm);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

}
