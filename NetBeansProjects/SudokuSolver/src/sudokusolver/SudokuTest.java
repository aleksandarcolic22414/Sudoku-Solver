/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

import java.util.Scanner;

/**
 *
 * @author Korisnik
 */
public class SudokuTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
//      Pri unosu vrednosti redova, prazna polja uneti kao '0' !!!
        SudokuFunkcije.ucitaj(); 
        
        if (SudokuFunkcije.resi(SudokuFunkcije.getSudoku()) == true) {
            System.out.println("\nResenje:\n");
            SudokuFunkcije.stampaj();
        } else {
            System.out.println("Nema resenja");
        }
        
    }
    
}
