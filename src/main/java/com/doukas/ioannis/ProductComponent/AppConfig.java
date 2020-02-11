package com.doukas.ioannis.ProductComponent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.*;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan("com.doukas.ioannis.ProductComponent")
@EnableCassandraRepositories(basePackages = {"com.doukas.ioannis.ProductComponent"})
public class AppConfig extends AbstractCassandraConfiguration {
    private String keyspace = "mykeyspace";

    @Bean
    public CassandraCqlClusterFactoryBean cluster() {

        CassandraCqlClusterFactoryBean cluster = new CassandraCqlClusterFactoryBean();
        cluster.setKeyspaceCreations(getKeyspaceCreations());
        cluster.setContactPoints("0.0.0.0");
        cluster.setPort(32779);
        cluster.setUsername("cassandra");
        cluster.setJmxReportingEnabled(false);

        return cluster;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        final CreateKeyspaceSpecification specification =
                CreateKeyspaceSpecification.createKeyspace(keyspace)
                        .ifNotExists()
                        .with(KeyspaceOption.DURABLE_WRITES, true)
                        .withSimpleReplication();
        List<CreateKeyspaceSpecification> l = new ArrayList<CreateKeyspaceSpecification>();
        l.add(specification);
        return l;
    }

    @Override
    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
        DropKeyspaceSpecification dropKeyspaceSpecification = DropKeyspaceSpecification.dropKeyspace(keyspace);
        List<DropKeyspaceSpecification> res = new ArrayList<DropKeyspaceSpecification>();
        res.add(dropKeyspaceSpecification);
        return res;
    }

    @Bean
    public CassandraMappingContext mappingContext() {

        BasicCassandraMappingContext mappingContext =  new BasicCassandraMappingContext();
        mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cluster().getObject(), "mykeyspace"));

        return mappingContext;
    }

    @Bean
    public CassandraConverter converter() {
        return new MappingCassandraConverter(mappingContext());
    }

    /*
     * Factory bean that creates the com.datastax.driver.core.Session instance
     */
    @Bean
    public CassandraSessionFactoryBean session() {

        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster().getObject());
        session.setKeyspaceName(keyspace);
        session.setConverter(converter());
        session.setSchemaAction(SchemaAction.CREATE_IF_NOT_EXISTS);

        return session;
    }

    @Override
    protected String getKeyspaceName() {
        return "mykeyspace";
    }

//    @Bean
//    public ProductController productController(){
//        return new ProductController();
//    }

}
