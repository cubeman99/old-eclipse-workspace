package base.engine.common;


public class Matrix4f
{
	private float[][] m;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Matrix4f() {
		m = new float[4][4];
	}

	
	
	// ================= INITIALIZATION ================= //
	
	public Matrix4f initIdentity()
	{
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}
	
	public Matrix4f initTranslation(float x, float y, float z)
	{
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = x;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = y;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = z;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initRotation(float x, float y, float z)
	{
		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();
		
		x = (float)Math.toRadians(x);
		y = (float)Math.toRadians(y);
		z = (float)Math.toRadians(z);
		
		rz.m[0][0] = (float)Math.cos(z);rz.m[0][1] = -(float)Math.sin(z);rz.m[0][2] = 0;				rz.m[0][3] = 0;
		rz.m[1][0] = (float)Math.sin(z);rz.m[1][1] = (float)Math.cos(z);rz.m[1][2] = 0;					rz.m[1][3] = 0;
		rz.m[2][0] = 0;					rz.m[2][1] = 0;					rz.m[2][2] = 1;					rz.m[2][3] = 0;
		rz.m[3][0] = 0;					rz.m[3][1] = 0;					rz.m[3][2] = 0;					rz.m[3][3] = 1;
		
		rx.m[0][0] = 1;					rx.m[0][1] = 0;					rx.m[0][2] = 0;					rx.m[0][3] = 0;
		rx.m[1][0] = 0;					rx.m[1][1] = (float)Math.cos(x);rx.m[1][2] = -(float)Math.sin(x);rx.m[1][3] = 0;
		rx.m[2][0] = 0;					rx.m[2][1] = (float)Math.sin(x);rx.m[2][2] = (float)Math.cos(x);rx.m[2][3] = 0;
		rx.m[3][0] = 0;					rx.m[3][1] = 0;					rx.m[3][2] = 0;					rx.m[3][3] = 1;
		
		ry.m[0][0] = (float)Math.cos(y);ry.m[0][1] = 0;					ry.m[0][2] = -(float)Math.sin(y);ry.m[0][3] = 0;
		ry.m[1][0] = 0;					ry.m[1][1] = 1;					ry.m[1][2] = 0;					ry.m[1][3] = 0;
		ry.m[2][0] = (float)Math.sin(y);ry.m[2][1] = 0;					ry.m[2][2] = (float)Math.cos(y);ry.m[2][3] = 0;
		ry.m[3][0] = 0;					ry.m[3][1] = 0;					ry.m[3][2] = 0;					ry.m[3][3] = 1;
		
		m = rz.times(ry.times(rx)).getM();
		
		return this;
	}
	
	public Matrix4f initScale(float x, float y, float z)
	{
		m[0][0] = x;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = y;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = z;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar)
	{
		float tanHalfFOV = (float) Math.tan(fov / 2);
		float zRange = zNear - zFar;
		
		m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);	m[0][1] = 0;					m[0][2] = 0;						m[0][3] = 0;
		m[1][0] = 0;									m[1][1] = 1.0f / tanHalfFOV;	m[1][2] = 0;						m[1][3] = 0;
		m[2][0] = 0;									m[2][1] = 0;					m[2][2] = (-zNear -zFar)/zRange;	m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;									m[3][1] = 0;					m[3][2] = 1;						m[3][3] = 0;
		
		return this;
	}

	public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far)
	{
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;

		m[0][0] = 2/width;m[0][1] = 0;	m[0][2] = 0;	m[0][3] = -(right + left)/width;
		m[1][0] = 0;	m[1][1] = 2/height;m[1][2] = 0;	m[1][3] = -(top + bottom)/height;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = -2/depth;m[2][3] = -(far + near)/depth;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f initRotation(Vector3 forward, Vector3 up)
	{
		Vector3 f = forward.normalized();
		
		Vector3 r = up.normalized();
		r = r.cross(f);
		
		Vector3 u = f.cross(r);

		return initRotation(f, u, r);
	}

	public Matrix4f initRotation(Vector3 forward, Vector3 up, Vector3 right)
	{
		Vector3 f = forward;
		Vector3 r = right;
		Vector3 u = up;

		m[0][0] = (float) r.x;	m[0][1] = (float) r.y;	m[0][2] = (float) r.z;	m[0][3] = 0;
		m[1][0] = (float) u.x;	m[1][1] = (float) u.y;	m[1][2] = (float) u.z;	m[1][3] = 0;
		m[2][0] = (float) f.x;	m[2][1] = (float) f.y;	m[2][2] = (float) f.z;	m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;

		return this;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
//	public Vector3 transform(Vector3 r)
//	{
//		return new Vector3(m[0][0] * r.x + m[0][1] * r.y + m[0][2] * r.z + m[0][3],
//		                    m[1][0] * r.x + m[1][1] * r.y + m[1][2] * r.z + m[1][3],
//		                    m[2][0] * r.x + m[2][1] * r.y + m[2][2] * r.z + m[2][3]);
//	}
	
	/** Return the product of this and a given matrix. **/
	public Matrix4f times(Matrix4f r) {
		Matrix4f result = new Matrix4f();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.set(i, j, m[i][0] * r.get(0, j) +
							  	 m[i][1] * r.get(1, j) +
							  	 m[i][2] * r.get(2, j) +
							  	 m[i][3] * r.get(3, j));
			}
		}
		
		return result;
	}
	
	/** Return a copy of the raw matrix data. **/
	public float[][] getM() {
		float[][] res = new float[4][4];
		
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				res[i][j] = m[i][j];
		
		return res;
	}
	
	/** Get the matrix data located at the given position. **/
	public float get(int x, int y) {
		return m[x][y];
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setM(float[][] m) {
		this.m = m;
	}

	public void set(int x, int y, float value) {
		m[x][y] = value;
	}

	
	
	// ================ STATIC METHODS ================ //
	
	public static Matrix4f createTranslation(float x, float y, float z) {
		return new Matrix4f().initTranslation(x, y, z);
	}
	
	public static Matrix4f createScale(float x, float y, float z) {
		return new Matrix4f().initScale(x, y, z);
	}
	
	public static Matrix4f createRotation(float x, float y, float z) {
		return new Matrix4f().initRotation(x, y, z);
	}
}
