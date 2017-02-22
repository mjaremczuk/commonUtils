package pl.revo.helperutils.storage;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;

public class InternalStorage extends AbstractStorage implements Storage {

	InternalStorage(Context context) {
		super(context);
	}

	@NonNull
	private String createApplicationFolder(Context context) {
		return context.getFilesDir().getAbsolutePath();
	}

	public String buildPath(String name) {
		return createApplicationFolder(context) + File.separator + name;
	}

	public String buildPath(String folderPath, String name) {
		return createApplicationFolder(context) + File.separator + folderPath + File.separator + name;
	}
}
