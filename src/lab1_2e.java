import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.VCARD;


public class lab1_2e {
	private static String personURI    = "http://utdallas/semclass";
	private static String givenName    = "Steven";
	private static String familyName   = "Seida";
	private static String fullName     = givenName + " " + familyName;
	private static String DOB = "01/01/1901";
	private static String Role = "Senior Lecturer";
	private static String email = "steven.seida@utdallas.edu";
	private static FileOutputStream fop =null;
	private static File Sample = new File("Sample.xml");
	private static File XMLFile = new File("Lab1_2_cthirugnanasambantham.xml");
	private static File N3File = new File("Lab1_2_cthirugnanasambantham.n3");
	private static File NTPFile = new File("Lab1_2_cthirugnanasambantham.ntp");
	private static Store store =null;
	public static void main(String args[])
	{
		lab1_2e l2e= new lab1_2e();
		//Creating a default model
		l2e.createModel();
		//Creating a store
		System.setProperty("jena.db.user", "jenasdb");
		System.setProperty("jena.db.password", "fastdb");
		System.setProperty("jena.db.url", "jdbc:mysql://localhost/sdb2");
		store = SDBFactory.connectStore("sdb.ttl") ;
		store.getTableFormatter().create();
		//Removing any data in the tables in the store 
		store.getTableFormatter().truncate();
		//creating an unnamed SDB model
		l2e.createUnnamedSDB();
		//creating a named SDB model
		l2e.createNamedSDB();
		//store.getTableFormatter().truncate();
		store.close();
	}
	//Method for creating a named SDB model
	private void createNamedSDB() {
		IDBConnection idbc =  ModelFactory.createSimpleRDBConnection();
		ModelMaker mm = ModelFactory.createModelRDBMaker(idbc);
		Model namedSDBModel = mm.createModel("myrdf");
		 InputStream inp = FileManager.get().open("Sample.xml");
		namedSDBModel.read(inp,null);
		Model resultModel = SDBFactory.connectNamedModel(store, "http://utdallas/semclass");
		 /*try {
				fop = new FileOutputStream(XMLFile);
				resultModel.write(fop, "RDF/XML-ABBREV");
				fop = new FileOutputStream(N3File);
				namedSDBModel.write(fop,"N-TRIPLES");
				fop = new FileOutputStream(NTPFile);
				namedSDBModel.write(fop,"N3");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		
		
		
	}
	//Method for creating an unnamed SDB model
	private void createUnnamedSDB() {
		 Model SDBmodel = SDBFactory.connectDefaultModel(store) ;
		 InputStream in = FileManager.get().open("Sample.xml");
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
	//Method for creating an default model
	private void createModel() {
		Model model = ModelFactory.createDefaultModel();
		Resource stevenSeida  = model.createResource(personURI).
			     addProperty(VCARD.FN, fullName)
		         .addProperty(VCARD.N,
		                      model.createResource()
		                           .addProperty(VCARD.Given, givenName)
		                           .addProperty(VCARD.Family, familyName)).addProperty(VCARD.BDAY,DOB).addProperty(VCARD.EMAIL, email).addProperty(VCARD.ROLE, Role);
		try {
			fop = new FileOutputStream(Sample);
			model.write(fop, "RDF/XML-ABBREV");
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		} 
		
		
		
		
	}
	

}
