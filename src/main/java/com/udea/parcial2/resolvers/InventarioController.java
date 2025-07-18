package com.udea.parcial2.resolvers;

import com.udea.parcial2.entities.Almacen;
import com.udea.parcial2.entities.Inventario;
import com.udea.parcial2.entities.Producto;
import com.udea.parcial2.repositories.AlmacenRepository;
import com.udea.parcial2.repositories.InventarioRepository;
import com.udea.parcial2.services.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class InventarioController {

    private final InventarioService inventarioService;
    private final InventarioRepository inventarioRepository;
    private final AlmacenRepository almacenRepository;

    @Autowired
    public InventarioController(
            InventarioService inventarioService,
            InventarioRepository inventarioRepository,
            AlmacenRepository almacenRepository) {
        this.inventarioService = inventarioService;
        this.inventarioRepository = inventarioRepository;
        this.almacenRepository = almacenRepository;
    }

    @QueryMapping
    public List<ProductoConCantidad> getProductosYCantidadesPorAlmacen(@Argument Long almacenId) {
        Map<Producto, Integer> productosYCantidades = inventarioService.getProductosYCantidadesPorAlmacen(almacenId);
        List<ProductoConCantidad> result = new ArrayList<>();
        
        for (Map.Entry<Producto, Integer> entry : productosYCantidades.entrySet()) {
            result.add(new ProductoConCantidad(entry.getKey(), entry.getValue()));
        }
        
        return result;
    }

    @QueryMapping
    public List<Almacen> getAlmacenes() {
        return almacenRepository.findAll();
    }

    @QueryMapping
    public Almacen getAlmacen(@Argument Long id) {
        Optional<Almacen> almacen = almacenRepository.findById(id);
        return almacen.orElse(null);
    }

    public static class ProductoConCantidad {
        private Producto producto;
        private Integer cantidad;

        public ProductoConCantidad(Producto producto, Integer cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public Producto getProducto() {
            return producto;
        }
        public Integer getCantidad() {
            return cantidad;
        }
    }
}