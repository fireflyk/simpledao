package com.codinghero.sql.util.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.codinghero.sql.util.BeanUtils;

public class AutoArrayList<E> implements List<E> {

	private ArrayList<E> arrayList; 
	private Class<E> itemClazz;

	public AutoArrayList(Class<E> itemClazz) {
		this.arrayList = new ArrayList<E>();
		this.itemClazz = itemClazz;
	}

	public E get(int index) {
		while (index >= arrayList.size())
			arrayList.add(BeanUtils.newInstance(itemClazz));
//		if (index < arrayList.size())
			return arrayList.get(index);
//		else
//			return BeanUtils.newInstance(itemClazz);
	}
	
	//--- use arrayList method
	
	public int size() {
		return arrayList.size();
	}

	public boolean isEmpty() {
		return arrayList.isEmpty();
	}

	public boolean contains(Object o) {
		return arrayList.contains(o);
	}

	public Iterator<E> iterator() {
		return arrayList.iterator();
	}

	public Object[] toArray() {
		return arrayList.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return arrayList.toArray(a);
	}

	public boolean add(E o) {
		return arrayList.add(o);
	}

	public boolean remove(Object o) {
		return arrayList.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return arrayList.containsAll(c);
	}

	public boolean addAll(Collection<? extends E> c) {
		return arrayList.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return arrayList.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return arrayList.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return arrayList.retainAll(c);
	}

	public void clear() {
		arrayList.clear();
	}

	public E set(int index, E element) {
		return arrayList.set(index, element);
	}

	public void add(int index, E element) {
		arrayList.add(index, element);
	}

	public E remove(int index) {
		return arrayList.remove(index);
	}

	public int indexOf(Object o) {
		return arrayList.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return arrayList.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return arrayList.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return arrayList.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return arrayList.subList(fromIndex, toIndex);
	}
}
