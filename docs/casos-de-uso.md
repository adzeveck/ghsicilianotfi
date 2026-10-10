# Casos de uso - Epicentro Gourmet (borrador)

Actores: **Visitante** (sin sesión), **Administrador** (precargado), **Responsable** (de una unidad de venta), **Empleado** (Cocina o Cajero).

Rama por caso de uso: `CU<Funcionalidad><NombreApellido>`, por ejemplo `CUAltaEmpleadoJuanPerez`.

## Casos de uso grupales (los hace el grupo, son la base del sistema)

| ID | Caso de uso | Actor | Descripción | Requisito grupal |
|----|-------------|-------|-------------|------------------|
| CU01 | Ver página de inicio | Visitante | Muestra los festivales precargados (nombre, temporada, fechas). | |
| CU02 | Login | Todos | Inicio de sesión; redirige según rol. | |
| CU03 | Registrar Responsable con Unidad de Venta | Visitante | Formulario con datos de la unidad (Food Truck o Puesto) y del responsable. Crea el `User` con `ROLE_RESPONSABLE` (usuario y contraseña obligatorios). | |
| CU04 | Alta de empleado | Responsable | Formulario; crea el `Personal`, genera su `User` (usuario = email, contraseña aleatoria) y le envía un mail. Luego lista los empleados activos. | **Envío de mail** |
| CU05 | Alta de plato | Responsable | Crea un plato (nombre, precio de venta, costo de producción) en su unidad. | |
| CU06 | Recibo de sueldo en PDF | Empleado | Muestra el sueldo recibido en un festival y permite descargar el recibo. | **Generación de PDF** |
| CU07 | Reporte de recaudación | Responsable | Dado un festival, devuelve la recaudación total (`ReporteVentaDTO`). | **DTO de reporte** |

## Casos de uso a repartir entre los integrantes

Cada integrante toma **2 CU de esta lista** (distintos de los grupales) y **2 validaciones de carga**.

### Administrador

| ID | Caso de uso | Descripción |
|----|-------------|-------------|
| CU08 | Ver festivales | Listado de festivales con detalle. |
| CU09 | Ver unidades de venta de un festival | Unidades que participan en un festival. |
| CU10 | Ranking de unidades | Para un festival, lista de unidades ordenada de mayor a menor recaudación. |
| CU11 | Unidades con mayor canon | Dado un festival y `x`, devuelve las `x` unidades que más pagaron de canon (`ReporteMayoresCanonDTO`: nombre comercial, código, tipo, canon). |

### Responsable

| ID | Caso de uso | Descripción |
|----|-------------|-------------|
| CU12 | Modificar empleado | Edita los datos de un empleado de su unidad. |
| CU13 | Baja lógica de empleado | Marca `eliminado = true`; no borra el registro. |
| CU14 | Reactivar empleado eliminado | Si se vuelve a dar de alta uno eliminado, se actualiza y reactiva con un método específico. |
| CU15 | Filtrar empleados | Filtros por uno o varios atributos (nombre, rol, turno, antigüedad); solo no eliminados. |
| CU16 | Calcular canon | Food Truck: `m² * $500 + $2000` si usa electricidad. Puesto: `m² * $500 - minutos de montaje * $10`. Los montos salen de `CostoFestival`. |
| CU17 | Liquidación de haberes | Sueldo del personal según tipo, antigüedad y duración del festival (reglas de 15 días del enunciado). |
| CU18 | Rentabilidad neta | Pedidos totales - costo de platos - sueldos - canon, para una unidad en un festival. |
| CU19 | Rentabilidad neta entre dos fechas | Lo mismo que CU18 acotado a un rango de fechas. |
| CU20 | Plato estrella | Dada una unidad y un festival, devuelve el `PlatoDTO` con mayor cantidad pedida. |

### Empleado

| ID | Caso de uso | Descripción |
|----|-------------|-------------|
| CU21 | Ver mis festivales y unidades | Historial de festivales y unidades en las que trabajó. |
| CU22 | Ver mi asignación actual | Si hoy hay un festival activo, en qué unidad trabaja. |
| CU23 | Cerrar caja | Solo Cajero: al finalizar el día registra el total recaudado (`CierreCaja`). |

## Validaciones de carga (cada integrante elige 2)

| ID | Validación |
|----|------------|
| V01 | Festival: la fecha de fin no puede ser anterior a la de inicio. |
| V02 | Pedido: solo puede tener la fecha del día en que se carga. |
| V03 | Personal: debe ser mayor de edad (18 años). |
| V04 | Unidad de venta: código de 10 caracteres con lógica propia (por ejemplo, formato y dígito verificador) y único. |
| V05 | Personal: DNI y email únicos, con formato válido. |
| V06 | Usuario: nombre de usuario único y contraseña con largo mínimo. |
| V07 | Plato: precio de venta mayor que cero y mayor o igual al costo de producción. |
| V08 | Pedido: cantidad mayor que cero y la unidad debe participar en el festival. |
| V09 | Unidad de venta: superficie mayor que cero; Food Truck con patente válida; Puesto con carpas y tiempo de montaje positivos. |
| V10 | Reportes entre fechas: la fecha desde no puede ser posterior a la fecha hasta ni salir del período del festival. |

## Cobertura de requisitos del TP

| Requisito | Cubierto por |
|-----------|--------------|
| Al menos un envío de mail | CU04 |
| Al menos un DTO de reporte | CU07 (y CU11, CU20) |
| Al menos una generación de PDF | CU06 |
| Flujos 1 a 7 del enunciado | CU02, CU03, CU04, CU05, CU07 a CU11, CU22, CU06 |
| 2 CU y 2 validaciones por integrante | Lista de CU08-CU23 y V01-V10 |
