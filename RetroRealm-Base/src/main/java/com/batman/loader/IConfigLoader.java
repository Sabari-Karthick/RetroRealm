package com.batman.loader;


public interface IConfigLoader<K, V> {

	void load();

	V get(K key);

//	void clear();
}
