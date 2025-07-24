package com.demo.ecommerce.products;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public enum ProductExcludeFlag {
    INACTIVE {
        @Override
        public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            return builder.isTrue(root.get(Product_.active));
        }
    },
    DELETED {
        @Override
        public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            return builder.isNull(root.get(Product_.deletedAt));
        }
    };

    /**
     * Converts a {@link ProductExcludeFlag} into a JPA {@link Predicate}.
     *
     * @param root the root of the query
     * @param query the criteria query
     * @param builder the criteria builder
     * @return a predicate that represents the condition for excluding products based on the flag
     */
    public abstract Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder);
}
