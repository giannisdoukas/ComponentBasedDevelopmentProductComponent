package com.doukas.ioannis.ProductComponent;

public class ProductBean {
    private Long id;
    private String description;
    private float price;

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public static class ProductBeanBuilder{
        ProductBean productBean;
        public ProductBeanBuilder(){
            productBean = new ProductBean();
        }

        public void setPrice(float price){
            productBean.price = price;
        }

        public void setDescription(String description){
            productBean.description = description;
        }

        public void setId(Long id){
            productBean.id = id;
        }

        public ProductBean build(){
            return productBean;
        }
    }
}
