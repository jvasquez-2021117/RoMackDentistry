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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.josevasquez.bean.Paciente;
import org.josevasquez.db.Conexion;
import org.josevasquez.report.GenerarReporte;
import org.josevasquez.system.Principal;


public class PacientesController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    private enum operaciones{NUEVO, GUARGADAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Paciente> listaPaciente;
    private DatePicker fNacimiento;
    private DatePicker fPrimeraVisita;
    
    @FXML private TextField txtCodigoPaciente;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtSexo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private GridPane grpFechas;
    @FXML private TableView tblPacientes;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colSexo;
    @FXML private TableColumn colFechaNacimiento;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colPrimeraVisita;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        cargarDatos();
        fNacimiento = new DatePicker(Locale.ENGLISH);
        fNacimiento.setDateFormat(new SimpleDateFormat("yy-MM-dd"));
        fNacimiento.getCalendarView().todayButtonTextProperty().set("Today");
        fNacimiento.getCalendarView().setShowWeeks(false);
        grpFechas.add(fNacimiento, 0, 4);
        fNacimiento.getStylesheets().add("/org/josevasquez/resource/DatePicker.css");
        fPrimeraVisita = new DatePicker(Locale.ENGLISH);
        fPrimeraVisita.setDateFormat(new SimpleDateFormat("yy-MM-dd"));
        fPrimeraVisita.getCalendarView().todayButtonTextProperty().set("Today");
        fPrimeraVisita.getCalendarView().setShowWeeks(false);
        grpFechas.add(fPrimeraVisita, 6, 4);
        fPrimeraVisita.getStylesheets().add("/org/josevasquez/resource/DatePicker.css");
        fNacimiento.setDisable(true);
        fPrimeraVisita.setDisable(true);
        
    }
    
    public void cargarDatos(){
        tblPacientes.setItems(getPaciente());
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codigoPaciente"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombrePaciente"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidosPacientes"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaNacimiento"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("direccionPaciente"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Paciente, String>("telefonoPersonal"));
        colPrimeraVisita.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaPrimeraVisita"));
    }
    
    public void seleccionarElemento(){   
        
        try{    
        txtCodigoPaciente.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        txtNombres.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombrePaciente());
        txtApellidos.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidosPacientes());
        txtSexo.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
        fNacimiento.selectedDateProperty().set(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        txtDireccion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccionPaciente());
        txtTelefono.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        fPrimeraVisita.selectedDateProperty().set(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaPrimeraVisita());
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "sin elementos");
        }
    }
     
    
    public ObservableList<Paciente> getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"), resultado.getString("nombrePaciente"),
                        resultado.getString("apellidosPacientes"), resultado.getString("sexo"),
                        resultado.getDate("fechaNacimiento"), resultado.getString("direccionPaciente"),
                        resultado.getString("telefonoPersonal"), resultado.getDate("fechaPrimeraVisita")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableArrayList(lista);
    }
    
    
    public void desactivarControles(){
        txtCodigoPaciente.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtSexo.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        fNacimiento.setDisable(true);
        fPrimeraVisita.setDisable(true);
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
                tipoDeOperacion = operaciones.GUARGADAR;
                break;
            case GUARGADAR:
                if(txtCodigoPaciente.getText().isEmpty() || txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() ||
                        txtSexo.getText().isEmpty() || fNacimiento.getSelectedDate() == null || txtDireccion.getText().isEmpty() ||
                        txtTelefono.getText().isEmpty() || fPrimeraVisita.getSelectedDate() == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("org/josevasquez/images/Icon.png"));
                    alert.setContentText("Rebise que haya ingresa datos en todos los campos");
                    alert.show();
                } else{
                    desactivarControles();
                    guardar();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/josevasquez/images/Nuevo.png"));
                    imgEliminar.setImage(new Image("/org/josevasquez/images/Eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                }
                break;
            
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARGADAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/josevasquez/images/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/josevasquez/images/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro que quieres eliminar el registro?","Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPacientes(?)}");
                            procedimiento.setInt(1, ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                            procedimiento.execute();
                            listaPaciente.remove(tblPacientes.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Debe leccionar un elemento");
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            
            case NINGUNO:
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/josevasquez/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/josevasquez/images/Cancelar.png"));
                    activarControles();
                    txtCodigoPaciente.setEditable(false);
                    
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe selecionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/josevasquez/images/Editar.png"));
                imgReporte.setImage(new Image("/org/josevasquez/images/Reporte.png"));
                cargarDatos();
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarPacientes(?, ?, ?, ?, ?, ?, ?, ?)}");
            Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
            registro.setNombrePaciente(txtNombres.getText());
            registro.setApellidosPacientes(txtApellidos.getText());
            registro.setSexo(txtSexo.getText());
            registro.setFechaNacimiento(fNacimiento.getSelectedDate());
            registro.setDireccionPaciente(txtDireccion.getText());
            registro.setTelefonoPersonal(txtTelefono.getText());
            registro.setFechaPrimeraVisita(fPrimeraVisita.getSelectedDate());
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getNombrePaciente());
            procedimiento.setString(3, registro.getApellidosPacientes());
            procedimiento.setString(4, registro.getSexo());
            procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(6, registro.getDireccionPaciente());
            procedimiento.setString(7, registro.getTelefonoPersonal());
            procedimiento.setDate(8, new java.sql.Date(registro.getFechaPrimeraVisita().getTime()));
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
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
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/josevasquez/images/Editar.png"));
                imgReporte.setImage(new Image("/org/josevasquez/images/Reporte.png"));
                tblPacientes.getSelectionModel().clearSelection();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
        public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("Foto", "org/josevasquez/images/PlantillaReporte.png");
        parametros.put("codigoPaciente", null);
        GenerarReporte.mostrarReporte("ReportePacientes.jasper", "ReportePaciente", parametros);
    }
    
    public void guardar(){
        Paciente registro = new Paciente();
        registro.setCodigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
        registro.setNombrePaciente(txtNombres.getText());
        registro.setApellidosPacientes(txtApellidos.getText());
        registro.setSexo(txtSexo.getText());
        registro.setFechaNacimiento(fNacimiento.getSelectedDate());
        registro.setDireccionPaciente(txtDireccion.getText());
        registro.setTelefonoPersonal(txtTelefono.getText());
        registro.setFechaPrimeraVisita(fPrimeraVisita.getSelectedDate());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPacientes(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getNombrePaciente());
            procedimiento.setString(3, registro.getApellidosPacientes());
            procedimiento.setString(4, registro.getSexo());
            procedimiento.setDate(5,new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(6, registro.getDireccionPaciente());
            procedimiento.setString(7, registro.getTelefonoPersonal());
            procedimiento.setDate(8,new java.sql.Date(registro.getFechaPrimeraVisita().getTime()));
            procedimiento.execute();
            listaPaciente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    
    public void activarControles(){
        txtCodigoPaciente.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtSexo.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        fNacimiento.setDisable(false);
        fPrimeraVisita.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoPaciente.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtSexo.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        tblPacientes.getSelectionModel().clearSelection();
        fNacimiento.setSelectedDate(null);
        fPrimeraVisita.setSelectedDate(null);
    }
  
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
