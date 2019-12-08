package dcomp.es2.biblioteca.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Repository {

    protected EntityManager entityManager;

    EntityManager getEntityManager() {
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("bibliotecaPU_test");
        if (entityManager == null) {
            entityManager = factory.createEntityManager();
        }

        return entityManager;
    }


}
