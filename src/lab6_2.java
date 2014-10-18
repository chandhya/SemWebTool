/*
* Lab6 
*  
* 
* @author
* Chandhya thirugnanasambantham
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;


public class lab6_2 {
	private static Store store =null;
	private static File XMLFile = new File("alldata.xml");
	private static FileOutputStream fop =null;
	Model m;
	Query query;
	 ArrayList<String> nodeList08 = new ArrayList<String>();
	    ArrayList<String> nodeList09 = new ArrayList<String>();
	   
	public static void main(String args[])
	{
		lab6_2 l6_2= new lab6_2();
		//Creating a default model
		l6_2.createModel();
		l6_2.queryModel();
		l6_2.createAssertion();
		l6_2.createReasoner();
		//Creating a store
		System.setProperty("jena.db.user", "jenasdb");
		System.setProperty("jena.db.password", "fastdb");
		System.setProperty("jena.db.url", "jdbc:mysql://localhost/sdb2");
		store = SDBFactory.connectStore("sdb.ttl") ;
		store.getTableFormatter().create();
		//Removing any data in the tables in the store 
		store.getTableFormatter().truncate();
			//store.getTableFormatter().truncate();
		store.close();
	}
//Method to create reasoner
	private void createReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		InfModel infm = ModelFactory.createInfModel(reasoner, m);
		int j=0;
		while(j<nodeList08.size())
		{
			String q = "PREFIX foaf:<http://xmlns.com/foaf/0.1/> " +
    			       "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
					   "SELECT distinct ?paper ?name WHERE {" +
					   "<"+ nodeList08.toArray()[j].toString() +"> <http://www.w3.org/2002/07/owl#sameAs> ?s1. " +
					   "{<"+ nodeList08.toArray()[j].toString() +"> foaf:made ?o} " +
					   "UNION {?s1 foaf:made ?o}. ?o dc:title ?paper." +
					   " <"+ nodeList08.toArray()[j].toString() +"> foaf:name ?name }";
			
			query = QueryFactory.create(q);
			QueryExecution qe = QueryExecutionFactory.create(query, infm);
			ResultSet results = qe.execSelect();
			ResultSetFormatter.out(System.out, results);
			while (j<nodeList08.size()-1) 
			{
				if((nodeList08.toArray()[j].toString().equals(nodeList08.toArray()[j+1].toString()))) 
				{
					j++;
				}
				else 
				{
					j++;
					break;
				}					
			}
			j++;
		}
		System.out.println("End of Third Listing");

		//PART g
		FileOutputStream out;
		try {
			out = new FileOutputStream("Lab6_2_cthirugnanasambantham.N3");
			  m.write(out, "N3");
			    
				m.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
}

		

	//Method to create query 
	private void queryModel() {
		
		
		//PART c
        String querystr = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
			    "PREFIX dc:<http://purl.org/dc/elements/1.1/>"+
			    "PREFIX foaf:<http://xmlns.com/foaf/0.1/>"+
			    "SELECT  ?y08 ?y09 ?Name ?Paper  WHERE {" +
			    "?y08 foaf:mbox_sha1sum ?mbox. " +
			    "?y09 foaf:mbox_sha1sum ?mbox." +
			    "FILTER (?y08 != ?y09). " +
			    "?y08 a foaf:Person. " +
			    "?y08 foaf:name ?Name. " +
			    "?o dc:creator ?y08. " +
			    "?o dc:title ?Paper" +
			    " }";
          
        Query query = QueryFactory.create(querystr);
	    QueryExecution qe = QueryExecutionFactory.create(query, m);
	    ResultSet results =  qe.execSelect();
	    System.out.println("URI                                                               |                     Name                     |                paper");
	    while(results.hasNext())
	    {
	    	QuerySolution qs = results.nextSolution();
	    	RDFNode p08 = qs.get("y08");
	    	RDFNode p09 = qs.get("y09");
	    	RDFNode name = qs.get("Name");
	    	RDFNode paper = qs.get("Paper");
	    	
	    	System.out.println(p08.toString()+"     |      " + name.toString() +  "            |         " + paper.toString());
	    	nodeList08.add(p08.toString());
	    	nodeList09.add(p09.toString());
	    }
	    
	    System.out.println("End of first listing");
	}

	//Method to create in-memory default model
	private void createModel() {
		 m = ModelFactory.createDefaultModel();
		
	
		try {
			InputStream in = FileManager.get().open("C9b_Lab6_swc_ontology_unmodified.rdf");
			m.read(in,null);		
			fop = new FileOutputStream(XMLFile);
			
			in =  FileManager.get().open("C9c_Lab6_eswc-2008-complete_modified.rdf");
			m.read(in,null);
			
			in =  FileManager.get().open("C9d_Lab6_eswc-2009-complete_modified.rdf");
			m.read(in,null);
			m.write(fop,"RDF/XML-ABBREV");
			
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}
	
	}
	
	//Method to create assertion
	private void createAssertion() {
		
	    String URI = "http://utdallas.semclass/";
	    Property samePerson =m.createProperty(URI,"samePerson");
	   
	    for(int i=0;i<nodeList08.size();i++)
	    {
	    	Resource assertion1 = m.getResource(nodeList08.toArray()[i].toString());
	    	assertion1.addProperty(OWL.sameAs, m.getResource(nodeList09.toArray()[i].toString()));
	    	assertion1.addProperty(DC.creator, "Chandhya");
	    	
	    	Resource assertion2 = m.createResource(nodeList08.toArray()[i].toString());
	    	assertion2.addProperty(samePerson, m.getResource(nodeList09.toArray()[i].toString()));
	    	assertion2.addProperty(DC.creator, "Chandhya");
	    }
	    
	    int j=0;
	    while(j<nodeList08.size())
	    {
	    	String querystr = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
			    	      "PREFIX dc:<http://purl.org/dc/elements/1.1/>"+
			    	      "PREFIX foaf:<http://xmlns.com/foaf/0.1/>"+
				          "SELECT distinct ?paper ?name  WHERE {" +
			     	      "<"+ nodeList08.toArray()[j].toString() +"> <" + URI + "samePerson> ?P09." +
			     	      " {<"+ nodeList08.toArray()[j].toString() +"> foaf:made ?o}" +
			     	      " UNION {?P09 foaf:made ?o}." +
			     	      " ?o dc:title ?paper. " +
			     	      "<"+ nodeList08.toArray()[j].toString() +"> foaf:name ?name }";
	    	
	    		query=QueryFactory.create(querystr);
	    		QueryExecution qe=QueryExecutionFactory.create(query,m);
	    		ResultSet results = qe.execSelect();
	    		ResultSetFormatter.out(System.out, results);

				while (j<nodeList08.size()-1) 
				{
					if((nodeList08.toArray()[j].toString().equals(nodeList08.toArray()[j+1].toString()))) 
					{
						j++;
					}
					else 
					{
						j++;
						break;
					}
					
				}
				j++;
	    }
	    System.out.println("End of Second Listing");
	    

}
}
