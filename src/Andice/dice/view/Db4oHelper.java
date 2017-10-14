package Andice.dice.view;

import android.content.Context;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;

public class Db4oHelper {
   
	private static ObjectContainer container = null;
	private Context context;
	
	public Db4oHelper(Context context){
		this.context = context;
	}
	
	public ObjectContainer db(){
		try{
			if(container == null || container.ext().isClosed())
				container = Db4o.openFile(dbConfig(),db4oDBFullPath(context));
			 return container;
		}catch(Exception e){
			return null;
		}	
	}
	
	private String db4oDBFullPath(Context ctx){
	    
		return ctx.getDir("data",0) + "/" +"person.db4o";
	}
	
	private Configuration dbConfig(){
		
		Configuration c = Db4o.newConfiguration();
		c.objectClass(threedice.class).objectField("name").indexed(true);
		c.objectClass(threedice.class).cascadeOnDelete(true);
		c.objectClass(threedice.class).cascadeOnUpdate(true);
		return c;
	}
	
	public void closed(){
		 
		if(container != null) container.close();
	}
	
	public void addData(String name,int f1,int f2,int f3, int f4,int f5,int f6){
		if(this.getdice(name) == null){
		threedice d = new threedice(name,f1,f2,f3,f4,f5,f6);
		db().store(d);
		db().commit();
		}
		else{
			threedice d = this.getdice(name);
			d.face1 = f1;
			d.face2 = f2;
			d.face3 = f3;
			d.face4 = f4;
			d.face5 = f5;
			d.face6 = f6;
			db().store(d);
			db().commit();
			
		}
	}
	
	public threedice getdice(String name){
		
		threedice proto = new threedice();
		proto.name = name;
		ObjectSet<threedice> result = db().queryByExample(proto);
		if(result.hasNext()){
			return (threedice)result.next();
		}
		else{
			return null;
		}
	}
	
}
