package com.example;


import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Reader {

	public static final String ROOT_PATH = "excelreader/resources/";

//	public static final String FILE_NAME = "dietetycy.csv";
//	public static final String OUT_FILE_NAME = "dietetycyJson.json";
//	public static final String DIET_OUT_PATH = new File(ROOT_PATH + OUT_FILE_NAME).getAbsolutePath();

	public static final String NAME = "name";
	public static final String SHORT_DESC = "short_desc";
	public static final String DESC = "desc";
	public static final String EMAIL = "email";
	public static final String PHONE_NUMBER = "phone_number";
	public static final String CITY = "city";
	public static final String TAGS = "tags";
	public static final String AVATAR = "image_url";
	private static final String ID = "id";
	private static final String THUMBNAIL = "thumbnail_url";


	public static void main(String[] args) throws Exception {
//		csv document have to be with separator type comma ',' only
//		if we have csv with other separators - save as ods first, then while saving ods to csv format change separator to comma
//		createDietitianJson();
//		createDietitianJsonList();
		createStringResourcesFile();
	}


	private static void createDietitianJson() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter file to parse to JSON:");
		String inFile = br.readLine();
		System.out.println("Enter output file name:");
		String outFile = br.readLine();
		File in = new File(ROOT_PATH + inFile);
		File out = new File(ROOT_PATH + outFile);
		System.out.println("Input File path: " + in.getAbsolutePath());
		System.out.println("Output File will be created at: " + out.getAbsolutePath());
		CSVReader reader = new CSVReader(new FileReader(in.getAbsolutePath()));
		FileWriter output = new FileWriter(out.getAbsolutePath());
		output.write(readContent(reader.readAll()).toJSONString());
		output.flush();
		output.close();
	}


	private static void createDietitianJsonList() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter file to parse to JSON:");
		String inFile = br.readLine();
		System.out.println("Enter output file name:");
		String outFile = br.readLine();
		File in = new File(ROOT_PATH + inFile);
		File out = new File(ROOT_PATH + outFile);
		System.out.println("Input File path: " + in.getAbsolutePath());
		System.out.println("Output File will be created at: " + out.getAbsolutePath());
		CSVReader reader = new CSVReader(new FileReader(in.getAbsolutePath()));
		FileWriter output = new FileWriter(out.getAbsolutePath());
		output.write(readContentList(reader.readAll()).toJSONString());
		output.flush();
		output.close();
	}

	private static JSONArray readContent(List<String[]> entries) {
		JSONArray array = new JSONArray();
		for (int j = 1; j < entries.size(); j++) {
			JSONObject dietitianListObj = new JSONObject();
			String[] entry = entries.get(j);
			JSONObject dietitianObj = new JSONObject();
			dietitianObj.put(ID,j);
			dietitianObj.put(NAME, entry[0] + " " + entry[1]);
			dietitianObj.put(DESC, entry[3]);
			dietitianObj.put(EMAIL, entry[6]);
			dietitianObj.put(PHONE_NUMBER, entry[5]);
			dietitianObj.put(CITY, entry[4]);
			JSONArray tags = new JSONArray();
			String[] tagList = entry[7].split(";");
			tags.addAll(Arrays.asList(tagList));
			dietitianObj.put(TAGS, tags);
			String photo = String.format("%s_%s",entry[0].toLowerCase(),entry[1].toLowerCase());
			dietitianObj.put(AVATAR, photo);
			dietitianListObj.put("dietitian",dietitianObj);
			array.add(dietitianListObj);
		}

		return array;
	}


	private static JSONObject readContentList(List<String[]> entries) {
		JSONObject dietitianListObj = new JSONObject();
		JSONArray array = new JSONArray();
		for (int j = 1; j < entries.size(); j++) {
			String[] entry = entries.get(j);
			JSONObject dietitianObj = new JSONObject();
			dietitianObj.put(ID,j);
			dietitianObj.put(NAME, entry[0] + " " + entry[1]);
			dietitianObj.put(SHORT_DESC, entry[2]);
//			dietitianObj.put(DESC, entry[3]);
//			dietitianObj.put(EMAIL, entry[6]);
//			dietitianObj.put(PHONE_NUMBER, entry[5]);
//			dietitianObj.put(CITY, entry[4]);
			JSONArray tags = new JSONArray();
			String[] tagList = entry[7].split(";");
			tags.addAll(Arrays.asList(tagList));
			dietitianObj.put(TAGS, tags);
			String photo = String.format("%s_%s",entry[0].toLowerCase(),entry[1].toLowerCase());
			dietitianObj.put(THUMBNAIL, photo);
			array.add(dietitianObj);
		}
		dietitianListObj.put("dietitians",array);
		return dietitianListObj;
	}

	private static void createStringResourcesFile() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter file to parse to JSON:");
		String inFile = br.readLine();
		System.out.println("Enter output file name:");
		String outFile = br.readLine();
		File in = new File(ROOT_PATH + inFile);
		File out = new File(ROOT_PATH + outFile);
		System.out.println("Input File path: " + in.getAbsolutePath());
		System.out.println("Output File will be created at: " + out.getAbsolutePath());
		CSVReader reader = new CSVReader(new FileReader(in.getAbsolutePath()));
		FileWriter output = new FileWriter(out.getAbsolutePath());
		output.write(readTranslations(reader.readAll()));
		output.flush();
		output.close();
	}

	private static String readTranslations(List<String[]> entries) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < entries.size(); i++) {
			String[] entry = entries.get(i);
//			builder.append("\"%s\" = \"%s\"",);
			builder.append(String.format(Locale.ENGLISH, "<string name=\"%s\">%s</string>\n", entry[0], entry[1]));
		}
		return builder.toString();
	}
}
