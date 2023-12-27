//Emil Davlityarov
//e.davlityarov@innopolis.university
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Collection;

public class Main {
    public static FileReader file;

    static {
        try {
            file = new FileReader("input.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        String s;
        Scanner scanner = new Scanner(file);
        ArrayList<String> states;
        ArrayList<String> alpha;
        String initst;
        ArrayList<String> fin;
        ArrayList<String> trans;
        s = scanner.nextLine();
        int index1 = s.indexOf("[");
        int index2 = s.indexOf("]");
        String sss = s.split("\\[")[0];
        if (!sss.equals("states=")){ // check that input consists string "states="
            System.out.println("E1: Input file is malformed");
            file.close();
            System.exit(0);
        }
        s = s.substring(index1 + 1, index2); // split 1st string
        String[] sSplit = s.split(","); // split 1st string
        if (sSplit[0].equals("")){ // check if state in empty
            System.out.println("E1: Input file is malformed");
            file.close();
            System.exit(0);
        }
        states = new ArrayList<>(Arrays.asList(sSplit)); // states is array list that contains all stages in FSA
        for (String state : states) {
            if (!checkState(state)) { // check states on error 1
                System.out.println("E1: Input file is malformed");
                file.close();
                System.exit(0);
            }
        }
        s = scanner.nextLine();
        sss = s.split("\\[")[0];
        if (!sss.equals("alpha=")){ // check that input consists string "alpha="
            System.out.println("E1: Input file is malformed");
            file.close();
            System.exit(0);
        }
        index1 = s.indexOf("[");
        index2 = s.indexOf("]");
        s = s.substring(index1 + 1, index2); // split 2nd string
        String[] sSplit1 = s.split(","); // split 2nd string
        alpha = new ArrayList<>(Arrays.asList(sSplit1)); // alpha is array list that consists all alphabets in FSA
        for (String value : alpha) { // check alphabet on error 1
            if (!checkAlpha(value)) {
                System.out.println("E1: Input file is malformed");
                file.close();
                System.exit(0);
            }
        }
        s = scanner.nextLine();
        sss = s.split("\\[")[0];
        if (!sss.equals("initial=")){
            System.out.println("E1: Input file is malformed");
            file.close();
            System.exit(0);
        }
        index1 = s.indexOf("[");
        index2 = s.indexOf("]");
        s = s.substring(index1 + 1, index2); // split 3rd string
        for (int i = 0; i < s.length(); i++) { // check initial state on error 5
            if (s.charAt(i) == ',') { // if initial state has more 2 elements (comma)
                System.out.println("E1: Input file is malformed");
                file.close();
                System.exit(0);
            }
        }
        if (s.equals("")) { // check on error 5 if initial state is empty
            System.out.println("E2: Initial state is not defined");
            file.close();
            System.exit(0);
        }
        initst = s;
        boolean flagInist = false; // flagInit needs to check exist initial state in states
        for (String state : states) {
            if (initst.equals(state)) { // if initial state equals to some state, it's good, otherwise if
                // we don't find some state which equals to initial state, so we get error 4
                flagInist = true;
                break;
            }
        }
        if (!flagInist) { // print error 5 if flagInit is false
            System.out.println("E4: A state '" + initst + "' is not in the set of states");
            file.close();
            System.exit(0);
        }
        s = scanner.nextLine();
        sss = s.split("\\[")[0];
        if (!sss.equals("accepting=")){ // check that input consists string "accepting="
            System.out.println("E1: Input file is malformed");
            file.close();
            System.exit(0);
        }
        index1 = s.indexOf("[");
        index2 = s.indexOf("]");
        s = s.substring(index1 + 1, index2); // split 4th string
        String[] sSplit2 = s.split(","); // split 4th string
        fin = new ArrayList<>(Arrays.asList(sSplit2)); // fin is Array list that consists all finite states
        if (fin.size() == 1 && fin.get(0).equals("")) { // if we did not get finite states(empty), so print error 3
            System.out.println("E3: Set of accepting states is empty");
            file.close();
            System.exit(0);
        }
        s = scanner.nextLine();
        sss = s.split("\\[")[0];
        if (!sss.equals("trans=")){ // check that input consists string "trans="
            System.out.println("E1: Input file is malformed");
            file.close();
            System.exit(0);
        }
        index1 = s.indexOf("[");
        index2 = s.indexOf("]");

        s = s.substring(index1 + 1, index2); // split 5th string
        String[] sSplit3 = s.split(","); // split 5th string
        trans = new ArrayList<>(Arrays.asList(sSplit3)); // trans is array list that consists all transitions on FSA
        checkTrans(trans, states, alpha); // checkTrans is function that check all transition on error 4 or error 5
        int m = trans.size(); // count of transitions
        int n = states.size(); // count of states
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>(n); // initialize graph as adjacency list
        for (int i = 0; i < n; i++) {  // initialize graph as adjacency list
            graph.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            String[] ss = trans.get(i).split(">"); // split trans array list into ">"
            int first = states.indexOf(ss[0]); // take index of 1st state from state array list
            int second = states.indexOf(ss[2]);// take index of 2nd state from state array list
            graph.get(first).add(second); // connect 1st and transition states
            graph.get(second).add(first); // connect transition and 1st states
        }
        int[] busy = new int[n]; // busy is array for visiting states
        for (int i = 0; i < n; i++) {
            busy[i] = 0;
        }
        dfs(busy, 0, graph); // launch dfs for check error 6
        for (int i = 0; i < n; i++) {
            if (busy[i] == 0) { // if we didn't visit some state therefore we get error 6
                System.out.println("E6: Some states are disjoint");
                file.close();
                System.exit(0);
            }
        }

        ArrayList<ArrayList<Integer>> graph2 = new ArrayList<>(n); // initialize graph as adjacency list
        for (int i = 0; i < n; i++) {  // initialize graph as adjacency list
            graph2.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            String[] ss = trans.get(i).split(">"); // split trans array list into ">"
            int first = states.indexOf(ss[0]); // take index of 1st state from state array list
            int second = alpha.indexOf(ss[1]);// take index of alphabet from alphabet array list
            graph2.get(first).add(second); // connect 1st state and alphabet
        }

        boolean nondeterministic = false; // boolean nondeterministic needs to check deterministic or not fsa
        for (int i = 0; i < graph2.size(); i++) {
            Map<Integer, Integer> map = new HashMap<>(); // initialize map to count outgoing alphabets
            for (int j = 0; j < graph2.get(i).size(); j++) {
                if (map.get(graph2.get(i).get(j)) == null) { // if our value on key is null, therefore we put 1
                    map.put(graph2.get(i).get(j), 1);
                } else {
                    nondeterministic = true; // if we get value > 1, it means that the same alphabet outgoing
                    // more than once hence our fsa is nondeterministic
                    break;
                }
            }
        }
        if (nondeterministic) { // if nondeterministic equals to true, so print error 7
            System.out.println("E7: FSA is nondeterministic");
            file.close();
            System.exit(0);
        }
        file.close();
        n = states.size();
        Map<String, Integer> state = new HashMap<>(); // map needs for save for each states value of index
        for (int i = 0; i < n; i++) {
            state.put(states.get(i), i);
        }
        String[][] R = new String[n][n]; // this matrix saves regular expression, where under index i and j we save Rij
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                R[i][j] = "{}"; // put empty in start
            }
        }
        for (int i = 0; i < trans.size(); i++) {
            String[] transition = trans.get(i).split(">"); //split elements from trans array list
            int ii = state.get(transition[0]); // here we get index from map correspond state
            int jj = state.get(transition[2]); // here we get index from map correspond state
            if (R[ii][jj].equals("{}")){ // if Rij is empty, then we just put transition between ii and jj
                R[ii][jj] = transition[1];
            } else {
                R[ii][jj] += "|" + transition[1]; // if Rif is not empty, then we derive via |
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) { // all at below is just lexicographical sort
                String[] ss = R[i][j].split("\\|");
                List<String> arrayList = Arrays.asList(ss);
                arrayList.sort(null);
                R[i][j] = arrayList.get(0);
                for (int k = 1; k < arrayList.size(); k++) {
                    R[i][j] += "|" + arrayList.get(k);
                }
            }
        }
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++) {
                if (i == j){ // if i == j, then we push "eps"
                    if (R[i][j].equals("{}"))
                        R[i][j] = "eps";
                    else
                        R[i][j] += "|eps";
                }
            }
        }
        String[][] R_new = new String[n][n]; // initialize new R for calculate expression on K step
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    R_new[i][j] = "";
                    R_new[i][j] = "(" + R[i][k] + ")(" + R[k][k] + ")*(" + R[k][j] + ")|(" + R[i][j] + ")"; // use formula
                }
            }
            for (int i = 0; i < n; i++) // here we just update old R matrix for calculate new R
                for (int j = 0; j < n; j++)
                    R[i][j] = R_new[i][j];
        }
        //Sort accepting states by lexicographically
        fin.sort(String::compareTo);
        for (int i = 0; i < fin.size(); i++){ // print finite regular expression
            System.out.print("(" + R_new[state.get(initst)][state.get(fin.get(i))] + ")");
            if (i != fin.size() - 1)
                System.out.print('|'); // derive all regular expressions on |
        }
    }

    /**
     * checkTrans is method which check correct input otherwise we print error1 or error3
     * @param trans
     * @param states
     * @param alpha
     * @throws IOException
     */
    static void checkTrans(ArrayList<String> trans, ArrayList<String> states, ArrayList<String> alpha) throws IOException {
        for (int i = 0; i < trans.size(); i++){
            boolean flag1 = false;// flag1 for check correct 1st state
            boolean flag2 = false;// flag2 for check correct alphabet
            boolean flag3 = false;// flag3 for check correct 2nd state
            String[] ss = trans.get(i).split(">"); //split elements from trans array list
            for (int j = 0; j < states.size(); j++) {
                if (ss[0].equals(states.get(j))) { //if 1st state does not equal some states from states array list
                    flag1 = true;
                }
            }
            for (int j = 0; j < states.size(); j++){
                if (ss[2].equals(states.get(j))){ //if alphabet does not equal some alphabet from alpha array list
                    flag3 = true;
                }
            }
            for (int k = 0; k < alpha.size(); k++){
                if (ss[1].equals(alpha.get(k))){ //if 2nd state does not equal some states from states array list
                    flag2 = true;
                    break;
                }
            }
            if (!flag1){ // if flag1 equal to false, so print error 4
                System.out.println("E4: A state '" + ss[0] + "' is not in the set of states" );
                file.close();
                System.exit(0);
            }
            if (!flag3){ // if flag3 equal to false, so print error 4
                System.out.println("E4: A state '" + ss[2] + "' is not in the set of states" );
                file.close();
                System.exit(0);
            }
            if (!flag2){ // if flag2 equal to false, so print error 5
                System.out.println("E5: A transition '" + ss[1] + "' is not represented in the alphabet");
                file.close();
                System.exit(0);
            }
        }
    }

    /**
     * check states, that they contain latin letters, words and numbers
     * @param s
     * @return true or false
     */
    static boolean checkState(String s){
        for (int i = 0; i < s.length(); i++){
            if (!((s.charAt(i) >= 65 && s.charAt(i) <= 90) || (s.charAt(i) >= 48 && s.charAt(i) <= 57)
                    || (s.charAt(i) >= 97 && s.charAt(i) <= 122))) {
                return false;
            }
        }
        return true;
    }

    /**
     * check alphabets, that they contain latin letters, words and numbers
     * @param s
     * @return true or false
     */
    static boolean checkAlpha(String s){
        // and character '_’(underscore)
        for (int i = 0; i < s.length(); i++){
            if ((s.charAt(i) >= 65 && s.charAt(i) <= 90) || (s.charAt(i) >= 48 && s.charAt(i) <= 57)
                    || (s.charAt(i) >= 97 && s.charAt(i) <= 122) || (s.charAt(i) == 95) ) {

            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * dfs needs to traversal graph
     * @param busy
     * @param vert
     * @param graph
     */
    static void dfs(int[] busy, int vert, ArrayList<ArrayList<Integer>> graph){
        busy[vert] = 1; // set to 1 if vertex v has been visited
        int size_graph = graph.get(vert).size(); // number of vertices adjacent to v
        for (int i = 0; i < size_graph; i++){ // go through the vertices adjacent to v
            int next = graph.get(vert).get(i); // // next - vertex adjacent to v
            if (busy[next] == 0 ){ // // check whether we visited this vertex or not
                dfs(busy, next, graph);
            }
        }
    }

}
