package book.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfiguration {

	// MyBatis 설정 정보 담는 객체
	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean
	/* 설정 파일(application.properties)에서 
     * "spring.datasource.hikari" 접두어가 
       붙은 값을 읽어 내부적으로 객체 생성 */
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    HikariConfig hikariConfig() {
        return new HikariConfig();
    }
	
	@Bean
	DataSource dataSource() {
        // DataSource 인터페이스를 상속받은 HikariDataSource 클래스 객체 반환
        DataSource dataSource = new HikariDataSource(hikariConfig());
        System.out.println(">>> " + dataSource);
        return dataSource;
	}
	
	 /* SqlSessionFactory : SqlSession 객체를 생성하는 역할을 담당하는 인터페이스로,
    DB 연결, 트랜잭션 관리, 맵퍼 파일의 위치 등 MyBatis 설정 정보를 포함함 */
	// SqlSession : MyBatis에서 DB와 상호작용하는 인터페이스
	@Bean
	SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(
            applicationContext.getResources("classpath:/mapper/**/sql-*.xml")
        );
		
		// MyBatis 설정 추가
		org.apache.ibatis.session.Configuration configuration 
			= new org.apache.ibatis.session.Configuration();
			        configuration.setMapUnderscoreToCamelCase(true);
			        sessionFactoryBean.setConfiguration(configuration);

		return sessionFactoryBean.getObject();
	}
	
	/* SqlSesstionTemplate : MyBatis와 스프링 프레임워크를 통합할 때 사용하는 클래스로,
	                SqlSession 인터페이스를 구현함 */
	@Bean
    SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
