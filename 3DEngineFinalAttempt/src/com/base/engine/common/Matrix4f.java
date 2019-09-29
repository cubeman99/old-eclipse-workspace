package com.base.engine.common;



public class Matrix4f
{
	private float[][] m;
	
	
	// ================== CONSTRUCTORS ================== //

	public Matrix4f() {
		m = new float[4][4];
	}

	
	
	// =================== ACCESSORS =================== //
	
	public float get(int x, int y) {
		return m[x][y];
	}
	
	public float[][] getM() {
		float[][] res = new float[4][4];
		
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				res[i][j] = m[i][j];
		
		return res;
	}
	
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

	public Vector3f transform(Vector3f r) {
		return new Vector3f(m[0][0] * r.x + m[0][1] * r.y + m[0][2] * r.z + m[0][3],
		                    m[1][0] * r.x + m[1][1] * r.y + m[1][2] * r.z + m[1][3],
		                    m[2][0] * r.x + m[2][1] * r.y + m[2][2] * r.z + m[2][3]);
	}
	
	public Quaternion toQuaternionRotation() {
		float trace = 1 + m[0][0] + m[1][1] + m[2][2];
		float x, y, z, w;
		
		if (trace > 0) {
			float s = (float) Math.sqrt(trace + 1) * 2;
			w = 0.25f * s;
			x = (m[2][1] - m[1][2]) / s;
			y = (m[0][2] - m[2][0]) / s;
			z = (m[1][0] - m[0][1]) / s;
		}
		else if (m[0][0] > m[1][1] && m[0][0] > m[2][2]) {
			float s = (float) Math.sqrt(1 + m[0][0] - m[1][1] - m[2][2]) * 2;
			w = (m[2][1] - m[1][2]) / s;
			x = 0.25f * s;
			y = (m[0][1] - m[1][0]) / s;
			z = (m[0][2] - m[2][0]) / s;
		}
        else if (m[1][1] > m[2][2]) {
			float s = (float) Math.sqrt(1 + m[1][1] - m[0][0] - m[2][2]) * 2;
			w = (m[0][2] - m[2][0]) / s;
			x = (m[0][1] - m[1][0]) / s;
			y = 0.25f * s;
			z = (m[2][1] - m[1][2]) / s;
		}
        else {
			float s = (float) Math.sqrt(1 + m[2][2] - m[0][0] - m[1][1]) * 2;
			w = (m[1][0] - m[0][1]) / s;
			x = (m[0][2] - m[2][0]) / s;
			y = (m[1][2] - m[2][1]) / s;
			z = 0.25f * s;
        }
		
		return new Quaternion(x, y, z, w);
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void setM(float[][] m) {
		this.m = m;
	}

	public void set(int x, int y, float value) {
		m[x][y] = value;
	}
	
	public Matrix4f initIdentity() {
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initTranslation(float x, float y, float z) {
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = x;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = y;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = z;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initRotation(float x, float y, float z) {
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
	
	public Matrix4f initScale(float x, float y, float z) {
		m[0][0] = x;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = y;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = z;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}

	public Matrix4f initRotation(Vector3f forward, Vector3f up) {
		Vector3f f = forward.normalized();
		Vector3f r = up.normalized().cross(f).normalized();
		Vector3f u = f.cross(r).normalized();
		return initRotation(f, u, r);
	}

	public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right) {
		Vector3f f = forward;
		Vector3f r = right;
		Vector3f u = up;

		m[0][0] = r.x;	m[0][1] = r.y;	m[0][2] = r.z;	m[0][3] = 0;
		m[1][0] = u.x;	m[1][1] = u.y;	m[1][2] = u.z;	m[1][3] = 0;
		m[2][0] = f.x;	m[2][1] = f.y;	m[2][2] = f.z;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}
	
	public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		float ar         = aspectRatio;
		float tanHalfFOV = (float) Math.tan(fov / 2);
		float zRange     = zNear - zFar;
		
		m[0][0] = 1.0f / (tanHalfFOV * ar);	m[0][1] = 0;					m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;						m[1][1] = 1.0f / tanHalfFOV;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;						m[2][1] = 0;					m[2][2] = (-zNear -zFar)/zRange;	m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;						m[3][1] = 0;					m[3][2] = 1;	m[3][3] = 0;
		
		return this;
	}
	
	public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far) {
		float width  = right - left;
		float height = top - bottom;
		float depth  = far - near;

		m[0][0] = 2/width;	m[0][1] = 0;		m[0][2] = 0;		m[0][3] = -(right + left)/width;
		m[1][0] = 0;		m[1][1] = 2/height;	m[1][2] = 0;		m[1][3] = -(top + bottom)/height;
		m[2][0] = 0;		m[2][1] = 0;		m[2][2] = 2/depth;	m[2][3] = -(far + near)/depth;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;
		
		return this;
	}
	
	
	
	// ================ STATIC METHODS ================ //
	
	public static Matrix4f createIdentity() {
		return new Matrix4f().initIdentity();
	}

	public static Matrix4f createTranslation(float x, float y, float z) {
		return new Matrix4f().initTranslation(x, y, z);
	}
	
	public static Matrix4f createTranslation(Vector3f translation) {
		return new Matrix4f().initTranslation(translation.x, translation.y, translation.z);
	}
	
	public static Matrix4f createRotation(float x, float y, float z) {
		return new Matrix4f().initRotation(x, y, z);
	}
	
	public static Matrix4f createRotation(Vector3f forward, Vector3f up) {
		return new Matrix4f().initRotation(forward, up);
	}
	
	public static Matrix4f createRotation(Vector3f forward, Vector3f up, Vector3f right) {
		return new Matrix4f().initRotation(forward, up, right);
	}
	
	public static Matrix4f createScale(float x, float y, float z) {
		return new Matrix4f().initScale(x, y, z);
	}
	
	public static Matrix4f createPerspective(float fov, float aspectRatio, float depthMin, float depthMax) {
		return new Matrix4f().initPerspective(fov, aspectRatio, depthMin, depthMax);
	}
	
	public static Matrix4f createOrthographic(float left, float right, float bottom, float top, float near, float far) {
		return new Matrix4f().initOrthographic(left, right, bottom, top, near, far);
	}
}
