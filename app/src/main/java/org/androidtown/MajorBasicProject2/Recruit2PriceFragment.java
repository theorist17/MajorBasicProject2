package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class Recruit2PriceFragment extends Fragment {

    EditText etPrice, etBonus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rt1_price, container, false);
        etPrice = view.findViewById(R.id.etPrice);
        etBonus = view.findViewById(R.id.etBonus);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((RegisterActivity) getActivity()).price = etPrice.getText().toString();
        ((RegisterActivity) getActivity()).bonus = etBonus.getText().toString();
    }
}
