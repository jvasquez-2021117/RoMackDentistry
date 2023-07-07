/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.josevasquez.bean;

/**
 *
 * @author vqzjo
 */
public class Especialidad {
    
    private int codigoEspecialidad;
    private String descripcion;

    public Especialidad() {
    }

    public Especialidad(int codigoEspecialidad, String descripcion) {
        this.codigoEspecialidad = codigoEspecialidad;
        this.descripcion = descripcion;
    }

    public int getCodigoEspecialidad() {
        return codigoEspecialidad;
    }

    public void setCodigoEspecialidad(int codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return getCodigoEspecialidad() + " | " + getDescripcion();
    }
    
    
    
    
    
}
