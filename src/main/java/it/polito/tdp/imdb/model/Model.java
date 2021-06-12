package it.polito.tdp.imdb.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.Adiacenza;
import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	ImdbDAO dao ;
	Map<Integer,Director> idMap ;
	Graph<Director,DefaultWeightedEdge> grafo;
	List<Director> vertici;
	
	List<Director> soluzioneMigliore;
	int pesoMigliore;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.idMap = new HashMap<>();
		this.dao.listAllDirectors(idMap);
	}
	
	public void creGrafo(int anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiungo i vertici
		vertici = this.dao.getVertices(idMap,anno);
		Graphs.addAllVertices(this.grafo,vertici);
		
		// Aggiugo gli archi
		for(Adiacenza a : this.dao.getEdges(idMap,anno))
			if(!this.grafo.containsEdge(a.getD1(),a.getD2())) 
				Graphs.addEdge(this.grafo,a.getD1(),a.getD2(),a.getPeso());
		
	}
	
	public List<Adiacenza> getAdiacenti(Director scelto) {
		List<Adiacenza> adiacenti = new LinkedList<>();
		
		for(DefaultWeightedEdge arco : this.grafo.edgesOf(scelto)) {
			int peso = (int) this.grafo.getEdgeWeight(arco);
			Director d = Graphs.getOppositeVertex(this.grafo,arco,scelto);
			Adiacenza a = new Adiacenza(d,null,peso);
			adiacenti.add(a);
		}
		Collections.sort(adiacenti);
		
		return adiacenti;
	}
	
	public String getNumeroVertici() {
		return "Numero vertici: " +this.grafo.vertexSet().size() +"\n";
	}
	
	public String getNumeroArchi() {
		return "Numero Archi: " +this.grafo.edgeSet().size() +"\n";
	}
	
	public List<Director> getVertici() {
		return this.vertici;
	}
	
	
	public String cercaRegistiAffini(Director partenza, int c) {
		List<Director> parziale = new LinkedList<>();
		parziale.add(partenza);
		int pesoParziale = 0;
		this.pesoMigliore=0;
		this.soluzioneMigliore = new LinkedList<>();
		
		ricorsione(parziale,pesoParziale,c);
		
		String result = "\n\nSequenza di registi affini a " +partenza +":\n";
		
		for(Director d : this.soluzioneMigliore) 
			result += d +"\n";
		result += "Con punteggio " +this.pesoMigliore;
		
		return result;
			
	}

	private void ricorsione(List<Director> parziale, int pesoParziale, int c) {
		Director ultimoAggiunto = parziale.get(parziale.size()-1);
		
		// Caso terminale
		if(parziale.size()>this.soluzioneMigliore.size()) {
			this.soluzioneMigliore = new LinkedList<>(parziale);
			this.pesoMigliore = pesoParziale;
		}
		
		for(DefaultWeightedEdge arco : this.grafo.edgesOf(ultimoAggiunto)) {
			Director vicino = Graphs.getOppositeVertex(this.grafo,arco,ultimoAggiunto);
			// Controllo che vicino non sia già presente in parziale
			if(!parziale.contains(vicino)) {
				int peso = (int) this.grafo.getEdgeWeight(arco);
				
				// Aggiungendo vicino a parziale, supero la soglia c? Se no, lo aggiungo
				if(pesoParziale+peso<=c) {

					// Provo ad aggiungere vicino
					parziale.add(vicino);
					int nuovoPeso = pesoParziale + peso;
					ricorsione(parziale,nuovoPeso,c);
					parziale.remove(vicino);
				}
			}
		}
	}

}
