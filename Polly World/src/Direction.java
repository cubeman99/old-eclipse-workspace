
public class Direction {
	public static float simp(float dir) {
		float d;
		d = dir;
		
		while( d >= 360.0f )
			d -= 360.0f;
		while( d < 0.0f )
			d += 360.0f;
		
		return d;
	}
	
	public static float direction(float h, float v) {
		float dir = 0.0f;
		if( h != 0.0f ) {
		    dir = -MyMath.degrees((float) Math.atan(v / h));
		    if( h < 0.0f )
		        dir += 180.0f;
		}
		else if( v > 0 )
		    dir = 270.0f;
		else if( v < 0 )
		    dir = 90.0f;
		
		return dir;
	}
}
