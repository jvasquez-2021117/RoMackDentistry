/*
Nombre completo: Jose Marcos Vasquez Roman
Código Técnico: IN5AM
Carnet: 2021117
Fecha de creación: 5/4/2022
Modificaciones:
*/
package org.josevasquez.system;





import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.josevasquez.controller.CitasController;
import org.josevasquez.controller.DetalleRecetaController;
import org.josevasquez.controller.DoctorController;
import org.josevasquez.controller.EspecialidadesController;
import org.josevasquez.controller.LoginController;
import org.josevasquez.controller.MedicamentoController;
import org.josevasquez.controller.MenuPrincipalController;
import org.josevasquez.controller.PacientesController;
import org.josevasquez.controller.ProgramadorController;
import org.josevasquez.controller.RecetaController;
import org.josevasquez.controller.UsuarioController;




public class Principal extends Application {
private Stage escenarioPrincipal;
private Scene escena;

private final String PAQUETE_VISTA   = "/org/josevasquez/view/"; 



@Override
public void start(Stage escenarioPrincipal) throws Exception {
this.escenarioPrincipal = escenarioPrincipal;
this.escenarioPrincipal.setTitle("RoMackDentistry");
escenarioPrincipal.getIcons().add(new Image("/org/josevasquez/images/Icon.png"));
//Parent root = FXMLLoader.load(getClass().getResource("/org/josevasquez/view/PacientesView.fxml"));
//Scene escena = new Scene(root);
//escenarioPrincipal.setScene(escena);

menuPrincipal();
escenarioPrincipal.show();
}

public void ventanaLogin(){
    try{
        LoginController login = (LoginController)cambiarEscena("LoginView.fxml", 525, 480);
        login.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void ventanaUsuario(){
    try{
        UsuarioController usuario = (UsuarioController)cambiarEscena("UsuarioView.fxml", 866, 400);
        usuario.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void menuPrincipal(){
    try{
        MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 600, 311);
        ventanaMenu.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void ventanaProgramador(){
    try{
        ProgramadorController ventanaProgramador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 600, 400);
        ventanaProgramador.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void ventanaPacientes(){
    try{
        PacientesController ventanaPacientes = (PacientesController)cambiarEscena("PacientesView.fxml", 866, 400);
        ventanaPacientes.setEscenarioPrincipal(this);
    }catch(Exception e){
            e.printStackTrace();
            }
}

public void ventanaEspecialidades(){
    try{
        EspecialidadesController ventanaEspecialidades = (EspecialidadesController)cambiarEscena("EspecialidadesView.fxml", 866, 400);
        ventanaEspecialidades.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
 }

public void ventanaDoctores(){
    try{
        DoctorController ventanaDoctores = (DoctorController)cambiarEscena("DoctoresView.fxml", 866, 400);
        ventanaDoctores.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void ventanaMedicamentos(){
    try{
        MedicamentoController ventanaMedicamentos = (MedicamentoController)cambiarEscena("MedicamentosView.fxml", 866, 400);
        ventanaMedicamentos.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void ventaRecetas(){
    try{
        RecetaController ventanaReceta = (RecetaController)cambiarEscena("RecetasView.fxml", 866, 400);
        ventanaReceta.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void ventanaDetalleReceta(){
    try{
        DetalleRecetaController ventanaDetalleReceta = (DetalleRecetaController)cambiarEscena("DetalleRecetaView.fxml", 866, 400);
        ventanaDetalleReceta.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void ventanaCitas(){
    try{
        CitasController ventanaCitas = (CitasController)cambiarEscena("CitasView.fxml", 866, 400);
        ventanaCitas.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
}

public static void main(String[] args) {
launch(args);
}


public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML =  new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
}

//Scene((AnchorPane)cargadorFXML.load((archivo), ancho, alto));
}


//friill frame