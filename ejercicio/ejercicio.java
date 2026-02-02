import java.util.ArrayList;
import java.util.Scanner;

enum Nivel {
    ALTO, MEDIO, BAJO
}

class Alumno {
    private String id;
    private String nombre;
    private ArrayList<Double> notas;

    public Alumno(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.notas = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void agregarNota(double n) {
        if (n >= 0 && n <= 5) {
            notas.add(n);
        }
    }

    public double promedio() {
        if (notas.size() < 3) return 0;
        double suma = 0;
        for (double n : notas) {
            suma += n;
        }
        return suma / notas.size();
    }

    public boolean aprueba() {
        return promedio() >= 3.0;
    }

    public Nivel nivel() {
        double p = promedio();
        if (p >= 4.5) return Nivel.ALTO;
        if (p >= 3.0) return Nivel.MEDIO;
        return Nivel.BAJO;
    }

    @Override
    public String toString() {
        return id + " - " + nombre +
                " | Promedio: " + String.format("%.2f", promedio()) +
                " | Estado: " + (aprueba() ? "Aprobado" : "Reprobado") +
                " | Nivel: " + nivel();
    }
}

public class ejercicio {

    static ArrayList<Alumno> alumnos = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== MENÚ =====");
            System.out.println("1. Registrar estudiante");
            System.out.println("2. Agregar notas");
            System.out.println("3. Mostrar estudiantes");
            System.out.println("4. Ver estadísticas");
            System.out.println("5. Salir");
            System.out.print("Seleccione opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> registrar(sc);
                case 2 -> registrarNotas(sc);
                case 3 -> mostrar();
                case 4 -> estadisticas();
                case 5 -> System.out.println("Programa finalizado.");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);

        sc.close();
    }

    static void registrar(Scanner sc) {
        System.out.print("Código: ");
        String id = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        alumnos.add(new Alumno(id, nombre));
        System.out.println("Estudiante registrado correctamente.");
    }

    static void registrarNotas(Scanner sc) {
        System.out.print("Código del estudiante: ");
        String id = sc.nextLine();
        Alumno a = buscar(id);

        if (a == null) {
            System.out.println("Estudiante no encontrado.");
            return;
        }

        double nota;
        do {
            System.out.print("Ingrese nota (-1 para salir): ");
            nota = sc.nextDouble();
            if (nota != -1) a.agregarNota(nota);
        } while (nota != -1);

        sc.nextLine();
    }

    static Alumno buscar(String id) {
        for (Alumno a : alumnos) {
            if (a.getId().equals(id)) return a;
        }
        return null;
    }

    static void mostrar() {
        if (alumnos.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        for (Alumno a : alumnos) {
            System.out.println(a);
        }
    }

    static void estadisticas() {
        int aprobados = 0;
        for (Alumno a : alumnos) {
            if (a.aprueba()) aprobados++;
        }

        System.out.println("Total estudiantes: " + alumnos.size());
        System.out.println("Aprobados: " + aprobados);
        System.out.println("Reprobados: " + (alumnos.size() - aprobados));
    }
}
