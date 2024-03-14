package PROJECTM.controller;

import PROJECTM.model.Categoria;
import PROJECTM.model.Detalle;
import PROJECTM.model.Juego;
import PROJECTM.model.Mod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.persistence.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;

public class PROJECTController {
    private EntityManager entityManager;

    public PROJECTController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void borrarTablas() {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.createNativeQuery("DROP TABLE IF EXISTS Categoria").executeUpdate();
            entityManager.createNativeQuery("DROP TABLE IF EXISTS Detalle").executeUpdate();
            entityManager.createNativeQuery("DROP TABLE IF EXISTS Mod").executeUpdate();
            entityManager.createNativeQuery("DROP TABLE IF EXISTS Juego").executeUpdate();

            // Confirmar la transacción
            transaction.commit();

            // Imprimir un mensaje de éxito después de borrar las tablas
            System.out.println("\n" + "Tablas borradas exitosamente." + "\n");
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            // Si se produce una excepción, se imprimirá el seguimiento de la pila
            ex.printStackTrace();
        }
    }


    public void crearTablas() {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Sentencia SQL para crear la tabla Juego
            String crearTablaJuego = "CREATE TABLE Juego (" +
                    "JuegoID SERIAL PRIMARY KEY," +
                    "Nombre VARCHAR(255) NOT NULL," +
                    "Descripcion TEXT);";

            // Sentencia SQL para crear la tabla Mod
            String crearTablaMod = "CREATE TABLE Mod (" +
                    "ModID SERIAL PRIMARY KEY," +
                    "JuegoID INT," +
                    "Nombre VARCHAR(255) NOT NULL," +
                    "Autor VARCHAR(255) NOT NULL," +
                    "Descripcion TEXT," +
                    "FOREIGN KEY (JuegoID) REFERENCES Juego(JuegoID)" +
                    ");";

            // Sentencia SQL para crear la tabla Detalles
            String crearTablaDetalle = "CREATE TABLE Detalle (" +
                    "DetalleID SERIAL PRIMARY KEY," +
                    "ModID INT," +
                    "Descripcion TEXT," +
                    "FOREIGN KEY (ModID) REFERENCES Mod(ModID)" +
                    ");";

            // Sentencia SQL para crear la tabla Categoria
            String crearTablaCategoria = "CREATE TABLE Categoria (" +
                    "CategoriaID SERIAL PRIMARY KEY," +
                    "ModID INT," +
                    "Nombre VARCHAR(255) NOT NULL," +
                    "FOREIGN KEY (ModID) REFERENCES Mod(ModID));";

            // Ejecutar las sentencias SQL
            entityManager.createNativeQuery(crearTablaJuego).executeUpdate();
            entityManager.createNativeQuery(crearTablaMod).executeUpdate();
            entityManager.createNativeQuery(crearTablaDetalle).executeUpdate();
            entityManager.createNativeQuery(crearTablaCategoria).executeUpdate();

            // Confirmar la transacción
            transaction.commit();

            // Imprimir un mensaje de éxito después de crear las tablas
            System.out.println("\n" + "Tablas creadas exitosamente." + "\n");
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            // Si se produce una excepción, se imprimirá el seguimiento de la pila
            ex.printStackTrace();
        }
    }


    public void poblarMasivamente() {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Cargar el archivo XML y obtener el documento
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse("CurseForge.xml");

            document.getDocumentElement().normalize();

            NodeList juegosNodeList = document.getElementsByTagName("Juego");

            // Obtener la lista de nodos 'Juego' del documento XML
            for (int temp = 0; temp < juegosNodeList.getLength(); temp++) {
                Node juegoNode = juegosNodeList.item(temp);

                if (juegoNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element juegoElement = (Element) juegoNode;

                    // Obtener el nombre y la descripción del juego
                    String nombreJuego = juegoElement.getElementsByTagName("Nombre").item(0).getTextContent();
                    String descripcionJuego = juegoElement.getElementsByTagName("Descripcion").item(0).getTextContent();

                    // Crear un nuevo objeto Juego y persistirlo en la base de datos
                    Juego juego = new Juego();
                    juego.setNombre(nombreJuego.replaceAll("\n", "").replaceAll(" ", ""));
                    juego.setDescripcion(descripcionJuego.replaceAll("\n", "").replaceAll("  ", ""));
                    entityManager.persist(juego);

                    // Obtener la lista de nodos 'Mod' del juego actual
                    NodeList modsNodeList = juegoElement.getElementsByTagName("Mod");
                    for (int modIndex = 0; modIndex < modsNodeList.getLength(); modIndex++) {
                        Node modNode = modsNodeList.item(modIndex);

                        if (modNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element modElement = (Element) modNode;

                            // Obtener los detalles del mod
                            String nombreMod = modElement.getElementsByTagName("Nombre").item(0).getTextContent();
                            String autorMod = modElement.getElementsByTagName("Autor").item(0).getTextContent();
                            String descripcionMod = modElement.getElementsByTagName("Descripcion").item(0).getTextContent();

                            // Crear un nuevo objeto Mod y persistirlo en la base de datos
                            Mod mod = new Mod();
                            mod.setJuego(juego);
                            mod.setNombre(nombreMod);
                            mod.setAutor(autorMod);
                            mod.setDescripcion(descripcionMod);
                            entityManager.persist(mod);

                            // Obtener detalles del mod
                            NodeList detallesNodeList = modElement.getElementsByTagName("Detalles");
                            for (int detalleIndex = 0; detalleIndex < detallesNodeList.getLength(); detalleIndex++) {
                                Node detalleNode = detallesNodeList.item(detalleIndex);
                                if (detalleNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element detalleElement = (Element) detalleNode;
//                                    String descripcionDetalle = detalleElement.getElementsByTagName("Descripcion").item(0).getTextContent();
                                    String descripcionDetalle = detalleElement.getTextContent();

                                    // Crear un nuevo objeto Detalle y persistirlo en la base de datos
                                    Detalle detalle = new Detalle();
                                    detalle.setMod(mod);
                                    detalle.setDescripcion(descripcionDetalle.replaceAll("\n", "").replaceAll("  ", ""));
                                    entityManager.persist(detalle);
                                }
                            }

                            // Obtener categorías del mod
                            NodeList categoriasNodeList = modElement.getElementsByTagName("Categoria");
                            for (int categoriaIndex = 0; categoriaIndex < categoriasNodeList.getLength(); categoriaIndex++) {
                                Node categoriaNode = categoriasNodeList.item(categoriaIndex);
                                if (categoriaNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element categoriaElement = (Element) categoriaNode;
//                                    String nombreCategoria = categoriaElement.getElementsByTagName("Nombre").item(0).getTextContent();
                                    String nombreCategoria = categoriaElement.getTextContent();

                                    // Crear un nuevo objeto Categoria y persistirlo en la base de datos
                                    Categoria categoria = new Categoria();
                                    categoria.setMod(mod);
                                    categoria.setNombre(nombreCategoria.replaceAll("\n", "").replaceAll("  ", ""));
                                    entityManager.persist(categoria);
                                }
                            }
                        }
                    }
                }
            }
            // Confirmar la transacción
            transaction.commit();

            // Imprimir un mensaje de éxito después de poblar las tablas con datos masivos
            System.out.println("\n" + "Tablas pobladas masivamente con éxito." + "\n");

        } catch (Exception e) {
            // Si se produce una excepción, se imprimirá el seguimiento de la pila para ayudar en la depuración
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }




    public void seleccionarConTexto() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione la tabla de la que desea eliminar el registro:");
        System.out.println("1. Juego");
        System.out.println("2. Mod");
        System.out.println("3. Detalle");
        System.out.println("4. Categoria");

        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        switch (opcionTabla) {
            case 1:
                seleccionarConTextoJuego();
                break;
            case 2:
                seleccionarConTextoMod();
                break;
            case 3:
                seleccionarConTextoDetalle();
                break;
            case 4:
                seleccionarConTextoCategoria();
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
        }
    }

    public void seleccionarConTextoJuego() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el texto a buscar en la descripción:");
        String texto = scanner.nextLine().trim();

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT j FROM Juego j WHERE j.descripcion LIKE :texto";
            Query query = entityManager.createQuery(jpql, Juego.class);
            query.setParameter("texto", "%" + texto + "%");
            List<Juego> juegos = query.getResultList();

            transaction.commit();

            if (!juegos.isEmpty()) {
                System.out.println("Resultados de la búsqueda:");
                for (Juego juego : juegos) {
                    System.out.println("JuegoID: " + juego.getJuegoID() +
                            ", Nombre: " + juego.getNombre() +
                            ", Descripcion: " + juego.getDescripcion());
                }
            } else {
                System.out.println("No se encontraron juegos que contengan el texto: " + texto);
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void seleccionarConTextoMod() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el texto a buscar en la descripción:");
        String texto = scanner.nextLine().trim();

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT m FROM Mod m WHERE m.descripcion LIKE :texto";
            Query query = entityManager.createQuery(jpql, Mod.class);
            query.setParameter("texto", "%" + texto + "%");
            List<Mod> mods = query.getResultList();

            transaction.commit();

            if (!mods.isEmpty()) {
                System.out.println("Resultados de la búsqueda:");
                for (Mod mod : mods) {
                    System.out.println("ModID: " + mod.getModID() +
                            ", Nombre: " + mod.getNombre() +
                            ", Autor: " + mod.getAutor() +
                            ", Descripcion: " + mod.getDescripcion());
                }
            } else {
                System.out.println("No se encontraron mods que contengan el texto: " + texto);
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void seleccionarConTextoDetalle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el texto a buscar en la descripción:");
        String texto = scanner.nextLine().trim();

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT d FROM Detalle d WHERE d.descripcion LIKE :texto";
            Query query = entityManager.createQuery(jpql, Detalle.class);
            query.setParameter("texto", "%" + texto + "%");
            List<Detalle> detalles = query.getResultList();

            transaction.commit();

            if (!detalles.isEmpty()) {
                System.out.println("Resultados de la búsqueda:");
                for (Detalle detalle : detalles) {
                    System.out.println("DetalleID: " + detalle.getDetalleID() +
                            ", ModID: " + detalle.getMod().getModID() +
                            ", Descripción: " + detalle.getDescripcion());
                }
            } else {
                System.out.println("No se encontraron detalles que contengan el texto: " + texto);
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void seleccionarConTextoCategoria() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el texto a buscar en el nombre:");
        String texto = scanner.nextLine().trim();

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT c FROM Categoria c WHERE c.nombre LIKE :texto";
            Query query = entityManager.createQuery(jpql, Categoria.class);
            query.setParameter("texto", "%" + texto + "%");
            List<Categoria> categorias = query.getResultList();

            transaction.commit();

            if (!categorias.isEmpty()) {
                System.out.println("Resultados de la búsqueda:");
                for (Categoria categoria : categorias) {
                    System.out.println("CategoriaID: " + categoria.getCategoriaID() +
                            ", ModID: " + categoria.getMod().getModID() +
                            ", Nombre: " + categoria.getNombre());
                }
            } else {
                System.out.println("No se encontraron categorías que contengan el texto: " + texto);
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }


    public void seleccionarElementosPorCondicion() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione la tabla de la que desea eliminar el registro:");
        System.out.println("1. Juego");
        System.out.println("2. Mod");
        System.out.println("3. Detalle");
        System.out.println("4. Categoria");

        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        switch (opcionTabla) {
            case 1:
                seleccionarElementosPorCondicionJuego();
                break;
            case 2:
                seleccionarElementosPorCondicionMod();
                break;
            case 3:
                seleccionarElementosPorCondicionDetalle();
                break;
            case 4:
                seleccionarElementosPorCondicionCategoria();
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
        }
    }

    public void seleccionarElementosPorCondicionJuego() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el valor para el atributo 'nombre':");
        String nombre = scanner.nextLine().trim();

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT j FROM Juego j WHERE j.nombre = :nombre";
            Query query = entityManager.createQuery(jpql, Juego.class);
            query.setParameter("nombre", nombre);
            List<Juego> juegos = query.getResultList();

            transaction.commit();

            if (!juegos.isEmpty()) {
                System.out.println("Resultados de la búsqueda:");
                for (Juego juego : juegos) {
                    System.out.println("JuegoID: " + juego.getJuegoID() +
                            ", Nombre: " + juego.getNombre() +
                            ", Descripcion: " + juego.getDescripcion());
                }
            } else {
                System.out.println("No se encontraron juegos con el nombre proporcionado: " + nombre);
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void seleccionarElementosPorCondicionMod() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el valor para el atributo 'nombre':");
        String nombre = scanner.nextLine().trim();

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT m FROM Mod m WHERE m.nombre = :nombre";
            Query query = entityManager.createQuery(jpql, Mod.class);
            query.setParameter("nombre", nombre);
            List<Mod> mods = query.getResultList();

            transaction.commit();

            if (!mods.isEmpty()) {
                System.out.println("Resultados de la búsqueda:");
                for (Mod mod : mods) {
                    System.out.println("ModID: " + mod.getModID() +
                            ", JuegoID: " + mod.getJuego().getJuegoID() +
                            ", Nombre: " + mod.getNombre() +
                            ", Autor: " + mod.getAutor() +
                            ", Descripcion: " + mod.getDescripcion());
                }
            } else {
                System.out.println("No se encontraron elementos con el nombre proporcionado: " + nombre);
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void seleccionarElementosPorCondicionDetalle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el valor para el atributo 'descripcion':");
        String descripcion = scanner.nextLine().trim();

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT d FROM Detalle d WHERE d.descripcion = :descripcion";
            Query query = entityManager.createQuery(jpql, Detalle.class);
            query.setParameter("descripcion", descripcion);
            List<Detalle> detalles = query.getResultList();

            transaction.commit();

            if (!detalles.isEmpty()) {
                System.out.println("Resultados de la búsqueda:");
                for (Detalle detalle : detalles) {
                    System.out.println("DetalleID: " + detalle.getDetalleID() +
                            ", ModID: " + detalle.getMod().getModID() +
                            ", Descripcion: " + detalle.getDescripcion());
                }
            } else {
                System.out.println("No se encontraron elementos con la descripción proporcionada: " + descripcion);
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void seleccionarElementosPorCondicionCategoria() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el valor para el atributo 'nombre':");
        String nombre = scanner.nextLine().trim();

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT c FROM Categoria c WHERE c.nombre = :nombre";
            Query query = entityManager.createQuery(jpql, Categoria.class);
            query.setParameter("nombre", nombre);
            List<Categoria> categorias = query.getResultList();

            transaction.commit();

            if (!categorias.isEmpty()) {
                System.out.println("Resultados de la búsqueda:");
                for (Categoria categoria : categorias) {
                    System.out.println("CategoriaID: " + categoria.getCategoriaID() +
                            ", ModID: " + categoria.getMod().getModID() +
                            ", Nombre: " + categoria.getNombre());
                }
            } else {
                System.out.println("No se encontraron elementos con el nombre proporcionado: " + nombre);
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }


    public void seleccionarElementosConcretos() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione la tabla de la que desea eliminar el registro:");
        System.out.println("1. Juego");
        System.out.println("2. Mod");
        System.out.println("3. Detalle");
        System.out.println("4. Categoria");

        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        switch (opcionTabla) {
            case 1:
                seleccionarElementosJuego();
                break;
            case 2:
                seleccionarElementosMod();
                break;
            case 3:
                seleccionarElementosDetalle();
                break;
            case 4:
                seleccionarElementosCategoria();
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
        }
    }

    public void seleccionarElementosJuego() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese los IDs de los juegos que desea seleccionar (separados por coma):");
        String input = scanner.nextLine().trim();
        String[] ids = input.split(",");

        List<Long> juegoIDs = new ArrayList<>();
        for (String id : ids) {
            juegoIDs.add(Long.parseLong(id.trim()));
        }

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT j FROM Juego j WHERE j.juegoID IN :ids";
            Query query = entityManager.createQuery(jpql, Juego.class);
            query.setParameter("ids", juegoIDs);
            List<Juego> juegos = query.getResultList();

            transaction.commit();

            if (!juegos.isEmpty()) {
                System.out.println("Elementos seleccionados:");
                for (Juego juego : juegos) {
                    System.out.println(juego);
                }
            } else {
                System.out.println("No se encontraron juegos con los IDs proporcionados.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void seleccionarElementosMod() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese los IDs de los mods que desea seleccionar (separados por coma):");
        String input = scanner.nextLine().trim();
        String[] ids = input.split(",");

        List<Long> modIDs = new ArrayList<>();
        for (String id : ids) {
            modIDs.add(Long.parseLong(id.trim()));
        }

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT m FROM Mod m WHERE m.modID IN :ids";
            Query query = entityManager.createQuery(jpql, Mod.class);
            query.setParameter("ids", modIDs);
            List<Mod> mods = query.getResultList();

            transaction.commit();

            if (!mods.isEmpty()) {
                System.out.println("Elementos seleccionados:");
                for (Mod mod : mods) {
                    System.out.println(mod);
                }
            } else {
                System.out.println("No se encontraron mods con los IDs proporcionados.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void seleccionarElementosDetalle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese los IDs de los detalles que desea seleccionar (separados por coma):");
        String input = scanner.nextLine().trim();
        String[] ids = input.split(",");

        List<Long> detalleIDs = new ArrayList<>();
        for (String id : ids) {
            detalleIDs.add(Long.parseLong(id.trim()));
        }

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT d FROM Detalle d WHERE d.detalleID IN :ids";
            Query query = entityManager.createQuery(jpql, Detalle.class);
            query.setParameter("ids", detalleIDs);
            List<Detalle> detalles = query.getResultList();

            transaction.commit();

            if (!detalles.isEmpty()) {
                System.out.println("Elementos seleccionados:");
                for (Detalle detalle : detalles) {
                    System.out.println(detalle);
                }
            } else {
                System.out.println("No se encontraron detalles con los IDs proporcionados.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void seleccionarElementosCategoria() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese los IDs de las categorías que desea seleccionar (separados por coma):");
        String input = scanner.nextLine().trim();
        String[] ids = input.split(",");

        List<Long> categoriaIDs = new ArrayList<>();
        for (String id : ids) {
            categoriaIDs.add(Long.parseLong(id.trim()));
        }

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "SELECT c FROM Categoria c WHERE c.categoriaID IN :ids";
            Query query = entityManager.createQuery(jpql, Categoria.class);
            query.setParameter("ids", categoriaIDs);
            List<Categoria> categorias = query.getResultList();

            transaction.commit();

            if (!categorias.isEmpty()) {
                System.out.println("Elementos seleccionados:");
                for (Categoria categoria : categorias) {
                    System.out.println(categoria);
                }
            } else {
                System.out.println("No se encontraron categorías con los IDs proporcionados.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }




    public void modificarRegistro() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione la tabla que desea modificar:");
        System.out.println("1. Juego");
        System.out.println("2. Mod");
        System.out.println("3. Detalle");
        System.out.println("4. Categoria");

        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        switch (opcionTabla) {
            case 1:
                modificarJuego();
                break;
            case 2:
                modificarMod();
                break;
            case 3:
                modificarDetalle();
                break;
            case 4:
                modificarCategoria();
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
        }
    }

    private void modificarJuego() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("La tabla Juego tiene los siguientes atributos: JuegoID, Nombre, Descripcion");

        System.out.println("Ingrese el ID del juego que desea modificar:");
        Long idRegistro = Long.parseLong(scanner.nextLine().trim());

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Juego juego = entityManager.find(Juego.class, idRegistro);

            if (juego != null) {
                System.out.println("Información actual del juego:");
                System.out.println(juego.toString());

                System.out.println("\nIngrese los nuevos valores (presione Enter para mantener el valor actual):");

                System.out.print("Nombre: ");
                String nuevoNombre = scanner.nextLine().trim();
                if (!nuevoNombre.isEmpty()) {
                    juego.setNombre(nuevoNombre);
                }

                System.out.print("Descripcion: ");
                String nuevaDescripcion = scanner.nextLine().trim();
                if (!nuevaDescripcion.isEmpty()) {
                    juego.setDescripcion(nuevaDescripcion);
                }

                entityManager.merge(juego);
                transaction.commit();
                System.out.println("\nJuego modificado exitosamente.");
            } else {
                System.out.println("El juego con ID " + idRegistro + " no existe.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

        public void modificarMod() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("La tabla Mod tiene los siguientes atributos: ModID, JuegoID, Nombre, Autor, Descripcion");

        System.out.println("Ingrese el ID del registro que desea modificar:");
        Long idRegistro = Long.parseLong(scanner.nextLine().trim());

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Mod mod = entityManager.find(Mod.class, idRegistro);

            if (mod != null) {
                System.out.println("Información actual del registro:");
                System.out.println(mod.toString());

                System.out.println("\nIngrese los nuevos valores (presione Enter para mantener el valor actual):");

                System.out.print("Nombre: ");
                String nuevoNombre = scanner.nextLine().trim();
                if (!nuevoNombre.isEmpty()) {
                    mod.setNombre(nuevoNombre);
                }

                System.out.print("Autor: ");
                String nuevoAutor = scanner.nextLine().trim();
                if (!nuevoAutor.isEmpty()) {
                    mod.setAutor(nuevoAutor);
                }

                System.out.print("Descripcion: ");
                String nuevaDescripcion = scanner.nextLine().trim();
                if (!nuevaDescripcion.isEmpty()) {
                    mod.setDescripcion(nuevaDescripcion);
                }

                entityManager.merge(mod);
                transaction.commit();
                System.out.println("\nRegistro modificado exitosamente.");
            } else {
                System.out.println("El registro con ID " + idRegistro + " no existe en la tabla Mod.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void modificarDetalle() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("La tabla Detalle tiene los siguientes atributos: DetalleID, ModID, Descripcion");

        System.out.println("Ingrese el ID del detalle que desea modificar:");
        Long idRegistro = Long.parseLong(scanner.nextLine().trim());

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Detalle detalle = entityManager.find(Detalle.class, idRegistro);

            if (detalle != null) {
                System.out.println("Información actual del detalle:");
                System.out.println(detalle.toString());

                System.out.println("\nIngrese los nuevos valores (presione Enter para mantener el valor actual):");

                System.out.print("Descripción: ");
                String nuevaDescripcion = scanner.nextLine().trim();
                if (!nuevaDescripcion.isEmpty()) {
                    detalle.setDescripcion(nuevaDescripcion);
                }

                entityManager.merge(detalle);
                transaction.commit();
                System.out.println("\nDetalle modificado exitosamente.");
            } else {
                System.out.println("El detalle con ID " + idRegistro + " no existe.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void modificarCategoria() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("La tabla Categoria tiene los siguientes atributos: CategoriaID, ModID, Nombre");

        System.out.println("Ingrese el ID de la categoría que desea modificar:");
        Long idRegistro = Long.parseLong(scanner.nextLine().trim());

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Categoria categoria = entityManager.find(Categoria.class, idRegistro);

            if (categoria != null) {
                System.out.println("Información actual de la categoría:");
                System.out.println(categoria.toString());

                System.out.println("\nIngrese los nuevos valores (presione Enter para mantener el valor actual):");

                System.out.print("Nombre: ");
                String nuevoNombre = scanner.nextLine().trim();
                if (!nuevoNombre.isEmpty()) {
                    categoria.setNombre(nuevoNombre);
                }

                entityManager.merge(categoria);
                transaction.commit();
                System.out.println("\nCategoría modificada exitosamente.");
            } else {
                System.out.println("La categoría con ID " + idRegistro + " no existe.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }


    public void modificarRegistros() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione la tabla que desea modificar:");
        System.out.println("1. Juego");
        System.out.println("2. Mod");
        System.out.println("3. Detalle");
        System.out.println("4. Categoria");

        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        switch (opcionTabla) {
            case 1:
                modificarRegistrosJuego();
                break;
            case 2:
                modificarRegistrosMod();
                break;
            case 3:
                modificarRegistrosDetalle();
                break;
            case 4:
                modificarRegistrosCategoria();
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
        }
    }

    private void modificarRegistrosJuego() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("La tabla Juego tiene los siguientes atributos: JuegoID, Nombre, Descripcion");

        System.out.print("Ingrese los IDs de los juegos que desea modificar (separados por coma): ");
        String input = scanner.nextLine().trim();
        String[] ids = input.split(",");

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            for (String idStr : ids) {
                Long idRegistro = Long.parseLong(idStr.trim());
                Juego juego = entityManager.find(Juego.class, idRegistro);

                if (juego != null) {
                    System.out.println("Información actual del juego con ID " + idRegistro + ":");
                    System.out.println(juego.toString());

                    System.out.println("\nIngrese los nuevos valores para el juego con ID " + idRegistro + " (presione Enter para mantener el valor actual):");

                    System.out.print("Nombre: ");
                    String nuevoNombre = scanner.nextLine().trim();
                    if (!nuevoNombre.isEmpty()) {
                        juego.setNombre(nuevoNombre);
                    }

                    System.out.print("Descripcion: ");
                    String nuevaDescripcion = scanner.nextLine().trim();
                    if (!nuevaDescripcion.isEmpty()) {
                        juego.setDescripcion(nuevaDescripcion);
                    }

                    entityManager.merge(juego);
                    System.out.println("Juego con ID " + idRegistro + " modificado exitosamente.");
                } else {
                    System.out.println("El juego con ID " + idRegistro + " no existe.");
                }
            }

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void modificarRegistrosMod() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("La tabla Mod tiene los siguientes atributos: ModID, JuegoID, Nombre, Autor, Descripcion");

        System.out.print("Ingrese los IDs de los registros que desea modificar (separados por coma): ");
        String input = scanner.nextLine().trim();
        String[] ids = input.split(",");

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            for (String idStr : ids) {
                Long idRegistro = Long.parseLong(idStr.trim());
                Mod mod = entityManager.find(Mod.class, idRegistro);

                if (mod != null) {
                    System.out.println("Información actual del registro con ID " + idRegistro + ":");
                    System.out.println(mod.toString());

                    System.out.println("\nIngrese los nuevos valores para el registro con ID " + idRegistro + " (presione Enter para mantener el valor actual):");

                    System.out.print("Nombre: ");
                    String nuevoNombre = scanner.nextLine().trim();
                    if (!nuevoNombre.isEmpty()) {
                        mod.setNombre(nuevoNombre);
                    }

                    System.out.print("Autor: ");
                    String nuevoAutor = scanner.nextLine().trim();
                    if (!nuevoAutor.isEmpty()) {
                        mod.setAutor(nuevoAutor);
                    }

                    System.out.print("Descripcion: ");
                    String nuevaDescripcion = scanner.nextLine().trim();
                    if (!nuevaDescripcion.isEmpty()) {
                        mod.setDescripcion(nuevaDescripcion);
                    }

                    entityManager.merge(mod);
                    System.out.println("Registro con ID " + idRegistro + " modificado exitosamente.");
                } else {
                    System.out.println("El registro con ID " + idRegistro + " no existe en la tabla Mod.");
                }
            }

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void modificarRegistrosDetalle() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("La tabla Detalle tiene los siguientes atributos: DetalleID, ModID, Descripcion");

        System.out.print("Ingrese los IDs de los detalles que desea modificar (separados por coma): ");
        String input = scanner.nextLine().trim();
        String[] ids = input.split(",");

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            for (String idStr : ids) {
                Long idRegistro = Long.parseLong(idStr.trim());
                Detalle detalle = entityManager.find(Detalle.class, idRegistro);

                if (detalle != null) {
                    System.out.println("Información actual del detalle con ID " + idRegistro + ":");
                    System.out.println(detalle.toString());

                    System.out.println("\nIngrese los nuevos valores para el detalle con ID " + idRegistro + " (presione Enter para mantener el valor actual):");

                    System.out.print("Descripción: ");
                    String nuevaDescripcion = scanner.nextLine().trim();
                    if (!nuevaDescripcion.isEmpty()) {
                        detalle.setDescripcion(nuevaDescripcion);
                    }

                    entityManager.merge(detalle);
                    System.out.println("Detalle con ID " + idRegistro + " modificado exitosamente.");
                } else {
                    System.out.println("El detalle con ID " + idRegistro + " no existe en la tabla Detalle.");
                }
            }

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void modificarRegistrosCategoria() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("La tabla Categoria tiene los siguientes atributos: CategoriaID, ModID, Nombre");

        System.out.print("Ingrese los IDs de las categorías que desea modificar (separados por coma): ");
        String input = scanner.nextLine().trim();
        String[] ids = input.split(",");

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            for (String idStr : ids) {
                Long idRegistro = Long.parseLong(idStr.trim());
                Categoria categoria = entityManager.find(Categoria.class, idRegistro);

                if (categoria != null) {
                    System.out.println("Información actual de la categoría con ID " + idRegistro + ":");
                    System.out.println(categoria.toString());

                    System.out.println("\nIngrese los nuevos valores para la categoría con ID " + idRegistro + " (presione Enter para mantener el valor actual):");

                    System.out.print("Nombre: ");
                    String nuevoNombre = scanner.nextLine().trim();
                    if (!nuevoNombre.isEmpty()) {
                        categoria.setNombre(nuevoNombre);
                    }

                    entityManager.merge(categoria);
                    System.out.println("Categoría con ID " + idRegistro + " modificada exitosamente.");
                } else {
                    System.out.println("La categoría con ID " + idRegistro + " no existe en la tabla Categoria.");
                }
            }

            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }




    public void eliminarRegistro() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione la tabla de la que desea eliminar el registro:");
        System.out.println("1. Juego");
        System.out.println("2. Mod");
        System.out.println("3. Detalle");
        System.out.println("4. Categoria");

        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        switch (opcionTabla) {
            case 1:
                eliminarJuego();
                break;
            case 2:
                eliminarMod();
                break;
            case 3:
                eliminarDetalle();
                break;
            case 4:
                eliminarCategoria();
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
        }
    }

    private void eliminarJuego() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el ID del juego que desea eliminar:");
        Long idRegistro = Long.parseLong(scanner.nextLine().trim());

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Juego juego = entityManager.find(Juego.class, idRegistro);

            if (juego != null) {
                entityManager.remove(juego);
                transaction.commit();
                System.out.println("\nJuego eliminado exitosamente.");
            } else {
                System.out.println("El juego con ID " + idRegistro + " no existe.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void eliminarMod() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el ID del registro que desea eliminar:");
        Long idRegistro = Long.parseLong(scanner.nextLine().trim());

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Mod mod = entityManager.find(Mod.class, idRegistro);

            if (mod != null) {
                entityManager.remove(mod);
                transaction.commit();
                System.out.println("\nRegistro eliminado exitosamente.");
            } else {
                System.out.println("El registro con ID " + idRegistro + " no existe en la tabla Mod.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void eliminarDetalle() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el ID del detalle que desea eliminar:");
        Long idRegistro = Long.parseLong(scanner.nextLine().trim());

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Detalle detalle = entityManager.find(Detalle.class, idRegistro);

            if (detalle != null) {
                entityManager.remove(detalle);
                transaction.commit();
                System.out.println("\nDetalle eliminado exitosamente.");
            } else {
                System.out.println("El detalle con ID " + idRegistro + " no existe.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void eliminarCategoria() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el ID de la categoría que desea eliminar:");
        Long idRegistro = Long.parseLong(scanner.nextLine().trim());

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Categoria categoria = entityManager.find(Categoria.class, idRegistro);

            if (categoria != null) {
                entityManager.remove(categoria);
                transaction.commit();
                System.out.println("\nCategoría eliminada exitosamente.");
            } else {
                System.out.println("La categoría con ID " + idRegistro + " no existe.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }


    public void eliminarRegistros() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione la tabla de la que desea eliminar registros:");
        System.out.println("1. Juego");
        System.out.println("2. Mod");
        System.out.println("3. Detalle");
        System.out.println("4. Categoria");

        int opcionTabla = Integer.parseInt(scanner.nextLine().trim());

        switch (opcionTabla) {
            case 1:
                eliminarRegistrosJuego();
                break;
            case 2:
                eliminarRegistrosMod();
                break;
            case 3:
                eliminarRegistrosDetalle();
                break;
            case 4:
                eliminarRegistrosCategoria();
                break;
            default:
                System.out.println("La opción ingresada no es válida.");
        }
    }

    private void eliminarRegistrosJuego() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los IDs de los juegos que desea eliminar (separados por comas):");
        String[] ids = scanner.nextLine().trim().split(",");

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            for (String idStr : ids) {
                Long id = Long.parseLong(idStr.trim());
                Juego juego = entityManager.find(Juego.class, id);
                if (juego != null) {
                    entityManager.remove(juego);
                    System.out.println("Juego con ID " + id + " eliminado exitosamente.");
                } else {
                    System.out.println("El juego con ID " + id + " no existe.");
                }
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void eliminarRegistrosMod() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los IDs de los registros Mod que desea eliminar (separados por comas):");
        String[] ids = scanner.nextLine().trim().split(",");

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            for (String idStr : ids) {
                Long id = Long.parseLong(idStr.trim());
                Mod mod = entityManager.find(Mod.class, id);
                if (mod != null) {
                    entityManager.remove(mod);
                    System.out.println("Registro Mod con ID " + id + " eliminado exitosamente.");
                } else {
                    System.out.println("El registro Mod con ID " + id + " no existe.");
                }
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void eliminarRegistrosDetalle() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los IDs de los registros Detalle que desea eliminar (separados por comas):");
        String[] ids = scanner.nextLine().trim().split(",");

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            for (String idStr : ids) {
                Long id = Long.parseLong(idStr.trim());
                Detalle detalle = entityManager.find(Detalle.class, id);
                if (detalle != null) {
                    entityManager.remove(detalle);
                    System.out.println("Registro Detalle con ID " + id + " eliminado exitosamente.");
                } else {
                    System.out.println("El registro Detalle con ID " + id + " no existe.");
                }
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    private void eliminarRegistrosCategoria() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los IDs de los registros Categoria que desea eliminar (separados por comas):");
        String[] ids = scanner.nextLine().trim().split(",");

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            for (String idStr : ids) {
                Long id = Long.parseLong(idStr.trim());
                Categoria categoria = entityManager.find(Categoria.class, id);
                if (categoria != null) {
                    entityManager.remove(categoria);
                    System.out.println("Registro Categoria con ID " + id + " eliminado exitosamente.");
                } else {
                    System.out.println("El registro Categoria con ID " + id + " no existe.");
                }
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }
}