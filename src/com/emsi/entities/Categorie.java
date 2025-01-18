package com.emsi.entities;

public class Categorie {

	private int id;
	private String code;
	private String libelle;
	private String description;
	private double prix;
	private static int count = 0;

	public Categorie(String libelle, String description, double prix) {
		super();
		this.id = ++count;
		this.libelle = libelle;
		this.description = description;
		this.prix = prix;
	}

	public Categorie() {
		// Default constructor
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Categorie [id=" + id + ", libelle=" + libelle + ", description=" + description + ", prix=" + prix + "]";
	}

}
