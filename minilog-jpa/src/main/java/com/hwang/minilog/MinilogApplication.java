package com.hwang.minilog;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinilogApplication {

	public static void main(String[] args) {

		// .env 파일 로드 및 System Property 설정
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});

		// 스프링 부트 애플리케이션 실행
		SpringApplication.run(MinilogApplication.class, args);
	}

}
