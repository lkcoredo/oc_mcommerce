package com.mpaiement.web.controller;

import com.mpaiement.beans.CommandeBean;
import com.mpaiement.dao.PaiementDao;
import com.mpaiement.model.Paiement;
import com.mpaiement.proxies.MicroserviceCommandeProxy;
import com.mpaiement.web.exceptions.PaiementExistantException;
import com.mpaiement.web.exceptions.PaiementImpossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PaiementController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PaiementDao paiementDao;

    @Autowired
    MicroserviceCommandeProxy microserviceCommandeProxy;


    @PostMapping(value = "/paiement")
    public ResponseEntity<Paiement>  payerUneCommande(@RequestBody Paiement paiement){
        Paiement paiementExistant = paiementDao.findByidCommande(paiement.getIdCommande());
        if(paiementExistant != null) {
            throw new PaiementExistantException("Cette commande est déjà payée");
        }

        Paiement nouveauPaiement = paiementDao.save(paiement);
        if(nouveauPaiement == null) {
            throw new PaiementImpossibleException("Erreur, impossible d'établir le paiement, réessayez plus tard");
        }

        Optional<CommandeBean> commandeReq = microserviceCommandeProxy.recupererUneCommande(paiement.getIdCommande());
        CommandeBean commandeBean = commandeReq.get();
        commandeBean.setCommandePayee(true);
        microserviceCommandeProxy.updateCommande(commandeBean);

        log.info("Paiement n°" + paiementExistant.getId() + ", commande n° " + paiementExistant.getIdCommande());
        return new ResponseEntity<Paiement>(nouveauPaiement, HttpStatus.CREATED);

    }

}
