package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Business3LocaFragment extends Fragment implements View.OnClickListener {
    EditText etCountry, etProvince, etCity, etTown, etRoad, etPostCode;
    Button btnAddAddress, btnNext;

    AdapterAddressListView adapter;
    ListView lvAddress;

    boolean overwrite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rb3_loca, container, false);
        overwrite = false;

        ((RegisterActivity)getActivity()).setButtonGone();
        etCountry = view.findViewById(R.id.etCountry);
        etProvince = view.findViewById(R.id.etProvince);
        etCity = view.findViewById(R.id.etCity);
        etTown = view.findViewById(R.id.etTown);
        etRoad = view.findViewById(R.id.etRoad);
        etPostCode = view.findViewById(R.id.etPostcode);
        btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        btnAddAddress = view.findViewById(R.id.btnAddAddress);
        btnAddAddress.setOnClickListener(this);
        adapter = new AdapterAddressListView();

        lvAddress = view.findViewById(R.id.lvAddress);

        if(((RegisterActivity)getActivity()).id_address != null) {
            overwrite = true;
            selectAddress();
        }
        downloadAddress();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddAddress:
                uploadAddress();
                break;
            case R.id.btnNext:
                uploadAddress();
                ((RegisterActivity)getActivity()).nextFragment();
                break;
        }
    }
    public void downloadAddress(){
        HandlerDB download_business = new HandlerDB("download_address.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_user", ((RegisterActivity)getActivity()).id_user);
        download_business.execute(input);
        download_business.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    printAddress(dbResult);
                } else {
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void selectAddress(){
        HandlerDB download_address = new HandlerDB("select_address.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_address", ((RegisterActivity)getActivity()).id_address);
        download_address.execute(input);
        download_address.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    writeAddress(dbResult);
                } else {
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void printAddress(ArrayList<HashMap<String, String>> dbResult){
        for (int i =0; i<dbResult.size();i++){
            String [] strarr= new String [9];
            strarr[0] = dbResult.get(i).get("id_address");
            strarr[1] = dbResult.get(i).get("country");
            strarr[2] = dbResult.get(i).get("province");
            strarr[3] = dbResult.get(i).get("city");
            strarr[4] = dbResult.get(i).get("town");
            strarr[5] = dbResult.get(i).get("road");
            strarr[6] = dbResult.get(i).get("postcode");
            strarr[7] = dbResult.get(i).get("latitude");
            strarr[8] = dbResult.get(i).get("longitude");
            adapter.addItem(strarr);
            lvAddress.setAdapter(adapter);
            lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // get item
                    ItemAddressView item = (ItemAddressView) parent.getItemAtPosition(position) ;
                    writeAddress(item);
                }
            }) ;
        }
        setListViewHeightBasedOnChildren(lvAddress);
    }
    public void writeAddress(ItemAddressView item) {
        etCountry.setText(item.getCountry());
        etProvince.setText(item.getProvince());
        etCity.setText(item.getCity());
        etTown.setText(item.getTown());
        etRoad.setText(item.getRoad());
        etPostCode.setText(item.getPostcode());

        ((RegisterActivity)getActivity()).id_address = item.getId_address();
        overwrite = true;
    }
    public void writeAddress(ArrayList<HashMap<String, String>> dbResult){
        etCountry.setText(dbResult.get(0).get("country"));
        etProvince.setText(dbResult.get(0).get("province"));
        etCity.setText(dbResult.get(0).get("city"));
        etTown.setText(dbResult.get(0).get("town"));
        etRoad.setText(dbResult.get(0).get("road"));
        etPostCode.setText(dbResult.get(0).get("postcode"));
    }
    public void uploadAddress(){
        if(overwrite==true)
            return;

        HandlerDB download_business = new HandlerDB("upload_address.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_user", ((RegisterActivity)getActivity()).id_user);
        input.put("country", etCountry.getText().toString());
        input.put("province", etProvince.getText().toString());
        input.put("city", etCity.getText().toString());
        input.put("town", etTown.getText().toString());
        input.put("road", etRoad.getText().toString());
        input.put("postcode", etPostCode.getText().toString());
        download_business.execute(input);
        download_business.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    ((RegisterActivity)getActivity()).id_address =  dbResult.get(0).get("id_last");
                    downloadAddress();
                } else {
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((RegisterActivity)getActivity()).setButtonVisible();
        ((RegisterActivity)getActivity()).country = etCountry.getText().toString();
        ((RegisterActivity)getActivity()).province = etProvince.getText().toString();
        ((RegisterActivity)getActivity()).city = etCity.getText().toString();
        ((RegisterActivity)getActivity()).town = etTown.getText().toString();
        ((RegisterActivity)getActivity()).road =  etRoad.getText().toString();
        ((RegisterActivity)getActivity()).postcode = etPostCode.getText().toString();
    }
}