package top.makersy.web.handler;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by makersy on 2019
 */

public class MappingHandler {

    private String uri;
    private Method method;
    private Class<?> controller;
    private String[] args;

    public boolean handle(ServletRequest request, ServletResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String requestUri = ((HttpServletRequest)request).getRequestURI();
        if (!uri.equals(requestUri)) {
            return false;
        }

        Object[] parameters = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            parameters[i] = request.getParameter(args[i]);
        }

        Object ctl = controller.newInstance();
        Object res = method.invoke(ctl, parameters);
        response.getWriter().println(res.toString());
        return true;
    }
    public MappingHandler(String uri, Method method, Class<?> controller, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }


}
