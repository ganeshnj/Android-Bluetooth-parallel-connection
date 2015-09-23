package com.ensisa.ganesh.abluetoothdriver.fora.balance;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ensisa.ganesh.abluetoothdriver.FragmentCommunicator;
import com.ensisa.ganesh.abluetoothdriver.MainActivity;
import com.ensisa.ganesh.abluetoothdriver.R;
import com.ensisa.ganesh.abluetoothdriver.common.fragments.BluetoothFragment;
import com.ensisa.ganesh.abluetoothdriver.fora.temperature.TemperatureSession;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BalanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BalanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BalanceFragment extends BluetoothFragment implements FragmentCommunicator {
    TextView mTextViewUser;
    TextView mTextViewGender;
    TextView mTextViewAge;
    TextView mTextViewHeight;
    TextView mTextViewWeight;
    TextView mTextViewWeightXL;
    TextView mTextViewBodyFatRatio;
    TextView mTextViewBasalMetabolism;
    TextView mTextViewMoistureContent;
    TextView mTextViewMuscleRatio;
    TextView mTextViewSkeletonWeight;

    private String mDeviceName;
    private String mDeviceAddress;

    private OnFragmentInteractionListener mListener;
    FragmentCommunicator mFragmentCommunicator;

    TemperatureSession session;

    public static BalanceFragment newInstance(String name, String address) {
        BalanceFragment fragment = new BalanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    public BalanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDeviceName = getArguments().getString(ARG_NAME);
            mDeviceAddress = getArguments().getString(ARG_ADDRESS);
        }

        mFragmentCommunicator = this;
        ((MainActivity) getActivity()).mFragmentCommunicators.put(mDeviceAddress, mFragmentCommunicator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextViewUser = (TextView) view.findViewById(R.id.text_view_user);
        mTextViewGender = (TextView) view.findViewById(R.id.text_view_gender);
        mTextViewAge = (TextView) view.findViewById(R.id.text_view_age);
        mTextViewHeight = (TextView) view.findViewById(R.id.text_view_height);
        mTextViewWeight = (TextView) view.findViewById(R.id.text_view_weight);
        mTextViewWeightXL = (TextView) view.findViewById(R.id.text_view_weight_xl);
        mTextViewBodyFatRatio = (TextView) view.findViewById(R.id.text_view_body_fat_ratio);
        mTextViewBasalMetabolism = (TextView) view.findViewById(R.id.text_view_basal_metabolism);
        mTextViewMoistureContent = (TextView) view.findViewById(R.id.text_view_moisture_content);
        mTextViewMuscleRatio = (TextView) view.findViewById(R.id.text_view_muscle_ratio);
        mTextViewSkeletonWeight = (TextView) view.findViewById(R.id.text_view_skeleton_weight);
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
        ((MainActivity) getActivity()).fragmentCommunicator3 = this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void passDataToFragment(byte[] buffer, int length, String address) {
        byte start = buffer[0];
        if(start == BalanceFrame.START_PARAMETER.getValue()){
            mTextViewUser.setText(BalanceAnalyzer.analyzeUser(buffer[1]));
            mTextViewGender.setText(BalanceAnalyzer.analyzeGender(buffer[2]));
            mTextViewAge.setText(String.valueOf(BalanceAnalyzer.analyzeAge(buffer[3])));
            mTextViewHeight.setText(String.valueOf(BalanceAnalyzer.analyzeHeight(buffer[4])));
        }
        if(start == BalanceFrame.START_MEASUREMENT.getValue()){
            mTextViewWeight.setText(String.valueOf(BalanceAnalyzer.analyzeWeight(buffer[1], buffer[2])) + "Kg" + " (" + String.valueOf(BalanceAnalyzer.analyzeWeight(buffer[1], buffer[2]) * 2.2046) + "lbs)");
            mTextViewBodyFatRatio.setText(String.valueOf(BalanceAnalyzer.analyzeBodyFatRatio(buffer[3], buffer[4])) + "%");
            mTextViewBasalMetabolism.setText(String.valueOf((int)BalanceAnalyzer.analyzeBasalMetabolism(buffer[5], buffer[6])) + "Kcal");
            mTextViewMoistureContent.setText(String.valueOf(BalanceAnalyzer.analyzeMoistureContent(buffer[7], buffer[8])) + "%");
            mTextViewMuscleRatio.setText(String.valueOf(BalanceAnalyzer.analyzeMuscleRatio(buffer[9], buffer[10])) + "%");
            mTextViewSkeletonWeight.setText(String.valueOf(BalanceAnalyzer.analyzeSkletonWeight(buffer[11])) + "Kg");
            mTextViewWeightXL.setText(String.valueOf(BalanceAnalyzer.analyzeWeight(buffer[1], buffer[2])) + "Kg");
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

}
