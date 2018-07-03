package cc.colorcat.counttimer.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cc.colorcat.counttimer.CountTimer;
import cc.colorcat.counttimer.TextCountTimer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CountTimer";

    private CountTimer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView countDownTv = findViewById(R.id.tv_count_down);
        countDownTv.setOnClickListener(this);
        mTimer = new TextCountTimer<>(countDownTv, 30, 1000);

        mTimer.addOnCountDownListener(new CountTimer.OnCountDownListener() {
            @Override
            public void onStart() {
                Log.v(TAG, "onStart");
            }

            @Override
            public void onCountDown(int totalCount, int currentCount) {
                Log.d(TAG, "onCountDown, totalCount = " + totalCount + ", currentCount = " + currentCount);
            }

            @Override
            public void onStop() {
                Log.i(TAG, "onStop");
            }
        });

        mTimer.addOnStateChangeListener(new CountTimer.OnStateChangeListener() {
            @Override
            public void onStateChanged(int state) {
                Log.e(TAG, "state changed, state = " + state + ", " + CountTimer.stateToString(state));
            }
        });

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_restart).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mTimer.start();
                break;
            case R.id.btn_pause:
                mTimer.pause();
                break;
            case R.id.btn_restart:
                mTimer.restart();
                break;
            case R.id.btn_stop:
                mTimer.stop();
                break;
            case R.id.tv_count_down:
                mTimer.start();
                break;
            default:
                break;
        }
    }
}
