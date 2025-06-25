package org.example.exercice3_rest.controller;

import jakarta.servlet.http.HttpSession;
import org.example.exercice3_rest.service.PanierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/panier/")
public class PanierController {
    private final PanierService panierService;
    private final HttpSession session;

    public PanierController(PanierService panierService, HttpSession session) {
        this.panierService = panierService;
        this.session = session;
    }

    //Quantité = nombre d’occurrences du produit dans la liste
    @GetMapping("/ajouter/{id}")
    public ResponseEntity<String> ajouterProduit(@PathVariable Long id) {
        Map<Long, Integer> panier = (Map<Long, Integer>) session.getAttribute("panier");
        try{
            panier = panierService.addItemToPanier(panier, id);
            session.setAttribute("panier", panier);
            return ResponseEntity.ok("Produit ajouté au panier");
        } catch(IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Retire une occurrence du produit seulement
    @GetMapping("/retirer/{id}")
    public ResponseEntity<String> retirerProduit(@PathVariable Long id) {
        Map<Long, Integer> panier = (Map<Long, Integer>) session.getAttribute("panier");
        try {
            panier = panierService.removeItemFromPanier(panier, id);
            session.setAttribute("panier", panier);
            return ResponseEntity.ok("Produit retiré du panier");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //Panier inexistant
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //Produit non trouvé dans le panier
        }
    }

    //Affichage
    @GetMapping("/valider")
    public ResponseEntity<String> validerPanier() {
        Map<Long, Integer> panier = (Map<Long, Integer>) session.getAttribute("panier");
        String affichagePanier = "";
        try{
            affichagePanier = panierService.validerPanier(panier);
            return ResponseEntity.ok(affichagePanier);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Panier vide");
        }
    }
}
