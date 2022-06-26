package com.example.scp.util;


import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;


import java.util.Set;

/**
 * ValidatorUtil入参校验工具类
 *
 * @Author TongChao
 * @Date 2022/6/2
 */
public class ValidatorUtil {

    /**
     * 使用hibernate的注解来进行验证
     */
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 功能描述: <br>
     * 〈注解验证参数〉
     *
     * @param obj
     */
    public static <T> void validate(T obj) {
        // 校验
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.getMessage());
        }
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            throw new RuntimeException();
        }
    }

}
