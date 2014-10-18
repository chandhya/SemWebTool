import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.util.FileManager;

import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.VCARD;     

public class lab2 {
	
	private static String SOURCE = "http://utdallas/semclass#";
	private static File Lab2 = new File("Lab02.xml");
	private static FileOutputStream fop =null;
	private static Store store =null;
	private static File XMLFile = new File("Lab2_3_cthirugnanasambantham.xml");
	private static File N3File = new File("Lab2_3_cthirugnanasambantham.n3");
	private static File NTPFile = new File("Lab2_3_cthirugnanasambantham.ntp");
	public static void main(String args[])
	{
		lab2 l2= new lab2();
		l2.createModel();
		System.setProperty("jena.db.user", "jenasdb");
		System.setProperty("jena.db.password", "fastdb");
		System.setProperty("jena.db.url", "jdbc:mysql://localhost/sdb2");
		store = SDBFactory.connectStore("sdb.ttl") ;
		store.getTableFormatter().create();
		//Removing any data in the tables in the store 
		store.getTableFormatter().truncate();
		//creating an unnamed SDB model
		l2.createUnnamedSDB();
	}

	private void createModel() {
		OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		m.setNsPrefix("Movie", SOURCE);
		OntClass film     = m.createClass( SOURCE + "Movie" );
		DatatypeProperty  name = m.createDatatypeProperty( SOURCE + "Name" );
		DatatypeProperty  year = m.createDatatypeProperty( SOURCE + "Year" );
		ObjectProperty director = m.createObjectProperty( SOURCE + "Director" );
		ObjectProperty	bookMadeMovie = m.createObjectProperty(SOURCE + "bookMadeMovie");
		m.setNsPrefix("BookMadeIntoMovie", SOURCE+"bookMadeMovie");
		//director.addLabel("Director",SOURCE);
		name.addDomain(film);
		year.addDomain(film);
		director.addDomain(film);
		//director.addProperty(VCARD.NAME, "Stanley Kubrick");
		//director.addProperty(VCARD.ROLE, "Director");
		bookMadeMovie.addDomain(film);
		//bookMadeMovie.addProperty(DCTerms.creator, m.createResource().addProperty(VCARD.NAME, "Peter George").addProperty(VCARD.ROLE, "Author"));
		//bookMadeMovie.addProperty(DCTerms.title, "Red Alert");
		
		//InputStream in = 
		Individual instance1 = film.createIndividual( SOURCE + "Clockwork Orange");
		instance1.addLiteral(name, "Clockwork Orange");
		instance1.addLiteral(year, 1971);
		
		instance1.addProperty(director,m.createResource().addProperty(VCARD.NAME, "Stanley Kubrick").addProperty(VCARD.ROLE, "Director"));
		
		instance1.addProperty(bookMadeMovie, m.createResource().addProperty(DCTerms.creator, m.createResource().addProperty(VCARD.NAME, "Peter George").addProperty(VCARD.ROLE, "Author")).addProperty(DCTerms.title, "Red Alert"));
		
		//instance1.addProperty(instance1.getProperty(director);
		Individual instance2 = film.createIndividual( SOURCE + "Dr. Strangelove");
		instance2.addLiteral(name, "Dr. Strangelove");
		instance2.addLiteral(year, 1964);
		instance2.addProperty(director,m.createResource().addProperty(VCARD.NAME, "Stanley Kubrick").addProperty(VCARD.ROLE, "Director"));
		instance2.addProperty(bookMadeMovie, m.createResource().addProperty(DCTerms.creator, m.createResource().addProperty(VCARD.NAME, "Peter George").addProperty(VCARD.ROLE, "Author")).addProperty(DCTerms.title, "Red Alert"));
		
		try {
			fop = new FileOutputStream(Lab2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.write(fop,"RDF/XML-ABBREV");
		m.write(System.out,"RDF/XML-ABBREV");
		//m.read( SOURCE, "OWL/XML" );
	}
	private void createUnnamedSDB() {
		 Model SDBmodel = SDBFactory.connectDefaultModel(store) ;
		 InputStream in = FileManager.get().open("Lab02.xml");
		 SDBmodel.read(in,null);
		 try {
				fop = new FileOutputStream(XMLFile);
				SDBmodel.write(fop,"RDF/XML-ABBREV");
				fop = new FileOutputStream(N3File);
				SDBmodel.write(fop,"N3");
				fop = new FileOutputStream(NTPFile);
				SDBmodel.write(fop,"N-TRIPLES");
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
		
	}
}
