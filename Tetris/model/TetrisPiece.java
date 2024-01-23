package model;

import java.io.Serializable;
import java.sql.Array;
import java.util.*;

/** An immutable representation of a tetris piece in a particular rotation.
 *  Each piece is defined by the blocks that make up its body.
 *
 * Based on the Tetris assignment in the Nifty Assignments Database, authored by Nick Parlante
 */
public class TetrisPiece implements Serializable {

    /*
     Implementation notes:
     -The starter code does a few simple things
     -It stores the piece body as a TetrisPoint[] array
     -The attributes in the TetrisPoint class are x and y coordinates
     -Don't assume there are 4 points in the piece body; there might be less or more!
    */
    private TetrisPoint[] body; // y and x values that make up the body of the piece.
    private int[] lowestYVals; //The lowestYVals array contains the lowest y value for each x in the body.
    private int width;
    private int height;
    private TetrisPiece next; // We'll use this to link each piece to its "next" rotation.
    static private TetrisPiece[] pieces;	// array of rotations for this piece


    // String constants for the standard 7 tetris pieces
    public static final String STICK_STR	= "0 0	0 1	 0 2  0 3";
    public static final String L1_STR		= "0 0	0 1	 0 2  1 0";
    public static final String L2_STR		= "0 0	1 0 1 1	 1 2";
    public static final String S1_STR		= "0 0	1 0	 1 1  2 1";
    public static final String S2_STR		= "0 1	1 1  1 0  2 0";
    public static final String SQUARE_STR	= "0 0  0 1  1 0  1 1";
    public static final String PYRAMID_STR	= "0 0  1 0  1 1  2 0";

    /**
     * Defines a new piece given a TetrisPoint[] array that makes up its body.
     * Makes a copy of the input array and the TetrisPoint inside it.
     * Note that this constructor should define the piece's "lowestYVals". For each x value
     * in the body of a piece, the lowestYVals will contain the lowest y value for that x in the body.
     * This will become useful when computing where the piece will land on the board!!
     */
    public TetrisPiece(TetrisPoint[] points) {
        List<Integer> yValues = new ArrayList<>();
        List<Integer> xValues = new ArrayList<>();

        if (points.length == 0){
            return;
        }
        this.body = points;
        Map<Integer, Integer> lowestYTable = new TreeMap<>();
        int yCounter = 0;
        for (TetrisPoint point : points){
            if(point == null){
                break;
            }
            yValues.add(point.y);
            xValues.add(point.x);
            if (!lowestYTable.containsKey(point.x)){
                lowestYTable.put(point.x, point.y);

            }

            else if (lowestYTable.containsKey(point.x) && lowestYTable.get(point.x) >= point.y){

                lowestYTable.put(point.x, point.y);
            }


        }
        this.width = Collections.max(xValues) - (Collections.min(xValues)) + 1;
        this.height = Collections.max(yValues) - (Collections.min(yValues)) + 1;

        int[] lowestYs = new int[width];
        int i = 0;
        for(int element : lowestYTable.values()){
            lowestYs[i] = element;
            i++;
        }
        this.lowestYVals = lowestYs;

    }

    /**
     * Alternate constructor for a piece, takes a String with the x,y body points
     * all separated by spaces, such as "0 0  1 0  2 0  1 1".
     */
    public TetrisPiece(String points) {
        this(parsePoints(points));
    }

    /**
     * Returns the width of the piece measured in blocks.
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the piece measured in blocks.
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a pointer to the piece's body. The caller
     * should not modify this array.
     *
     * @return point array that defines piece body
     */
    public TetrisPoint[] getBody() {
        return body;
    }

    /**
     * Returns a pointer to the piece's lowest Y values. For each x value
     * across the piece, this gives the lowest y value in the body.
     * This is useful for computing where the piece will land (if dropped).
     * The caller should not modify the values that are returned
     *
     * @return array of integers that define the lowest Y value for every X value of the piece.
     */
    public int[] getLowestYVals() {
        return lowestYVals;
    }

    /**
     * Returns true if two pieces are the same --
     * their bodies contain the same points.
     * Interestingly, this is not the same as having exactly the
     * same body arrays, since the points may not be
     * in the same order in the bodies. Used internally to detect
     * if two rotations are effectively the same.
     *
     * @param obj the object to compare to this
     *
     * @return true if objects are the same
     */
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        assert obj instanceof TetrisPiece;
        TetrisPiece other = (TetrisPiece) obj;
        if (other.getBody() == this.getBody()){ //if they have the same contents in the same order, the easy case is true
            return true;
        }
        if(other.getWidth() != this.getWidth()){ //check width matches
            return false;
        }
        if(other.getHeight() != this.getHeight()){ //check height matches
            return false;
        }
        for (TetrisPoint otherPoint : other.getBody()){ //check all points are found within the other
            boolean pointFound = false;
            for(TetrisPoint thisPoint : this.getBody()){
                if(otherPoint.equals(thisPoint)){
                    pointFound = true;
                }
            }
            if(!pointFound){ //if point isnt found, piece's cant be equal
                return false;
            }
        }
        return true; //its the same size and has the same points. Its equal!
    }

    /**
     * This is a static method that will return all rotations of
     * each of the 7 standard tetris pieces:
     * STICK, L1, L2, S1, S2, SQUARE, PYRAMID.
     * The next (counterclockwise) rotation can be obtained
     * from each piece with the fastRotation() method.
     * This method will be called by the model to facilitate
     * selection of random pieces to add to the board.
     * The pieces can be easily rotated because the rotations
     * have been precomputed.
     *
     * @return a list of all the rotations for all the given pieces.
     */
    public static TetrisPiece[] getPieces() {
        // lazy evaluation -- create static array only if needed
        if (TetrisPiece.pieces==null) {
            // use makeFastRotations() to compute all the rotations for each piece
            try {
                TetrisPiece.pieces = new TetrisPiece[]{
                        makeFastRotations(new TetrisPiece(STICK_STR)),
                        makeFastRotations(new TetrisPiece(L1_STR)),
                        makeFastRotations(new TetrisPiece(L2_STR)),
                        makeFastRotations(new TetrisPiece(S1_STR)),
                        makeFastRotations(new TetrisPiece(S2_STR)),
                        makeFastRotations(new TetrisPiece(SQUARE_STR)),
                        makeFastRotations(new TetrisPiece(PYRAMID_STR)),
                };
            } catch (UnsupportedOperationException e) {
                System.out.println("You must implement makeFastRotations!");
                System.exit(1);
            }
        }
        return TetrisPiece.pieces;
    }

    /**
     * Returns a pre-computed piece that is 90 degrees counter-clockwise
     * rotated from the receiver. Fast because the piece is pre-computed.
     * This only works on pieces set up by makeFastRotations(), and otherwise
     * just returns null.
     *
     * @return the next rotation of the given piece
     */
    public TetrisPiece fastRotation() {
        return next;
    }

    /**
     * Given the "first" root rotation of a piece, computes all
     * the other rotations and links them all together
     * in a circular list. The list should loop back to the root as soon
     * as possible.
     * Return the root piece. makeFastRotations() will need to relies on the
     * pointer structure setup here. The suggested implementation strategy
     * is to use the subroutine computeNextRotation() to generate 90 degree rotations.
     * Use this to generate a sequence of rotations and link them together.
     * Continue generating rotations until you generate the piece you started with.
     * Use Piece.equals() to detect when the rotations
     * have gotten you back to the first piece.
     *
     * @param root the default rotation for a piece
     *
     * @return a piece that is a linked list containing all rotations for the piece
     */
    public static TetrisPiece makeFastRotations(TetrisPiece root) {
        TetrisPiece currRot = root.computeNextRotation();
        root.next = currRot;
        while (!currRot.equals(root)){
            TetrisPiece newRot = currRot;
            newRot.next = currRot.computeNextRotation();
            currRot = newRot.next;
        }
        currRot.next = root.next;
        return root;
    }

    /**
     * Moves pieces down if there is space to do so
     * @param piece
     * @return TetrisPiece
     */
    public static TetrisPiece Shunt(TetrisPiece piece){
        ArrayList<Integer> emptyRows = new ArrayList<Integer>(Arrays.asList(0, 1,2));
        ArrayList<Integer> emptyCols = new ArrayList<Integer>(Arrays.asList(0, 1,2));
        for(TetrisPoint point : piece.getBody()){//remove row/column as empty is piece uses it for some point
            if(point == null){
                break;
            }
            if (emptyRows.contains(point.y)){
                emptyRows.remove(Integer.valueOf(point.y));
            }
            if (emptyCols.contains(point.x)){
                emptyCols.remove(Integer.valueOf(point.x));
            }
        }
        if(emptyRows.contains(2) && emptyRows.contains(0) && emptyRows.contains(1)){ //has 3 empty rows it can move down to
            for (TetrisPoint point : piece.getBody()){
                if(point == null){
                    break;
                }
                point.y -= 3;
            }
        }
        else if(emptyRows.contains(1) && emptyRows.contains(0)){ //has 2 empty rows it can move down to
            for (TetrisPoint point : piece.getBody()){
                if(point == null){
                    break;
                }
                point.y -= 2;
            }
        }
        else if(emptyRows.contains(0)){ //can only be moved down a single row
            for (TetrisPoint point : piece.getBody()){
                if(point == null){
                    break;
                }
                point.y -= 1;
            }
        }

        if(emptyCols.contains(2) && emptyCols.contains(0) && emptyCols.contains(1)){ //move left 3 columns,
            for (TetrisPoint point : piece.getBody()){
                if(point == null){
                    break;
                }
                point.x -= 3;
            }
        }

        else if(emptyCols.contains(1) && emptyCols.contains(0)){ //move left 2 columns,
            for (TetrisPoint point : piece.getBody()){
                if(point == null){
                    break;
                }
                point.x -= 2;
            }
        }
        else if(emptyCols.contains(0)){ //move left single column
            for (TetrisPoint point : piece.getBody()){
                if(point == null){
                    break;
                }
                point.x -= 1;
            }
        }
        return new TetrisPiece(piece.body); //return new "shunted" piece!
    }

    /**
     * Returns a new piece that is 90 degrees counter-clockwise
     * rotated from the receiver.
     *
     * @return the next rotation of the given piece
     */
    public TetrisPiece computeNextRotation() {
        TetrisPoint[] newPoints = new TetrisPoint[this.getBody().length];
        int i = 0;
        for (TetrisPoint point : this.getBody()){ //mechanically "swaps" points
            if(point == null){
                break;
            }
            if(point.x == 0){
                TetrisPoint newPoint = new TetrisPoint(point.y,3);
                newPoints[i] = newPoint;
                i += 1;
            }
            if(point.x == 1){
                TetrisPoint newPoint = new TetrisPoint(point.y, 2);
                newPoints[i] = newPoint;
                i += 1;
            }
            if(point.x == 2){
                TetrisPoint newPoint = new TetrisPoint(point.y, 1);
                newPoints[i] = newPoint;
                i += 1;
            }
            if(point.x == 3){
                TetrisPoint newPoint = new TetrisPoint(point.y, 0);
                newPoints[i] = newPoint;
                i += 1;
            }
        }
    return Shunt(new TetrisPiece(newPoints)); //moves piece down as far as possible to bottom left
    }
    /**
     * Print the points within the piece
     *
     * @return a string representation of the piece
     */
    public String toString() {
        String str = "";
        for (int i = 0; i < body.length; i++) {
            str += body[i].toString();
        }
        return str;
    }

    /**
     * Given a string of x,y pairs (e.g. "0 0 0 1 0 2 1 0"), parses
     * the points into a TPoint[] array.
     *
     * @param string input of x,y pairs
     *
     * @return an array of parsed points
     */
    private static TetrisPoint[] parsePoints(String string) {
        List<TetrisPoint> points = new ArrayList<TetrisPoint>();
        StringTokenizer tok = new StringTokenizer(string);
        try {
            while(tok.hasMoreTokens()) {
                int x = Integer.parseInt(tok.nextToken());
                int y = Integer.parseInt(tok.nextToken());

                points.add(new TetrisPoint(x, y));
            }
        }
        catch (NumberFormatException e) {
            throw new RuntimeException("Could not parse x,y string:" + string);
        }
        // Make an array out of the collection
        TetrisPoint[] array = points.toArray(new TetrisPoint[0]);
        return array;
    }
}
