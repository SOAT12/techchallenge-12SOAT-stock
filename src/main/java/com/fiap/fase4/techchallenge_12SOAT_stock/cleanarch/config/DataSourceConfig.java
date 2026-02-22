package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DataSourceConfig {

    @Value("${aws.access.key}")
    private String awsAccessKeyId;

    @Value("${aws.secret.key}")
    private String awsSecretAccessKey;

    @Value("${aws.region}")
    private String awsSecretsManagerRegion;

    @Value("${aws.secret.name}")
    private String awsSecretsManagerSecretName;

    @Value("${app.environment}")
    private String environment;

    @Bean
    public DataSource getDataSource() throws Exception {
        String driverClassName;
        String url;
        String username;
        String password;

        if ("prod".equalsIgnoreCase(environment)) {
            SecretsManager.getInstance().builder(
                    awsAccessKeyId,
                    awsSecretAccessKey,
                    awsSecretsManagerRegion,
                    awsSecretsManagerSecretName
            );

            url = SecretsManager.getInstance().get("jdbc_url");
            username = SecretsManager.getInstance().get("username");
            password = SecretsManager.getInstance().get("password");
        } else {
            url = "jdbc:postgresql://localhost:5432/stock_db";
            username = "postgres";
            password = "password";
        }

        driverClassName = "org.postgresql.Driver";

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);

        DataSource dataSource = dataSourceBuilder.build();

        log.info("Database connection valid = {}", dataSource.getConnection().isValid(1000));
        return dataSource;
    }

}
