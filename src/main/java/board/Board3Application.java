package board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 예전 자바 7버전에서는 시간관련 클래스가 사용하기 힘들었음
// 자바 8버전에서 JSR-310이 표준 명세로 등록되었지만 MySql에서 오류가 발생할 가능성이 존재함
// -> 그래서 Jsr310JpaConverters 클래스를 사용하여 해결함
@EnableJpaAuditing
@EntityScan(
		basePackageClasses = {Jsr310JpaConverters.class},
		basePackages = {"board"})

@SpringBootApplication
public class Board3Application {

	public static void main(String[] args) {
		SpringApplication.run(Board3Application.class, args);
	}

}
