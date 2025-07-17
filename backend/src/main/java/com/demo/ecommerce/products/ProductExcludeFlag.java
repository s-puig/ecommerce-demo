package com.demo.ecommerce.products;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public enum ProductExcludeFlag {
    INACTIVE {
        @Override
        public Predicate toPredicate(Root<Product> root, CriteriaQuery query, CriteriaBuilder builder) {
            return builder.isTrue(root.get("active"));
        }
    },
    DELETED {
        @Override
        public Predicate toPredicate(Root<Product> root, CriteriaQuery query, CriteriaBuilder builder) {
            return builder.isNull(root.get("deletedAt"));
        }
    };

    public abstract Predicate toPredicate(Root<Product> root, CriteriaQuery query, CriteriaBuilder builder);
}
