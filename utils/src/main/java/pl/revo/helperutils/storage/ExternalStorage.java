package pl.revo.helperutils.storage;

import android.content.Context;
import android.os.Environment;
import java.io.File;

public class ExternalStorage extends AbstractStorage implements Storage {

	private final boolean readOnly;

	public ExternalStorage(Context context, boolean readOnly) {
		super(context);
		this.readOnly = readOnly;
	}

	private String createApplicationFolder(Context context) {
		String filePath =
				Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName();
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		return filePath;
	}

	public String buildPath(String name) {
		return createApplicationFolder(context) + File.separator + name;
	}

	public String buildPath(String folderPath, String name) {
		return createApplicationFolder(context) + File.separator + folderPath + File.separator + name;
	}
}
