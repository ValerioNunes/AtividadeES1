package dcomp.es2.biblioteca.repository_interface;

import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Livro;
import dcomp.es2.biblioteca.repository.LivroRepository;

import java.util.List;

public interface EmprestimoRepositoryInterface {

    public Emprestimo salvar(Emprestimo emprestimo);
    public List<Livro> getLivroEmprestados();
    public List<Livro> getLivroEmAtraso();
    public Emprestimo getById(final int id);

}
