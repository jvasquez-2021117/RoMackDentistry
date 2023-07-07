Drop database if exists DBRoMack2021117;
create database DBRoMack2021117;
use DBRoMack2021117;

create table Pacientes(
	codigoPaciente int not null,
    nombrePaciente varchar(50) not null,
    apellidosPacientes varchar(50) not null,
    sexo char not null,
    fechaNacimiento date not null,
    direccionPaciente varchar(100) not null,
    telefonoPersonal varchar(8) not null,
    fechaPrimeraVisita date,
    primary key PK_codigoPaciente (codigoPaciente)
);

create table Especialidades (
	codigoEspecialidad int not null auto_increment,
    descripcion varchar(100) not null,
    primary key PK_codigoEspecilidad (codigoEspecialidad)
);

create table Medicamentos (
	codigoMedicamento int not null auto_increment,
    nombreMedicamento varchar(100) not null,
    primary key PK_codigoMedicamento (codigoMedicamento)
);

create table Doctores (
	numeroColegiado int not null,
    nombresDoctor varchar(50) not null,
    apellidosDoctor varchar(50) not null,
    telefonoContacto varchar(8) not null,
    codigoEspecialidad int not null,
    primary key PK_numeroColegiado (numeroColegiado),
    constraint FK_Doctores_Especialidades foreign key (codigoEspecialidad)
		references Especialidades (codigoEspecialidad)
);

Create table Recetas (
	codigoReceta int not null auto_increment,
    fechaReceta date not null,
    numeroColegiado int not null,
    primary key PK_codigoReceta (codigoReceta),
    constraint FK_Recetas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

create table Citas (
	codigoCita int not null auto_increment,
    fechaCita date not null,
    horaCita time not null,
    tratamiento varchar(150),
    descripCondiActual varchar(255) not null,
    codigoPaciente int not null,
    numeroColegiado int not null,
    primary key PK_codigoCita (codigoCita),
    constraint FK_Citas_Pacientes foreign key (codigoPaciente)
		references Pacientes (codigoPaciente),
	constraint FK_Citas_Doctortes foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

create table DetalleReceta (
	codigoDetalleReceta int not null auto_increment,
    dosis varchar(100) not null,
    codigoReceta int not null,
    codigoMedicamento int not null,
    primary key PK_codigoDetalleReceta (codigoDetalleReceta),
    constraint FK_DetalleReceta_Receta foreign key (codigoReceta)
		references Recetas (codigoReceta),
	constraint FK_DetalleReceta_Medicamentos foreign key (codigoMedicamento)
		references Medicamentos (codigoMedicamento)
);

-- ------------------------------------------------------------------------------------------------------------------

-- ---------------PROCEDIMIENTOS ALMACENADOS --------------------------------------------------------------
-- ---------------PACIENTES------------------------------------------------------------------------------------------

-- AGREGAR -----

DELIMITER $$
	create procedure sp_AgregarPacientes (in codigoPaciente int, in nombrePaciente varchar(50), in apellidosPacientes varchar(50), in sexo char,
		in fechaNacimiento date, in direccionPaciente varchar(100), in telefonoPersonal varchar(8), in fechaPrimeraVisita date)
        Begin
			insert into Pacientes (codigoPaciente, nombrePaciente, apellidosPacientes, sexo, fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita)
				values(codigoPaciente, nombrePaciente, apellidosPacientes, upper(sexo), fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita);
        end$$
DELIMITER ;

call sp_AgregarPacientes(101, 'Fernando', 'Lopez', 'm', '1996-03-17', 'zona 1', '54731094', now());

-- LISTAR ------

Delimiter $$
	create procedure sp_ListarPacientes ()
	Begin
    select
		P.codigoPaciente,
        P.nombrePaciente,
        P.apellidosPacientes,
        P.sexo,
        P.fechaNacimiento,
        P.direccionPaciente,
        P.telefonoPersonal,
        P.fechaPrimeraVisita
	From Pacientes P;
    end$$
Delimiter ;

call sp_ListarPacientes();

-- BUSCAR ------

Delimiter $$
	create procedure sp_BuscarPacientes (in codPaciente int)
	Begin
    select
		P.codigoPaciente,
        P.nombrePaciente,
        P.apellidosPacientes,
        P.sexo,
        P.fechaNacimiento,
        P.direccionPaciente,
        P.telefonoPersonal,
        P.fechaPrimeraVisita
	From Pacientes P
		where codigoPaciente = codPaciente;
    end$$
Delimiter ;

call sp_BuscarPacientes(101);

-- ELMINAR -----

Delimiter $$
	create procedure sp_EliminarPacientes (in codPaciente int)
	Begin
		delete from Pacientes
			where codigoPaciente = codPaciente;
    end$$
Delimiter ;

-- call sp_EliminarPacientes(0);

-- EDITAR ------

Delimiter $$
	create procedure sp_EditarPacientes(in codPaciente int, in nomPaciente varchar(50), in apePaciente varchar(50), in sex char, in fechaNaci date,
		in direcPaciente varchar(100), in telPaciente varchar(8), in fechaPV date)
        Begin
			update Pacientes P
				set
					P.nombrePaciente = nomPaciente,
                    P.apellidosPacientes = apePaciente,
                    P.sexo = sex,
                    P.fechaNacimiento = fechaNaci,
                    P.direccionPaciente = direcPaciente,
                    P.telefonoPersonal = telPaciente,
                    P.fechaPrimeraVisita = fechaPV
                    where codigoPaciente = codPaciente;
        End$$
Delimiter ;

-- drop procedure sp_EditarPacientes
-- call sp_EditarPacientes(101, 'fer', 'Gonzales', 'f', '2000-09-24', 'zona 5', '12345678', now());

-- ESPECIALIDADES

-- AGREGAR ESPECIALIDADES

Delimiter $$
	create procedure sp_AgregarEspecialidades (in descri varchar(100))
		Begin
			insert into Especialidades(descripcion)
				values(descri);
        end$$
Delimiter ;

-- LISTAR ESPECIALIDADES	

Delimiter $$
	create procedure sp_ListarEspecialidades()
		Begin
			select codigoEspecialidad, descripcion from Especialidades;
        end$$
Delimiter ;

-- BUSCAR ESPECIALIDADES

Delimiter $$
	create procedure sp_BuscarEspecialidad (in codEsp int) 
		Begin
			select codigoEspecialidad, descripcion from Especialidades where codigoEspecialidad = CodEsp;
        end$$
Delimiter ;

-- ELIMINAR ESPECIALIDADES	

Delimiter $$
	create procedure sp_EliminarEspecialidades(in codEsp int)
    Begin
		delete from Especialidades where codigoEspecialidad  = cpdEsp;
    End$$
Delimiter ;

-- EDITAR ESPECIALIDADES

Delimiter $$
	create procedure sp_EditarEspecialidades (in codEsp int, in descri varchar(100))
		Begin
			update Especialidades E
				set E.descripcion = descri
					where codigoEspecialidad = codEsp;
        End$$
Delimiter ;

-- MEDICAMENTOS

-- AGREGAR

Delimiter $$
	create procedure sp_AgregarMedicamentos(in nombre varchar(100))
		Begin
			insert into Medicamentos(nombreMedicamento)
				values(nombre);
		End$$
Delimiter ;

-- LISTAR

Delimiter $$
	create procedure sp_ListarMedicamentos()
		Begin
			select codigoMedicamento, nombreMedicamento from Medicamentos;
        end$$
Delimiter ;

-- BUSCAR

Delimiter $$
	create procedure sp_BuscarMedicamentos(in codMedi int) 
		Begin
			select codigoMedicamento, nombreMedicamento from Medicamentos where codigoMedicamento = codMedi;
		end$$
Delimiter ;

-- ELIMINAR

Delimiter $$
	create procedure sp_EliminarMedicamentos(in codMedi int) 
		Begin
			delete from Medicamentos where codigoMedicamento = codMedi;
        end$$
Delimiter ;

-- EDITAR

Delimiter $$
	create procedure sp_EditarMedicamentos (in codMedi int, in nombre varchar(100))
		Begin
			update Medicamentos M
				set M.nombreMedicamento = nombre
					where codigoMedicamento = codMedi;
        end$$
Delimiter ;

-- DOCTORES 

-- AGREGAR

Delimiter $$
	create procedure sp_AgregarDoctores(in numeroColegiado int, in nombresDoctor varchar(50), in apellidosDoctor varchar(50),
    in telefonoContacto varchar(8), in codigoEspecialidad int)
		Begin
			insert into Medicamentos(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad)
				values(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad);
		End$$
Delimiter ;

-- LISTAR

Delimiter $$
	create procedure sp_ListarDoctores()
		Begin
			select numeroColegiado, nombresDoctor, apellidosDoctores, telefonoContacto, descripcion from Medicamentos M
				inner join Especialidades E on M.codigoEspecialidad = E.codigoEspecialidad;
        end$$
Delimiter ;


-- BUSCAR

Delimiter $$
	create procedure sp_BuscarDoctores(in codDoc int) 
		Begin
			select numeroColegiado, nombresDoctor, apellidosDoctores, telefonoContacto, descripcion from Medicamentos M
				inner join Especialidades E on M.codigoEspecialidad = E.codigoEspecialidad where numeroColegiado = codDoc;
		end$$
Delimiter ;

-- ELIMINAR

Delimiter $$
	create procedure sp_EliminarDoctores(in codDoc int) 
		Begin
			delete from Doctores where numeroColegiado = codDoc;
        end$$
Delimiter ;

-- EDITAR	

Delimiter $$
	create procedure sp_EditarDoctores (in codDoc int, in numeroCole int, in nombresDoc varchar(50), in apellidosDoc varchar(50),
		in telefono varchar(8), in codEspecialidad int)
		Begin
			update Doctores D
				set D.numeroColegiado = numeroCole,
					D.nombresDocotor = nombresDoc,
                    D.apellidosDoctor = apellidosDoc,
                    D.telefenocontacto = telefono,
                    D.codigoEspecialidad = codEspecialidad
					where cnumeroColegiado = codDoc;
        end$$
Delimiter ;

-- RECETAS

-- AGREGAR

Delimiter $$
	create procedure sp_AgregarRecetas(in fechaReceta date, in numeroColegiado int)
		Begin
			insert into Recetas(fechaReceta, numeroColegiado)
				values(fechaReceta, numerocolegiado);
		End$$
Delimiter ;

-- LISTAR

Delimiter $$
	create procedure sp_ListarRecetas()
		Begin
			select codigoReceta, fechaReceta, numeroColegiado from Recetas;
        end$$
Delimiter ;

-- BUSCAR

Delimiter $$
	create procedure sp_BuscarRecetas(in codRece int) 
		Begin
			select codigoReceta, fechaReceta, nombresDoctor, apellidosDoctor 
				from Recetas R inner join Doctores D on R.numeroColegiado = D.numeroColegiado
					where codigoReceta = codRece;
		end$$
Delimiter ;

-- ELIMINAR

Delimiter $$
	create procedure sp_EliminarRecetas(in codRece int) 
		Begin
			delete from Recetas where codigoReceta = codRece;
        end$$
Delimiter ;

-- EDITAR

Delimiter $$
	create procedure sp_EditarRecetas (in codRece int, in fechaRece date, in numeroCole int)
		Begin
			update Recetas R
				set R.fechaReceta = fechaRece,
					R.numeroColegiado = numeroCole
					where codigoReceta = codRece;
        end$$
Delimiter ;

-- AGREGAR

Delimiter $$
	create procedure sp_AgregarCitas(in fechaCita date, in horaCita time, in tratamiento varchar(150), in descripCondiActual varchar(255),
		in codigoPaciente int, in numeroColegiado int)
		Begin
			insert into Citas(fechaCita, horaCita, tratamiento, descripCondiActual, codigoPaciente, numeroColegiado)
				values(fechacita, horaCita, tratamiento, descripCondiActual, codigoPaciente, numeroColegiado);
		End$$
Delimiter ;

-- LISTAR

Delimiter $$
	create procedure sp_ListarCitas()
		Begin
			select codigoCita, fechaCita, horaCita, tratamiento, descripCondiActual, nombrePaciente, apellidosPaciente, nombresDoctor, apellidosDoctor
				from Citas C inner join Paciente P on C.codigoPaciente = P.codigoPaciente
					inner join Doctores D on C.numeroColegiado = D.numeroColegiado;
        end$$
Delimiter ;

-- BUSCAR

Delimiter $$
	create procedure sp_BuscarCitas(in codCita int) 
		Begin
			select codigoCita, fechaCita, horaCita, tratamiento, descripCondiActual, nombrePaciente, apellidosPaciente, nombresDoctor, apellidosDoctor
				from Citas C inner join Paciente P on C.codigoPaciente = P.codigoPaciente
					inner join Doctores D on C.numeroColegiado = D.numeroColegiado where codigoCita = codCita;
		end$$
Delimiter ;

-- ELIMINAR

Delimiter $$
	create procedure sp_EliminarCitas(in codCita int) 
		Begin
			delete from Citas where codigoCita = codCita;
        end$$
Delimiter ;

-- EDITAR

Delimiter $$
	create procedure sp_EditarCitas (in codCita int, in fecha date, in hora time, in trata varchar(150), in condiActual varchar(250),
		in codPaciente int, numColegiado int)
		Begin
			update Citas C
				set C.fechaCita = fecha,
                    C.horaCita = hora,
                    C.tratamiento = trata,
                    C.descripCondiActual = condiActual,
                    C.codigoPaciente = codPaciente,
                    C.numeroColegiado = numColegiado
					where codigoCita = codCita;
        end$$
Delimiter ;

-- AGREGAR

Delimiter $$
	create procedure sp_AgregarDetalleReceta(in dosis varchar(100), in codigoReceta int, in codigoMedicamento int)
		Begin
			insert into DetalleReceta(dosis, codigoReceta, codigoMedicamento)
				values(dosis, codigoReceta, codigoMedicamento);
		End$$
Delimiter ;

-- LISTAR

Delimiter $$
	create procedure sp_ListarDetalleReceta()
		Begin
			select codigoDetalleReceta, dosis, codigoReceta, codigoMedicamento from DetalleReceta D inner join Recetas R on D.codigoReceta = R.codigoReceta
				inner join Medicamentos M on D.codigoMedicamento = M.codigoMedicamento;
        end$$
Delimiter ;

-- BUSCAR 

Delimiter $$
	create procedure sp_BuscarDetalleReceta(in codDeta int) 
		Begin
			select codigoDetalleReceta, dosis, codigoReceta, codigoMedicamento from DetalleReceta D inner join Recetas R on D.codigoReceta = R.codigoReceta
				inner join Medicamentos M on D.codigoMedicamento = M.codigoMedicamento where codigoDetalleReceta = codDeta;
		end$$
Delimiter ;

-- ELIMINAR

Delimiter $$
	create procedure sp_EliminarDetalleReceta(in codDeta int) 
		Begin
			delete from DetalleReceta where codigoDetalleReceta = codDeta;
        end$$
Delimiter ;

-- EDITAR

Delimiter $$
	create procedure sp_EditarDetalleReceta (in codDeta int, in dosi varchar(100), in codReceta int, in codMedicamento int)
		Begin
			update DetalleReceta D
				set D.dosis = dosi,
					D.codigoReceta = codReceta,
                    D.codigoMedicamento = codMedicamento
					where codigoReceta = codRece;
        end$$
Delimiter ;
