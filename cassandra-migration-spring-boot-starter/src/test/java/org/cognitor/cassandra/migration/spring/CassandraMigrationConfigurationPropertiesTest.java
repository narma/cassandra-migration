package org.cognitor.cassandra.migration.spring;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.util.EnvironmentTestUtils.addEnvironment;

/**
 * @author Patrick Kranz
 */
public class CassandraMigrationConfigurationPropertiesTest {

    @Test
    public void shouldPopulatePropertiesWhenPropertiesFileGiven() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        addEnvironment(context, "cassandra.migration.script-location:cassandra/migrationpath");
        addEnvironment(context, "cassandra.migration.keyspace-name:test_keyspace");
        addEnvironment(context, "cassandra.migration.strategy:IGNORE_DUPLICATES");
        context.register(CassandraMigrationAutoConfiguration.class);
        context.refresh();
        CassandraMigrationConfigurationProperties properties =
                context.getBean(CassandraMigrationConfigurationProperties.class);
        assertThat(properties.getKeyspaceName(), is(equalTo("test_keyspace")));
        assertThat(properties.getScriptLocation(), is(equalTo("cassandra/migrationpath")));
        assertThat(properties.getStrategy(), is(equalTo(ScriptCollectorStrategy.IGNORE_DUPLICATES)));
    }

    @Test
    public void shouldReturnDefaultValuesWhenNoOptionalPropertiesGiven() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.register(CassandraMigrationAutoConfiguration.class);
        context.refresh();
        CassandraMigrationConfigurationProperties properties =
                context.getBean(CassandraMigrationConfigurationProperties.class);
        assertThat(properties.hasKeyspaceName(), is(false));
        assertThat(properties.getScriptLocation(), is(equalTo("cassandra/migration")));
        assertThat(properties.getStrategy(), is(equalTo(ScriptCollectorStrategy.FAIL_ON_DUPLICATES)));
    }
}