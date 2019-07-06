package top.makersy.web.server;


import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import top.makersy.web.servlet.DispatcherServlet;

/**
 * Created by makersy on 2019
 */

public class TomcatServer {

    private Tomcat tomcat;
    private String[] args;

    public TomcatServer(String[] args) {
        this.args = args;
    }

    public void startTomcat() throws LifecycleException {
        tomcat = new Tomcat();
        tomcat.setPort(6699);
        tomcat.start();
        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());  //默认监听器
        DispatcherServlet servlet = new DispatcherServlet();
        Tomcat.addServlet(context, "dispatcherServlet", servlet).setAsyncSupported(true);  //支持异步的servlet
        context.addServletMappingDecoded("/", "dispatcherServlet");  //为Servlet绑定映射
        tomcat.getHost().addChild(context);


        Thread awaitThread = new Thread("tomcat_await_thread"){
            @Override
            public void run() {
                TomcatServer.this.tomcat.getServer().await();  //声明该线程一直在等待
            }
        };
        //设置为非守护线程
        awaitThread.setDaemon(false);
        //使该线程等待
        awaitThread.start();
    }
}
