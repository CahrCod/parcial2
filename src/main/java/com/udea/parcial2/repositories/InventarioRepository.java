package com.udea.parcial2.repositories;

import com.udea.parcial2.entities.Almacen;
import com.udea.parcial2.entities.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    List<Inventario> findByAlmacen(Almacen almacen);
    List<Inventario> findByAlmacenId(Long almacenId);
}
