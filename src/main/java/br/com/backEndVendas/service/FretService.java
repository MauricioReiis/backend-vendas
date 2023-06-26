package br.com.backEndVendas.service;

import br.com.backEndVendas.service.dto.EnderecoDto;
import br.com.backEndVendas.service.dto.FretPedidoDescontoDto;
import br.com.backEndVendas.service.dto.PedidoStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FretService {

    @Qualifier("mock")
    @Autowired
    RestTemplate rest;

    @Autowired
    RestTemplate restTemplate;

    EnderecoDto buscarCep(String cep) {
        String url = "https://viacep.com.br/ws/"+cep+"/json/";
        ResponseEntity<EnderecoDto> resp = restTemplate.getForEntity(url, EnderecoDto.class);
        EnderecoDto e = resp.getBody();

        return e;
    }

    public double aplicarCupomFrete(int idCliente) {
        String url = "https://localhost:8080/crm/validar/cupom/desconto/" + idCliente;
        ResponseEntity<FretPedidoDescontoDto> resp = rest.getForEntity(url, FretPedidoDescontoDto.class);
        FretPedidoDescontoDto c = resp.getBody();
        double result = c.getValorDesconto();
        return result;
    }

    public String calcularFret(String cep, int qtdeVolume, int idCliente) {
        EnderecoDto uf = buscarCep(cep);
        int preco = obterPrecoEstado(uf.getUf());
        double desconto = aplicarCupomFrete(idCliente);
        double fretBase = (preco * qtdeVolume);
        double precoFret = fretBase - (idCliente==0?0: (desconto>=fretBase)?fretBase: desconto);
        String message = "Estado: "+ uf.getUf()+ ", Cidade: "+uf.getLocalidade() + ", Preço fret: "+precoFret+"";

        return message;
    }


    public static int obterPrecoEstado(String sigla) {
        int numero;

        switch (sigla.toUpperCase()) {
            case "AC":
                numero = 100;
                break;
            case "AL":
                numero = 200;
                break;
            case "AP":
                numero = 300;
                break;
            case "AM":
                numero = 400;
                break;
            case "BA":
                numero = 150;
                break;
            case "CE":
                numero = 160;
                break;
            case "DF":
                numero = 170;
                break;
            case "ES":
                numero = 80;
                break;
            case "GO":
                numero = 90;
                break;
            case "MA":
                numero = 100;
                break;
            case "MT":
                numero = 110;
                break;
            case "MS":
                numero = 120;
                break;
            case "MG":
                numero = 130;
                break;
            case "PA":
                numero = 140;
                break;
            case "PB":
                numero = 150;
                break;
            case "PR":
                numero = 160;
                break;
            case "PE":
                numero = 170;
                break;
            case "PI":
                numero = 180;
                break;
            case "RJ":
                numero = 190;
                break;
            case "RN":
                numero = 200;
                break;
            case "RS":
                numero = 210;
                break;
            case "RO":
                numero = 220;
                break;
            case "RR":
                numero = 230;
                break;
            case "SC":
                numero = 240;
                break;
            case "SP":
                numero = 250;
                break;
            case "SE":
                numero = 260;
                break;
            case "TO":
                numero = 270;
                break;
            default:
                numero = 1;
                break;
        }

        return numero;
    }
}
