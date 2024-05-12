package dev.oscarrojas.issuetracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

@Configuration
public class DataConfig {

    // SQLite config
    @Bean
    public DataSource dataSource(@Value("${SQLITE_FILE_PATH}") String filepath) {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl("jdbc:sqlite:file:" + filepath);
        SQLiteConfig config = ds.getConfig();
        config.enforceForeignKeys(true);
        config.setDateClass(SQLiteConfig.DateClass.TEXT.getValue());
        ds.setConfig(config);
        return ds;
    }
}
