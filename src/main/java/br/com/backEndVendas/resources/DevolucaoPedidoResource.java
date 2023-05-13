package br.com.backEndVendas.resources;

import br.com.backEndVendas.service.DevolucaoPedidoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pedido") // base da URL
@RestController // indica que é um componente web que pode receber requisições HTTP
public class DevolucaoPedidoResource {

    @Autowired
    private DevolucaoPedidoService devolucaoPedidoService;

    @PostMapping("/devolucao")
    public ResponseEntity<String> registrarDevolucaoPedido(@RequestBody String devolucaoJson) {
        try {
            // Parse da data de devolução do JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(devolucaoJson);
            String dataDevolucaoString = jsonNode.get("dataDevolucao").asText();

            // Verificar se a data de devolução é anterior a 7 dias da data atual
            boolean dentroDoPrazo = DevolucaoPedidoService.verificarPrazoDevolucao(dataDevolucaoString);

            if (!dentroDoPrazo) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O prazo para devolução expirou.");
            } else {
                devolucaoPedidoService.registrarDevolucao(devolucaoJson);
                return ResponseEntity.status(HttpStatus.CREATED).body("Devolução registrada com sucesso.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
