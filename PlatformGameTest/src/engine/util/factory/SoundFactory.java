package engine.util.factory;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import engine.audio.IAudioDevice;
import engine.audio.Sound;
import engine.audio.SoundData;

public class SoundFactory {
	private final IAudioDevice device;
	private Map<String, SoftReference<SoundData>> loaded;

	public SoundFactory(IAudioDevice device) {
		this.device = device;
		this.loaded = new HashMap<>();
	}

	public Sound get(String fileName, double volume, double pitch,
			boolean shouldLoop) throws IOException {
		SoftReference<SoundData> ref = loaded.get(fileName);
		SoundData data = ref == null ? null : ref.get();
		if (data == null) {
			loaded.remove(fileName);
			data = new SoundData(device, fileName);
			loaded.put(fileName, new SoftReference<SoundData>(data));
		}
		
		return new Sound(device, data, volume, pitch, shouldLoop);
	}
}
