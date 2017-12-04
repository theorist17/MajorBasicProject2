package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Quest3BodyFragment extends Fragment {
    EditText etBody;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rd3_body, container, false);
        etBody = view.findViewById(R.id.etBody);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((RegisterActivity) getActivity()).body = etBody.getText().toString();
    }
}
