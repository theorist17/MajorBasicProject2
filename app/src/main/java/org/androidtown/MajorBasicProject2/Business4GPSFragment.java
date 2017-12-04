package org.androidtown.MajorBasicProject2;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Business4GPSFragment extends Fragment implements OnMapReadyCallback {

    Location location;
    GoogleMap googleMap;
    MapView mapView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rb4_gps, container, false);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return view;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        selectAddress();
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
                    HashMap<String, String> selected = dbResult.get(0);
                    String address = selected.get("province") + " " + selected.get("city") + " " + selected.get("town") + " " + selected.get("road");
                    location = findGeoPoint(getActivity(), address);

                    //위도 경도 정보 등록
                    uploadLocation(selected.get("id_address"), location.getLatitude(), location.getLongitude());

                    //마커 등록
                    LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(gps);
                    markerOptions.title("사업장 위치");
                    markerOptions.snippet(((RegisterActivity)getActivity()).city + " " + ((RegisterActivity)getActivity()).town);
                    googleMap.addMarker(markerOptions);

                    //지도 이동
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(gps));
                } else {
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void uploadLocation(String id_address, Double lat, Double lon){
        HandlerDB upload_location = new HandlerDB("upload_location.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("id_address", id_address);
        input.put("latitude", lat.toString());
        input.put("longitude", lon.toString());
        upload_location.execute(input);
        upload_location.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(log.equals("success")){
                    //Toast.makeText(getActivity(), "위도와 경도값을 반영했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //주소로부터 위치정보 취득
    public static Location findGeoPoint(Context mcontext, String address) {
        Location loc = new Location("");
        Geocoder coder = new Geocoder(mcontext);
        List<Address> addr = null;// 한 좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 설정

        try {
            addr = coder.getFromLocationName(address, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }// 몇개 까지의 주소를 원하는지 지정 1~5개 정도가 적당
        if (addr != null) {
            for (int i = 0; i < addr.size(); i++) {
                Address lating = addr.get(i);
                double lat = lating.getLatitude(); // 위도가져오기
                double lon = lating.getLongitude(); // 경도가져오기
                loc.setLatitude(lat);
                loc.setLongitude(lon);
            }
        }
        return loc;
    }
    //위도,경도로 주소구하기
    public static String getAddress(Context mContext,double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List <Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;

                }
            }
        } catch (IOException e) {
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return nowAddress;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((RegisterActivity)getActivity()).latitude = location.getLatitude();
        ((RegisterActivity)getActivity()).longitude = location.getLongitude();
    }

}
