package pl.revo.helperutils.storage;

import java.io.File;
import java.util.List;

public interface Storage {
	// TODO: 28.11.2016 implement possibility of creating folde/file outside application folder "Android/data/package.name
	String createFolder(String name);
	File createFile(String filename,String content);
	File createFile(String directoryPath,String filename,String content);
	boolean deleteFolder(String path);
	List<File> getFolderFiles(String path);
}
