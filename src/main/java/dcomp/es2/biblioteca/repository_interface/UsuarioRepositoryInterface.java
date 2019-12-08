package dcomp.es2.biblioteca.repository_interface;

import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Usuario;

import java.util.List;

public interface UsuarioRepositoryInterface {
    public Usuario getById(final int id);
    public Usuario salvar(Usuario usuario);
    public Usuario atualizar(Usuario usuario);
}
