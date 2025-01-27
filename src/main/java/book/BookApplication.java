package book;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

//@Slf4j
@SpringBootApplication
public class BookApplication {

	// 로그 출력
   // private static Logger log = LoggerFactory.getLogger(BookApplication.class);
	
	public static void main(String[] args) {
		/*log.trace("trace");
		log.debug("debug");
		log.info("info");
		log.warn("warn");
		log.error("error");*/
		
		SpringApplication.run(BookApplication.class, args);
	}

}
