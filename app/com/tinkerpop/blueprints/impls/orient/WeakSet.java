/*
 * Copyright since 2013 Shigeru GOUGI (sgougi@gmail.com)
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
package com.tinkerpop.blueprints.impls.orient;



import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

public class WeakSet<V> implements Set<V> {

	private WeakHashMap<V, WeakReference<V>> weakHashMap = new WeakHashMap<V, WeakReference<V>>();
	
	public WeakSet () {
		/* nop */
	}

	public boolean add(final V obj) {
		weakHashMap.put(obj, new WeakReference<V>(obj));
		return true;
	}

	public boolean addAll(final Collection<? extends V> collection) {
		final Iterator<? extends V> it = collection.iterator();
		while(it.hasNext()) {
			this.add(it.next());
		}
		return false;
	}

	public void clear() {
		this.weakHashMap.clear();
	}

	public boolean contains(final Object obj) {
		return this.weakHashMap.containsKey(obj);
	}

	public boolean containsAll(final Collection<?> collection) {
		final Iterator<?> it = collection.iterator();
		while(it.hasNext()) {
			if (! this.contains(it.next())) return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return this.weakHashMap.isEmpty();
	}

	public Iterator<V> iterator() {
		return this.weakHashMap.keySet().iterator();
	}

	public boolean remove(final Object obj) {
		final Object oldObj = this.weakHashMap.remove(obj);
		return oldObj != null;
	}

	public boolean removeAll(final Collection<?> collection) {
		final Iterator<?> it = collection.iterator();
		while(it.hasNext()) {
			this.remove(it.next());
		}
		return true;		
	}

	public boolean retainAll(Collection<?> collection) {
		throw new IllegalStateException("not implmented");
	}

	public int size() {
		return this.weakHashMap.size();
	}

	public Object[] toArray() {
		return this.weakHashMap.keySet().toArray();
	}

	public <T> T[] toArray(T[] a) {
		return this.weakHashMap.keySet().toArray(a);
	}
	
}
