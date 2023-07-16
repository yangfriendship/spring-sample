package me.youzheng.core.configure.jpa;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class CoreSimpleJpaRepository<T, ID> extends SimpleJpaRepository<T, ID> {

    public CoreSimpleJpaRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(ID id) {
        return this.findById(id).isPresent();
    }

}