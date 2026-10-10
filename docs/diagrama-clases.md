# Diagrama de clases - Epicentro Gourmet (borrador)

Se renderiza directo en GitHub (Mermaid).

## 1. Dominio (entidades persistentes)

```mermaid
classDiagram
    direction TB

    class Festival {
        -int id
        -String nombre
        -Temporada temporada
        -LocalDate fechaInicio
        -LocalDate fechaFin
        +getDuracionDias() int
        +estaActivo(LocalDate) boolean
    }

    class CostoFestival {
        -int id
        -TipoCosto tipoCosto
        -BigDecimal monto
        -TipoUnidad aplicaA  «null = todas»
    }

    class UnidadVenta {
        <<abstract>>
        -int id
        -String nombreComercial
        -double superficieM2
        -String codigo  «10 caracteres»
        +calcularCanon(CostoFestival) BigDecimal*
        +validarCodigo() boolean
    }
    class FoodTruck {
        -String patente
        -boolean requiereElectricidad
        +calcularCanon() BigDecimal
    }
    class PuestoDesarmable {
        -int cantidadCarpas
        -int minutosMontaje
        +calcularCanon() BigDecimal
    }

    class Plato {
        -int id
        -String nombre
        -BigDecimal precioVenta
        -BigDecimal costoProduccion
        -boolean eliminado
    }

    class Pedido {
        -int id
        -LocalDate fecha
        +getTotal() BigDecimal
        +getCostoPlatos() BigDecimal
    }
    class ItemPedido {
        -int id
        -int cantidad
        +getSubtotal() BigDecimal
    }

    class Personal {
        <<abstract>>
        -int id
        -String nombre
        -String apellido
        -String dni
        -LocalDate fechaNacimiento
        -LocalDate fechaIngreso
        -String email
        -boolean eliminado
        +getAntiguedadAnios() int
        +esMayorDeEdad() boolean
    }
    class EmpleadoCocina {
        -RolCocina rol
        -String tarea
    }
    class Cajero {
        -Turno turno
    }
    class Responsable {
    }

    class CierreCaja {
        -int id
        -LocalDate fecha
        -BigDecimal totalRecaudado
    }

    class User {
        -int id
        -String username
        -String password
        -boolean enabled
    }
    class UserRole {
        -int id
        -String role
    }

    class Temporada { <<enumeration>> VERANO OTONIO INVIERNO PRIMAVERA }
    class TipoCosto { <<enumeration>> SUPERFICIE MONTAJE ELECTRICIDAD SUELDO_BASE ANTIGUEDAD PLUS_COCINA }
    class TipoUnidad { <<enumeration>> FOOD_TRUCK PUESTO_DESARMABLE }
    class RolCocina { <<enumeration>> COCINERO AYUDANTE LAVAPLATOS }
    class Turno { <<enumeration>> MANIANA NOCHE }

    UnidadVenta <|-- FoodTruck
    UnidadVenta <|-- PuestoDesarmable
    Personal <|-- EmpleadoCocina
    Personal <|-- Cajero
    Personal <|-- Responsable

    Festival "1" *-- "0..*" CostoFestival : define
    Festival "1" o-- "0..*" UnidadVenta : habilita
    UnidadVenta "1" *-- "0..*" Plato : ofrece
    UnidadVenta "0..*" o-- "0..*" Personal : staff
    UnidadVenta "1" --> "1" Responsable : a cargo de
    Festival "1" <-- "0..*" Pedido : ocurre en
    UnidadVenta "1" <-- "0..*" Pedido : entrega
    Pedido "1" *-- "1..*" ItemPedido
    ItemPedido "0..*" --> "1" Plato
    Cajero "1" --> "0..*" CierreCaja
    CierreCaja "0..*" --> "1" UnidadVenta
    Personal "1" --> "0..1" User : inicia sesión con
    User "1" *-- "1..*" UserRole

    Festival ..> Temporada
    CostoFestival ..> TipoCosto
    CostoFestival ..> TipoUnidad
    EmpleadoCocina ..> RolCocina
    Cajero ..> Turno
```

## 2. DTOs de reporte (no persisten) y servicios

```mermaid
classDiagram
    direction LR

    class ReporteVentaDTO {
        -String festival
        -BigDecimal recaudacionTotal
    }
    class ReporteMayoresCanonDTO {
        -String nombreComercial
        -String codigo
        -TipoUnidad tipoUnidad
        -BigDecimal canon
    }
    class RankingUnidadDTO {
        -String nombreComercial
        -BigDecimal recaudacion
    }
    class PlatoDTO {
        -String nombre
        -int cantidadPedida
    }
    class RentabilidadDTO {
        -BigDecimal ingresos
        -BigDecimal costoPlatos
        -BigDecimal sueldos
        -BigDecimal canon
        -BigDecimal rentabilidadNeta
    }

    class ReporteService {
        +ranking(festivalId) List~RankingUnidadDTO~
        +mayoresCanon(festivalId, x) List~ReporteMayoresCanonDTO~
        +recaudacion(festivalId) ReporteVentaDTO
        +rentabilidadNeta(unidadId, festivalId) RentabilidadDTO
        +rentabilidadEntreFechas(unidadId, festivalId, desde, hasta) RentabilidadDTO
        +platoEstrella(unidadId, festivalId) PlatoDTO
    }
    class LiquidacionService {
        +calcularSueldo(personalId, festivalId) BigDecimal
        +generarReciboPdf(personalId, festivalId) byte[]
    }
    class PersonalService {
        +alta(PersonalDTO)
        +modificar(PersonalDTO)
        +baja(id)  «lógica»
        +reactivar(id)
        +filtrar(criterios) List~Personal~
    }
    class MailService {
        +enviarCredenciales(email, passwordGenerada)
    }
    class UnidadVentaService {
        +registrarConResponsable(RegistroDTO)
        +calcularCanon(unidadId, festivalId) BigDecimal
    }

    ReporteService ..> ReporteVentaDTO
    ReporteService ..> ReporteMayoresCanonDTO
    ReporteService ..> RankingUnidadDTO
    ReporteService ..> PlatoDTO
    ReporteService ..> RentabilidadDTO
    ReporteService ..> UnidadVentaService : usa canon
    ReporteService ..> LiquidacionService : usa sueldos
    PersonalService ..> MailService
```

## Decisiones de diseño

- `Personal` es la raíz de la herencia (JOINED, igual que `Person` en el repo de ejemplo). `Responsable` es un `Personal`, porque el enunciado dice que es "uno de los miembros del Staff".
- Los montos (sueldo base, $500/m², plus de antigüedad, plus de cocina) **no se hardcodean**: viven en `CostoFestival` y se leen desde ahí.
- La baja lógica es el campo `eliminado` en `Personal` y `Plato`.
- Los DTOs de reporte no son entidades: se arman en `ReporteService` a partir de consultas.
