
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DOMAK {
// name: Leo Carlsson
    class Atom {
        public double x, y, z;

        public Atom(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    List<String> pdbfile;
    String fileToWrite;

    public DOMAK(String fileToRead, String fileToWrite) {
        this.pdbfile = this.readFromPath(fileToRead);
        this.fileToWrite = fileToWrite;
    }

    public static void main(String[] args) {
        DOMAK domak = new DOMAK(
                "./1CDH.pdb",
                "./1CDH.domak");
        Atom[] atoms = domak.getAtomCoordinates(domak.getFilteredPDB());
        List<Integer> score = domak.getScoreForDomains(atoms);
        String scoreStr = domak.convertScoresToPairs(score);
        domak.printScoreToFile(scoreStr);
    }    
    private List<Integer> getScoreForDomains(Atom[] atoms) {
        int initLen = 10;
        Atom[]segmentA = Arrays.copyOfRange(atoms, 0, initLen);
        Atom[]segmentB = Arrays.copyOfRange(atoms, initLen, atoms.length);
        int bestScore = 0;
        int bestLen = -1;
        int minLen = initLen;
        List<Integer> scoreList = new ArrayList<>();
        while(segmentB.length >  initLen){
            int intA, intB, ext, score;
            intA = countIntCollisions(segmentA);
            intB = countIntCollisions(segmentB);
            ext = countExtCollisions(segmentA, segmentB);
            score = (intA * intB) / ext;
            if (score > bestScore){
                bestScore = score;
                bestLen = minLen;
            }
            scoreList.add(score);
            minLen++;
            segmentA = Arrays.copyOfRange(atoms, 0, minLen);
            segmentB = Arrays.copyOfRange(atoms, minLen, atoms.length);
            

        }


        System.out.print(bestLen);

        
        return scoreList;
    }   
     private String convertScoresToPairs(List<Integer> scores) {
        StringBuilder str = new StringBuilder();        
        for (int i = 0; i < scores.size(); i++) {
            String line = i + ", " + scores.get(i) + "\n";
            str.append(line);
        }
        return str.toString();
    }

    private List<String> readFromPath(String filePath) {
        List<String> content = new ArrayList<>();
        try {
            content = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    /**
     * Filters away all non CA-atom strings from the pdbfile non-destructively
     *
     * @return a list of strings containing the atom information for CA-atoms
     */
    private List<String> getFilteredPDB() {
        List<String> filteredPDB = new ArrayList<>();
        for (String str : this.pdbfile) {
            if (str.startsWith("ATOM") && str.contains(" CA ")) {
                String editedString = str.replaceAll("\\s+"," ");
                filteredPDB.add(editedString);
            }
        }
        return filteredPDB;
    }

    /**
     * Gets the xyz atom coordinates for all the atoms in the input
     *
     * @param filteredPDB the list of strings from @filterPDB()
     * @return a list of coordinates in strings
     */
    private Atom[] getAtomCoordinates(List<String> filteredPDB) {
        Atom[] atomCoordinates = new Atom[filteredPDB.size()];
        int index = 0;
        for (String str : filteredPDB) {
            String[] strArray = str.split(" ");
            Atom atom = new Atom(Double.parseDouble(strArray[6]), Double.parseDouble(strArray[7]), Double.parseDouble(strArray[8]));
            atomCoordinates[index++] = atom;
        }
        return atomCoordinates;
    }

    private int countCollisions(Atom atom1, Atom[] segment){
        int numberOfCol = 0;
        for(Atom atom2 : segment){
            if (withinThreshold(atom1, atom2)){
                numberOfCol++;
            }
        }
        return numberOfCol;
    }
    private int countExtCollisions(Atom[] atoms1, Atom[] atoms2){
        int numberOfCol = 0;
        for(Atom atom1 : atoms1){
            numberOfCol += countCollisions(atom1, atoms2);
        }
        for(Atom atom2 : atoms2){
            numberOfCol += countCollisions(atom2, atoms1);
        }
        return numberOfCol;
    }
    private int countIntCollisions(Atom[]atoms){
        int numberOfCol = 0;
        int[] array = new int[atoms.length];
        Arrays.setAll(array, i -> i == 0 ? 0 : i++);
        for (int i : array){
            numberOfCol += countCollisions(atoms[i], Arrays.copyOfRange(atoms, 0, i+1));
        }
        return numberOfCol;
    }

    private boolean withinThreshold(Atom a1, Atom a2) {
        double distance = Math.sqrt(Math.pow(a2.x-a1.x, 2) + Math.pow(a2.y-a1.y, 2) +
                Math.pow(a2.z - a1.z, 2));
        return (distance < 8; //this gives better values
    }



    private void printScoreToFile(String score) {

        Path file = Paths.get(this.fileToWrite);
        try {
            Files.writeString(file, score, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}