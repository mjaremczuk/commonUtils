package pl.revo.helperutils.storage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.util.SparseArray;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static pl.revo.helperutils.storage.StorageManager.StorageType.EXTERNAL;
import static pl.revo.helperutils.storage.StorageManager.StorageType.INTERNAL;

public class StorageManager {

	static SparseArray<Storage> storage = new SparseArray<>();
	static
	@StorageType
	int type;

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({StorageAvailable.AVAILABLE, StorageAvailable.READABLE_ONLY, StorageAvailable.UNAVAILABLE})
	public @interface StorageAvailable {
		int AVAILABLE = 1;

		int READABLE_ONLY = 2;

		int UNAVAILABLE = 3;
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({StorageType.EXTERNAL, StorageType.INTERNAL})
	public @interface StorageType {
		int EXTERNAL = 1;

		int INTERNAL = 2;
	}

	private static
	@StorageAvailable
	int isExternalStorageWritable() {
		@StorageAvailable int mExternalStorageAvailable;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = StorageAvailable.AVAILABLE;
		}
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = StorageAvailable.READABLE_ONLY;
		}
		else {
			mExternalStorageAvailable = StorageAvailable.UNAVAILABLE;
		}
		return mExternalStorageAvailable;
	}

	public static void init(Context context, @StorageType int storageType) {
		String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
		int res = context.checkCallingOrSelfPermission(permission);
		if (res != PackageManager.PERMISSION_GRANTED) {
			throw new NullPointerException("Permission "+ permission +" not granted exception");
		}
		if (storageType == INTERNAL) {
			storage.put(INTERNAL, getInternalStorage(context));
		}
		else {
			storage.put(EXTERNAL, getExternalStorage(context));
		}
		type = storageType;
	}

	public static Storage getStorage() {
		return get(type);
	}

	public static Storage getStorage(@StorageType int storageType) {
		return get(storageType);
//		if (storageType == INTERNAL) {
//			return getInternalStorage();
//		}
//		else {
//			return getExternalStorage();
//		}
	}

	private static Storage get(@StorageType int type){
		 Storage  store = storage.get(type);
		if(store == null){
			throw new NullPointerException("This type of storage was not initialized");
		}
		return store;
	}

	private static Storage getInternalStorage(Context context) {
		if (storage.get(INTERNAL) == null) {
			storage.put(INTERNAL, new InternalStorage(context));

		}
		return storage.get(INTERNAL);
	}

	private static Storage getExternalStorage(Context context) {
		if (storage.get(EXTERNAL) == null) {
			if (isExternalStorageWritable() != StorageAvailable.UNAVAILABLE) {
				storage.put(EXTERNAL,
						new ExternalStorage(context, isExternalStorageWritable() == StorageAvailable.READABLE_ONLY));
			}
			else {
				throw new NullPointerException("External storage not available exception");
			}
		}
		return storage.get(EXTERNAL);
	}
}
