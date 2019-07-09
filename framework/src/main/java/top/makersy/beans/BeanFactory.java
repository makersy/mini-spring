package top.makersy.beans;

import top.makersy.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by makersy on 2019
 */

public class BeanFactory {

    //用一个类和实例的映射来模拟Bean工厂
    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();

    //从映射中获取Bean
    public static Object getBean(Class<?> cls){
        return classToBean.get(cls);
    }

    /**
     * Bean初始化，将表中所有类的依赖都加入 classToBean 这个映射中
     * @param classList 包中所有类的集合
     * @throws Exception
     */
    public static void initBean(List<Class<?>> classList) throws Exception {
        List<Class<?>> toCreate = new ArrayList<>(classList);  //新建1个list来存放，以免改变了传入List的值
        //一一初始化每一个类直到将类集合全部初始化完为止
        while (toCreate.size() != 0) {
            int remainSize = toCreate.size();
            for (int i = 0; i < toCreate.size(); i++) {
                if (finishCreate(toCreate.get(i))) {
                    toCreate.remove(i);
                }
            }
            // 当要创建的Bean列表长度始终不变时，说明出现了Bean之间相互依赖的问题。
            // 因为我们采取的策略是当前Bean创建失败就去创建下一个，这样后面的创建完了有来重新创建前
            // 面的依赖，如果这个阶段了list长度始终不变，那么说明Bean之间存在相互的依赖，A不创建B没
            // 法创建，B不创建A也没法创建。
            // 这里不进行处理，而是直接抛出异常
            if (toCreate.size() == remainSize) {
                throw new Exception("cycle dependency!");
            }
        }
    }

    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        if (!cls.isAnnotationPresent(Bean.class) && !cls.isAnnotationPresent(Controller.class)) {
            return true;
        }

        Object bean = cls.newInstance();
        //获取所有字段，处理其中需要自动注入的
        for (Field field : cls.getDeclaredFields()) {
            //处理 autowired 注解，自动注入
            if (field.isAnnotationPresent(Autowired.class)) {
                Class<?> fieldType = field.getType();
                Object relianBean = BeanFactory.getBean(fieldType);  //从BeanFactory中获取依赖的Bean
                //注意这里在BeanFactory中没有找到依赖的Bean时就返回false，放弃创建当前Bean，转而创建后面的。
                if (relianBean == null) {
                    return false;
                }
                field.setAccessible(true);  //关闭安全检查
                field.set(bean, relianBean);  //将bean的field值更新为relianBean，field值即需要注入的依赖
            }
        }
        classToBean.put(cls, bean);
        return true;
    }
}
