package org.edrak.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.web.context.ServletContextAware;

public class ResourceBundles implements ServletContextAware{

    private static final String rootElmName = "resourceBundel";
    private static final String cacheFileName = "cache.properties";
    private static final String defaultBundel="resources";
    
    private static ResourceBundles resourceBundles;
    
    ServletContext servletContext;
    
    boolean isInit = false;
    Map bundels = new HashMap();
    
    boolean includeLangInJSVariableName=false;
    
    String resourcesFolder = "";
    String resourcesPublishFolder="";
    
    File pFolder;
    
    
    
    
    public boolean isIncludeLangInJSVariableName() {
        return includeLangInJSVariableName;
    }

    public void setIncludeLangInJSVariableName(boolean includeLangInJSVariableName) {
        this.includeLangInJSVariableName = includeLangInJSVariableName;
    }

    public String getResourcesFolder() {
	return resourcesFolder;
    }

    public void setResourcesFolder(String resourcesFolder) {
	this.resourcesFolder = resourcesFolder;
    }

    public  String getResourcesPublishFolder() {
        return resourcesPublishFolder;
    }

    public  void setResourcesPublishFolder(String resourcesPublishFolder) {
        this.resourcesPublishFolder = resourcesPublishFolder;
    }
       
    public  void init() throws Exception {
	

	if (isInit) return;
	
	Util.fixLog(); 
	String contextPath=servletContext.getRealPath("");
	if (resourcesFolder == null || resourcesFolder.equals("")) throw new Exception("Resource Folder is null");

	

	if (contextPath == null || contextPath.equals("")) throw new Exception("contextPath");
	
	
	
	contextPath=normalizePath(contextPath);
	resourcesFolder=contextPath+normalizePath(resourcesFolder);
	resourcesPublishFolder=contextPath+normalizePath(resourcesPublishFolder);
	
	
	// get list of files

	File rFolder = new File(resourcesFolder);

	if (!rFolder.exists() || !rFolder.isDirectory()) throw new Exception("Invalid recosure Folder:" + resourcesFolder);

	pFolder=new File(resourcesPublishFolder);
	
	if (!pFolder.exists()) pFolder.mkdirs();
	
	// get list of bundles

	File[] fileList = rFolder.listFiles();

	if (fileList == null || fileList.length == 0) throw new Exception("Empty Resource Folder : " + resourcesFolder);

	// load bundle
	for (int i = 0; i < fileList.length; i++) {

	    File file = fileList[i];

	    if (file.getName().toLowerCase().endsWith(".xml")) {

		String local=file.getName().toLowerCase().substring(0,file.getName().length()-4);
		
		
		if (local.lastIndexOf("_")<0){
		    local="en";
		}else {
		    local=local.substring(local.lastIndexOf("_"));
		}

		

		if (local.equals("")) local = "en";
		if (local.startsWith("_")) local = local.substring(1);
		if (local.equals("")) local = "en";
		loadLocal(local, file);

	    }

	}

	if (bundels.size() == 0) throw new Exception("No resource files with  in folder:" + resourcesFolder);

	
	ResourceBundles.resourceBundles=this;
	isInit = true;
    }

    

    private String normalizePath(String normalizePath){
	
	if (normalizePath==null ) return null;
	
	normalizePath = normalizePath.replace('\\', '/');

	if (!normalizePath.endsWith("/")) normalizePath += "/";
	
	return normalizePath;
	
    }
    
    private String getResourceFileName(File xmlFile){
	String ret=xmlFile.getName().toLowerCase();
	ret=ret.substring(0,ret.length()-4);
	
	if (ret.lastIndexOf("_")>=0){
	    ret=ret.substring(0,ret.lastIndexOf("_"));
	}
	return ret;
    }
    private  void generateScriptFile(String local,File xmlFile) throws FileNotFoundException, IOException {

	if (resourcesFolder == null) return;
	if (xmlFile==null || local==null || local.equals("")) return;
	
	String cacheFilePath = resourcesFolder + cacheFileName;

	File cacheFile = new File(cacheFilePath);
	Properties cacheProp = new Properties();

	if (cacheFile.exists()) {
	    cacheProp.load(new FileInputStream(cacheFile));
	}
	
	
	
	    Properties prop = (Properties) getProp(getResourceFileName(xmlFile),local);
	    if (prop==null) return;
	    
	    
	    
	    String jsFileName = getResourceFileName(xmlFile) + "_" + local + ".js";
	    String jsFilePath = resourcesFolder + jsFileName;

	    File jsFile = new File(jsFilePath);
	    String modTimetxt = cacheProp.getProperty(xmlFile.getName());
	    long modTime = 0;

	    try {
		if (modTimetxt != null) modTime = Long.parseLong(modTimetxt);
	    } catch (Exception e) {
	    }

	    if (jsFile.exists() && modTime > 0 && modTime >= xmlFile.lastModified()) {

	    } else {

		regenrateJsFile(local, prop, jsFile, xmlFile,cacheProp);
		cacheProp.store(new FileOutputStream(cacheFile), "");
	    }
	
	
	    
    }

    private  void regenrateJsFile(String local, Properties prop, File file, File xmlFile,Properties cacheProp) {

	if (local == null || local.equals("")) return;
	if (prop == null || file == null || cacheProp == null) return;

	if (file.exists()) file.delete();
	

	BufferedWriter out = null;
	try {
	    String variableName=prop.getProperty("sys_variableName");
	    if (variableName==null) variableName="res";
	    
	    if (includeLangInJSVariableName){
		variableName+="_"+local;
	    }
	    
	    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	    // the Unicode value for UTF-8 BOM
	    out.write("\ufeff");
	    out.write("if (typeof(MMAA)==\"undefined\") {MMAA={};}\n");
	    out.write("if (typeof(MMAA."+variableName+")==\"undefined\") {\n");
	    out.write("MMAA."+variableName+"=new Array();\n");

	    for (Enumeration it = prop.keys(); it.hasMoreElements();) {
		String key = (String) it.nextElement();
		String value = prop.getProperty(key, "");
		if(value!=null) {
		    value=value.replace('\n',' ');
		    value=value.replace('\r',' ');
		    value=value.replace('"','\'');
		}
		
		out.write("MMAA."+variableName+"[\"" + key + "\"]=\"" + value + "\";\n");
	    }
	    out.write("}\n");
	    out.close();
	    out=null;
	    FileUtils.copyFileToDirectory(file, pFolder);
	    cacheProp.put(xmlFile.getName(), xmlFile.lastModified()+"");
	} catch (Exception e) {

	    e.printStackTrace();
	    return;
	} finally {
	    try {
		if (out != null) out.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

    }

    private void loadLocal(String local, File file) throws JDOMException, IOException, Exception {

	SAXBuilder builder = new SAXBuilder();

	Document doc = builder.build(file);
	Element rootElm = doc.getRootElement();

	if (rootElm == null) throw new Exception("invalid xml file:" + file.getAbsoluteFile());

	if (!rootElm.getName().equals(rootElmName)) throw new Exception("Root Element in xml is not " + rootElmName + " for file :" + file.getAbsoluteFile());

	List childs = rootElm.getChildren();

	
	if (childs == null) return;
	
	Attribute attr = rootElm.getAttribute("var");
	String variableName=null;
	
	if (attr!=null) {
	    variableName=attr.getValue();
	}
	
	if (variableName==null) variableName="res";
	
	Properties prop = new Properties();
	
	prop.put("sys_variableName", variableName);
	
	for (int i = 0; i < childs.size(); i++) {

	    Element child = (Element) childs.get(i);

	    String key = child.getName();
	    String value = child.getText();
	    if (value == null) continue;

	    value = value.trim();
	    if (value.equals("")) continue;

	    prop.setProperty(key, value);

	}

	put(getResourceFileName(file),local, prop);
	generateScriptFile(local,file);
    }

    private void put(String bundelName,String local ,Properties prop){
	
	if (bundelName==null || local==null) return;
	
	Map bundel=(Map)bundels.get(bundelName);
	if (bundel==null){
	    bundel=new HashMap();
	    bundels.put(bundelName, bundel);
	}
	
	bundel.put(local, prop);
	
    }
    
    public Properties getProp(String bundelName,String locale){
	Map bundel=(Map)bundels.get(bundelName);
	if (bundel==null) return null;
	
	return (Properties)bundel.get(locale);
	
    }
   
    public String get(String bundelName,String key, String locale) {
	String ret = "";

	if (key == null || locale == null || bundelName==null) return ret;
	bundelName=bundelName.toLowerCase();
	if (locale.equals("")) locale = "en";

	Properties bundel = (Properties) getProp(bundelName,locale);

	if (bundel==null) return ret;
	if (locale.length() > 2) locale = locale.substring(0, 2);
	

	ret = bundel.getProperty(key, "");

	return ret;
	
    }
    
    public  String get(String key, String locale) {
	
	return get(defaultBundel,key,locale);
	
    }
    public static String getString(String bundelName,String key, String locale) {
	
	return resourceBundles.get(bundelName, key, locale);
	
    }
    
    public static String getString(String key, String locale) {
	
	return getString(defaultBundel,key,locale);
	
    }

    public void setServletContext(ServletContext servletContext) {
	this.servletContext=servletContext;
	
    }

}
