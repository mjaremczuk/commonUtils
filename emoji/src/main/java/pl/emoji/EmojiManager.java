package pl.emoji;

import android.content.Context;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;


public class EmojiManager {
	private static final String EMOJI_PATH = "res/raw/emoji.json";
	private static final Pattern ALIAS_CANDIDATE_PATTERN = Pattern.compile("(?<=:)\\+?(\\w|\\||\\-)+(?=:)");
	private static final String EMOJI_FILE = "emoji.json";
	static Map<String, String> EMOJIS = new HashMap<>();

	/**
	 * Static loading emojis will be called before initializing
	 * so if somebody would like to deliver his own json file with emoticons in format:
	 * @<code>
	 *     {
	 *      ":grinning_face:": "😀",
	 *		":grinning_face_with_smiling_eyes:": "😁",
	 *		...
	 *     }
	 * </code>
	 */
	static {
		try {
			InputStream stream = EmojiManager.class.getClassLoader().getResourceAsStream(EMOJI_PATH);
			loadEmojis(stream);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void initialize(Context context) {
		initialize(context, EMOJI_FILE);
	}

	public static void initialize(Context context, String filename) {
		try {
			InputStream inputStream = context.getAssets().open(filename);
			loadEmojis(inputStream);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadEmojis(InputStream stream) throws Exception {
		JSONObject emojiJSON = new JSONObject(inputStreamToString(stream));
		Iterator<String> keyIter = emojiJSON.keys();
		EMOJIS.clear();
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
			if (replacement != null) {
				result = result.replace(candidate, replacement);
			}
		}
		return result;
	}

	public static String parseToAliases(String input) {
		String result = input;
		List<String> keySet = new ArrayList<>(EMOJIS.keySet());
		Collections.reverse(keySet);
//		for (String key : EMOJIS.keySet()) {
		for (String key : keySet) {
			String emoti = EMOJIS.get(key);
			if (result.contains(emoti)) {
				result = result.replace(emoti, key);
			}
		}
		return result;
	}

	public static Observable<String> rxParseToAliases(String input) {
		return Observable.just(parseToAliases(input))
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread());
	}

	public static Observable<String> rxParseToUnicode(String input) {
		return Observable.just(parseToUnicode(input))
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread());
	}

	private static List<String> getAliasCandidates(String input) {
		List<String> candidates = new ArrayList<>(1);
		Matcher matcher = ALIAS_CANDIDATE_PATTERN.matcher(input);
		matcher = matcher.useTransparentBounds(true);
		while (matcher.find()) {
			String match = matcher.group();
			candidates.add(":" + match + ":");
		}
		return candidates;
	}
}
