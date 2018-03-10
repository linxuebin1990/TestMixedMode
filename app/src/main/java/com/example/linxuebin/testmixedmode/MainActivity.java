package com.example.linxuebin.testmixedmode;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends Activity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    private Button mModeTextBtn;
    private SeekBar mSeekBar;
    private TestMixedModeView mTestMixedModeView;
    private static final String[] DRAW_MODEL = new String[]{
            "normal", "bitmap"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mModeTextBtn = (Button) findViewById(R.id.draw_model_select);
        mSeekBar = (SeekBar) findViewById(R.id.mixed_mode_seek_bar);
        mTestMixedModeView = (TestMixedModeView) findViewById(R.id.mixed_mode_draw_view);
        mSeekBar.setProgress(100);
        mSeekBar.setOnSeekBarChangeListener(this);
        mModeTextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mModeTextBtn)) {
            mModeTextBtn.setSelected(!mModeTextBtn.isSelected());
            mModeTextBtn.setText(mModeTextBtn.isSelected() ? DRAW_MODEL[1] : DRAW_MODEL[0]);
            mTestMixedModeView.setDrawByBitmap(mModeTextBtn.isSelected());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTestMixedModeView.setAlpha(progress / 100f);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
