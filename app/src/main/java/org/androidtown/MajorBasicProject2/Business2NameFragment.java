package org.androidtown.MajorBasicProject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static android.app.Activity.RESULT_OK;


public class Business2NameFragment extends Fragment implements View.OnClickListener{

    public EditText etBusinessSignature, etBusinessLicense, etBusinessField, etBusinessBrand;
    Button btnBusinessField, btnBusinessBrand;

    String businessField;
    String businessBrand;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rb2_name, container, false);

        etBusinessSignature = view.findViewById(R.id.etBusinessSignature);
        etBusinessLicense = view.findViewById(R.id.etBusinessLicense);
        etBusinessField = view.findViewById(R.id.etBusinessField);
        etBusinessBrand = view.findViewById(R.id.etBusinessBrand);
        btnBusinessField = view.findViewById(R.id.btnBusinessField);
        btnBusinessBrand = view.findViewById(R.id.btnBusinessBrand);
        btnBusinessField.setOnClickListener(this);
        btnBusinessBrand.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()){
            case R.id.btnBusinessField:
                intent = new Intent(getActivity(), PageFieldActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btnBusinessBrand:
                intent = new Intent(getActivity(), PageBrandActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    businessField = data.getStringExtra("select");
                    etBusinessField.setText(businessField);
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    businessBrand = data.getStringExtra("select");
                    etBusinessBrand.setText(businessBrand);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((RegisterActivity)getActivity()).id_field_busi = etBusinessField.getText().toString();
        ((RegisterActivity)getActivity()).id_brand_busi = etBusinessBrand.getText().toString();
        ((RegisterActivity)getActivity()).signature = etBusinessSignature.getText().toString();
        ((RegisterActivity)getActivity()).license = etBusinessLicense.getText().toString();
    }
}
