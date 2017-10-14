package Andice.dice.view;

public class Matrix {
    double x1,x2,x3,y1,y2,y3,z1,z2,z3;
    
    Matrix(double a,double b,double c,double d,double e,double f,double g,double h,double i){
   	 this.x1=a;
   	 this.x2=b;
   	 this.x3=c;
   	 this.y1=d;
   	 this.y2=e;
   	 this.y3=f;
   	 this.z1=g;
   	 this.z2=h;
   	 this.z3=i;
    }
    
    void MUX(Matrix a){
   	 double A,B,C,D,E,F,G,H,I;
   	 A=a.x1*this.x1+a.x2*this.y1+a.x3*this.z1;
   	 B=a.x1*this.x2+a.x2*this.y2+a.x3*this.z2;
   	 C=a.x1*this.x3+a.x2*this.y3+a.x3*this.z3;
   	 D=a.y1*this.x1+a.y2*this.y1+a.y3*this.z1;
   	 E=a.y1*this.x2+a.y2*this.y2+a.y3*this.z2;
   	 F=a.y1*this.x3+a.y2*this.y3+a.y3*this.z3;
   	 G=a.z1*this.x1+a.z2*this.y1+a.z3*this.z1;
   	 H=a.z1*this.x2+a.z2*this.y2+a.z3*this.z2;
   	 I=a.z1*this.x3+a.z2*this.y3+a.z3*this.z3;
   	 
   	 this.x1=A;
   	 this.x2=B;
   	 this.x3=C;
   	 this.y1=D;
   	 this.y2=E;
   	 this.y3=F;
   	 this.z1=G;
   	 this.z2=H;
   	 this.z3=I;
   	 
    }
	
	
}
