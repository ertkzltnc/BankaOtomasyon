/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication3;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author ertugrul KZLTNC
 */
public class FXMLDocumentController implements Initializable {

    static String aktifHesap = "";

    Connection con;

    @FXML
    AnchorPane PaneAna, PaneEkle, PaneGoruntule, PaneHesapIslemleri;

    @FXML
    Button kayitEkle, goruntule, geri;

    //////////////////////
    @FXML
    TextField ad, soyad, tc;

    @FXML
    ComboBox hesapTuru; 

    @FXML
    Label ekleUyari;
    @FXML
    String shesap="1 ";

    @FXML
    private void kaydet(ActionEvent event) {
        String sad = ad.getText().toString();
        String ssoyad = soyad.getText().toString();
        String stc = tc.getText().toString();
         shesap = hesapTuru.getValue().toString();
        String hesapNo = "";

        // hesap no oluştur
        Random rand = new Random();
        if (shesap.equals("Cari Hesap")) {
            // cari hesap no 
            shesap = "1";
            hesapNo = String.valueOf(rand.nextInt(1000) + 1000); //

        } else if (shesap.equals("Kısa Vadeli Hesap")) {
            // kısa vadeli hespa no 
            shesap = "2";
            hesapNo = String.valueOf(rand.nextInt(1000) + 2000); // 

        } else if (shesap.equals("Uzun Vadeli Hesap")) {
            // uzun vadeli hespa no 
            shesap = "3";
            hesapNo = String.valueOf(rand.nextInt(1000) + 2000); // 

        } else {
            shesap = "4";
            hesapNo = String.valueOf(rand.nextInt(1000) + 3000); // 

        }

        /// kontrolleri yap
        boolean kaydet = false;
        if(stc.length() != 11 || sad.length() == 0 || ssoyad.length() ==0  ){
           ekleUyari.setText("TC 11 haneli olmali. ad, soyad bos olmamali"); //eksik bilgi sorgu
            kaydet = false;
        }else {
            kaydet=true;
            
            
        }
        if (kaydet) {
            // müşteri ekleme sorgusu
            String query = "insert into musteriler set musteri_adi='" + sad + "', musteri_soyadi='" + ssoyad + "', musteri_tc='" + stc + "', hesap_no='" + hesapNo + "'";
            int lastId = 0;
            Statement stm;
            try {
                stm = (Statement) con.createStatement();
                lastId = stm.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

                try {
                    query = "select musteri_id from musteriler order by musteri_id desc limit 1";
                    ResultSet rs = stm.executeQuery(query);

                    while (rs.next()) {
                        lastId = rs.getInt("musteri_id");
                    }
                    System.out.println(lastId);
                } catch (SQLException ex) {
                    System.out.println("id çekilemedi");
                }
            } catch (SQLException ex) {
                ekleUyari.setText("Sistem Hatası: " + ex);
            }

            // hesap ekleme sorgusu
            Date d = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            String query2 = "insert into hesaplar set hesap_no='" + hesapNo + "', musteri_id='" + lastId + "', hesap_tipi='" + shesap + "', bakiye='0', kayit_tarihi='" + dateFormat.format(date) + "', hesap_durumu='1', borc='0'";
            try {
                stm = (Statement) con.createStatement();
                lastId = stm.executeUpdate(query2);
                ekleUyari.setText("Kayit Başarılı");
            } catch (SQLException ex) {
                ekleUyari.setText("Sistem Hatası2: " + ex);
            }

        } 
        
    }
//////////////////////

    @FXML
    private void ekle(ActionEvent event) {
        PaneEkle.setVisible(true);
        PaneGoruntule.setVisible(false);
        PaneAna.setVisible(false);
        PaneHesapIslemleri.setVisible(false);
    }

    ////////////////////////////////////////
    // hesaplar başlangıç
    @FXML
    TextField gtc, gad, gsoyad, ghesapTipi, tara;

    @FXML
    Button gguncelle;

    @FXML
    Button ara;

    @FXML
    private void ara(ActionEvent event) {
        hesaplarTablosu.getColumns().clear();
        hesaplarTablosunuDoldur(1);
    }

    @FXML
    private void gmusteri(ActionEvent event) {
        try {
            String query = "update musteriler set  musteri_adi='" + gad.getText() + "', "
                    + "musteri_soyadi='" + gsoyad.getText() + "', musteri_tc='" + gtc.getText()
                    + "', hesap_no='" + ghesapTipi.getText()
                    + "' where musteri_tc='" + gtc.getText() + "'";
            PreparedStatement update = con.prepareStatement(query);
            int updateEx = update.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            System.out.println("güncelleme hatası");
        }
        hesaplarTablosu.getColumns().clear();
        hesaplarTablosunuDoldur(0);
    }

    @FXML
    private TableView<Hesaplar> hesaplarTablosu = new TableView<Hesaplar>();

    private void hesaplarTablosunuDoldur(int secim) {

        hesaplarTablosu.setEditable(true);

        // burayı veri tabanından çekecez
        TableColumn tad = new TableColumn("AD");
        tad.setMinWidth(100);
        tad.setCellValueFactory(
                new PropertyValueFactory<Hesaplar, String>("ad")
        );

        TableColumn tsoyad = new TableColumn("soyad");
        tsoyad.setMinWidth(100);
        tsoyad.setCellValueFactory(
                new PropertyValueFactory<Hesaplar, String>("soyad")
        );

        TableColumn ttc = new TableColumn("tc");
        ttc.setMinWidth(100);
        ttc.setCellValueFactory(
                new PropertyValueFactory<Hesaplar, String>("tc")
        );

        TableColumn thesapNo = new TableColumn("Hesap No");
        thesapNo.setMinWidth(100);
        thesapNo.setCellValueFactory(
                new PropertyValueFactory<Hesaplar, String>("hesapNo")
        );

        TableColumn tguncelle = new TableColumn("Güncelle");
        tguncelle.setMinWidth(100);
        tguncelle.setCellValueFactory(
                new PropertyValueFactory<Hesaplar, Button>("guncelle")
        );

        TableColumn thesapIslemleri = new TableColumn("Hesap İşlemleri");
        thesapIslemleri.setMinWidth(100);
        thesapIslemleri.setCellValueFactory(
                new PropertyValueFactory<Hesaplar, Button>("hesapIslemleri")
        );

        final ObservableList<Hesaplar> data = FXCollections.observableArrayList();

        if (secim == 0) {
            try {
                String query = "select * from musteriler";
                Statement stm = (Statement) con.createStatement();
                ResultSet rs = stm.executeQuery(query);

                while (rs.next()) {
                    String ctc = rs.getString("musteri_tc");
                    String cad = rs.getString("musteri_adi");
                    String csoyad = rs.getString("musteri_soyadi");
                    String chesapNo = rs.getString("hesap_no");

                    Hesaplar row = new Hesaplar(cad, csoyad, ctc, chesapNo,
                            PaneAna, PaneEkle, PaneGoruntule, PaneHesapIslemleri,
                            gtc, gad, gsoyad, ghesapTipi);
                    data.add(row);
                }

            } catch (SQLException ex) {
                System.out.println("musteriler listesi çekilemedi");
            }
        } else if (secim == 1) {
            try {
                String query = "select * from musteriler where musteri_tc='" + tara.getText() + "' or hesap_no='" + tara.getText() + "'";
                Statement stm = (Statement) con.createStatement();
                ResultSet rs = stm.executeQuery(query);

                while (rs.next()) {
                    String ctc = rs.getString("musteri_tc");
                    String cad = rs.getString("musteri_adi");
                    String csoyad = rs.getString("musteri_soyadi");
                    String chesapNo = rs.getString("hesap_no");

                    Hesaplar row = new Hesaplar(cad, csoyad, ctc, chesapNo,
                            PaneAna, PaneEkle, PaneGoruntule, PaneHesapIslemleri,
                            gtc, gad, gsoyad, ghesapTipi);
                    data.add(row);
                }

            } catch (SQLException ex) {
                System.out.println("musteriler listesi çekilemedi");
            }
        }

        hesaplarTablosu.setItems(data);
        hesaplarTablosu.getColumns().addAll(tad, tsoyad, ttc, thesapNo, tguncelle, thesapIslemleri);
    }

    @FXML
    private void goruntule(ActionEvent event) {
        PaneEkle.setVisible(false);
        PaneGoruntule.setVisible(true);
        PaneAna.setVisible(false);
        PaneHesapIslemleri.setVisible(false);
        hesaplarTablosu.getColumns().clear();
        hesaplarTablosunuDoldur(0);
    }

    public static class Hesaplar {

        private SimpleStringProperty ad;
        private SimpleStringProperty soyad;
        private SimpleStringProperty tc;
        private SimpleStringProperty hesapNo;
        private Button guncelle;
        private Button hesapIslemleri;
        AnchorPane PaneAna, PaneEkle, PaneGoruntule, PaneHesapIslemleri;
        TextField gtc, gad, gsoyad, ghesapTipi;

        Hesaplar(String ad, String soyad, String tc, String hesapNo,
                AnchorPane PaneAna, AnchorPane PaneEkle, AnchorPane PaneGoruntule,
                AnchorPane PaneHesapIslemleri,
                TextField gtc, TextField gad, TextField gsoyad, TextField ghesapTipi) {

            this.ad = new SimpleStringProperty(ad);
            this.soyad = new SimpleStringProperty(soyad);
            this.tc = new SimpleStringProperty(tc);
            this.hesapNo = new SimpleStringProperty(hesapNo);

            this.PaneAna = PaneAna;
            this.PaneEkle = PaneEkle;
            this.PaneGoruntule = PaneGoruntule;
            this.PaneHesapIslemleri = PaneHesapIslemleri;

            this.gtc = gtc;
            this.gad = gad;
            this.gsoyad = gsoyad;
            this.ghesapTipi = ghesapTipi;

            this.guncelle = new Button("Güncelle");
            this.hesapIslemleri = new Button("Hesap İşlemleri");

            guncelle.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    gtc.setText(tc);
                    gad.setText(ad);
                    gsoyad.setText(soyad);
                    ghesapTipi.setText(hesapNo);
                }

            });

            hesapIslemleri.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    PaneEkle.setVisible(false);
                    PaneGoruntule.setVisible(false);
                    PaneAna.setVisible(false);
                    PaneHesapIslemleri.setVisible(true);
                    aktifHesap = hesapNo;
                }

            });

        }

        /**
         * hesap
         *
         * @return the ad
         */
        public String getAd() {
            return ad.get();
        }

        /**
         * @param ad the ad to set
         */
        public void setAd(String ad) {
            this.ad.set(ad);
        }

        /**
         * @return the soyad
         */
        public String getSoyad() {
            return soyad.get();
        }

        /**
         * @param soyad the soyad to set
         */
        public void setSoyad(String soyad) {
            this.soyad.set(soyad);
        }

        /**
         * @return the tc
         */
        public String getTc() {
            return tc.get();
        }

        /**
         * @param tc the tc to set
         */
        public void setTc(String tc) {
            this.tc.set(tc);
        }

        /**
         * @return the hesapNo
         */
        public String getHesapNo() {
            return hesapNo.get();
        }

        /**
         * @param hesapNo the hesapNo to set
         */
        public void setHesapNo(String hesapNo) {
            this.hesapNo.set(hesapNo);
        }

        /**
         * @return the guncelle
         */
        public Button getGuncelle() {
            return guncelle;
        }

        /**
         * @param guncelle the guncelle to set
         */
        public void setGuncelle(Button guncelle) {
            this.guncelle = guncelle;
        }

        /**
         * @return the hesapIslemleri
         */
        public Button getHesapIslemleri() {
            return hesapIslemleri;
        }

        /**
         * @param hesapIslemleri the hesapIslemleri to set
         */
        public void setHesapIslemleri(Button hesapIslemleri) {
            this.hesapIslemleri = hesapIslemleri;
        }

    }

    // hesaplar son
    //////////////////////////////////////////
    @FXML
    private void geri(ActionEvent event) {
        PaneEkle.setVisible(false);
        PaneGoruntule.setVisible(false);
        PaneAna.setVisible(true);
        PaneHesapIslemleri.setVisible(false);
        hitablosu.setVisible(false);
        sbakiye.setText("");
        sborc.setText("");
        ssimdikiFaiz.setText("");
        sgelecektekiFaiz.setText("");
        kilitDurumu.setText("");
        paraCekUyari.setText("");
        paraYatirUyari.setText("");
    }

    //////////////////////////////////////////////////////////////////////
    // Hesap İşlemleri Başlagıç
    @FXML
    TextField htekle, htekle1;

    @FXML
    Label hlbakiye, hlbakiye1, paraYatirUyari, paraCekUyari, kilitDurumu;

    String hesapDurumu;

    @FXML
    private void bakiye() {
        hitablosu.setVisible(false);
        sbakiye.setText("");
        sborc.setText("");
        ssimdikiFaiz.setText("");
        sgelecektekiFaiz.setText("");
        kilitDurumu.setText("");
        paraCekUyari.setText("");
        paraYatirUyari.setText("");
        try {
            String query = "select * from hesaplar where hesap_no='" + aktifHesap + "'";
            Statement stm = (Statement) con.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                hlbakiye.setText(rs.getString("bakiye"));
                hesapDurumu = rs.getString("hesap_durumu");
            }

        } catch (SQLException ex) {
            System.out.println("bakiye listesi çekilemedi");
        }
    }

    @FXML
    private void paraYatirCek() {
        if (hesapDurumu.equals("1")) {
            int bakiyeInt = Integer.valueOf(hlbakiye.getText()) + Integer.valueOf(htekle.getText());
            String query2 = "update hesaplar set bakiye='" + bakiyeInt + "' where hesap_no='" + aktifHesap + "'";
            try {
                Statement stm;
                stm = (Statement) con.createStatement();
                stm.executeUpdate(query2);

                Date d = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                query2 = "insert into hesap_islemleri set hesap_no='" + aktifHesap + "', yapilan_islem='" + (Integer.valueOf(htekle.getText()) + " TL yatirildi.") + "', islem_tarihi='" + dateFormat.format(date) + "'";
                stm = (Statement) con.createStatement();
                stm.executeUpdate(query2);
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            bakiye();
        } else {
            paraYatirUyari.setText("Hesap Kilitli");
        }
    }

    @FXML
    private void paraCek() {
        if (hesapDurumu.equals("1")) {
            int bakiyeInt = Integer.valueOf(hlbakiye1.getText()) - Integer.valueOf(htekle1.getText());
            String query2 = "update hesaplar set bakiye='" + bakiyeInt + "' where hesap_no='" + aktifHesap + "'";
            try {
                Statement stm;
                stm = (Statement) con.createStatement();
                stm.executeUpdate(query2);

                Date d = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                query2 = "insert into hesap_islemleri set hesap_no='" + aktifHesap + "', yapilan_islem='" + (Integer.valueOf(htekle1.getText()) + " TL cekildi.") + "', islem_tarihi='" + dateFormat.format(date) + "'";
                stm = (Statement) con.createStatement();
                stm.executeUpdate(query2);
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            bakiye1();
        } else {
            paraCekUyari.setText("Hesap Kilitli");
        }

    }

    @FXML
    private void kilitleKaldir() {
        bakiye();
        int yeni = 0;
        if (hesapDurumu.equals("1")) {
            yeni = 0;
            hesapDurumu = String.valueOf(yeni);
        } else {
            yeni = 1;
            hesapDurumu = String.valueOf(yeni);
        }
        String query = "update hesaplar set hesap_durumu='" + hesapDurumu + "' where hesap_no='" + aktifHesap + "'";
        Statement stm;
        try {
            stm = (Statement) con.createStatement();
            stm.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("javafxapplication3.FXMLDocumentController.kilitleKaldir()");
        }
        if (hesapDurumu.equals("1")) {
            kilitDurumu.setText("Hesap islemlere acik");
        } else {
            kilitDurumu.setText("Hesap islemlere kapali");
        }

    }

    @FXML
    private void bakiye1() {
        hitablosu.setVisible(false);
        sbakiye.setText("");
        sborc.setText("");
        ssimdikiFaiz.setText("");
        sgelecektekiFaiz.setText("");
        kilitDurumu.setText("");
        paraCekUyari.setText("");
        paraYatirUyari.setText("");
        try {
            String query = "select * from hesaplar where hesap_no='" + aktifHesap + "'";
            Statement stm = (Statement) con.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                hlbakiye1.setText(rs.getString("bakiye"));
                hesapDurumu = rs.getString("hesap_durumu");
            }

        } catch (SQLException ex) {
            System.out.println("bakiye listesi çekilemedi");
        }
    }

    @FXML
    private TableView<HesapIslemleri> hitablosu = new TableView<HesapIslemleri>();

    private void hesapIslemleriTablosunuDoldur() {
        hitablosu.setEditable(true);

        // burayı veri tabanından çekecez
        TableColumn tislem = new TableColumn("Islem");
        tislem.setMinWidth(300);
        tislem.setCellValueFactory(
                new PropertyValueFactory<HesapIslemleri, String>("islem")
        );

        TableColumn ttarihi = new TableColumn("Tarihi ");
        ttarihi.setMinWidth(300);
        ttarihi.setCellValueFactory(
                new PropertyValueFactory<Hesaplar, String>("tarihi")
        );

        final ObservableList<HesapIslemleri> data = FXCollections.observableArrayList();

        try {
            String query = "select * from hesap_islemleri where hesap_no='" + aktifHesap + "'";
            Statement stm = (Statement) con.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                String cislem = rs.getString("yapilan_islem");
                String ctarihi = rs.getString("islem_tarihi");

                HesapIslemleri row = new HesapIslemleri(cislem, ctarihi);
                data.add(row);
            }

        } catch (SQLException ex) {
            System.out.println("islemler listesi çekilemedi");
        }

        hitablosu.setItems(data);
        hitablosu.getColumns().addAll(tislem, ttarihi);
    }

    @FXML
    private void hesapGoruntule() {
        hitablosu.setVisible(true);
        hitablosu.getColumns().clear();
        hesapIslemleriTablosunuDoldur();
        try {
            int hesapTuru=0;
            String query = "select * from hesaplar where hesap_no='" + aktifHesap + "'";
            Statement stm = (Statement) con.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                int bakiye = rs.getInt("bakiye");
                hesapTuru=rs.getInt("hesap_tipi");
                if(bakiye<0) {
                    String msg = (bakiye*-1)+"Tl Borcunuz var";
                   sbakiye.setText(msg); 
                }else {
                    sbakiye.setText(""+bakiye+"Tl Paraniz var"); 
                }
               
                
                if( hesapTuru == 1 ||  hesapTuru == 4){
                ssimdikiFaiz.setText("0");
                sgelecektekiFaiz.setText("0");
                }
                else if(hesapTuru == 2){
                ssimdikiFaiz.setText(""+((bakiye*8*2)/3600));
                sgelecektekiFaiz.setText(""+((bakiye*8*3600)/3600));
                }
                 else if(hesapTuru == 3){
                ssimdikiFaiz.setText(""+((bakiye*14*2)/3600));
                sgelecektekiFaiz.setText(""+((bakiye*14*3600)/3600));
                }
                
            }

        } catch (SQLException ex) {
            System.out.println("bakiye listesi çekilemedi");
        }
    }

    public static class HesapIslemleri {

        /**
         * @return the islem
         */
        public String getIslem() {
            return islem.get();
        }

        /**
         * @param islem the islem to set
         */
        public void setIslem(String islem) {
            this.islem.set(islem);
        }

        /**
         * @return the tarihi
         */
        public String getTarihi() {
            return tarihi.get();
        }

        /**
         * @param tarihi the tarihi to set
         */
        public void setTarihi(String tarihi) {
            this.tarihi.set(tarihi);
        }

        private SimpleStringProperty islem;
        private SimpleStringProperty tarihi;

        public HesapIslemleri(String islem, String tarihi) {
            this.islem = new SimpleStringProperty(islem);
            this.tarihi = new SimpleStringProperty(tarihi);
        }

    }

    @FXML
    Label sbakiye, sborc, ssimdikiFaiz, sgelecektekiFaiz;

    // Hesap İşlemleri Son
    /////////////////////////////////////////////////////////////////////
    @FXML
    private void cikis(){
        PaneAna.getScene().getWindow().hide();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hesapTuru.getItems().addAll("Cari Hesap", "Uzun Vadeli Hesap", "Kısa Vadeli Hesap", "Vadesiz Hesap");
        try {
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/odev", "root", "");
            /*
            String query = "insert into musteriler set musteri_adi='enes', musteri_soyadi='Akdoğan', musteri_tc='123123', hesap_no='11111'";

            Statement stm = (Statement) con.createStatement();
            int executeUpdate = stm.executeUpdate(query);
             */

        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

}
