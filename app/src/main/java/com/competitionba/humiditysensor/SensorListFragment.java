package com.competitionba.humiditysensor;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SensorListFragment extends Fragment {
    private RecyclerView mSensorRecyclerView;
    private SensorAdapter mAdapter;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_list, container, false);
        mSensorRecyclerView = view.findViewById(R.id.sensor_recycler_view);
        mSensorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }
    private void updateUI(){
        SensorLab sensorLab = SensorLab.get(getActivity());
        List<Sensor> sensors = sensorLab.getSensors();
        mAdapter = new SensorAdapter(sensors);
        mSensorRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_sensor_list, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_sensor:
                startActivity(new Intent(getActivity(),SensorActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class SensorHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mLastUpdateTimeTextView;
        private ConstraintLayout mOutLayout;
        public SensorHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView=itemView.findViewById(R.id.sensor_title);
            mLastUpdateTimeTextView=itemView.findViewById(R.id.sensor_lastuplatedate);
            mOutLayout =  itemView.findViewById(R.id.out);
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private List<Sensor> mSensors;
        public SensorAdapter(List<Sensor> sensors){
            mSensors = sensors;
        }
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_sensor, parent, false);
            final SensorHolder holder=new SensorHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            final Sensor sensor = mSensors.get(position);
            holder.mNameTextView.setText(sensor.getNickname());
            holder.mLastUpdateTimeTextView.setText(sensor.getLastUpdateTime().toString());
            holder.mOutLayout.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   builder = new AlertDialog.Builder(getContext());
                   alert = builder.setTitle("详细信息：")
                           .setMessage("传感器昵称："+sensor.getNickname()+"\n传感器GUID"+sensor.getSensorGUID()+"\n湿度"+sensor.getHumidity())
                           .setPositiveButton("确定",null)
                           .create();
                   alert.show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return mSensors.size();
        }
    }

}
