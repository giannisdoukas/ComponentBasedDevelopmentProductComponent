package com.doukas.ioannis.ProductComponent;

import com.oracle.tools.packager.Log;
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
import java.util.Set;

@Configuration
@EnableCassandraRepositories(basePackages = "com.doukas.ioannis.ProductComponent")
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
    public CassandraMappingContext mappingContext() throws Exception{

        CassandraMappingContext mappingContext = new CassandraMappingContext();
        mappingContext.setInitialEntitySet(getInitialEntitySet());

        return mappingContext;
    }

    @Bean
    public CassandraConverter converter() throws Exception {
        return new MappingCassandraConverter(mappingContext());
    }

    /*
     * Factory bean that creates the com.datastax.driver.core.Session instance
     */
    @Override
    @Bean
    public CassandraSessionFactoryBean session() {

        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster().getObject());
        session.setKeyspaceName(keyspace);
        try{
            session.setConverter(converter());
        } catch (Exception e){
            Log.info(e.toString());
        }
        session.setSchemaAction(SchemaAction.CREATE_IF_NOT_EXISTS);

        return session;
    }

    @Override
    protected String getKeyspaceName() {
        return "mykeyspace";
    }

    @Override
    protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
        return CassandraEntityClassScanner.scan(getEntityBasePackages());
    }

//    @Override
//    public String[] getEntityBasePackages() {
//        return new String[] { "com.doukas.ioannis.ProductComponent" };
//    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    //    @Bean
//    public ProductController productController(){
//        return new ProductController();
//    }

}
