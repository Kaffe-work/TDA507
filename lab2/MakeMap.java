
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
 
public class MakeMap {
 
    List<String> pdbfile;

    public static void main(String[] args) {
        MakeMap map = new MakeMap("./1CDH.pdb");
        map.printPairsToFile();
    }
 
    public MakeMap(String filePath) {
        this.pdbfile = this.readFromPath(filePath);
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
     * @param pdbFile the pdbfile to be filtered
     * @return a list of strings containing the atom information for CA-atoms
     */
    private List<String> filterPDB(List<String> pdbFile) {
        List<String> filteredPDB = new ArrayList<>();
        for (String str : pdbFile) {
            if (str.startsWith("ATOM") && str.contains("CA")) {
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
    private List<String> getAtomCoordinates(List<String> filteredPDB) {
        List<String> atomCoordinates = new ArrayList<>();
        for (String str : filteredPDB) {
            String[] strArray = str.split(" ");
            atomCoordinates.add(strArray[6]);
            atomCoordinates.add(strArray[7]);
            atomCoordinates.add(strArray[8]);
        }
        return atomCoordinates;
    }
 
    /**
     * Checks if atom2 is within the threshold distance (7 Å) of atom1
     *
     * @param atom1 an array containing xyz coordinates of an atom
     * @param atom2 the atom coordinates to check distance with
     * @return true if within threshold distance, else returns false
     */
    private boolean withinThreshold(int[] atom1, int[] atom2) {
        int atom1x = atom1[0];
        int atom1y = atom1[1];
        int atom1z = atom1[2];
 
        int atom2x = atom2[0];
        int atom2y = atom2[1];
        int atom2z = atom2[2];
 
        double distance = Math.sqrt(Math.pow(atom2x-atom1x, 2) + Math.pow(atom2y-atom1y, 2) +
                          Math.pow(atom2z - atom1z, 2));
        return (distance <= 7);
    }
 
    /**
     * Reads the coordinates of all CA-atoms and pairs those that are within threshold distance.
     * This is done by using helper methods.
     *
     * @return a formatted string that contains all CA-pairs within threshold distance
     */
    public String getCAPairs() {
        List<String> atomCoord = getAtomCoordinates(filterPDB(this.pdbfile));
        String pairs = "";
        for (int i = 0; i < atomCoord.size() - 3; i = i + 3) {
            int[] atom1 = {(int)Double.parseDouble(atomCoord.get(i)), (int)Double.parseDouble(atomCoord.get(i+1)),
                           (int)Double.parseDouble(atomCoord.get(i+2))};
            for (int j = i; j < atomCoord.size() - 3; j = j + 3) {
                int[] atom2 = {(int)Double.parseDouble(atomCoord.get(j)), (int)Double.parseDouble(atomCoord.get(j+1)),
                               (int)Double.parseDouble(atomCoord.
                               get(j+2))};
                if (withinThreshold(atom1, atom2)) {
                    pairs =  pairs + i + " " + j + "\n" +
                                j  + " " + i + "\n"; 

                }
            }

        }
        return pairs;
    }
 
    public void printPairsToFile() {
        String CAPairs = getCAPairs();
        Path file = Paths.get("./1CDH.pairs");
        try {
            Files.writeString(file, CAPairs, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
 
 
}