package br.com.backEndVendas.service;
import br.com.backEndVendas.model.Meta;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;

@Service
public class MetaService {

    private final RestTemplate restTemplate;
    private final String url;

    public MetaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.url = "https://gateway-sgeu.up.railway.app/pessoal/vendedor/mes/{mes}/{ano}";
    }

    public double getVendas() {
        LocalDate currentDate = LocalDate.now();
        int mes = currentDate.getMonthValue();
        int ano = currentDate.getYear();

        String endpoint = url.replace("{mes}", String.valueOf(mes)).replace("{ano}", String.valueOf(ano));

        Meta meta = restTemplate.getForObject(endpoint, Meta.class);
        return meta != null ? meta.getVendas() : 0.0;
    }
}