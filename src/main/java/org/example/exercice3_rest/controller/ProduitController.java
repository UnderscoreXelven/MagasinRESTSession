package org.example.exercice3_rest.controller;

import org.example.exercice3_rest.dto.ProduitReceiveDTO;
import org.example.exercice3_rest.dto.ProduitResponseDTO;
import org.example.exercice3_rest.service.ProduitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/produit")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitResponseDTO> getProduit(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(produitService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<ProduitResponseDTO> ajouterProduit(@RequestBody ProduitReceiveDTO produitReceiveDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produitService.save(produitReceiveDTO));
    }
}