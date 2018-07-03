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
import android.view.View;

/**
 * Author: cxx
 * Date: 2018-07-03
 * GitHub: https://github.com/ccolorcat
 */
public class ViewCountTimer<V extends View> extends CountTimer {
    protected V mView;
    protected ViewBinder<V> mBinder;

    {
        addOnCountDownListener(updateViewStateCountDownListener());
    }

    public ViewCountTimer(@NonNull V view) {
        super();
        mView = view;
    }

    public ViewCountTimer(@NonNull V view, int totalCount) {
        super(totalCount);
        mView = view;
    }

    public ViewCountTimer(@NonNull V view, int totalCount, int intervalInMilliseconds) {
        super(totalCount, intervalInMilliseconds);
        mView = view;
    }

    public void setViewBinder(ViewBinder<V> binder) {
        mBinder = binder;
    }

   protected OnCountDownListener updateViewStateCountDownListener() {
        return new OnCountDownListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCountDown(int totalCount, int currentCount) {
                if (mBinder != null) {
                    mBinder.onBindView(mView, totalCount, currentCount);
                }
            }

            @Override
            public void onStop() {

            }
        };
    }


    public interface ViewBinder<V> {
        void onBindView(V view, int totalCount, int currentCount);
    }
}
