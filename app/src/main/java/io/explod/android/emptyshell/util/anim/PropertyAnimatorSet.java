package io.explod.android.emptyshell.util.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for animator sets.
 */
public class PropertyAnimatorSet {

	private AnimatorSet set;
	private List<ObjectAnimator> animations;

	public PropertyAnimatorSet() {
		this.set = new AnimatorSet();
		this.animations = new ArrayList<>();
	}

	public PropertyAnimatorSet(int capacity) {
		this.set = new AnimatorSet();
		this.animations = new ArrayList<>(capacity);
	}

	public PropertyAnimatorSet(PropertyAnimator... animators) {
		this(animators.length);
		this.add(animators);
	}

	public PropertyAnimatorSet add(PropertyAnimator animator) {
		this.animations.add(animator.anim);
		return this;
	}

	public PropertyAnimatorSet add(ObjectAnimator animator) {
		this.animations.add(animator);
		return this;
	}

	public PropertyAnimatorSet add(PropertyAnimator... animators) {
		for (PropertyAnimator animator : animators) {
			this.add(animator);
		}
		return this;
	}

	public PropertyAnimatorSet add(ObjectAnimator... animators) {
		for (ObjectAnimator animator : animators) {
			this.add(animator);
		}
		return this;
	}

	public PropertyAnimatorSet setDuration(long duration) {
		this.set.setDuration(duration);
		return this;
	}

	public PropertyAnimatorSet setInterpolator(Interpolator interpolator) {
		this.set.setInterpolator(interpolator);
		return this;
	}

	public PropertyAnimatorSet setStartDelay(long delay) {
		this.set.setStartDelay(delay);
		return this;
	}

	public PropertyAnimatorSet addListener(Animator.AnimatorListener listener) {
		this.set.addListener(listener);
		return this;
	}

	public boolean isRunning() {
		return this.set.isRunning();
	}

	public boolean isStarted() {
		return this.set.isStarted();
	}

	public PropertyAnimatorSet cancel() {
		this.set.cancel();
		return this;
	}

	public PropertyAnimatorSet start() {
		if (!this.isEmpty()) {
			final ObjectAnimator[] animations = this.animations.toArray(new ObjectAnimator[this.animations.size()]);
			this.set.playTogether(animations);
			this.set.start();
		}
		return this;
	}

	public PropertyAnimatorSet clear() {
		this.animations.clear();
		return this;
	}

	public int size() {
		return this.animations.size();
	}

	public boolean isEmpty() {
		return this.size() == 0;
	}

}
