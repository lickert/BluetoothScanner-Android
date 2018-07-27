package person.zhc.bluetoothscan.adapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import person.zhc.bluetoothscan.R;

/**
 * Created by huanchen on 2018/7/27.
 */

public class BluetoothListAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> mlistData;
    private Context mContext;



    public BluetoothListAdapter(Context context, ArrayList<BluetoothDevice> data){
        mContext = context;
        this.mlistData = data;
    }

    @Override
    public int getCount() {
        return mlistData.size();
    }

    @Override
    public Object getItem(int i) {
        return mlistData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BluetoothDevice data = mlistData.get(i);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.bluetooth_list_item,null);
        Holder holder = new Holder();
        holder.mTvMac = (TextView)view.findViewById(R.id.tv_bluetooth_list_mac);
        holder.mTvName = (TextView)view.findViewById(R.id.tv_bluetooth_list_name);
        holder.mTvUuid = (TextView)view.findViewById(R.id.tv_bluetooth_list_uuid);
        holder.mTvIndex = (TextView)view.findViewById(R.id.tv_bluetooth_list_index);

        holder.mTvMac.setText(data.getAddress());
        holder.mTvName.setText(data.getName());
        holder.mTvIndex.setText( i+1+"");
        ParcelUuid[] parcelUuids = data.getUuids();
        if(parcelUuids!=null){
            holder.mTvUuid.setText(parcelUuids[0].getUuid().toString());
        }

        return view;
    }

    public static class Holder{
        private TextView mTvMac;
        private TextView mTvName;
        private TextView mTvUuid;
        private TextView mTvIndex;
    }
}
