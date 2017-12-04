package org.androidtown.MajorBasicProject2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterAddressListView extends BaseAdapter {

    TextView tvAddress, tvLatitude, tvLongitude;
    ItemAddressView taskViewItem;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ItemAddressView> listItemViewTask = new ArrayList<>() ;

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listItemViewTask.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "item_taskview" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_addressview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        tvAddress = convertView.findViewById(R.id.tvAddress) ;
        tvLatitude = convertView.findViewById(R.id.tvLatitude) ;
        tvLongitude = convertView.findViewById(R.id.tvLongitude) ;

        // Data Set(listItemViewTask)에서 position에 위치한 데이터 참조 획득
        taskViewItem = listItemViewTask.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tvAddress.setText(taskViewItem.getAddress());
        tvLatitude.setText(taskViewItem.getLatitude());
        tvLongitude.setText(taskViewItem.getLongitude());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listItemViewTask.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String ... str) {
        ItemAddressView item = new ItemAddressView();

        item.setId_address(str[0]);
        item.setCountry(str[1]);
        item.setProvince(str[2]);
        item.setCity(str[3]);
        item.setTown(str[4]);
        item.setRoad(str[5]);
        item.setPostcode(str[6]);
        item.setLatitude(str[7]);
        item.setLongitude(str[8]);

        listItemViewTask.add(item);
    }
    public void clearAll(){
        int size = listItemViewTask.size();
        for(int i=0; i<size;i++){
            listItemViewTask.remove(0);
        }
    }
}
