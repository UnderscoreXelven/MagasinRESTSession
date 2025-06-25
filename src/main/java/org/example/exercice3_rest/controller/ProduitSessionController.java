package org.example.exercice3_rest.controller;

import jakarta.servlet.http.HttpSession;
import org.example.exercice3_rest.dto.ProduitReceiveDTO;
import org.example.exercice3_rest.dto.ProduitResponseDTO;
import org.example.exercice3_rest.service.ProduitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/panier/session")
public class ProduitSessionController {

    private final ProduitService produitService;
    private final HttpSession session;

    public ProduitSessionController(ProduitService produitService, HttpSession session) {
        this.produitService = produitService;
        this.session = session;
    }

    @GetMapping("/produit/{id}")
    public ResponseEntity<ProduitResponseDTO> getProduit(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(produitService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/produit")
    public ResponseEntity<ProduitResponseDTO> ajouterProduit(@RequestBody ProduitReceiveDTO produitReceiveDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produitService.save(produitReceiveDTO));
    }

    //Quantité = nombre d’occurrences du produit dans la liste
    @GetMapping("/ajouter/{id}")
    public ResponseEntity<String> ajouterProduit(@PathVariable Long id) {
        Map<Long, Integer> panier = (Map<Long, Integer>) session.getAttribute("panier");
        if (panier == null) {
            panier = new HashMap<>();
        }

        //Vérifier que le produit existe
        try {
            produitService.get(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé");
        }

        panier.put(id, panier.getOrDefault(id, 0) + 1);
        session.setAttribute("panier", panier);
        return ResponseEntity.ok("Produit ajouté au panier");
    }

    //Retire une occurrence du produit seulement
    @GetMapping("/retirer/{id}")
    public ResponseEntity<String> retirerProduit(@PathVariable Long id) {
        Map<Long, Integer> panier = (Map<Long, Integer>) session.getAttribute("panier");
        if (panier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Panier inexistant");
        }
        if (panier.get(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé dans le panier");
        } else{
            panier.put(id, panier.get(id) - 1);
            //Si il n'y a plus de quantité dans le panier, alors on retire le produit
            if (panier.get(id) == 0) {
                panier.remove(id);
            }
            session.setAttribute("panier", panier);
        }
        return ResponseEntity.ok("Produit retiré du panier");
    }

    //Affichage
    @GetMapping("/valider")
    public ResponseEntity<?> validerPanier() {
        Map<Long, Integer> panier = (Map<Long, Integer>) session.getAttribute("panier");
        if (panier == null || panier.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Panier vide");
        }

        //Récupérer les produits et calculer le total
        String affichagePanier = "Panier :\n";
        double total = 0;
        for (Map.Entry<Long, Integer> entry : panier.entrySet()) {
            ProduitResponseDTO detail = produitService.get(entry.getKey());
            total += detail.getPrix() * entry.getValue();

            //String pour afficher le panier;
            affichagePanier += detail.getNom() + " x" + entry.getValue() + " = " + detail.getPrix() * entry.getValue() + "\n";
        }
        affichagePanier += "Total : " + total;


        return ResponseEntity.ok(affichagePanier);
    }
}