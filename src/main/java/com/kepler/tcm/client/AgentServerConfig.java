package com.kepler.tcm.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.kepler.tcm.core.util.StringList;
@Component
public class AgentServerConfig {
	
	 /**
	  * 代理服务列表容器
	  */
	  private StringList list = null;
	  
	  /**
	   * 配置文件
	   */
	  private File f = null;
	 
	  /**
	   * 代理配置文件名
	   */
	  private String  confFileName = "agent.conf" ;
	  
	  private static Map map = new HashMap();
	  
	  public  AgentServerConfig() throws Exception {
		  this("agent.conf");
	  }
	  
	  public  AgentServerConfig(String name) throws Exception {
		  this(null ,name);
	  }
	  
	  public AgentServerConfig(String basePath ,String name) throws Exception {
		    this.confFileName = name ;
		    this.f = new File((basePath == null ? "../" : basePath + "/" )+ confFileName);
		    this.list = new StringList();
		    reload();
	  }
	  public static AgentServerConfig getConfig(String name) throws Exception {
	    return getConfig(name, true);
	  }

	  public static synchronized AgentServerConfig getConfig(String name, boolean reload)
	    throws Exception
	  {
		  AgentServerConfig config = (AgentServerConfig)map.get(name);
	    if (config == null)
	    {
	      config = new AgentServerConfig(name);
	      map.put(name, config);
	    }
	    else if (reload) { config.reload(); }
	    return config;
	  }
	  
	  
	  public synchronized void add(String name, String value)
	    throws Exception
	  {
	    if (value == null) value = "";
	    this.list.add(name + "=" + value);
	    save();
	  }

	  public synchronized void edit(String name, String value) throws Exception
	  {
	    if (value == null) value = "";
	    this.list.setValue(name, value);

	    save();
	  }

	  public synchronized void edit(String name, String newname, String newvalue) throws Exception
	  {
	    int n = this.list.indexOfName(name);
	    if (n >= 0) this.list.set(n, newname + "=" + newvalue); else
	      this.list.add(newname, newvalue);
	    save();
	  }

	  public synchronized void save() throws Exception
	  {
	    this.list.saveToFile(this.f, "UTF-8");
	  }

	  public synchronized void remove(String name) throws Exception
	  {
	    this.list.removeByName(name); save();
	  }

	  public synchronized int size() {
	    return this.list.getCount();
	  }

	  public synchronized String getName(int i) {
	    return this.list.getName(i);
	  }

	  public synchronized int indexOfName(String name){
	    return this.list.indexOfName(name);
	  }

	  public synchronized String getValue(int i){
	    return this.list.getValue(i);
	  }

	  public synchronized String getValue(String s) {
	    return this.list.getValue(s);
	  }

	  public synchronized void reload()
	    throws Exception{
	    if (this.f.exists()) this.list.loadFromFile(this.f, "UTF-8");
	  }
}
