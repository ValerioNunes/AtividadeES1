package dcomp.es2.biblioteca.servico;

import dcomp.es2.biblioteca.builder.LivroBuilder;
import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Livro;
import dcomp.es2.biblioteca.modelo.Usuario;
import dcomp.es2.biblioteca.repository.EmprestimoRepository;
import dcomp.es2.biblioteca.repository.LivroRepository;
import dcomp.es2.biblioteca.repository.PagamentoRepository;
import dcomp.es2.biblioteca.repository.UsuarioRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;

import java.io.EOFException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TesteServicesEmprestimoTest {


    Emprestimo emprestimo1, emprestimo2, emprestimo3;
    //@Mock
    Usuario usuario1, usuario2, usuario3;

    Livro livro1, livro2, livro3;

    @Mock
    EmprestimoService emprestimoService;
    @Mock
    EnviadorDeEmail emailService;

    @Mock
    EmprestimoRepository emprestimoRepository;

    @InjectMocks
    EstatisticasDeEmprestimo estatisticasDeEmprestimo;//  =  new EstatisticasDeEmprestimo();;


    @Before
    public void inicializacaoTest() {
        estatisticasDeEmprestimo = new EstatisticasDeEmprestimo();
        emprestimoRepository = new EmprestimoRepository();
        estatisticasDeEmprestimo.setEmprestimoRepository(emprestimoRepository);
        emprestimoService = new EmprestimoService();
        emailService = new EnviadorDeEmail();
        emprestimoService.setEmailService(emailService);

        usuario1 = new Usuario();
        usuario1.setNome("Tito");
        usuario1.setEmail("tito@ifma.com");
        UsuarioRepository.getInstance().salvar(usuario1);

        usuario2 = new Usuario();
        usuario2.setNome("Joao");
        usuario2.setEmail(null);
        UsuarioRepository.getInstance().salvar(usuario2);

        usuario3 = new Usuario();
        usuario3.setNome("Pedro");
        usuario3.setEmail("pedro@fiema.com");
        UsuarioRepository.getInstance().salvar(usuario3);


        livro1 = LivroBuilder.umLivro().comNome("Livro1").constroi();
        livro1.setAutor("Autor1");

        livro2 = LivroBuilder.umLivro().comNome("Livro2").constroi();
        livro2.setAutor("Autor2");

        livro3 = LivroBuilder.umLivro().comNome("Livro2").constroi();
        livro3.setAutor("Autor2");

        LivroRepository.getInstance().salvar(livro1);
        LivroRepository.getInstance().salvar(livro2);
        LivroRepository.getInstance().salvar(livro3);

        EmprestimoService emprestimoService = new EmprestimoService();


        List<Livro> livros1 = new ArrayList<Livro>();
        livros1.add(livro1);
        emprestimo1 = emprestimoService.emprestarLivro(usuario1, livros1);


        List<Livro> livros2 = new ArrayList<Livro>();
        livros2.add(livro2);
        emprestimo2 = emprestimoService.emprestarLivro(usuario2, livros2);


        List<Livro> livros3 = new ArrayList<Livro>();
        livros3.add(livro3);
        emprestimo3 = emprestimoService.emprestarLivro(usuario3, livros3);


        PagamentoRepository.getInstance().salvar(emprestimo1.getPagamento());
        PagamentoRepository.getInstance().salvar(emprestimo2.getPagamento());
        PagamentoRepository.getInstance().salvar(emprestimo3.getPagamento());


    }

    @After
    public void FinalizarTest() {
//        EmprestimoRepository.getInstance().remove(emprestimo1);
//        EmprestimoRepository.getInstance().remove(emprestimo2);
//        EmprestimoRepository.getInstance().remove(emprestimo3);

    }

    @Test
    public void TesteEnvioDeEmailParaOsUsuarioEmprestimoForaData() {
        emprestimo2.setDataPrevista(LocalDateTime.now().plusDays(-1));
        emprestimo3.setDataPrevista(LocalDateTime.now().plusDays(-1));

        EmprestimoRepository.getInstance().salvar(emprestimo1);
        EmprestimoRepository.getInstance().salvar(emprestimo2);
        EmprestimoRepository.getInstance().salvar(emprestimo3);

        List<Usuario> usuarioEmAtrasoEsperado = Arrays.asList(usuario2, usuario3);
        List<Usuario> usuarioEmAtraso = emprestimoRepository.getUsuarioEmAtraso();
        System.out.println(usuarioEmAtraso);

        usuarioEmAtrasoEsperado.forEach(usuario -> {
            assertTrue(usuarioEmAtraso.contains(usuario));
        });

        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> emprestimoService.enviarEmailParaUsuarioComAtraso(),
                        "Usuário sem email cadastrado");

        MockitoAnnotations.initMocks(this);

        List<Usuario> usuariosAtrasados = new ArrayList<Usuario>();
        usuariosAtrasados.add(usuario2);
        usuariosAtrasados.add(usuario3);
        when(emprestimoRepository.getUsuarioEmAtraso()).thenReturn(usuariosAtrasados);
        doThrow(new IllegalArgumentException("Usuário sem email cadastrado")).when(emprestimoService).enviarEmailParaUsuarioComAtraso();

        emprestimoService = new EmprestimoService();
        emailService = mock(EnviadorDeEmail.class);
        emprestimoService.setEmailService(emailService);

        try {
            emprestimoService.enviarEmailParaUsuarioComAtraso();
        } catch (Exception e) {
        }

        usuariosAtrasados.forEach(usuario -> {
            System.out.println(usuario.getNome());
            verify(emailService, Mockito.times(1)).enviarEmailPara(usuario, "Atraso na devolução de Livros blabla");
        });


    }

    @Test
    public void TesteRecuperacaoDeListaEmprestimosFeitoNumDadoPeriodo() {

        emprestimo1.setDataEmprestimo(LocalDateTime.now().plusDays(-7));
        emprestimo2.setDataEmprestimo(LocalDateTime.now().plusDays(-3));
        emprestimo3.setDataEmprestimo(LocalDateTime.now().plusDays(-1));

        EmprestimoRepository.getInstance().salvar(emprestimo1);
        EmprestimoRepository.getInstance().salvar(emprestimo2);
        EmprestimoRepository.getInstance().salvar(emprestimo3);

        LocalDateTime inicio = LocalDateTime.now().plusDays(-6);
        LocalDateTime fim = LocalDateTime.now().plusDays(-2);
        List<Livro> livros = estatisticasDeEmprestimo.getLivrosEmprestadoNoPeriodo(inicio, fim);
        assertEquals(1, livros.size());
        assertTrue(livros.contains(livro2));

        List<Livro> livroDoPeriodo = new ArrayList<Livro>();
        livroDoPeriodo.add(livro2);

        MockitoAnnotations.initMocks(this);
        when(emprestimoRepository.getLivroNoPeriodo(inicio, fim)).thenReturn(livroDoPeriodo);
    }
}
