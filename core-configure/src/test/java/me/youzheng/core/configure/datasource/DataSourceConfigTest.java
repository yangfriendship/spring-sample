package me.youzheng.core.configure.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.core.parameters.P;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

class DataSourceConfigTest {

    ApplicationContextRunner applicationContextRunner;

    static final String[] DATA_SOURCE_PROPERTIES = {
            "spring.datasource.hikari.username=root"
            , "spring.datasource.hikari.password=dnwjd123"
            , "spring.datasource.hikari.jdbc-url=jdbc:mysql://some-url"
            , "spring.datasource.hikari.driver-class-name=com.mysql.cj.jdbc.Driver"
    };

    @BeforeEach
    void setUp() {
        this.applicationContextRunner = new ApplicationContextRunner();
    }

    @Test
    void 데이터소스_설정_테스트() {
        this.applicationContextRunner.withUserConfiguration(DataSourceConfig.class)
                .withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class))
                .withPropertyValues(
                        DATA_SOURCE_PROPERTIES
                )
                .run(context -> {
                    assertThat(context).getBean("defaultDataSource", DataSource.class).isNotNull();
                    final DataSource dataSource = context.getBean(DataSource.class);
                    assertThat(dataSource).isNotNull();
                    assertThat(dataSource).isInstanceOf(HikariDataSource.class);

                    final HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
                    assertThat(hikariDataSource).extracting(HikariDataSource::getPassword).isEqualTo("dnwjd123");
                    assertThat(hikariDataSource).extracting(HikariDataSource::getDriverClassName).isEqualTo("com.mysql.cj.jdbc.Driver");
                    assertThat(hikariDataSource).extracting(HikariDataSource::getUsername).isEqualTo("root");
                    assertThat(hikariDataSource).extracting(HikariDataSource::getJdbcUrl).isEqualTo("jdbc:mysql://some-url");
                });
    }

    @Test
    void 데이터소스_설정_테스트_다른_데이터소스가_존재() {
        final String otherDataSource = "otherDataSource";
        this.applicationContextRunner.withBean(otherDataSource, HikariDataSource.class)
                .withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class))
                .withUserConfiguration(DataSourceConfig.class)
                .withPropertyValues(
                        DATA_SOURCE_PROPERTIES
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(DataSource.class)
                            .describedAs("다른 DataSource 가 Bean 으로 등록되었다면 DataSourceConfig 의 DataSource 는 등록되지 않는다.");
                    assertThat(context.getBean(otherDataSource))
                            .describedAs("등록된 DataSource 는 '%s' 이여야한다.", otherDataSource)
                            .isNotNull();
                });
    }

    @Test
    void 데이터소스_설정_테스트_프로퍼티가_존재하지_않음() {
        this.applicationContextRunner
                .withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class))
                .withUserConfiguration(DataSourceConfig.class)
                // hikari 설정 프로퍼티가 존재하지 않음
                .run(context -> {
                    // DataSourceAutoConfiguration.class 에 의해 DataSource Bean 이 등록되기는 한다.
                    assertThat(context).getBean(DataSource.class).hasFieldOrPropertyWithValue("username", null);
                    assertThat(context).getBean(DataSource.class).hasFieldOrPropertyWithValue("password", null);
                    assertThat(context).getBean(DataSource.class).hasFieldOrPropertyWithValue("jdbcUrl", null);
                    assertThat(context).getBean(DataSource.class).hasFieldOrPropertyWithValue("driverClassName", null);
                });
    }

}