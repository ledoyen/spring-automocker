package com.github.ledoyen.automocker.tools;

public class Tuple<U, V> {

	private final U _1;
	private final V _2;

	private Tuple(U u, V v) {
		this._1 = u;
		this._2 = v;
	}

	public static <U, V> Tuple<U, V> of(U u, V v) {
		return new Tuple<U, V>(u, v);
	}

	public U _1() {
		return _1;
	}

	public V _2() {
		return _2;
	}
}
