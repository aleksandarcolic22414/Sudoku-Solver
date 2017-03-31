package sudokusolver;

import java.util.Scanner;

/**
 *
 * @author Aleksandar Colic, 2016;
 */
public class SudokuFunkcije {
    
    /**  Dvodimenzionalni niz karaktera koji predstavlja sudoku polje za igru. */
    private static char sudoku[][] = new char [9][9];

    /**  Duzina jednog reda i kolone. */
    private static final int duzina = 9;

    /**  Zbir elemenata jednog reda.
    *    Ovo sranje je uvedeno zbog kasnije optimizacije.
    *    Trenutno nema nikakvu funkciju. */
    private static final int sumaReda = 45;
    
    /**  Ukupni broj elemenata, odnosno velicina polja za igru. */
    private static final int ukupniBrojElemenata = duzina * duzina;
   
    /**  Konstanta koja predstavlja najmanju dozvoljenu vrednost u polju. */
    public static final char minimalniKarakter = '1';
    
    /**  Konstanta koja predstavlja najvecu dozvoljenu vrednost u polju. */
    public static final char maximalniKarakter = '9';
   
    /**
     * Funkcija koja stampa polje za igru.
     */
    public static void stampaj() {
        for (int i = 0; i < duzina; i++) {
            for (int j = 0; j < duzina; j++) {
                System.out.printf("%c%s", 
                        sudoku[i][j], 
                        j == 8 ? "\n" : " "
                );
            }
        }
    }
    
    /**
     * Funkcija koja vraca polje za igru.
     * @return polje za igru.
     */
    public static char [][] getSudoku(){
        return sudoku;
    }
    
    /**
     * Obezbedjuje ucitavanje sudoku igre sa tastature.
     * Unose se celi brojevi bez razmaka u vrednosti od
     * 1 do 9.Unosi se red po red.Kada se unese jedan red, lupi se enter.
     * Onda se istim postupkom unese jos 8 redova.
     * Napomena:
     * Umesto praznih karaktera (praznih polja u igri) unose se nule ('0').
     * Lako se moze obezbediti da se cela igra unese u jednom redu, ali je
     * ovako lakse za ispravljanje gresaka prilikom unosa.
     */
    public static void ucitaj() {

        final Scanner in = new Scanner(System.in);
        String red;

        try {
            for (int i = 0; i < duzina; i++) {
                red = in.nextLine();
                for (int j = 0 ; j < duzina; j++) {
                    sudoku[i][j] = red.charAt(j);
                }
            }
        } catch (Exception E) {
            System.out.println("Pogresan ulaz.");
        }
    }

    /**
     * Jedina slozenija funkcija u celom programu.
     * Ideja je vrlo jednostavna.Trazi se prvo slobodno polje i u njega
     * se unosi vrednost koja se povecava u svakoj iteraciji dok se ne nadje
     * ona koja odgovara.Vrednost naravno ide od '1' do '9'.Ukoliko se nadje vrednost koja,
     * po pravilima igre, moze da se stavi u to polje, ona se stavi i ponovo
     * se pozove funkcija resi.Isti postupak se rekurzivno ponavlja dok se ne dodje do
     * resenja.
     * Pre ulaska u glavni deo funkcije,vrsi se provera resenja.
     * To, naravno, nije optimalno ali radi lakseg razumevanja programa, ostace
     * kako jeste. Sada najbitnija stvar:
     * Ukoliko u nekom trenutku promenljiva noviKarakter dodje do vrednosti 
     * '9' + 1, znaci da tom polju na kojem se trenutno nalazimo 
     * ne odgovara ni jedna vrednost, a samim tim znaci da nam prethodni korak,
     * odnosno karakter koji smo stavili u prethodnom pozivu funkcije
     * nije bio dobar.Zato se postavlja vrednost polja u prethodnom pozivu na
     * '0' ("najuvuceniji else").To se naravno izvrsava 
     * prethodno pozvanom funkcijom kada joj resi() vrati false.
     * Sada smo na prethodnoj funkciji i karakter koji nam je bio dobar
     * u prethodnom pozivu,sada nam ne odgovara.Zato vrednost polja stavljamo
     * na '0' i uvecavamo vrednost promenljive noviKarakter, da bi nasli sledeci
     * koji potencijalno odgovara.
     * 
     * 
     * @param sudoku Totalno nepotrebna stvar,ali opet zbog lakseg razumevanja.
     * @return true ukoliko je prosla funkcija reseno(), a ukoliko je 
     * noviKarakter == '9' + 1 funkcija vraca false i vraca se u prethodni poziv.
     */
    public static boolean resi(char[][] sudoku) {
        /** Index reda, koji ima slobodno polje */
        int indexX = 0;
        
        /** Index kolone, koja ima slobodno polje */
        int indexY = 0;
        
        /** Karakter koji se uvecava od '1' do '9' */
        char noviKarakter = minimalniKarakter;
        
        boolean reseno = false;
        
        if (reseno()) {
            reseno = true;
        } else {
            indexX = nadjiSlobodanIndexX();
            indexY = nadjiSlobodanIndexY();
            while (!reseno && noviKarakter <= maximalniKarakter) {
                popuniPraznoPolje(indexX, indexY, noviKarakter);
                if (provera() == true) {
                    if (resi(sudoku) == true) {
                        reseno = true;
                    } else
                        sudoku[indexX][indexY] = '0';
                } else
                    sudoku[indexX][indexY] = '0';
                noviKarakter++;
            }
            
        }
        return reseno;
    }
    
    /**
     * Nalazi prvi slobodan red
     * @return index prvog slobodnog reda.
     */
    public static int nadjiSlobodanIndexX(){
        for (int i = 0; i < duzina; i++) {
            for (int j = 0; j < duzina; j++) {
                if (sudoku[i][j] == '0')
                    return i;
            }
        }
        return -1;
    }
    
    /**
     * Nalazi prvu slobodnu kolonu.U kombinaciji sa funkcijom 
     * nadjiSlobodanRed() nalazi idex prvog slobodnog polja.
     * @return index prve slobodne kolone.
     */
    public static int nadjiSlobodanIndexY(){
        for (int i = 0; i < duzina; i++) {
            for (int j = 0; j < duzina; j++) {
                if (sudoku[i][j] == '0')
                    return j;
            }
        }
        return -1;
    }
    
    /**
     * Postavlja vrednost polja sa idexima indexI i indexY na unetu vrednost
     * noviKarakter.
     * Totalno nepotrebna funkcija, ali zbog lakseg razumevanja programa ostace
     * kakva jeste.
     * 
     * @param indexI
     * @param indexY
     * @param noviKarakter 
     */
    public static void popuniPraznoPolje 
    (int indexI, int indexY, char noviKarakter) {
          sudoku[indexI][indexY] = noviKarakter;
    }
   
    /**
     * Funkcija koja vrsi validaciju sudoku polja.
     * Da objasnimo pravila igre:
     * U bilo kom redu, ne smeju da se pojave dva ista elementa.
     * U bilo kojoj koloni, ne smeju da se pojave dva ista elementa.
     * U svakom 3x3 kvadraticu, koji je boldnovan, ne smeju da se pojave
     * dva ista elementa.
     * @return true ukoliko je sve po zakonu.U suprotnom false.
     */
    public static boolean provera() {
        return proveraRedova() && proveraKolona() && proveraKockica();
    }
    
    /**
     * Funkcija koja provera da li se u bilo kom redu nalaze dva ista elementa.
     * @return  true ako je sve po zazkonu, ako nije false.
     */
    public static boolean proveraRedova() {
        for (int i = 0; i < duzina; i++) {
            for (int j = 0; j < duzina; j++) {
                if (sudoku[i][j] == '0')
                    continue;
                for (int k = j + 1; k < duzina; k++) {
                    if (sudoku[i][j] == sudoku[i][k])
                        return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Funkcija koja provera da li se u bilo kojoj koloni nalaze dva ista elementa.
     * @return  true ako je sve po zazkonu, ako nije false.
     */
    public static boolean proveraKolona() {
        for (int j = 0; j < duzina; j++) {
            for (int i = 0; i < duzina; i++) {
                if (sudoku[i][j] == '0')
                    continue;
                for (int k = i + 1; k < duzina; k++) {
                    if (sudoku[i][j] == sudoku[k][j])
                        return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Funkcija koja provera da li se u bilo kojoj 3x3 kockici nalaze dva ista elementa.
     * @return  true ako je sve po zazkonu, ako nije false.
     */
    public static boolean proveraKockica() {
        char niz [] = new char [duzina];
        int brojacElemenataUNizu = 0;
        boolean moze = true;
        for (int x = 0 ; x < duzina; x += 3) {
            for (int y = 0; y < duzina; y += 3) {
                    brojacElemenataUNizu = 0;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            niz[brojacElemenataUNizu++] = sudoku[x + i][y + j];
                        }
                    }
                    moze = proveraJednogReda(niz);
                    if (!moze)
                        return false;
            }
        }
        
        return true;
    }
    
    /**
     * Funkcija koja proverava da li se u jednom redu nalaze dva ista elementa
     * @param red
     * @return true, ako je sve po zakonu, u suprotnom false.
     */
    public static boolean proveraJednogReda(char red[]) {
        for (int i = 0; i < duzina; i++)
            for (int j = i + 1; j < duzina; j++)
                if (red[i] == '0') {
                    continue;
                } else if (red[i] == red[j])
                    return false;
        return true;
    }
    
    /**
     * Totalno nepotrebna funkcija koja vraca broj elemenata.
     * @return broj elemenata sudoku polja za igru.
     */
    public static int nadjiBrojElemenata() {
        int brojacElemenata = 0;
        for (int i = 0; i < duzina; i++) {
            for (int j = 0; j < duzina; j++) {
                if (sudoku[i][j] != '0')
                    brojacElemenata++;
            }
        }
        return brojacElemenata;
    }
    
    /**
     * Totalno nepotrebna funkcija koja u kombinaciji sa funkcijom resi
     * moze takodje da nadje da li je sudoku igra resena.
     * @return 
     */
    public static boolean reseno() {
        return nadjiBrojElemenata() == ukupniBrojElemenata;
    }
    
    /**
     * Vraca element sa odgovarajucim indexima na '0'.
     * @param x
     * @param y 
     */
    public static void vratiElementNaNulu(int x, int y){
        sudoku[x][y] = '0';
    }
    
}
