package io.explod.android.emptyshell.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeakList<T> {

	private final ArrayList<WeakReference<T>> mList;

	public WeakList() {
		mList = new ArrayList<>();
	}

	public WeakList(int capacity) {
		mList = new ArrayList<>(capacity);
	}


	public int size() {
		pruneOld();
		return mList.size();
	}

	public void add(T obj) {
		synchronized (mList) {
			mList.add(new WeakReference<>(obj));
		}
	}

	public boolean remove(Object needle) {
		if (needle == null) {
			return false;
		}
		boolean found = false;
		synchronized (mList) {
			Iterator<WeakReference<T>> iterator = mList.iterator();
			while (iterator.hasNext()) {
				T obj = iterator.next().get();
				if (needle.equals(obj)) {
					iterator.remove();
					found = true;
					break;
				}
			}
		}
		return found;
	}

	public Iterator<T> iterator() {
		return new WeakListIterator();
	}

	public void pruneOld() {
		synchronized (mList) {
			for (WeakReference<T> item : mList) {
				if (item.get() == null) {
					mList.remove(item);
				}
			}
		}
	}


	public class WeakListIterator implements Iterator<T> {

		private List<WeakReference<T>> list;
		private int index;

		public WeakListIterator() {
			list = new ArrayList<>(mList);
			index = 0;
		}

		@Override
		public boolean hasNext() {
			return index < list.size();
		}

		@Override
		public T next() {
			T obj;
			do {
				obj = list.get(index++).get();
			} while (obj == null && hasNext());
			return obj;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}


}
