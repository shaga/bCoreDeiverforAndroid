package net.shaga_workshop.bcoredriverforandroid.views.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import net.shaga_workshop.bcore_lib.BcoreConsts;
import net.shaga_workshop.bcoredriverforandroid.R;

/**
 * Custom View within Vertical SeekBar and TextView for Label
 */
public class BcoreSeekBarView extends LinearLayout {

    /**
     * interface for using BcoreSeekBarView
     */
    public interface BcoreSeekViewListener {
        void UpdateMotorValue(int idx, int value);
        void UpdateServoValue(int idx, int value);
    }

    private static final int[] MOTOR_STRING_IDS = {
            R.string.text_mot0_name,
            R.string.text_mot1_name,
            R.string.text_mot2_name,
            R.string.text_mot3_name,
    };

    private static final int[] SERVO_STRING_IDS = {
            R.string.text_srv0_name,
            R.string.text_srv1_name,
            R.string.text_srv2_name,
            R.string.text_srv3_name,
    };

    private int mType;
    private int mIndex;

    private VSeekBar mVSeekBar;

    private BcoreSeekViewListener mListener;

    /**
     * Vertial SeekBar ChangedListener
     */
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mListener != null) {
                if (mType == getContext().getResources().getInteger(R.integer.seekbarview_type_servo)) {
                    mListener.UpdateServoValue(mIndex, progress);
                } else {
                    mListener.UpdateMotorValue(mIndex, progress);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mListener != null) {
                if (mType == getContext().getResources().getInteger(R.integer.seekbarview_type_servo)) {
                    mListener.UpdateServoValue(mIndex, seekBar.getProgress());
                } else {
                    mListener.UpdateMotorValue(mIndex, seekBar.getProgress());
                }
            }
        }
    };

    public BcoreSeekBarView(Context context) {
        super(context);
        init(null, 0);
    }

    public BcoreSeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BcoreSeekBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * initialize BcoreSeekBarView
     * @param attrs
     * @param defStyle
     */
    private void init(AttributeSet attrs, int defStyle) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_bcore_seekbar, this);

        TextView textType = (TextView) view.findViewById(R.id.text_bcore_vseek_type);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.BcoreSeekBarView);

        final int typeMotor = getResources().getInteger(R.integer.seekbarview_type_motor);
        final int typeServo = getResources().getInteger(R.integer.seekbarview_type_servo);

        mType = a.getInt(R.styleable.BcoreSeekBarView_vseekbarType, typeMotor);

        mIndex = a.getInt(R.styleable.BcoreSeekBarView_vseekbarIndex, 0);

        mVSeekBar = (VSeekBar) view.findViewById(R.id.bcore_vseekbar);

        Drawable seekBarProgressDrawable;
        if (mType == typeServo) {
            if (mIndex < 0 || BcoreConsts.MAX_SERVO_COUNT <= mIndex) mIndex = 0;
            textType.setText(SERVO_STRING_IDS[mIndex]);
            seekBarProgressDrawable = getContext().getResources().getDrawable(R.drawable.my_seek_selector_blue);
        } else {
            if (mIndex < 0 || BcoreConsts.MAX_MOTOR_COUNT <= mIndex) mIndex = 0;
            textType.setText(MOTOR_STRING_IDS[mIndex]);
            seekBarProgressDrawable = getContext().getResources().getDrawable(R.drawable.my_seek_selector_red);
            mVSeekBar.setAutoReset(true, BcoreConsts.MOTOR_STOP_VALUE);
        }

        mVSeekBar.setProgressDrawable(seekBarProgressDrawable);
        mVSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        boolean isVisible = a.getBoolean(R.styleable.BcoreSeekBarView_vseekbarLableVisible, true);

        if (!isVisible) {
            textType.setVisibility(GONE);
        }

        a.recycle();

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        mVSeekBar.setEnabled(enabled);
    }

    /**
     * set to Vertical SeekBar Progress
     * @param progress
     */
    public void setVSeekBarProgress(int progress) {
        mVSeekBar.setProgress(progress);
    }

    /**
     * set to Vertical SeekBar Maximum
     * @param max
     */
    public void setVSeekBarMax(int max) {
        mVSeekBar.setMax(max);
    }

    /**
     * set to Listener of BcoreSeekBarView
     * @param listener
     */
    public void setBcoreSeekBarViewListener(BcoreSeekViewListener listener) {
        mListener = listener;
    }

    /**
     * get current Vertical SeekBar Progress;
     * @return
     */
    public int getVSeekBarProgress() {
        if (mVSeekBar != null) return mVSeekBar.getProgress();
        return 0;
    }
}
