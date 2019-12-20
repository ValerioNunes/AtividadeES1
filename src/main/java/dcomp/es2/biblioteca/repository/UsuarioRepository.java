package dcomp.es2.biblioteca.repository;

import dcomp.es2.biblioteca.modelo.Usuario;
import dcomp.es2.biblioteca.repository_interface.UsuarioRepositoryInterface;

public class UsuarioRepository extends Repository  implements UsuarioRepositoryInterface {
    
    private static UsuarioRepository instance;

    public static UsuarioRepository getInstance(){
        if (instance == null){
            instance = new UsuarioRepository();
        }
        return instance;
    }

    public UsuarioRepository() {
        entityManager = getEntityManager();
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(usuario);
            entityManager.getTransaction().commit();
            return usuario;
        } catch (Exception ex) {
            //ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
        return  null;
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(usuario);
            entityManager.getTransaction().commit();
            return usuario;
        } catch (Exception ex) {
            //ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
        return  null;
    }

    public void remove(Usuario usuario) {
        try {
            entityManager.getTransaction().begin();
            usuario = entityManager.find(Usuario.class, usuario.getId());
            entityManager.remove(usuario);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    @Override
    public Usuario getById(final int id) {
        return entityManager.find(Usuario.class, id);
    }
}
