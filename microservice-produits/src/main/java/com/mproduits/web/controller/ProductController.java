package com.mproduits.web.controller;

import com.mproduits.configurations.ApplicationPropertiesConfiguration;
import com.mproduits.dao.ProductDao;
import com.mproduits.model.Product;
import com.mproduits.web.exceptions.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductDao productDao;

    @Autowired
    ApplicationPropertiesConfiguration appProperties;

    // Affiche la liste de tous les produits disponibles
    @GetMapping(value = "/produits")
    public List<Product> listeDesProduits(){
        List<Product> products = productDao.findAll();

        if(products.isEmpty()) {
            throw new ProductNotFoundException("Aucun produit n'est disponible à la vente");
        }

        log.info("Récupération de la liste des produits");
        return products.subList(0, appProperties.getLimitDeProduits());

    }

    //Récuperer un produit par son id
    @GetMapping( value = "/produits/{id}")
    public Optional<Product> recupererUnProduit(@PathVariable int id) {
        Optional<Product> product = productDao.findById(id);

        if(!product.isPresent()) {
            throw new ProductNotFoundException("Le produit correspondant à l'id " + id + " n'existe pas");
        }

        log.info("Récupération du produit d'id : " + id);
        return product;
    }
}

