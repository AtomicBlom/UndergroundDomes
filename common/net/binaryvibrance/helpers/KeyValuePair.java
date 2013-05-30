package net.binaryvibrance.helpers;

public class KeyValuePair<TKey, TValue> {
	public final TKey key;
	public final TValue value;

	public KeyValuePair(TKey key, TValue value) {
		this.key = key;
		this.value = value;
	}

}
