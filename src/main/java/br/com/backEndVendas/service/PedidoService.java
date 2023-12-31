package br.com.backEndVendas.service;

import br.com.backEndVendas.model.*;
import br.com.backEndVendas.service.dao.NotaVendaDao;
import br.com.backEndVendas.service.dao.PedidoDao;
import br.com.backEndVendas.service.dto.*;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Service
public class PedidoService {

    @Autowired
    PedidoDao pdao;
    @Autowired
    NotaVendaDao notaVendaDao;

    @Autowired
    ProdutoService produtoService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    FretService fretService;

    @Autowired
    DevolucaoPedidoService devolucaoPedidoService;
    @Autowired
    ProcessarPagamentoService processarPagamentoService;

    public Object buscarPedidoPeloId(int id) {
        Optional<Pedido> op = pdao.findById(id);
        if (op.isPresent()) {
            PedidoDto pedidoDto = PedidoDto.builder()
                    .pedido(op.get())
                    .build();
            return pedidoDto;
        } else {
            return PedidoStatusDto.builder()
                    .status("Pedido inexistente!")
                    .build();
        }
    }

    public PedidoStatusDto realizarPedido(Pedido pedidoJson) throws Exception {

        String pedidoResponse = (processarPedido(pedidoJson));
        return PedidoStatusDto.builder()
                .status(pedidoResponse)
                .build();
    }

    public String processarPedido(Pedido pedidoJson) throws Exception {

        if (!processarPagamentoService.realizarPagamento(pedidoJson.getIdCliente(), pedidoJson.getIdCarrinho(), pedidoJson.getPrecoTotal(), "CREDITO" )){
            return "Houve um erro com o pagamento do pedido!";
        }
        Pedido pedido = new Pedido();
        pedido.setPrecoTotal(pedidoJson.getPrecoTotal());
        pedido.setIdCliente(pedidoJson.getIdCliente());
        pedido.setIdVendedor(pedidoJson.getIdVendedor());
        pedido.setDataPedido(pedidoJson.getDataPedido());
        pedido.setIdCarrinho(pedidoJson.getIdCarrinho());
        pedido.setStatusPedido(pedidoJson.getStatusPedido());

        List<ItemPedido> itensPedido = new ArrayList<>();
        for (ItemPedido itemJson : pedidoJson.getItensPedido()) {

            CompraBuscarProdutoDto p = produtoService.produtoPeloId(itemJson.getIdProduto());
            if (p == null) {
                return "Id de produto inexistente!";
            }

            if (!produtoService.verificarEstoque(itemJson.getIdProduto(), itemJson.getQuantidade())){
                throw new Exception("Quantidade indisponível em estoque!");
            }

            if (!produtoService.atualizarEstoque(itemJson.getIdProduto(), itemJson.getQuantidade())) {
                throw new Exception("Não foi possível atualizar o estoque!");
            }

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setIdProduto(itemJson.getIdProduto());
            itemPedido.setQuantidade(itemJson.getQuantidade());
            itensPedido.add(itemPedido);
        }

        pedido.setItensPedido(itensPedido);
        pdao.save(pedido);

        List<Integer> idsProduto = new ArrayList<>();
        for (ItemPedido itemPedido : pedido.getItensPedido()) {
            idsProduto.add(itemPedido.getIdProduto());
        }

        NotaVenda notaVenda = new NotaVenda();
        notaVenda.setPedido(pedido);
        notaVenda.setIdCliente(pedidoJson.getIdCliente());
        notaVenda.setIdVendedor(pedidoJson.getIdVendedor());
        notaVenda.setDataEmissao(LocalDate.now());

//        String url = "https://localhost:8080/crm/cliente/verificarCadastro/" + pedidoJson.getIdCliente();
//        ResponseEntity<ClienteCadastroDto> resp = rest.getForEntity(url, ClienteCadastroDto.class);
//        ClienteCadastroDto d = resp.getBody();
//        assert d != null;
//        if (d.isCadastro()) {
//            notaVenda.setValorTotal(pedidoJson.getPrecoTotal() * (5 / 100));
//        } else {
//            notaVenda.setValorTotal(pedidoJson.getPrecoTotal());
//        }

        atualizarPontuacao(pedidoJson.getIdCliente(), pedido.getIdPedido());

        notaVendaDao.save(notaVenda);

//        vonageApi(pedidoJson.getIdPedido(),pedidoJson.getIdCliente());

        return "Pedido Realizado com sucesso!";
    }

    public PedidoFretDto calcularFretPedido(FretPedido fretPedido) {
        String numeroString = fretPedido.getCep();

        if (numeroString.length() != 8) {
            throw new EntityNotFoundException("Formato de cep inválido!");
        }

        String fret = fretService.calcularFret(fretPedido.getCep(), fretPedido.getQtdeVolume(), fretPedido.getIdCliente());
        return PedidoFretDto.builder()
                .valorFret(fret)
                .build();
    }

    public PedidoValorVendedorDto valorMensalVendedor(int idVendedor, int ano, int mes) {

        double somaValor = 0;

        List<Pedido> listaPedidos = pdao.findByIdVendedor(idVendedor);
        for (Pedido p : listaPedidos) {
            if (p.getDataPedido().getYear() == ano) {
                if (p.getDataPedido().getMonthValue() == mes) {
                    somaValor += p.getPrecoTotal();
                }
            }
        }
        return PedidoValorVendedorDto.builder()
                .valorVendas(somaValor)
                .build();
    }

    public void registrarDevolucao(Pedido pedido, int idProduto, int qtdeProduto, LocalDate dataDev) throws Exception {
        if (pedido != null) {
            DevolucaoPedido pedidoDevolvido = new DevolucaoPedido(pedido.getIdPedido(), idProduto, qtdeProduto, dataDev);
            produtoService.verificarPrazoDevolucao(dataDev, qtdeProduto);


            // verificar se o produto existe
            if(!produtoService.validarProdutoExistente(idProduto)){
                throw new Exception("Produto informado não existe!");
            } else{
                // verificar se a quantidade do produto é igual a quantidade do pedido

            }

            if (!produtoService.verificarEstoque(idProduto, qtdeProduto)) {
                throw new Exception("Quantidade indisponível em estoque!");
            }

            if (!produtoService.atualizarEstoque(idProduto, qtdeProduto)) {
                throw new Exception("Não foi possível atualizar o estoque!");
            }

        } else {
            throw new Exception("Pedido não encontrado.");
        }
    }

    public PedidoStatusDto atualizarStatus(int idPedido, int idProduto, int qtdeDevolvida) throws Exception {

        Optional<Pedido> op = pdao.findById(idPedido);

        if (op.isPresent()) {
            LocalDate d = java.time.LocalDate.now();
            registrarDevolucao(op.get(), idProduto, qtdeDevolvida, d);
            return PedidoStatusDto.builder()
                    .status("O pedido foi devolvido com sucesso!")
                    .build();
        } else {
            return PedidoStatusDto.builder()
                    .status("Falha ao devolver o pedido")
                    .build();

        }
    }

    public void vonageApi(int idPedido, int idCliente) {

        String url = "https://backend-crm.up.railway.app/cliente/telefone/" + idCliente;
        ResponseEntity<ClienteStatusDto> resp = restTemplate.getForEntity(url, ClienteStatusDto.class);
        ClienteStatusDto c = resp.getBody();

        VonageClient client = VonageClient.builder().apiKey("e25b3d26").apiSecret("QzJjoOs4Jpufk1Kq").build();

        TextMessage message = new TextMessage("Compras", "+55" + c.getTelefone(),
                "Ola " + c.getNome() + ". Seu Pedido numero: " + idPedido + " foi realizado com sucesso");

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }

    public PedidoValorVendedorDto valorAnualVendedor(int idVendedor, int ano) {
        double somaValor = 0;

        List<Pedido> listaPedidos = pdao.findByIdVendedor(idVendedor);
        for (Pedido p : listaPedidos) {
            if (p.getDataPedido().getYear() == ano) {
                somaValor += p.getPrecoTotal();
            }
        }
        return PedidoValorVendedorDto.builder()
                .valorVendas(somaValor)
                .build();
    }

    public List<Pedido> comprasValorVendedor(int idVendedor)throws Exception {

        List<Pedido> listaPedidos = pdao.findByIdVendedor(idVendedor);

        if(!listaPedidos.isEmpty()){
            return listaPedidos;

        }
        throw new Exception("Id do vendedor não existe.");
    }

    public PedidoValorDto getValorPedido(int idPedido)throws Exception {
        Optional<Pedido> op = pdao.findById(idPedido);
        if (op.isPresent()) {
            PedidoValorDto pedidoDto = PedidoValorDto.builder()
                    .valorTotal(op.get().getPrecoTotal())
                    .build();
            return pedidoDto;
        } else {
           throw new Exception("Id do pedido não existe.");
        }

    }

    public void atualizarPontuacao(int idCliente, int idPedido) {
        String url = "https://backend-crm.up.railway.app/cliente/pontuacao";

        try {
            var atualizarPontuacaoDto = AtualizarPontuacaoDto.builder()
                    .id(idCliente)
                    .idPedido(idPedido)
                    .build();

            HttpEntity<Object> requestEntity = new HttpEntity<>(atualizarPontuacaoDto);
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Object.class, atualizarPontuacaoDto);

            System.out.println(response.getBody());

        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao atualizar a pontuação: " + e.getMessage());
        }
    }
}
