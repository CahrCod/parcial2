package com.udea.parcial2.services;

import com.udea.parcial2.entities.Almacen;
import com.udea.parcial2.entities.Inventario;
import com.udea.parcial2.entities.Producto;
import com.udea.parcial2.repositories.AlmacenRepository;
import com.udea.parcial2.repositories.InventarioRepository;
import com.udea.parcial2.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final AlmacenRepository almacenRepository;
    private final InventarioRepository inventarioRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository,
                           AlmacenRepository almacenRepository,
                           InventarioRepository inventarioRepository) {
        this.productoRepository = productoRepository;
        this.almacenRepository = almacenRepository;
        this.inventarioRepository = inventarioRepository;
    }


    @Transactional
    public Producto addProducto(String nombre, String descripcion, Double precio, String categoria,
                               Long almacenId, String nombreAlmacen, String direccionAlmacen,
                               Integer cantidad) {
        

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        
        if (precio == null || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        
        if (cantidad == null || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setCategoria(categoria);

        producto = productoRepository.save(producto);

        Almacen almacen;
        if (almacenId != null) {
            Optional<Almacen> almacenOptional = almacenRepository.findById(almacenId);
            if (almacenOptional.isPresent()) {
                almacen = almacenOptional.get();

                if (nombreAlmacen != null && !nombreAlmacen.trim().isEmpty()) {
                    almacen.setNombre(nombreAlmacen);
                }
                if (direccionAlmacen != null && !direccionAlmacen.trim().isEmpty()) {
                    almacen.setDireccion(direccionAlmacen);
                }
            } else {

                almacen = new Almacen();
                almacen.setId(almacenId);
                almacen.setNombre(nombreAlmacen != null ? nombreAlmacen : "Almacén " + almacenId);
                almacen.setDireccion(direccionAlmacen);
            }
        } else {
            almacen = new Almacen();
            almacen.setNombre(nombreAlmacen != null ? nombreAlmacen : "Nuevo Almacén");
            almacen.setDireccion(direccionAlmacen);
        }

        almacen = almacenRepository.save(almacen);
        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setAlmacen(almacen);
        inventario.setCantidad(cantidad);

        inventarioRepository.save(inventario);
        
        return producto;
    }
}