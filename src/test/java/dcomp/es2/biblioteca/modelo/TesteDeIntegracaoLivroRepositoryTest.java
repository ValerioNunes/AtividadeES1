package dcomp.es2.biblioteca.modelo;

import dcomp.es2.biblioteca.builder.LivroBuilder;
import dcomp.es2.biblioteca.repository.EmprestimoRepository;
import dcomp.es2.biblioteca.repository.LivroRepository;
import dcomp.es2.biblioteca.repository.PagamentoRepository;
import dcomp.es2.biblioteca.repository.UsuarioRepository;
import dcomp.es2.biblioteca.servico.EmprestimoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;

public class TesteDeIntegracaoLivroRepositoryTest {



//    @Test Usando o Mock
//    public  void RealizarCadastroDoLivroNoBancodeDadostest() {
//
//        livroRepository = Mockito.mock(LivroRepository.class);
//        Livro livro = mock(Livro.class);
//        livro.setAutor("Jose de Alencar");
//        livro.setTitulo("Iracema");
//
//        Livro livroSalvo = livroRepository.salvar(livro);
//
//        when(livroRepository.getById(livro.getId())).thenReturn(livro);
//
//        Assertions.assertEquals(livro,livroSalvo);
//    }




    @Test
    public  void RealizarCadastroDoLivroNoBancodeDadostest() {
        Livro livro = LivroBuilder.umLivro().comNome("Iracema").constroi();
        livro.setAutor("Jose de Alencar");
        LivroRepository.getInstance().salvar(livro);

        Livro livroSalvo = LivroRepository.getInstance().getById(livro.getId());
        Assertions.assertEquals(livro,livroSalvo);
    }

    @Test
    public void RealizarAtualizacaoCadastroDoLivroNoBancodeDados() {

        Livro livro = LivroBuilder.umLivro().comNome("Iracema").constroi();
        livro.setAutor("Jose de Alencar");
        LivroRepository.getInstance().salvar(livro);

        String livroValoresIniciais = livro.toString();
        System.out.println(livroValoresIniciais);
        Livro livroAlterado = LivroRepository.getInstance().getById(livro.getId());
        System.out.println(livroAlterado);
        livroAlterado.setAutor("José de Alencar");
        LivroRepository.getInstance().atualizar(livroAlterado);

        String livroValoresAlterados = livroAlterado.toString();

        Assertions.assertFalse(livroValoresAlterados.equals(livroValoresIniciais));
    }


    @Test
    public void BuscarHistoricoDeEmprestimoParaUmLivro() {
        Usuario usuario = new Usuario();
        usuario.setNome("Tito");
        UsuarioRepository.getInstance().salvar(usuario);

        System.out.println("Expo: "+ usuario.getId());

        Livro livro1 = LivroBuilder.umLivro().comNome("Livro1").constroi();
        livro1.setAutor("Autor1");

        Livro livro2 = LivroBuilder.umLivro().comNome("Livro2").constroi();
        livro2.setAutor("Autor2");

        Livro livro3 = LivroBuilder.umLivro().comNome("Livro2").constroi();
        livro3.setAutor("Autor2");

        LivroRepository.getInstance().salvar(livro1);
        LivroRepository.getInstance().salvar(livro2);
        LivroRepository.getInstance().salvar(livro3);

        List<Livro> livros = new ArrayList<>();

        livros.add(livro1);
        livros.add(livro2);
        livros.add(livro3);

        System.out.println("Tamanho: " +livros.size());

        EmprestimoService emprestimoService =  new EmprestimoService();
        System.out.println(usuario.toString());
        livros.forEach( livro -> {
            System.out.println(livro.toString());
        });

        Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livros);
        PagamentoRepository.getInstance().salvar(emprestimo.getPagamento());

        emprestimo = EmprestimoRepository.getInstance().salvar(emprestimo);
        List<Emprestimo> emprestimos = LivroRepository.getInstance().historicoDeEmprestimosDo(livro2);
        Assertions.assertEquals(1,emprestimos.size());
        Assertions.assertTrue(emprestimos.contains(emprestimo));
    }

}
