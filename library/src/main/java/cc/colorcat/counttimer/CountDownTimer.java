/*
 * Copyright 2018 cxx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.colorcat.counttimer;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: cxx
 * Date: 2018-07-03
 * GitHub: https://github.com/ccolorcat
 */
public class CountDownTimer {
    /**
     * 倒计时计数状态——已停止
     */
    public static final int STATE_STOP = -1;
    /**
     * 倒计时计数状态——已暂停
     */
    public static final int STATE_PAUSE = 0;
    /**
     * 倒计时计数状态——进行中
     */
    public static final int STATE_GOING = 1;

    @IntDef({CountDownTimer.STATE_STOP, CountDownTimer.STATE_PAUSE, CountDownTimer.STATE_GOING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    protected final Handler mHandler = new Handler(Looper.getMainLooper());

    private final int mTotalCount; // 倒计时总计数
    private int mCount; // 当前倒计时计数
    private int mInterval; // 倒计时计数间隔时间，以毫秒为单位。
    private boolean mReverse = false;
    @State
    private int mState = CountDownTimer.STATE_STOP; // 倒计时当前计数状态

    private List<OnCountDownListener> mCountListeners = new ArrayList<>();
    private List<OnStateChangeListener> mStateListeners = new ArrayList<>();
    private OnCountDownListener mCountDownListener;
    private OnStateChangeListener mStateListener;

    public CountDownTimer(int totalCount, int intervalInMilliseconds) {
        mTotalCount = totalCount;
        mCount = totalCount;
        mInterval = intervalInMilliseconds;
    }

    public void start() {
        if (mState == STATE_STOP) {
            resetCount();
            notifyCountDownStart();
            execute();
        }
    }

    public void pause() {
        if (mState == STATE_GOING) {
            mState = STATE_PAUSE;
            notifyStateChanged();
            mHandler.removeCallbacks(mRunnable);
        }
    }

    public void restart() {
        if (mState == STATE_PAUSE) {
            execute();
        }
    }

    public void stop() {
        if (mState != STATE_STOP) {
            mState = STATE_STOP;
            notifyStateChanged();
            resetCount();
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private void resetCount() {
        mCount = mReverse ? 0 : mTotalCount;
    }

    private void execute() {
        mState = STATE_GOING;
        notifyStateChanged();
        mHandler.postDelayed(mRunnable, mInterval);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mState == STATE_GOING) {
                if (mReverse) {
                    ++mCount;
                } else {
                    --mCount;
                }
                if (mCount <= 0 || mCount >= mTotalCount) {
                    stop();
                    notifyCountDownEnd();
                } else {
                    notifyCountDownChanged();
                    mHandler.postDelayed(mRunnable, mInterval);
                }
            }
        }
    };


    private void notifyCountDownStart() {
        for (int i = 0, size = mCountListeners.size(); i < size; ++i) {
            mCountListeners.get(i).onStart();
        }
    }

    private void notifyCountDownChanged() {
        for (int i = 0, size = mCountListeners.size(); i < size; ++i) {
            mCountListeners.get(i).onCountDown(mTotalCount, mCount);
        }
    }

    private void notifyCountDownEnd() {
        for (int i = 0, size = mCountListeners.size(); i < size; ++i) {
            mCountListeners.get(i).onStop();
        }
    }

    private void notifyStateChanged() {
        if (mStateListener != null) {
            mStateListener.onStateChanged(mState);
        }
    }


    public interface OnCountDownListener {
        void onStart();

        void onCountDown(int totalCount, int currentCount);

        void onStop();
    }


    public interface OnStateChangeListener {
        void onStateChanged(@State int state);
    }
}
