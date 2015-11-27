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
public class BinPacking extends GASequenceList
{
    int rozmiar_zadany_pudelka;
    
    public BinPacking(int []dane,String indeksy_elementow) throws GAException
    {
        super(  dane[1], //size of chromosome
                300, //population has N chromosomes 300
                0.7, //crossover probability
                100, //random selection chance % (regardless of fitness) 10
                5000, //max generations 2000
                0, //num prelim runs (to build good breeding stock for final/full run)
                25, //max generations per prelim run
                0.06, //chromosome mutation prob. 
                5, //number of decimal places in chrom
                indeksy_elementow, //gene space (possible gene values)
                Crossover.ctTwoPoint, //crossover type
                true); //compute statisitics?
        
        rozmiar_zadany_pudelka=dane[0];
        setInitialSequence(dane);
    }
    
    public static String generuj_index_element(int liczba_elementow)
    {
        String znaki="";
        char a ='A';
        
        for(int i=0;i<liczba_elementow;i++){
            znaki+=a;
            a++;
        }
        
        return znaki;
    }
    
    void setInitialSequence(int[]elementy)
    {
        int []tab={35,15,50,25,25};
        //initialize one dimensional locations (e.g. traveling salesman's cities)
        for (int i = 0; i < chromosomeDim; i++)
            this.sequence[i] = elementy[i+2];
    }
    
    protected double getFitness(int iChromIndex)
    {
        double pojemnosc_pudelka, rozmiar_elementu, ilosc_pudelek=0;
        int geneIndex1;//nazwa genu/elementu podanego w konstruktorze

        pojemnosc_pudelka = 0;
        
        
        char genes[] = this.getChromosome(iChromIndex).getGenes();
        int lenChromosome = genes.length;
        if(lenChromosome!=0)
            ilosc_pudelek=1;
           // System.out.println(" cycek ");
        
        for (int i = 0; i < lenChromosome; i++)
        {
            geneIndex1 = this.possGeneValues.indexOf(genes[i]);  //optimize this
            
            rozmiar_elementu = this.sequence[geneIndex1];
            
            
            if(pojemnosc_pudelka+rozmiar_elementu>  rozmiar_zadany_pudelka  ){
                pojemnosc_pudelka=rozmiar_elementu;
                ilosc_pudelek++;
                 
            }
            else
                pojemnosc_pudelka+=rozmiar_elementu;
            
        }

        return -ilosc_pudelek;
    }
    
//////////////////////////////////////////////////////////////////////////////////////MAIN
    public static void main(String[] args) throws FileNotFoundException
    {
        
        String plik=JOptionPane.showInputDialog("Podaj ścieżkę do pliku:");
        
        //String a="C:/dane.txt";
        int []dane_z_pliku = czytaj_plik(plik);//dziala(bez obslugi wyjatkow}
        String indeksy = generuj_index_element(dane_z_pliku[1]);
        
        //wyswietl:
        //for(int i=0;i<dane_z_pliku[1]+2;i++)
        //      System.out.print(dane_z_pliku[i]  + " | ");
        
        //int liczba_elementow=dane_z_pliku[1];
        
        try
        {
            BinPacking test = new BinPacking(dane_z_pliku,indeksy);
            Thread threadSalesman = new Thread(test);
            threadSalesman.start();
        }
        catch (GAException gae)
        {
            System.out.println(gae.getMessage());
        }
        
        
    }
    
    static public int[] czytaj_plik(String sciezka_pliku ) throws FileNotFoundException
    {

        System.out.println("* Wczytuje plik. \n");
        
        File plik = new File(sciezka_pliku);
        
        Scanner input = new Scanner(plik);
        String dane = input.nextLine();
        
        int pojemnosc_kubelkow = Integer.parseInt( dane.substring( dane.indexOf('<')+1, dane.indexOf('>') ) );//dobre
        
        dane = input.nextLine();
        //System.out.println(dane);
        
        int ilosc_elementow = Integer.parseInt( dane.substring( dane.indexOf('<')+1, dane.indexOf('>') ) );//dobre
        
        
        //int []rozmiar_elementu_tab= new int[ilosc_elementow];
        
        int []tablica = new int[ilosc_elementow+2];
        
        tablica[0]=pojemnosc_kubelkow;
        tablica[1]=ilosc_elementow;
        
        for(int i=0;i<ilosc_elementow;i++)
        {
            dane = input.nextLine();
            tablica[i+2] = Integer.parseInt( dane.substring( dane.indexOf('<')+1, dane.indexOf('>') ) );//dobre
        }
        //test:
        //System.out.println("Pojemność kubełka: "+ pojemnosc_kubelkow);
        //System.out.println("Ilość elementów: " + ilosc_elementow);
        
        System.out.print("Tablica elementów: ");
        
        for(int i=0;i<ilosc_elementow+2;i++)
            System.out.print(tablica[i]  +" | ");
        
        System.out.println(" \n ");
        
        return tablica;
    }


}

    

