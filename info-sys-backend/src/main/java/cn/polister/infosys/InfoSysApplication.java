package cn.polister.infosys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("cn.polister.infosys.mapper")
public class InfoSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfoSysApplication.class, args);
    }

}
