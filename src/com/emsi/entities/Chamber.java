package com.emsi.entities;

public class Chamber {
    private int id;
    private String telephone;
    private int categorieId;
    private Categorie categorie;
    private String status;

    public Chamber() {
        // Default constructor
    }

    public Chamber(int id, String telephone, int categorieId) {
        this.id = id;
        this.telephone = telephone;
        this.categorieId = categorieId;
    }

    public Chamber(String telephone, int categorieId) {
        this.telephone = telephone;
        this.categorieId = categorieId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        this.categorieId = categorie.getId();
    }

    @Override
    public String toString() {
        return "Chamber [id=" + id + ", telephone=" + telephone + ", categorieId=" + categorieId + "]";
    }
}
