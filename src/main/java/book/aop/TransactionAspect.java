package book.aop;

import java.util.Arrays;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionAspect {

    // 트랜잭션 관리자
    @Autowired
    private PlatformTransactionManager transactionManager;

    // 트랜잭션 인터셉터 정의
    // 트랜잭션 관리자를 사용해서 트랜잭션 시작, 커밋, 롤백 등의 처리 수행
    @Bean
    TransactionInterceptor transactionAdvice() {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager);

        // TransactionAttributeSource 인터페이스 상속받는 MatchAlwaysTransactionAttributeSource 클래스 객체 생성
        // => 커밋, 롤백 등의 기준이 됨 (모든 메서드에 동일한 트랜잭션 속성 적용)
        MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();

        // 트랜잭션 이름, 롤백 규칙 등 트랜잭션 속성 정의 
        RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();

        transactionAttribute.setName("*");
        // Exception 발생하면 Rollback 되도록 기준 설정
        transactionAttribute.setRollbackRules(Arrays.asList(new RollbackRuleAttribute(Exception.class)));

        source.setTransactionAttribute(transactionAttribute);

        transactionInterceptor.setTransactionAttributeSource(source);
        return transactionInterceptor;
    }

    // AOP 포인트컷과 어드바이저 설정
    @Bean
    Advisor transactionAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        // 포인트 컷 설정
        pointcut.setExpression("execution(* book..service.*Impl.*(..))");

        return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
    }
} 