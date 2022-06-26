package com.example.scp.test;

import com.example.scp.test.entity.Person;
import com.example.scp.util.ValidatorUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/test")
public class Controller {

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test(@RequestBody Person person) {

        System.out.println(person.toString());
        return "访问成功";
    }

    @RequestMapping(value = "/test111", method = RequestMethod.GET)
    public void test111(@RequestBody Person person) {
        System.out.println("==============");
        ValidatorUtil.validate(person);
        System.out.println("==============");
    }
}
