
public class Main {
	public static void main(String[] args) {
		FrequencyList control = new FrequencyList("src/letter_frequencies.txt");
		
		FrequencyList test = CodeBreaker.getFileFrequencies("src/testfile.txt");
		test.print();
	}
}
