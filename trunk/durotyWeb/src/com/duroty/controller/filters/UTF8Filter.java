package com.duroty.controller.filters;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class UTF8Filter implements Filter {
	private static final String charset = Charset.defaultCharset().displayName();
	
    /**
     * DOCUMENT ME!
     */
    public void destroy() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     * @param chain DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
    	request.setCharacterEncoding(charset);    	
    	response.setCharacterEncoding(charset);
        chain.doFilter(request, response);
    }

    /**
     * DOCUMENT ME!
     *
     * @param filterConfig DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     */
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
