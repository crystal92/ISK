package com.softtechdesign.ga.examples;

import com.softtechdesign.ga.*;
import java.util.*;

/**
    Traveling salesman problem. A salesman has to travel to N different cities. In what
  sequence should he visit each city to minimize the total distance traveled. Each city
  is represented in the chromosome string as a letter (e.g. 'A' or 'B').

  To simplify the mathematics of the fitness function, this example reduces the coordinate
  space to one dimension. Each city (or node) has a position given by one coordinate.
  This model can be extrapolated to 2 or 3 dimensions by giving each city (or node) a 2
  dimensional (X,Y) or 3 dimensional (X,Y,Z) coordinate and then modifying the distance
  calculating function accordingly.

  If a chromosome = 'ABCDEFGHIJKLMNOPQRST', then the fitness is evaluated as
    fitness = Dist(A, B) + Dist(B, C) + Dist(C, D)....+ Dist(S, T)
  Higher fitness values mean a higher probability that this chromosome will reproduce.

  The possible combinations (sequences of cities) is N factorial. For 20 cities, the possible
  combinations = 20! or 2.432902008177e+18.
  This is an enormous number. If you tried every combination of sequences and could test
  10,000 of those sequences per second (looking for the minium), it would take your
  computer 8 million years to randomly come up with the minimum (ideal) sequence.
*/

public class GASalesman extends GASequenceList
{
    public GASalesman() throws GAException
    {
        super(  5, //size of chromosome
                300, //population has N chromosomes
                0.7, //crossover probability
                10, //random selection chance % (regardless of fitness)
                3000, //max generations
                0, //num prelim runs (to build good breeding stock for final/full run)
                25, //max generations per prelim run
                0.06, //chromosome mutation prob.
                5, //number of decimal places in chrom
                "ABCDE", //gene space (possible gene values)
                Crossover.ctTwoPoint, //crossover type
                true); //compute statisitics?
       
        setInitialSequence();
    }

    void setInitialSequence()
    {
        int []tab={35,15,50,25,25};
        //initialize one dimensional locations (e.g. traveling salesman's cities)
        for (int i = 0; i < chromosomeDim; i++)
            this.sequence[i] = tab[i];
    }
    
    /** Fitness function for GASalesman now access genes directly through genes[] array
     * Old benchmark: 29 sec. New benchmark 16 sec.
     */
    protected double getFitness(int iChromIndex)
    {
        double pojemnosc_pudelka, rozmiar_elementu, ilosc_pudelek=0;
        int geneIndex1;//nazwa genu/elementu podanego w konstruktorze

        pojemnosc_pudelka = 50;
        
        
        char genes[] = this.getChromosome(iChromIndex).getGenes();
        int lenChromosome = genes.length;
        if(lenChromosome!=0)
            ilosc_pudelek=1;
           // System.out.println(" cycek ");
        
        for (int i = 0; i < lenChromosome; i++)
        {
            geneIndex1 = this.possGeneValues.indexOf(genes[i]);  //optimize this
            
            rozmiar_elementu = this.sequence[geneIndex1];
            
            
            if(pojemnosc_pudelka<rozmiar_elementu){
                pojemnosc_pudelka=50-rozmiar_elementu;
                ilosc_pudelek++;
                 
            }
            else
                pojemnosc_pudelka-=rozmiar_elementu;
            
        }

        return -ilosc_pudelek;
    }
    
    public static void main(String[] args)
    {
        String startTime = new Date().toString();
        System.out.println("GASalesman GA..." + startTime);
        
        try
        {
            GASalesman salesman = new GASalesman();
            Thread threadSalesman = new Thread(salesman);
            threadSalesman.start();
        }
        catch (GAException gae)
        {
            System.out.println(gae.getMessage());
        }
        
        System.out.println("Process started at " + startTime + ". Process completed at " +  new Date().toString());
    }

}