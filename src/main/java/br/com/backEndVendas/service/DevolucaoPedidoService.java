package br.com.backEndVendas.service;

import br.com.backEndVendas.model.DevolucaoPedido;
import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.dao.DevolucaoDao;
import br.com.backEndVendas.service.dao.PedidoDao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@RestController
public class DevolucaoPedidoService {

    @Autowired
    private DevolucaoDao devolucaoDAO;

    @Autowired
    private PedidoDao pedidoDao;

    @Autowired
    PedidoService pedidoService;

    public void registrarDevolucao(String devolucaoJson) throws Exception { // O Pedido é devolvido pelo um Json.

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(devolucaoJson);

        // Instânciando as váriaveis para tratar as exceções.
    
        int codigoPedido = jsonNode.get("codigoPedido").asInt();
        String date = jsonNode.get("dataDevolucao").asText();
        LocalDate dataDevolucao =  LocalDate.parse(date);

        String motivoDevolucao = jsonNode.get("motivoDevolucao").asText();
        boolean prazoExpirado = verificarPrazoDevolucao(String.valueOf(dataDevolucao));

        Pedido pedidoDevolvido = pedidoService.buscarPedidoPeloId(codigoPedido);
        if (pedidoDevolvido == null) {
            throw new Exception("Pedido não encontrado.");
        }

        if (!prazoExpirado) {
            throw new Exception("O prazo para devolução expirou.");
        }

        //int quantidadeDisponivel = pedidoDevolvido.getQuantidadeEstoque(); // Verificar a quantidade no estoque
        //if (quantidadeDevolvida > quantidadeDisponivel) {
        //    throw new Exception("Quantidade de devolução maior do que a disponível em estoque.");
        // }

        //produtoDevolvido.atualizarEstoque(quantidadeDevolvida * -1);  // Atualizar o estoque
        //produtoDAO.atualizarProduto(produtoDevolvido);

        DevolucaoPedido devolucao = new DevolucaoPedido();
        devolucao.setCodigoPedido(codigoPedido);
        devolucao.setDataDevolucao(date);
        devolucao.setMotivoDevolucao(motivoDevolucao);
        devolucaoDAO.save(devolucao);

    }

    public static boolean verificarPrazoDevolucao(String dataDevolucaoString) {
        // Fazer o parsing da string para um objeto LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate dataDevolucao = LocalDate.parse(dataDevolucaoString, formatter);

        // Calcular a diferença em dias entre a data atual e a data de devolução
        LocalDate dataAtual = LocalDate.now();
        long diferencaDias = (ChronoUnit.DAYS.between(dataDevolucao, dataAtual) * -1);

        // Verificar se a diferença é menor ou igual a 7 dias
        if (diferencaDias <= 7) {
            System.out.println("O prazo para devolução expirou.");
            return true;

        } else {
            System.out.println("O prazo para devolução não expirou.");
            return false;
        }
    }
}
