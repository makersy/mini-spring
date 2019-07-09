package top.makersy.starter;

import org.apache.catalina.LifecycleException;
import top.makersy.beans.BeanFactory;
import top.makersy.core.ClassScanner;
import top.makersy.web.handler.HandlerManager;
import top.makersy.web.server.TomcatServer;

import java.io.IOException;
import java.util.List;

/**
 * Created by makersy on 2019
 */

public class MiniApplication {
    public static void run(Class<?> cls, String[] args) {
        System.out.println("Hello mini-spring!");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startTomcat();
            List<Class<?>> classList = ClassScanner.scanClass(cls.getPackage().getName());
            BeanFactory.initBean(classList);
            HandlerManager.resolveMappingHandler(classList);
//            classList.forEach(it -> System.out.println(it.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
