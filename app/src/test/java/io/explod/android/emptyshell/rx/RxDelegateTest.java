package io.explod.android.emptyshell.rx;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import io.explod.android.emptyshell.rx.NoCompleteObserver;
import io.explod.android.emptyshell.rx.RxDelegate;
import rx.Observer;
import rx.Subscription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RxDelegateTest {

	static class TesterRxDelegate<T> extends RxDelegate<T> {
		@NonNull
		@Override
		public Observer<T> getSink() {
			return super.getSink();
		}
	}

	@Test
	public void testGetSink() throws Exception {
		assertNotNull(new TesterRxDelegate<String>().getSink());
	}

	@Test
	public void testSubscribe() throws Exception {

		final AtomicInteger errorCount = new AtomicInteger(0);
		final AtomicInteger nextCount = new AtomicInteger(0);
		final Observer<String> sink = new NoCompleteObserver<String>() {
			@Override
			public void onError(Throwable e) {
				errorCount.addAndGet(1);
			}

			@Override
			public void onNext(String s) {
				nextCount.addAndGet(1);
				assertEquals(s, "cool mode");
			}
		};


		TesterRxDelegate<String> delegate = new TesterRxDelegate<>();


		Subscription subscription = delegate.getObservable().subscribe(sink);

		// send an item
		delegate.getSink().onNext("cool mode");

		// 0 errors, 1 next event
		assertEquals(1, nextCount.get());
		assertEquals(0, errorCount.get());

		// reset counters
		errorCount.set(0);
		nextCount.set(0);
		subscription.unsubscribe();

		// subscribing a 2nd time should forward the last result
		subscription = delegate.getObservable().subscribe(sink);

		// 0 errors, 1 next event
		assertEquals(1, nextCount.get());
		assertEquals(0, errorCount.get());

		// reset counters
		errorCount.set(0);
		nextCount.set(0);
		subscription.unsubscribe();

		// subscription
		subscription = delegate.getObservable().subscribe(sink);

		// send an error
		delegate.getSink().onError(new Throwable());

		// 1 errors, 1 next event
		assertEquals(1, nextCount.get()); // received on next from subscription
		assertEquals(1, errorCount.get());

		// reset counters
		errorCount.set(0);
		nextCount.set(0);
		subscription.unsubscribe();

		// subscription
		delegate.getObservable().subscribe(sink);

		// 1 errors, 1 next event
		assertEquals(0, nextCount.get()); // no next, only error
		assertEquals(1, errorCount.get());
	}


}