package com.udea.parcial2.services;

import com.udea.parcial2.entities.Almacen;
import com.udea.parcial2.entities.Inventario;
import com.udea.parcial2.entities.Producto;
import com.udea.parcial2.repositories.AlmacenRepository;
import com.udea.parcial2.repositories.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private AlmacenRepository almacenRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Almacen almacen;
    private Producto producto1;
    private Producto producto2;
    private Inventario inventario1;
    private Inventario inventario2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        almacen = new Almacen();
        almacen.setId(1L);
        almacen.setNombre("Almacen Test");
        almacen.setDireccion("Dirección Test");

        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Producto 1");
        producto1.setPrecio(100.0);
        producto1.setCategoria("Categoría 1");

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Producto 2");
        producto2.setPrecio(200.0);
        producto2.setCategoria("Categoría 2");

        inventario1 = new Inventario();
        inventario1.setId(1L);
        inventario1.setAlmacen(almacen);
        inventario1.setProducto(producto1);
        inventario1.setCantidad(10);

        inventario2 = new Inventario();
        inventario2.setId(2L);
        inventario2.setAlmacen(almacen);
        inventario2.setProducto(producto2);
        inventario2.setCantidad(20);
    }

    @Test
    void getProductosYCantidadesPorAlmacen_shouldReturnProductsAndQuantities() {
        // Arrange
        Long almacenId = 1L;
        List<Inventario> inventarios = Arrays.asList(inventario1, inventario2);

        when(almacenRepository.existsById(almacenId)).thenReturn(true);
        when(inventarioRepository.findByAlmacenId(almacenId)).thenReturn(inventarios);

        // Act
        Map<Producto, Integer> result = inventarioService.getProductosYCantidadesPorAlmacen(almacenId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10, result.get(producto1));
        assertEquals(20, result.get(producto2));

        verify(almacenRepository).existsById(almacenId);
        verify(inventarioRepository).findByAlmacenId(almacenId);
    }

    @Test
    void getProductosYCantidadesPorAlmacen_shouldThrowException_whenAlmacenNotExists() {
        // Arrange
        Long almacenId = 999L;
        when(almacenRepository.existsById(almacenId)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.getProductosYCantidadesPorAlmacen(almacenId);
        });

        assertTrue(exception.getMessage().contains("no existe"));
        verify(almacenRepository).existsById(almacenId);
        verify(inventarioRepository, never()).findByAlmacenId(anyLong());
    }
}