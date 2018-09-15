package com.clientui.beans;

public class PaiementBean {

    private int id;
    private int idCommande;
    private double montant;
    private long numeroCarte;

    public PaiementBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public long getNumeroCarte() {
        return numeroCarte;
    }

    public void setNumeroCarte(long numeroCarte) {
        this.numeroCarte = numeroCarte;
    }

    @Override
    public String toString() {
        return "PaiementBean{" +
                "id=" + id +
                ", idCommande=" + idCommande +
                ", montant=" + montant +
                ", numeroCarte=" + numeroCarte +
                '}';
    }
}
