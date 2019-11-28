package com.gzcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value={Exception.class,RuntimeException.class,InternalAuthenticationServiceException.class})
	public ResponseEntity exceptionHandler(HttpServletRequest request, Exception e){
		e.printStackTrace();
		System.out.println("这里是全局异常处理器");
		if(e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			//取错误的第一条信息：
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			System.out.println("全局异常处理器的错误："+msg);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
