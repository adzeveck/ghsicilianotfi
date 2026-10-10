package com.unla.ghsicilianotfi.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Resuelve la relacion N a N entre Festival y UnidadVenta.
// El staff asignado, los pedidos y el cierre de caja son por participacion.
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "participacion_festival",
		uniqueConstraints = @UniqueConstraint(columnNames = { "festival_id", "unidad_venta_id" }))
public class ParticipacionFestival {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "festival_id")
	private Festival festival;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "unidad_venta_id")
	private UnidadVenta unidadVenta;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "participacion_personal",
			joinColumns = @JoinColumn(name = "participacion_id"),
			inverseJoinColumns = @JoinColumn(name = "personal_id"))
	private Set<Personal> staff = new HashSet<>();

	@OneToMany(mappedBy = "participacion", fetch = FetchType.LAZY)
	private Set<Pedido> pedidos = new HashSet<>();

	public ParticipacionFestival(Festival festival, UnidadVenta unidadVenta) {
		this.festival = festival;
		this.unidadVenta = unidadVenta;
	}
}
