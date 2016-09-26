package pl.revo.commonhelpers;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class SimpleEmojiManager {
	private static final Pattern ALIAS_CANDIDATE_PATTERN =
			Pattern.compile("(?<=:)\\+?(\\w|\\||\\-)+(?=:)");
	static Map<String, String> EMOJIS = new HashMap<>();

	public static void initialize(Context context) {
		try {
			InputStream inputStream = context.getAssets().open("emoji.json");
			loadEmojis(inputStream);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadEmojis(InputStream stream) throws Exception {
		JSONObject emojiJSON = new JSONObject(inputStreamToString(stream));
		Map<String, String> emojis = new HashMap<>();
		Iterator<String> keyIter = emojiJSON.keys();
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			EMOJIS.put(key, (String) emojiJSON.get(key));
		}
	}

	private static String inputStreamToString(InputStream stream) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String read;
		while ((read = br.readLine()) != null) {
			sb.append(read);
		}
		br.close();
		return sb.toString();
	}

	public static String parseToUnicode(String string) {
		List<String> candidates = getAliasCandidates(string);
		String result = string;
		for (String candidate : candidates) {
			String replacement = EMOJIS.get(candidate);
			result = result.replace(candidate,replacement);
		}
		return result;
	}

	public static String parseToAliases(String input){
		String result = input;
		for (String key : EMOJIS.keySet()) {
			String emoti = EMOJIS.get(key);
			if(result.contains(emoti)){
				result = result.replace(emoti,key);
			}
		}
		return result;
	}

	private static List<String> getAliasCandidates(String input) {
		List<String> candidates = new ArrayList<>();
		Matcher matcher = ALIAS_CANDIDATE_PATTERN.matcher(input);
		matcher = matcher.useTransparentBounds(true);
		while (matcher.find()) {
			String match = matcher.group();
			candidates.add(":"+match+":");
		}
		return candidates;
	}
}
