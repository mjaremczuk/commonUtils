package pl.revo.helperutils.storage;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

abstract class AbstractStorage {

	Context context;

	public abstract String buildPath(String name);

	public abstract String buildPath(String folderPath, String filename);

	AbstractStorage(Context context) {
		this.context = context;
	}

	private boolean createFileInternal(String filePath, byte[] content) {
		try {
			OutputStream stream = new FileOutputStream(new File(filePath));
			stream.write(content);
			stream.flush();
			stream.close();
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to create", e);
		}
		return true;
	}

	public String createFolder(String name) {
		String filePath = buildPath(name);
		File file = new File(filePath);
		boolean created = file.mkdirs();
		return created ? filePath : null;
	}

	public File createFile(String filename, String content) {
		String path = buildPath(filename);
		boolean created = createFileInternal(path, content.getBytes());
		File file = new File(path);
		return created ? file : null;
	}

	public File createFile(String directoryPath, String filename, String content) {
		String path = buildPath(directoryPath, filename);
		createFolder(directoryPath);
		boolean created = createFileInternal(path, content.getBytes());
		File file = new File(path);
		return created ? file : null;
	}

	public boolean deleteFolder(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			if (files == null) {
				return dir.delete();
			}
			for (File file : files) {
				if (file.isDirectory()) {
					deleteFolder(file.getAbsolutePath());
				}
				else {
					file.delete();
				}
			}
		}
		return dir.delete();
	}

	public List<File> getFolderFiles(String path) {
		File dir = new File(buildPath(path));
		return Arrays.asList(dir.listFiles());
	}
}
