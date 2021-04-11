package id.ac.umn.effendi.andrio.smallchange;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarAnimation extends Animation {
    private ProgressBar progressBar;
    private TextView textView;
    private float from;
    private float  to;

    public ProgressBarAnimation(ProgressBar progressBar, TextView textView, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.textView = textView;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
        textView.setText((int)value + "%");
    }

}