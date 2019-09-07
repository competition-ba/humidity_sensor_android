package com.competitionba.humiditysensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class SensorListFragment extends Fragment {
    private RecyclerView mSensorRecyclerView;
    private SensorAdapter mAdapter;
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
            Intent intent = new Intent(getActivity(), SensorActivity.class);
            startActivity(intent);
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
