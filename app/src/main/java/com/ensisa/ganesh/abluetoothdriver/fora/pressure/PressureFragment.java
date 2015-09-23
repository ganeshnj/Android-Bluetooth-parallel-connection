package com.ensisa.ganesh.abluetoothdriver.fora.pressure;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ensisa.ganesh.abluetoothdriver.FragmentCommunicator;
import com.ensisa.ganesh.abluetoothdriver.MainActivity;
import com.ensisa.ganesh.abluetoothdriver.R;
import com.ensisa.ganesh.abluetoothdriver.common.fragments.BluetoothFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PressureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PressureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PressureFragment extends BluetoothFragment implements FragmentCommunicator, View.OnClickListener {

    private String mDeviceName;

    public String getDeviceAddress() {
        return mDeviceAddress;
    }

    private String mDeviceAddress;

    private OnFragmentInteractionListener mListener;

    TextView mTextViewModel;
    TextView mTextViewSerial1;
    TextView mTextViewSerial2;
    TextView mTextViewClock;

    TextView mTextViewCountUser1;
    TextView mTextViewCountUser2;
    TextView mTextViewCountUser3;
    TextView mTextViewCountUser4;

    TextView mTextViewCurrentUser;
    TextView mTextViewMeanPressure;

    Button mButtonGetUser1Data;
    Button mButtonGetUser2Data;
    Button mButtonGetUser3Data;
    Button mButtonGetUser4Data;

    List<Pressure> mPressures;
    PressureAdapter mPressureAdapter;
    ListView mListViewPressure;

    PressureSession session;
    FragmentCommunicator mFragmentCommunicator;


    public static PressureFragment newInstance(String name, String address) {
        PressureFragment fragment = new PressureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    public PressureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mDeviceName = getArguments().getString(ARG_NAME);
            mDeviceAddress = getArguments().getString(ARG_ADDRESS);
        }
        session = new PressureSession(((MainActivity) getActivity()).mBluetoothService, mDeviceAddress);

        mPressures = new ArrayList<Pressure>();
        mPressureAdapter = new PressureAdapter(getActivity(), mPressures);

        session.send(PressureMessageID.READ_COUNT, 1);
        session.send(PressureMessageID.READ_COUNT, 2);
        session.send(PressureMessageID.READ_COUNT, 3);
        session.send(PressureMessageID.READ_COUNT, 4);

        mFragmentCommunicator = this;
        ((MainActivity) getActivity()).mFragmentCommunicators.put(mDeviceAddress, mFragmentCommunicator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pressure, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mTextViewModel = (TextView) view.findViewById(R.id.text_view_model_no);
//        mTextViewSerial1 = (TextView) view.findViewById(R.id.text_view_serial_1);
//        mTextViewSerial2 = (TextView) view.findViewById(R.id.text_view_serial_2);
//        mTextViewClock = (TextView) view.findViewById(R.id.text_view_clock);

        mTextViewCountUser1 = (TextView) view.findViewById(R.id.text_view_user_1_memory_count);
        mTextViewCountUser2 = (TextView) view.findViewById(R.id.text_view_user_2_memory_count);
        mTextViewCountUser3 = (TextView) view.findViewById(R.id.text_view_user_3_memory_count);
        mTextViewCountUser4 = (TextView) view.findViewById(R.id.text_view_user_4_memory_count);

        mTextViewCurrentUser = (TextView) view.findViewById(R.id.text_view_current_user);
        mTextViewMeanPressure = (TextView) view.findViewById(R.id.text_view_mean_pressure);

        mButtonGetUser1Data = (Button) view.findViewById(R.id.button_get_user_1_data);
        mButtonGetUser2Data = (Button) view.findViewById(R.id.button_get_user_2_data);
        mButtonGetUser3Data = (Button) view.findViewById(R.id.button_get_user_3_data);
        mButtonGetUser4Data = (Button) view.findViewById(R.id.button_get_user_4_data);

        mButtonGetUser1Data.setOnClickListener(this);
        mButtonGetUser2Data.setOnClickListener(this);
        mButtonGetUser3Data.setOnClickListener(this);
        mButtonGetUser4Data.setOnClickListener(this);

        mListViewPressure = (ListView) view.findViewById(R.id.list_view_pressure);
        mListViewPressure.setAdapter(mPressureAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) getActivity()).fragmentCommunicator2 = this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_pressure, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_write_clock)
        {
            session.send(PressureMessageID.WRITE_CLOCK);
            return true;
        }

        else if(id == R.id.action_turn_off)
        {
            session.send(PressureMessageID.TURN_OFF);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void passDataToFragment(byte[] buffer, int length, String address) {
        int user = 1;
        if(buffer != null && address == mDeviceAddress){
            if(buffer[1] == PressureMessageID.READ_COUNT.getValue()){
                user = session.lastCommands.get(0).getFrame()[2];
            }
            else if (buffer[1] == PressureMessageID.READ_DATA_1.getValue()
                    || buffer[1] == PressureMessageID.READ_DATA_2.getValue()) {
                user = session.lastCommands.get(0).getFrame()[5];
            }

            if(session.checkSanity(buffer, length)){
                byte cmd = buffer[1];

                if(cmd == PressureMessageID.READ_CLOCK.getValue()) {
                    PressureClock clock = PressureAnalyzer.analyseReadClock(buffer);
                    mTextViewClock.setText(clock.getDate() + " " + clock.getTime());
                }
                else if(cmd == PressureMessageID.READ_MODEL.getValue()){
                    mTextViewModel.setText(String.valueOf(PressureAnalyzer.analyseReadModel(buffer)));
                }
                else if(cmd == PressureMessageID.READ_DATA_1.getValue()){
                    Pressure pressure = new Pressure();
                    PressureClock pressureClock = PressureAnalyzer.analyzeReadData1(buffer);
                    pressure.setClock(pressureClock);
                    mPressures.add(pressure);
                }
                else if(cmd == PressureMessageID.READ_DATA_2.getValue()){
                    Pressure pressure = PressureAnalyzer.analyseReadData2(buffer);
                    pressure.setClock(mPressures.get(mPressures.size() - 1).getClock());
                    mPressures.remove(mPressures.size() - 1);
                    mPressures.add(pressure);
                    mPressureAdapter.notifyDataSetChanged();
                    int sys=0, dia=0, i=0;
                    for(Pressure p: mPressures){
                        if(p.getClock().getbType() == PressureClock.BloodMeasureType.BLOODPRESSURE){
                            sys  += p.getSystolicMeasure();
                            dia += p.getDiastolicMeasure();
                            i++;
                        }

                    }
                    mTextViewMeanPressure.setText(String.valueOf(sys/i + "/"+ dia/i));
                }
                else if(cmd == PressureMessageID.READ_SERIAL_1.getValue()){
                    mTextViewSerial1.setText(String.valueOf(PressureAnalyzer.analyseReadSerial1(buffer)));
                }
                else if(cmd == PressureMessageID.READ_SERIAL_2.getValue()){
                    mTextViewSerial2.setText(String.valueOf(PressureAnalyzer.analyseReadSerial2(buffer)));
                }
                else if(cmd == PressureMessageID.READ_COUNT.getValue()){
                    if(user == 1){
                        mTextViewCountUser1.setText(String.valueOf(PressureAnalyzer.analyseReadCount(buffer)));
                    }
                    else if(user == 2){
                        mTextViewCountUser2.setText(String.valueOf(PressureAnalyzer.analyseReadCount(buffer)));
                    }
                    else if(user == 3){
                        mTextViewCountUser3.setText(String.valueOf(PressureAnalyzer.analyseReadCount(buffer)));
                    }
                    else if(user == 4){
                        mTextViewCountUser4.setText(String.valueOf(PressureAnalyzer.analyseReadCount(buffer)));
                    }
                }
                else if(cmd == PressureMessageID.TURN_OFF.getValue()){

                }
            }
            if(!session.lastCommands.isEmpty()){
                session.send();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int n;
        switch(v.getId()){
            case R.id.button_get_user_1_data:
                n =  Integer.parseInt(mTextViewCountUser1.getText().toString());
                for(int i=0; i<n; i++){
                    session.send(PressureMessageID.READ_DATA_1, i, 1);
                    session.send(PressureMessageID.READ_DATA_2, i, 1);
                }
                mTextViewCurrentUser.setText("User 1");
                mPressures.removeAll(mPressures);
                mPressureAdapter.notifyDataSetChanged();
                break;
            case R.id.button_get_user_2_data:
                n =  Integer.parseInt(mTextViewCountUser2.getText().toString());
                for(int i=0; i<n; i++){
                    session.send(PressureMessageID.READ_DATA_1, i, 2);
                    session.send(PressureMessageID.READ_DATA_2, i, 2);
                }
                mTextViewCurrentUser.setText("User 2");
                mPressures.removeAll(mPressures);
                mPressureAdapter.notifyDataSetChanged();
                break;
            case R.id.button_get_user_3_data:
                n =  Integer.parseInt(mTextViewCountUser3.getText().toString());
                for(int i=0; i<n; i++){
                    session.send(PressureMessageID.READ_DATA_1, i, 3);
                    session.send(PressureMessageID.READ_DATA_2, i, 3);
                }
                mTextViewCurrentUser.setText("User 3");
                mPressures.removeAll(mPressures);
                mPressureAdapter.notifyDataSetChanged();
                break;
            case R.id.button_get_user_4_data:
                n =  Integer.parseInt(mTextViewCountUser4.getText().toString());
                for(int i=0; i<n; i++){
                    session.send(PressureMessageID.READ_DATA_1, i, 4);
                    session.send(PressureMessageID.READ_DATA_2, i, 4);
                }
                mTextViewCurrentUser.setText("User 4");
                mPressures.removeAll(mPressures);
                mPressureAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public class PressureAdapter extends ArrayAdapter<Pressure> {

        public PressureAdapter(Context context, List<Pressure> pressures) {
            super(context, 0, pressures);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Pressure pressure = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.pressure_list_item, parent, false);
            }

            // Lookup view for data population
            TextView textViewDay = (TextView) convertView.findViewById(R.id.text_view_day);
            TextView textViewTime = (TextView) convertView.findViewById(R.id.text_view_time);
            TextView textViewBP = (TextView) convertView.findViewById(R.id.text_view_bp);
            TextView textViewPulse = (TextView) convertView.findViewById(R.id.text_view_pulse);
            TextView textViewGlucose = (TextView) convertView.findViewById(R.id.text_view_glucose);

            // Populate the data into the template view using the data object

            textViewDay.setText(pressure.getDate());
            textViewTime.setText(pressure.getTime());
            if(pressure.getClock().getbType() == PressureClock.BloodMeasureType.BLOODPRESSURE){
                textViewBP.setText(String.valueOf((int)pressure.getSystolicMeasure())+"/"+String.valueOf((int)pressure.getDiastolicMeasure()));
                textViewPulse.setText(String.valueOf((int)pressure.getPulse()) + " (" + pressure.getClock().getHeartBeatType() + ")");
                textViewGlucose.setText("-");
            }
            else if(pressure.getClock().getbType() == PressureClock.BloodMeasureType.BLOODGLUCOSE){
                textViewBP.setText("-");
                textViewPulse.setText("-");
                textViewGlucose.setText(String.valueOf((int)pressure.getGlucoseMeasure()) + " (" + pressure.getGlucoseType() + ")");
            }

            // Return the completed view to render on screen
            return convertView;
        }
    }
}
