package zelda.game.control.script;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import zelda.common.Sounds;
import zelda.main.Sound;


public class ScriptTestConsole {
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		
		Sounds.initialize();
		
		
		while (true) {
			System.out.print("Enter a sound name: ");
			String name = console.nextLine();
			if (name.equals("end"))
				break;
			
			Sound sound = Sounds.loadSound(name);
			if (sound == null)
				System.out.println("Null Sound!");
			else {
				sound.loop();
				System.out.println("Playing sound \"" + sound.getName() + "\"");
			}
		}
		
		console.close();
		
		System.out.println("done");
	}
	
	public static AudioStream play(String name) throws IOException {
	    InputStream in = new FileInputStream("sounds/" + name);
	    AudioStream audioStream = new AudioStream(in);
	    AudioPlayer.player.start(audioStream);
	    return audioStream;
	}
	
	public static AudioStream stop(String name) throws IOException {
	    InputStream in = new FileInputStream("sounds/" + name);
	    AudioStream audioStream = new AudioStream(in);
	    AudioPlayer.player.stop(audioStream);
	    return audioStream;
	}
}
