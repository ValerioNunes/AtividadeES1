package dcomp.es2.biblioteca.exemplos_codigos;

//package br.com.crudhibernate;

import dcomp.es2.biblioteca.modelo.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

    public class UsuarioJpaDAO {

        private static UsuarioJpaDAO instance;
        protected EntityManager entityManager;

        public static UsuarioJpaDAO getInstance(){
            if (instance == null){
                instance = new UsuarioJpaDAO();
            }

            return instance;
        }

        private UsuarioJpaDAO() {
            entityManager = getEntityManager();
        }

        private EntityManager getEntityManager() {
            EntityManagerFactory factory =
                    Persistence.createEntityManagerFactory("bibliotecaPU_test");
            if (entityManager == null) {
                entityManager = factory.createEntityManager();
            }

            return entityManager;
        }

        public Usuario getById(final int id) {
            return entityManager.find(Usuario.class, id);
        }

        @SuppressWarnings("unchecked")
        public List<Usuario> findAll() {
            return entityManager.createQuery("FROM " +
                    Usuario.class.getName()).getResultList();
        }

        public void persist(Usuario Usuario) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(Usuario);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                entityManager.getTransaction().rollback();
            }
        }

        public void merge(Usuario Usuario) {
            try {
                entityManager.getTransaction().begin();
                entityManager.merge(Usuario);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                entityManager.getTransaction().rollback();
            }
        }

        public void remove(Usuario Usuario) {
            try {
                entityManager.getTransaction().begin();
                Usuario = entityManager.find(Usuario.class, Usuario.getId());
                entityManager.remove(Usuario);
                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                entityManager.getTransaction().rollback();
            }
        }

        public void removeById(final int id) {
            try {
                Usuario Usuario = getById(id);
                remove(Usuario);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

