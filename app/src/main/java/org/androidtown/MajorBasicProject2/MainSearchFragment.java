package org.androidtown.MajorBasicProject2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;


public class MainSearchFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, View.OnTouchListener {
    public static final int MY_PERMISSION_LOCATION = 4;

    private int m_nPreTouchPosX = 0;
    GoogleMap googleMap;
    ArrayList<HashMap<String, String>> questList = new ArrayList<>();
    ArrayList<Marker> markerList = new ArrayList<>();

    //custom marker
    View marker_root_view;
    TextView tv_marker;
    Marker selectedMarker;
    boolean isDownloadTrue = true;
    //layout
    Button btnList, btnFind;
    EditText etFind;
    MapView mapView;
    ViewFlipper viewFlipper;
    TextView tvGps;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (container == null)
            return null;

        FrameLayout rootView = (FrameLayout) inflater.inflate(R.layout.fragment_m1_search, container, false);
        btnFind = rootView.findViewById(R.id.btnFind);
        btnList = rootView.findViewById(R.id.btnList);
        etFind = rootView.findViewById(R.id.etFind);
        mapView = rootView.findViewById(R.id.map);
        viewFlipper = rootView.findViewById(R.id.viewFlipper);
        tvGps = rootView.findViewById(R.id.tvGps);

        viewFlipper.setOnTouchListener(this);

        //지도 시작
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return rootView;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnCameraIdleListener(this);
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.537523, 126.96558),10));

        marker_root_view = LayoutInflater.from(getActivity()).inflate(R.layout.marker_layout, null);
        tv_marker = marker_root_view.findViewById(R.id.tv_marker);

        //현재 위치 찾기
        if(!startLocationService())
            Toast.makeText(getActivity(), "error : GPS Location Listener를 호출하는 데 실패", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        //지도 이동
        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        isDownloadTrue = false;
        googleMap.animateCamera(center);
        changeSelectedMarker(marker);

        //뷰어 이동
        int index = Integer.parseInt(marker.getSnippet());
        viewFlipper.setDisplayedChild(index);

        return true;
    }
    public void onMapClick(LatLng latLng) {
        changeSelectedMarker(null);
    }
    @Override
    public void onCameraIdle() {
        LatLngBounds curScreen = googleMap.getProjection().getVisibleRegion().latLngBounds;
        LatLng ne = curScreen.northeast;
        LatLng sw = curScreen.southwest;

        if(isDownloadTrue)
            questLocation(ne, sw);
        else
            isDownloadTrue = true;
    }
    public boolean onTouch(View v, MotionEvent event){
        switch (v.getId()){
            case R.id.viewFlipper:
                //누른 곳의 x위치 저장
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    m_nPreTouchPosX = (int)event.getX();
                }
                // 땐 곳의 x위치를 보고
                if (event.getAction() == MotionEvent.ACTION_UP){
                    int nTouchPosX = (int)event.getX();

                    //왼쪽으로 10이상 이동한 경우 다음 화면
                    if (nTouchPosX < m_nPreTouchPosX - 10){
                        MoveNextView();
                    }
                    //오른쪽으로 10이상 이동한 경우 이전화면
                    else if (nTouchPosX > m_nPreTouchPosX + 10){
                        MovePreviousView();
                        //어느쪽으로도 10이상을 이동하지 않은 경우에는 클릭한 것임
                    } else {
                        Intent intent = new Intent(getActivity(), PageQuestActivity.class);
                        intent.putExtra("id_user", ((MainActivity)getActivity()).id_user);
                        int index = viewFlipper.getDisplayedChild();
                        intent.putExtra("id_quest", questList.get(index).get("id_quest"));
                        startActivity(intent);
                    }
                    m_nPreTouchPosX = nTouchPosX;
                }
                break;
        }
        return true;
    }
    public boolean startLocationService(){
        if(!checkLocationPermission())
            return false;

        LocationManager manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        logIsProviderEnabled(manager);
        LocationListener locationListener = new LocationListener() {
            /**
             . 위치 정보를 가져올 수 있는 메소드입니다.
             . 위치 이동이나 시간 경과 등으로 인해 호출됩니다.
             . 최신 위치는 location 파라메터가 가지고 있습니다.
             . 최신 위치를 가져오려면, location 파라메터를 이용하시면 됩니다.
            **/
            public void onLocationChanged(Location location) {
                //여기서 위치값이 갱신되면 이벤트가 발생한다.

                Location lastKnownLocation = getLastKnownLocation(location);

                //변화한 위치로 데이터 최신화하기
                lastKnownLocation = location;

                //변화한 위치로 지도 최신화하기
                relocateMap(lastKnownLocation);

                //변화한 위치로 텍스트뷰 최신화하기
                tvGps.setText("위치정보 : " + location.getProvider() + "\n위도 : " + location.getLatitude() + "\n경도 : " + location.getLongitude() + "\n고도 : " + location.getAltitude() + "\n정확도 : "  + location.getAccuracy());
            }
            /**
             . 위치 공급자가 사용 불가능해질(disabled) 때 호출 됩니다.
             . 단순히 위치 정보를 구한다면, 코드를 작성하실 필요는 없습니다.
             **/
            public void onProviderDisabled(String provider) {
                Log.d("test", "onProviderDisabled, provider:" + provider);
            }
            /**
             . 위치 공급자가 사용 가능해질(enabled) 때 호출 됩니다.
             . 단순히 위치 정보를 구한다면, 코드를 작성하실 필요는 없습니다.
             **/
            public void onProviderEnabled(String provider) {
                Log.d("test", "onProviderEnabled, provider:" + provider);
            }
            /**
             . 위치 공급자의 상태가 바뀔 때 호출 됩니다.
             . 단순히 위치 정보를 구한다면, 코드를 작성하실 필요는 없습니다.
             **/
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
            }
        };
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener); //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다. Register the listener with the Location Manager to receive location updates
        //manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);//Network 위치제공자에 의한 위치변화 (Network 위치는 Gps에 비해 정확도가 많이 떨어진다. 에뮬레이터에서 한국을 위도와 경도값을 설정해도 미국이 나올 수 있다.)

        //업데이트를 중지하는 법
        //manager.removeUpdates(locationListener);

        return true;
    }
    private boolean checkLocationPermission(){
        // 위치정보 접근 권한 체크
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우

            //사용자의에 의한 재요청인지 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_LOCATION);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_LOCATION);
                //최초로 권한을 요청하는 경우
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_LOCATION);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_LOCATION);
            }

            return false;
        }
        //사용권한이 있음을 확인한 경우 아무것도 하지 않음
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case MY_PERMISSION_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //사용자가 권한 동의 버튼 클릭
                    Toast.makeText(getActivity(), "권한 사용에 동의하셨습니다.", Toast.LENGTH_LONG).show();
                } else {
                    //사용자가 권한 동의를 안함
                    Toast.makeText(getActivity(), "권한 사용에 동의해 주셔야 이용이 가능합니다.", Toast.LENGTH_LONG).show();
                }
        }
    }
    public void logIsProviderEnabled(LocationManager manager){
        List<String> list = manager.getAllProviders(); // 위치제공자 모두 가져오기
        String str = ""; // 출력할 문자열
        for (int i = 0; i < list.size(); i++) {
            str += "위치제공자 : " + list.get(i) + ", 사용가능여부 -"
                    + manager.isProviderEnabled(list.get(i)) +"\n";
        }
        Log.d("Location", str);

        // 패시브 프로바이더 사용가능여부
        boolean isPassiveEnabled = manager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
        Log.d("Location", "isPassiveEnabled="+ isPassiveEnabled);
        // GPS 프로바이더 사용가능여부
        boolean isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d("Location", "isGPSEnabled="+ isGPSEnabled);
        // 네트워크 프로바이더 사용가능여부
        boolean isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d("Location", "isNetworkEnabled="+ isNetworkEnabled);
    }
    public Location getLastKnownLocation(Location location) {
        //권한 검사
        checkLocationPermission();

        LocationManager manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        String locationProvider;
        if(location == null)
            locationProvider = LocationManager.GPS_PROVIDER;
        else
            locationProvider = location.getProvider();
        Location lastKnownLocation = manager.getLastKnownLocation(locationProvider);
        double lon = lastKnownLocation.getLongitude();
        double lat = lastKnownLocation.getLatitude();
        Log.d("Location", "Previously Last Known Location -> longtitude=" + lon + ", latitude=" + lat);

        return lastKnownLocation;
    }
    public void relocateMap(Location loc){
        LatLng gps = new LatLng(loc.getLatitude(), loc.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(gps));
    }
    public void relocateMap(LatLng gps){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(gps));
    }
    private Marker addMarker(Marker marker, boolean isSelectedMarker) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        int price = parseInt(marker.getTitle());
        int index = parseInt(marker.getSnippet());
        ItemMarker temp = new ItemMarker(lat, lon, price, index);
        return addMarker(temp, isSelectedMarker);

    }
    private Marker addMarker(ItemMarker itemMarker, boolean isSelectedMarker) {


        LatLng position = new LatLng(itemMarker.getLat(), itemMarker.getLon());
        int price = itemMarker.getPrice();
        int index = itemMarker.getIndex();
        String formatted = NumberFormat.getCurrencyInstance().format((price));

        tv_marker.setText(formatted);

        if (isSelectedMarker) {
            tv_marker.setBackgroundResource(R.drawable.ic_marker_phone_blue);
            tv_marker.setTextColor(Color.WHITE);
        } else {
            tv_marker.setBackgroundResource(R.drawable.ic_marker_phone);
            tv_marker.setTextColor(Color.BLACK);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(Integer.toString(price));
        markerOptions.snippet(Integer.toString(index));
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker_root_view)));


        return googleMap.addMarker(markerOptions);

    }

    // View를 Bitmap으로 변환
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    private void changeSelectedMarker(Marker marker) {
        // 선택했던 마커 되돌리기
        if (selectedMarker != null) {
            switchMarkerTitle(selectedMarker);
            markerList.add(addMarker(selectedMarker, false));
            markerList.remove(selectedMarker);
            selectedMarker.remove();
        }

        // 선택한 마커 표시
        if (marker != null) {
            switchMarkerTitle(marker);
            selectedMarker = addMarker(marker, true);
            markerList.add(selectedMarker);
            markerList.remove(marker);
            marker.remove();
        }
    }
    private void switchMarkerTitle(Marker marker){
        String temp = marker.getTitle().substring(0);
        marker.setTitle(marker.getSnippet());
        marker.setSnippet(temp);
    }
    private void MoveNextView()
    {
        int index = viewFlipper.getDisplayedChild();
        if(index==viewFlipper.getChildCount()-1) return;

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),	R.anim.appear_from_right));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.disappear_to_left));
        viewFlipper.showNext();

        //지도
        index = viewFlipper.getDisplayedChild();
        isDownloadTrue = false;
        //relocateMap(new LatLng(Double.valueOf(questList.get(index).get("latitude")).doubleValue(), Double.valueOf(questList.get(index).get("longitude"))));
        changeSelectedMarker(markerList.get(index));
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(questList.get(index).get("latitude")).doubleValue(), Double.valueOf(questList.get(index).get("longitude"))));
        googleMap.animateCamera(center);
    }
    private void MovePreviousView()
    {
        int index = viewFlipper.getDisplayedChild();
        if(index==0) return;

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),	R.anim.appear_from_left));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.disappear_to_right));
        viewFlipper.showPrevious();

        //지도
        index = viewFlipper.getDisplayedChild();
        isDownloadTrue = false;
        //relocateMap(new LatLng(Double.valueOf(questList.get(index).get("latitude")).doubleValue(), Double.valueOf(questList.get(index).get("longitude"))));
        changeSelectedMarker(markerList.get(index));
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(questList.get(index).get("latitude")).doubleValue(), Double.valueOf(questList.get(index).get("longitude"))));
        googleMap.animateCamera(center);
    }
    public void questLocation(LatLng ne, LatLng sw){
        //데이터 로딩
        HandlerDB quest_near = new HandlerDB("quest_location.php");
        HashMap<String,String> input = new HashMap<>();
        input.put("north", ne.latitude+"");
        input.put("south", sw.latitude+"");
        input.put("east", ne.longitude+"");
        input.put("west", sw.longitude+"");
        quest_near.execute(input);
        quest_near.setDbResponse(new HandlerDB.DBResponse() {
            @Override
            public void onResepones(ArrayList<HashMap<String, String>> dbResult, String log) {
                if(!log.equals("json"))
                    Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();

                viewFlipper.removeAllViews();
                questList.clear();
                markerList.clear();
                googleMap.clear();

                LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i< dbResult.size(); i++){
                    // inflate child
                    View item = inflator.inflate(R.layout.item_questbar, null);

                    // initialize review UI
                    TextView tvfield = item.findViewById(R.id.orderbar_item_field);
                    TextView tvType = item.findViewById(R.id.orderbar_item_type);
                    TextView tvValue = item.findViewById(R.id.orderbar_item_value);

                    TextView tvTitle = item.findViewById(R.id.orderbar_item_title);
                    LinearLayout llTaskLayout = item.findViewById(R.id.orderbar_item_task_layout);
                    TextView tvPrice = item.findViewById(R.id.orderbar_item_price);

                    TextView tvCell = item.findViewById(R.id.orderbar_item_cell);
                    TextView tvAddress = item.findViewById(R.id.orderbar_item_address);

                    // set inputData
                    tvfield.setText(dbResult.get(i).get("field"));
                    tvType.setText(dbResult.get(i).get("type"));
                    //tvValue.setText(dbResult.get(i).get("type"));
                    tvPrice.setText(dbResult.get(i).get("price"));
                    tvCell.setText(dbResult.get(i).get("cell"));
                    tvTitle.setText(dbResult.get(i).get("title"));
                    tvAddress.setText(dbResult.get(i).get("address"));

                    // add child
                    viewFlipper.addView(item);
                    HashMap<String, String> quest = new HashMap<String, String>();
                    quest.put("index", i+"");
                    quest.put("id_quest", dbResult.get(i).get("id_quest"));
                    quest.put("latitude", dbResult.get(i).get("latitude"));
                    quest.put("longitude", dbResult.get(i).get("longitude"));
                    questList.add(quest);


                    //add custom marker
                    ItemMarker itemMarker = new ItemMarker(Double.valueOf(dbResult.get(i).get("latitude")),  Double.valueOf(dbResult.get(i).get("longitude")), Integer.valueOf(dbResult.get(i).get("price")), i);
                    markerList.add(addMarker(itemMarker, false));
                }
            }
        });
    }
}
