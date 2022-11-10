package net.lax1dude.eaglercraft.sp;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.indexeddb.IDBCountRequest;
import org.teavm.jso.indexeddb.IDBCursorRequest;
import org.teavm.jso.indexeddb.IDBCursorSource;
import org.teavm.jso.indexeddb.IDBDatabase;
import org.teavm.jso.indexeddb.IDBGetRequest;
import org.teavm.jso.indexeddb.IDBIndex;
import org.teavm.jso.indexeddb.IDBKeyRange;
import org.teavm.jso.indexeddb.IDBObjectStoreParameters;
import org.teavm.jso.indexeddb.IDBRequest;
import org.teavm.jso.indexeddb.IDBTransaction;

public abstract class IDBObjectStorePatched implements JSObject, IDBCursorSource {
	
	@JSBody(params = { "db", "name", "optionalParameters" }, script = "return db.createObjectStore(name, optionalParameters);")
	public static native IDBObjectStorePatched createObjectStorePatch(IDBDatabase db, String name, IDBObjectStoreParameters optionalParameters);
	
	@JSBody(params = { "tx", "name" }, script = "return tx.objectStore(name);")
	public static native IDBObjectStorePatched objectStorePatch(IDBTransaction tx, String name);
	
    @JSProperty
    public abstract String getName();

    @JSProperty("keyPath")
    abstract JSObject getKeyPathImpl();

    public final String[] getKeyPath() {
        JSObject result = getKeyPathImpl();
        if (JSString.isInstance(result)) {
            return new String[] { result.<JSString>cast().stringValue() };
        } else {
            return unwrapStringArray(result);
        }
    }

    @JSBody(params = { "obj" }, script = "return this;")
    private static native String[] unwrapStringArray(JSObject obj);

    @JSProperty
    public abstract String[] getIndexNames();

    @JSProperty
    public abstract boolean isAutoIncrement();

    public abstract IDBRequest put(JSObject value, JSObject key);

    public abstract IDBRequest put(JSObject value);

    public abstract IDBRequest add(JSObject value, JSObject key);

    public abstract IDBRequest add(JSObject value);

    public abstract IDBRequest delete(JSObject key);

    public abstract IDBGetRequest get(JSObject key);

    public abstract IDBRequest clear();

    public abstract IDBCursorRequest openCursor();

    public abstract IDBCursorRequest openCursor(IDBKeyRange range);

    public abstract IDBIndex createIndex(String name, String key);

    public abstract IDBIndex createIndex(String name, String[] keys);

    public abstract IDBIndex index(String name);

    public abstract void deleteIndex(String name);

    public abstract IDBCountRequest count();

    public abstract IDBCountRequest count(JSObject key);
}
