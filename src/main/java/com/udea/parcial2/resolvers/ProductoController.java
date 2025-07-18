package com.udea.parcial2.resolvers;

import com.udea.parcial2.entities.Producto;
import com.udea.parcial2.repositories.ProductoRepository;
import com.udea.parcial2.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoController(ProductoService productoService, ProductoRepository productoRepository) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    @MutationMapping
    public Producto addProducto(
            @Argument String nombre,
            @Argument String descripcion,
            @Argument Double precio,
            @Argument String categoria,
            @Argument Long almacenId,
            @Argument String nombreAlmacen,
            @Argument String direccionAlmacen,
            @Argument Integer cantidad) {
        
        return productoService.addProducto(
                nombre, 
                descripcion, 
                precio, 
                categoria, 
                almacenId, 
                nombreAlmacen, 
                direccionAlmacen, 
                cantidad);
    }

    @QueryMapping
    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

    @QueryMapping
    public Producto getProducto(@Argument Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.orElse(null);
    }
}