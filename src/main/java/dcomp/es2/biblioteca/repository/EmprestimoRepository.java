package dcomp.es2.biblioteca.repository;

import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Livro;
import dcomp.es2.biblioteca.repository_interface.EmprestimoRepositoryInterface;

import java.util.List;

public class EmprestimoRepository implements EmprestimoRepositoryInterface {
    @Override
    public Emprestimo salvar() {
        return null;
    }

    @Override
    public List<Livro> getLivroEmprestador() {
        return null;
    }

    @Override
    public List<Livro> getLivroEmAtraso() {
        return null;
    }
}
