package top.itreatment.comparedir.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(value = "本地IP过滤",urlPatterns = "compare/**")
public class RequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String remoteAddr = servletRequest.getRemoteAddr();
        if (remoteAddr.equals("127.0.0.1")) {
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            System.out.println("过滤掉：remoteAddr = " + remoteAddr);
        }
    }
}
