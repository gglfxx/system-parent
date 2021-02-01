package com.online.system.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 敏感字符过滤
 */
public class CharacterEncodingPerFilter implements Filter {
	
	protected String encoding = null;
	public static ArrayList<Pattern> filList = new ArrayList<>();
	
	//过滤地址
	private Set<String> excludesPattern;
	
	static {
		String strBegin = "(&|%26)(#|%23)60(;|%3b)"; //<
		String strEnd   = "(&|%26)(#|%23)62(;|%3b)"; //>
		ArrayList<String> dangerLst = new ArrayList<>();
		dangerLst.add("javascript");
		dangerLst.add("(<script)|(script>)");
		dangerLst.add("(" + strBegin + "script)|(script" + strEnd + ")");
		
		/**
		 以下是javascript的多种变体

   		("&#x6a;&#x61;&#x76;&#x61;&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;"); //javascript
		("%26%23x6a;%26%23x61;%26%23x76;%26%23x61;%26%23x73;%26%23x63;%26%23x72;%26%23x69;%26%23x70;%26%23x74;"); //javascript
		("%26%23x6a%3b%26%23x61%3b%26%23x76%3b%26%23x61%3b%26%23x73%3b%26%23x63%3b%26%23x72%3b%26%23x69%3b%26%23x70%3b%26%23x74%3b");//javascript
		 */
		//以下两行是javascript中十个字母("j","a","v","a"....."t")的拦截，认为用以下格式书写其中任何一个字母，都是有不正当企图
		dangerLst.add("(&|%26)(#|%23)x\\d[\\d|a](;|%3b)");		
		for (int i=0; i<dangerLst.size(); i++) {
			Pattern p = Pattern.compile((String)dangerLst.get(i));
			filList.add(p);
		}
   }

	protected FilterConfig filterConfig = null;

	protected boolean ignore = true;

	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	/**
	 * 字符过滤
	 * 1、首先对参数进行解密渲染到后台
	 * 2、对参数的值进行过滤
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		//字符编码设置
		if (ignore || (request.getCharacterEncoding() == null)) {
			String encoding = selectEncoding(request);
			if (encoding != null) {	
			   request.setCharacterEncoding(encoding);					
			   response.setContentType("text/html;charset=" + encoding);
			}
		}
        //防止20%和.的攻击,该类攻击会导致源代码泄露
		String strUrl =  req.getRequestURL().toString();
		//先转小写
		if(null != strUrl){
		    strUrl = strUrl.toLowerCase();
		}
		if (strUrl !=null && strUrl.indexOf(".html")>-1 && !".html".equals(strUrl.substring(strUrl.indexOf(".html")))) {
			System.out.println("拦截成功！=" + strUrl.substring(strUrl.indexOf(".html")));
			throw new RuntimeException("错误地址！");
		}

		//对传参进行解密 to do

		if (this.isExcludedUri(strUrl)) {
	    	chain.doFilter(request, response);
	    }else{
	    	if(validateSql(request)== false){
				resp.sendRedirect( req.getContextPath()+"/error/404.html" );          
				return;
			}
			
			if (validate(request, response)) {
				String retUrl = getWebUrl(req.getRequestURL().toString());
				String msg = "页面含有非法字符 <a href='" + retUrl + "'>返回</a>";
				resp.getWriter().println(msg);
				return;
			}
			chain.doFilter(request, response);
	    }
	}

	/**
	 * 校验过滤地址
	 * @param url
	 * @return
	 */
	private boolean isExcludedUri(String url) {
	      if (excludesPattern == null || excludesPattern.size() <= 0) {
	         return false;
	      }
	      Iterator<String> iterators = excludesPattern.iterator();
	      while(iterators.hasNext()){
	    	  url = url.trim();
	    	  String ex = iterators.next().trim();
		      if (url.toLowerCase().contains(ex.toLowerCase().replace("**","")))
		         return true;
	      }
	      return false;
	   }
	
	protected String selectEncoding(ServletRequest request) {
		return (this.encoding);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
		String value = filterConfig.getInitParameter("ignore");
		String param = filterConfig.getInitParameter("exclusions");
	    if (StringUtils.isNotBlank(param)) {
	        this.excludesPattern = new HashSet<>(Arrays.asList(param.split("\\s*,\\s*")));
	    }
		if (value == null)
			this.ignore = true;
		else if (value.equalsIgnoreCase("true")||value.equalsIgnoreCase("yes"))
			this.ignore = true;
		else
			this.ignore = false;
	}

	/**
	 * 过滤非法字符
	 *  
	 * @param request
	 * @param response
	 */
	public boolean validate(ServletRequest request, ServletResponse response) {
		HttpServletRequest req = (HttpServletRequest) request;
		Map<String, String[]> map = req.getParameterMap();
		//表单值检查		
		Collection<?> cs = map.values();
		Iterator<?> it = cs.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			String[] ss = (String[]) obj;
			for (int i = 0; i < ss.length; i++) {
				// 得到输入页面的文本
				String temp = ss[i];
				if (hasDangerous(temp)) {
					return true;
				}
			}
		}
        //表单名称检查
		cs = map.keySet();
		it = cs.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			String tname = (String) obj;
			if (hasDangerous(tname)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 检查字符串中是否有危害到系统的内容
	 * 
	 * @param vStr
	 * @return
	 */
	private static boolean hasDangerous(String vStr) {
		//检查是否有危险字符
		for (int i=0; i<filList.size(); i++) {
			Pattern px = (Pattern)filList.get(i);
			Matcher mx = px.matcher(vStr.toLowerCase()); //转为小写
			if (mx.find()) {				
				return true;
			}
		}
		return  false;
	}
	
	/**
	 * 根据URL取得网站地址
	 * @param href
	 * @return
	 */
	private static String getWebUrl(String href) {		
		href = href==null?"":href;		
		Pattern p = Pattern.compile("(http://.*?)/.*");
		Matcher m = p.matcher(href);		
		if (m.matches()) {
			return (String)m.group(1)+"/zjql";	
		} else {
			return "/";
		}
	}
	
    /**
     * 检查参数是否含有特殊字符(预防sql注入)
     */
    private static boolean validateSql(ServletRequest sreq) {
        HttpServletRequest request = (HttpServletRequest)sreq;
        Map<String, String[]> map = request.getParameterMap();
        Set<Entry<String, String[]>>  keSet = map.entrySet();
        for (Iterator<Entry<String, String[]>> itr = keSet.iterator(); itr.hasNext();) {
            Entry<String, String[]> me = itr.next();
            Object value = me.getValue();
            String[] values = new String[1];
            if (value instanceof String[]) {
            	values = (String[])value;
            } else {
                values[0] = value.toString();
            }
            for (int k = 0; k < values.length; k++) {
                String paramValue = values[k];
                if (StringUtils.isNotEmpty(paramValue)) {
                    if (paramValue.indexOf("alert") != -1 || paramValue.indexOf("import") != -1 || paramValue.indexOf("script") != -1 
                    		|| paramValue.indexOf("style") != -1 || paramValue.indexOf("\"") != -1 
                            || "\\|".indexOf(paramValue)  != -1 || "%".indexOf(paramValue)  != -1 
                            || "=".indexOf(paramValue) != -1 ) {
                    	System.out.println("输入含有敏感字符" + values[k]);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
