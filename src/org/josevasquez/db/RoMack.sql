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
call sp_AgregarPacientes(102, 'Octavio', 'Corzo', 'm', '1985-06-23', 'zona 2', '54731078', now());
call sp_AgregarPacientes(103, 'Gerardo', 'Lopez', 'm', '2000-04-10', 'zona 3', '74341094', now());
call sp_AgregarPacientes(104, 'Alejandro', 'Lucas', 'm', '2010-06-27', 'zona 4', '87730094', now());
call sp_AgregarPacientes(105, 'Alexander', 'Lopez', 'm', '2003-11-01', 'zona 5', '59931014', now());
call sp_AgregarPacientes(106, 'Kimberly', 'Reyes', 'f', '2001-08-12', 'zona 12', '20731824', now());
call sp_AgregarPacientes(107, 'Cristian', 'Rodrguez', 'm', '2010-02-17', 'zona 4', '39551091', now());
call sp_AgregarPacientes(108, 'Dulce', 'Ramirez', 'f', '1888-12-24', 'zona 18', '86231091', now());
call sp_AgregarPacientes(109, 'Pablo', 'Ventura', 'm', '1992-03-07', 'zona 13', '96200093', now());
call sp_AgregarPacientes(110, 'Emanuel', 'Perez', 'm', '1989-02-28', 'zona 12', '54799094', now());

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

call sp_AgregarEspecialidades('Ortodoxia');
call sp_AgregarEspecialidades('Odontólogo general');
call sp_AgregarEspecialidades('Odontopediatra');
call sp_AgregarEspecialidades('Ortodoncista');
call sp_AgregarEspecialidades('Periodoncista');
call sp_AgregarEspecialidades('Endodoncista');
call sp_AgregarEspecialidades('Patólogo oral');
call sp_AgregarEspecialidades('Prostodoncista');
call sp_AgregarEspecialidades('Endodoncia');
call sp_AgregarEspecialidades('Patología maxilofacial');


-- LISTAR ESPECIALIDADES	

Delimiter $$
	create procedure sp_ListarEspecialidades()
		Begin
			select codigoEspecialidad, descripcion from Especialidades;
        end$$
Delimiter ;

call sp_ListarEspecialidades;

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
		delete from Especialidades where codigoEspecialidad  = codEsp;
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

call sp_AgregarMedicamentos('Paracetamol comprimido revestido de 500 mg');
call sp_AgregarMedicamentos('Benzocaína en gel 200 mg/g');
call sp_AgregarMedicamentos('Lidocaína pomada 50 mg/g');
call sp_AgregarMedicamentos('Paracetamol comprimido revestido de 750 mg');
call sp_AgregarMedicamentos('Paracetamol gotas');
call sp_AgregarMedicamentos('Dipirona comprimido de 500 mg');
call sp_AgregarMedicamentos('Dipirona solución oral 500 mg/ml');
call sp_AgregarMedicamentos('Ibuprofeno');
call sp_AgregarMedicamentos('Naproxeno comprimidos revestidos de 250 mg');
call sp_AgregarMedicamentos('Ácido acetilsalicílico');


-- LISTAR

Delimiter $$
	create procedure sp_ListarMedicamentos()
		Begin
			select codigoMedicamento, nombreMedicamento from Medicamentos;
        end$$
Delimiter ;

call sp_ListarMedicamentos;

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

-- call sp_EditarMedicamentos(1, 'no');

-- DOCTORES 

-- AGREGAR

Delimiter $$
	create procedure sp_AgregarDoctores(in numeroColegiado int, in nombresDoctor varchar(50), in apellidosDoctor varchar(50),
    in telefonoContacto varchar(8), in codigoEspecialidad int)
		Begin
			insert into Doctores(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad)
				values(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad);
		End$$
Delimiter ;

call sp_AgregarDoctores(12, 'Edgar', 'Velasquez', '12345678', 1);
call sp_AgregarDoctores(13, 'Pablo', 'Reyez', '12345678', 1);
call sp_AgregarDoctores(14, 'Josue', 'Quevedo', '12345678', 1);
call sp_AgregarDoctores(15, 'Diego', 'Villeda', '12345678', 1);
call sp_AgregarDoctores(16, 'Selvin', 'Monterroso', '12345678', 1);
call sp_AgregarDoctores(17, 'Mario', 'Sanchez', '12345678', 1);
call sp_AgregarDoctores(18, 'Fernanda', 'Pineda', '12345678', 1);
call sp_AgregarDoctores(19, 'Dayana', 'Rodriguez', '12345678', 1);
call sp_AgregarDoctores(20, 'Pedro', 'Perez', '12345678', 1);
call sp_AgregarDoctores(21, 'Juan', 'Saldaña', '12345678', 1);


-- LISTAR

Delimiter $$
	create procedure sp_ListarDoctores()
		Begin
			select numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad from Doctores;
        end$$
Delimiter ;

call sp_ListarDoctores;
-- BUSCAR

Delimiter $$
	create procedure sp_BuscarDoctores(in codDoc int) 
		Begin
			select numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad from Doctores where numeroColegiado = codDoc;
		end$$
Delimiter ;


call sp_BuscarDoctores(12);

-- ELIMINAR

Delimiter $$
	create procedure sp_EliminarDoctores(in codDoc int) 
		Begin
			delete from Doctores where numeroColegiado = codDoc;
        end$$
Delimiter ;

-- EDITAR	

Delimiter $$
	create procedure sp_EditarDoctores (in codDoc int, in nombresDoc varchar(50), in apellidosDoc varchar(50),
		in telefono varchar(8))
		Begin
			update Doctores D
				set 
					D.nombresDoctor = nombresDoc,
                    D.apellidosDoctor = apellidosDoc,
                    D.telefonoContacto = telefono
					where D.numeroColegiado = codDoc;
        end$$
Delimiter ;

-- drop procedure sp_EditarDoctores;



-- RECETAS

-- AGREGAR

Delimiter $$
	create procedure sp_AgregarRecetas(in fechaReceta date, in numeroColegiado int)
		Begin
			insert into Recetas(fechaReceta, numeroColegiado)
				values(fechaReceta, numerocolegiado);
		End$$
Delimiter ;

call sp_AgregarRecetas('2022-04-22', 12);
call sp_AgregarRecetas('2022-04-28', 13);
call sp_AgregarRecetas('2022-04-30', 12);
call sp_AgregarRecetas('2022-05-07', 12);
call sp_AgregarRecetas('2022-05-07', 14);
call sp_AgregarRecetas('2022-05-08', 12);
call sp_AgregarRecetas('2022-05-15', 15);
call sp_AgregarRecetas('2022-05-27', 19);
call sp_AgregarRecetas('2022-06-10', 17);
call sp_AgregarRecetas('2022-06-15', 20);


-- LISTAR

Delimiter $$
	create procedure sp_ListarRecetas()
		Begin
			select codigoReceta, fechaReceta, numeroColegiado from Recetas;
        end$$
Delimiter ;

call sp_ListarRecetas;

-- BUSCAR

Delimiter $$
	create procedure sp_BuscarRecetas(in codRece int) 
		Begin
			select codigoReceta, fechaReceta, numeroColegiado
				from Recetas where codigoReceta = codRece;
		end$$
Delimiter ;

call sp_BuscarRecetas(2);

-- ELIMINAR

Delimiter $$
	create procedure sp_EliminarRecetas(in codRece int) 
		Begin
			delete from Recetas where codigoReceta = codRece;
        end$$
Delimiter ;

-- EDITAR

Delimiter $$
	create procedure sp_EditarRecetas (in codRece int, in fechaRece date)
		Begin
			update Recetas R
				set R.fechaReceta = fechaRece
					where codigoReceta = codRece;
        end$$
Delimiter ;

-- drop procedure sp_EditarRecetas;

-- AGREGAR

Delimiter $$
	create procedure sp_AgregarCitas(in fechaCita date, in horaCita time, in tratamiento varchar(150), in descripCondiActual varchar(255),
		in codigoPaciente int, in numeroColegiado int)
		Begin
			insert into Citas(fechaCita, horaCita, tratamiento, descripCondiActual, codigoPaciente, numeroColegiado)
				values(fechacita, horaCita, tratamiento, descripCondiActual, codigoPaciente, numeroColegiado);
		End$$
Delimiter ;

call sp_AgregarCitas('2022-04-22', '09:00:00', 'Limpieza', 'Bien', 101, 12);
call sp_AgregarCitas('2022-04-28', '09:00:00', 'Implantes', 'Bien', 102, 14);
call sp_AgregarCitas('2022-04-30', '10:00:00', 'Caries', 'Mal', 103, 12);
call sp_AgregarCitas('2022-05-07', '10:30:00', 'Estética Dental', 'Bien', 104, 19);
call sp_AgregarCitas('2022-05-07', '12:30:00', 'Limpieza', 'Mal', 105, 21);
call sp_AgregarCitas('2022-05-08', '14:00:00', 'Caries', 'Mal', 106, 20);
call sp_AgregarCitas('2022-05-15', '14:00:00', 'Limpieza', 'Mal', 107, 12);
call sp_AgregarCitas('2022-05-27', '15:00:00', 'Estética Dental', 'Bien', 108, '15');
call sp_AgregarCitas('2022-06-10', '16:00:00', 'Limpieza', 'Mal', 109, 17);
call sp_AgregarCitas('2022-06-15', '16:00:00', 'Prótesis', 'Bien', 110, 19);

-- LISTAR

Delimiter $$
	create procedure sp_ListarCitas()
		Begin
			select codigoCita, fechaCita, horaCita, tratamiento, descripCondiActual, codigoPaciente, numeroColegiado
				from Citas;
        end$$
Delimiter ;

call sp_ListarCitas;

-- drop procedure sp_ListarCitas;

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
	create procedure sp_EditarCitas (in codCita int, in fecha date, in hora time, in trata varchar(150), in condiActual varchar(250))
		Begin
			update Citas C
				set C.fechaCita = fecha,
                    C.horaCita = hora,
                    C.tratamiento = trata,
                    C.descripCondiActual = condiActual
					where codigoCita = codCita;
        end$$
Delimiter ;

-- drop procedure sp_EditarCitas;

-- AGREGAR

Delimiter $$
	create procedure sp_AgregarDetalleReceta(in dosis varchar(100), in codigoReceta int, in codigoMedicamento int)
		Begin
			insert into DetalleReceta(dosis, codigoReceta, codigoMedicamento)
				values(dosis, codigoReceta, codigoMedicamento);
		End$$
Delimiter ;

call sp_AgregarDetalleReceta('1 cada 8 horas', 1, 1);
call sp_AgregarDetalleReceta('2 cada 12 horas', 1, 4);
call sp_AgregarDetalleReceta('1 cada 4 horas', 1, 6);
call sp_AgregarDetalleReceta('3 cada 12 horas', 2, 1);
call sp_AgregarDetalleReceta('1/2 cada 8 horas', 2, 7);
call sp_AgregarDetalleReceta('1 cada 8 horas', 3, 9);
call sp_AgregarDetalleReceta('1 cada 6 horas', 4, 10);
call sp_AgregarDetalleReceta('1/2 cada 8 horas', 5, 1);
call sp_AgregarDetalleReceta('2 cada 6 horas', 6, 1);
call sp_AgregarDetalleReceta('1 cada 12 horas', 7, 2);


-- LISTAR

Delimiter $$
	create procedure sp_ListarDetalleReceta()
		Begin
			select codigoDetalleReceta, dosis, codigoReceta, codigoMedicamento from DetalleReceta;
        end$$
Delimiter ;


call sp_ListarDetalleReceta;

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
	create procedure sp_EditarDetalleReceta (in codDeta int, in dosi varchar(100))
		Begin
			update DetalleReceta D
				set D.dosis = dosi
					where codigoDetalleReceta = codDeta;
        end$$
Delimiter ;

-- drop procedure sp_EditarDetalleReceta;

-- call sp_EditarDetalleReceta(2, 'dgsdgfsd', 1, 1);

call sp_ListarDetalleReceta;

create table Usuario (
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario(codigoUsuario)
);


Delimiter $$
create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100), in usuarioLogin varchar(50), in contrasena varchar(50))
	Begin
		insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
			values(nombreUsuario, apellidoUsuario,  usuarioLogin, contrasena);
    End$$
Delimiter ;

Delimiter $$
create procedure sp_ListarUsuarios()
	Begin
		select 
			U.codigoUsuario,
			U.nombreUsuario,
			U.apellidoUsuario,
			U.usuarioLogin,
			U.contrasena 
			from Usuario U;
	End$$
Delimiter ;


call sp_AgregarUsuario('Marcos','Roman','MRoman','@123');
call sp_AgregarUsuario('Gerado','Sandoval','Gsando','@122');
call sp_AgregarUsuario('Alexander','Lopez','Lale','ale123');
call sp_AgregarUsuario('Octavio','Corzo','0corzo','Octacorzo');
call sp_AgregarUsuario('Alejandro','Reyes','ReyesAle','Kingale');
call sp_AgregarUsuario('Pablo','Vasquez','MRoman','elPabloxd');
call sp_AgregarUsuario('Juan','Roman','JRoman','eladmin');
call sp_AgregarUsuario('Fernanda','Rodriguez','FRodri','@123');
call sp_AgregarUsuario('Jose','Villeda','ville','@12vile');
call sp_AgregarUsuario('Selvin','Perez','selvinPe','@perez');
call sp_ListarUsuarios;

create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster(usuarioMaster)
)
