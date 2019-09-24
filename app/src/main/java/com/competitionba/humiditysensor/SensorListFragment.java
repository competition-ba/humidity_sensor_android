package com.competitionba.humiditysensor;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SensorListFragment extends Fragment {
    private RecyclerView mSensorRecyclerView;
    private SensorAdapter mAdapter;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    static String username;
    final OkHttpClient client = new OkHttpClient();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String result = (String)msg.obj;
            //result = "[{\"data\":28,\"nickname\":\"no3\",\"x\":2,\"senNo\":\"41F85FD482E2665971273F4DE6D4F52D\",\"y\":3,\"time\":\"2000-01-01 12:00:00\"}]";
            //Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
            //拆分
            SensorLab sensorLab = SensorLab.get(getActivity());
            sensorLab.updateSensor(result);
            //更新界面
            updateUI();

        }
    };
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
        //获取传感器信息
        JSONObject data = new JSONObject();
        try{
            data.put("user",username);
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        RequestBody formBody = new FormBody.Builder()
                .add("USN",data.toString())
                .build();
        final Request request = new Request.Builder()
                .url("http://cloud.fhh200000.com/Arduino/USN")
                .post(formBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response;
                try {
                    response = client.newCall(request).execute();
                    if(response!=null) {
                        if (response.isSuccessful()) {
                            mHandler.obtainMessage(1, response.body().string()).sendToTarget();
                        } else {
                            throw new IOException("Unexpected code:" + response);
                        }
                    }
                    else
                        throw new IOException("Empty response!");
                }
                catch (IOException e) {
                    mHandler.obtainMessage(1, "FATAL").sendToTarget();
                    e.printStackTrace();
                }
            }
        }).start();
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
            case R.id.new_sensor:{
                Intent intent = new Intent(getActivity(),SensorActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class SensorHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mLastUpdateTimeTextView;
        private ImageView mClearImageView;
        private ConstraintLayout mOutLayout;
        public SensorHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView=itemView.findViewById(R.id.sensor_title);
            mLastUpdateTimeTextView=itemView.findViewById(R.id.sensor_lastuplatedate);
            mClearImageView=itemView.findViewById(R.id.nh3_needclear);
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
                           .setMessage("传感器昵称："+sensor.getNickname()+
                                   "\n传感器GUID:"+sensor.getSensorGUID()+
                                   "\n湿度:"+sensor.getHumidity()+
                                   "\n最后更新时间："+sensor.getLastUpdateTime()+
                                   "\n氨气相对浓度："+sensor.getNH3())
                           .setPositiveButton("确定",null)
                           .create();
                   alert.show();
                }
            });
            if (sensor.getNH3()<512){
                holder.mClearImageView.setVisibility(View.INVISIBLE);
            }
        }
        @Override
        public int getItemCount() {
            return mSensors.size();
        }
    }

}
