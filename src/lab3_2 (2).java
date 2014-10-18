import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.util.FileManager;


public class lab3_2 {
	private static Store store =null;
	Model SDBmodel =null;
	private static FileOutputStream fop =null;
	private static File XMLFile = new File("Lab3_2_cthirugnanasambantham.xml");
	public static void main(String args[]){
		
		lab3_2 l32=new lab3_2();
		l32.loadSDBModel();
		l32.executeQuery();
		store.close();
	}
private void executeQuery() {
	String queryString = 
			"PREFIX urn: <http://xmlns.com/foaf/0.1/> " +
			"SELECT ?p ?o " +
			"WHERE {" +
			"  <urn:monterey:#incident1> ?p ?o    }";

		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, SDBmodel);
		ResultSet results = qe.execSelect();
	
		// Output query results	
		//ResultSetFormatter.out(fop, results, query);
		try {
			fop = new FileOutputStream(XMLFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 System.out.println("Output file is generated");
		ResultSetFormatter.outputAsXML(fop, results);
		//ResultSetFormatter.out
		// Important - free up resources used running the query
		qe.close();
		
	}
	//Method to load data into SDB Model
	private void loadSDBModel() {
		long startTime = System.currentTimeMillis();
		
		System.setProperty("jena.db.user", "jenasdb");
		System.setProperty("jena.db.password", "fastdb");
		System.setProperty("jena.db.url", "jdbc:mysql://localhost/sdb2");
		store = SDBFactory.connectStore("sdb.ttl") ;
		store.getTableFormatter().create();
		//Removing any data in the tables in the store 
		store.getTableFormatter().truncate();
		SDBmodel = SDBFactory.connectDefaultModel(store) ;
		 InputStream in = FileManager.get().open("monterey.xml");
		 SDBmodel.read(in,null);
		 long endTime = System.currentTimeMillis();
		 System.out.println("Load of monterey.rdf took "+(endTime-startTime)/100 +" seconds");
		

	}}


