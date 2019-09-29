package projects.fractals;

public class Wave {
	public float amplitude;    // A
	public float waveLength;   // L, (frequency: w = 2pi / L)
	public float speed;        // S, (phase-constant: Phi = S x (2pi/L))
	public Vector2f direction; // D
	
	
	
	public Wave(float amplitude, float waveLength, float speed, Vector2f direction) {
		this.amplitude  = amplitude;
		this.waveLength = waveLength;
		this.speed      = speed;
		this.direction  = direction.normalized();
	}
	
	public float getState(float x, float y, float t) {
		// A * sin(D dot (x, y) * w + t / phi)
		return amplitude * (float) Math.sin((direction.dot(x, y) * getFrequency()) + (t / getPhaseConstant()));
	}
	
	public float getFrequency() {
		return ((2.0f * (float) Math.PI) / waveLength);
	}
	
	public float getPhaseConstant() {
		return speed * getFrequency();
	}
}
