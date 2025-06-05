package techlab;

import java.util.*;

public class Main {
    // Clase Producto
    static class Producto {
        private static int idCounter = 0;
        private final int id;
        private String nombre;
        private double precio;
        private int stock;

        public Producto(String nombre, double precio, int stock) {
            this.id = ++idCounter;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        @SuppressWarnings("unused")

        public void setNombre(String nombre) { this.nombre = nombre; }
        public double getPrecio() { return precio; }
        public void setPrecio(double precio) { this.precio = precio; }
        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }

        @Override
        public String toString() {
            return String.format("ID: %d | Nombre: %s | Precio: %.2f | Stock: %d", id, nombre, precio, stock);
        }
    }

    // Clase LineaPedido
        record LineaPedido(Producto producto, int cantidad) {
    }

    // Clase Pedido
    static class Pedido {
        private static int idCounter = 0;
        private final int id;
        private final List<LineaPedido> lineas;
        private final Date fecha;

        public Pedido() {
            this.id = ++idCounter;
            this.lineas = new ArrayList<>();
            this.fecha = new Date();
        }

        public void agregarLinea(Producto producto, int cantidad) throws StockInsuficienteException {
            if (producto.getStock() < cantidad) {
                throw new StockInsuficienteException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            lineas.add(new LineaPedido(producto, cantidad));
            producto.setStock(producto.getStock() - cantidad);
        }

        public double calcularTotal() {
            double total = 0;
            for (LineaPedido linea : lineas) {
                total += linea.producto().getPrecio() * linea.cantidad();
            }
            return total;
        }

        @Override
        public String toString() {
            return "Pedido ID: " + id + " | Fecha: " + fecha + " | Total: " + calcularTotal();
        }
    }

    // Clase excepción personalizada
    static class StockInsuficienteException extends Exception {
        public StockInsuficienteException(String message) {
            super(message);
        }
    }

    // Variables globales
    static List<Producto> productos = new ArrayList<>();
    static List<Pedido> pedidos = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion();
            ejecutarOpcion(opcion);
        } while (opcion != 10);
        System.out.println("¡Gracias por usar el sistema!");
    }

    static void mostrarMenu() {
        System.out.println("\n=== Menú ===");
        System.out.println("1. Agregar Producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar/Actualizar producto");
        System.out.println("4. Eliminar producto");
        System.out.println("5. Crear pedido");
        System.out.println("6. Listar pedidos");
        System.out.println("10. Salir");
        System.out.print("Seleccione una opción: ");
    }

    static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida, intente nuevamente.");
            return 0;
        }
    }

    static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1: agregarProducto(); break;
            case 2: listarProductos(); break;
            case 3: buscarYActualizarProducto(); break;
            case 4: eliminarProducto(); break;
            case 5: crearPedido(); break;
            case 6: listarPedidos(); break;
            case 10: System.out.println("Saliendo..."); break;
            default: System.out.println("Opción no válida"); break;
        }
    }

    static void agregarProducto() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Precio: ");
            double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Stock: ");
            int stock = Integer.parseInt(scanner.nextLine());
            if (stock < 0 || precio < 0) {
                System.out.println("Valor negativo no permitido.");
                return;
            }
            Producto p = new Producto(nombre, precio, stock);
            productos.add(p);
            System.out.println("Producto agregado: " + p);
        } catch (NumberFormatException e) {
            System.out.println("Entrada no válida.");
        }
    }

    static void listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos en la lista.");
        } else {
            System.out.println("=== Lista de Productos ===");
            for (Producto p : productos) {
                System.out.println(p);
            }
        }
    }

    static void buscarYActualizarProducto() {
        try {
            System.out.print("Buscar por ID (1) o Nombre (2): ");
            String opcionBusq = scanner.nextLine();
            Producto prodEncontrado = null;
            if (opcionBusq.equals("1")) {
                System.out.print("ID: ");
                int id = Integer.parseInt(scanner.nextLine());
                for (Producto p : productos) {
                    if (p.getId() == id) {
                        prodEncontrado = p;
                        break;
                    }
                }
            } else if (opcionBusq.equals("2")) {
                System.out.print("Nombre: ");
                String nombre = scanner.nextLine();
                for (Producto p : productos) {
                    if (p.getNombre().equalsIgnoreCase(nombre)) {
                        prodEncontrado = p;
                        break;
                    }
                }
            } else {
                System.out.println("Opción inválida");
                return;
            }

            if (prodEncontrado != null) {
                System.out.println("Encontrado: " + prodEncontrado);
                System.out.print("Actualizar precio (s/n)? ");
                String respPrecio = scanner.nextLine();
                if (respPrecio.equalsIgnoreCase("s")) {
                    System.out.print("Nuevo precio: ");
                    double nuevoPrecio = Double.parseDouble(scanner.nextLine());
                    if (nuevoPrecio >= 0) {
                        prodEncontrado.setPrecio(nuevoPrecio);
                    } else {
                        System.out.println("Valor inválido");
                    }
                }
                System.out.print("Actualizar stock (s/n)? ");
                String respStock = scanner.nextLine();
                if (respStock.equalsIgnoreCase("s")) {
                    System.out.print("Nuevo stock: ");
                    int nuevoStock = Integer.parseInt(scanner.nextLine());
                    if (nuevoStock >= 0) {
                        prodEncontrado.setStock(nuevoStock);
                    } else {
                        System.out.println("Valor inválido");
                    }
                }
            } else {
                System.out.println("Producto no encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada no válida.");
        }
    }

    static void eliminarProducto() {
        try {
            System.out.print("Eliminar por ID (1) o posición (2): ");
            String opcionElim = scanner.nextLine();
            if (opcionElim.equals("1")) {
                System.out.print("ID: ");
                int id = Integer.parseInt(scanner.nextLine());
                Iterator<Producto> it = productos.iterator();
                boolean encontrado = false;
                while (it.hasNext()) {
                    Producto p = it.next();
                    if (p.getId() == id) {
                        System.out.print("¿Estás seguro de eliminar? (s/n): ");
                        String conf = scanner.nextLine();
                        if (conf.equalsIgnoreCase("s")) {
                            it.remove();
                            System.out.println("Producto eliminado.");
                        }
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) System.out.println("Producto no encontrado.");
            } else if (opcionElim.equals("2")) {
                System.out.print("Posición: ");
                int pos = Integer.parseInt(scanner.nextLine());
                if (pos >= 0 && pos < productos.size()) {
                    System.out.print("¿Estás seguro de eliminar? (s/n): ");
                    String conf = scanner.nextLine();
                    if (conf.equalsIgnoreCase("s")) {
                        productos.remove(pos);
                        System.out.println("Producto eliminado.");
                    }
                } else {
                    System.out.println("Posición inválida.");
                }
            } else {
                System.out.println("Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada no válida.");
        }
    }

    static void crearPedido() {
        try {
            Pedido p = new Pedido();
            boolean seguir = true;
            while (seguir) {
                listarProductos();
                System.out.print("ID del producto a agregar: ");
                int idProd = Integer.parseInt(scanner.nextLine());
                Producto prod = null;
                for (Producto ptemp : productos) {
                    if (ptemp.getId() == idProd) {
                        prod = ptemp;
                        break;
                    }
                }
                if (prod == null) {
                    System.out.println("Producto no encontrado.");
                    continue;
                }
                System.out.print("Cantidad: ");
                int cant = Integer.parseInt(scanner.nextLine());
                try {
                    p.agregarLinea(prod, cant);
                    System.out.println("Producto agregado al pedido.");
                } catch (StockInsuficienteException e) {
                    System.out.println(e.getMessage());
                }
                System.out.print("¿Agregar otro producto? (s/n): ");
                String resp = scanner.nextLine();
                if (!resp.equalsIgnoreCase("s")) seguir = false;
            }
            System.out.println("Total del pedido: " + p.calcularTotal());
            pedidos.add(p);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }

    static void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos realizados.");
        } else {
            for (Pedido p : pedidos) {
                System.out.println(p);
            }
        }
    }
}