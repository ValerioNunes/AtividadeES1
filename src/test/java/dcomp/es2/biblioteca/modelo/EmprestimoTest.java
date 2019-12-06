package dcomp.es2.biblioteca.modelo;
import dcomp.es2.biblioteca.builder.LivroBuilder;
import dcomp.es2.biblioteca.servico.EmprestimoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.objects.Global.print;

public class EmprestimoTest {

        @Test
        void RealizarEmprestimoEmLivroqueNaoEstejaReservado() {

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            Livro livro = LivroBuilder.umLivro().comNome("Iracema").constroi();
            EmprestimoService emprestimoService =  new EmprestimoService();

            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livro);

            Assertions.assertEquals(true, emprestimo.getLivro().isReservado());

        }

        @Test
        void   TentativaDeEmprestimoEmLivroQueJaPossuiUmaReserva(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            Livro livro = LivroBuilder.umLivro().comNome("Iracema").estaReservedo().constroi();

            EmprestimoService emprestimoService =  new EmprestimoService();

            IllegalArgumentException exception =
                    Assertions.assertThrows(IllegalArgumentException.class,
                    () -> emprestimoService.emprestarLivro(usuario, livro),
                    "Livro reservado");
           // Assertions.assertTrue(exception.getMessage().contains("Livro reservado"));

        }

        @Test
        void   GarantirQueDataPrevistaEstejaCorretaAposLocacaoDeUmLivro(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            Livro livro = LivroBuilder.umLivro().comNome("Iracema").constroi();
            EmprestimoService emprestimoService =  new EmprestimoService();

            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livro);
            LocalDateTime dataPrevista = emprestimo.getDataPrevista();
            LocalDateTime dataEmprestimo =  dataPrevista.plusDays(-EmprestimoService.getNumeroDiasMaximoSemAtraso());

            Assertions.assertEquals(LocalDateTime.now(),dataEmprestimo);
        }

        @Test
        void   TestaUmUsuarioSemEmprestimo(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();
            List<Emprestimo> emprestimos = emprestimoService.getEmprestimosVirgentesDoUsuario(usuario);

            Assertions.assertEquals(0,emprestimos.size());
        }

        @Test
        void   TestaUmUsuarioComUmEmprestimo(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro1 = LivroBuilder.umLivro().comNome("Livro1").constroi();

            Emprestimo emprestimo1 = emprestimoService.emprestarLivro(usuario,livro1);


            List<Emprestimo> emprestimos = emprestimoService.getEmprestimosVirgentesDoUsuario(usuario);
            List<Emprestimo> emprestimosEsperados =  new ArrayList<Emprestimo>();
            emprestimosEsperados.add(emprestimo1);
            Assertions.assertEquals(emprestimosEsperados.size(),emprestimos.size());
            emprestimosEsperados.forEach(x -> Assertions.assertEquals(true, emprestimos.contains(x)));
        }

        @Test
        void   TestaUmUsuarioComDoisEmprestimo(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro1 = LivroBuilder.umLivro().comNome("Livro1").constroi();
            Livro livro2 = LivroBuilder.umLivro().comNome("Livro2").constroi();

            Emprestimo emprestimo1 = emprestimoService.emprestarLivro(usuario,livro1);
            Emprestimo emprestimo2 = emprestimoService.emprestarLivro(usuario,livro2);

            List<Emprestimo> emprestimos = emprestimoService.getEmprestimosVirgentesDoUsuario(usuario);

            List<Emprestimo> emprestimosEsperados =  new ArrayList<Emprestimo>();
            emprestimosEsperados.add(emprestimo1);
            emprestimosEsperados.add(emprestimo2);

            Assertions.assertEquals(emprestimosEsperados.size(),emprestimos.size());
            emprestimosEsperados.forEach(x -> Assertions.assertEquals(true, emprestimos.contains(x)));
        }
        
        @Test
        void   TentativaDeRealizarUm3oEmprestimoParaOMesmoUsuario(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro1 = LivroBuilder.umLivro().comNome("Livro1").constroi();
            Livro livro2 = LivroBuilder.umLivro().comNome("Livro2").constroi();
            Livro livro3 = LivroBuilder.umLivro().comNome("Livro3").constroi();

            emprestimoService.emprestarLivro(usuario,livro1);
            emprestimoService.emprestarLivro(usuario,livro2);

            Assertions.assertThrows( IllegalArgumentException.class,
                    () -> emprestimoService.emprestarLivro(usuario,livro3),
                    "Usuario com numero maximo de emprestimos");
        }

        @Test
        void TestarUmaDevolucaoAntesDaDataPrevista(){
            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro = LivroBuilder.umLivro().comNome("Livro1").constroi();

            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livro);

            Assertions.assertEquals(true,emprestimo.getLivro().isEmprestado());

            emprestimo = emprestimoService.finalizarEmprestimo(emprestimo);

            Assertions.assertEquals(false,emprestimo.getLivro().isEmprestado());
            Assertions.assertEquals(EmprestimoService.getValorAluguelFixo(),emprestimo.getPagamento().valorPago);
        }

        @Test
        void TestarUmaDevolucaoNaDataPrevista(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro = LivroBuilder.umLivro().comNome("Livro1").constroi();

            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livro);

            Assertions.assertEquals(true,emprestimo.getLivro().isEmprestado());

            emprestimo.setDataDevolucao(LocalDateTime.now().plusDays(7));

            emprestimo = emprestimoService.finalizarEmprestimo(emprestimo);

            Assertions.assertEquals(false,emprestimo.getLivro().isEmprestado());
            Assertions.assertEquals(EmprestimoService.getValorAluguelFixo(),emprestimo.getPagamento().valorPago);
        }

        @Test
        void TestarUmaDevolucaoUmDiaDepoisPrevista(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro = LivroBuilder.umLivro().comNome("Livro1").constroi();

            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livro);

            Assertions.assertEquals(true,emprestimo.getLivro().isEmprestado());

            emprestimo.setDataDevolucao(LocalDateTime.now().plusDays(8));

            emprestimo = emprestimoService.finalizarEmprestimo(emprestimo);

            Assertions.assertEquals(false,emprestimo.getLivro().isEmprestado());
            double valor = EmprestimoService.getValorAluguelFixo() + EmprestimoService.getTaxaDiaria();

            Assertions.assertEquals(valor,emprestimo.getPagamento().valorPago);
        }

        @Test
        void TestarUmaDevolucaoTrintaDiasDepoisPrevista(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro = LivroBuilder.umLivro().comNome("Livro1").constroi();

            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livro);


            emprestimo.setDataDevolucao(LocalDateTime.now().plusDays(30));

            emprestimo = emprestimoService.finalizarEmprestimo(emprestimo);


            Assertions.assertEquals(false,emprestimo.getLivro().isEmprestado());
            double valor = EmprestimoService.getValorAluguelFixo() + EmprestimoService.getValorAluguelFixoPoncentagemDe(60);

            Assertions.assertEquals(valor,emprestimo.getPagamento().valorPago);
        }

}

