package PROJECTM.view;

import PROJECTM.controller.PROJECTController;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class PROJECTMenu {

    public static EntityManagerFactory createEntityManagerFactory() {
        EntityManagerFactory emf;
        try {
            emf = Persistence.createEntityManagerFactory("Company"); //TODO
        } catch (Throwable ex) {
            System.err.println("Failed to create EntityManagerFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return emf;
    }

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        PROJECTController PROJECTController = new PROJECTController(entityManager);

        // Menú de opciones
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n" + "1. Borrar tablas de la base de datos y su información");
            System.out.println("2. Crear tablas de la base de datos");
            System.out.println("3. Poblar masivamente las tablas desde el XML" + "\n");
            System.out.println("4. Seleccionar todos los elementos que contengan un texto concreto");
            System.out.println("5. Seleccionar todos los elementos que cumplan una condición");
            System.out.println("6. Seleccionar elementos concretos" + "\n");
            System.out.println("7. Seleccionar un elemento concreto y permitir su modificación");
            System.out.println("8. Modificar diferentes registros de información" + "\n");
            System.out.println("9. Eliminar un registro concreto de información");
            System.out.println("10. Eliminar un conjunto de registros de información que cumplan una condición");
            System.out.println("11. Salir" + "\n");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    PROJECTController.borrarTablas();
                    break;
                case 2:
                    PROJECTController.crearTablas();
                    break;
                case 3:
                    PROJECTController.poblarMasivamente();
                    break;
                case 4:
                    PROJECTController.seleccionarConTexto();
                    break;
                case 5:
                    PROJECTController.seleccionarElementosPorCondicion();
                    break;
                case 6:
                    PROJECTController.seleccionarElementosConcretos();
                    break;
                case 7:
                    PROJECTController.modificarRegistro();
                    break;
                case 8:
                    PROJECTController.modificarRegistros();
                    break;
                case 9:
                    PROJECTController.eliminarRegistro();
                    break;
                case 10:
                    PROJECTController.eliminarRegistros();
                    break;
            }

        } while (opcion != 11);

        scanner.close();
    }
}