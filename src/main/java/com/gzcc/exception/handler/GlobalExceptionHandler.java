package com.gzcc.exception.handler;

/**
 * controller在filter之后，所以异常捕获不到，这里后面再想下怎么结合起来就能省略另外两个异常类的实现了
 */
public class GlobalExceptionHandler {

    //TODO
    //TODO
}
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    /**
//     * @param request
//     * @param exception
//     * @return
//     * @throws Exception
//     * @// TODO: 2018/4/25 参数未通过验证异常
//     */
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    public Object MethodArgumentNotValidHandler(HttpServletRequest request, MethodArgumentNotValidException exception) throws Exception {
//        //按需重新封装需要返回的错误信息
//        //List<StatusCode> invalidArguments = new ArrayList<>();
//        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
//        ResultObject resultMsg = ResultObject.dataMsg(exception.getBindingResult().getFieldError().getDefaultMessage(), StatusCode.VARIABLE_ERROR);
//        return resultMsg;
//    }
//
//    /**
//     * @param request
//     * @param exception
//     * @return
//     * @throws Exception
//     * @// TODO: 2018/4/25 无法解析参数异常
//     */
//    @ExceptionHandler(value = HttpMessageNotReadableException.class)
//    public Object HttpMessageNotReadableHandler(HttpServletRequest request, HttpMessageNotReadableException exception) throws Exception {
//        logger.info(exception.getMessage());
//        ResultObject resultMsg = ResultObject.dataMsg("参数无法正常解析", StatusCode.VARIABLE_ERROR);
//        return resultMsg;
//    }
//
//    /**
//     * @param exception
//     * @return
//     * @throws Exception
//     * @// TODO: 2018/4/25 处理token 过期异常
//     */
//    @ExceptionHandler(value = ExpiredJwtException.class)
//    public Object ExpiredJwtExceptionHandler(ExpiredJwtException exception) throws Exception {
//        logger.info(exception.getMessage());
//        ResultObject resultMsg = ResultObject.dataMsg("登录已过期！", StatusCode.FORBIDDEN);
//        return resultMsg;
//    }
//
//    /**
//     * @param request
//     * @param exception
//     * @return
//     * @throws Exception
//     * @// TODO: 2018/4/25 方法访问权限不足异常
//     */
//    @ExceptionHandler(value = AccessDeniedException.class)
//    public Object AccessDeniedExceptionHandler(AccessDeniedException exception) throws Exception {
//        logger.info(exception.getMessage());
//        ResultObject resultMsg = ResultObject.dataMsg("权限不足！", StatusCode.FORBIDDEN);
//        return resultMsg;
//    }
//
//    @ExceptionHandler(value = NoHandlerFoundException.class)
//    public Object NoHandlerFoundExceptionHandler(NoHandlerFoundException exception) throws Exception {
//        logger.info(exception.getMessage());
//        return ResultObject.dataMsg("链接不存在", StatusCode.NOT_FOUND);
//    }
//    /**
//     * 处理自定义异常
//     */
//    @ExceptionHandler(value = WelendException.class)
//    public Object WelendExceptionHandler(WelendException e) {
//        ResultObject r = new ResultObject();
//        r.setStatus(String.valueOf(e.getCode()));
//        r.setMessage(e.getMessage());
//        return r;
//    }
//
//    @ExceptionHandler(value = AuthenticationException.class)
//    public Object AuthenticationExceptionHandler(AuthenticationException e) {
//        return ResultObject.dataMsg(e.getLocalizedMessage(),StatusCode.FORBIDDEN);
//    }
//
//    @ExceptionHandler(value = DuplicateKeyException.class)
//    public Object DuplicateKeyExceptionHandler(DuplicateKeyException e) throws Exception {
//        logger.error(e.getMessage(), e);
//        return ResultObject.codeMsg(StatusCode.EXISTED);
//    }
//
//    @ExceptionHandler(value = BadCredentialsException.class)
//    public Object BadCredentialsExceptionHandler(BadCredentialsException e) throws Exception {
//        logger.error(e.getMessage(), e);
//        return ResultObject.codeMsg(StatusCode.AUTH_ERROR);
//    }
//
//    @ExceptionHandler(value = Exception.class)
//    public Object ExceptionHandler(Exception e) throws Exception {
//        logger.error(e.getMessage(), e);
//        return ResultObject.codeMsg(StatusCode.FAILED);
//    }
//}
