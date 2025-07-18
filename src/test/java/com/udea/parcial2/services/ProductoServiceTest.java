package com.udea.parcial2.services;

import com.udea.parcial2.entities.Almacen;
import com.udea.parcial2.entities.Inventario;
import com.udea.parcial2.entities.Producto;
import com.udea.parcial2.repositories.AlmacenRepository;
import com.udea.parcial2.repositories.InventarioRepository;
import com.udea.parcial2.repositories.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private AlmacenRepository almacenRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private Almacen almacen;
    private Inventario inventario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar comportamiento de los mocks
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción Test");
        producto.setPrecio(100.0);
        producto.setCategoria("Categoría Test");

        almacen = new Almacen();
        almacen.setId(1L);
        almacen.setNombre("Almacén Test");
        almacen.setDireccion("Dirección Test");

        inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProducto(producto);
        inventario.setAlmacen(almacen);
        inventario.setCantidad(10);

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(almacenRepository.save(any(Almacen.class))).thenReturn(almacen);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);
    }

    @Test
    void addProducto_withValidData_shouldCreateEntities() {
        // Arrange
        String nombre = "Producto Test";
        String descripcion = "Descripción Test";
        Double precio = 100.0;
        String categoria = "Categoría Test";
        Long almacenId = 1L;
        String nombreAlmacen = "Almacén Test";
        String direccionAlmacen = "Dirección Test";
        Integer cantidad = 10;

        when(almacenRepository.findById(almacenId)).thenReturn(Optional.of(almacen));

        // Act
        Producto result = productoService.addProducto(nombre, descripcion, precio, categoria,
                almacenId, nombreAlmacen, direccionAlmacen, cantidad);

        // Assert
        assertNotNull(result);
        assertEquals(producto.getId(), result.getId());
        assertEquals(producto.getNombre(), result.getNombre());

        // Verificar que se llamaron los métodos de los repositorios
        verify(productoRepository).save(any(Producto.class));
        verify(almacenRepository).findById(almacenId);
        verify(almacenRepository).save(any(Almacen.class));
        verify(inventarioRepository).save(any(Inventario.class));

        // Verificar los datos del producto guardado
        ArgumentCaptor<Producto> productoCaptor = ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository).save(productoCaptor.capture());
        Producto savedProducto = productoCaptor.getValue();
        assertEquals(nombre, savedProducto.getNombre());
        assertEquals(descripcion, savedProducto.getDescripcion());
        assertEquals(precio, savedProducto.getPrecio());
        assertEquals(categoria, savedProducto.getCategoria());

        // Verificar los datos del inventario guardado
        ArgumentCaptor<Inventario> inventarioCaptor = ArgumentCaptor.forClass(Inventario.class);
        verify(inventarioRepository).save(inventarioCaptor.capture());
        Inventario savedInventario = inventarioCaptor.getValue();
        assertEquals(cantidad, savedInventario.getCantidad());
        assertSame(producto, savedInventario.getProducto());
        assertSame(almacen, savedInventario.getAlmacen());
    }

    @Test
    void addProducto_withNewAlmacen_shouldCreateNewAlmacen() {
        // Arrange
        String nombre = "Producto Test";
        String descripcion = "Descripción Test";
        Double precio = 100.0;
        String categoria = "Categoría Test";
        Long almacenId = 2L; // ID que no existe
        String nombreAlmacen = "Nuevo Almacén";
        String direccionAlmacen = "Nueva Dirección";
        Integer cantidad = 10;

        when(almacenRepository.findById(almacenId)).thenReturn(Optional.empty());

        // Act
        Producto result = productoService.addProducto(nombre, descripcion, precio, categoria,
                almacenId, nombreAlmacen, direccionAlmacen, cantidad);

        // Assert
        assertNotNull(result);

        // Verificar que se creó un nuevo almacén
        ArgumentCaptor<Almacen> almacenCaptor = ArgumentCaptor.forClass(Almacen.class);
        verify(almacenRepository).save(almacenCaptor.capture());
        Almacen savedAlmacen = almacenCaptor.getValue();
        assertEquals(almacenId, savedAlmacen.getId());
        assertEquals(nombreAlmacen, savedAlmacen.getNombre());
        assertEquals(direccionAlmacen, savedAlmacen.getDireccion());
    }

    @Test
    void addProducto_withNullAlmacenId_shouldCreateNewAlmacen() {
        // Arrange
        String nombre = "Producto Test";
        String descripcion = "Descripción Test";
        Double precio = 100.0;
        String categoria = "Categoría Test";
        Long almacenId = null; // ID nulo
        String nombreAlmacen = "Nuevo Almacén";
        String direccionAlmacen = "Nueva Dirección";
        Integer cantidad = 10;

        // Act
        Producto result = productoService.addProducto(nombre, descripcion, precio, categoria,
                almacenId, nombreAlmacen, direccionAlmacen, cantidad);

        // Assert
        assertNotNull(result);

        // Verificar que se creó un nuevo almacén sin ID específico
        ArgumentCaptor<Almacen> almacenCaptor = ArgumentCaptor.forClass(Almacen.class);
        verify(almacenRepository).save(almacenCaptor.capture());
        Almacen savedAlmacen = almacenCaptor.getValue();
        assertEquals(nombreAlmacen, savedAlmacen.getNombre());
        assertEquals(direccionAlmacen, savedAlmacen.getDireccion());
    }

    @Test
    void addProducto_withInvalidNombre_shouldThrowException() {
        // Arrange
        String nombre = ""; // Nombre vacío
        String descripcion = "Descripción Test";
        Double precio = 100.0;
        String categoria = "Categoría Test";
        Long almacenId = 1L;
        String nombreAlmacen = "Almacén Test";
        String direccionAlmacen = "Dirección Test";
        Integer cantidad = 10;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productoService.addProducto(nombre, descripcion, precio, categoria,
                    almacenId, nombreAlmacen, direccionAlmacen, cantidad);
        });

        assertTrue(exception.getMessage().contains("nombre del producto"));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void addProducto_withInvalidPrecio_shouldThrowException() {
        // Arrange
        String nombre = "Producto Test";
        String descripcion = "Descripción Test";
        Double precio = -10.0; // Precio negativo
        String categoria = "Categoría Test";
        Long almacenId = 1L;
        String nombreAlmacen = "Almacén Test";
        String direccionAlmacen = "Dirección Test";
        Integer cantidad = 10;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productoService.addProducto(nombre, descripcion, precio, categoria,
                    almacenId, nombreAlmacen, direccionAlmacen, cantidad);
        });

        assertTrue(exception.getMessage().contains("precio"));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void addProducto_withInvalidCantidad_shouldThrowException() {
        // Arrange
        String nombre = "Producto Test";
        String descripcion = "Descripción Test";
        Double precio = 100.0;
        String categoria = "Categoría Test";
        Long almacenId = 1L;
        String nombreAlmacen = "Almacén Test";
        String direccionAlmacen = "Dirección Test";
        Integer cantidad = -5; // Cantidad negativa

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productoService.addProducto(nombre, descripcion, precio, categoria,
                    almacenId, nombreAlmacen, direccionAlmacen, cantidad);
        });

        assertTrue(exception.getMessage().contains("cantidad"));
        verify(productoRepository, never()).save(any(Producto.class));
    }
}