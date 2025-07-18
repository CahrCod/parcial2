package com.udea.parcial2.services;

import com.udea.parcial2.entities.Inventario;
import com.udea.parcial2.entities.Producto;
import com.udea.parcial2.repositories.AlmacenRepository;
import com.udea.parcial2.repositories.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final AlmacenRepository almacenRepository;

    @Autowired
    public InventarioService(InventarioRepository inventarioRepository, AlmacenRepository almacenRepository) {
        this.inventarioRepository = inventarioRepository;
        this.almacenRepository = almacenRepository;
    }

    public Map<Producto, Integer> getProductosYCantidadesPorAlmacen(Long almacenId) {
        if (!almacenRepository.existsById(almacenId)) {
            throw new IllegalArgumentException("El almacén con ID " + almacenId + " no existe");
        }

        List<Inventario> inventarios = inventarioRepository.findByAlmacenId(almacenId);

        Map<Producto, Integer> productosYCantidades = new HashMap<>();
        
        for (Inventario inventario : inventarios) {
            productosYCantidades.put(inventario.getProducto(), inventario.getCantidad());
        }
        
        return productosYCantidades;
    }
}