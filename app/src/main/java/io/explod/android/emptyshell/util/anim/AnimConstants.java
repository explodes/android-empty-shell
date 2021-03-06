package io.explod.android.emptyshell.util.anim;

import android.animation.ArgbEvaluator;
import android.animation.TypeEvaluator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class AnimConstants {

	public static final Interpolator INTERP_LINEAR = new LinearInterpolator();
	public static final Interpolator INTERP_ACCELERATE = new AccelerateInterpolator();
	public static final Interpolator INTERP_DECELERATE = new DecelerateInterpolator();
	public static final Interpolator INTERP_ACCEL_DECEL = new AccelerateDecelerateInterpolator();

	@SuppressWarnings("unchecked")
	public static final TypeEvaluator<Integer> EVAL_ARGB = new ArgbEvaluator();

	public static long getValueAnimationDuration(double value, double duration, double bonus) {
		value = Math.log10(value);
		value = Math.max(0, value);
		value *= bonus;
		return (long) (value + duration);
	}
}
