package m3.uf6.examens.model;


import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

/**
 * Singleton Model
 *
 * El patró Singleton garanteix que només existirà una única instància de la classe
 *
 * Per accedir a la instància Model.getInstance();
 *
 * @author alex
 *
 */
public class Model {
	public static final String[] CICLES = {"DAM","DAW","ASIX"};
	public static final String EXAMENS_ODB = "$objectdb/db/examens.odb";
	private static Model instance = null;
	private EntityManagerFactory emf;
	private EntityManager em;
	private Stage stage;
	private Application app;

	private ObservableList<UnitatFormativa> unitats;
	private ObservableList<Estudiant> estudiants;
	private ObservableList<Examen> examens;

	// Dades examen seleccionat
	private Examen selectedExamen;
	private ObservableList<Estudiant> alumnesSelectedExamen;
	private ObservableList<Lliurament> lliuramentsSelectedExamen;
	private ObservableList<Lliurament> correccionsSelectedExamen;
	private ObservableList<Lliurament> revisionsSelectedExamen;


	protected Model() {
		this.initConnexio();	// EntityManager Factory
		this.unitats = FXCollections.observableArrayList();
		this.estudiants = FXCollections.observableArrayList();
		this.examens =  FXCollections.observableArrayList();
		this.alumnesSelectedExamen =  FXCollections.observableArrayList();
		this.lliuramentsSelectedExamen =  FXCollections.observableArrayList();
		this.correccionsSelectedExamen =  FXCollections.observableArrayList();
		this.revisionsSelectedExamen =  FXCollections.observableArrayList();
	}

	private void initConnexio() {
		/* Crear emf i em */
		emf = Persistence.createEntityManagerFactory(EXAMENS_ODB);
		em = emf.createEntityManager();
	}

	public static Model getInstance() {
		if(instance == null) instance = new Model();
		return instance;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public EntityManager getEM() {
		return this.em;
	}

	public Examen getSelectedExamen() {
		return selectedExamen;
	}

	public void setSelectedExamen(Examen selectedExamen) {
		this.selectedExamen = selectedExamen;
	}

	public void closeEM() {	// before close APP
		/* Tancar emf i em */
		em.close();
		emf.close();
	}

	public ObservableList<Examen> getExamens() {
		TypedQuery<Examen> query = em.createNamedQuery("Examen.findAll", Examen.class);

		this.examens.clear();
		this.examens.addAll(query.getResultList());

		return this.examens;
	}

	public void setExamens(ObservableList<Examen> examens) throws Excepcio {


		try {
			this.examens = examens;
			this.em.getTransaction().begin();
			for (Examen examen : examens) this.em.merge(examen);
			this.em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}

	}

	public ObservableList<Estudiant> getEstudiants() {
		TypedQuery<Estudiant> query = em.createNamedQuery("Estudiant.findAll", Estudiant.class);

		this.estudiants.clear();
		this.estudiants.addAll(query.getResultList());

		return this.estudiants;
	}

	public void setEstudiants(ObservableList<Estudiant> estudiants) throws Excepcio {

		try {
			this.estudiants = estudiants;
			this.em.getTransaction().begin();
			for (Estudiant estudiant : estudiants) this.em.merge(estudiants);
			this.em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}
	}

	public ObservableList<UnitatFormativa> getUnitats() {
		TypedQuery<UnitatFormativa> query = em.createNamedQuery("UnitatFormativa.findAll", UnitatFormativa.class);

		this.unitats.clear();
		this.unitats.addAll(query.getResultList());

		return this.unitats;
	}

	public void setUnitats(ObservableList<UnitatFormativa> unitats) throws Excepcio {

		try {
			this.unitats = unitats;
			this.em.getTransaction().begin();
			for (UnitatFormativa unitat : unitats) this.em.merge(unitats);
			this.em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}

	}

	public void updateUnitatFormativa(UnitatFormativa unitat) throws Excepcio {
		try{
			em.getTransaction().begin();
			em.merge(unitat);
			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}

	}

	public void addUnitatFormativa(String cicle, String modul, int num, String titol, int hores) throws Exception {
		try{
			em.getTransaction().begin();

			UnitatFormativa newUnitat = new UnitatFormativa(cicle, modul, num, titol, hores);
			this.unitats.add(newUnitat);
			this.em.persist(newUnitat);

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}
	}

	public void removeUnitatFormativa(UnitatFormativa unitat) throws Excepcio {
		try{
			em.getTransaction().begin();

			this.unitats.remove(unitat);
			em.remove(unitat);

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}
	}

	public void updateEstudiant(Estudiant estudiant) throws Excepcio {
		try{
			em.getTransaction().begin();

			em.merge(estudiant);

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}

	}

	public void removeEstudiant(Estudiant estudiant) throws Excepcio {
		try{
			em.getTransaction().begin();

			this.estudiants.remove(estudiant);
			em.remove(estudiant);

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}


	}

	public void addEstudiant(String nom, String cognoms, int edat) throws Excepcio {
		try{
			em.getTransaction().begin();

			Estudiant newEst = new Estudiant(nom, cognoms, edat);
			this.em.persist(newEst);
			System.out.println(newEst.getNom() + " " + nom );
			em.getTransaction().commit();
			this.getEstudiants();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}
	}

	public Examen addExamen(UnitatFormativa unitat) throws Excepcio  {
		Examen newExam = new Examen(unitat);
		try{
			em.getTransaction().begin();

			newExam = new Examen(unitat);
			this.examens.add(newExam);
			this.em.persist(newExam);

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}

		return newExam;
	}

	public void removeExamen(Examen examen) throws Excepcio {
		try{
			em.getTransaction().begin();

			this.examens.remove(examen);
			em.remove(examen);

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}

	}

	public void apilarLliuramentSelectedExamen(Estudiant estudiant) throws Excepcio {
		if (this.selectedExamen == null) throw new Excepcio("Model", "No es pot apilar el lliurament de l'estudiant. Error examen desconegut");
		try{
			em.getTransaction().begin();

			selectedExamen.apilarLliurament(estudiant);
			getLliuramentsSelectedExamen();


			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}

	}


	public void desapilarLliuramentSelectedExamen() throws Excepcio {
		if (this.selectedExamen == null) throw new Excepcio("Model", "No es pot desapilar el lliurament de l'estudiant. Error examen desconegut");
		try{
			em.getTransaction().begin();

			selectedExamen.desapilarLliurament();
			getLliuramentsSelectedExamen();

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}

	}

	public void corregirLliuramentSelectedExamen(double nota) throws Excepcio {
		if (this.selectedExamen == null) throw new Excepcio("Model", "No es pot corregir el lliurament. Error examen desconegut");

		try{
			em.getTransaction().begin();

			selectedExamen.corregirLliurament(nota);
			getLliuramentsSelectedExamen();
			getCorreccionsSelectedExamen();

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}
	}

	public void solicitarRevisioSelectedExamen(Lliurament lliurament) throws Excepcio {
		if (this.selectedExamen == null) throw new Excepcio("Model", "No es pot revisar la nota de l'estudiant. Error examen desconegut");

		try{
			em.getTransaction().begin();

			selectedExamen.solicitarRevisio(lliurament.getEstudiant());
			getRevisionsSelectedExamen();

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}
	}

	public void atendreRevisioSelectedExamen(double nota) throws Excepcio {
		if (this.selectedExamen == null) throw new Excepcio("Model", "No es pot revisar el lliurament. Error examen desconegut");

		try{
			em.getTransaction().begin();

			selectedExamen.atendreRevisio(nota);
			getRevisionsSelectedExamen();
			getCorreccionsSelectedExamen();

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}
	}

	public void inscriureEstudiantSelectedExamen(Estudiant estudiant) throws Excepcio {
		if (this.selectedExamen == null) throw new Excepcio("Model", "No es pot anular la matrícula de l'estudiant. Error examen desconegut");

		try{
			em.getTransaction().begin();

			selectedExamen.inscriureEstudiant(estudiant);

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}

	}

	public void anularMatriculaEstudiantSelectedExamen(Estudiant estudiant) throws Excepcio {
		if (this.selectedExamen == null) throw new Excepcio("Model", "No es pot anular la matrícula de l'estudiant. Error examen desconegut");

		try{
			em.getTransaction().begin();

			selectedExamen.anularMatriculaEstudiant(estudiant);

			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Excepcio("Model", e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
		}

	}


	public ObservableList<Estudiant> getAlumnesSelectedExamen() {
		this.alumnesSelectedExamen.clear();
		if (this.selectedExamen == null) return alumnesSelectedExamen;

		this.alumnesSelectedExamen.addAll(this.selectedExamen.getAlumnes());
		return this.alumnesSelectedExamen;
	}

	public ObservableList<Lliurament> getLliuramentsSelectedExamen() {
		this.lliuramentsSelectedExamen.clear();
		if (this.lliuramentsSelectedExamen == null) return lliuramentsSelectedExamen;

		this.lliuramentsSelectedExamen.addAll(this.selectedExamen.getLliuraments());
		return this.lliuramentsSelectedExamen;
	}

	public ObservableList<Lliurament> getCorreccionsSelectedExamen() {
		this.correccionsSelectedExamen.clear();
		if (this.correccionsSelectedExamen == null) return correccionsSelectedExamen;

		this.correccionsSelectedExamen.addAll(this.selectedExamen.getCorreccions());
		return this.correccionsSelectedExamen;
	}

	public ObservableList<Lliurament> getRevisionsSelectedExamen() {
		this.revisionsSelectedExamen.clear();
		if (this.revisionsSelectedExamen == null) return revisionsSelectedExamen;

		this.revisionsSelectedExamen.addAll(this.selectedExamen.getRevisions());
		return this.revisionsSelectedExamen;
	}

	public UnitatFormativa getUnitatFormativaByIndex(int index) {
		return this.getUnitats().get(index);
	}

	public int getIndexEstudiant(Estudiant estudiant) {
		return this.getEstudiants().indexOf(estudiant);
	}

	public Estudiant getEstudiantByIndex(int index) {
		return this.getEstudiants().get(index);
	}

	public boolean estudiantInscritSelectedExamen(Estudiant estudiant) {
		if (this.selectedExamen == null || estudiant == null) return false;

		for (Estudiant inscrit: this.selectedExamen.getAlumnes()) {
			if (inscrit.compareTo(estudiant) == 0) return true;
		}
		return false;
	}

	public boolean estudiantLliuramentSelectedExamen(Estudiant estudiant) {
		if (this.selectedExamen == null || estudiant == null) return false;

		Lliurament nou;
		try {
			nou = new Lliurament(estudiant, this.selectedExamen);
		} catch (Excepcio e) {
			return false;
		}

		if (this.selectedExamen.getLliuraments().contains(nou)) return true;

		return false;
	}

	public boolean estudiantCorreccioSelectedExamen(Estudiant estudiant) {
		if (this.selectedExamen == null || estudiant == null) return false;

		Lliurament nou;
		try {
			nou = new Lliurament(estudiant, this.selectedExamen);
		} catch (Excepcio e) {
			return false;
		}

		if (this.selectedExamen.getCorreccions().contains(nou)) return true;

		return false;
	}

	public Estudiant estudiantDarrerLliuramentSelectedExamen() {
		if (this.selectedExamen == null) return null;

		Lliurament darrer = this.selectedExamen.darrerLliurament();

		if (darrer == null) return null;
		return darrer.getEstudiant();
	}

	public Lliurament primerLliuramentRevisarSelectedExamen() {
		if (this.selectedExamen == null) return null;

		Lliurament primer = this.selectedExamen.primeraRevisio();

		return primer;
	}


	/**
	 * Consultes
	 *
	 */
	public ModelQueryResult query1(int pagina, int perPagina) {

		Vector<String> colNames = new Vector<String>(Arrays.asList(new String[] {"cicle", "modul", "numero", "titol", "hores", "N Preguntes", "N alumnes", "N lliuraments", "N correccions", "N revisions"}));
		Vector<Integer> colWidth = new Vector<Integer>(Arrays.asList(new Integer[] {200, 200, 200, 200, 200, 200, 200, 200, 200, 200}));
		Vector<String> colAlign = new Vector<String>(Arrays.asList(new String[] {"left-column", "left-column", "left-column", "left-column", "left-column", "left-column", "left-column", "left-column", "left-column", "left-column"}));

		String strQuery = "SELECT "
				+ "u.unitat.cicle, "
				+ "u.unitat.modul, "
				+ "u.unitat.num, "
				+ "u.unitat.titol , "
				+ "u.unitat.hores, "
				+ "u.preguntes.size(),"
				+ "u.alumnes.size(),"
				+ "u.lliuraments.size(),"
				+ "u.correccions.size(),"
				+ "u.revisions.size()";
		strQuery += " FROM Examen u ";
		strQuery += " ORDER BY u.unitat.cicle, u.unitat.modul, u.unitat.num";

		TypedQuery<Object[]> query = this.em.createQuery(strQuery, Object[].class);

		int count = query.getResultList().size();

		query.setFirstResult((pagina -1) * perPagina).setMaxResults(perPagina);

		return new ModelQueryResult("QUERY 1", count, colNames, colWidth, colAlign, query.getResultList() );
	}

	public ModelQueryResult query2(int pagina, int perPagina) {

		Vector<String> colNames = new Vector<String>(Arrays.asList(new String[] {"cicle", "modul", "titol", "hores", "N correccions", "Media notes"}));
		Vector<Integer> colWidth = new Vector<Integer>(Arrays.asList(new Integer[] {200, 200, 200, 200, 200, 200}));
		Vector<String> colAlign = new Vector<String>(Arrays.asList(new String[] {"left-column", "left-column", "left-column", "left-column", "left-column", "left-column"}));

		String strQuery = "SELECT "
				+ "e.unitat.cicle, "
				+ "e.unitat.modul, "
				+ "e.unitat.titol, "
				+ "e.unitat.hores, "
				+ "count(e.correccions), "
				+ "avg(c.nota) ";

		strQuery += " FROM Examen e left join e.correccions c"
				+ " GROUP BY e";
		strQuery += " ORDER BY e.unitat.cicle, e.unitat.modul, e.unitat.num";

		TypedQuery<Object[]> query = this.em.createQuery(strQuery, Object[].class);

		int count = query.getResultList().size();

		query.setFirstResult((pagina -1) * perPagina).setMaxResults(perPagina);

		return new ModelQueryResult("QUERY 2", count, colNames, colWidth, colAlign, query.getResultList() );
	}

	public ModelQueryResult query3(int pagina, int perPagina, double nota, String listaNombres) {

		Vector<String> colNames = new Vector<String>(Arrays.asList(new String[] {"cicle", "numero", "Nom", "edat", "nota"}));
		Vector<Integer> colWidth = new Vector<Integer>(Arrays.asList(new Integer[] {200, 200, 200, 200, 200}));
		Vector<String> colAlign = new Vector<String>(Arrays.asList(new String[] {"left-column", "left-column", "left-column", "left-column", "left-column"}));

		String strQuery = "SELECT distinct "
				+ "u.examen.unitat.cicle, "
				+ "u.examen.unitat.num, "
				+ "concat(u.estudiant.nom,' ',u.estudiant.cognoms) , "
				+ "u.estudiant.edat, "
				+ "u.nota";

		strQuery += " FROM Lliurament u "
				+ "where u.nota > :nota and u.estudiant.nom not in(:nom)";
		strQuery += " ORDER BY u.examen.unitat.cicle, u.examen.unitat.num, u.nota DESC";
		//se
		String[] listaFinal = listaNombres.split(",");
		for (int i = 0; i < listaFinal.length; i++) {
			listaFinal[i] = listaFinal[i].trim();
		}

		TypedQuery<Object[]> query = this.em.createQuery(strQuery, Object[].class);
		query.setParameter("nota", nota);
		query.setParameter("nom", listaFinal);


		int count = query.getResultList().size();

		query.setFirstResult((pagina -1) * perPagina).setMaxResults(perPagina);

		return new ModelQueryResult("QUERY 3", count, colNames, colWidth, colAlign, query.getResultList() );
	}

	/**
	 * Carregar dadees d'exemple
	 *
	 * @throws Excepcio
	 */
	public void init() throws Excepcio  {
		//sdf = new SimpleDateFormat("yyyy/MM/dd",new Locale("CA","ES"));
		this.em.getTransaction().begin();

		UnitatFormativa m3damUf4 = null;
		if (this.getUnitats().size() == 0) {
			m3damUf4 = new UnitatFormativa("DAM", "M03. Programació", 4, "Programació orientada a objectes (POO). Fonaments", 35);
			UnitatFormativa m7asixUf2 = new UnitatFormativa("ASIX", "M07. Planificació i Administració de Xarxes", 2, "Administració de dispositius de xarxa", 55);

			this.em.persist(m3damUf4);
			this.em.persist(m7asixUf2);
		} else {
			m3damUf4 = this.getUnitats().get(0);
		}

		Examen pe1Uf4M3 = null;
		if (this.getExamens().size() == 0) {
			pe1Uf4M3 = new Examen(m3damUf4);
			this.em.persist(pe1Uf4M3);
		} else {
			pe1Uf4M3 = this.getExamens().get(0);
		}

		if (this.getEstudiants().size() == 0) {
			Estudiant joan = new Estudiant("Joan", "Pérez i Castells", 19);
			Estudiant maria = new Estudiant("Maria", "González i Fornells", 21);
			Estudiant john = new Estudiant("John", "Doe", 31);
			Estudiant marta = new Estudiant("Marta", "Martínez i Miracles", 24);
			Estudiant pere = new Estudiant("Pere", "Gálvez i Gaudí", 22);
			Estudiant anna = new Estudiant("Anna", "Ruiz i Roure", 22);
			Estudiant raul = new Estudiant("Raul", "Ruiz i Miralls", 26);
			Estudiant toni = new Estudiant("Toni", "Téllez i Saperas", 28);
			Estudiant gemma = new Estudiant("Gemma", "Suárez i Rius", 19);
			Estudiant marc = new Estudiant("Marc", "Gómez i Crusellas", 23);
			this.em.persist(joan);
			this.em.persist(maria);
			this.em.persist(marta);
			this.em.persist(pere);
			this.em.persist(anna);
			this.em.persist(raul);
			this.em.persist(toni);
			this.em.persist(gemma);
			this.em.persist(marc);

			/* Matrícula alumnat */
			pe1Uf4M3.inscriureEstudiants(new Estudiant[] {joan, maria, john, marta, pere, anna, raul, toni, gemma, marc} );
			pe1Uf4M3.inscriureEstudiant(joan); // repetit
			pe1Uf4M3.inscriureEstudiant(marc);
			pe1Uf4M3.anularMatriculaEstudiant(john);

			pe1Uf4M3.afegirPreguntaOberta("Explica què és una classe abstracte, quines característiques té i les diferències respecte una classe normal.", 1.5);
			pe1Uf4M3.afegirPreguntaOpcions(	"Marca la resposta correcte. Exemples de tipus de dades primitives poden ser:", 0.8,
					new String[] {"int, double, String.", "char, double, float.", "Char, Float, Boolean." });
			pe1Uf4M3.afegirPreguntaOpcions(	"Marca la resposta correcte. La millor manera per comparar el text \"Hola\" amb la variable de tipus String hola és:", 0.8,
					new String[] {"\"Hola\" == hola", "hola.equals(\"Hola\")", "\"Hola\".equals(hola)", "Cap de les anteriors" });
			pe1Uf4M3.afegirPreguntaOpcions(	"Marca la resposta correcte. L'ordre correcte dels elements dins una classe és:", 0.8,
					new String[] {	"atributs > constructor > getters/setters > altres mètodes.",
							"atributs > getters/setters > constructor > altres mètodes.",
							"atributs > altres mètodes > constructor > getters/setters.",
			"altres mètodes > getters/setters > constructor > atributs." });
			pe1Uf4M3.afegirPreguntaOberta("Descriu l'esquema jeràrquic de classes que implementa el llenguatge Java i les seves característiques principals. Posa exemples concrets.", 2);
			pe1Uf4M3.afegirPreguntaVerdaderFals("Indica si la següent afirmació és certa o falsa. Una classe abstracta A implementa la interfície I, per tant la classe A està obligada a implementar els mètodes de I.", 0.8);
			pe1Uf4M3.afegirPreguntaVerdaderFals("Indica si la següent afirmació és certa o falsa. Una classe A hereta d'una classe B, aleshores es pot afirmar que B hereta atributs i mètodes de A.", 0.8);
			pe1Uf4M3.afegirPreguntaOberta("Proposa el diagrama UML complet d'una classe que representi una persona", 2.5);

		}

		this.em.getTransaction().commit();
	}
}
