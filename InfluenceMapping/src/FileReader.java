
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileReader {
	
	public static void readFile(String path) {
		try {
			FileInputStream fstream	= new FileInputStream(path);
			DataInputStream in		= new DataInputStream(fstream);
			BufferedReader br		= new BufferedReader(new InputStreamReader(in));
			
			//Read File Line By Line
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				
				System.out.println(line);
				
			}
			in.close();
		}
		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
