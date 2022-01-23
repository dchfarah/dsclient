package com.devsuperior.dsclient.resources;

import com.devsuperior.dsclient.dto.ClientDTO;
import com.devsuperior.dsclient.services.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/clients")
public class ClientResource {
    private final ClientService service;

    public ClientResource(ClientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> findAllPaged(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "linesPerPage", defaultValue = "6") Integer linesPerPage,
            @RequestParam(name = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction
    ) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<ClientDTO> list = service.findAllPaged(pageRequest);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
        ClientDTO entity = service.findById(id);
        return ResponseEntity.ok().body(entity);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> insert(@RequestBody ClientDTO dto) {
        ClientDTO entity = service.insert(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(entity);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> update(@PathVariable Long id, @RequestBody ClientDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
