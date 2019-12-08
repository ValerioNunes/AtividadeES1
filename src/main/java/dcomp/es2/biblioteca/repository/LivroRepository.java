package dcomp.es2.biblioteca.repository;
import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Livro;
import dcomp.es2.biblioteca.modelo.Usuario;
import dcomp.es2.biblioteca.repository_interface.LivroRepositoryInterface;

import java.util.ArrayList;
import java.util.List;

public class LivroRepository  extends Repository  implements LivroRepositoryInterface {
    
    private static LivroRepository instance;

    public static LivroRepository getInstance(){
        if (instance == null){
            instance = new LivroRepository();
        }
        return instance;
    }

    public LivroRepository() {
        entityManager = getEntityManager();
    }

    @Override
    public Livro salvar(Livro livro) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(livro);
            entityManager.getTransaction().commit();
            return livro;
        } catch (Exception ex) {
            //ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
        return  null;
    }

    @Override
    public Livro atualizar(Livro livro) {
        try {
            entityManager.getTransaction().begin();
            livro = entityManager.merge(livro);
            entityManager.getTransaction().commit();
            return livro;
        } catch (Exception ex) {
            //ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
        return  null;
    }

    @Override
    public List<Emprestimo> historicoDeEmprestimosDo(Livro livro) {
        List<Emprestimo> emprestimos = EmprestimoRepository.getInstance().findAll();
        List<Emprestimo> emprestimosSelect = new ArrayList<>();
        emprestimos.forEach(emprestimo2 -> {
           emprestimo2.getLivros().forEach(livro1 -> {
               if(livro.getId() == livro1.getId() )
                emprestimosSelect.add(emprestimo2);
           });
        });
        return emprestimosSelect;
    }

    public void remove(Livro livro) {
        try {
            entityManager.getTransaction().begin();
            //livro = entityManager.find(Livro.class, livro.getId());
            livro =  entityManager.merge(livro);
            entityManager.remove(livro);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    @Override
    public Livro getById(final int id) {
        return entityManager.find(Livro.class, id);
    }
}
