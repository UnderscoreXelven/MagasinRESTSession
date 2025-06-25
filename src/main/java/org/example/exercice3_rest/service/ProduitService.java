package org.example.exercice3_rest.service;

import org.example.exercice3_rest.dto.ProduitReceiveDTO;
import org.example.exercice3_rest.dto.ProduitResponseDTO;
import org.example.exercice3_rest.entity.Produit;
import org.example.exercice3_rest.exception.NotFoundException;
import org.example.exercice3_rest.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {
    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public ProduitResponseDTO save(ProduitReceiveDTO produitReceiveDTO) {
        return produitRepository.save(produitReceiveDTO.dtoToEntity()).entityToDTO();
    }

    public ProduitResponseDTO get(Long id){
        return produitRepository.findById(id).orElseThrow(NotFoundException::new).entityToDTO();
    }

    public void delete(Long id){
        produitRepository.deleteById(id);
    }

    public List<ProduitResponseDTO> getAll(){
        return produitRepository.findAll().stream().map(Produit::entityToDTO).toList();
    }

    public ProduitResponseDTO update(Long id, ProduitReceiveDTO produitReceiveDTO){
        Produit produitFound = produitRepository.findById(id).orElseThrow(NotFoundException::new);
        produitFound.setNom(produitReceiveDTO.getNom());
        produitFound.setPrix(produitReceiveDTO.getPrix());
        Produit updatedProduit = produitRepository.save(produitFound);
        return updatedProduit.entityToDTO();
    }

}
