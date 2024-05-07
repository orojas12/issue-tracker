package dev.oscarrojas.issuetracker;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class IntegrationTestConfig {

    @Bean
    DataSource testDataSource() {
        DataSourceBuilder<HikariDataSource> builder = DataSourceBuilder.create().type(HikariDataSource.class);
        builder.driverClassName("org.sqlite.JDBC")
                .url("jdbc:sqlite::memory:")
                .username("")
                .password("");
        return builder.build();
    }
}
