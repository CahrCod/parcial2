package com.udea.parcial2.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private String categoria;

    @OneToMany
    private Set<Inventario> inventarios;
}
