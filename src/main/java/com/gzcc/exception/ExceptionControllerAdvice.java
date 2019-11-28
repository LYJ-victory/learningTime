package com.gzcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by ASUS on 2019/11/24.
 */
@ControllerAdvice
public class ExceptionControllerAdvice

{

    /**

     * 对验证约束异常进行拦截，返回约定的响应体

     */

    @ExceptionHandler(MethodArgumentNotValidException.class)

    @ResponseBody

    public ResponseEntity bindExceptionHandler(MethodArgumentNotValidException ex)   {

        BindingResult bindingResult = ex.getBindingResult();

        List<ObjectError> errors = bindingResult.getAllErrors();

        StringBuffer buffer = new StringBuffer();

        for (ObjectError error : errors) {

            buffer.append(error.getDefaultMessage()).append(" ");

        }

        return new ResponseEntity( buffer.toString(), HttpStatus.BAD_REQUEST);

    }



    /**

     * 参数类型转换错误

     */

    @ExceptionHandler(HttpMessageConversionException.class)

    @ResponseBody

    public ResponseEntity parameterTypeException(HttpMessageConversionException exception) {

        return new ResponseEntity(exception.getCause().getLocalizedMessage(),HttpStatus.BAD_REQUEST);

    }

}
