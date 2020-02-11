package com.doukas.ioannis.ProductComponent;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<ProductBean, String> {

    @Query("SELECT * FROM ProductBean WHERE id=(?0)")
    ProductBean findProductById(String id);

    public List<ProductBean> findAll();
}