package dcomp.es2.biblioteca.modelo;

import dcomp.es2.biblioteca.repository.EmprestimoRepository;
import dcomp.es2.biblioteca.repository.PagamentoRepository;
import dcomp.es2.biblioteca.repository.PagamentoRepository;
import dcomp.es2.biblioteca.repository.UsuarioRepository;
import dcomp.es2.biblioteca.servico.EmprestimoService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class TesteDeIntegracaoPagamentoRepositoryTest {

    @Test
    public  void RealizarCadastroDoPagamentoNoBancodeDadostest() {
        Pagamento pagamento = new Pagamento(0d);
        PagamentoRepository.getInstance().salvar(pagamento);

        Pagamento PagamentoSalvo = PagamentoRepository.getInstance().getById(pagamento.getId());
        Assertions.assertEquals(pagamento,PagamentoSalvo);
    }

    @Test
    public void RealizarAtualizacaoCadastroDoPagamentoNoBancodeDados() {

        Pagamento pagamento = new Pagamento(0d);
        PagamentoRepository.getInstance().salvar(pagamento);
        String PagamentoValoresIniciais = pagamento.toString();
        System.out.println(PagamentoValoresIniciais);
        Pagamento pagamentoAlterado = PagamentoRepository.getInstance().getById(pagamento.getId());
        pagamentoAlterado.valorPago = 4.5;
        System.out.println(pagamentoAlterado);
        PagamentoRepository.getInstance().atualizar(pagamentoAlterado);
        String PagamentoValoresAlterados = pagamentoAlterado.toString();

        Assertions.assertFalse(PagamentoValoresAlterados.equals(PagamentoValoresIniciais));
    }


}
