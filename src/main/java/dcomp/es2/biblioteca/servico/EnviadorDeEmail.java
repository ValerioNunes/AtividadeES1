package dcomp.es2.biblioteca.servico;

import dcomp.es2.biblioteca.modelo.Usuario;
import dcomp.es2.biblioteca.repository.EmprestimoRepository;

public class EnviadorDeEmail {

    public Usuario enviarEmailPara(Usuario usuario, String mensagem) {
        if (usuario.getEmail() == null) {
            System.out.println("Usuário sem email cadastrado");
            return usuario;
        }
        System.out.println(usuario.toString());
        System.out.println("Email para " + usuario.getNome() + " Enviado com sucesso!");
        return null;
    }


}
