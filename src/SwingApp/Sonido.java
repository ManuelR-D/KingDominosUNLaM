package SwingApp;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sonido {
	Clip clip;
	private static String pathAssets = "./assets/Sound/";
	private static String extension = ".wav";
	public Sonido(String path) throws Exception {
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
		clip = AudioSystem.getClip();
		clip.open(audioIn);
	}

	public void play() {
		clip.stop();
		clip.flush();
		clip.setMicrosecondPosition(0);
		clip.start();
	}

	private static void playSonido(String path) {
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.stop();
			clip.flush();
			clip.setMicrosecondPosition(0);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.out.println("Error en la reproducción de: " + path);
			e.printStackTrace();
		}
	}

	public static void playPonerCarta() {
		playSonido(pathAssets + "ponerCarta" + getRandomNumber(0, 3) + extension);
	}

	public static void playCartaSeleccionada() {
		playSonido(pathAssets + "cartaSeleccion" + getRandomNumber(0, 2) + extension);
	}
	public static void playMainTheme() {
		playSonido("./assets/Sound/main.wav");
	}
	public void setVolume(float volume) {
		if (volume < 0f || volume > 1f)
			throw new IllegalArgumentException("Volume not valid: " + volume);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(20f * (float) Math.log10(volume));
	}

	public static int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
}
