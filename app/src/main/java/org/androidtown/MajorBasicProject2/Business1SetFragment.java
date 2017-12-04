package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class Business1SetFragment extends Fragment {
    ListView lvBusiness;
    ArrayList<String> id_business = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rb1_set, container, false);

        lvBusiness = view.findViewById(R.id.lvBusiness);
        downloadBusiness();
        return view;
    }
    public void downloadBusiness(){
        HandlerDB download_business = new HandlerDB("download_business.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_user", ((RegisterActivity)getActivity()).id_user);
        download_business.execute(input);
        download_business.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")){
                    printBusiness(dbResult);
                } else {
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void printBusiness(ArrayList<HashMap<String, String>> dbResult){
        String[] item_listview = new String[dbResult.size()];

        for (int i =0; i<dbResult.size();i++){
            id_business.add(dbResult.get(i).get("id_business"));
            String str = dbResult.get(i).get("signature") + " / " + dbResult.get(i).get("field_name") + "\n";
            str += dbResult.get(i).get("address") + " " + "(" + dbResult.get(i).get("space") + ")";
            item_listview[i] = str;
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, item_listview) ;
        lvBusiness.setAdapter(adapter) ;
        lvBusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {// get TextView's Text. String strText = (String) parent.getItemAtPosition(position) ;
                setBusiness(id_business.get(position));
            }
        }) ;
    }
    public void setBusiness(String id_business){
        HandlerDB download_business = new HandlerDB("select_business.php");
        HashMap<String,String>input = new HashMap<>();
        input.put("id_business", id_business);
        download_business.execute(input);
        download_business.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("json")) {

                    writeBusiness(dbResult);
                    Toast.makeText(getActivity(), "환경정보를 불러왔습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "환경정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return;
    }
    public void writeBusiness(ArrayList<HashMap<String, String>> dbResult){
        ((RegisterActivity)getActivity()).id_address = dbResult.get(0).get("id_address");
        ((RegisterActivity)getActivity()).id_field_busi = dbResult.get(0).get("id_field");
        ((RegisterActivity)getActivity()).id_brand_busi = dbResult.get(0).get("id_brand");
        ((RegisterActivity)getActivity()).signature = dbResult.get(0).get("signature");
        ((RegisterActivity)getActivity()).license = dbResult.get(0).get("license");
        ((RegisterActivity)getActivity()).space = dbResult.get(0).get("space");
        ((RegisterActivity)getActivity()).accesses[0] = dbResult.get(0).get("transport");
        ((RegisterActivity)getActivity()).accesses[1] = dbResult.get(0).get("subway");
        ((RegisterActivity)getActivity()).accesses[2] = dbResult.get(0).get("bus_stop");
        ((RegisterActivity)getActivity()).accesses[3] = dbResult.get(0).get("parking_lot");
        ((RegisterActivity)getActivity()).safeties[0] = dbResult.get(0).get("security");
        ((RegisterActivity)getActivity()).safeties[1] = dbResult.get(0).get("fire_extingusher");
        ((RegisterActivity)getActivity()).facilities[0] = dbResult.get(0).get("toilet");
        ((RegisterActivity)getActivity()).facilities[1] = dbResult.get(0).get("air_conditioner");
        ((RegisterActivity)getActivity()).facilities[2] = dbResult.get(0).get("heater");
        ((RegisterActivity)getActivity()).facilities[3] = dbResult.get(0).get("desk");
        ((RegisterActivity)getActivity()).facilities[4] = dbResult.get(0).get("wifi");
        ((RegisterActivity)getActivity()).facilities[5] = dbResult.get(0).get("locker");
        ((RegisterActivity)getActivity()).facilities[6] = dbResult.get(0).get("laptop");
        ((RegisterActivity)getActivity()).facilities[7] = dbResult.get(0).get("wheel_chair");
        ((RegisterActivity)getActivity()).welfares[0] = dbResult.get(0).get("breakfast");
        ((RegisterActivity)getActivity()).welfares[1] = dbResult.get(0).get("lunch");
        ((RegisterActivity)getActivity()).welfares[2] = dbResult.get(0).get("dinner");
        ((RegisterActivity)getActivity()).welfares[3] = dbResult.get(0).get("lodgment");
        ((RegisterActivity)getActivity()).welfares[4] = dbResult.get(0).get("uniform");
        ((RegisterActivity)getActivity()).welfares[5] = dbResult.get(0).get("locker_room");
        ((RegisterActivity)getActivity()).welfares[6] = dbResult.get(0).get("shower");
        ((RegisterActivity)getActivity()).welfares[7] = dbResult.get(0).get("laundry");
        ((RegisterActivity)getActivity()).welfares[8] = dbResult.get(0).get("drier");
        ((RegisterActivity)getActivity()).welfares[9] = dbResult.get(0).get("fitness_center");
        ((RegisterActivity)getActivity()).welfares[10] = dbResult.get(0).get("pregnancy");
        ((RegisterFragment)((RegisterActivity) getActivity()).fragments[0]).phase = 2;
        ((RegisterActivity)getActivity()).initFragment();
    }
}
