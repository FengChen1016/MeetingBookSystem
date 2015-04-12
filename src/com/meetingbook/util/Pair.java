package com.meetingbook.util;

public class Pair<K, V> {
	private K first;
	private V second;
	
	public Pair(K first, V second) {
		this.first = first;
		this.second = second;
	}
	
	public K getFirst() {
		return first;
	}
	
	public V getSecond() {
		return second;
	}
	
	public static <K, V> Pair<K, V> create(K first, V second) {
		return new Pair<K, V>(first, second);
	}
	
	@Override
	public String toString() {
		return "(" + first.toString() + ":" + second.toString() + ")";
	}

}
