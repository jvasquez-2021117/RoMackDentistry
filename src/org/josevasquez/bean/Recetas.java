/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.josevasquez.bean;

import java.util.Date;

/**
 *
 * @author vqzjo
 */
public class Recetas {
    
    private int codigoReceta;
    private Date fechaReceta;
    private int numeroColegiado;

    public Recetas() {
    }

    public Recetas(int codigoReceta, Date fechaReceta, int numeroColegiado) {
        this.codigoReceta = codigoReceta;
        this.fechaReceta = fechaReceta;
        this.numeroColegiado = numeroColegiado;
    }

    public int getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(int codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public Date getFechaReceta() {
        return fechaReceta;
    }

    public void setFechaReceta(Date fechaReceta) {
        this.fechaReceta = fechaReceta;
    }

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }   

    @Override
    public String toString() {
        return getCodigoReceta() + " | " + getFechaReceta() + getNumeroColegiado();
    }
    
    
    
}
