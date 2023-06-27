package br.com.backEndVendas.resources;

import br.com.backEndVendas.service.MetaService;
import br.com.backEndVendas.service.dto.MetaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meta")
public class MetaResource {

    private final MetaService metaService;

    @Autowired
    public MetaResource(MetaService metaService) {
        this.metaService = metaService;
    }

    @GetMapping
    public ResponseEntity<MetaDto> getMeta() {
        double vendas = metaService.getVendas();
        MetaDto metaDto = new MetaDto();
        metaDto.setVendas(vendas);
        return new ResponseEntity<>(metaDto, HttpStatus.OK);
    }
}

