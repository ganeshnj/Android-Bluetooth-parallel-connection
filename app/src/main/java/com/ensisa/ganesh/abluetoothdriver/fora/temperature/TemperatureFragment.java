package com.ensisa.ganesh.abluetoothdriver.fora.temperature;

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
import android.widget.ImageButton;
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
 * {@link TemperatureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemperatureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemperatureFragment extends BluetoothFragment implements FragmentCommunicator, View.OnClickListener {
    ImageButton mButtonGetTemperature;

    TextView mTextViewTemperature;
    TextView mTextViewModel;
    TextView mTextViewSerial1;
    TextView mTextViewSerial2;
    TextView mTextViewCount;
    TextView mTextViewClock;

    List<Temperature> mTemperatures;
    TemperaturesAdapter temperaturesAdapter;
    ListView mListViewTemperatures;

    private OnFragmentInteractionListener mListener;
    FragmentCommunicator mFragmentCommunicator;

    TemperatureSession session;

    public static TemperatureFragment newInstance(String name, String address) {
        TemperatureFragment fragment = new TemperatureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    public TemperatureFragment() {
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

        mTemperatures = new ArrayList<Temperature>();
        temperaturesAdapter = new TemperaturesAdapter(getActivity(), mTemperatures);

        session = new TemperatureSession(((MainActivity) getActivity()).mBluetoothService, mDeviceAddress);
        session.init();
        //session.send(TemperatureMessageID.READ_MODEL);
        session.send(TemperatureMessageID.READ_COUNT);
        session.send(TemperatureMessageID.READ_SERIAL_1);
        session.send(TemperatureMessageID.READ_SERIAL_2);
        session.send(TemperatureMessageID.INIT.READ_CLOCK);

        for(int i=0; i<10; i++) {
            session.send(TemperatureMessageID.READ_DATA_1, i);
            session.send(TemperatureMessageID.READ_DATA_2, i);
        }

        mFragmentCommunicator = this;
        ((MainActivity) getActivity()).mFragmentCommunicators.put(mDeviceAddress, mFragmentCommunicator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temperature, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButtonGetTemperature = (ImageButton) view.findViewById(R.id.image_button_get_temperature);
        mButtonGetTemperature.setOnClickListener(this);

        mTextViewTemperature = (TextView) view.findViewById(R.id.text_view_temperature);
        mTextViewModel = (TextView) view.findViewById(R.id.text_view_model_no);
        mTextViewSerial1 = (TextView) view.findViewById(R.id.text_view_serial_1);
        mTextViewSerial2 = (TextView) view.findViewById(R.id.text_view_serial_2);
        mTextViewCount = (TextView) view.findViewById(R.id.text_view_memory_count);
        mTextViewClock = (TextView) view.findViewById(R.id.text_view_clock);

        mListViewTemperatures = (ListView) view.findViewById(R.id.list_view_temperatures);
        mListViewTemperatures.setAdapter(temperaturesAdapter);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_temperature, menu);
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
            session.send(TemperatureMessageID.WRITE_CLOCK);
            return true;
        }

        else if(id == R.id.action_turn_off)
        {
            session.send(TemperatureMessageID.TURN_OFF);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void passDataToFragment(byte[] buffer, int length, String address) {
        if(buffer != null && address == mDeviceAddress){
            if(session.checkSanity(buffer, length)){
                byte cmd = buffer[1];

                if(cmd == TemperatureMessageID.READ_CLOCK.getValue()) {
                    TemperatureClock clock = TemperatureAnalyzer.analyseClock(buffer);
                    mTextViewClock.setText(clock.getDate() + " " + clock.getTime());
                }
                else if(cmd == TemperatureMessageID.READ_MODEL.getValue()){
                    mTextViewModel.setText(String.valueOf(TemperatureAnalyzer.analyseReadModel(buffer)));
                }
                else if(cmd == TemperatureMessageID.READ_DATA_1.getValue()){
                    TemperatureClock clock = TemperatureAnalyzer.analyzeReadData1(buffer);

                    Temperature temperature = new Temperature(clock);
                    mTemperatures.add(temperature);
                }
                else if(cmd == TemperatureMessageID.READ_DATA_2.getValue()){
                    Temperature temp = mTemperatures.get(mTemperatures.size() - 1);

                    Temperature temperature = TemperatureAnalyzer.analyseReadData2(buffer);
                    temperature.setClock(temp.getClock());

                    mTemperatures.remove(temp);
                    mTemperatures.add(temperature);
                    temperaturesAdapter.notifyDataSetChanged();
                }
                else if(cmd == TemperatureMessageID.READ_SERIAL_1.getValue()){
                    mTextViewSerial1.setText(String.valueOf(TemperatureAnalyzer.analyseReadSerial1(buffer)));
                }
                else if(cmd == TemperatureMessageID.READ_SERIAL_2.getValue()){
                    mTextViewSerial2.setText(String.valueOf(TemperatureAnalyzer.analyseReadSerial2(buffer)));
                }
                else if(cmd == TemperatureMessageID.READ_COUNT.getValue()){
                    mTextViewCount.setText(String.valueOf(TemperatureAnalyzer.analyseReadCount(buffer)));
                }
                else if(cmd == TemperatureMessageID.START_TEMPERATURE.getValue()){
                    Temperature temperature = TemperatureAnalyzer.analyseStartTemperature(buffer);
                    TemperatureClock clock = new TemperatureClock();
                    temperature.setClock(clock);
                    mTextViewTemperature.setText(String.valueOf(temperature.getObjectTemp()) + "\u00b0C");
                }
                else if(cmd == TemperatureMessageID.TURN_OFF.getValue()){

                }
            }
            if(!session.lastCommands.isEmpty()){
                session.send();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.image_button_get_temperature:
                session.send(TemperatureMessageID.START_TEMPERATURE);
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

    public class TemperaturesAdapter extends ArrayAdapter<Temperature> {

        public TemperaturesAdapter(Context context, List<Temperature> users) {
            super(context, 0, users);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Temperature temperature = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.temperature_list_item, parent, false);
            }
            // Lookup view for data population
            TextView textViewClock = (TextView) convertView.findViewById(R.id.text_view_clock);
            TextView textViewType = (TextView) convertView.findViewById(R.id.text_view_temperature_type);
            TextView textViewTempObject = (TextView) convertView.findViewById(R.id.text_view_ambient_temperature);
            TextView textViewTempAmbient = (TextView) convertView.findViewById(R.id.text_view_object_temperature);

            // Populate the data into the template view using the data object
            textViewClock.setText(temperature.getDate() + " " + temperature.getTime());
            textViewType.setText(temperature.getClock().getType());
            textViewTempObject.setText(String.valueOf(temperature.getObjectTemp()) + "\u00b0C");
            textViewTempAmbient.setText(String.valueOf(temperature.getAmbientTemp()) + "\u00b0C");
            // Return the completed view to render on screen
            return convertView;
        }
    }

}
