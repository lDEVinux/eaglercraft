package net.lax1dude.eaglercraft.glemu;

public class GLObjectMap<T> {
	private Object[] values;
	private int size;
	private int insertIndex;
	public int allocatedObjects;
	
	public GLObjectMap(int initialSize) {
		this.values = new Object[initialSize];
		this.size = initialSize;
		this.insertIndex = 0;
		this.allocatedObjects = 0;
	}

	public int register(T obj) {
		int start = insertIndex;
		do {
			++insertIndex;
			if(insertIndex >= size) {
				insertIndex = 0;
			}
			if(insertIndex == start) {
				resize();
				return register(obj);
			}
		}while(values[insertIndex] != null);
		values[insertIndex] = obj;
		++allocatedObjects;
		return insertIndex;
	}
	
	public T free(int obj) {
		if(obj >= size || obj < 0) return null;
		Object ret = values[obj];
		values[obj] = null;
		--allocatedObjects;
		return (T) ret;
	}
	
	public T get(int obj) {
		if(obj >= size || obj < 0) return null;
		return (T) values[obj];
	}
	
	private void resize() {
		int oldSize = size;
		size += size / 2;
		Object[] oldValues = values;
		values = new Object[size];
		System.arraycopy(oldValues, 0, values, 0, oldSize);
	}
}
