package testCase;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import clase.AeronavaCargo;
import clase.AeronavaFactory;
import clase.AeronavaPasageri;
import clase.Cargo;
import clase.Dispecerat;
import clase.GestiuneAeronava;
import clase.GestiuneCargo;
import clase.GestiunePasageri;
import clase.Pasager;
import clase.TipMarfa;
import enumerari.TipAeronava;

public class CaseTest {
	String numeFisier = "DateTestare.ser";
	//ne propunem sa alocam memorie pentru doua obiecte si sa le verificam cu 
	//metodele Before si After
	AeronavaCargo aeronavaCargoBefore = null;
	AeronavaPasageri aeronavaPasageriAfter = null;
	
	@After
	public void testareAfterCreareObiecte(){
		assertNotNull("aeronavaCargoCorrect trebuie sa nu fie null",aeronavaCargoBefore);
		aeronavaPasageriAfter = (AeronavaPasageri)AeronavaFactory.creazaAeronava(TipAeronava.PASAGERI);
	}
	@Before
	public void testareBeforeCreareObiecte(){
		aeronavaCargoBefore = (AeronavaCargo)AeronavaFactory.creazaAeronava(TipAeronava.CARGO);
//		assertNotNull("aeronavaPasageriAfter trebuie sa nu fie null",aeronavaPasageriAfter);
	}
	//ne propunem sa cream un fisier de date populat cu o lista de aeronava pe care sa le citim ulterior
	@Before
	public void testareFisierDateScriere(){
		Dispecerat.getInstance().adaugaAeronava(TipAeronava.CARGO);
		Dispecerat.getInstance().adaugaAeronava(TipAeronava.PASAGERI);
		GestiuneCargo aeronavaCargoFisier = (GestiuneCargo)Dispecerat.getInstance().getAeronavaDupaId(0);
		aeronavaCargoFisier.adaugaMarfa(new Cargo("Monitoare", 250));
		GestiunePasageri aeronavaPasageriFisier = (GestiunePasageri)Dispecerat.getInstance().getAeronavaDupaId(1);
		aeronavaPasageriFisier.setNumarLocuri(200);
		aeronavaPasageriFisier.adaugaPasager(new Pasager("Mirica", "Horatiu", 5));
		Dispecerat.getInstance().salveazaDate(numeFisier);
	}
	@After
	public void testareFisierDateCitire(){
		Dispecerat.getInstance().incarcaDate(numeFisier);
		GestiuneCargo aeronavaCargoFisier = (GestiuneCargo)Dispecerat.getInstance().getAeronavaDupaId(0);
		assertNotNull("aeronavaCargoFisier trebuie sa nu fie null", aeronavaCargoFisier);
		assertNotNull("Nu exista marfa", aeronavaCargoFisier.getListaMarfa().get(0));
		GestiunePasageri aeronavaPasageriFisier = (GestiunePasageri)Dispecerat.getInstance().getAeronavaDupaId(1);
		assertNotNull("aeronavaPasageriFisier trebuie nu fie null",aeronavaPasageriFisier);
		assertNotNull("Nu exista Pasageri",aeronavaPasageriFisier.getListaPasageri().get(0));
	}
	@Test
	public void SingletoneDispeceratTest(){
		assertSame(Dispecerat.getInstance(), Dispecerat.getInstance());
	}	

	//ne propunem sa testam daca Design paternul Factory functioneaza corespunzator
	//creeandu-ne doua obiecte, AeronavaCargo si AeronavaPasageri, in urmatoarele doua metode
	@Test
	public void FactoryAeroanvaCargoTest(){
		AeronavaCargo aeroanvaCargo = (AeronavaCargo)AeronavaFactory.creazaAeronava(TipAeronava.CARGO);
		assertNotNull(aeroanvaCargo);
		assertNotNull(aeroanvaCargo.getNumeAeronava());
	}
	
	@Test
	public void FactoryAeroanvaPasageriTest(){
		AeronavaPasageri aeronavaPasageri = (AeronavaPasageri)AeronavaFactory.creazaAeronava(TipAeronava.PASAGERI);
		assertNotNull(aeronavaPasageri);
		assertTrue(aeronavaPasageri.getNumeAeronava() != null);
	}
	//ne propunem sa testam DP-ul Builder daca la crearea unei de gestionare a marfii ne sunt alocate memorie
	//pentru toate obiectele si anularea zborului este implicit falsa in urmatoarele 2 metode
	@Test
	public void BuilderAeronavaCargoTest(){
		GestiuneCargo gestiuneCargo = (GestiuneCargo)Dispecerat.getInstance().adaugaAeronava(TipAeronava.CARGO);
		assertFalse(gestiuneCargo.esteAnulatZborul());
		assertNotNull(gestiuneCargo.getAeronava());
	}
	
	@Test
	public void BuilderAeronavaPasageriTest(){
		GestiunePasageri gestiunePasageri = (GestiunePasageri)Dispecerat.getInstance().adaugaAeronava(TipAeronava.PASAGERI);
		assertFalse(gestiunePasageri.esteAnulatZborul());
		assertNotNull(gestiunePasageri.getAeronava());
	}
	//ne propunem sa testam daca la adaugarea unui pasager ne sunt setate implicit cu false
	//checkin-ul respectiv anularea zborului si daca interfata implementata (metoda anuleazaZbor) functioneaza corespunzator
	@Test
	public void PasagerTest(){
		TipMarfa pasager = new Pasager("Mirica", "Horatiu", 12);
		assertFalse(((Pasager)pasager).getCheckIn());
		assertFalse(((Pasager)pasager).esteZborulAnulat());
		pasager.anuleazaZbor();
		assertTrue(((Pasager)pasager).esteZborulAnulat());
	}
	//ne propunem sa testam daca la adaugarea unui tip de marfa ne este setate implicit cu false
	//anularea zborului si daca interfata implementata (metoda anuleazaZbor) functioneaza corespunzator
	@Test
	public void CargoTest(){
		TipMarfa cargo = new Cargo("Telefoane", 120);
		assertFalse(((Cargo)cargo).esteAnulatZborul());
		cargo.anuleazaZbor();
		assertTrue(((Cargo)cargo).esteAnulatZborul());
	}
	
	//vrem sa testam daca anularea zborului, si tipul de marfa este anulata odata cu ea, pentru cele doua
	//tipuri de aeronave
	@Test
	public void GestiuneAeronavaCargoTest(){
		GestiuneCargo gestiuneCargo = (GestiuneCargo)Dispecerat.getInstance().adaugaAeronava(TipAeronava.CARGO);
		gestiuneCargo.adaugaMarfa(new Cargo("Televizoare", 430));
		gestiuneCargo.adaugaMarfa(new Cargo("Telefoane", 320));
		assertFalse(gestiuneCargo.esteAnulatZborul());
		assertFalse(gestiuneCargo.getListaMarfa().get(0).esteAnulatZborul());
		assertFalse(gestiuneCargo.getListaMarfa().get(1).esteAnulatZborul());
		gestiuneCargo.anuleazaZborul();		
		assertTrue(gestiuneCargo.esteAnulatZborul());
		assertTrue(gestiuneCargo.getListaMarfa().get(0).esteAnulatZborul());
		assertTrue(gestiuneCargo.getListaMarfa().get(1).esteAnulatZborul());
	}
	
	@Test
	public void GestiuneAeronavaPasageriTest(){
		GestiunePasageri gestiunePasageri = (GestiunePasageri)Dispecerat.getInstance().adaugaAeronava(TipAeronava.PASAGERI);
		gestiunePasageri.adaugaPasager(new Pasager("Mirica", "Horatiu", 12));
		gestiunePasageri.adaugaPasager(new Pasager("Iliescu", "Stefania", 6));
		assertFalse(gestiunePasageri.esteAnulatZborul());
		assertFalse(gestiunePasageri.getListaPasageri().get(0).esteZborulAnulat());
		assertFalse(gestiunePasageri.getListaPasageri().get(1).esteZborulAnulat());				
		gestiunePasageri.anuleazaZborul();		
		assertTrue(gestiunePasageri.esteAnulatZborul());
		assertTrue(gestiunePasageri.getListaPasageri().get(0).esteZborulAnulat());
		assertTrue(gestiunePasageri.getListaPasageri().get(1).esteZborulAnulat());
	}
	//vrem sa vedem daca la creearea unui obiect GestiuneAeronava este alocata memorie si pentru
	//aeronava corespunzator
	@Test
	public void GestiuneAeronavaTest(){
		GestiuneAeronava gestiuneAeronavaCargo = Dispecerat.getInstance().adaugaAeronava(TipAeronava.CARGO);
		assertNotNull(gestiuneAeronavaCargo.getAeronava());
		assertTrue(gestiuneAeronavaCargo instanceof GestiuneCargo);
		GestiuneAeronava gestiuneAeronavaPasageri = Dispecerat.getInstance().adaugaAeronava(TipAeronava.PASAGERI);
		assertNotNull(gestiuneAeronavaPasageri.getAeronava());
		assertTrue(gestiuneAeronavaPasageri instanceof GestiunePasageri);
	}
	
}
