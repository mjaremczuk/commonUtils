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
//		createStringResourcesFile();
		createiOSDictionaryResources();
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

	private static void createiOSDictionaryResources() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter output file name:");
		String outFile = br.readLine();
		File out = new File(ROOT_PATH + outFile);
		System.out.println("Enter output file name: " + out.getAbsolutePath());
		String data = "\"Other\" = \"Otro\";\n" +
				"\"AF\" = \"Afganistán\";\n" +
				"\"AL\" = \"Albania\";\n" +
				"\"DZ\" = \"Argelia\";\n" +
				"\"AD\" = \"Andorra\";\n" +
				"\"AO\" = \"Angola\";\n" +
				"\"AI\" = \"Anguilla\";\n" +
				"\"AQ\" = \"Antártida\";\n" +
				"\"AG\" = \"Antigua y Barbuda\";\n" +
				"\"SA\" = \"Arabia Saudita\";\n" +
				"\"AR\" = \"Argentina\";\n" +
				"\"AM\" = \"Armenia\";\n" +
				"\"AW\" = \"Aruba\";\n" +
				"\"AU\" = \"Australia\";\n" +
				"\"AT\" = \"Austria\";\n" +
				"\"AZ\" = \"Azerbaiyán\";\n" +
				"\"BS\" = \"Bahamas\";\n" +
				"\"BH\" = \"Bahrain\";\n" +
				"\"BD\" = \"Bangladesh\";\n" +
				"\"BB\" = \"Barbados\";\n" +
				"\"BE\" = \"Bélgica\";\n" +
				"\"BZ\" = \"Belice\";\n" +
				"\"BJ\" = \"Benin\";\n" +
				"\"BM\" = \"Bermuda\";\n" +
				"\"BT\" = \"Bhutan\";\n" +
				"\"BY\" = \"Bielorrusia\";\n" +
				"\"BO\" = \"Bolivia\";\n" +
				"\"BQ\" = \"Bonaire, Sint Eustatius y Saba\";\n" +
				"\"BA\" = \"Bosnia y Herzegovina\";\n" +
				"\"BW\" = \"Botswana\";\n" +
				"\"BR\" = \"Brazil\";\n" +
				"\"BN\" = \"Brunei Darussalam\";\n" +
				"\"BG\" = \"Bulgaria\";\n" +
				"\"BF\" = \"Burkina Faso\";\n" +
				"\"BI\" = \"Burundi\";\n" +
				"\"XC\" = \"Ceuta\";\n" +
				"\"CL\" = \"Chile\";\n" +
				"\"CN\" = \"China\";\n" +
				"\"CW\" = \"Curaçao\";\n" +
				"\"HR\" = \"Croacia\";\n" +
				"\"CY\" = \"Chipre\";\n" +
				"\"TD\" = \"Chad\";\n" +
				"\"ME\" = \"Montenegro\";\n" +
				"\"DK\" = \"Dinamarca\";\n" +
				"\"DM\" = \"Dominica\";\n" +
				"\"DO\" = \"República Dominicana\";\n" +
				"\"DJ\" = \"Djibouti\";\n" +
				"\"EG\" = \"Egipto\";\n" +
				"\"EC\" = \"Ecuador\";\n" +
				"\"ER\" = \"Eritrea\";\n" +
				"\"EE\" = \"Estonia\";\n" +
				"\"ET\" = \"Etiopía\";\n" +
				"\"FK\" = \"Islas Malvinas (Falkland)\";\n" +
				"\"FJ\" = \"Fiji\";\n" +
				"\"PH\" = \"Filipinas\";\n" +
				"\"FI\" = \"Finland\";\n" +
				"\"FR\" = \"Finlandia\";\n" +
				"\"GA\" = \"Gabón\";\n" +
				"\"GM\" = \"Gambia\";\n" +
				"\"GH\" = \"Ghana\";\n" +
				"\"GI\" = \"Gibraltar\";\n" +
				"\"GR\" = \"Grecia\";\n" +
				"\"GD\" = \"Granada\";\n" +
				"\"GL\" = \"Greenland\";\n" +
				"\"GE\" = \"Georgia\";\n" +
				"\"GU\" = \"Guam\";\n" +
				"\"GY\" = \"Guayana\";\n" +
				"\"GT\" = \"Guatemala\";\n" +
				"\"GN\" = \"Guinea\";\n" +
				"\"GQ\" = \"Guinea Ecuatorial\";\n" +
				"\"GW\" = \"Guinea-Bissau\";\n" +
				"\"HT\" = \"Haití\";\n" +
				"\"ES\" = \"España\";\n" +
				"\"HN\" = \"Honduras\";\n" +
				"\"HK\" = \"Hong Kong\";\n" +
				"\"IN\" = \"India\";\n" +
				"\"ID\" = \"Indonesia\";\n" +
				"\"IQ\" = \"Irak\";\n" +
				"\"IR\" = \"Irán, República Islámica de\";\n" +
				"\"IE\" = \"Irlanda\";\n" +
				"\"IS\" = \"Islandia\";\n" +
				"\"IL\" = \"Israel\";\n" +
				"\"JM\" = \"Jamaica\";\n" +
				"\"JP\" = \"Japón\";\n" +
				"\"YE\" = \"Yemen\";\n" +
				"\"JO\" = \"Jordan\";\n" +
				"\"KY\" = \"Islas Caimán\";\n" +
				"\"KH\" = \"Camboya\";\n" +
				"\"CM\" = \"Camerún\";\n" +
				"\"CA\" = \"Canadá\";\n" +
				"\"QA\" = \"Katar\";\n" +
				"\"KZ\" = \"Kazajstán\";\n" +
				"\"KE\" = \"Kenia\";\n" +
				"\"KG\" = \"Kirguistán\";\n" +
				"\"KI\" = \"Kiribati\";\n" +
				"\"CO\" = \"Colombia\";\n" +
				"\"KM\" = \"Comoras\";\n" +
				"\"CG\" = \"Congo (Rep.)\";\n" +
				"\"CD\" = \"Congo (Rep. Dem.)\";\n" +
				"\"KP\" = \"Corea, República Popular Democrática de\";\n" +
				"\"XK\" = \"Kosovo\";\n" +
				"\"CR\" = \"Costa Rica\";\n" +
				"\"CU\" = \"Cuba\";\n" +
				"\"KW\" = \"Kuwait\";\n" +
				"\"LA\" = \"Lao, República Democrática Popular de\";\n" +
				"\"LS\" = \"Lesoto\";\n" +
				"\"LB\" = \"Líbano\";\n" +
				"\"LR\" = \"Liberia\";\n" +
				"\"LY\" = \"Jamahiriya Árabe Libia\";\n" +
				"\"LI\" = \"Liechtenstein\";\n" +
				"\"LT\" = \"Lituania\";\n" +
				"\"LU\" = \"Luxemburgo\";\n" +
				"\"LV\" = \"Letonia\";\n" +
				"\"MK\" = \"Macedonia, la ex República Yugoslava de\";\n" +
				"\"MG\" = \"Madagascar\";\n" +
				"\"YT\" = \"Mayotte\";\n" +
				"\"MO\" = \"Macao\";\n" +
				"\"MW\" = \"Malawi\";\n" +
				"\"MV\" = \"Maldivas\";\n" +
				"\"MY\" = \"Malasia\";\n" +
				"\"ML\" = \"Malí\";\n" +
				"\"MT\" = \"Malta\";\n" +
				"\"MP\" = \"Islas Marianas del Norte\";\n" +
				"\"MA\" = \"Marruecos\";\n" +
				"\"MR\" = \"Mauritania\";\n" +
				"\"MU\" = \"Mauricio\";\n" +
				"\"MX\" = \"México\";\n" +
				"\"XL\" = \"Melilla\";\n" +
				"\"FM\" = \"Micronesia, Estados Federados de\";\n" +
				"\"UM\" = \"Islas menores alejadas de los Estados Unidos\";\n" +
				"\"MD\" = \"Moldova, República de\";\n" +
				"\"MN\" = \"Mongolia\";\n" +
				"\"MS\" = \"Montserrat\";\n" +
				"\"MZ\" = \"Mozambique\";\n" +
				"\"MM\" = \"Myanmar\";\n" +
				"\"NA\" = \"Namibia\";\n" +
				"\"NR\" = \"Nauru\";\n" +
				"\"NP\" = \"Nepal\";\n" +
				"\"NL\" = \"Países Bajos\";\n" +
				"\"DE\" = \"Alemania\";\n" +
				"\"NE\" = \"Níger\";\n" +
				"\"NG\" = \"Nigeria\";\n" +
				"\"NI\" = \"Nicaragua\";\n" +
				"\"NU\" = \"Niue\";\n" +
				"\"NF\" = \"Isla Norfolk\";\n" +
				"\"NO\" = \"Noruega\";\n" +
				"\"NC\" = \"Nueva Caledonia\";\n" +
				"\"NZ\" = \"Nueva Zelanda\";\n" +
				"\"PS\" = \"Palestina, Territorio Ocupado\";\n" +
				"\"OM\" = \"Omán\";\n" +
				"\"PK\" = \"Pakistán\";\n" +
				"\"PW\" = \"Palau\";\n" +
				"\"PA\" = \"Panamá\";\n" +
				"\"PG\" = \"Papua New Guinea\";\n" +
				"\"PY\" = \"Paraguay\";\n" +
				"\"PE\" = \"Perú\";\n" +
				"\"PN\" = \"Pitcairn\";\n" +
				"\"PF\" = \"Polinesia francés\";\n" +
				"\"PL\" = \"Polonia\";\n" +
				"\"GS\" = \"Georgia del Sur y Sandwich del Sur Islas\";\n" +
				"\"PT\" = \"Portugal\";\n" +
				"\"CZ\" = \"República Checa\";\n" +
				"\"KR\" = \"Corea, República de\";\n" +
				"\"ZA\" = \"Sudáfrica\";\n" +
				"\"CF\" = \"República Centroafricana\";\n" +
				"\"RU\" = \"Federación de Rusia\";\n" +
				"\"RW\" = \"Rwanda\";\n" +
				"\"EH\" = \"Sáhara Occidental\";\n" +
				"\"BL\" = \"San Bartolomé\";\n" +
				"\"RO\" = \"Romania\";\n" +
				"\"SV\" = \"EI Salvador\";\n" +
				"\"WS\" = \"Samoa\";\n" +
				"\"AS\" = \"Samoa Americana\";\n" +
				"\"SM\" = \"San Marino\";\n" +
				"\"SN\" = \"Senegal\";\n" +
				"\"XS\" = \"Serbia\";\n" +
				"\"SC\" = \"Seychelles\";\n" +
				"\"SL\" = \"Sierra Leona\";\n" +
				"\"SG\" = \"Singapur\";\n" +
				"\"SZ\" = \"Swazilandia\";\n" +
				"\"SK\" = \"Eslovaquia\";\n" +
				"\"SI\" = \"Eslovenia\";\n" +
				"\"SO\" = \"Somalia\";\n" +
				"\"LK\" = \"Sri Lanka\";\n" +
				"\"PM\" = \"San Pedro y Miquelón\";\n" +
				"\"KN\" = \"St.Kittsand y Nieves\";\n" +
				"\"LC\" = \"Santa Lucía\";\n" +
				"\"VC\" = \"San Vicente y las Granadinas\";\n" +
				"\"US\" = \"Estados Unidos de América\";\n" +
				"\"SD\" = \"Sudán\";\n" +
				"\"SS\" = \"Sudán del Sur\";\n" +
				"\"SR\" = \"Suriname\";\n" +
				"\"SY\" = \"República Árabe Siria\";\n" +
				"\"CH\" = \"Suiza\";\n" +
				"\"SE\" = \"Sweden\";\n" +
				"\"SH\" = \"St.Helena\";\n" +
				"\"TJ\" = \"Tayikistán\";\n" +
				"\"TH\" = \"Tailandia\";\n" +
				"\"TW\" = \"Taiwán, Provice China\";\n" +
				"\"TZ\" = \"Tanzania\";\n" +
				"\"TG\" = \"Togo\";\n" +
				"\"TK\" = \"Tokelau\";\n" +
				"\"TO\" = \"Tonga\";\n" +
				"\"TT\" = \"Trinidad y Tobago\";\n" +
				"\"TN\" = \"Túnez\";\n" +
				"\"TR\" = \"Turquía\";\n" +
				"\"TM\" = \"Turkmenistán\";\n" +
				"\"TC\" = \"Turcas y Caicos\";\n" +
				"\"TV\" = \"Tuvalu\";\n" +
				"\"UG\" = \"Uganda\";\n" +
				"\"UA\" = \"Ucrania\";\n" +
				"\"UY\" = \"Uruguay\";\n" +
				"\"UZ\" = \"Uzbekistán\";\n" +
				"\"VU\" = \"Vanuatu\";\n" +
				"\"WF\" = \"WalIis y Futuna\";\n" +
				"\"VA\" = \"Ciudad del Vaticano\";\n" +
				"\"VE\" = \"Venezuela\";\n" +
				"\"HU\" = \"Hungría\";\n" +
				"\"GB\" = \"Reino Unido\";\n" +
				"\"VN\" = \"Vietnam\";\n" +
				"\"IT\" = \"Italia\";\n" +
				"\"TL\" = \"Timor Leste\";\n" +
				"\"CI\" = \"Cote D'Ivoire\";\n" +
				"\"BV\" = \"Bouvet Isla de\";\n" +
				"\"CX\" = \"Islas de Navidad\";\n" +
				"\"CK\" = \"Islas Cook\";\n" +
				"\"VI\" = \"Islas Vírgenes de (USA)\";\n" +
				"\"VG\" = \"Islas Vírgenes de British)\";\n" +
				"\"HM\" = \"Islas Heard y McDonald\";\n" +
				"\"CC\" = \"Islas Cocos (Keeling)\";\n" +
				"\"FO\" = \"Islas Feroe\";\n" +
				"\"MH\" = \"Islas Marshall \";\n" +
				"\"SB\" = \"Islas Salomón\";\n" +
				"\"SX\" = \"Sint Maarten (parte neerlandesa)\";\n" +
				"\"ST\" = \"Santo Tomé y Príncipe\";\n" +
				"\"ZM\" = \"Zambia\";\n" +
				"\"CV\" = \"Cabo Verde\";\n" +
				"\"ZW\" = \"Zimbabue\";\n" +
				"\"AE\" = \"Emiratos Árabes Unidos\";";
		StringBuilder builder = new StringBuilder();
		String[] rows = data.split("\n");
		for (String row : rows) {
			String[] newRow = row.replace(";","").replace("\"","").split(" = ");
			builder.append(String.format("<key>%s</key>\n<string>%s</string>\n\n",newRow[0],newRow[1]));
		}
//		File out = new File(ROOT_PATH + "country_en.txt");
		FileWriter output = new FileWriter(out.getAbsolutePath());
		output.write(builder.toString());
		output.flush();
	}
}
