package ParadigmaDocs_Controller;

import ParadigmaDocs_Model.*;

/**
 * Controller
 * Clase donde se crean los métodos funcionales de ParadigmaDocs
 *
 * @author Nícolas Farfán Cheneaux
 */
public class Controller {
  // Atributos
  private Editor paradigmaDocs;

  // Constructor
  /**
   * @param paradigmaDocs instancia de la clase editor
   */
  public Controller(Editor paradigmaDocs) {
    this.paradigmaDocs = paradigmaDocs;
  }

  // Getter
  /**
   * @return ParadigmaDocs la plataforma
   */
  public Editor getParadigmaDocs() {
    return paradigmaDocs;
  }

  // Métodos
  /**
   * @return Usuario
   */
  public Usuario getLogeado() {
    Editor pDocs = getParadigmaDocs();
    return pDocs.getLogeado();

  }

  public boolean isLogeado() {
    Editor pDocs = getParadigmaDocs();
    return pDocs.isConectado();
  }
  // Métodos

  /**
   * Método que permite registrar un nuevo usuario en paradigmaDocs
   *
   * @param username El username del usurio
   * @param password La contraseña del usuario
   */
  public void register(String username, String password) {
    Editor pDocs = getParadigmaDocs();
    for (int i = 0; i < pDocs.getUsuarios().size(); i++) {
      if (pDocs.getUsuarios().get(i).getUsername().equals(username)) {
        System.out.println("El username " + username + " ya existe");
        return;
      }
    }
    Usuario nuevoUsuario = new Usuario(username, password);
    pDocs.getUsuarios().add(nuevoUsuario);
  }

  /**
   * Método que permite iniciar sesión a un usuario previamente registrado
   *
   * @param username El username del usurio
   * @param password La contraseña del usuario
   */
  public void login(String username, String password) {
    Editor pDocs = getParadigmaDocs();
    for (int i = 0; i < pDocs.getUsuarios().size(); i++) {
      if (pDocs.getUsuarios().get(i).getUsername().equals(username)
          && pDocs.getUsuarios().get(i).getPassword().equals(password)) {
        pDocs.setLogeado(pDocs.getUsuarios().get(i));
        pDocs.setConectado(true);
        System.out.println("Logeo Exitoso");
        return;
      }
    }
    System.out.println("Usuario no registrado");
  }

  /**
   * Método que permite cerrar sesión al usuario que tiene iniciada sesión
   */
  public void logout() {
    Editor pDocs = getParadigmaDocs();
    pDocs.setConectado(false);
  }

  /**
   * Método que permite crear un documento en la plataforma a un usuario
   *
   * @param titulo    Titulo del documento
   * @param logeado   Usuario logeado
   * @param contenido Contenido del documento a crear
   */
  public void create(String titulo, Usuario logeado, String contenido) {
    Editor pDocs = getParadigmaDocs();
    Documento doc = new Documento(titulo, contenido, logeado);
    logeado.getDocsCreados().add(doc);
    pDocs.getDocumentos().add(doc);
    System.out.println("Documento creado con exito");
  }

  /**
   * Método que permite compartir un documento al dueño de este
   *
   * @param logeado         Usuario logeado
   * @param id              Id del documento
   * @param usuariosPermiso lista usuarios que se quiere dar el permiso
   * @param access          permiso otorgado [w|r|c]
   */
  public void share(Usuario logeado, Integer id, String[] usuariosPermiso, String access) {
    Editor pDocs = getParadigmaDocs();
    // Se comprueban los permisos
    if (!(access.equals("w") || access.equals("c") || access.equals("r"))) {
      System.out.println("El permiso ingresado no es del tipo [w, r, c]");
      return;
    }
    for (int i = 0; i < logeado.getDocsCreados().size(); i++) {
      if (logeado.getDocsCreados().get(i).getId() == id) {
        for (int k = 0; k < usuariosPermiso.length; k++) {
          for (int j = 0; j < pDocs.getUsuarios().size(); j++) {
            if (pDocs.getUsuarios().get(j).getUsername().equals(usuariosPermiso[k])
                && !usuariosPermiso[k].equals(logeado.getUsername())) {
              // Se busca si ya tiene acceso
              for (int l = 0; l < logeado.getDocsCreados().get(i).getAccesses().size(); l++) {
                if (logeado.getDocsCreados().get(i).getAccesses().get(l).getUser().getUsername()
                    .equals(usuariosPermiso[k])) {
                  logeado.getDocsCreados().get(i).getAccesses().remove(l);
                }
              }
              Acceso acceso = new Acceso(pDocs.getUsuarios().get(j), access);
              logeado.getDocsCreados().get(i).getAccesses().add(acceso);
              pDocs.getUsuarios().get(j).getDocsAcceso().add(logeado.getDocsCreados().get(i));
              System.out.println("Permiso [" + access + "] concedido a " + usuariosPermiso[k]);
            }
          }
        }
      }
    }
  }

  /**
   * Método que permite añadir texto a una version del documento, se requiere
   * permiso de edición.
   *
   * @param logeado   Usuario logeado
   * @param id        Id del documento
   * @param contenido Contenido del documento
   */

  public void add(Usuario logeado, Integer id, String contenido) {
    if (getIndexDocCreados(id, logeado) != -1) {
      String contenidoAnt = logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getContenido();
      String contenidoMod = contenidoAnt.concat(contenido);
      Version version = new Version(contenidoMod);
      version.setId(logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getHistorial().size());
      logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getHistorial().add(version);
      logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).setContenido(contenidoMod);
      System.out.println("Se agrego " + contenido + " en documento id(" + id + ")");
      return;
    }
    if (isEditor(logeado, id)) {
      String contenidoAnta = logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).getContenido();
      String contenidoModi = contenidoAnta.concat(contenido);
      Version version = new Version(contenidoModi);
      version.setId(logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).getHistorial().size());
      logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).getHistorial().add(version);
      logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).setContenido(contenidoModi);
      System.out.println("Se agrego " + contenido + " en documento id(" + id + ")");
      return;
    }
    System.out.println("Operacion no exitosa");
    return;
  }

  /**
   * Método que permite restaurar una version anterior del documento, es decir
   * esta pasa a ser la versión activa
   *
   * @param logeado Usuario logeado
   * @param id      Id del documento
   * @param idVer   Id de la version
   */
  public void rollback(Usuario logeado, Integer id, Integer idVer) {
    int index = getIndexDocCreados(id, logeado);
    if (index == -1) {
      System.out.println("Documento no existe");
      return;
    }
    for (int i = 0; i < logeado.getDocsCreados().get(index).getHistorial().size(); i++) {
      if (logeado.getDocsCreados().get(index).getHistorial().get(i).getId().equals(idVer)) {
        String contenido = logeado.getDocsCreados().get(index).getHistorial().get(i).getContenido();
        logeado.getDocsCreados().get(index).setContenido(contenido);
        System.out.println("Versión restaurada correctamente");
        return;
      }
    }
    System.out.println("No se hizo rollback"); // Pendiente: Verificar si esto vale la pena o no
    return;
  }

  /**
   * Método que permite revocar todos los accesos a un documento
   *
   * @param logeado Usuario logeado
   * @param id      Id del documento
   */
  public void revokeAccess(Usuario logeado, Integer id) {
    int index = getIndexDocCreados(id, logeado);
    if (index == -1) {
      System.out.println("Documento no existe");
      return;
    }
    logeado.getDocsCreados().get(index).getAccesses().clear();
    ;
    System.out.println("Acccesos revocados del documento (" + id + ") Satisfactorio");
  }

  /**
   * Método que permite buscar texto en un documento propio o compartido
   *
   * @param logeado    Usuario logeado
   * @param searchText Texto a buscar
   */

  // Visualize:

  public void search(Usuario logeado, String searchText) {
    int found = 0;
    for (int i = 0; i < logeado.getDocsCreados().size(); i++) {
      for (int j = 0; j < logeado.getDocsCreados().get(i).getHistorial().size(); j++) {
        if (logeado.getDocsCreados().get(i).getHistorial().get(j).getContenido().contains(searchText)) {
          System.out.println("id(" + logeado.getDocsCreados().get(i).getId() + ") " + logeado.getDocsCreados().get(i)
              .getTitulo());
          found = 1;
        }
      }
    }
    for (int i = 0; i < logeado.getDocsAcceso().size(); i++) {
      for (int j = 0; j < logeado.getDocsAcceso().get(i).getHistorial().size(); j++) {
        if (logeado.getDocsAcceso().get(i).getHistorial().get(j).getContenido().contains(searchText)) {
          System.out.println("id(" + logeado.getDocsAcceso().get(i).getId() + ") " + logeado.getDocsAcceso().get(i)
              .getTitulo());
          found = 1;
        }
      }
    }
    if (found != 1) {
      System.out.println("No se ha encontrado texto");
      return;
    }
    return;
  }

  /**
   * Método que convierte toda la plataforma de paradigmadocs a String
   *
   * @return StringBuilder
   */
  public StringBuilder editorToString() {
    StringBuilder string = new StringBuilder();
    string.append("* * * * * * * * * * ParadigmaDocs * * * * * * * * * *\n");
    string.append("-> Documentos:");
    for (int j = 0; j < paradigmaDocs.getDocumentos().size(); j++) {
      string.append("\n\t* * * * * " + paradigmaDocs.getDocumentos().get(j).getTitulo() + " id("
          + paradigmaDocs.getDocumentos().get(j).getId() + ") * * * * * ");
      int n_versiones = paradigmaDocs.getDocumentos().get(j).getHistorial().size();
      string.append("\nCreado por " + paradigmaDocs.getDocumentos().get(j).getAutor().getUsername() + " el ["
          + paradigmaDocs.getDocumentos().get(j).getFecha() + "]" + "\nNumero de versiones ["
          + n_versiones + "]\n");
      for (int k = 0; k < paradigmaDocs.getDocumentos().get(j).getHistorial().get(n_versiones - 1).getComentarios()
          .size(); k++) {
        string.append(paradigmaDocs.getDocumentos().get(j).getHistorial().get(n_versiones - 1).getComentarios().get(k)
            .toString());
      }
    }
    return string;
  }

  /**
   * Metodo que convierte a string todos los documentos creados o compartidos a un
   * String ententidble por el usuario
   *
   * @param logeado Usuario logeado
   * @return StringBuilder
   */
  public StringBuilder editorStringLogeado(Usuario logeado) {
    StringBuilder editorString = new StringBuilder();
    editorString.append("* * * * * * * * * * " + logeado.getUsername() + " * * * * * * * * * *\n" + "Miembro desde ["
        + logeado.getFecha() + "]");
    for (int i = 0; i < logeado.getDocsCreados().size(); i++) { // String Version Activa
      editorString.append("\n\t* * * * * " + logeado.getDocsCreados().get(i).getTitulo() + " id("
          + logeado.getDocsCreados().get(i).getId() + ") * * * * * \n"
          + logeado.getDocsCreados().get(i).getContenido() + "\nNumero de versiones ["
          + logeado.getDocsCreados().get(i).getHistorial().size() + "]");
      editorString.append("\n-> Permisos");
      for (int j = 0; j < logeado.getDocsCreados().get(i).getAccesses().size(); j++) {
        editorString.append("\n\t" + logeado.getDocsCreados().get(i).getAccesses().get(j).getUser().getUsername()
            + " -> " + logeado.getDocsCreados().get(i).getAccesses().get(j).accessToString());
      }
      int n_versiones = logeado.getDocsCreados().get(i).getHistorial().size();
      for (int k = 0; k < logeado.getDocsCreados().get(i).getHistorial().get(n_versiones - 1).getComentarios()
          .size(); k++) {
        editorString.append(logeado.getDocsCreados().get(i).getHistorial().get(n_versiones - 1).getComentarios().get(k)
            .toString());
      }
    }
    for (int i = 0; i < logeado.getDocsAcceso().size(); i++) {// String Version Activa
      editorString
          .append("\n\t* * * * * " + logeado.getDocsAcceso().get(i).getTitulo() + " id("
              + logeado.getDocsAcceso().get(i).getId() + ") * * * * * " + "\nAutor: "
              + logeado.getDocsAcceso().get(i).getAutor().getUsername() + "\n"
              + logeado.getDocsAcceso().get(i).getContenido() + "\nNumero de versiones ["
              + logeado.getDocsAcceso().get(i).getHistorial().size() + "]\n");
      int n_versiones = logeado.getDocsAcceso().get(i).getHistorial().size();
      for (int k = 0; k < logeado.getDocsAcceso().get(i).getHistorial().get(n_versiones - 1).getComentarios()
          .size(); k++) {
        editorString.append(logeado.getDocsAcceso().get(i).getHistorial().get(n_versiones - 1).getComentarios().get(k)
            .toString());
      }
    }
    return editorString;
  }

  /**
   * Método que permite eliminar texto de la versión activa de un documento, se
   * requiere permiso de edición.
   *
   * @param logeado Usuario logeado
   * @param id      Id del documento
   * @param delete  Caracteres a eliminar
   */
  public void delete(Usuario logeado, Integer id, Integer delete) {
    if (getIndexDocCreados(id, logeado) != -1) {
      String contenidoAnt = logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getContenido();
      int largo = contenidoAnt.length();
      String contenidoMod;
      if (largo < delete) {
        contenidoMod = "";
      } else {
        contenidoMod = contenidoAnt.substring(0, largo - delete);
      }
      Version version = new Version(contenidoMod);
      version.setId(logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getHistorial().size());
      logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getHistorial().add(version);
      logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).setContenido(contenidoMod);
      System.out.println(
          "Se eliminaron " + delete + " caracteres, resultando: " + contenidoMod + " en documento id(" + id + ")");
      return;
    }
    if (isEditor(logeado, id)) {
      String contenidoAnt = logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).getContenido();
      String contenidoMod;
      int largo = contenidoAnt.length();
      if (largo < delete) {
        contenidoMod = "";
      } else {
        contenidoMod = contenidoAnt.substring(0, largo - delete);
      }
      Version version = new Version(contenidoMod);
      version.setId(logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).getHistorial().size());
      logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).getHistorial().add(version);
      logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).setContenido(contenidoMod);
      System.out.println(
          "Se eliminaron " + delete + " caracteres, resultando '" + contenidoMod + "' en documento id(" + id + ")");
      return;
    }
    System.out.println("Operacion no exitosa");
  }

  /**
   * Método que permite buscar y reemplazar texto de la versión activa de un
   * documento, se requiere permiso de edición.
   *
   * @param logeado     Usuario logeado
   * @param id          Id del documento
   * @param searchText  Texto a buscar
   * @param replaceText Texto a reemplazar
   */
  public void searchAndReplace(Usuario logeado, Integer id, String searchText, String replaceText) {
    String reemplazo;
    if (getIndexDocCreados(id, logeado) != -1) {
      if (logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getContenido().contains(searchText)) {
        reemplazo = logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getContenido().replace(searchText,
            replaceText);
        Version version = new Version(reemplazo);
        version.setId(logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getHistorial().size());
        logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getHistorial().add(version);
        logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).setContenido(reemplazo);
        System.out.println("Operacion Exitosa");
        return;
      }
      System.out.println("El texto " + searchText + " no se encuentra en el documento de id(" + id + ")");
      return;
    }
    if (isEditor(logeado, id)) {
      if (logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).getContenido().contains(searchText)) {
        reemplazo = logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).getContenido().replace(searchText,
            replaceText);
        Version version = new Version(reemplazo);
        version.setId(logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).getHistorial().size());
        logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).getHistorial().add(version);
        logeado.getDocsAcceso().get(getIndexDocAcceso(id, logeado)).setContenido(reemplazo);
        System.out.println("Operacion Exitosa");
        return;
      }
      System.out.println("El texto " + searchText + " no se encuentra en el documento de id(" + id + ")");
      return;
    }
    System.out.println("Operacion no exitosa");
  }

  /**
   * Método que permite aplicar estilos a un texto de la versión activa de un
   * documento, se requiere permiso de edición.
   *
   * @param logeado    Usuario logeado
   * @param id         Id del documento
   * @param searchText Texto a buscar
   * @param styles     Estilos a aplicar
   */
  public void applyStyles(Usuario logeado, Integer id, String searchText, String[] styles) {
    String styles_f = "";
    int j = 0;
    for (int i = 0; i < styles.length; i++) {
      if ((styles[i].equals("#\\i") || styles[i].equals("#\\b") || styles[i].equals("#\\u"))) {
        styles_f += styles[i];
        j = 1;
      }
    }
    if (j == 1) {
      String stylesString = " [" + styles_f.toString() + "]" + searchText + "[" + styles_f.toString() + "] ";
      searchAndReplace(logeado, id, searchText, stylesString);
    } else {
      System.out.println("Operacion no exitosa");
    }
  }

  /**
   * Método que permite comentar un texto de la versión activa, se requiere
   * permiso de comentarios.
   *
   * @param logeado    Usuario logeado
   * @param id         Id del documento
   * @param searchText Texto a buscar
   * @param comentario Comentario
   */
  public void comment(Usuario logeado, Integer id, String searchText, String comentario) {
    String reemplazo;
    if (getIndexDocCreados(id, logeado) != -1) {
      if (logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getContenido().contains(searchText)) {
        Comentario comentarios = new Comentario(logeado, searchText, comentario);
        reemplazo = logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getContenido();
        Version version = new Version(reemplazo);
        version.setId(logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getHistorial().size());
        version.getComentarios().add(comentarios);
        logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).getHistorial().add(version);
        logeado.getDocsCreados().get(getIndexDocCreados(id, logeado)).setContenido(reemplazo);
        System.out.println("Operacion Exitosa");
        return;
      }
    }
    System.out.println("Operacion no exitosa");
  }

  /**
   * Método que determina si un usuario es editor de un documento mediante su id
   *
   * @param logeado Usuario logeado
   * @param id      Id del documento
   * @return boolean
   */
  public boolean isEditor(Usuario logeado, Integer id) {
    for (int i = 0; i < logeado.getDocsAcceso().size(); i++) {
      if (logeado.getDocsAcceso().get(i).getId() == id) {
        for (int j = 0; j < logeado.getDocsAcceso().get(i).getAccesses().size(); j++) {
          if (logeado.getDocsAcceso().get(i).getAccesses().get(i).getUser().equals(logeado)) {
            if (logeado.getDocsAcceso().get(i).getAccesses().get(i).getPermiso().equals("w")) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  /**
   * Método que obtiene el indice de la lista de documentos con acceso del usuario
   *
   * @param id      Id del documento
   * @param logeado Usuario logeado
   * @return Integer
   */
  public Integer getIndexDocAcceso(Integer id, Usuario logeado) {
    for (int i = 0; i < logeado.getDocsAcceso().size(); i++) {
      if (logeado.getDocsAcceso().get(i).getId().equals(id)) {
        return i;
      }
    }
    return -1; // Retorna -1 cuando no encuentra el documento
  }

  /**
   * Método que obtiene el indice de la lista de documentos que el usuario es
   * dueño
   *
   * @param id      Id del documento
   * @param logeado Usuario logeado
   * @return Integer
   */
  public Integer getIndexDocCreados(Integer id, Usuario logeado) {
    for (int i = 0; i < logeado.getDocsCreados().size(); i++) {
      if (logeado.getDocsCreados().get(i).getId().equals(id)) {
        return i;
      }
    }
    return -1;
  }
}
