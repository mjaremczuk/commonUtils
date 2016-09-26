package pl.revo.helperutils.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import pl.revo.helperutils.RadioController;
import pl.revo.helperutils.presenter.RadioPresenter;

public class RadioService extends Service {
	public RadioService() {
	}

	public class RadioServiceBinder extends Binder {

		public RadioService getService() {
			return RadioService.this;
		}
	}

	private final IBinder serviceBinder = new RadioServiceBinder();

	RadioPresenter presenter;

	@Override
	public void onCreate() {
		super.onCreate();
		presenter = new RadioPresenter();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return serviceBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			boolean startPlay = intent.getBooleanExtra(RadioController.EXTRA_START_PLAYING,false);
			boolean stopPlaying = intent.getBooleanExtra(RadioController.EXTRA_STOP_PLAYING,false);
			String sourceUrl = intent.getStringExtra(RadioController.EXTRA_SOURCE_URL);
			if(startPlay) {
				presenter.startRadioPlayer(this, sourceUrl);
			}
			if(stopPlaying){
				presenter.pause();
			}
		}
		return START_STICKY;
	}
}
