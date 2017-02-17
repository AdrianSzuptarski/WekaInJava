/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hurtowniedanych;

import java.net.URL;
import weka.core.Attribute;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import static hurtowniedanych.HurtownieDanychKsiegarnie.db;
import hurtowniedanychKsiegarnie.Models.Ksiegarnia;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.AttributeStats;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.neighboursearch.LinearNNSearch;
import weka.experiment.InstanceQuery;

/**
 *
 * @author daniel
 */
public class FXMLController implements Initializable {
    
    @FXML
    private ComboBox comboboxLiczbaKlubowiczow, comboboxOdleglosc, comboboxLiczStatystyki;
    
    @FXML
    private TableView<Ksiegarnia> tabelaksiegarnie = new TableView<>();
    
    @FXML
    private TextField textFieldKnn;
    
    @FXML
    private ObservableList<Ksiegarnia> data = FXCollections.observableArrayList();
    
    @FXML
    public TableColumn colNazwa, colregion,colmiasto, colLiczba;
    
    @FXML
    private Label label, labelAtrybut, labelNieNum, spr, labelKnn, labelOdleglosc;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            fillComboboxOdleglosc();
            fillTable();
            getWojewodztwo();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void getWojewodztwo() throws SQLException
    {
        ResultSet p = null;
        p = db.select("SELECT DISTINCT miasto FROM ksiegarnie order by miasto ASC");
        while (p.next()) {
            comboboxLiczbaKlubowiczow.getItems().add(p.getString("miasto"));
        }
    }
    
    public void fillTable() throws SQLException
    {
        try {
            ResultSet p = null;
            p = db.select("select ks.nazwa, ks.region, ks.miasto, count(*)\n" +
                            "from ksiegarnie ks, klienci kl, zakupy z \n" +
                            "where  z.id_ksiegarnie=ks.id_ksiegarnie \n" +
                            "group by rollup(ks.nazwa, ks.region,ks.miasto);");
            data.clear();
            tabelaksiegarnie.setEditable(true);
            while (p.next()) {
                data.add(new Ksiegarnia(p.getString("nazwa"),p.getString("region"), p.getString("miasto"), p.getInt(4)));
            }
            colNazwa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
            colregion.setCellValueFactory(new PropertyValueFactory<>("region"));
            colmiasto.setCellValueFactory(new PropertyValueFactory<>("miasto"));
            colLiczba.setCellValueFactory(new PropertyValueFactory<>("liczba"));
            tabelaksiegarnie.setItems(data);
        } catch (SQLException ex) {
            System.out.println("Błąd: "+ex.getMessage());
        }
    }
    
    public void sort()
    {
        String str = comboboxLiczbaKlubowiczow.getSelectionModel().getSelectedItem().toString();
        try {
            ResultSet p = null;
            p = db.select("select ks.nazwa, ks.region, ks.miasto, count(*)\n" +
                            "from ksiegarnie ks, klienci kl, zakupy z \n" +
                            "where  z.id_ksiegarnie=ks.id_ksiegarnie and ks.miasto='"+str+"'\n" +
                            "group by rollup(ks.nazwa, ks.region,ks.miasto);");
            data.clear();
            tabelaksiegarnie.setEditable(true);
            while (p.next()) {
                data.add(new Ksiegarnia(p.getString("nazwa"),p.getString("region"), p.getString("miasto"), p.getInt(4)));
            }
           colNazwa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
            colregion.setCellValueFactory(new PropertyValueFactory<>("region"));
            colmiasto.setCellValueFactory(new PropertyValueFactory<>("miasto"));
            colLiczba.setCellValueFactory(new PropertyValueFactory<>("liczba"));
            tabelaksiegarnie.setItems(data);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void fillComboboxOdleglosc(){
        comboboxOdleglosc.getItems().addAll(
                                                "Euklidesowa",
                                                "Manhatan"
                                                );
        comboboxOdleglosc.setValue("Euklidesowa");
    }
    
    
            
    public void trainAndTestKNN() throws FileNotFoundException,
            IOException, Exception {

        InstanceQuery instanceQuery = new InstanceQuery(); 
        instanceQuery.setUsername("postgres");
        instanceQuery.setPassword("szupek");
        instanceQuery.setCustomPropsFile(new File("./src/data/DatabaseUtils.props")); // Wskazanie pliku z ustawieniami dla PostgreSQL
        
        String query = "select ks.wydawnictwo,ks.gatunek, kl.mia-sto\n" +
                        "from zakupy z,ksiazki ks,klienci kl\n" +
                        "where ks.id_ksiazka=z.id_ksiazka and kl.id_klient=z.id_klient";
        
        instanceQuery.setQuery(query); 
        Instances data = instanceQuery.retrieveInstances(); 
        data.setClassIndex(data.numAttributes()-1);
        
        data.randomize(new Random());
        double percent = 70.0;
        int trainSize = (int) Math.round(data.numInstances() * percent / 100);
        int testSize = data.numInstances() - trainSize;
        Instances trainData = new Instances(data, 0, trainSize);
        Instances testData = new Instances(data, trainSize, testSize);
        
        int lSasiadow = Integer.parseInt(textFieldKnn.getText());
        System.out.println(lSasiadow);

        IBk ibk = new IBk(lSasiadow); 

        // Ustawienie odleglosci
        EuclideanDistance euclidean = new EuclideanDistance();   // euklidesowej
        ManhattanDistance manhatan =  new ManhattanDistance(); // miejska  
        
        LinearNNSearch linearNN = new LinearNNSearch();
        
        if(comboboxOdleglosc.getSelectionModel().getSelectedItem().equals("Manhatan")){
            linearNN.setDistanceFunction(manhatan);     
        }
        else {
            linearNN.setDistanceFunction(euclidean); 
        }
        
        ibk.setNearestNeighbourSearchAlgorithm(linearNN); // ustawienie sposobu szukania sasiadow

        // Tworzenie klasyfikatora
        ibk.buildClassifier(trainData);

        Evaluation eval = new Evaluation(trainData);
        eval.evaluateModel(ibk, testData);
        spr.setVisible(true);
        labelKnn.setVisible(true);
        labelOdleglosc.setVisible(true);
        labelKnn.setText(textFieldKnn.getText());
        labelOdleglosc.setText(comboboxOdleglosc.getSelectionModel().getSelectedItem().toString());
        spr.setText(eval.toSummaryString("Wynik:", true));
        }
    }

    

