package pl.revo.helperutils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import javax.inject.Inject;
import javax.inject.Singleton;
import pl.revo.helperutils.service.RadioService;
import pl.revo.helperutils.service.RadioService.RadioServiceBinder;

@Singleton
public class RadioController {


	public static final String EXTRA_START_PLAYING = "extra_start_play";
	public static final String EXTRA_SOURCE_URL = "extra_source_url";
	public static final String EXTRA_STOP_PLAYING = "extra_stop_playing";
	private final Context context;
	RadioService radioService;
	AudioServiceBindListener bindListener;
	boolean isBound = false;

	@Inject
	public RadioController(Context context){
		this.context = context;
	}

	/**
	 * Start audio service
	 */
	public void startRadioService(String url) {
		Intent radioIntent = new Intent(context, RadioService.class)
				.putExtra(EXTRA_START_PLAYING,true)
				.putExtra(EXTRA_SOURCE_URL,url);
		context.startService(radioIntent);
	}

	public void stopRadioService(){
		Intent radioIntent = new Intent(context, RadioService.class)
				.putExtra(EXTRA_STOP_PLAYING,true);
		context.startService(radioIntent);
	}

	/**
	 * Bind to audio service
	 */
	public void bindRadioService(AudioServiceBindListener listener) {
		this.bindListener = listener;
		Intent radioIntent = new Intent(context, RadioService.class);
		context.bindService(radioIntent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	/**
	 * Unbind from the audio service
	 */
	public void unBindRadioService() {
		if (isBound) {
			context.unbindService(serviceConnection);
			isBound = false;
		}
	}

	/**
	 * ServiceConnection object for communication with the service
	 */
	ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			RadioService.RadioServiceBinder binder = (RadioServiceBinder) service;
			radioService = binder.getService();
			isBound = true;
			if (bindListener != null) {
				bindListener.onAudioServiceBindSuccess();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			isBound = false;
			if (bindListener != null) {
				bindListener.onAudioServiceDisconnect();
			}
		}
	};



	/**
	 * Interface for binding activity with service
	 */
	public interface AudioServiceBindListener {

		/**
		 * Called when activity successfully binded to service
		 */
		void onAudioServiceBindSuccess();

		/**
		 * Called when activity successfully unbinded from service
		 */
		void onAudioServiceDisconnect();
	}
}
