package com.doukas.ioannis.ProductComponent;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

//@Table(caseSensitiveKeyspace = false,
//        caseSensitiveTable = false)
@Table("productbean")
public class ProductBean {
    @PrimaryKey
    private final String id;
    private final String description;
    private final Float price;

    public ProductBean(String id, String description, Float price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public Float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductBean) {
            ProductBean p2 = (ProductBean) obj;
            return id.equals(p2.id) && price.equals(p2.price) && description.equals(p2.description);
        } else {
            return false;
        }

    }
}
