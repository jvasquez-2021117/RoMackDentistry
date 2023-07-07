/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.josevasquez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.josevasquez.bean.Medicamentos;
import org.josevasquez.db.Conexion;
import org.josevasquez.report.GenerarReporte;
import org.josevasquez.system.Principal;

/**
 *
 * @author vqzjo
 */
public class MedicamentoController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medicamentos> listaMedi;
    
    private Principal escenarioPrincipal;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombreMedicamento;
    @FXML private TableView tblMedicamentos;
    @FXML private TableColumn colCodigoMedicamentos;
    @FXML private TableColumn colMedicamentos;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
//    public void cargarDatos(){
//        tblMedicamentos.setItems(getMedicamentos());
//        colCodigoMedicamentos.setCellValueFactory(new PropertyValueFactory<Medicamentos, Integer>("codigoMedicamento"));
//        colMedicamentos.setCellValueFactory(new PropertyValueFactory<Medicamentos, String>("nombreMedicamento"));
//    }
//    
//    public ObservableList<Medicamentos> getMedicamentos(){
//        ArrayList<Medicamentos> lista = new ArrayList<Medicamentos>();
//        try{
//            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos}");
//            ResultSet resultado = procedimiento.executeQuery();
//            while(resultado.next()){
//                lista.add(new Medicamentos(resultado.getInt("codigoMedicamento"), resultado.getString("nombreMedicamento")));
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return listaMedi = FXCollections.observableArrayList(lista);
//    }
//    
    
    public ObservableList<Medicamentos> getMedicamentos(){
        ArrayList<Medicamentos> lista = new ArrayList<Medicamentos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicamentos(resultado.getInt("codigoMedicamento"), resultado.getString("nombreMedicamento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaMedi = FXCollections.observableArrayList(lista);
    }
    
    public void cargarDatos(){
        tblMedicamentos.setItems(getMedicamentos());
        colCodigoMedicamentos.setCellValueFactory(new PropertyValueFactory<Medicamentos, Integer>("codigoMedicamento"));
        colMedicamentos.setCellValueFactory(new PropertyValueFactory<Medicamentos, String>("nombreMedicamento"));     
    }
    
    public void guardar(){
        Medicamentos registro = new Medicamentos();
        registro.setNombreMedicamento(txtNombreMedicamento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarMedicamentos(?)}");
            procedimiento.setString(1, registro.getNombreMedicamento());
            procedimiento.execute();
            listaMedi.add(registro);
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
                if(txtNombreMedicamento.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/org/josevasquez/images/Icon.png"));
                    alert.setContentText("Debe de ingresare una Descripción");
                    alert.show();
                } else{
                    desactivarControles();
                    guardar();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("org/josevasquez/images/AgregarG.png"));
                    imgEliminar.setImage(new Image("org/josevasquez/images/EliminarG.png"));
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
        }
    }
    
    public void seleccionarElemento(){
        try{
            txtCodigo.setText(String.valueOf(((Medicamentos)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
            txtNombreMedicamento.setText(((Medicamentos)tblMedicamentos.getSelectionModel().getSelectedItem()).getNombreMedicamento());
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Sin elementos");
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("org/josevasquez/images/AgregarG.png"));
                imgEliminar.setImage(new Image("org/josevasquez/images/EliminarG.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Estás seguro de eliminar este reistro?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarMedicamentos(?)}");
                            procedimiento.setInt(1, (((Medicamentos)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
                            procedimiento.execute();
                            listaMedi.remove(tblMedicamentos.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }  
                    }else{
                        limpiarControles();
                    }
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                }
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarMedicamentos(?, ?)}");
            Medicamentos registro = (Medicamentos)tblMedicamentos.getSelectionModel().getSelectedItem();
            registro.setNombreMedicamento(txtNombreMedicamento.getText());
            procedimiento.setInt(1, Integer.parseInt(txtCodigo.getText()));
            procedimiento.setString(2, txtNombreMedicamento.getText());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("org/josevasquez/images/Actualizar.png"));
                    imgReporte.setImage(new Image("org/josevasquez/images/Cancelar.png"));
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("org/josevasquez/images/EditarG.png"));
                imgReporte.setImage(new Image("org/josevasquez/images/ReporteG.png"));
                cargarDatos();
                desactivarControles();
                limpiarControles();
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
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("org/josevasquez/images/EditarG.png"));
                imgReporte.setImage(new Image("org/josevasquez/images/ReporteG.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("Foto", "org/josevasquez/images/PlantillaReporte.png");
        parametros.put("codigoMedicamento", null);
        GenerarReporte.mostrarReporte("ReporteMedicamentos.jasper", "Reporte Medicamentos", parametros);
    }
    
    public void activarControles(){
        txtNombreMedicamento.setEditable(true);
    }
    
    public void desactivarControles(){
        txtNombreMedicamento.setEditable(false);
    }
    
    public void limpiarControles(){
        txtNombreMedicamento.clear();
        txtCodigo.clear();
        tblMedicamentos.getSelectionModel().clearSelection();
    }
    
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
