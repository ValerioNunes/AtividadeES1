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
            List<Livro> livros =  new ArrayList<>();
            livros.add(livro);
            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livros);
            emprestimo.getLivros().forEach( l -> Assertions.assertEquals(true, l.isReservado()));

        }

        @Test
        void   TentativaDeEmprestimoEmLivroQueJaPossuiUmaReserva(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            Livro livro = LivroBuilder.umLivro().comNome("Iracema").estaReservedo().constroi();

            EmprestimoService emprestimoService =  new EmprestimoService();
            List<Livro> livros =  new ArrayList<>();
            livros.add(livro);

            IllegalArgumentException exception =
                    Assertions.assertThrows(IllegalArgumentException.class,
                    () -> emprestimoService.emprestarLivro(usuario, livros),
                    "Livro reservado");
           // Assertions.assertTrue(exception.getMessage().contains("Livro reservado"));

        }

        @Test
        void   GarantirQueDataPrevistaEstejaCorretaAposLocacaoDeUmLivro(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            Livro livro = LivroBuilder.umLivro().comNome("Iracema").constroi();
            EmprestimoService emprestimoService =  new EmprestimoService();
            List<Livro> livros =  new ArrayList<>();
            livros.add(livro);
            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livros);
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
            List<Livro> livros =  new ArrayList<>();
            livros.add(livro1);

            Emprestimo emprestimo1 = emprestimoService.emprestarLivro(usuario,livros);


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

            List<Livro> livros1 =  new ArrayList<>();
            livros1.add(livro1);
            Emprestimo emprestimo1 = emprestimoService.emprestarLivro(usuario,livros1);

            List<Livro> livros2 =  new ArrayList<>();
            livros2.add(livro2);
            Emprestimo emprestimo2 = emprestimoService.emprestarLivro(usuario,livros2);

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

            List<Livro> livros1 =  new ArrayList<>();
            livros1.add(livro1);
            emprestimoService.emprestarLivro(usuario,livros1);

            List<Livro> livros2 =  new ArrayList<>();
            livros2.add(livro2);
            emprestimoService.emprestarLivro(usuario,livros2);


            List<Livro> livros3 =  new ArrayList<>();
            livros3.add(livro3);

            Assertions.assertThrows( IllegalArgumentException.class,
                    () -> emprestimoService.emprestarLivro(usuario,livros3),
                    "Usuario com numero maximo de emprestimos");
        }

        @Test
        void TestarUmaDevolucaoAntesDaDataPrevista(){
            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro = LivroBuilder.umLivro().comNome("Livro1").constroi();

            List<Livro> livros1 =  new ArrayList<>();
            livros1.add(livro);

            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livros1);



            emprestimo.getLivros().forEach( lv ->
                    Assertions.assertEquals(true,lv.isEmprestado())
            );
            emprestimo = emprestimoService.finalizarEmprestimo(emprestimo);

            emprestimo.getLivros().forEach( lv ->
                    Assertions.assertEquals(false,lv.isEmprestado())
            );
            Assertions.assertEquals(EmprestimoService.getValorAluguelFixo(),emprestimo.getPagamento().valorPago);
        }

        @Test
        void TestarUmaDevolucaoNaDataPrevista(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro = LivroBuilder.umLivro().comNome("Livro1").constroi();


            List<Livro> livros1 =  new ArrayList<>();
            livros1.add(livro);
            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livros1);

            emprestimo.getLivros().forEach( lv ->
                    Assertions.assertEquals(true,lv.isEmprestado())
            );

            emprestimo.setDataDevolucao(LocalDateTime.now().plusDays(7));

            emprestimo = emprestimoService.finalizarEmprestimo(emprestimo);
            emprestimo.getLivros().forEach( lv ->
                    Assertions.assertEquals(false,lv.isEmprestado())
            );
            Assertions.assertEquals(EmprestimoService.getValorAluguelFixo(),emprestimo.getPagamento().valorPago);
        }

        @Test
        void TestarUmaDevolucaoUmDiaDepoisPrevista(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro = LivroBuilder.umLivro().comNome("Livro1").constroi();


            List<Livro> livros1 =  new ArrayList<>();
            livros1.add(livro);
            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livros1);
            emprestimo.getLivros().forEach( lv ->
                    Assertions.assertEquals(true,lv.isEmprestado())
            );

            emprestimo.setDataDevolucao(LocalDateTime.now().plusDays(8));

            emprestimo = emprestimoService.finalizarEmprestimo(emprestimo);

            emprestimo.getLivros().forEach( lv ->
                    Assertions.assertEquals(false,lv.isEmprestado())
            );

            double valor = EmprestimoService.getValorAluguelFixo() + EmprestimoService.getTaxaDiaria();

            Assertions.assertEquals(valor,emprestimo.getPagamento().valorPago);
        }

        @Test
        void TestarUmaDevolucaoTrintaDiasDepoisPrevista(){

            Usuario usuario = new Usuario();
            usuario.setNome("Valerio");
            EmprestimoService emprestimoService =  new EmprestimoService();

            Livro livro = LivroBuilder.umLivro().comNome("Livro1").constroi();


            List<Livro> livros1 =  new ArrayList<>();
            livros1.add(livro);
            Emprestimo emprestimo = emprestimoService.emprestarLivro(usuario,livros1);


            emprestimo.setDataDevolucao(LocalDateTime.now().plusDays(30));

            emprestimo = emprestimoService.finalizarEmprestimo(emprestimo);

            emprestimo.getLivros().forEach( lv ->
                    Assertions.assertEquals(false,lv.isEmprestado())
            );

            double valor = EmprestimoService.getValorAluguelFixo() + EmprestimoService.getValorAluguelFixoPoncentagemDe(60);

            Assertions.assertEquals(valor,emprestimo.getPagamento().valorPago);
        }

}

