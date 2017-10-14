package Andice.dice.view;

import android.content.Context;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;

public class Db4oHelper2 {
   
	private static ObjectContainer container = null;
	private Context context;
	
	public Db4oHelper2(Context context){
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
	    
		return ctx.getDir("data",0) + "/" +"person2.db4o";
	}
	
	private Configuration dbConfig(){
		
		Configuration c = Db4o.newConfiguration();
		c.objectClass(path.class).objectField("name").indexed(true);
		c.objectClass(path.class).cascadeOnDelete(true);
		c.objectClass(path.class).cascadeOnUpdate(true);
		return c;
	}
	
	public void closed(){
		 
		if(container != null) container.close();
	}
	
	public void addData(String name,int p){
		if(this.getpath(name) == null){
		path d = new path(p,name);
		db().store(d);
		db().commit();
		}
		else{
			path d = this.getpath(name);
			d.p = p;
			db().store(d);
			db().commit();
			
		}
	}
	
	public path getpath(String name){
		
		path proto = new path();
		proto.name = name;
		ObjectSet<path> result = db().queryByExample(proto);
		if(result.hasNext()){
			return (path)result.next();
		}
		else{
			return null;
		}
	}
}
