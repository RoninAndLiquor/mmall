package com.mmall.aspect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.google.common.collect.Maps;

@Component
@Aspect
public class LogAspect {
	
	private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);
	//String date = "[ "+new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date())+" ]  ~  ";
	
	private String getDateStr(){
		return "[ "+new SimpleDateFormat("yyyy-dd-MM HH:mm:ss:SSS").format(new Date())+" ]  ~  ";
	}
	/**
	 * 
	 * TODO 方法作用：切入点
	 * 匹配high.controller包及其子包下的所有类的所有方法
	 * @Author: 蒋帅锋
	 * @Date: 2017年12月26日 下午5:17:21
	 */
	@Pointcut("execution(* com.mmall.controller..*.*(..))")
	public void pointcut(){}
	
	/**
	 * 
	 * TODO 方法作用：前置通知
	 * @param point
	 * @Author: 蒋帅锋
	 * @Date: 2017年12月26日 下午5:11:58
	 */
	/*@Before(value="pointcut()")
	public void before(JoinPoint point){
		LOG.info(""+getDateStr()+"前置通知");
		//获取目标方法的参数信息
		Object[] args = point.getArgs();
		//AOP代理类的信息
		Object this1 = point.getThis();
		//代理的目标对象
		Object target = point.getTarget();
		//用的最多  通知的签名
		Signature signature = point.getSignature();
		//代理的方法
		String name = signature.getName();
		LOG.info(""+getDateStr()+"代理的方法:"+name);
		//AOP代理类的名字
		String declaringTypeName = signature.getDeclaringTypeName();
		LOG.info(""+getDateStr()+"AOP代理类的名字:"+declaringTypeName);
		//AOP代理类(class)信息
		Class declaringType = signature.getDeclaringType();
		//获取RequestAttributes
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		//从获取的RequestAttributes中获取HttpServletRequest信息
		HttpServletRequest request  = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
		String ip = "";
		if(request.getHeader("x-forwarded-for")==null){
			ip = request.getRemoteAddr();
		}else{
			ip = request.getHeader("x-forwarded-for");
		}
		LOG.info(""+getDateStr()+"訪問者的IP:"+ip);
		//如果想要获取Session的话 可以这样写
		HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
		Enumeration<String> enumeration = request.getParameterNames();
		Map<String,String> paramMap = Maps.newHashMap();
		while(enumeration.hasMoreElements()){
			String param = enumeration.nextElement();
			paramMap.put(param, request.getParameter(param));
		}
		String str = JSON.toJSONString(paramMap);
		if(args.length>0){
			LOG.info(getDateStr()+"param:"+str);
		}
	}*/
	
 
	/**
	 * 
	 * TODO 方法作用：后置返回通知 
	 * 这里需要注意的是: 
     *      如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息 
     *      如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数 
     * returning 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值 
	 * @param joinPoint
	 * @param keys
	 * @Author: 蒋帅锋
	 * @Date: 2017年12月26日 下午5:14:35
	 */
    @AfterReturning(value = "pointcut()",returning = "keys")  
    public void doAfterReturningAdvice1(JoinPoint joinPoint,Object keys){  
  
        LOG.info(getDateStr()+"返回值:"+new Gson().toJson(keys));
        String name = joinPoint.getSignature().getName();
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        LOG.info(getDateStr()+declaringTypeName+"."+name);
        System.err.println("------------------------------------END------------------------------------");
        System.gc();
    }  
  
    /*@AfterReturning(value = "pointcut()",returning = "keys",argNames = "keys")  
    public void doAfterReturningAdvice2(String keys){  
  
        LOG.info("*** 第二个后置返回通知的返回值 ***："+keys);  
    } */ 
    
    /**
     * 
     * TODO 方法作用：后置异常通知
     * 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法； 
     * throwing 限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行， 
     * 对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
     * @param joinPoint
     * @param exception
     * @Author: 蒋帅锋
     * @Date: 2017年12月26日 下午5:15:58
     */
    /*@AfterThrowing(value = "pointcut()",throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint,Throwable exception){  
        //目标方法名：  
        LOG.info(joinPoint.getSignature().getName()+"  *****  "+exception.getMessage());  
        if(exception instanceof NullPointerException){  
            LOG.info("发生了空指针异常!!!!!");  
        }  
    } */

    
    /**
     *  
     * TODO 方法作用：环绕通知
     * 环绕通知： 
     *   环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。 
     *   环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型 
     * @param proceedingJoinPoint
     * @return
     * @Author: 蒋帅锋
     * @Date: 2017年12月26日 下午5:16:52
     */
    @Around("pointcut()")  
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
		Signature signature = proceedingJoinPoint.getSignature();
		String name = signature.getName();
		String declaringTypeName = signature.getDeclaringTypeName();
		System.err.println("-----------------------------------START-----------------------------------");
		Object[] args = proceedingJoinPoint.getArgs();
		LOG.info(""+getDateStr()+"环绕通知的目标方法名:"+name);
		LOG.info(""+getDateStr()+"环绕通知的目标方法参数:"+new Gson().toJson(args));
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        Object attribute = session.getAttribute("user");
        try {//obj之前可以写目标方法执行前的逻辑  
            Object obj = proceedingJoinPoint.proceed();//调用执行目标方法  
            return obj;  
        } catch (Throwable throwable) {  
            //throwable.printStackTrace();
        	LOG.info(getDateStr()+"出现异常了："+throwable.getMessage());
        }
        return null;  
    }  
}
