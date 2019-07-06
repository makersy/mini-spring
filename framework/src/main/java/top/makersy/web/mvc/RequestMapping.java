package top.makersy.web.mvc;

import java.lang.annotation.*;

/**
 * Created by makersy on 2019
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    String value();
}
