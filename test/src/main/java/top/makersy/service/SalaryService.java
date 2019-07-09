package top.makersy.service;

import top.makersy.beans.Bean;

/**
 * Created by makersy on 2019
 */

@Bean
public class SalaryService {
    public Integer calSalary(Integer experience) {
        return experience * 5000;
    }
}
