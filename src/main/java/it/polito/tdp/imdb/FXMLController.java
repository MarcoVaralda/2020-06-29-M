/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.db.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	int anno = this.boxAnno.getValue();
    	this.model.creGrafo(anno);
    	
    	this.txtResult.setText("Grafo creato!\n\n" +this.model.getNumeroVertici() +this.model.getNumeroArchi());
    	
    	this.boxRegista.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	Director scelto = this.boxRegista.getValue();
    	if(scelto==null) {
    		this.txtResult.setText("Devi inserire un regista!");
    		return;
    	}
    	
    	String risultato = "";
    	for(Adiacenza a : this.model.getAdiacenti(scelto))
    		risultato += a.getD1() +" con "+a.getPeso() +" attori condivisi" +"\n";
    	
    	this.txtResult.appendText("\n\nRegisti adiacenti a " +scelto +":\n" +risultato);
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	String stringaC = this.txtAttoriCondivisi.getText();
    	Director partenza = this.boxRegista.getValue();
    	
    	int c = 0;
    	try {
    		c = Integer.parseInt(stringaC);
    	}
    	catch(NumberFormatException nbe) {
    		this.txtResult.setText("Devi inserire un valore intero di attori condivisi!");
    		return;
    	}
    	
    	this.txtResult.appendText(this.model.cercaRegistiAffini(partenza,c));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	this.model = model;
    	List<Integer> anni = new LinkedList<>();
    	for(int i=2004;i<=2006;i++)
    		anni.add(i);
    	this.boxAnno.getItems().addAll(anni);
    }
    
}
