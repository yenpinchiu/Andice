package Andice.dice.view;

public class Mass {

	public float m;									// ��q
	public float I;									// ��ʺD�q
    public Vector3D pos;								// ��m
    public Vector3D vel;								// �t��
    public Vector3D force;								// �O
    public Vector3D torque;								// �O�x

	Mass(float m, float I)								// �c�y���
	{
		this.m = m;
		this.I = I;
		force =new Vector3D(0,0,0);
		torque =new Vector3D(0,0,0);
	}

	void applyForce(Vector3D force)
	{
		this.force.add(force);						// �W�[�@�O
	}

	void init()								// ��l
	{
		force.x = 0;
		force.y = 0;
		force.z = 0;
		torque.x = 0;
		torque.y = 0;
		torque.z = 0;
	}

	void simulate(float dt)
	{
		Vector3D tempF=new Vector3D();
		tempF.equ(force);
		tempF.equ(torque);
		tempF.div(m);
		tempF.mux(dt);
		vel.add(tempF);					// ��s�t��w
		tempF.equ(vel);
		tempF.mux(dt);						

		pos.add(tempF);						// ��s��m
										
	}



}
