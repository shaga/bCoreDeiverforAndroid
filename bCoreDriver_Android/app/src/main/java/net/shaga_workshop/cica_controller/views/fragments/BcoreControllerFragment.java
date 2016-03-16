package net.shaga_workshop.cica_controller.views.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.shaga_workshop.bcore_lib.BcoreConsts;
import net.shaga_workshop.cica_controller.R;
import net.shaga_workshop.cica_controller.views.controls.BcoreSeekBarView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnBcoreControllerListener} interface
 * to handle interaction events.
 * Use the {@link BcoreControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BcoreControllerFragment extends Fragment {
    public interface OnBcoreControllerListener {
        void setMotorSpeed(int idx, int spped);
        void setServoPos(int idx, int pos);
        void setPortOut(int idx, boolean state);
    }

    private static final int[] MOTOR_RES_IDS = {
            R.id.vseek_mot0,
            R.id.vseek_mot1,
            R.id.vseek_mot2,
            R.id.vseek_mot3,
    };

    private static final int[] SERVO_RES_IDS = {
            R.id.vseek_srv0,
            R.id.vseek_srv1,
            R.id.vseek_srv2,
            R.id.vseek_srv3,
    };

    private static final int[] PORT_OUT_RES_IDS = {
            R.id.toggle_po0,
            R.id.toggle_po1,
            R.id.toggle_po2,
            R.id.toggle_po3,
    };

    private BcoreSeekBarView[] mMotorSeekBars = new BcoreSeekBarView[BcoreConsts.MAX_MOTOR_COUNT];
    private BcoreSeekBarView[] mServoSeekBars = new BcoreSeekBarView[BcoreConsts.MAX_SERVO_COUNT];
    private ToggleButton[] mPortOutToggles = new ToggleButton[BcoreConsts.MAX_PORT_COUNT];
    private TextView mBatteryText;
    private int mBatteryValue;
    private String mBatteryFmt;
    private OnBcoreControllerListener mListener;

    private BcoreSeekBarView.BcoreSeekViewListener mSeekViewListener = new BcoreSeekBarView.BcoreSeekViewListener() {
        @Override
        public void UpdateMotorValue(int idx, int value) {
            if (mListener != null) {
                mListener.setMotorSpeed(idx, value);
            }
        }

        @Override
        public void UpdateServoValue(int idx, int value) {
        }
    };


    public BcoreControllerFragment() {
        // Required empty public constructor
    }

    public static BcoreControllerFragment newInstance() {
        BcoreControllerFragment fragment = new BcoreControllerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_bcore_controller, container, false);

        for (int i = 0; i < BcoreConsts.MAX_MOTOR_COUNT; i++) {
            mMotorSeekBars[i] = (BcoreSeekBarView) view.findViewById(MOTOR_RES_IDS[i]);
            if (i == 0 || i == 1) {
                mMotorSeekBars[i].setVisibility(View.VISIBLE);
            } else {
                mMotorSeekBars[i].setVisibility(View.GONE);
            }
            mMotorSeekBars[i].setBcoreSeekBarViewListener(mSeekViewListener);
        }

        for (int i = 0; i < BcoreConsts.MAX_SERVO_COUNT; i++) {
            mServoSeekBars[i] = (BcoreSeekBarView) view.findViewById(SERVO_RES_IDS[i]);
            mServoSeekBars[i].setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < BcoreConsts.MAX_PORT_COUNT; i++) {
            mPortOutToggles[i] = (ToggleButton) view.findViewById(PORT_OUT_RES_IDS[i]);
            mPortOutToggles[i].setVisibility(View.INVISIBLE);
        }

        mBatteryFmt = getActivity().getString(R.string.text_battery_fmt);
        mBatteryText = (TextView) view.findViewById(R.id.battery_info_text);
        if (mBatteryValue > 0) {
            mBatteryText.setText(String.format(mBatteryFmt, mBatteryValue));
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public int getServoCurrentValue(int idx) {
        BcoreSeekBarView seek = mServoSeekBars[idx];

        if (seek == null) return BcoreConsts.SERVO_CENTER_POS;

        return seek.getVSeekBarProgress();
    }

    public void setBcoreFunctions(byte[] functions) {
    }

    public void setListener (OnBcoreControllerListener listener) {
        mListener = listener;
    }

    public void setBatteryValue(int value) {
        mBatteryValue = value;
        if (mBatteryText != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBatteryText.setText(String.format(mBatteryFmt, mBatteryValue));
                }
            });
        }
    }
}
