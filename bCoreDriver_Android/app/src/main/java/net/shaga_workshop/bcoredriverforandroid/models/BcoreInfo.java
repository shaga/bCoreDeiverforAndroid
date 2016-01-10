package net.shaga_workshop.bcoredriverforandroid.models;

import android.view.View;

import net.cattaka.util.cathandsgendroid.annotation.DataModel;
import net.cattaka.util.cathandsgendroid.annotation.DataModelAttrs;
import net.shaga_workshop.bcore_lib.BcoreConsts;

/**
 * Created by shaga on 2015/11/21.
 */
@DataModel(find = {
        "id", "deviceAddr"
}, unique = {
        "deviceAddr"
})
public class BcoreInfo {

    private static final int SERVO_TRIM_ABS_MAX = 15;
    public static final int SERVO_TRIM_MAX = SERVO_TRIM_ABS_MAX;
    public static final int SERVO_TRIM_MIN = 0 - SERVO_TRIM_ABS_MAX;

    @DataModelAttrs(primaryKey = true)
    private Long id;

    private String deviceAddr;
    private String deviceName;
    private String displayName;

    private boolean isShowMotor0 = true;
    private boolean isShowMotor1 = true;
    private boolean isShowMotor2 = true;
    private boolean isShowMotor3 = true;

    private boolean isShowServo0 = true;
    private boolean isShowServo1 = true;
    private boolean isShowServo2 = true;
    private boolean isShowServo3 = true;

    private boolean isShowPortOut0 = true;
    private boolean isShowPortOut1 = true;
    private boolean isShowPortOut2 = true;
    private boolean isShowPortOut3 = true;

    private boolean isFlipMotor0;
    private boolean isFlipMotor1;
    private boolean isFlipMotor2;
    private boolean isFlipMotor3;

    private boolean isFlipServo0;
    private boolean isFlipServo1;
    private boolean isFlipServo2;
    private boolean isFlipServo3;

    private boolean isSyncServo1;
    private boolean isSyncServo2;
    private boolean isSyncServo3;

    private int trimServo0;
    private int trimServo1;
    private int trimServo2;
    private int trimServo3;

    public BcoreInfo() {

    }

    public BcoreInfo(String name, String addr) {
        deviceName = name;
        displayName = name;
        deviceAddr = addr;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String name) { displayName = name; }
    public String getDeviceAddr() { return deviceAddr; }
    public void setDeviceAddr(String addr) { deviceAddr = addr; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String name) { deviceName = name; }

    public boolean getIsShowMotor0() { return getIsShowMotor(0); }
    public void setIsShowMotor0(boolean isShow) { setIsShowMotor(0, isShow); }
    public boolean getIsShowMotor1() { return getIsShowMotor(1); }
    public void setIsShowMotor1(boolean isShow) { setIsShowMotor(1, isShow); }
    public boolean getIsShowMotor2() { return getIsShowMotor(2); }
    public void setIsShowMotor2(boolean isShow) { setIsShowMotor(2, isShow); }
    public boolean getIsShowMotor3() { return getIsShowMotor(3); }
    public void setIsShowMotor3(boolean isShow) { setIsShowMotor(3, isShow); }

    public boolean getIsShowServo0() { return getIsShowServo(0); }
    public void setIsShowServo0(boolean isShow) { setIsShowServo(0, isShow); }
    public boolean getIsShowServo1() { return getIsShowServo(1); }
    public void setIsShowServo1(boolean isShow) { setIsShowServo(1, isShow); }
    public boolean getIsShowServo2() { return getIsShowServo(2); }
    public void setIsShowServo2(boolean isShow) { setIsShowServo(2, isShow); }
    public boolean getIsShowServo3() { return getIsShowServo(3); }
    public void setIsShowServo3(boolean isShow) { setIsShowServo(3, isShow); }

    public boolean getIsShowPortOut0() { return getIsShowPortOut(0); }
    public void setIsShowPortOut0(boolean isShow) { setIsShowPortOut(0, isShow); }
    public boolean getIsShowPortOut1() { return getIsShowPortOut(1); }
    public void setIsShowPortOut1(boolean isShow) { setIsShowPortOut(1, isShow); }
    public boolean getIsShowPortOut2() { return getIsShowPortOut(2); }
    public void setIsShowPortOut2(boolean isShow) { setIsShowPortOut(2, isShow); }
    public boolean getIsShowPortOut3() { return getIsShowPortOut(3); }
    public void setIsShowPortOut3(boolean isShow) { setIsShowPortOut(3, isShow); }

    public boolean getIsFlipMotor0() { return getIsFlipMotor(0); }
    public void setIsFlipMotor0(boolean isFlip) { setIsFlipMotor(0, isFlip); }
    public boolean getIsFlipMotor1() { return getIsFlipMotor(1); }
    public void setIsFlipMotor1(boolean isFlip) { setIsFlipMotor(1, isFlip); }
    public boolean getIsFlipMotor2() { return getIsFlipMotor(2); }
    public void setIsFlipMotor2(boolean isFlip) { setIsFlipMotor(2, isFlip); }
    public boolean getIsFlipMotor3() { return getIsFlipMotor(3); }
    public void setIsFlipMotor3(boolean isFlip) { setIsFlipMotor(3, isFlip); }

    public boolean getIsFlipServo0() { return getIsFlipServo(0); }
    public void setIsFlipServo0(boolean isFlip) { setIsFlipServo(0, isFlip); }
    public boolean getIsFlipServo1() { return getIsFlipServo(1); }
    public void setIsFlipServo1(boolean isFlip) { setIsFlipServo(1, isFlip); }
    public boolean getIsFlipServo2() { return getIsFlipServo(2); }
    public void setIsFlipServo2(boolean isFlip) { setIsFlipServo(2, isFlip); }
    public boolean getIsFlipServo3() { return getIsFlipServo(3); }
    public void setIsFlipServo3(boolean isFlip) { setIsFlipServo(3, isFlip); }

    public boolean getIsSyncServo1() { return getIsSyncServo(1); }
    public void setIsSyncServo1(boolean isSync) { setIsSyncServo(1, isSync); }
    public boolean getIsSyncServo2() { return getIsSyncServo(2); }
    public void setIsSyncServo2(boolean isSync) { setIsSyncServo(2, isSync); }
    public boolean getIsSyncServo3() { return getIsSyncServo(3); }
    public void setIsSyncServo3(boolean isSync) { setIsSyncServo(3, isSync); }

    public int getTrimServo0() { return getTrimServo(0); }
    public void setTrimServo0(int trim) { setTrimServo(0, trim); }
    public int getTrimServo1() { return getTrimServo(1); }
    public void setTrimServo1(int trim) { setTrimServo(1, trim); }
    public int getTrimServo2() { return getTrimServo(2); }
    public void setTrimServo2(int trim) { setTrimServo(2, trim); }
    public int getTrimServo3() { return getTrimServo(3); }
    public void setTrimServo3(int trim) { setTrimServo(3, trim); }

    public boolean getIsShowMotor(int idx) {
        switch (idx) {
            case 0:
                return isShowMotor0;
            case 1:
                return isShowMotor1;
            case 2:
                return isShowMotor2;
            case 3:
                return isShowMotor3;
        }
        return false;
    }

    public void setIsShowMotor(int idx, boolean isShow) {
        switch (idx) {
            case 0:
                isShowMotor0 = isShow;
                break;
            case 1:
                isShowMotor1 = isShow;
                break;
            case 2:
                isShowMotor2 = isShow;
                break;
            case 3:
                isShowMotor3 = isShow;
                break;
        }
    }

    public boolean getIsShowServo(int idx) {
        switch (idx) {
            case 0:
                return isShowServo0;
            case 1:
                return isShowServo1;
            case 2:
                return isShowServo2;
            case 3:
                return isShowServo3;
        }
        return false;
    }

    public void setIsShowServo(int idx, boolean isShow) {
        switch (idx) {
            case 0:
                isShowServo0 = isShow;
                break;
            case 1:
                isShowServo1 = isShow;
                break;
            case 2:
                isShowServo2 = isShow;
                break;
            case 3:
                isShowServo3 = isShow;
                break;
        }
    }

    public boolean getIsShowPortOut(int idx) {
        switch (idx) {
            case 0:
                return isShowPortOut0;
            case 1:
                return isShowPortOut1;
            case 2:
                return isShowPortOut2;
            case 3:
                return isShowPortOut3;
        }
        return false;
    }

    public void setIsShowPortOut(int idx, boolean isShow) {
        switch (idx) {
            case 0:
                isShowPortOut0 = isShow;
                break;
            case 1:
                isShowPortOut1 = isShow;
                break;
            case 2:
                isShowPortOut2 = isShow;
                break;
            case 3:
                isShowPortOut3 = isShow;
                break;
        }
    }

    public boolean getIsFlipMotor(int idx) {
        switch (idx) {
            case 0:
                return isFlipMotor0;
            case 1:
                return isFlipMotor1;
            case 2:
                return isFlipMotor2;
            case 3:
                return isFlipMotor3;
        }
        return false;
    }

    public void setIsFlipMotor(int idx, boolean isFlip) {
        switch (idx) {
            case 0:
                isFlipMotor0 = isFlip;
                break;
            case 1:
                isFlipMotor1 = isFlip;
                break;
            case 2:
                isFlipMotor2 = isFlip;
                break;
            case 3:
                isFlipMotor3 = isFlip;
                break;
        }
    }

    public boolean getIsFlipServo(int idx) {
        switch (idx) {
            case 0:
                return isFlipServo0;
            case 1:
                return isFlipServo1;
            case 2:
                return isFlipServo2;
            case 3:
                return isFlipServo3;
        }
        return false;
    }

    public void setIsFlipServo(int idx, boolean isFlip) {
        switch (idx) {
            case 0:
                isFlipServo0 = isFlip;
                break;
            case 1:
                isFlipServo1 = isFlip;
                break;
            case 2:
                isFlipServo2 = isFlip;
                break;
            case 3:
                isFlipServo3 = isFlip;
                break;
        }
    }

    public boolean getIsSyncServo(int idx) {
        switch (idx) {
            case 1:
                return isSyncServo1;
            case 2:
                return isSyncServo2;
            case 3:
                return isSyncServo3;
        }
        return false;
    }

    public void setIsSyncServo(int idx, boolean isSync) {
        switch (idx) {
            case 1:
                isSyncServo1 = isSync;
                break;
            case 2:
                isSyncServo2 = isSync;
                break;
            case 3:
                isSyncServo3 = isSync;
                break;
            default:
                break;
        }
    }

    public int getTrimServo(int idx) {
        switch (idx) {
            case 0:
                return trimServo0;
            case 1:
                return trimServo1;
            case 2:
                return trimServo2;
            case 3:
                return trimServo3;
            default:
                break;
        }

        return 0;
    }

    public void setTrimServo(int idx, int trim) {
        switch (idx) {
            case 0:
                trimServo0 = trim;
                break;
            case 1:
                trimServo1 = trim;
                break;
            case 2:
                trimServo2 = trim;
                break;
            case 3:
                trimServo3 = trim;
                break;
            default:
                break;
        }
    }

    public void reset() {
        displayName = deviceName;
        for (int i = 9; i < BcoreConsts.MAX_MOTOR_COUNT; i++) {
            setIsShowMotor(i, true);
            setIsFlipMotor(i, false);
        }

        for (int i = 0; i < BcoreConsts.MAX_SERVO_COUNT; i++) {
            setIsShowServo(i, true);
            setIsFlipServo(i, false);
            setIsSyncServo(i, false);
            setTrimServo(i, 0);
        }

        for (int i = 0; i < BcoreConsts.MAX_PORT_COUNT; i++) {
            setIsShowPortOut(i, true);
        }
    }
}
