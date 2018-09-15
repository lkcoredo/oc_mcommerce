package com.clientui.controller;

import com.clientui.beans.CommandeBean;
import com.clientui.beans.PaiementBean;
import com.clientui.beans.ProductBean;
import com.clientui.proxies.MicroserviceCommandeProxy;
import com.clientui.proxies.MicroservicePaiementProxy;
import com.clientui.proxies.MicroserviceProduitsProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ClientController {

    @Autowired
    private MicroserviceProduitsProxy produitsProxy;

    @Autowired
    private MicroserviceCommandeProxy commandeProxy;

    @Autowired
    private MicroservicePaiementProxy paiementProxy;

    @RequestMapping("/")
    public String accueil(Model model) {
        List<ProductBean> produits = produitsProxy.listeDesProduits();
        model.addAttribute("produits", produits);
        return "accueil";
    }

    @RequestMapping("/details-produit/{id}")
    public String ficheProduit(@PathVariable int id, Model model){
        ProductBean produit = produitsProxy.recupererUnProduit(id);
        model.addAttribute("produit", produit);
        return "ficheProduit";
    }

    @RequestMapping(value = "/commander-produit/{idProduit}/{montant}")
    public String passerCommande(@PathVariable int idProduit, @PathVariable Double montant,  Model model){
        CommandeBean commande = new CommandeBean();

        commande.setProductId(idProduit);
        commande.setQuantite(1);
        commande.setDateCommande(new Date());

        CommandeBean commandeAjoutee = commandeProxy.ajouterCommande(commande);

        model.addAttribute("commande", commandeAjoutee);
        model.addAttribute("montant", montant);

        return "paiement";
    }

    @RequestMapping(value = "/payer-commande/{idCommande}/{montantCommande}")
    public String payerCommande(@PathVariable int idCommande, @PathVariable Double montantCommande, Model model){

        PaiementBean paiementAExcecuter = new PaiementBean();

        //on reseigne les détails du produit
        paiementAExcecuter.setIdCommande(idCommande);
        paiementAExcecuter.setMontant(montantCommande);
        paiementAExcecuter.setNumeroCarte(numcarte()); // on génère un numéro au hasard pour simuler une CB

        // On appel le microservice et (étape 7) on récupère le résultat qui est sous forme ResponseEntity<PaiementBean> ce qui va nous permettre de vérifier le code retour.
        ResponseEntity<PaiementBean> paiement = paiementProxy.payerUneCommande(paiementAExcecuter);

        Boolean paiementAccepte = false;
        //si le code est autre que 201 CREATED, c'est que le paiement n'a pas pu aboutir.
        if(paiement.getStatusCode() == HttpStatus.CREATED)
            paiementAccepte = true;

        model.addAttribute("paiementOk", paiementAccepte); // on envoi un Boolean paiementOk à la vue

        return "confirmation";
    }

    private Long numcarte() {
        return ThreadLocalRandom.current().nextLong(1000000000000000L,9000000000000000L );
    }
}
