package net.shaga_workshop.cica_controller.views.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by shaga on 2015/11/03.
 */
public class VSeekBar extends SeekBar {

    public VSeekBar(Context context) {
        super(context);
        lastUpdatedTime = System.currentTimeMillis();
    }

    public VSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        lastUpdatedTime = System.currentTimeMillis();
    }

    public VSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        lastUpdatedTime = System.currentTimeMillis();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.rotate(90);
        canvas.translate(0, -getWidth());
        super.onDraw(canvas);
    }

    private OnSeekBarChangeListener onChangedListener;

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        onChangedListener = l;
    }

    private long SEND_SPAN = 40;
    private long SEND_SPAN_SAME_MIN = 150;
    private final int MAX_COUNT_SAME = 5;
    private int sameCount = 0;

    private int lastProgress = 0;

    private boolean isAutoReset = false;

    private int autoResetValue = 0;

    private long lastUpdatedTime = 0;

    public boolean getAutoReset() {
        return isAutoReset;
    }

    public void setAutoReset(boolean autoReset, int autoResetValue) {
        isAutoReset = autoReset;
        this.autoResetValue = autoResetValue;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onChangedListener.onStartTrackingTouch(this);
                setPressed(true);
                setSelected(true);
                break;
            case MotionEvent.ACTION_MOVE:
                super.onTouchEvent(event);
                int progress = (int)(getMax() * event.getY() / getHeight());

                if (progress < 0) progress = 0;
                else if(progress > getMax()) progress = getMax();

                setProgress(progress);
                long now = System.currentTimeMillis();
                long span = now - lastUpdatedTime;

                if ((progress != lastProgress && span > SEND_SPAN) ||
                        (progress == lastProgress && span > SEND_SPAN_SAME_MIN)) {
                    lastProgress = progress;
                    lastUpdatedTime = now;
                    if (onChangedListener != null)
                        onChangedListener.onProgressChanged(this, progress, true);
                }

                onSizeChanged(getWidth(), getHeight(), 0, 0);
                setPressed(true);
                setSelected(true);
                break;
            case MotionEvent.ACTION_UP:
                if (getAutoReset()) {
                    setProgress(autoResetValue);
                    onSizeChanged(getWidth(), getHeight(), 0, 0);

                    if (onChangedListener != null)
                        onChangedListener.onProgressChanged(this, getProgress(), true);
                }

                if (onChangedListener != null)
                    onChangedListener.onStopTrackingTouch(this);

                lastUpdatedTime = 0;
                setPressed(false);
                setSelected(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                super.onTouchEvent(event);
                setPressed(false);
                setSelected(false);
                break;
        }

        return true;
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }
}
