package top.makersy.controllers;

import top.makersy.beans.Autowired;
import top.makersy.service.SalaryService;
import top.makersy.web.mvc.Controller;
import top.makersy.web.mvc.RequestMapping;
import top.makersy.web.mvc.RequestParam;

/**
 * Created by makersy on 2019
 */

@Controller
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @RequestMapping("/get_salary.json")
    public Integer getSalary(@RequestParam("name") String name, @RequestParam("experience") String experience) {
        return salaryService.calSalary(Integer.parseInt(experience));
    }
}
