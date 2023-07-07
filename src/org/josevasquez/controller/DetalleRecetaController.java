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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.josevasquez.bean.DetalleReceta;
import org.josevasquez.bean.Medicamentos;
import org.josevasquez.bean.Recetas;
import org.josevasquez.db.Conexion;
import org.josevasquez.report.GenerarReporte;
import org.josevasquez.system.Principal;

/**
 *
 * @author vqzjo
 */
public class DetalleRecetaController implements Initializable {
    
    private Principal escenarioPrincipal;
    private ObservableList<DetalleReceta> listaDetalle;
    private ObservableList<Recetas> listaReceta;
    private ObservableList<Medicamentos> listaMedicamentos;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, CANCELAR, ACTUALIZAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    @FXML Button btnNuevo;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReporte;
    @FXML TextField txtCodigo;
    @FXML TextField txtDosis;
    @FXML ComboBox cmbCodigoReceta;
    @FXML ComboBox cmbCodigoMedicamento;
    @FXML TableView tblDetalleReceta;
    @FXML TableColumn colCodigo;
    @FXML TableColumn colDosis;
    @FXML TableColumn colCodigoReceta;
    @FXML TableColumn colCodigoMedicamento;
    @FXML ImageView imgNuevo;
    @FXML ImageView imgEliminar;
    @FXML ImageView imgEditar;
    @FXML ImageView imgReporte;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoReceta.setItems(getRecetas());
        cmbCodigoMedicamento.setItems(getMedicamentos());
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setDisable(true);
    }
    
    public ObservableList<DetalleReceta> getDetalle(){
        ArrayList<DetalleReceta> lista = new ArrayList<DetalleReceta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetalleReceta}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleReceta(resultado.getInt("codigoDetalleReceta"), resultado.getString("dosis"),
                resultado.getInt("codigoReceta"), resultado.getInt("codigoMedicamento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDetalle = FXCollections.observableArrayList(lista);
    }
    
    public void cargarDatos(){
        tblDetalleReceta.setItems(getDetalle());
        colCodigo.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoDetalleReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("dosis"));
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoReceta"));
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoMedicamento"));
    }
    
    public void seleccionarElemento(){
        try{
            txtCodigo.setText(String.valueOf(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta()));
            txtDosis.setText(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getDosis());
            cmbCodigoReceta.getSelectionModel().select(buscarReceta(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            cmbCodigoMedicamento.getSelectionModel().select(buscarMedicamentos(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Sin Elemtos");
        }
    }
    
    public Recetas buscarReceta(int codigoReceta){
        Recetas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarRecetas(?)}");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Recetas(registro.getInt("codigoReceta"), registro.getDate("fechaReceta"), registro.getInt("numeroColegiado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Medicamentos buscarMedicamentos(int codigoMedicamento){
        Medicamentos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarMedicamentos(?)}");
            procedimiento.setInt(1, codigoMedicamento);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medicamentos(registro.getInt("codigoMedicamento"), registro.getString("nombreMedicamento"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Recetas> getRecetas(){
        ArrayList<Recetas> lista = new ArrayList<Recetas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRecetas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Recetas(resultado.getInt("codigoReceta"), resultado.getDate("fechaReceta"),
                resultado.getInt("numeroColegiado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaReceta = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Medicamentos> getMedicamentos(){
        ArrayList<Medicamentos> lista = new ArrayList<Medicamentos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarMedicamentos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicamentos(resultado.getInt("codigoMedicamento"), resultado.getString("nombreMedicamento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaMedicamentos = FXCollections.observableArrayList(lista);
    }
    
    public void guardar(){
        DetalleReceta registro = new DetalleReceta();
        registro.setDosis(txtDosis.getText());
        registro.setCodigoReceta(((Recetas)cmbCodigoReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
        registro.setCodigoMedicamento(((Medicamentos)cmbCodigoMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDetalleReceta(?, ?, ?)}");
            procedimiento.setString(1, registro.getDosis());
            procedimiento.setInt(2, registro.getCodigoReceta());
            procedimiento.setInt(3, registro.getCodigoMedicamento());
            procedimiento.execute();
            listaDetalle.add(registro);
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
                imgNuevo.setImage(new Image("/org/josevasquez/images/Guardar.png"));
                imgEliminar.setImage(new Image("org/josevasquez/images/Cancelar.png"));
                btnReporte.setDisable(true);
                btnEditar.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(txtDosis.getText().isEmpty() || cmbCodigoReceta.getValue() == null || cmbCodigoMedicamento.getValue() == null){
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
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("org/josevasquez/images/AgregarG.png"));
                imgEliminar.setImage(new Image("org/josevasquez/images/EliminarG.png"));
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("org/josevasquez/images/AgregarG.png"));
                imgEliminar.setImage(new Image("org/josevasquez/images/EliminarG.png"));
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                break;
            default:
                if(tblDetalleReceta.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este elemento?", "Eliminar Elemento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDetalleReceta(?)}");
                            procedimiento.setInt(1, Integer.parseInt(txtCodigo.getText()));
                            procedimiento.execute();
                            listaDetalle.remove(tblDetalleReceta.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarDetalleReceta(?, ?)");
            DetalleReceta registro = (DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem();
            registro.setCodigoDetalleReceta(Integer.parseInt(txtCodigo.getText()));
            registro.setDosis(txtDosis.getText());
            procedimiento.setInt(1, registro.getCodigoDetalleReceta());
            procedimiento.setString(2, registro.getDosis());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblDetalleReceta.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("org/josevasquez/images/Actualizar.png"));
                    imgReporte.setImage(new Image("org/josevasquez/images/Cancelar.png"));
                    txtCodigo.setEditable(false);
                    cmbCodigoReceta.setDisable(true);
                    cmbCodigoMedicamento.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
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
        parametros.put("codigoDetalleReceta", null);
        GenerarReporte.mostrarReporte("DetalleRece.jasper", "Reporte Detalle Receta", parametros);
        }
    
    public void desactivarControles(){
        txtDosis.setEditable(false);
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setDisable(true);
    }
    
    public void activarControles(){
        txtDosis.setEditable(true);
        cmbCodigoReceta.setDisable(false);
        cmbCodigoMedicamento.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigo.clear();
        txtDosis.clear();
        cmbCodigoReceta.getSelectionModel().clearSelection();
        cmbCodigoReceta.setValue(null);
        cmbCodigoMedicamento.getSelectionModel().clearSelection();
        cmbCodigoMedicamento.setValue(null);
        tblDetalleReceta.getSelectionModel().clearSelection();
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
