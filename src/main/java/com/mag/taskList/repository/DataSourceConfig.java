package com.mag.taskList.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Фабрика.
 * Данная конфигурация позволяет получить из пула подключений
 * необходимое подключение (connection).
 */
@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final DataSource dataSource;

    public Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
}
