package net.shaga_workshop.bcoredriverforandroid.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.shaga_workshop.bcore_lib.BcoreConsts;
import net.shaga_workshop.bcore_lib.BcoreValueUtil;
import net.shaga_workshop.bcoredriverforandroid.R;
import net.shaga_workshop.bcoredriverforandroid.views.controls.BcoreSeekBarView;
import net.shaga_workshop.bcoredriverforandroid.models.BcoreInfo;

public class BcoreSettingFragment extends Fragment {

    private static final int ROW_TEXT = 0;
    private static final int ROW_SHOW = 1;
    private static final int ROW_FLIP = 2;
    private static final int ROW_SYNC = 3;
    private static final int ROW_TRIM = 4;

    private static final int[][] SERVO_CTRL_RES_ID = {
            {R.id.text_setting_srv1, R.id.sw_setting_show_srv0, R.id.sw_setting_flip_srv0, -1, R.id.vseek_setting_trim_srv0 },
            {R.id.text_setting_srv2, R.id.sw_setting_show_srv1, R.id.sw_setting_flip_srv1, R.id.sw_setting_sync_srv1, R.id.vseek_setting_trim_srv1},
            {R.id.text_setting_srv3, R.id.sw_setting_show_srv2, R.id.sw_setting_flip_srv2, R.id.sw_setting_sync_srv2, R.id.vseek_setting_trim_srv2},
            {R.id.text_setting_srv4, R.id.sw_setting_show_srv3, R.id.sw_setting_flip_srv3, R.id.sw_setting_sync_srv3, R.id.vseek_setting_trim_srv3},
    };

    private static final int[][] PORT_OUT_CTRL_RES_ID = {
            {R.id.text_setting_po1, R.id.sw_setting_show_po0},
            {R.id.text_setting_po2, R.id.sw_setting_show_po1},
            {R.id.text_setting_po3, R.id.sw_setting_show_po2},
            {R.id.text_setting_po4, R.id.sw_setting_show_po3},
    };

    private static final int[][] MOT_CTRL_RES_ID = {
            {R.id.text_setting_mot1, R.id.sw_setting_show_mot0, R.id.sw_setting_flip_mot0},
            {R.id.text_setting_mot2, R.id.sw_setting_show_mot1, R.id.sw_setting_flip_mot1},
            {R.id.text_setting_mot3, R.id.sw_setting_show_mot2, R.id.sw_setting_flip_mot2},
            {R.id.text_setting_mot4, R.id.sw_setting_show_mot3, R.id.sw_setting_flip_mot3},
    };

    private GridLayout mGridServo;
    private TextView[] mTextServo = new TextView[BcoreConsts.MAX_SERVO_COUNT];
    private ToggleButton[] mToggleShowServo = new ToggleButton[BcoreConsts.MAX_SERVO_COUNT];
    private ToggleButton[] mToggleFlipServo = new ToggleButton[BcoreConsts.MAX_SERVO_COUNT];
    private ToggleButton[] mToggleSyncServo = new ToggleButton[BcoreConsts.MAX_SERVO_COUNT];
    private BcoreSeekBarView[] mVSeekTrimServo = new BcoreSeekBarView[BcoreConsts.MAX_SERVO_COUNT];

    private GridLayout mGridMotor;
    private TextView[] mTextMotor = new TextView[BcoreConsts.MAX_MOTOR_COUNT];
    private ToggleButton[] mToggleShowMotor = new ToggleButton[BcoreConsts.MAX_MOTOR_COUNT];
    private ToggleButton[] mToggleFlipMotor = new ToggleButton[BcoreConsts.MAX_MOTOR_COUNT];

    private GridLayout mGridPortOut;
    private TextView[] mTextPortOut = new TextView[BcoreConsts.MAX_PORT_COUNT];
    private ToggleButton[] mToggleShowPortOut = new ToggleButton[BcoreConsts.MAX_PORT_COUNT];

    private EditText mEditBcoreName;

    private byte[] mFunctions;

    private BcoreInfo mBcoreInfo;

    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();

            switch (id) {
                case R.id.sw_setting_show_mot0:
                    mBcoreInfo.setIsShowMotor0(isChecked);
                    break;
                case R.id.sw_setting_show_mot1:
                    mBcoreInfo.setIsShowMotor1(isChecked);
                    break;
                case R.id.sw_setting_show_mot2:
                    mBcoreInfo.setIsShowMotor2(isChecked);
                    break;
                case R.id.sw_setting_show_mot3:
                    mBcoreInfo.setIsShowMotor3(isChecked);
                    break;
                case R.id.sw_setting_show_srv0:
                    mBcoreInfo.setIsShowServo0(isChecked);
                    break;
                case R.id.sw_setting_show_srv1:
                    mBcoreInfo.setIsShowServo1(isChecked);
                    break;
                case R.id.sw_setting_show_srv2:
                    mBcoreInfo.setIsShowServo2(isChecked);
                    break;
                case R.id.sw_setting_show_srv3:
                    mBcoreInfo.setIsShowServo3(isChecked);
                    break;
                case R.id.sw_setting_show_po0:
                    mBcoreInfo.setIsShowPortOut0(isChecked);
                    break;
                case R.id.sw_setting_show_po1:
                    mBcoreInfo.setIsShowPortOut1(isChecked);
                    break;
                case R.id.sw_setting_show_po2:
                    mBcoreInfo.setIsShowPortOut2(isChecked);
                    break;
                case R.id.sw_setting_show_po3:
                    mBcoreInfo.setIsShowPortOut3(isChecked);
                    break;
                case R.id.sw_setting_flip_mot0:
                    mBcoreInfo.setIsFlipMotor0(isChecked);
                    break;
                case R.id.sw_setting_flip_mot1:
                    mBcoreInfo.setIsFlipMotor1(isChecked);
                    break;
                case R.id.sw_setting_flip_mot2:
                    mBcoreInfo.setIsFlipMotor2(isChecked);
                    break;
                case R.id.sw_setting_flip_mot3:
                    mBcoreInfo.setIsFlipMotor3(isChecked);
                    break;
                case R.id.sw_setting_flip_srv0:
                    mBcoreInfo.setIsFlipServo0(isChecked);
                    break;
                case R.id.sw_setting_flip_srv1:
                    mBcoreInfo.setIsFlipServo1(isChecked);
                    break;
                case R.id.sw_setting_flip_srv2:
                    mBcoreInfo.setIsFlipServo2(isChecked);
                    break;
                case R.id.sw_setting_flip_srv3:
                    mBcoreInfo.setIsFlipServo3(isChecked);
                    break;
                case R.id.sw_setting_sync_srv1:
                    mBcoreInfo.setIsSyncServo1(isChecked);
                    break;
                case R.id.sw_setting_sync_srv2:
                    mBcoreInfo.setIsSyncServo2(isChecked);
                    break;
                case R.id.sw_setting_sync_srv3:
                    mBcoreInfo.setIsSyncServo3(isChecked);
                    break;
                default:
                    return;
            }

            if (mListener != null) {
                mListener.onUpdatedBcoreInfo();
            }
        }
    };

    private BcoreSeekBarView.BcoreSeekViewListener mSeekBarViewListener = new BcoreSeekBarView.BcoreSeekViewListener() {

        @Override
        public void UpdateMotorValue(int idx, int value) {
        }

        @Override
        public void UpdateServoValue(int idx, int value) {
            value += BcoreInfo.SERVO_TRIM_MIN;
            if (mBcoreInfo.getTrimServo(idx) != value) {
                mBcoreInfo.setTrimServo(idx, value);
                if (mListener != null) mListener.onUpdatedBcoreInfo();
            }
        }
    };

    public interface OnSettingUpdateListener {
        void onUpdatedBcoreInfo();
    }

    private OnSettingUpdateListener mListener;

    public BcoreSettingFragment() {
        // Required empty public constructor
    }

    public static BcoreSettingFragment newInstance() {
        BcoreSettingFragment fragment = new BcoreSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setFunctions(byte[] functions) {
        mFunctions = functions;

        Activity activity = getActivity();

        if (activity != null) {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setCtrlVisibility();
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bcore_setting, container, false);

        mEditBcoreName = (EditText) view.findViewById(R.id.edit_bcore_name);
        mEditBcoreName.setText(mBcoreInfo.getDisplayName());
        mEditBcoreName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBcoreInfo.setDisplayName(s.toString());
                if (mListener != null) {
                    mListener.onUpdatedBcoreInfo();
                }
            }
        });

        mGridServo = (GridLayout) view.findViewById(R.id.grid_setting_servo);
        for (int i = 0; i < BcoreConsts.MAX_SERVO_COUNT; i++) {
            mTextServo[i] = (TextView) view.findViewById(SERVO_CTRL_RES_ID[i][ROW_TEXT]);
            mToggleShowServo[i] = (ToggleButton) view.findViewById(SERVO_CTRL_RES_ID[i][ROW_SHOW]);
            mToggleShowServo[i].setOnCheckedChangeListener(mCheckedChangeListener);
            mToggleFlipServo[i] = (ToggleButton) view.findViewById(SERVO_CTRL_RES_ID[i][ROW_FLIP]);
            mToggleFlipServo[i].setOnCheckedChangeListener(mCheckedChangeListener);
            if (SERVO_CTRL_RES_ID[i][ROW_SYNC] != -1) {
                mToggleSyncServo[i] = (ToggleButton) view.findViewById(SERVO_CTRL_RES_ID[i][ROW_SYNC]);
                mToggleSyncServo[i].setOnCheckedChangeListener(mCheckedChangeListener);
            } else {
                mToggleSyncServo[i] = null;
            }
            mVSeekTrimServo[i] = (BcoreSeekBarView) view.findViewById(SERVO_CTRL_RES_ID[i][ROW_TRIM]);
            mVSeekTrimServo[i].setVSeekBarMax(BcoreInfo.SERVO_TRIM_MAX - BcoreInfo.SERVO_TRIM_MIN);
            mVSeekTrimServo[i].setBcoreSeekBarViewListener(mSeekBarViewListener);
        }

        mGridMotor = (GridLayout) view.findViewById(R.id.grid_setting_motor);
        for (int i = 0; i < BcoreConsts.MAX_MOTOR_COUNT; i++) {
            mTextMotor[i] = (TextView) view.findViewById(MOT_CTRL_RES_ID[i][ROW_TEXT]);
            mToggleShowMotor[i] = (ToggleButton) view.findViewById(MOT_CTRL_RES_ID[i][ROW_SHOW]);
            mToggleShowMotor[i].setOnCheckedChangeListener(mCheckedChangeListener);
            mToggleFlipMotor[i] = (ToggleButton) view.findViewById(MOT_CTRL_RES_ID[i][ROW_FLIP]);
            mToggleFlipMotor[i].setOnCheckedChangeListener(mCheckedChangeListener);
        }

        mGridPortOut = (GridLayout) view.findViewById(R.id.grid_setting_port_out);
        for (int i = 0; i < BcoreConsts.MAX_PORT_COUNT; i++) {
            mTextPortOut[i] = (TextView) view.findViewById(PORT_OUT_CTRL_RES_ID[i][ROW_TEXT]);
            mToggleShowPortOut[i] = (ToggleButton) view.findViewById(PORT_OUT_CTRL_RES_ID[i][ROW_SHOW]);
            mToggleShowPortOut[i].setOnCheckedChangeListener(mCheckedChangeListener);
        }

        setCtrlVisibility();

        setControlValue();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    public void setOnSettingUpdateListener(OnSettingUpdateListener listener) {
        mListener = listener;
    }

    public void setBcoreInfo(BcoreInfo info) {
        mBcoreInfo = info;
        setCtrlVisibility();
        setControlValue();
    }

    private void setCtrlVisibility() {
        if (mFunctions == null) return;

        if (BcoreValueUtil.hasEnableMotor(mFunctions)) {
            for (int i = 0; i < BcoreConsts.MAX_MOTOR_COUNT; i++) {
                int visibility = BcoreValueUtil.isEnabledMotorIdx(mFunctions, i) ? View.VISIBLE : View.GONE;
                if (mTextMotor[i] != null)  mTextMotor[i].setVisibility(visibility);
                if (mToggleShowMotor[i] != null) mToggleShowMotor[i].setVisibility(visibility);
                if (mToggleFlipMotor[i] != null) mToggleFlipMotor[i].setVisibility(visibility);
           }
        } else if (mGridMotor != null) {
            mGridMotor.setVisibility(View.INVISIBLE);
        }

        if (BcoreValueUtil.hasEnableServo(mFunctions)) {
            for (int i = 0; i < BcoreConsts.MAX_SERVO_COUNT; i++) {
                int visibility = BcoreValueUtil.isEnabledServoIdx(mFunctions, i) ? View.VISIBLE : View.INVISIBLE;
                if (mTextServo[i] != null) mTextServo[i].setVisibility(visibility);
                if (mToggleShowServo[i] != null) mToggleShowServo[i].setVisibility(visibility);
                if (mToggleFlipServo[i] != null) mToggleFlipServo[i].setVisibility(visibility);
                if (mToggleSyncServo[i] != null) mToggleSyncServo[i].setVisibility(visibility);
                if (mVSeekTrimServo[i] != null) mVSeekTrimServo[i].setVisibility(visibility);
            }
        } else if (mGridServo != null) {
            mGridServo.setVisibility(View.INVISIBLE);
        }

        if (BcoreValueUtil.hasEnablePortOut(mFunctions)) {
            for (int i = 0; i < BcoreConsts.MAX_PORT_COUNT; i++) {
                int visibility = BcoreValueUtil.isEnabledPortIdx(mFunctions, i) ? View.VISIBLE : View.GONE;
                if (mTextPortOut[i] != null) mTextPortOut[i].setVisibility(visibility);
                if (mToggleShowPortOut[i] != null) mToggleShowPortOut[i].setVisibility(visibility);
            }
        } else if (mGridPortOut != null) {
            mGridPortOut.setVisibility(View.INVISIBLE);
        }
    }

    public void setControlValue() {
        Activity activity = getActivity();

        if (activity == null) return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditBcoreName.setText(mBcoreInfo.getDisplayName());

                for (int i = 0; i < BcoreConsts.MAX_MOTOR_COUNT; i++) {
                    mToggleShowMotor[i].setChecked(mBcoreInfo.getIsShowMotor(i));
                    mToggleFlipMotor[i].setChecked(mBcoreInfo.getIsFlipMotor(i));
                }

                for (int i = 0; i < BcoreConsts.MAX_SERVO_COUNT; i++) {
                    mToggleShowServo[i].setChecked(mBcoreInfo.getIsShowServo(i));
                    mToggleFlipServo[i].setChecked(mBcoreInfo.getIsFlipServo(i));
                    if (mToggleSyncServo[i] != null) {
                        mToggleSyncServo[i].setChecked(mBcoreInfo.getIsSyncServo(i));
                    }
                    mVSeekTrimServo[i].setVSeekBarMax(BcoreInfo.SERVO_TRIM_MAX - BcoreInfo.SERVO_TRIM_MIN);
                    mVSeekTrimServo[i].setVSeekBarProgress(mBcoreInfo.getTrimServo(i) - BcoreInfo.SERVO_TRIM_MIN);
                }

                for (int i = 0; i < BcoreConsts.MAX_PORT_COUNT; i++) {
                    mToggleShowPortOut[i].setChecked(mBcoreInfo.getIsShowPortOut(i));
                }
            }
        });
    }
}
