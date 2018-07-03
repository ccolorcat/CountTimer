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

import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * Author: cxx
 * Date: 2018-07-03
 * GitHub: https://github.com/ccolorcat
 */
public class SimpleCountTimer<V extends TextView> extends CountTimer<V> {

    {
        super.setOnStateChangeListener(new OnStateChangeListener<V>() {
            @Override
            public void onStateChange(@NonNull V v, int state) {
                final V view = v;
                final int currentState = state;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.setEnabled(currentState == CountTimer.STATE_CANCEL);
                    }
                });
            }
        });
    }

    public SimpleCountTimer(@NonNull V v) {
        super(v);
    }

    public SimpleCountTimer(@NonNull V v, int totalCount) {
        super(v, totalCount);
    }

    public SimpleCountTimer(@NonNull V v, int totalCount, int intervalInMilliseconds) {
        super(v, totalCount, intervalInMilliseconds);
    }

    /**
     * 请勿调用此方法，否则会抛异常, 如需设置状态监听, 请使用 CountTimer
     *
     * @param listener 倒计时计数状态的监听
     * @throws UnsupportedOperationException 调用此方法即抛出此异常
     * @deprecated 建议使用 CountTimer 以设置状态监听
     */
    @Override
    public void setOnStateChangeListener(OnStateChangeListener<V> listener) {
        throw new UnsupportedOperationException("SimpleCountTimer can not set OnStateChangeListener.");
    }
}
