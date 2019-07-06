package top.makersy.web.mvc;

import java.lang.annotation.*;

/**
 * Created by makersy on 2019
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)  //保存级别
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    String value();
}
