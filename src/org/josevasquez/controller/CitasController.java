/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.josevasquez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.josevasquez.bean.Citas;
import org.josevasquez.bean.Doctor;
import org.josevasquez.bean.Paciente;
import org.josevasquez.db.Conexion;
import org.josevasquez.report.GenerarReporte;
import org.josevasquez.system.Principal;

/**
 *
 * @author informatica
 */
public class CitasController implements Initializable {
    
    private Principal escenarioPrincipal;
    private ObservableList<Citas> listaCitas;
    private ObservableList<Doctor> listaDoctores;
    private ObservableList<Paciente> listaPaciente;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, CANCELAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private DatePicker fCitas;
    
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtHora;
    @FXML private TextField txtTratamiento;
    @FXML private TextField txtCondicion;
    @FXML private ComboBox cmbPaciente;
    @FXML private ComboBox cmbColegiado;
    @FXML private GridPane grpFechas;
    @FXML private TableView tblCitas;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colTratamiento;
    @FXML private TableColumn colCondicion;
    @FXML private TableColumn colPaciente;
    @FXML private TableColumn colColegiado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fCitas = new DatePicker(Locale.ENGLISH);
        fCitas.setDateFormat(new SimpleDateFormat("yy-MM-dd"));
        fCitas.getCalendarView().todayButtonTextProperty().set("Today");
        fCitas.getCalendarView().setShowWeeks(false);
        grpFechas.add(fCitas, 2, 1);
        fCitas.getStylesheets().add("/org/josevasquez/resource/DatePicker.css");
        cmbPaciente.setItems(getPaciente());
        cmbColegiado.setItems(getDoctor());
        fCitas.setDisable(true);
        cmbPaciente.setDisable(true);
        cmbColegiado.setDisable(true);
    }
    
    public ObservableList<Citas> getCita(){
        ArrayList<Citas> lista = new ArrayList<Citas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCitas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Citas(resultado.getInt("codigoCita"), resultado.getDate("fechaCita"), resultado.getTime("horaCita"), resultado.getString("tratamiento"),
                resultado.getString("descripCondiActual"), resultado.getInt("codigoPaciente"), resultado.getInt("numeroColegiado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCitas = FXCollections.observableArrayList(lista);
    }
    
    public void cargarDatos(){
        tblCitas.setItems(getCita());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Citas, Integer>("codigoCita"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Citas, Date>("fechaCita"));
        colHora.setCellValueFactory(new PropertyValueFactory<Citas, Time>("horaCita"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<Citas, String>("tratamiento"));
        colCondicion.setCellValueFactory(new PropertyValueFactory<Citas, String>("descripCondiActual") );
        colPaciente.setCellValueFactory(new PropertyValueFactory<Citas, Integer>("codigoPaciente"));
        colColegiado.setCellValueFactory(new PropertyValueFactory<Citas, Integer>("numeroColegiado"));
    }
    
    public ObservableList<Paciente> getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"), resultado.getString("nombrePaciente"),
                resultado.getString("apellidosPacientes"), resultado.getString("sexo"), resultado.getDate("fechaNacimiento"),
                resultado.getString("direccionPaciente"), resultado.getString("telefonoPersonal"), resultado.getDate("fechaPrimeraVisita")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPaciente = FXCollections.observableArrayList(lista);
    }
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPacientes(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente(registro.getInt("codigoPaciente"), registro.getString("nombrePaciente"),
                registro.getString("apellidosPacientes"), registro.getString("sexo"), registro.getDate("fechaNacimiento"),
                registro.getString("direccionPaciente"), registro.getString("telefonoPersonal"), registro.getDate("fechaPrimeraVisita"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;  
    }
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"), resultado.getString("nombresDoctor"),
                resultado.getString("apellidosDoctor"), resultado.getString("telefonoContacto"), resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctores = FXCollections.observableArrayList(lista);
    }
    
    public Doctor buscarDoctor(int numeroColegiado){
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctores(?)}");
            procedimiento.setInt(1, numeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Doctor(registro.getInt("numeroColegiado"), registro.getString("nombresDoctor"),
                registro.getString("apellidosDoctor"), registro.getString("telefonoContacto"), registro.getInt("codigoEspecialidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void seleccionarElemento(){
        try{
            txtCodigo.setText(String.valueOf(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));
            fCitas.selectedDateProperty().set(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
            txtHora.setText(String.valueOf(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()));
            txtTratamiento.setText(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento());
            txtCondicion.setText(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getDescripCondiActual());
            cmbPaciente.getSelectionModel().select(buscarPaciente(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
            cmbColegiado.getSelectionModel().select(buscarDoctor(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "sin elementos");
        }
    }
    
    public void guardar(){
        Citas registro =  new Citas();
        registro.setFechaCita(fCitas.getSelectedDate());
        registro.setHoraCita(java.sql.Time.valueOf(txtHora.getText()));
        registro.setTratamiento(txtTratamiento.getText());
        registro.setDescripCondiActual(txtCondicion.getText());
        registro.setCodigoPaciente(((Paciente)cmbPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setNumeroColegiado(((Doctor)cmbColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCitas(?, ?, ?, ?, ?, ?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(2, registro.getHoraCita());
            procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDescripCondiActual());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            procedimiento.execute();
            listaCitas.add(registro);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
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
                if(fCitas.getSelectedDate() == null || txtHora.getText().isEmpty() || txtTratamiento.getText().isEmpty() ||
                        txtCondicion.getText().isEmpty() || cmbPaciente.getValue() == null || cmbColegiado.getValue() == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("org/josevasquez/images/Icon.png"));
                    alert.setContentText("Rebise que haya ingresado datos en todos los campos excepto el Codigo, este dato lo ingresa automaticamente el sistema");
                    alert.show();
                } else{
                    desactivarControles();
                    cargarDatos();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("org/josevasquez/images/AgregarG.png"));
                    imgEliminar.setImage(new Image("org/josevasquez/images/EliminarG.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    guardar();
                    limpiarControles();
                }
                break;
                
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                cargarDatos();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("org/josevasquez/images/AgregarG.png"));
                imgEliminar.setImage(new Image("org/josevasquez/images/EliminarG.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default: 
                if(tblCitas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este elemento?", "Eliminar Elemento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCitas(?)}");
                            procedimiento.setInt(1, ((Citas)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                            procedimiento.execute();
                            listaCitas.remove(tblCitas.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "DebeSeleccionar un elemento");
                }
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCitas(?, ?, ?, ?, ?)}");
            Citas registro = (Citas)tblCitas.getSelectionModel().getSelectedItem();
            registro.setFechaCita(fCitas.getSelectedDate());
            registro.setHoraCita(java.sql.Time.valueOf(txtHora.getText()));
            registro.setTratamiento(txtTratamiento.getText());
            registro.setDescripCondiActual(txtCondicion.getText());
            procedimiento.setInt(1, ((Citas)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(3, registro.getHoraCita());
            procedimiento.setString(4, registro.getTratamiento());
            procedimiento.setString(5, registro.getDescripCondiActual());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCitas.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("org/josevasquez/images/Actualizar.png"));
                    imgReporte.setImage(new Image("org/josevasquez/images/Cancelar.png"));
                    cmbPaciente.setDisable(true);
                    cmbColegiado.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("org/josevasquez/images/EditarG.png"));
                imgReporte.setImage(new Image("org/josevasquez/images/ReporteG.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                actualizar();
                cargarDatos();
                limpiarControles();
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
                limpiarControles();
                desactivarControles();
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
        parametros.put("codigoCitas", null);
        GenerarReporte.mostrarReporte("ReporteCitas.jasper", "Reporte Citas", parametros);
    }
    
    
    
    
    
    public void activarControles(){
        txtHora.setEditable(true);
        txtTratamiento.setEditable(true);
        txtCondicion.setEditable(true);
        cmbPaciente.setDisable(false);
        cmbColegiado.setDisable(false);
        fCitas.setDisable(false);
    }
    
    public void desactivarControles(){
        txtHora.setEditable(false);
        txtTratamiento.setEditable(false);
        txtCondicion.setEditable(false);
        cmbPaciente.setDisable(true);
        cmbColegiado.setDisable(true);
        fCitas.setDisable(true);
    } 
    
    public void limpiarControles(){
        txtCodigo.clear();
        txtHora.clear();
        txtTratamiento.clear();
        txtCondicion.clear();
        cmbPaciente.getSelectionModel().clearSelection();
        cmbPaciente.setValue(null);
        cmbColegiado.getSelectionModel().clearSelection();
        cmbColegiado.setValue(null);
        fCitas.setSelectedDate(null);
        tblCitas.getSelectionModel().clearSelection();
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
