package Andice.dice.view;

import java.lang.Math;

public class Vector3D {

	public float x;									// the x value of this Vector3D
    public float y;									// the y value of this Vector3D
    public float z;									// the z value of this Vector3D

	Vector3D()									// Constructor to set x = y = z = 0
	{
		x = 0;
		y = 0;
		z = 0;
	}

	Vector3D(float x, float y, float z)			// Constructor that initializes this Vector3D to the intended values of x, y and z
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	Vector3D equ (Vector3D v)			// operator= sets values of v to this Vector3D. example: v1 = v2 means that values of v2 are set onto v1
	{
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}

	 Vector3D add(Vector3D v)				// operator+ is used to add two Vector3D's. operator+ returns a new Vector3D
	{
		 Vector3D temp=new Vector3D(x + v.x, y + v.y, z + v.z);
		 return temp;
	}

	Vector3D sub (Vector3D v)				// operator- is used to take difference of two Vector3D's. operator- returns a new Vector3D
	{
		Vector3D temp=new Vector3D(x - v.x, y - v.y, z - v.z);
		return temp;
	}

	Vector3D mux (float value)			// operator* is used to scale a Vector3D by a value. This value multiplies the Vector3D's x, y and z.
	{
		Vector3D temp=new Vector3D(x * value, y * value, z * value);
		return temp;
	}

	Vector3D div (float value)			// operator/ is used to scale a Vector3D by a value. This value divides the Vector3D's x, y and z.
	{
		Vector3D temp=new Vector3D(x / value, y / value, z / value);
		return temp;
	}




	Vector3D neg ()						// operator- is used to set this Vector3D's x, y, and z to the negative of them.
	{
		Vector3D temp=new Vector3D(-x,-y,-z);
		return temp;
	}

	float length()								// length() returns the length of this Vector3D
	{
		
		return (float) Math.sqrt(x*x + y*y + z*z);
	};			   		

	void unitize()								// unitize() normalizes this Vector3D that its direction remains the same but its length is 1.
	{
		float length = this.length();

		if (length == 0)
			return;

		x /= length;
		y /= length;
		z /= length;
	}

	Vector3D unit()								// unit() returns a new Vector3D. The returned value is a unitized version of this Vector3D.
	{
		float length = this.length();

		if (length == 0)
			return this;
		
		Vector3D temp=new Vector3D(x / length, y / length, z / length);
		
		return temp;
	}
}
