package kr.co.jsol.jem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.TimeZone

@SpringBootApplication
@EnableJpaAuditing // JpaAuditing 생성시간 수정시간 사용을 위함
class ApiApplication

fun main(args: Array<String>) {
    // 특정 타임존으로 로컬 타임 설정
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    runApplication<ApiApplication>(*args)
}
