package dcomp.es2.biblioteca.repository_interface;

import dcomp.es2.biblioteca.modelo.Usuario;

public interface UsuarioRepositoryInterface {
    public Usuario getById(final int id);
    public Usuario salvar(Usuario usuario);
    public Usuario atualizar(Usuario usuario);
}
