/*
 * Copyright (C) 2016 Gemeente Amsterdam, Marktbureau
 */
package com.amsterdam.marktbureau.makkelijkemarkt.api.model;

import android.content.ContentValues;

import com.amsterdam.marktbureau.makkelijkemarkt.data.MakkelijkeMarktProvider;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcolangebeeke
 */
public class ApiKoopman {

    private int id;
    private String erkenningsnummer;
    private String voorletters;
    private String achternaam;
    private String telefoon;
    private String email;
    private String fotoUrl;
    private String fotoMediumUrl;
    private String status;
    private int perfectViewNummer;
    private List<ApiSollicitatie> sollicitaties = new ArrayList<ApiSollicitatie>();

    /**
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getErkenningsnummer() {
        return erkenningsnummer;
    }

    /**
     * @param erkenningsnummer
     */
    public void setErkenningsnummer(String erkenningsnummer) {
        this.erkenningsnummer = erkenningsnummer;
    }

    /**
     * @return
     */
    public String getVoorletters() {
        return voorletters;
    }

    /**
     * @param voorletters
     */
    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    /**
     * @return
     */
    public String getAchternaam() {
        return achternaam;
    }

    /**
     * @param achternaam
     */
    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    /**
     * @return
     */
    public String getTelefoon() {
        return telefoon;
    }

    /**
     * @param telefoon
     */
    public void setTelefoon(String telefoon) {
        this.telefoon = telefoon;
    }

    /**
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return
     */
    public String getFotoUrl() {
        return fotoUrl;
    }

    /**
     * @param fotoUrl
     */
    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    /**
     * @return
     */
    public String getFotoMediumUrl() {
        return fotoMediumUrl;
    }

    /**
     * @param fotoMediumUrl
     */
    public void setFotoMediumUrl(String fotoMediumUrl) {
        this.fotoMediumUrl = fotoMediumUrl;
    }

    /**
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return
     */
    public int getPerfectViewNummer() {
        return perfectViewNummer;
    }

    /**
     * @param perfectViewNummer
     */
    public void setPerfectViewNummer(int perfectViewNummer) {
        this.perfectViewNummer = perfectViewNummer;
    }

    /**
     * @return
     */
    public List<ApiSollicitatie> getSollicitaties() {
        return sollicitaties;
    }

    /**
     * @param sollicitaties
     */
    public void setSollicitaties(List<ApiSollicitatie> sollicitaties) {
        this.sollicitaties = sollicitaties;
    }

    /**
     * Convert object to type contentvalues
     * @return contentvalues object containing the objects name value pairs
     */
    public ContentValues toContentValues() {
        ContentValues koopmanValues = new ContentValues();

        koopmanValues.put(MakkelijkeMarktProvider.Koopman.COL_ID, getId());
        koopmanValues.put(MakkelijkeMarktProvider.Koopman.COL_ERKENNINGSNUMMER, getErkenningsnummer());
        koopmanValues.put(MakkelijkeMarktProvider.Koopman.COL_VOORLETTERS, getVoorletters());
        koopmanValues.put(MakkelijkeMarktProvider.Koopman.COL_ACHTERNAAM, getAchternaam());
        koopmanValues.put(MakkelijkeMarktProvider.Koopman.COL_TELEFOON, getTelefoon());
        koopmanValues.put(MakkelijkeMarktProvider.Koopman.COL_EMAIL, getEmail());
        koopmanValues.put(MakkelijkeMarktProvider.Koopman.COL_FOTO_URL, getFotoUrl());
        koopmanValues.put(MakkelijkeMarktProvider.Koopman.COL_FOTO_MEDIUM_URL, getFotoMediumUrl());
        koopmanValues.put(MakkelijkeMarktProvider.Koopman.COL_STATUS, getStatus());
        koopmanValues.put(MakkelijkeMarktProvider.Koopman.COL_PERFECTVIEWNUMMER, getPerfectViewNummer());

        // the list of sollicitaties is not added to the contentvalues object

        return koopmanValues;
    }
}