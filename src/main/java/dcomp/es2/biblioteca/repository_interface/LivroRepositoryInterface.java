package dcomp.es2.biblioteca.repository_interface;

import dcomp.es2.biblioteca.modelo.Livro;

public interface LivroRepositoryInterface {
    public Livro salvar(Livro livro);
    public Livro atualizar(Livro livro);
}
