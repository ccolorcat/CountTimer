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
public class TextCountTimer<V extends TextView> extends ViewCountTimer<V> {

    public TextCountTimer(@NonNull V view) {
        super(view);
    }

    public TextCountTimer(@NonNull V view, int totalCount) {
        super(view, totalCount);
    }

    public TextCountTimer(@NonNull V view, int totalCount, int intervalInMilliseconds) {
        super(view, totalCount, intervalInMilliseconds);
    }

    @Override
    protected OnCountDownListener updateViewStateCountDownListener() {
        return new OnCountDownListener() {
            private CharSequence mTextBack;
            private boolean mEnabledBack;

            @Override
            public void onStart() {
                mTextBack = mView.getText();
                mEnabledBack = mView.isEnabled();
                mView.setEnabled(false);
            }

            @Override
            public void onCountDown(int totalCount, int currentCount) {
                if (mBinder != null) {
                    mBinder.onBindView(mView, totalCount, currentCount);
                } else {
                    mView.setText(String.valueOf(currentCount));
                }
            }

            @Override
            public void onStop() {
                if (mTextBack != null) {
                    mView.setText(mTextBack);
                }
                mView.setEnabled(mEnabledBack);
            }
        };
    }
}
