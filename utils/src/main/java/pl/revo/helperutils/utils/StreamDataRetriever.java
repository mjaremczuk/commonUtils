package pl.revo.helperutils.utils;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.revo.helperutils.model.RadioStreamData;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StreamDataRetriever {

	RadioStreamData radioStreamData;

	public Observable<String> fetchStreamData(String url) throws MalformedURLException {
		URL radioURL = new URL(url);
		return Observable.create((OnSubscribe<String>) subscriber -> {
//			new Handler().postDelayed(() -> subscriber.onNext(getTrackDetails(radioURL)), 5000);
			subscriber.onNext(getTrackDetails(radioURL));
		}).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
	}

	public String getTrackDetails(URL streamUrl) {
		radioStreamData = new RadioStreamData();
		Map<String, String> metadata;
		String strTitle = "";
		String strArtist = "";
		try {
			strTitle = executeToFetchData(streamUrl);
//			if (metadata != null) {
//				String streamHeading = "";
//				Map<String, String> data = metadata;
//				if (data != null && data.containsKey("StreamTitle")) {
//					strArtist = data.get("StreamTitle");
//					streamHeading = strArtist;
//				}
//				if (!TextUtils.isEmpty(strArtist) && strArtist.contains("-")) {
//					strArtist = strArtist.substring(0, strArtist.indexOf("-"));
//					trackData.artist = strArtist.trim();
//				}
//				if (!TextUtils.isEmpty(streamHeading)) {
//					if (streamHeading.contains("-")) {
//						strTitle = streamHeading.substring(streamHeading
//								.indexOf("-") + 1);
//						trackData.title = strTitle.trim();
//					}
//				}
//			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return strTitle;
	}

	private URLConnection con;
	private InputStream stream;
	private List<String> headerList;

	private String executeToFetchData(URL streamUrl) throws IOException {
		Map<String, String> metadata = new HashMap<>();
		String data = null;

		try {
			con = streamUrl.openConnection();
			con.setRequestProperty("Icy-MetaData", "3");
			// con.setRequestProperty("Connection", "close");
			// con.setRequestProperty("Accept", null);
			con.connect();
			int metaDataOffset = 0;
			Map<String, List<String>> headers = con.getHeaderFields();
			stream = con.getInputStream();

			if (headers.containsKey("icy-metaint")) {
				headerList = headers.get("icy-metaint");
				if (headerList != null) {
					if (headerList.size() > 0) {
						metaDataOffset = Integer.parseInt(headers.get(
								"icy-metaint").get(0));
					}
					else
						return null;
				}
				else
					return null;

			}
			else {
				return null;

			}

			// In case no data was sent
			if (metaDataOffset == 0) {
				return null;
			}

			// Read metadata
			int b;
			int count = 0;
			int metaDataLength = 4080; // 4080 is the max length
			boolean inData = false;
			StringBuilder metaData = new StringBuilder();
			while ((b = stream.read()) != -1) {
				count++;
				if (count == metaDataOffset + 1) {
					metaDataLength = b * 16;
				}
				if (count > metaDataOffset + 1
						&& count < (metaDataOffset + metaDataLength)) {
					inData = true;
				}
				else {
					inData = false;
				}
				if (inData) {
					if (b != 0) {
						metaData.append((char) b);
					}
				}
				if (count > (metaDataOffset + metaDataLength)) {
					break;
				}

			}
			data = metaData.toString();
			metadata = parsingMetadata(metaData.toString());
			stream.close();
		}
		catch (Exception e) {
			if (e != null && e.equals(null))
				Log.e("Error", e.getMessage());
		}
		finally {
			if (stream != null)
				stream.close();
		}
		return data;

	}


	public static Map<String, String> parsingMetadata(String metaString) {
		@SuppressWarnings({"rawtypes", "unchecked"})
		Map<String, String> metadata = new HashMap();
		String[] metaParts = metaString.split(";");
		Pattern p = Pattern.compile("^([a-zA-Z]+)=\\'([^\\']*)\\'$");
		Matcher m;
		for (int i = 0; i < metaParts.length; i++) {
			m = p.matcher(metaParts[i]);
			if (m.find()) {
				metadata.put(m.group(1), m.group(2));
			}
		}

		return metadata;
	}
}
