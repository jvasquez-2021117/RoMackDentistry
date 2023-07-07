
package org.josevasquez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javax.swing.JOptionPane;
import org.josevasquez.system.Principal;


public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
   
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }   
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    } 
    
    public void ventanaEspecialidades(){
        escenarioPrincipal.ventanaEspecialidades();
    }
    
    public void ventanaDoctores(){
        escenarioPrincipal.ventanaDoctores();
    }
    
    public void ventanaMedicamentos(){
        escenarioPrincipal.ventanaMedicamentos();
    }
    
    public void ventanaRecetas(){
        escenarioPrincipal.ventaRecetas();
    }
    
    public void ventanaDetalleReceta(){
        escenarioPrincipal.ventanaDetalleReceta();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
    public void ventanaCitas(){
        escenarioPrincipal.ventanaCitas();
    }
    
    public void salir(){
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres salir de la aplicacion?", "Salir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(respuesta == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }
    
    
}
