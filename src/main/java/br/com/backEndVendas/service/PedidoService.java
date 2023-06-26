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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    @Qualifier("mock")
    @Autowired
    RestTemplate rest;

    @Autowired
    FretService fretService;

    @Autowired
    DevolucaoPedidoService devolucaoPedidoService;

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



    public boolean validarCarrinho(int idCarrinho, int idCliente) {
        String url = "https://localhost:8080/compras/validar/pagamento/" + idCliente + "/" + idCarrinho + "/json/";
        ResponseEntity<CompraCarrinhoDto> resp = rest.getForEntity(url, CompraCarrinhoDto.class);
        CompraCarrinhoDto c = resp.getBody();
        boolean result = c.isPgAprovado();
        return result;
    }


    public String processarPedido(Pedido pedidoJson) throws Exception {
        if (!validarCarrinho(pedidoJson.getIdCarrinho(), pedidoJson.getIdCliente())) {
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

            int validarEstoque = produtoService.verificarEstoqueDisponível(itemJson.getIdProduto(), itemJson.getQuantidade());

            if (validarEstoque == 1) {
                return "Id de produto inexistente!";
            } else if (validarEstoque == 3) {
                throw new Exception("Quantidade de produto indisponível!");
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

//        String url = "https://localhost:8080/crm/cliente/verificarCadastro" + pedidoJson.getIdCliente();
//        ResponseEntity<ClienteCadastroDto> resp = rest.getForEntity(url, ClienteCadastroDto.class);
//        ClienteCadastroDto d = resp.getBody();
//        assert d != null;
//        if (d.isCadastro()){
//            notaVenda.setValorTotal(pedidoJson.getPrecoTotal() * (5/100));
//        }else{
//            notaVenda.setValorTotal(pedidoJson.getPrecoTotal());
//        }


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

    public PedidoStatusDto cancelarPedidoPeloId(int idPedido, int idProduto, int qtdeDevolvida) throws Exception {
        Optional<Pedido> op = pdao.findById(idPedido);

        if (op.isPresent()) {
            LocalDate d = java.time.LocalDate.now();
            registrarDevolucao(op.get(), idProduto, qtdeDevolvida, d);
            return PedidoStatusDto.builder()
                    .status("O pedido foi cancelado com sucesso!")
                    .build();
        } else {
            return PedidoStatusDto.builder()
                    .status("Falha ao cancelar o pedido")
                    .build();
        }
    }

    public boolean atualizarEstoque(int cdProduto, int qtdeDevolvida) {

        String url = "https://localhost:8080/compras/produto/devolver/" + cdProduto + "/" + qtdeDevolvida;
        ResponseEntity<CompraProdutoRetirarDto> resp = rest.getForEntity(url, CompraProdutoRetirarDto.class);
        CompraProdutoRetirarDto c = resp.getBody();

        return c.isStatus();
    }

    public String registrarDevolucao(Pedido pedido, int idProduto, int qtdeProduto, LocalDate dataDev) throws Exception {
        if (pedido != null) {
            DevolucaoPedido pedidoDevolvido = new DevolucaoPedido();
            pedidoDevolvido.setCodigoPedido(pedido.getIdPedido());
            pedidoDevolvido.setCodigoProduto(idProduto);
            pedidoDevolvido.setQtdeDevolvida(qtdeProduto);
            pedidoDevolvido.setDataDevolucao(dataDev);
            verificarPrazoDevolucao(dataDev, qtdeProduto);

            boolean estoqueSuficiente = true;
            boolean produtoEncontrado = false;

            if (!pedido.getItensPedido().isEmpty()) {
                for (ItemPedido item : pedido.getItensPedido()) {
                    if (item.getIdProduto() != 0 && item.getIdProduto() == idProduto) {
                        produtoEncontrado = true;
                        if (item.getQuantidade() < qtdeProduto) {
                            estoqueSuficiente = false;
                            break;
                        } else {
                            item.setQuantidade(item.getQuantidade() - qtdeProduto);
                            pedido.setStatusPedido("fechado");
                            devolucaoPedidoService.save(pedidoDevolvido);
                            return "O produto foi removido com sucesso!";
                        }
                    }
                }
            }

            if (!produtoEncontrado) {
                throw new Exception("Produto não encontrado no pedido.");
            } else if (!estoqueSuficiente) {
                throw new Exception("Quantidade devolvida maior do que a registrada no pedido.");
            }

            if (!atualizarEstoque(pedidoDevolvido.getCodigoProduto(), pedidoDevolvido.getQtdeDevolvida())) {
                throw new Exception("Não foi possível devolver o produto.");
            }
            return "Pedido devolvido";
        } else {
            throw new Exception("Pedido não encontrado.");
        }
    }

    public static boolean verificarPrazoDevolucao(LocalDate dataDevolucao, int diasExpiracao) throws Exception {
        LocalDate dataAtual = LocalDate.now();
        long diferencaDias = (ChronoUnit.DAYS.between(dataDevolucao, dataAtual) * -1);

        if (diferencaDias <= diasExpiracao) {
            return true;
        } else {
            throw new Exception("O prazo para devolução expirou.");
        }
    }

    public void vonageApi(int idPedido, int idCliente) {

        String url = "https://localhost:8080/crm/buscar/cliente/" + idCliente;
        ResponseEntity<ClienteStatusDto> resp = rest.getForEntity(url, ClienteStatusDto.class);
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
}
