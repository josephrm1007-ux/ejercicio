import java.util.Locale;
import java.util.Scanner;

public class ejercicio2 {

    static class Estudiante {
        String codigo;
        String nombre;
        double[] calificaciones;
        int cantidadNotas;

        Estudiante(String codigo, String nombre, int capacidadNotas) {
            this.codigo = codigo;
            this.nombre = nombre;
            if (capacidadNotas < 3) capacidadNotas = 3; // mínimo 3
            this.calificaciones = new double[capacidadNotas];
            this.cantidadNotas = 0;
        }

        void agregarCalificacion(double nota) {
            if (nota < 0.0 || nota > 5.0) {
                System.out.println("Nota inválida. Debe estar entre 0.0 y 5.0");
                return;
            }
            if (cantidadNotas >= calificaciones.length) {
                System.out.println("No se pueden agregar más notas a este estudiante (capacidad alcanzada).");
                return;
            }
            calificaciones[cantidadNotas++] = nota;
        }

        double promedio() {
            if (cantidadNotas == 0) return 0.0;
            double suma = 0.0;
            for (int i = 0; i < cantidadNotas; i++) {
                suma += calificaciones[i];
            }
            return suma / cantidadNotas;
        }

        double maxima() {
            if (cantidadNotas == 0) return 0.0;
            double max = calificaciones[0];
            for (int i = 1; i < cantidadNotas; i++) {
                if (calificaciones[i] > max) max = calificaciones[i];
            }
            return max;
        }

        double minima() {
            if (cantidadNotas == 0) return 0.0;
            double min = calificaciones[0];
            for (int i = 1; i < cantidadNotas; i++) {
                if (calificaciones[i] < min) min = calificaciones[i];
            }
            return min;
        }

        boolean aprobo() {
            return promedio() >= 3.0;
        }

        String categoria() {
            double p = promedio();
            if (p >= 4.5) return "Excelente";
            if (p >= 3.5) return "Bueno";
            if (p >= 3.0) return "Aceptable";
            return "Insuficiente";
        }

        public String toString() {
            String prom = String.format(Locale.US, "%.2f", promedio());
            String max = String.format(Locale.US, "%.2f", maxima());
            String min = String.format(Locale.US, "%.2f", minima());
            return "[" + codigo + "] " + nombre +
                   " | Notas: " + cantidadNotas +
                   " | Prom: " + prom +
                   " | Max: " + max +
                   " | Min: " + min +
                   " | Estado: " + (aprobo() ? "Aprobado" : "Reprobado") +
                   " | Categoria: " + categoria();
        }
    }

    static class ReporteEstadistico {
        public final double promedioGeneral;
        public final Estudiante destacado;
        public final int aprobados;
        public final int reprobados;
        public final int total;
        public final int excelentes;
        public final int buenos;
        public final int aceptables;
        public final int insuficientes;

        public ReporteEstadistico(double promedioGeneral, Estudiante destacado, int aprobados, int reprobados,
                                  int total, int excelentes, int buenos, int aceptables, int insuficientes) {
            this.promedioGeneral = promedioGeneral;
            this.destacado = destacado;
            this.aprobados = aprobados;
            this.reprobados = reprobados;
            this.total = total;
            this.excelentes = excelentes;
            this.buenos = buenos;
            this.aceptables = aceptables;
            this.insuficientes = insuficientes;
        }
    }

    static class GestorCurso {
        Estudiante[] estudiantes;
        int cantidad;

        GestorCurso(int capacidad) {
            if (capacidad < 1) capacidad = 1;
            estudiantes = new Estudiante[capacidad];
            cantidad = 0;
        }

        boolean agregarEstudiante(Estudiante e) {
            if (e == null) return false;
            if (cantidad >= estudiantes.length) {
                System.out.println("Capacidad máxima de estudiantes alcanzada.");
                return false;
            }
            // validar código único
            if (buscarPorCodigo(e.codigo) != null) {
                System.out.println("Ya existe un estudiante con el código " + e.codigo);
                return false;
            }
            estudiantes[cantidad++] = e;
            return true;
        }

        Estudiante buscarPorCodigo(String codigo) {
            for (int i = 0; i < cantidad; i++) {
                if (estudiantes[i].codigo.equalsIgnoreCase(codigo)) return estudiantes[i];
            }
            return null;
        }

        Estudiante[] listarEstudiantes() {
            Estudiante[] copia = new Estudiante[cantidad];
            for (int i = 0; i < cantidad; i++) copia[i] = estudiantes[i];
            return copia;
        }

        double promedioGeneral() {
            if (cantidad == 0) return 0.0;
            double suma = 0.0;
            for (int i = 0; i < cantidad; i++) {
                suma += estudiantes[i].promedio();
            }
            return suma / cantidad;
        }

        Estudiante mejorPromedio() {
            if (cantidad == 0) return null;
            Estudiante mejor = estudiantes[0];
            for (int i = 1; i < cantidad; i++) {
                if (estudiantes[i].promedio() > mejor.promedio()) mejor = estudiantes[i];
            }
            return mejor;
        }

        int contarAprobados() {
            int c = 0;
            for (int i = 0; i < cantidad; i++) if (estudiantes[i].aprobo()) c++;
            return c;
        }

        int contarReprobados() {
            int c = 0;
            for (int i = 0; i < cantidad; i++) if (!estudiantes[i].aprobo()) c++;
            return c;
        }

        void ordenarPorPromedioDesc() {
            // Burbuja descendente optimizada
            if (cantidad < 2) return;
            boolean intercambio;
            int n = cantidad;
            do {
                intercambio = false;
                for (int i = 0; i < n - 1; i++) {
                    if (estudiantes[i].promedio() < estudiantes[i + 1].promedio()) {
                        Estudiante tmp = estudiantes[i];
                        estudiantes[i] = estudiantes[i + 1];
                        estudiantes[i + 1] = tmp;
                        intercambio = true;
                    }
                }
                n--; // última posición ya está en su lugar
            } while (intercambio);
        }

        ReporteEstadistico generarReporte() {
            double promGen = promedioGeneral();
            Estudiante top = mejorPromedio();
            int aprob = contarAprobados();
            int reprob = contarReprobados();
            int total = cantidad;
            int exc = 0, bue = 0, ace = 0, ins = 0;
            for (int i = 0; i < cantidad; i++) {
                String cat = estudiantes[i].categoria();
                if ("Excelente".equals(cat)) exc++;
                else if ("Bueno".equals(cat)) bue++;
                else if ("Aceptable".equals(cat)) ace++;
                else ins++;
            }
            return new ReporteEstadistico(promGen, top, aprob, reprob, total, exc, bue, ace, ins);
        }
    }

    // =====================
    // Configuración y menú
    // =====================
    private static final int CAPACIDAD_ESTUDIANTES = 100;
    private static final int CAPACIDAD_NOTAS_POR_ESTUDIANTE = 50; // mínimo 3 garantizado en el constructor

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        GestorCurso gestor = new GestorCurso(CAPACIDAD_ESTUDIANTES);

        boolean salir = false;
        do {
            imprimirMenu();
            int opcion = leerEntero(sc, "Seleccione una opción: ");
            switch (opcion) {
                case 1:
                    agregarEstudiante(sc, gestor);
                    break;
                case 2:
                    registrarCalificaciones(sc, gestor);
                    break;
                case 3:
                    mostrarListado(gestor);
                    break;
                case 4:
                    gestor.ordenarPorPromedioDesc();
                    System.out.println("Estudiantes ordenados por promedio (descendente).");
                    break;
                case 5:
                    generarYMostrarReporte(gestor);
                    break;
                case 6:
                    salir = true;
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
            System.out.println();
        } while (!salir);

        sc.close();
    }

    private static void imprimirMenu() {
        System.out.println("===== MENÚ PRINCIPAL =====");
        System.out.println("1. Agregar estudiante");
        System.out.println("2. Registrar calificaciones");
        System.out.println("3. Mostrar listado completo");
        System.out.println("4. Ordenar estudiantes por promedio (desc)");
        System.out.println("5. Generar reporte estadístico");
        System.out.println("6. Salir");
    }

    private static void agregarEstudiante(Scanner sc, GestorCurso gestor) {
        System.out.print("Código: ");
        String codigo = sc.nextLine().trim();
        if (codigo.isEmpty()) {
            System.out.println("El código no puede estar vacío.");
            return;
        }
        if (gestor.buscarPorCodigo(codigo) != null) {
            System.out.println("Ya existe un estudiante con ese código.");
            return;
        }
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }

        Estudiante e = new Estudiante(codigo, nombre, CAPACIDAD_NOTAS_POR_ESTUDIANTE);
        if (gestor.agregarEstudiante(e)) {
            System.out.println("Estudiante agregado correctamente.");
        }
    }

    private static void registrarCalificaciones(Scanner sc, GestorCurso gestor) {
        System.out.print("Ingrese código del estudiante: ");
        String codigo = sc.nextLine().trim();
        Estudiante e = gestor.buscarPorCodigo(codigo);
        if (e == null) {
            System.out.println("No se encontró el estudiante con código " + codigo);
            return;
        }
        System.out.println("Registrando notas para: " + e.nombre + " (actual: " + e.cantidadNotas + "/" + e.calificaciones.length + ")");

        boolean continuar = true;
        while (continuar) {
            System.out.print("Ingrese nota (0.0 a 5.0) o -1 para terminar: ");
            String linea = sc.nextLine().trim();
            double nota;
            try {
                nota = Double.parseDouble(linea);
            } catch (NumberFormatException ex) {
                System.out.println("Entrada inválida. Intente de nuevo.");
                continue;
            }
            if (nota == -1) {
                continuar = false;
            } else {
                e.agregarCalificacion(nota);
                if (e.cantidadNotas >= e.calificaciones.length) {
                    System.out.println("Capacidad de notas alcanzada para este estudiante.");
                    continuar = false;
                }
            }
        }
    }

    private static void mostrarListado(GestorCurso gestor) {
        Estudiante[] lista = gestor.listarEstudiantes();
        if (lista.length == 0) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        System.out.println("===== LISTADO DE ESTUDIANTES =====");
        for (int i = 0; i < lista.length; i++) {
            System.out.println((i + 1) + ". " + lista[i]);
        }
    }

    private static void generarYMostrarReporte(GestorCurso gestor) {
        ReporteEstadistico r = gestor.generarReporte();
        System.out.println("===== REPORTE ESTADÍSTICO =====");
        System.out.println("Total estudiantes: " + r.total);
        System.out.println("Promedio general: " + String.format(Locale.US, "%.2f", r.promedioGeneral));
        System.out.println("Aprobados: " + r.aprobados + ", Reprobados: " + r.reprobados);
        System.out.println("- Excelentes: " + r.excelentes);
        System.out.println("- Buenos: " + r.buenos);
        System.out.println("- Aceptables: " + r.aceptables);
        System.out.println("- Insuficientes: " + r.insuficientes);
        if (r.destacado != null) {
            System.out.println("Estudiante destacado: " + r.destacado.nombre + " (" + r.destacado.codigo + ") con promedio " + String.format(Locale.US, "%.2f", r.destacado.promedio()));
        } else {
            System.out.println("Estudiante destacado: N/A");
        }
    }

    private static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                System.out.println("Entrada inválida. Intente de nuevo.");
            }
        }
    }
}