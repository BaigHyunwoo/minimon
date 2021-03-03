package com.minimon.repository;

import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class QueryDslRepositorySupportWrapper {

    private final PathBuilder<?> builder;

    private EntityManager entityManager;
    private Querydsl querydsl;

    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     */
    public QueryDslRepositorySupportWrapper(Class<?> domainClass) {

        Assert.notNull(domainClass, "Domain class must not be null!");
        this.builder = new PathBuilderFactory().create(domainClass);
    }

    /**
     * Setter to inject {@link EntityManager}.
     *
     * @param entityManager must not be {@literal null}.
     */
    @PersistenceContext(unitName = "yanadooMaster")
    public void setEntityManager(EntityManager entityManager) {

        Assert.notNull(entityManager, "EntityManager must not be null!");
        this.querydsl = new Querydsl(entityManager, builder);
        this.entityManager = entityManager;
    }

    /**
     * Callback to verify configuration. Used by containers.
     */
    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
    }

    /**
     * Returns the {@link EntityManager}.
     *
     * @return the entityManager
     */
    @Nullable
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Returns a fresh {@link JPQLQuery}.
     *
     * @param paths must not be {@literal null}.
     * @return the Querydsl {@link JPQLQuery}.
     */
    protected JPQLQuery<Object> from(EntityPath<?>... paths) {
        return getRequiredQuerydsl().createQuery(paths);
    }

    /**
     * Returns a {@link JPQLQuery} for the given {@link EntityPath}.
     *
     * @param path must not be {@literal null}.
     * @return
     */
    protected <T> JPQLQuery<T> from(EntityPath<T> path) {
        return getRequiredQuerydsl().createQuery(path).select(path);
    }

    /**
     * Returns a fresh {@link DeleteClause}.
     *
     * @param path
     * @return the Querydsl {@link DeleteClause}.
     */
    protected DeleteClause<JPADeleteClause> delete(EntityPath<?> path) {
        return new JPADeleteClause(getRequiredEntityManager(), path);
    }

    /**
     * Returns a fresh {@link UpdateClause}.
     *
     * @param path
     * @return the Querydsl {@link UpdateClause}.
     */
    protected UpdateClause<JPAUpdateClause> update(EntityPath<?> path) {
        return new JPAUpdateClause(getRequiredEntityManager(), path);
    }

    /**
     * Returns a {@link PathBuilder} for the configured domain type.
     *
     * @param <T>
     * @return the Querdsl {@link PathBuilder}.
     */
    @SuppressWarnings("unchecked")
    protected <T> PathBuilder<T> getBuilder() {
        return (PathBuilder<T>) builder;
    }

    /**
     * Returns the underlying Querydsl helper instance.
     *
     * @return
     */
    @Nullable
    protected Querydsl getQuerydsl() {
        return this.querydsl;
    }

    private Querydsl getRequiredQuerydsl() {

        if (querydsl == null) {
            throw new IllegalStateException("Querydsl is null!");
        }

        return querydsl;
    }

    private EntityManager getRequiredEntityManager() {

        if (entityManager == null) {
            throw new IllegalStateException("EntityManager is null!");
        }

        return entityManager;
    }
}