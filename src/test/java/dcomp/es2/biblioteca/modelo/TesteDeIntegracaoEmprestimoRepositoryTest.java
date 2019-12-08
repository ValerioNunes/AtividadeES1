package dcomp.es2.biblioteca.modelo;

import dcomp.es2.biblioteca.builder.EmprestimoBuilder;
import dcomp.es2.biblioteca.builder.LivroBuilder;
import dcomp.es2.biblioteca.repository.EmprestimoRepository;
import dcomp.es2.biblioteca.repository.LivroRepository;
import dcomp.es2.biblioteca.repository.PagamentoRepository;
import dcomp.es2.biblioteca.repository.UsuarioRepository;
import dcomp.es2.biblioteca.servico.EmprestimoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TesteDeIntegracaoEmprestimoRepositoryTest {

    Emprestimo emprestimo;
    Usuario usuario;
    Livro livro1,livro2,livro3;
    List<Livro> livros;

    @Before
    public void inicializacaoTest(){

        usuario = new Usuario();
        usuario.setNome("Tito");
        UsuarioRepository.getInstance().salvar(usuario);

        System.out.println("Expo: "+ usuario.getId());

        livro1 = LivroBuilder.umLivro().comNome("Livro1").constroi();
        livro1.setAutor("Autor1");

        livro2 = LivroBuilder.umLivro().comNome("Livro2").constroi();
        livro2.setAutor("Autor2");

        livro3 = LivroBuilder.umLivro().comNome("Livro2").constroi();
        livro3.setAutor("Autor2");

        LivroRepository.getInstance().salvar(livro1);
        LivroRepository.getInstance().salvar(livro2);
        LivroRepository.getInstance().salvar(livro3);

        livros = new ArrayList<>();

        livros.add(livro1);
        livros.add(livro2);
        livros.add(livro3);
        System.out.println("Tamanho: " +livros.size());

        EmprestimoService emprestimoService =  new EmprestimoService();
        System.out.println(usuario.toString());
        livros.forEach( livro -> {
            System.out.println(livro.toString());
        });

        emprestimo = emprestimoService.emprestarLivro(usuario,livros);

        PagamentoRepository.getInstance().salvar(emprestimo.getPagamento());



    }

    @After
    public void FinalizarTest(){
      EmprestimoRepository.getInstance().remove(emprestimo);
    }

    @Test
    public  void RealizarCadastroDoEmprestimoNoBancodeDadostest() {
        emprestimo = EmprestimoRepository.getInstance().salvar(emprestimo);
        System.out.println(emprestimo.toString());
        Emprestimo emprestimo1 = EmprestimoRepository.getInstance().getById(emprestimo.getId());
        System.out.println(emprestimo1.toString());



        Assertions.assertEquals(emprestimo,emprestimo1);
    }




}
