/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import ClasesTemporales.ResumenPedido;
import Clases.*;
import ClasesEnum.enums.EstadoPedido;
import ClasesEnum.enums.TipoPedido;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import logica.ConfiguracionJpaController;
import logica.PedidoJpaController;
import logica.TarifaJpaController;
import utilJpa.JPAUtil;

public class PedidoService {

    private final PedidoJpaController pedidoController;
    private final EntityManagerFactory emf;
    private final ConfiguracionJpaController configuracionController;

    public PedidoService() {
        this.emf = JPAUtil.getEMF();
        this.pedidoController = new PedidoJpaController(emf);
        this.configuracionController = new ConfiguracionJpaController(emf);

    }

    // =====================================================
    // CREAR PEDIDO
    // =====================================================
    // =====================================================
// CREAR PEDIDO
// =====================================================
    public Pedido crearPedido(
            Pedido pedido,
            Collection<DetallePedido> detalles,
            EntregaPedido entrega,
            Usuario usuario,
            Cliente cliente) {

        // =========================
        // VALIDACIONES
        // =========================
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido requerido");
        }

        if (pedido.getTipoPedido() == null) {
            throw new IllegalArgumentException("Tipo de pedido obligatorio");
        }

        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener productos");
        }

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente obligatorio");
        }

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario obligatorio");
        }
        EntityManager em = emf.createEntityManager();
        try {

            em.getTransaction().begin();

            // =========================
            // RELACIONES
            // =========================
            pedido.setIdCliente(cliente);
            pedido.setIdUsuario(usuario);

            // =========================
            // GENERAR CÓDIGO
            // =========================
            if (pedido.getCodigo() == null
                    || pedido.getCodigo().isBlank()) {

                pedido.setCodigo(
                        generarCodigo(
                                em,
                                pedido.getTipoPedido().name()
                        )
                );
            }

            // =========================
            // ESTADO INICIAL
            // =========================
            if (pedido.getEstado() == null) {
                pedido.setEstado(EstadoPedido.PENDIENTE);
            }

            // =========================
            // FECHA
            // =========================
            if (pedido.getFechaHora() == null) {
                pedido.setFechaHora(new Date());
            }

            // =========================
            // IMPORTES INICIALES
            // =========================
            pedido.setSubtotal(BigDecimal.ZERO);
            pedido.setImpuesto(BigDecimal.ZERO);
            pedido.setTotal(BigDecimal.ZERO);

            // =========================
            // GUARDAR PEDIDO
            // =========================
            em.persist(pedido);

            // importante para obtener ID
            em.flush();

            // =========================
            // CREAR DETALLES
            // =========================
            BigDecimal subtotal = BigDecimal.ZERO;

            for (DetallePedido d : detalles) {

                if (d.getIdProducto() == null) {
                    throw new IllegalArgumentException(
                            "Producto requerido en detalle"
                    );
                }

                if (d.getCantidad() == null
                        || d.getCantidad() <= 0) {

                    throw new IllegalArgumentException(
                            "Cantidad inválida"
                    );
                }

                // relacionar pedido
                d.setIdPedido(pedido);

                // precio actual producto
                BigDecimal precio
                        = d.getIdProducto().getPrecio();

                d.setPrecioUnitario(precio);

                // subtotal detalle
                BigDecimal sub
                        = precio.multiply(
                                BigDecimal.valueOf(
                                        d.getCantidad()
                                )
                        );

                d.setSubtotal(sub);

                // acumular subtotal general
                subtotal = subtotal.add(sub);

                // guardar detalle
                em.persist(d);
            }

            // =========================
            // CALCULAR TOTALES
            // =========================
            pedido.setSubtotal(subtotal);

            Configuracion config
                    = configuracionController.findConfiguracion(1);

            BigDecimal ivaDecimal
                    = config.getValor().divide(
                            new BigDecimal("100")
                    );

            BigDecimal impuesto
                    = subtotal.multiply(ivaDecimal);

            pedido.setImpuesto(impuesto);

            pedido.setTotal(
                    subtotal.add(impuesto)
            );

            // actualizar pedido
            em.merge(pedido);

            // =========================
            // SOLO DELIVERY
            // =========================
            if (pedido.getTipoPedido() == TipoPedido.DELIVERY) {

                if (entrega == null) {
                    throw new IllegalArgumentException("Delivery requiere entrega");
                }

                // 1. calcular tarifa primero (dato de negocio externo)
                TarifaJpaController tarifaController = new TarifaJpaController(emf);

                Tarifa tarifa = tarifaController.obtenerTarifaPorDistancia(
                        entrega.getDistanciaKm()
                );

                if (tarifa == null) {
                    throw new IllegalArgumentException("No existe tarifa para la distancia");
                }

                entrega.setIdTarifa(tarifa);
                entrega.setCostoEnvio(tarifa.getPrecio());

                // 2. ahora sí completar datos necesarios para validación
                entrega.setIdPedido(pedido);

                if (entrega.getFechaHoraSalida() == null) {
                    entrega.setFechaHoraSalida(new Date());
                }

                // 3. ahora validar (ya está completo)
                validarEntrega(entrega);

                // 4. persistir entrega
                em.persist(entrega);

                em.merge(pedido);
            }

            // =========================
            // COMMIT
            // =========================
            em.getTransaction().commit();

            return pedido;

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "Error creando pedido",
                    e
            );

        } finally {
            em.close();
        }
    }

    //metodo para calcular el total + envio 
    public BigDecimal calcularTotalCobrar(
            Pedido pedido,
            EntregaPedido entrega
    ) {

        BigDecimal total = pedido.getTotal();

        if (entrega != null) {
            total = total.add(entrega.getCostoEnvio());
        }

        return total;
    }

    public Pedido buscarPedidoConDetalles(Integer id) {
        return pedidoController.findPedidoConDetalles(id);
    }

    // =====================================================
    // CONSULTA POR ID
    // =====================================================
    public Pedido verPedido(int idPedido) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Pedido.class, idPedido);

        } finally {
            em.close();
        }
    }

    // =====================================================
    // PEDIDOS POR USUARIO
    // =====================================================
    public Collection<EntregaPedido> obtenerEntregasPorRepartidor(Usuario repartidor) {

        return repartidor.getEntregaPedidoCollection();
    }

    // =====================================================
    // CAMBIO DE ESTADO
    // =====================================================
    // ══════════════════════════════════════════════════════════════════════
// Reemplaza los dos métodos en PedidoService
// ══════════════════════════════════════════════════════════════════════
    public Pedido cambiarEstado(int idPedido, EstadoPedido nuevoEstado,
            Usuario usuario, Rol rolActual) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Pedido pedido = em.find(Pedido.class, idPedido);
            if (pedido == null) {
                throw new IllegalArgumentException("Pedido no encontrado.");
            }

            EstadoPedido actual = pedido.getEstado();

            if (actual == EstadoPedido.ENTREGADO || actual == EstadoPedido.CANCELADO) {
                throw new IllegalStateException(
                        "El pedido ya está finalizado y no puede modificarse.");
            }

            // CAMBIO 2: pasa rolActual.getNombre() en lugar de extraerlo del usuario
            verificarPermisoPorRol(rolActual != null ? rolActual.getNombre() : "Desconocido",
                    actual, nuevoEstado);

            if (!esTransicionValida(actual, nuevoEstado)) {
                throw new IllegalStateException(
                        "Transición inválida: " + actual + " → " + nuevoEstado);
            }

            pedido.setEstado(nuevoEstado);
            em.merge(pedido);

            HistorialPedido h = new HistorialPedido();
            h.setIdPedido(pedido);
            h.setIdUsuario(usuario);
            h.setEstado(nuevoEstado);
            h.setFechaHora(new java.util.Date());
            // CAMBIO 3: usa rolActual directamente en la observación
            h.setObservacion("Cambio de estado: " + actual + " → " + nuevoEstado
                    + " por " + usuario.getNombre()
                    + " [" + (rolActual != null ? rolActual.getNombre() : "Sin rol") + "]");
            em.persist(h);

            em.getTransaction().commit();
            return pedido;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

// CAMBIO 4: la firma de verificarPermisoPorRol ahora recibe String en lugar de Usuario
    private void verificarPermisoPorRol(String rol, EstadoPedido actual, EstadoPedido nuevo) {
        switch (rol) {
            case "Cajero" -> {
                boolean permitido = (actual == EstadoPedido.PENDIENTE)
                        || (nuevo == EstadoPedido.CANCELADO)
                        || (nuevo == EstadoPedido.ENTREGADO);
                if (!permitido) {
                    throw new IllegalStateException(
                            "El cajero solo puede gestionar pedidos PENDIENTES "
                            + "o cancelar/entregar cualquier pedido.");
                }
            }
            case "Cocinero" -> {
                boolean permitido = (actual == EstadoPedido.PENDIENTE
                        && nuevo == EstadoPedido.EN_PREPARACION)
                        || (actual == EstadoPedido.EN_PREPARACION
                        && nuevo == EstadoPedido.LISTO);
                if (!permitido) {
                    throw new IllegalStateException(
                            "El cocinero solo puede cambiar: "
                            + "PENDIENTE → EN_PREPARACION o EN_PREPARACION → LISTO.");
                }
            }
            case "Repartidor" -> {
                boolean permitido = (actual == EstadoPedido.LISTO
                        && nuevo == EstadoPedido.EN_RUTA)
                        || (actual == EstadoPedido.EN_RUTA
                        && nuevo == EstadoPedido.ENTREGADO);
                if (!permitido) {
                    throw new IllegalStateException(
                            "El repartidor solo puede cambiar: "
                            + "LISTO → EN_RUTA o EN_RUTA → ENTREGADO.");
                }
            }
            case "Administrador" -> {
                // Sin restricción de rol
            }
            default ->
                throw new IllegalStateException(
                        "Rol desconocido: " + rol + ". No tiene permisos.");
        }
    }

    /**
     * Verifica si el rol del usuario tiene permiso para realizar la transición
     * solicitada.
     *
     * CAJERO → PENDIENTE, ENTREGADO, CANCELADO COCINERO → EN_PREPARACION, LISTO
     * REPARTIDOR→ EN_RUTA, ENTREGADO
     */
    private void verificarPermisoPorRol(Usuario usuario,
            EstadoPedido actual,
            EstadoPedido nuevo) {

        String rol = obtenerNombreRol(usuario);

        switch (rol) {

            case "Cajero" -> {
                // Cajero: puede mover desde PENDIENTE, y puede cancelar o marcar entregado
                boolean permitido = (actual == EstadoPedido.PENDIENTE)
                        || (nuevo == EstadoPedido.CANCELADO)
                        || (nuevo == EstadoPedido.ENTREGADO);
                if (!permitido) {
                    throw new IllegalStateException(
                            "El cajero solo puede gestionar pedidos PENDIENTES "
                            + "o cancelar/entregar cualquier pedido.");
                }
            }

            case "Cocinero" -> {
                // Cocinero: solo puede avanzar PENDIENTE→EN_PREPARACION o EN_PREPARACION→LISTO
                boolean permitido = (actual == EstadoPedido.PENDIENTE
                        && nuevo == EstadoPedido.EN_PREPARACION)
                        || (actual == EstadoPedido.EN_PREPARACION
                        && nuevo == EstadoPedido.LISTO);
                if (!permitido) {
                    throw new IllegalStateException(
                            "El cocinero solo puede cambiar: "
                            + "PENDIENTE → EN_PREPARACION o EN_PREPARACION → LISTO.");
                }
            }

            case "Repartidor" -> {
                // Repartidor: solo puede avanzar LISTO→EN_RUTA o EN_RUTA→ENTREGADO
                boolean permitido = (actual == EstadoPedido.LISTO
                        && nuevo == EstadoPedido.EN_RUTA)
                        || (actual == EstadoPedido.EN_RUTA
                        && nuevo == EstadoPedido.ENTREGADO);
                if (!permitido) {
                    throw new IllegalStateException(
                            "El repartidor solo puede cambiar: "
                            + "LISTO → EN_RUTA o EN_RUTA → ENTREGADO.");
                }
            }

            case "Administrador" -> {
                // Administrador puede hacer cualquier transición válida — sin restricción de rol
            }

            default ->
                throw new IllegalStateException(
                        "Rol desconocido: " + rol + ". No tiene permisos para cambiar estados.");
        }
    }

    /**
     * Valida que la transición sea coherente con el flujo del negocio,
     * independientemente del rol.
     */
    private boolean esTransicionValida(EstadoPedido actual, EstadoPedido nuevo) {
        switch (actual) {
            case PENDIENTE:
                return nuevo == EstadoPedido.EN_PREPARACION
                        || nuevo == EstadoPedido.CANCELADO;
            case EN_PREPARACION:
                return nuevo == EstadoPedido.LISTO
                        || nuevo == EstadoPedido.CANCELADO;
            case LISTO:
                return nuevo == EstadoPedido.EN_RUTA
                        || nuevo == EstadoPedido.CANCELADO;
            case EN_RUTA:
                return nuevo == EstadoPedido.ENTREGADO
                        || nuevo == EstadoPedido.CANCELADO;
            default:
                return false;
        }
    }

    /**
     * Extrae el nombre del primer rol del usuario. Si tiene múltiples roles,
     * toma el primero de la colección.
     */
    private String obtenerNombreRol(Usuario usuario) {
        if (usuario.getRolCollection() == null || usuario.getRolCollection().isEmpty()) {
            return "Desconocido";
        }
        return usuario.getRolCollection().iterator().next().getNombre();
    }

    public List<DetallePedido> agregarOActualizarDetalle(
            List<DetallePedido> carrito, Producto producto, int cantidad) {

        for (DetallePedido d : carrito) {
            if (d.getIdProducto().getIdProducto().equals(producto.getIdProducto())) {
                d.setCantidad(d.getCantidad() + cantidad);
                d.setSubtotal(d.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(d.getCantidad())));
                return carrito;
            }
        }
        DetallePedido nuevo = new DetallePedido();
        nuevo.setIdProducto(producto);
        nuevo.setCantidad(cantidad);
        nuevo.setPrecioUnitario(producto.getPrecio());
        nuevo.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
        carrito.add(nuevo);
        return carrito;
    }

    // =====================================================
    // REGLAS DE NEGOCIO DE ESTADOS
    // =====================================================
    // =====================================================
    // VALIDAR ENTREGA
    // =====================================================
    private void validarEntrega(EntregaPedido e) {

        if (e.getDireccionEntrega() == null || e.getDireccionEntrega().isBlank()) {
            throw new IllegalArgumentException("Dirección requerida");
        }

        if (e.getIdUsuarioRepartidor() == null) {
            throw new IllegalArgumentException("Repartidor requerido");
        }

        if (e.getIdTarifa() == null) {
            throw new IllegalArgumentException("Tarifa requerida");
        }

        if (e.getDistanciaKm() == null) {
            throw new IllegalArgumentException("Distancia requerida");
        }

        if (e.getCostoEnvio() == null) {
            throw new IllegalArgumentException("Costo de envío requerido");
        }
    }

    // =====================================================
    // GENERAR CÓDIGO
    // =====================================================
    //HACERLO CON JPQL es mucho mejor, ya que hacerlo solamente con jpa, seria 
    //cargar innesesariamente la memoria al traer todos los pedidos existentes
    private String generarCodigo(EntityManager em, String tipo) {

        String prefijo = "P-" + tipo.toUpperCase();

        TypedQuery<String> q = em.createQuery(
                "SELECT p.codigo FROM Pedido p "
                + "WHERE p.codigo LIKE :pref "
                + "ORDER BY p.idPedido DESC",
                String.class
        );

        q.setParameter("pref", prefijo + "%");
        q.setMaxResults(1);

        String ultimo = q.getResultStream().findFirst().orElse(null);

        int num = 1;

        if (ultimo != null && ultimo.contains("-")) {
            String[] parts = ultimo.split("-");
            num = Integer.parseInt(parts[parts.length - 1]) + 1;
        }

        return String.format("%s-%03d", prefijo, num);
    }

    public ResumenPedido calcularResumen(
            List<DetallePedido> detalles) {

        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetallePedido d : detalles) {

            subtotal = subtotal.add(
                    d.getSubtotal()
            );
        }

        ConfiguracionJpaController configuracionController
                = new ConfiguracionJpaController(emf);

        Configuracion config
                = configuracionController.findConfiguracion(1);

        BigDecimal ivaDecimal
                = config.getValor().divide(
                        new BigDecimal("100")
                );

        BigDecimal impuesto
                = subtotal.multiply(ivaDecimal);

        BigDecimal total
                = subtotal.add(impuesto);

        ResumenPedido resumen
                = new ResumenPedido();

        resumen.setSubtotal(subtotal);

        resumen.setIva(impuesto);

        resumen.setTotal(total);

        return resumen;
    }

    public Pedido buscarPorCodigo(String codigo) {
        return pedidoController.findPedidoByCodigo(codigo);
    }

    public List<Pedido> listaPedidos() {
        return pedidoController.findPedidoEntities();
    }

    public List<Pedido> buscarFiltrado(String estado, String tipo, String codigo) {
        List<Pedido> pedidos = pedidoController.findPedidoEntities();
        return pedidos.stream()
                .filter(p -> estado == null || p.getEstado().name().equalsIgnoreCase(estado))
                .filter(p -> tipo == null || p.getTipoPedido().name().equalsIgnoreCase(tipo))
                .filter(p -> codigo == null || codigo.isBlank()
                || p.getCodigo().toLowerCase().contains(codigo.toLowerCase()))
                .toList();
    }

}
