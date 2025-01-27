package book.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/* SpringBoot에서는 @EnableAspectAutoProxy 어노테이션을 추가하지 않아도, 
자동으로 AOP 설정을 활성화함 */
@Aspect
@Slf4j
@Component
public class LoggerAspect {

    @Pointcut("execution(* book..controller.*Controller.*(..)) || execution(* book..service.*ServiceImpl.*(..)) || execution(* book..mapper.*Mapper.*(..))")
    private void loggerTarget() {

    }

    @Around("loggerTarget()")
    public Object logPrinter(ProceedingJoinPoint joinPoint) throws Throwable{
        String type = "";		// Controller, Service, Mapper 구분
        String className = joinPoint.getSignature().getDeclaringTypeName();		// 클래스 이름
        String methodName = joinPoint.getSignature().getName();		// 메소드 이름

        if(className.indexOf("Controller") > -1) {
            type = "[Controller]";
        }
        else if(className.indexOf("Service") > -1) {
            type = "[Service]";
        }
        if(className.indexOf("Mapper") > -1) {
            type = "[Mapper]";
        }
        log.debug(type + " " + className + "." + methodName);
        return joinPoint.proceed();
    }
}
