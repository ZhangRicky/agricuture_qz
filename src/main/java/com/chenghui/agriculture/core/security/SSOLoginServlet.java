package com.chenghui.agriculture.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Servlet implementation class SSOLoginServlet
 */
@WebServlet(description = "SSO for 4A", urlPatterns = { "/ssoLogin" })
public class SSOLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SSOLoginServlet.class);
	
	public static void main(String[] args){
		/*String str = "MAIN_ID=admin&op=4A&loginName=yudq&ticket=ticket";
		System.out.println(Base64.encodeBase64String(str.getBytes()));*/
		
		/*InputStream in;
		try {
			File file  = new File("D:\\home\\client.properties");
			File file1  = new File("D:\\home\\sql.txt");
			System.out.println(file.exists());
			System.out.println(file1.exists());
			in = new BufferedInputStream(new FileInputStream("D:\\home\\client.properties"));
			Properties p = new Properties();
		    p.load(in);
		   
			System.out.println("/home/jcomp/dhlrweb/clinet.properties:"+in.toString());
			System.out.println(p.getProperty("client.sso.server.connection.timeout"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*Client clinet=Client.getInstance("D:\\home\\");
		clinet.doSSO("", "", "", 11, "");*/
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSOLoginServlet() {
        super();
    }

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Autowired
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "";
		//url = request.getQueryString();//由于加密后的传过来的参数值里带有%符号,servlet会将其忽略.故用request.getQueryString()
		url = request.getParameter("pname");
		System.out.println();
		System.out.println("request url is "+url);
		String userIp = request.getRemoteAddr();
		String message = "";
		String loginName = "";//帐户名
		String ticket = "";//票据
		if("".equals(url)){message = "非法的ULR请求.";}
		String decodepname = new String(Base64.decodeBase64(url));
		System.out.println("decodepname is "+decodepname);
		//String decodepname = new String(Base64.decodeBase64(url.substring(url.indexOf("pname=")+6, url.length())));			
		String [] parm = decodepname.split("&");//将传过来的参数值在解密后,按&符号进split;
		for(String str : parm){
				if(str.contains("loginName=")){loginName = str.substring(10,str.length());}//得到登陆帐号
				if(str.contains("ticket=")){ticket = str.substring(7,str.length());}//得到票据
		}
		System.out.println("loginName is "+loginName);
		System.out.println("ticket is "+ticket);
		//未取到登陆帐户
		if("".equals(loginName)&&(!"".equals(url))){message = "未正确获取登陆帐户,url请求无效.";}
		//未取到票据
		if("".equals(ticket)&&(!"".equals(url))){message = "未正确获取票据资源,url请求无效.";}
		//admin测试用,正式需要替换为loginName
		FuJianSSOAuthenticationToken token = new FuJianSSOAuthenticationToken(loginName, ticket,userIp,message);
		try {
			 SecurityUtils.getSubject().login(token);
			 response.sendRedirect("home");
		 } catch (AuthenticationException e) {
			 //e.printStackTrace();
			 System.out.println("Exception message:"+e.getMessage());
			 response.sendRedirect("ssoError?ssoError="+java.net.URLEncoder.encode(e.getMessage()));
		} catch(Exception e){
			//e.printStackTrace();
			response.sendRedirect("ssoError?ssoError="+java.net.URLEncoder.encode(e.getMessage()));
		}
		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
