package Andice.dice.view;

public class Mass {

	public float m;									// 質量
	public float I;									// 轉動慣量
    public Vector3D pos;								// 位置
    public Vector3D vel;								// 速度
    public Vector3D force;								// 力
    public Vector3D torque;								// 力矩

	Mass(float m, float I)								// 構造函數
	{
		this.m = m;
		this.I = I;
		force =new Vector3D(0,0,0);
		torque =new Vector3D(0,0,0);
	}

	void applyForce(Vector3D force)
	{
		this.force.add(force);						// 增加一力
	}

	void init()								// 初始
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
		vel.add(tempF);					// 更新速度w
		tempF.equ(vel);
		tempF.mux(dt);						

		pos.add(tempF);						// 更新位置
										
	}



}
