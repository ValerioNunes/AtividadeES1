package dcomp.es2.biblioteca.repository_interface;

import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Livro;

import java.util.List;

public interface EmprestimoRepositoryInterface {
    public Emprestimo salvar();
    public List<Livro> getLivroEmprestador();
    public List<Livro> getLivroEmAtraso();
}
