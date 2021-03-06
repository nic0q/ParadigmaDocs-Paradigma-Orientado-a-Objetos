package ParadigmaDocs_View;

import ParadigmaDocs_Controller.*;
import java.util.Scanner;

/**
 * Menu
 */
public class Menu {
  private Controller controller;

  public Menu(Controller controller) {
    this.controller = controller;
  }

  public void setController(Controller controller) {
    this.controller = controller;
  }

  public void menu() {
    Scanner scan = new Scanner(System.in);
    boolean exit = false;
    int select;
    String username;
    String password;
    Controller controller = this.controller;
    while (!exit) {
      if (!controller.isLogeado()) {
        System.out.println("### EDITOR COLABORATIVO ###");
        System.out.println("Escoja la opcion que desea realizar: ");
        System.out.println("1. Registrarse");
        System.out.println("2. Logearse");
        System.out.println("3. Visualizar Plataforma");
        System.out.println("4. Salir");
        try {
          System.out.println("Ingrese la opcion: ");
          select = scan.nextInt();
          switch (select) {
            case 1:
              System.out.println("* * * * * Register * * * * *");
              System.out.println("Ingrese username");
              scan.nextLine();
              username = scan.nextLine();
              System.out.println("Ingrese password");
              password = scan.nextLine();
              controller.register(username, password);
              break;
            case 2:
              System.out.println("* * * * * Login * * * * *");
              System.out.println("Ingrese username");
              scan.nextLine();
              username = scan.nextLine();
              System.out.println("Ingrese password");
              password = scan.nextLine();
              controller.login(username, password);
              break;
            case 3:
              System.out.println(controller.editorToString());
              break;
            case 4:
              exit = true;
              break;
            default:
              break;
          }
        } catch (Exception e) {
          System.out.println("Ingrese solo numeros");
          scan.next();
        }
      } else {
        System.out.println("### EDITOR COLABORATIVO ###");
        System.out.println("## Registrado como: " + controller.getLogeado().getUsername() + " ##");
        System.out.println("1.  Crear nuevo documento");
        System.out.println("2.  Compartir documento [write, read, comment]");
        System.out.println("3.  Agregar contenido a un documento");
        System.out.println("4.  Restaurar version de un documento");
        System.out.println("5.  Revocar acceso a un documento");
        System.out.println("6.  Buscar en los documentos");
        System.out.println("7.  Visualizar documentos");
        System.out.println("8.  Eliminar ultimos caracteres documento");
        System.out.println("9.  Buscar y reemplazar texto");
        System.out.println("10. Aplicar Estilos [italic, bold, underlined]");
        System.out.println("11. Agregar comentario a documento");
        System.out.println("12. Cerrar Sesion");
        System.out.println("13. Cerrar el programa");
        try {
          select = scan.nextInt();
          switch (select) {
            case 1:
              System.out.println(" * * * * * Create * * * * *");
              System.out.println("Ingrese titulo del nuevo documento");
              scan.nextLine();
              String titulo = scan.nextLine();
              System.out.println("Ingrese contenido del nuevo documento");
              String contenido = scan.nextLine();
              controller.create(titulo, controller.getLogeado(), contenido);
              break;
            case 2:
              String users = "";
              System.out.println("* * * * * Share * * * * *");
              System.out.println("Ingrese el ID del documento al que quiere conceder permisos");
              Integer id = scan.nextInt();
              System.out.println("Ingrese los usuarios separados por espacio");
              scan.nextLine();
              users += scan.nextLine();
              System.out.println(
                  "Ingrese permiso del tipo:\n[w]: escritura\n[r]: lectura\n[c]: comentarios");
              String permiso = scan.nextLine();
              String[] usuariosPermiso = users.split(" ");
              controller.share(controller.getLogeado(), id, usuariosPermiso, permiso);
              break;
            case 3:
              System.out.println("* * * * * Add * * * * *");
              System.out.println("Ingrese el ID");
              Integer idDoc = scan.nextInt();
              System.out.println("Ingrese contenido");
              scan.nextLine();
              String text = scan.nextLine();
              controller.add(controller.getLogeado(), idDoc, text);
              break;
            case 4:
              System.out.println("* * * * * Rollback * * * * *");
              System.out.println("Ingrese ID documento");
              Integer idDocc = scan.nextInt();
              System.out.println("Ingrese id version");
              Integer idVer = scan.nextInt();
              controller.rollback(controller.getLogeado(), idDocc, idVer);
              break;
            case 5:
              System.out.println("* * * * * Revoke Access * * * * *");
              System.out.println("Ingrese ID documento");
              Integer idDc = scan.nextInt();
              controller.revokeAccess(controller.getLogeado(), idDc);
              break;
            case 6:
              System.out.println("* * * * * Search * * * * *");
              System.out.println("Ingrese texto a buscar");
              scan.nextLine();
              String searchText = scan.nextLine();
              controller.search(controller.getLogeado(), searchText);
              break;
            case 7:
              System.out.println(controller.editorStringLogeado(controller.getLogeado()));
              break;
            case 8:
              System.out.println("* * * * * Delete * * * * *");
              System.out.println("Ingrese el ID");
              Integer iDoc = scan.nextInt();
              System.out.println("Ingrese el numero de caracteres a eliminar");
              int deleteC = scan.nextInt();
              controller.delete(controller.getLogeado(), iDoc, deleteC);
              break;
            case 9:
              System.out.println("* * * * * Search and Replace * * * * *");
              System.out.println("Ingrese ID documento");
              Integer idRep = scan.nextInt();
              scan.nextLine();
              System.out.println("Ingrese el texto buscado");
              String sText = scan.nextLine();
              System.out.println("Ingrese el texto a reemplazar");
              String repText = scan.nextLine();
              controller.searchAndReplace(controller.getLogeado(), idRep, sText, repText);
              break;
            case 10:
              String styls = "";
              System.out.println("* * * * * Apply Styles * * * * *");
              System.out.println("Ingrese ID documento");
              Integer idAp = scan.nextInt();
              scan.nextLine();
              System.out.println("Ingrese el texto buscado");
              String saText = scan.nextLine();
              System.out.println("Ingrese los estilos del tipo [#\\i, #\\u #\\b] separados por espacio [_]");
              styls += scan.nextLine();
              String[] styles = styls.split(" ");
              controller.applyStyles(controller.getLogeado(), idAp, saText, styles);
              break;
            case 11:
              System.out.println("* * * * * Comment * * * * *");
              System.out.println("Ingrese ID documento");
              Integer idR = scan.nextInt();
              scan.nextLine();
              System.out.println("Ingrese el texto a comentar");
              String cText = scan.nextLine();
              System.out.println("Ingrese el comentario");
              String rText = scan.nextLine();
              controller.comment(controller.getLogeado(), idR, cText, rText);
              break;
            case 12:
              controller.logout();
              break;
            case 13:
              exit = true;
              scan.close();
              break;
            default:
              System.out.println("Seleccione solo las opciones disponibles");
              break;
          }
        } catch (Exception e) {
          System.out.println("Ingrese solo numeros");
          scan.next();
        }
      }
    }
  }
}