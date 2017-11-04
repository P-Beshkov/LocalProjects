package videoExtract;

import java.io.File;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

public class ExtractAudio {

	public void start() throws IllegalArgumentException, InputFormatException, EncoderException {
		File source = new File(
				"C:/Users/pbeshkov/Downloads/Sherlock.Season.4.1080p.HDTV.x265.HEVC.AAC-GOD/Sherlock.S04E01.1080p.HDTV.x265.HEVC.AAC-GOD/Sherlock.S04E01.1080p.HDTV.x265.HEVC.AAC-GOD.mkv");
		File target = new File("target.wav");
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("pcm_s16le");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("wav");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		encoder.encode(source, target, attrs);
	}
}
