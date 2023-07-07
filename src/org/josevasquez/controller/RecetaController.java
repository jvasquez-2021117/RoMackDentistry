/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.josevasquez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.josevasquez.bean.Doctor;
import org.josevasquez.bean.Recetas;
import org.josevasquez.db.Conexion;
import org.josevasquez.report.GenerarReporte;
import org.josevasquez.system.Principal;

/**
 *
 * @author vqzjo
 */
public class RecetaController implements Initializable {
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, CANCELAR, ACTUALIZAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Recetas> listaRecetas;
    private DatePicker fReceta;
    private ObservableList<Doctor> listaDoctores;
    
    private Principal escenarioPrincipal;
    
    @FXML Button btnNuevo;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReporte;
    @FXML TextField txtCodigo;
    @FXML GridPane grpRecetas;
    @FXML ImageView imgNuevo;
    @FXML ImageView imgEliminar;
    @FXML ImageView imgEditar;
    @FXML ImageView imgReporte;
    @FXML TableView tblRecetas;
    @FXML TableColumn colCodigo;
    @FXML TableColumn colFechas;
    @FXML TableColumn colColegiado;
    @FXML ComboBox cmbColegiado;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fReceta = new DatePicker(Locale.ENGLISH);
        fReceta.setDateFormat(new SimpleDateFormat("yy-MM-dd"));
        fReceta.getCalendarView().todayButtonTextProperty().set("Today");
        fReceta.getCalendarView().setShowWeeks(false);
        grpRecetas.add(fReceta, 2, 1);
        fReceta.getStylesheets().add("/org/josevasquez/resource/DatePicker.css");
        cmbColegiado.setItems(getDoctor());
        cmbColegiado.setDisable(true);
        fReceta.setDisable(true);
        desactivarControles();
    }
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"), resultado.getString("nombresDoctor"),
                resultado.getString("apellidosDoctor"), resultado.getString("telefonoContacto"),
                resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctores = FXCollections.observableArrayList(lista);
    }
    
    public void cargarDatos(){
        tblRecetas.setItems(getReceta());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Recetas, Integer>("codigoReceta"));
        colFechas.setCellValueFactory(new PropertyValueFactory<Recetas, Date>("fechaReceta"));
        colColegiado.setCellValueFactory(new PropertyValueFactory<Recetas, Integer>("numeroColegiado"));
    }
    
    public ObservableList<Recetas>getReceta(){
        ArrayList<Recetas> lista = new ArrayList<Recetas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRecetas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Recetas(resultado.getInt("codigoReceta"),
                        resultado.getDate("fechaReceta"), resultado.getInt("numeroColegiado")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaRecetas = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        try{
            txtCodigo.setText(String.valueOf(((Recetas)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            fReceta.selectedDateProperty().set(((Recetas)tblRecetas.getSelectionModel().getSelectedItem()).getFechaReceta());
            cmbColegiado.getSelectionModel().select(buscarDoctor(((Recetas)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Sin elementos");
        }
    }
    
    public Doctor buscarDoctor(int codigoColegiado){
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctores(?)}");
            procedimiento.setInt(1, codigoColegiado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Doctor(registro.getInt("numeroColegiado"), registro.getString("nombresDoctor"), registro.getString("apellidosDoctor"),
                registro.getString("telefonoContacto"), registro.getInt("codigoEspecialidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void guardar(){
        Recetas registro = new Recetas();
        registro.setFechaReceta(fReceta.getSelectedDate());
        registro.setNumeroColegiado(((Doctor)cmbColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarRecetas(?,?)}");
            procedimiento.setDate(1,new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.setInt(2, registro.getNumeroColegiado());
            procedimiento.execute();
            listaRecetas.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/josevasquez/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/josevasquez/images/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(fReceta.getSelectedDate() == null || cmbColegiado.getValue() == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("org/josevasquez/images/Icon.png"));
                    alert.setContentText("Rebise que haya ingresado datos en todos los campos excepto el Codigo, este dato lo ingresa automaticamente el sistema");
                    alert.show();
                }else{
                guardar();
                cargarDatos();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("org/josevasquez/images/AgregarG.png"));
                imgEliminar.setImage(new Image("org/josevasquez/images/EliminarG.png"));
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
                
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                fReceta.setDisable(true);
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("org/josevasquez/images/AgregarG.png"));
                imgEliminar.setImage(new Image("org/josevasquez/images/EliminarG.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este registro?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarRecetas(?)}");
                            procedimiento.setInt(1, Integer.parseInt(txtCodigo.getText()));
                            procedimiento.execute();
                            listaRecetas.remove(tblRecetas.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                }
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarRecetas(?,?)}");
            Recetas registro = (Recetas)tblRecetas.getSelectionModel().getSelectedItem();
            registro.setFechaReceta(fReceta.getSelectedDate());
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.execute();
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("org/josevasquez/images/Actualizar.png"));
                    imgReporte.setImage(new Image("org/josevasquez/images/Cancelar.png"));
                    txtCodigo.setEditable(false);
                    cmbColegiado.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                cargarDatos();
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("org/josevasquez/images/EditarG.png"));
                imgReporte.setImage(new Image("org/josevasquez/images/ReporteG.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                limpiarControles();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("org/josevasquez/images/EditarG.png"));
                imgReporte.setImage(new Image("org/josevasquez/images/ReporteG.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("Foto", "org/josevasquez/images/PlantillaReporte.png");
        parametros.put("codigoReceta", null);
        if(tblRecetas.getSelectionModel().getSelectedItem() ==  null){
            GenerarReporte.mostrarReporte("ReporteReceta.jasper", "Reporte Receta", parametros);
        }else{
            int codReceta = (((Recetas)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
            parametros.put("codReceta", codReceta);
            GenerarReporte.mostrarReporte("Receta.jasper", "Reporte Receta", parametros);
        }
    }
    
    public void activarControles(){
        fReceta.setDisable(false);
        cmbColegiado.setDisable(false);
    }
    
    public void desactivarControles(){
        cmbColegiado.setDisable(true);
        fReceta.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigo.clear();
        fReceta.setSelectedDate(null);
        cmbColegiado.getSelectionModel().clearSelection();
        cmbColegiado.setValue(null);
        tblRecetas.getSelectionModel().clearSelection();
    }
    
    public Principal principal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
