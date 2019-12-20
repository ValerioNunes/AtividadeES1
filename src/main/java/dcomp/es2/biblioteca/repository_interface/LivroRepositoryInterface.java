package dcomp.es2.biblioteca.repository_interface;

import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Livro;

import java.util.List;

public interface LivroRepositoryInterface {
    public Livro getById(final int id);
    public Livro salvar(Livro livro);
    public Livro atualizar(Livro livro);
    public List<Emprestimo> historicoDeEmprestimosDo(Livro livro);
}
