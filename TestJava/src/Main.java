import java.util.Random;


public class Main {
	
	
	public Main() {
		
		Random rand = new Random();
		for( int i = 0; i < 10; i++ ) {
			System.out.println(Math.abs(rand.nextInt()));
			
		}
	}

	
	public void println() {
		System.out.println();
	}
	public void println(String str) {
		System.out.println(str);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
