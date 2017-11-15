package tmall.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


public class BackServletFilter implements Filter {

	public BackServletFilter() {

	}

	public void destroy() {

	}

	// 判断地址是否以admin_开头，如果是则跳转到Servlet页面，如果不是不做操作
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hRequest = (HttpServletRequest) request;
		HttpServletResponse hResponse = (HttpServletResponse) response;

		String contextPath = hRequest.getServletContext().getContextPath();
		String uri = hRequest.getRequestURI();
		uri = StringUtils.remove(uri, contextPath);
		if (uri.startsWith("/admin_")) {
			String servletPath = StringUtils.substringBetween(uri, "_", "_") + "Servlet";
			String method = StringUtils.substringAfterLast(uri, "_");
			hRequest.setAttribute("method", method);
			request.getRequestDispatcher("/" + servletPath).forward(hRequest, hResponse);
			return;
		}
		chain.doFilter(hRequest, hResponse);
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
