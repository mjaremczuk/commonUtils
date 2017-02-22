package pl.revo.helperutils.utils;

import android.content.Context;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class CustomObservable implements ObservableOnSubscribe<Boolean> {

	private final Context context;

	CustomObservable(Context context){
		this.context = context;
	}

	@Override
	public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
		for (long i = 0; i < 50000000; i++) {
			
		}
		emitter.onNext(true);
		emitter.onComplete();
	}
}
