package dcomp.es2.biblioteca.repository;

import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Livro;
import dcomp.es2.biblioteca.modelo.Usuario;
import dcomp.es2.biblioteca.repository_interface.EmprestimoRepositoryInterface;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoRepository extends Repository implements EmprestimoRepositoryInterface {
    private static EmprestimoRepository instance;


    public static EmprestimoRepository getInstance(){
        if (instance == null){
            instance = new EmprestimoRepository();
        }
        return instance;
    }

    public EmprestimoRepository() {
        entityManager = getEntityManager();
    }

    @Override
    public Emprestimo salvar(Emprestimo emprestimo) {
        try {
            entityManager.getTransaction().begin();
            emprestimo = entityManager.merge(emprestimo);
            entityManager.getTransaction().commit();
            return emprestimo;
        } catch (Exception ex) {
            //ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
        return  null;
    }

    @Override
    public List<Livro> getLivroEmprestados() {

        List<Livro> livros = new  ArrayList<>();
        List<Emprestimo> emprestimos = entityManager.createQuery("FROM " +
                Emprestimo.class.getName() + " e WHERE e.dataDevolucao is NULL ").getResultList();

        emprestimos.forEach(emprestimo -> {
            emprestimo.getLivros().forEach( livro -> {
                livros.add(livro);
            });
        });

        return livros;
    }

    @Override
    public List<Livro> getLivroEmAtraso() {

        List<Livro> livros = new  ArrayList<>();
        List<Emprestimo> emprestimos = entityManager.createQuery("FROM " +
                Emprestimo.class.getName() + " e WHERE e.dataPrevista < now()").getResultList();

        emprestimos.forEach( emprestimo -> {
            emprestimo.getLivros().forEach( livro -> {
                livros.add(livro);
            });
        });

        return livros;
    }

    public Emprestimo atualizar(Emprestimo Emprestimo) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(Emprestimo);
            entityManager.getTransaction().commit();
            return Emprestimo;
        } catch (Exception ex) {
            //ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
        return  null;
    }

    public void remove(Emprestimo emprestimo) {
        try {
            entityManager.getTransaction().begin();
            //livro = entityManager.find(Livro.class, livro.getId());
            emprestimo =  entityManager.merge(emprestimo);
            entityManager.remove(emprestimo);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    @Override
    public Emprestimo getById(final int id) {
        return entityManager.find(Emprestimo.class, id);
    }

    public List<Emprestimo> findAll() {
        return entityManager.createQuery("FROM " +
                Emprestimo.class.getName()).getResultList();
    }


}
