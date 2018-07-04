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
public class CountTimer {
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

    @IntDef({CountTimer.STATE_STOP, CountTimer.STATE_PAUSE, CountTimer.STATE_GOING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private int mTotalCount; // 倒计时总计数
    private int mCount; // 当前倒计时计数
    private int mInterval; // 倒计时计数间隔时间，以毫秒为单位。
    private boolean mReverse = false; // 默认倒计时，即计数由大到小，设置为 true 则计数由小到大。
    @State
    private int mState = CountTimer.STATE_STOP; // 倒计时当前计数状态

    private List<OnCountDownListener> mCountListeners;
    private List<OnStateChangeListener> mStateListeners;

    public CountTimer() {
        this(60, 1000);
    }

    public CountTimer(int totalCount) {
        this(totalCount, 1000);
    }

    public CountTimer(int totalCount, int intervalInMilliseconds) {
        mTotalCount = totalCount;
        mInterval = intervalInMilliseconds;
    }

    public final int getTotalCount() {
        return mTotalCount;
    }

    public final void setTotalCount(int totalCount) {
        if (totalCount <= 0) {
            throw new IllegalArgumentException("totalCount <= 0");
        }
        mTotalCount = totalCount;
    }

    public final int getInterval() {
        return mInterval;
    }

    public final void setInterval(int intervalInMilliseconds) {
        if (intervalInMilliseconds <= 0) {
            throw new IllegalArgumentException("intervalInMilliseconds <= 0");
        }
        mInterval = intervalInMilliseconds;
    }

    @State
    public final int getState() {
        return mState;
    }

    public final void setReverse(boolean reverse) {
        mReverse = reverse;
    }

    public final void addOnCountDownListener(OnCountDownListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener == null");
        }
        if (mCountListeners == null) {
            mCountListeners = new ArrayList<>(4);
        }
        if (!mCountListeners.contains(listener)) {
            mCountListeners.add(listener);
        }
    }

    public final void removeOnCountDownListener(OnCountDownListener listener) {
        if (mCountListeners != null && listener != null) {
            mCountListeners.remove(listener);
        }
    }

    public final void addOnStateChangeListener(OnStateChangeListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener == null");
        }
        if (mStateListeners == null) {
            mStateListeners = new ArrayList<>(4);
        }
        if (!mStateListeners.contains(listener)) {
            mStateListeners.add(listener);
        }
    }

    public final void removeOnStateChangeListener(OnStateChangeListener listener) {
        if (mStateListeners != null && listener != null) {
            mStateListeners.remove(listener);
        }
    }

    public final void start() {
        if (mState == STATE_STOP) {
            resetCount();
            notifyCountDownStarted();
            execute();
        }
    }

    public final void pause() {
        if (mState == STATE_GOING) {
            mState = STATE_PAUSE;
            notifyStateChanged();
            mHandler.removeCallbacks(mRunnable);
        }
    }

    public final void resume() {
        if (mState == STATE_PAUSE) {
            execute();
        }
    }

    public final void stop() {
        if (mState != STATE_STOP) {
            mState = STATE_STOP;
            notifyStateChanged();
            notifyCountDownStopped();
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
                if (mCount < 0 || mCount > mTotalCount) {
                    stop();
                } else {
                    notifyCountDownChanged();
                    mHandler.postDelayed(mRunnable, mInterval);
                }
            }
        }
    };


    private void notifyCountDownStarted() {
        if (mCountListeners != null) {
            for (int i = 0, size = mCountListeners.size(); i < size; ++i) {
                mCountListeners.get(i).onStart();
            }
        }
    }

    private void notifyCountDownChanged() {
        if (mCountListeners != null) {
            for (int i = 0, size = mCountListeners.size(); i < size; ++i) {
                mCountListeners.get(i).onCountDown(mTotalCount, mCount);
            }
        }
    }

    private void notifyCountDownStopped() {
        if (mCountListeners != null) {
            for (int i = 0, size = mCountListeners.size(); i < size; ++i) {
                mCountListeners.get(i).onStop();
            }
        }
    }

    private void notifyStateChanged() {
        if (mStateListeners != null) {
            for (int i = 0, size = mStateListeners.size(); i < size; i++) {
                mStateListeners.get(i).onStateChanged(mState);
            }
        }
    }


    public static String stateToString(@State int state) {
        switch (state) {
            case CountTimer.STATE_GOING:
                return "GOING";
            case CountTimer.STATE_PAUSE:
                return "PAUSE";
            case CountTimer.STATE_STOP:
                return "STOP";
            default:
                throw new IllegalArgumentException("illegal state");
        }
    }


    public interface OnCountDownListener {
        /**
         * 倒计时计数开始
         *
         * @see CountTimer#start()
         */
        void onStart();

        /**
         * 倒计时计数进行中
         */
        void onCountDown(int totalCount, int currentCount);

        /**
         * 倒计时计数停止
         *
         * @see CountTimer#stop()
         */
        void onStop();
    }


    public interface OnStateChangeListener {
        /**
         * 倒计时计数状态改变时被调用
         *
         * @param state 倒计时计数的状态
         * @see CountTimer#STATE_STOP
         * @see CountTimer#STATE_PAUSE
         * @see CountTimer#STATE_GOING
         */
        void onStateChanged(@State int state);
    }
}
