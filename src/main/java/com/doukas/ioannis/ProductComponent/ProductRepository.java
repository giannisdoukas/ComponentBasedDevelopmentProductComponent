package com.doukas.ioannis.ProductComponent;

import com.datastax.driver.core.Row;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.core.cql.RowMapper;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<ProductBean, String> {


//    ProductBean findProductById(String id){}

//    default List<ProductBean> findAll() {
//        CqlTemplate cqlTemplate = new CqlTemplate();
//        final List<ProductBean> all = cqlTemplate.query(
//                "SELECT * FROM ProductBean",
//                ProductMapper.INSTANCE
//        );
//        return all;
//    }

    enum ProductMapper implements RowMapper<ProductBean> {
        INSTANCE;

        public ProductBean mapRow(Row row, int rowNum) {
            return new ProductBean(
                    row.getString("id"),
                    row.getString("description"),
                    row.getFloat("price")
            );
        }
    }
}