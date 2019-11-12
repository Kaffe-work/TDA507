//
// To compile this Java program, type:
//
//	javac GlobalAlignment.java
//
// To run the program, type:
//
//	java GlobalAlignment
//


public class Levenshtein {

	public static final int MAX_LENGTH	= 100;

	public static final int MATCH_SCORE	= 0;
	public static final int MISMATCH_SCORE	= 1;
	public static final int GAP_PENALTY	= 1;

	public static final int STOP		= 0;
	public static final int UP		= 1;
	public static final int LEFT		= 2;
	public static final int DIAG		= 3;

    public static void main(String[] args) {

	int i, j;
	int alignmentLength, score, tmp;

	String X = "ATCGAT";
	String Y = "ATACGT";

	int F[][] = new int[MAX_LENGTH+1][MAX_LENGTH+1];     /* score matrix */
	int trace[][] = new int[MAX_LENGTH+1][MAX_LENGTH+1]; /* trace matrix */	
	char[] alignX = new char[MAX_LENGTH*2];	/* aligned X sequence */
	char[] alignY = new char[MAX_LENGTH*2];	/* aligned Y sequence */

	int m = X.length();
	int n = Y.length();


	//
	// Initialise matrices
	//

	F[0][0] = 0;
	trace[0][0] = STOP;
	for ( i=1 ; i<=m ; i++ ) {
		F[i][0] = F[i-1][0] + GAP_PENALTY;
		trace[i][0] = STOP;
	}
	for ( j=1 ; j<=n ; j++ ) {
		F[0][j] = F[0][j-1] + GAP_PENALTY;
		trace[0][j] = STOP;
	}


	//
	// Fill matrices
	//

	for ( i=1 ; i<=m ; i++ ) {


		for ( j=1 ; j<=n ; j++ ) {


                int lev1 = 0, lev2 = 0, lev3 = 0;
                    //checks if i or j is empty, if so the distance is equal to the longer string.
                if (Math.min(m, n) == 0){
                    score = Math.max(m, n);
                }
                else{
                        //calculate Levenshtein value 1
                if ( X.charAt(i-1)==Y.charAt(j-1) ) {
                    score = F[i-1][j-1] + MATCH_SCORE;
                } else {
                    score = F[i-1][j-1] + MISMATCH_SCORE;
                }
                //find the minimum of the 3 Levenshtein values.
                lev1 = score;
                lev2 = F[i-1][j] + GAP_PENALTY;
                lev3 = F[i][j-1] + GAP_PENALTY;

                score = Math.min(Math.min(lev1, lev2),lev3);
            }
            F[i][j] = score;
        }

	}


	//
	// Print score matrix
	//

	System.out.println("Score matrix:");
	System.out.print("      ");
	for ( j=0 ; j<n ; ++j ) {
		System.out.print("    " + Y.charAt(j));
	}
	System.out.println();
	for ( i=0 ; i<=m ; i++ ) {
		if ( i==0 ) {
			System.out.print(" ");
		} else {
			System.out.print(X.charAt(i-1));
		}
		for ( j=0 ; j<=n ; j++ ) {
			System.out.format("%5d", F[i][j]);
		}
		System.out.println();
	}
	System.out.println();


	//
	// Trace back from the lower-right corner of the matrix
	//

	i = m;
	j = n;
	alignmentLength = 0;

	while ( trace[i][j] != STOP ) {

		switch ( trace[i][j] ) {

			case DIAG:
				alignX[alignmentLength] = X.charAt(i-1);
				alignY[alignmentLength] = Y.charAt(j-1);
				i--;
				j--;
				alignmentLength++;
				break;

			case LEFT:
				alignX[alignmentLength] = '-';
				alignY[alignmentLength] = Y.charAt(j-1);
				j--;
				alignmentLength++;
				break;

			case UP:
				alignX[alignmentLength] = X.charAt(i-1);
				alignY[alignmentLength] = '-';
				i--;
				alignmentLength++;
		}
	}


	//
	// Unaligned beginning
	//

	while ( i>0 ) {
		alignX[alignmentLength] = X.charAt(i-1);
		alignY[alignmentLength] = '-';
		i--;
		alignmentLength++;
	}

	while ( j>0 ) {
		alignX[alignmentLength] = '-';
		alignY[alignmentLength] = Y.charAt(j-1);
		j--;
		alignmentLength++;
	}


	//
	// Print alignment
	//

	

		//prints the last part of matrix as the Levenshtein distance 
	System.out.print("Levenshtein distance is equal to: " + F[m][n] + " for the strings " + X + " and " + Y + " ");
	
}}
