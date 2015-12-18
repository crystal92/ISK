/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.packing;

import com.softtechdesign.ga.GAException;
import com.softtechdesign.ga.GASequenceList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import com.softtechdesign.ga.*;
import java.io.*;
import javax.swing.*;
//import javax.swing.*;

/**
 *
 * @author Crystal
 */
public class BinPacking extends GASequenceList {

    int rozmiar_zadany_pudelka;
    int pudelko[];
    int zawartosc_pudelka[][];
    int ilosc_elementow_w_pudelku[];

    public BinPacking(int[] dane, String indeksy_elementow) throws GAException {
        super(dane[1], //size of chromosome
                300, //population has N chromosomes 300
                1.7, //crossover probability
                10, //random selection chance % (regardless of fitness) 10
                500, //max generations 2000
                0, //num prelim runs (to build good breeding stock for final/full run)
                25, //max generations per prelim run
                0.06, //chromosome mutation prob. 
                5, //number of decimal places in chrom
                indeksy_elementow, //gene space (possible gene values)
                Crossover.ctTwoPoint, //crossover type
                true); //compute statisitics?

        ilosc_elementow_w_pudelku = new int[dane[1] + 1];
        zawartosc_pudelka = new int[dane[1] + 1][dane[1] + 1];
        rozmiar_zadany_pudelka = dane[0];
        pudelko = new int[dane[1] + 1];
        setInitialSequence(dane);
    }

    public static String generuj_index_element(int liczba_elementow) {
        String znaki = "";
        char a = 'A';

        for (int i = 0; i < liczba_elementow; i++) {
            znaki += a;
            a++;
        }

        return znaki;
    }

    void setInitialSequence(int[] elementy) {
        int[] tab = {35, 15, 50, 25, 25};
        //initialize one dimensional locations (e.g. traveling salesman's cities)
        for (int i = 0; i < chromosomeDim; i++) {
            this.sequence[i] = elementy[i + 2];
        }
    }

    protected double getFitness(int iChromIndex) {
        int pojemnosc_pudelka, rozmiar_elementu, ilosc_pudelek = 0;
        int geneIndex1;//nazwa genu/elementu podanego w konstruktorze

        pojemnosc_pudelka = 0;

        char genes[] = this.getChromosome(iChromIndex).getGenes();
        int lenChromosome = genes.length;
        if (lenChromosome != 0) {
            ilosc_pudelek = 1;
            pudelko[1] = 0;
            ilosc_elementow_w_pudelku[1] = 0;
        }
        // System.out.println(" cycek ");

        for (int i = 0; i < lenChromosome; i++) {
            geneIndex1 = this.possGeneValues.indexOf(genes[i]);  //optimize this

            rozmiar_elementu = (int) this.sequence[geneIndex1];

            //sprawdza wszystkie pudełka od pierwszego czy konkretny element się zmieści
            //jak się zmieści przerywa sprawdzanie
            for (int j = 1; j <= ilosc_pudelek; j++) {

                if (pudelko[j] + rozmiar_elementu <= rozmiar_zadany_pudelka + 1) {
                    pudelko[j] += rozmiar_elementu;
                    ilosc_elementow_w_pudelku[j]++;
                    zawartosc_pudelka[j][ilosc_elementow_w_pudelku[j]] = genes[i];  //index elementu w pudełku

                    break;
                } else if (j == ilosc_pudelek) {
                    ilosc_pudelek++;
                    ilosc_elementow_w_pudelku[ilosc_pudelek] = 1;
                    zawartosc_pudelka[ilosc_pudelek][ilosc_elementow_w_pudelku[ilosc_pudelek]] = genes[i];
                    pudelko[ilosc_pudelek] = rozmiar_elementu;
                    break;
                }
            }
        }
        return (double) -ilosc_pudelek;
    }

    protected double getFitness2(int iChromIndex) {
        int pojemnosc_pudelka, rozmiar_elementu, ilosc_pudelek = 0;
        int geneIndex1;//nazwa genu/elementu podanego w konstruktorze

        pojemnosc_pudelka = 0;

        char genes[] = this.getChromosome(iChromIndex).getGenes();
        int lenChromosome = genes.length;
        
        if (lenChromosome != 0) {
            ilosc_pudelek = 1;
            pudelko[1] = 0; //rozmiar aktualny danego pudełka
            ilosc_elementow_w_pudelku[1] = 0;
        }
        // System.out.println(" cycek ");

        for (int i = 0; i < lenChromosome; i++) {
            geneIndex1 = this.possGeneValues.indexOf(genes[i]);  //optimize this

            rozmiar_elementu = (int)this.sequence[geneIndex1];

            if (pudelko[ilosc_pudelek] + rozmiar_elementu > rozmiar_zadany_pudelka + 1) {
                //pojemnosc_pudelka = rozmiar_elementu;
                
               
                //zawartosc_pudelka[i][ilosc_elementow_w_pudelku[i]] = genes[i];
                
                ilosc_pudelek++;
                ilosc_elementow_w_pudelku[ilosc_pudelek] = 1;
                zawartosc_pudelka[ilosc_pudelek][ilosc_elementow_w_pudelku[ilosc_pudelek]] = genes[i];
                

            } else { 
                pudelko[ilosc_pudelek] += rozmiar_elementu;
                ilosc_elementow_w_pudelku[ilosc_pudelek]++;
               
               // pojemnosc_pudelka += rozmiar_elementu;
                zawartosc_pudelka[ilosc_pudelek][ilosc_elementow_w_pudelku[ilosc_pudelek]] = genes[i];
            }

        }

        return (double)-ilosc_pudelek;
    }

//////////////////////////////////////////////////////////////////////////////////////MAIN
    /*
    public static void main(String[] args) throws FileNotFoundException {

        //String plik=JOptionPane.showInputDialog("Podaj ścieżkę do pliku:");
        //String plik="C:/dane.txt";
        
        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        //File plik = fc.getSelectedFile();
        
        int[] dane_z_pliku = czytaj_plik(fc.getSelectedFile());//dziala(bez obslugi wyjatkow}
        String indeksy = generuj_index_element(dane_z_pliku[1]);

        //wyswietl:
        //for(int i=0;i<dane_z_pliku[1]+2;i++)
        //      System.out.print(dane_z_pliku[i]  + " | ");
        //int liczba_elementow=dane_z_pliku[1];
        try {
            BinPacking test = new BinPacking(dane_z_pliku, indeksy);
            Thread threadBinpacking = new Thread(test);
            threadBinpacking.start();

            //int zawartosc_pudelka[][];   
            //int ilosc_elementow_w_pudelku[];
            while (threadBinpacking.isAlive());

            System.out.println("oooo " + test.getFittestChromosome());

            for (int i = 1; i <= (int) (test.getFittestChromosomesFitness() * -1); i++) {

                System.out.print(i + ".  ");

                for (int j = 1; j <= test.ilosc_elementow_w_pudelku[i]; j++) {

                    System.out.print((char) test.zawartosc_pudelka[i][j] + " | ");
                }
                System.out.println(" ");

            }

        } catch (GAException gae) {
            System.out.println(gae.getMessage());
        }

    }
*/
    static public int[] czytaj_plik(File plik) throws FileNotFoundException {
        
        //final JFileChooser fc = new JFileChooser();
       // fc.showOpenDialog(null);
        //File plik = fc.getSelectedFile();
        
        System.out.println("* Wczytuje plik. \n");
        
        Scanner input = new Scanner(plik);
        String dane = input.nextLine();
        
        int pojemnosc_kubelkow = Integer.parseInt(dane.substring(dane.indexOf('<') + 1, dane.indexOf('>')));//dobre
       
        dane = input.nextLine();
        
        int ilosc_elementow = Integer.parseInt(dane.substring(dane.indexOf('<') + 1, dane.indexOf('>')));//dobre
        
        int[] tablica = new int[ilosc_elementow + 2];
        tablica[0] = pojemnosc_kubelkow;
        tablica[1] = ilosc_elementow;
       
        for (int i = 0; i < ilosc_elementow; i++) {
            dane = input.nextLine();
            tablica[i + 2] = Integer.parseInt(dane.substring(dane.indexOf('<') + 1, dane.indexOf('>')));//dobre
            if (tablica[i + 2] > pojemnosc_kubelkow) {
                JOptionPane frame = new JOptionPane();
                JOptionPane.showMessageDialog(frame,
                        "Rozmiar elementu przekracza rozmiar pudełka.",
                        "Błąd pliku wsadowego",
                        JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
        //test:
        //System.out.println("Pojemność kubełka: "+ pojemnosc_kubelkow);
        //System.out.println("Ilość elementów: " + ilosc_elementow);
        System.out.print("Tablica elementów: ");
        for (int i = 0; i < ilosc_elementow + 2; i++) {
            System.out.print(tablica[i] + " | ");
        }
        System.out.println(" \n ");
        return tablica;
    }

}
