package com.competitionba.humiditysensor;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
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
        mSensorRecyclerView = (RecyclerView) view.findViewById(R.id.sensor_recycler_view);
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
    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private TextView mLastUpdateTimeTextView;
        private Sensor mSensor;
        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_sensor, parent, false));
            itemView.setOnClickListener(this);
            mNameTextView=(TextView)itemView.findViewById(R.id.sensor_title);
            mLastUpdateTimeTextView=(TextView)itemView.findViewById(R.id.sensor_lastuplatedate);
        }
        public void bind(Sensor sensor){
            mSensor = sensor;
            mNameTextView.setText(mSensor.getNickname());
            Date date = sensor.getLastUpdateTime();
            CharSequence cs = "EEEE, MMMM dd, yyyy"; //星期，月份 几号，几年   例如：星期一，十一月 5， 2018
            CharSequence re = DateFormat.format(cs,date);
            String dateFormat = re.toString();
            mLastUpdateTimeTextView.setText(dateFormat);
        }
        @Override
        public void onClick(View view) {
//            Intent intent = new Intent(getActivity(), SensorActivity.class);
//            startActivity(intent);
            builder = new AlertDialog.Builder(getContext());
            alert = builder.setTitle("详细信息：")
                    .setMessage("传感器昵称：")
                    .setPositiveButton("确定",null)
                    .create();
            alert.show();
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private List<Sensor> mSensors;
        public SensorAdapter(List<Sensor> sensors){
            mSensors = sensors;
        }
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SensorHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = mSensors.get(position);
            holder.bind(sensor);
        }
        @Override
        public int getItemCount() {
            return mSensors.size();
        }
    }

}
