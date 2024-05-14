package dev.oscarrojas.issuetracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

@Configuration
public class IntegrationTestConfig {

    // SQLite config
    @Bean
    public DataSource dataSource() {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl("jdbc:sqlite::memory:");
        SQLiteConfig config = ds.getConfig();
        config.enforceForeignKeys(true);
        config.setDateClass(SQLiteConfig.DateClass.TEXT.getValue());
        ds.setConfig(config);
        return ds;
    }

}
