package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainContractFragment extends Fragment implements View.OnClickListener {
    Button btRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_m3_order, container, false);
        btRegister = view.findViewById(R.id.btnRegister);
        btRegister.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra("id_user", ((MainActivity)getActivity()).id_user);
                startActivity(intent);
                break;
        }
    }
}
