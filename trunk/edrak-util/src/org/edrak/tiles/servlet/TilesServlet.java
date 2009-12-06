package org.edrak.tiles.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.edrak.util.Util;

public class TilesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2298391887210308963L;

	
	
	static final String LAYOUT_PATH="/WEB-INF/layouts";
	static final String DEFAULT_LAYOUT_NAME="default";
	static final String DEFAULT_LAYOUT_PAGE="main.jsp";
	static final String DEFAULT_POP_LAYOUT_PAGE="pop.jsp";
	static final String JSPS_PATH="/WEB-INF/jsp";
	
	
	String contextPath;
	String rootPath;
	boolean isInit=false;
	
	
	String jspsRealPath;
	String defaultLayoutPage;
	String defaultPopLayoutPage;
	
	
	private void initMe(HttpServletRequest req){
		if (isInit) return;
		
		
		rootPath = req.getSession().getServletContext().getRealPath("");
		rootPath = Util.getNormalizedPath(rootPath);
		
		jspsRealPath=rootPath+JSPS_PATH;
		jspsRealPath=Util.getNormalizedPath(jspsRealPath);
		
		defaultLayoutPage=LAYOUT_PATH+"/"+DEFAULT_LAYOUT_NAME+"/"+DEFAULT_LAYOUT_PAGE;
		defaultPopLayoutPage=LAYOUT_PATH+"/"+DEFAULT_LAYOUT_NAME+"/"+DEFAULT_POP_LAYOUT_PAGE;
		
		contextPath=req.getContextPath();
		
		isInit=true;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		initMe(req);
		
		String pop=req.getParameter("e_pop");
		
		String uri = req.getRequestURI();
		uri=Util.getNormalizedPath(uri);
		uri=uri.substring(contextPath.length()+1,uri.length()-2); // reomve x/
		
		File jspFile=new File(jspsRealPath+uri);
		
		if (!jspFile.exists()) return;
		
		String jspURI=JSPS_PATH+"/"+uri;
		
		req.setAttribute("org.edrak.tiles.body", jspURI);
		
		//forward to the tempalete
		if (pop!=null) {
			forward(defaultPopLayoutPage,req,resp);
			return;
		}
		forward(defaultLayoutPage,req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// do nothing
	}

	
	
	 private void forward(
			    String aResponsePage, HttpServletRequest aRequest, HttpServletResponse aResponse
			  ) throws ServletException, IOException {
			    RequestDispatcher dispatcher = aRequest.getRequestDispatcher(aResponsePage);
			    dispatcher.forward(aRequest, aResponse);
			  }

	
	
}
