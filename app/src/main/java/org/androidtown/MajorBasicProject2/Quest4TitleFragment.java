package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class Quest4TitleFragment extends Fragment {


    EditText etTitle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rd4_title, container, false);

        ((RegisterActivity) getActivity()).btnNext.setText("저장 >");
        etTitle = view.findViewById(R.id.etTitle);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((RegisterActivity) getActivity()).btnNext.setText("다음 >");
        ((RegisterActivity) getActivity()).title = etTitle.getText().toString();
    }
}