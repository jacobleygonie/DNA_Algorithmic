
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class subsequencev2 {
//////////// TASK 1 : naive algorithm to compute one longest common subsequence //////////// 
public static int naive(String a, String b){
int n = a.length();
int m=b.length();
if (n==0 || m==0){ // initialization for an empty word (a or b, or twice)
return 0;
}
else { // when the 2 words are not empty, we use the recursive relation 
// without memoisation, the algorithm is very naive
if (a.charAt(0)==b.charAt(0)) {
return 1+naive(a.substring(1),b.substring(1)); 
}
else {
return Math.max(naive(a.substring(1),b), naive(a,b.substring(1))) ;
}
}
}

String s3="CACTAAAAA";
String s4="CACTAAAAT";
System.out.println(naive(s3, s4));
////////////TASK 2 : algorithm based on dynamic programming to compute one longest common subsequence //////////// 
public static void dynamicAux(String a, String b, int i, int j, int[][]t){ //auxiliary function that fill in t(i,j) 
// t(i,j) = longest common subsequence between a(0...i-1) and b(0...j-1)
if (i==0||j==0) { //initialization for an empty word (a or b, or twice)
t[i][j]=0;
}
else { //same recursive relation than in the naive algorithm but we memorize intermediate results in t -> we calculate them only one time 
if (a.charAt(i-1)==b.charAt(j-1)) {
t[i][j]=t[i-1][j-1]+1;
}
else{
t[i][j]=Math.max(t[i-1][j], t[i][j-1]);
}
}
}

public static int dynamic(String a, String b){ 
//output : one longest subsequence using the auxiliary function dynamicAux
int n = a.length();
int m=b.length();
int[][]t = new int[n+1][m+1];
for (int i=0;i<n+1;i++) { //we fill in t, by increasing raw and column (because of the initialization in dynamicAux, it is feasible)
for (int j=0;j<m+1;j++) {
dynamicAux(a,b,i,j,t);
}
}
return t[n][m]; // t[n][m] is the longest subsequence between a(0...n-1)=a and b(0...m-1)=b
}

//////////// TASK3 : Algorithm based on dynamic programming wich computes and displays an optimal alignment ////////////

public static void alignementAux(String a, String b, int i, int j, int[][]t, String[][] res1, String [][] res2){
// auxiliary function that fill in t(i,j), res1(i,j) and res2(i,j)
// t(i,j) = longest common subsequence between a(0...i-1) and b(0...j-1)
// res1(i,j) = modified a = 1st line of the alignment between a(0...i-1) and b(0...j-1)
// res2(i,j) = modified b = 2nd line of the alignment between a(0...i-1) and b(0...j-1)
if (i==0 && j==0) { // case 1 
t[i][j]=0;
res1[i][j]="";
res2[i][j]="";
}
else if (i==0) { // a is empty -> we need j insertions on a (j hyphens), b is not changed
t[i][j]=0;
for (int k=0;k<j;k++) {
res1[i][j] = res1[i][j] + "-";
}
res2[i][j] = b.substring(0,j);
}
else if (j==0) {// b is empty -> we need i insertions on b (i hyphens), a is not changed
for (int k=0;k<i;k++) {
res2[i][j] = res2[i][j] + "-";
}
res1[i][j] = a.substring(0,i);
}
else { // case 2
if (a.charAt(i-1)==b.charAt(j-1)) { //case2.A.
t[i][j]=t[i-1][j-1]+1;
res1[i][j] = res1[i-1][j-1]+a.charAt(i-1);
res2[i][j] = res2[i-1][j-1]+b.charAt(j-1);
}
else{//case2.B.
t[i][j]=Math.max(Math.max(t[i-1][j], t[i][j-1]), t[i-1][j-1]);

if (t[i][j]==t[i][j-1]) { //case2.B.1 -> insertion in a of b(j-1) ie hyphen on a modified / +b(j-1) on b modified
res1[i][j]= res1[i][j-1]+"-";
res2[i][j] = res2[i][j-1]+b.charAt(j-1);
}
else if (t[i][j]==t[i-1][j]){//case2.B.2 -> deletion in a ie hyphen on b modified / + a(i-1) on a modified 
res1[i][j] = res1[i-1][j]+a.charAt(i-1);
res2[i][j] = res2[i-1][j]+"-";
}
else {//case2.B.3 -> substution -> + a(i-1) on a modified and +b(j-1) on b modified
res1[i][j]= res1[i-1][j-1]+a.charAt(i-1);
res2[i][j] = res2[i-1][j-1]+b.charAt(j-1);
} 
}
}
}

public static String[] alignement(String a, String b) {
//main function that return the alignment (1st line : a modified, 2nd line : b modified)
int n = a.length();
int m=b.length();
String[][] res1 = new String[n+1][m+1];
String[][] res2 = new String[n+1][m+1];
int[][]t = new int[n+1][m+1];
for (int i=0;i<n+1;i++) {
res1[i][0]="";
res2[i][0]="";
}
for (int j=0;j<m+1;j++){
res1[0][j]="";
res2[0][j]="";
}
for (int i=0;i<n+1;i++) { ////fill in t,res1, res2 by increasing raw and column (because of the initialization in dynamicAux, it is feasible)
for (int j=0;j<m+1;j++) {
alignementAux(a,b,i,j,t, res1, res2);
}
}
String[] fin = new String[2];
fin[0]=res1[n][m]; //res1(n,m) is a modified for alignment between a(0...n-1) and b(0...m-1)
fin[1]=res2[n][m];//res2(n,m) is b modified for alignment between a(0...n-1) and b(0...m-1)
return fin; //fin is the alignment (2 lines of String)
}

////////////TASK 4 : Algorithm based the previous computes one local alignment using the Blosum50 matrix ////////////
public static void ScoreAux(String a, String b, int i, int j, int[][]t, String[][] res1, String [][] res2){
// auxiliary function that fill in t(i,j), res1(i,j) and res2(i,j)
// each pair of character that we consider is compared with another one thanks to Blowsum50 matrix and its getScore function 
if (i==0 && j==0) {
t[i][j]=0;
res1[i][j]="";
res2[i][j]="";
}
else if (i==0) { // a is empty -> we need j insertions on a (j hyphens), b is not changed
t[i][j]=0;
for (int k=0;k<j;k++) {
res1[i][j] = res1[i][j] + "-";
t[i][j]=t[i][j]+ (int)Blosum50.getScore(b.charAt(k) ,'-');
}
res2[i][j] = b.substring(0,j);
}
else if (j==0) { // b is empty -> we need i insertions on b (i hyphens), a is not changed
for (int k=0;k<i;k++) {
res2[i][j] = res2[i][j] + "-";
t[i][j]=t[i][j]+ (int)Blosum50.getScore(a.charAt(k) ,'-');
}
res1[i][j] = a.substring(0,i);
}
else {
// 3 cases (substitution, deletion, insertion), we choose the ones with the bestscore of alignement between a(0...i-1) and b(0...j-1)
int A = (int)Blosum50.getScore(a.charAt(i-1) ,b.charAt(j-1)); 
int B = (int)Blosum50.getScore(a.charAt(i-1) ,'-');
int C = (int)Blosum50.getScore(b.charAt(j-1) ,'-');
t[i][j]=Math.max(Math.max(t[i-1][j] + B, t[i][j-1]+C), t[i-1][j-1] +A);
if (t[i][j]==t[i][j-1]+C) { //insertion in a of b(j-1) ie hyphen on a modified / +b(j-1) on b modified
res1[i][j]= res1[i][j-1]+"-";
res2[i][j] = res2[i][j-1]+b.charAt(j-1);
}
else if (t[i][j]==t[i-1][j]+B){ //deletion in a ie hyphen on b modified / + a(i-1) on a modified 
res1[i][j] = res1[i-1][j]+a.charAt(i-1);
res2[i][j] = res2[i-1][j]+"-";
}
else { //substitution -> + a(i-1) on a modified and +b(j-1) on b modified
res1[i][j]= res1[i-1][j-1]+a.charAt(i-1);
res2[i][j] = res2[i-1][j-1]+b.charAt(j-1);
} 
}

}

public static String[] Score(String a, String b) {
//main function that return the alignment (1st line : a modified, 2nd line : b modified) taking into account the Blowsum50 matrix 
int n = a.length();
int m=b.length();
String[][] res1 = new String[n+1][m+1];
String[][] res2 = new String[n+1][m+1];
int[][]t = new int[n+1][m+1];
for (int i=0;i<n+1;i++) {
res1[i][0]="";
res2[i][0]="";
}
for (int j=0;j<m+1;j++){
res1[0][j]="";
res2[0][j]="";
}
for (int i=0;i<n+1;i++) {////fill in t,res1, res2 by increasing raw and column (because of the initialization in ScoreAux, it is feasible)
for (int j=0;j<m+1;j++) {
ScoreAux(a,b,i,j,t, res1, res2);
}
}
String[] fin = new String[2];
fin[0]=res1[n][m]; //res1(n,m) is a modified for alignment between a(0...n-1) and b(0...m-1)
fin[1]=res2[n][m];//res2(n,m) is b modified for alignment between a(0...n-1) and b(0...m-1)
return fin; //fin is the alignment (2 lines of String)
}


////////////TASK 5 : adapted previous algorithm (2 additional parameters for opening gap penalty and the increasing gap penalty)////////////

public static void ScorePenaltyAux(String a, String b, int i, int j, int[][]t, String[][] res1, String [][] res2, int open, int incr, boolean [][][] pen, int[][][] lettres){
// auxiliary function that fill in t(i,j), res1(i,j), res2(i,j), pen(i,j), lettres(i,j)
// t, res1, res2 same as before 
// pen(i,j) is a 4 cells table :
//pen(i,j,0) = true -> a does not start yet (only hyphen) when comparing a(0...i-1) and b(0...j-1)
// pen(i,j,1) = true -> b does not start yet when comparing a(0...i-1) and b(0...j-1)
//pen(i,j,2) = true -> a is in increasing phase when comparing a(0...i-1) and b(0...j-1)
//pen(i,j,3) = true -> b is in increasing phase when comparing a(0...i-1) and b(0...j-1)
//lettres(i,j,0) = numbers of real letters (no hyphen) in a modified when comparing a(0...i-1) and b(0...j-1)
//lettres(i,j,1) = numbers of real letters (no hyphen) in b modified when comparing a(0...i-1) and b(0...j-1)
int n = a.length();
int m = b.length();
if (i==0 && j==0) { // a and b are empty 
t[i][j]=0;
res1[i][j]=""; 
res2[i][j]="";
pen[i][j][0]=true; 
pen[i][j][1]=true;
pen[i][j][2]=false;
pen[i][j][3]=false;

lettres[i][j][0]=0;
lettres[i][j][1]=0; 
} 

else if (i==0) { // a is empty 
t[i][j]=0; 
for (int k=0;k<j;k++) { 
res1[i][j] = res1[i][j] + "-"; //only hypens on a modified
t[i][j]=t[i][j]+ (int)Blosum50.getScore(b.charAt(k) ,'-');
}
res2[i][j] = b.substring(0,j); // b modified = b 
pen[i][j][0]=true; //a only have hyphens 
pen[i][j][1]=false; //b only have letters (ie not only hyphen)
pen[i][j][2]=false; //no increasing because insertion at the beginning of the word
pen[i][j][3]=false; //no increasing (no hyphen!)
lettres[i][j][0]=0; //no letter in a modified (only hyphen)
lettres[i][j][1]=j; //j letters in b modified that are the j letters of b(0...j-1)
}
else if (j==0) {
for (int k=0;k<i;k++) {
res2[i][j] = res2[i][j] + "-";
t[i][j]=t[i][j]+ (int)Blosum50.getScore(a.charAt(k) ,'-');
}
res1[i][j] = a.substring(0,i);
pen[i][j][0]=false; //a only have letters (ie not only hyphen)
pen[i][j][1]=true; //b only have hyphens 
pen[i][j][2]=false; //no increasing (no hyphen!)
pen[i][j][3]=false; //no increasing because insertion at the beginning of the word
lettres[i][j][0]=i; //i letters in a modified that are the i letters of a(0...i-1)
lettres[i][j][1]=0; //no letter in b modified (only hyphen)
}
else {
int A = (int)Blosum50.getScore(a.charAt(i-1) ,b.charAt(j-1)); //substitution case 
int B;
int C;

//value of B -> deletion case 
if (pen[i-1][j][1] || (lettres[i-1][j][1]==m)) { // if a not started or already finished -> no penalty
B= (int)Blosum50.getScore(a.charAt(i-1) ,'-');}
else if (pen[i-1][j][3]) { //if a is in increasing phase, B = score - increasing penalty
B=(int)Blosum50.getScore(a.charAt(i-1) ,'-')-incr;}
else { //if a is in opening case, B = score - opening penalty
B=(int)Blosum50.getScore(a.charAt(i-1) ,'-')-open;}

// value of C -> insertion case 
if (pen[i][j-1][0] || (lettres[i][j-1][0]==n)) { // if b not started or already finished -> no penalty
C= (int)Blosum50.getScore(b.charAt(j-1) ,'-');}
else if (pen[i][j-1][2]) { //if b is in increasing phase, C = score - increasing penalty
C= (int)Blosum50.getScore(b.charAt(j-1) ,'-')- incr;}
else { //if b is in opening case, C = score - opening penalty
C= (int)Blosum50.getScore(b.charAt(j-1) ,'-') - open;}

t[i][j]=Math.max(Math.max(t[i-1][j] + B, t[i][j-1]+C), t[i-1][j-1] +A); // we select the case that maximize the Score 

if (t[i][j]==t[i][j-1]+C) { //insertion 
res1[i][j]= res1[i][j-1]+"-";
res2[i][j] = res2[i][j-1]+b.charAt(j-1);
lettres[i][j][0] = lettres[i][j-1][0]; // no more letter in a modified
lettres[i][j][1] = lettres[i][j-1][1] +1; //+1 letter in b modified 
pen[i][j][0] = pen[i][j-1][0]; // if a not started it don't start at this step, if started, it is still the case 
pen[i][j][1] = false; //b started (we put a letter)
pen[i][j][2] = true; //if a not started-> this value is not taking into account (the if is before the else if the paragraph just before)
pen[i][j][3] = false; //we put a letter -> can't be increasing
}
else if (t[i][j]==t[i-1][j]+B){ //deletion 
res1[i][j] = res1[i-1][j]+a.charAt(i-1);
res2[i][j] = res2[i-1][j]+"-";
lettres[i][j][0] = lettres[i-1][j][0] +1;//+1 letter in a modified 
lettres[i][j][1] = lettres[i][j-1][1];// no more letter in n modified
pen[i][j][0] = false;//a started (we put a letter)
pen[i][j][1] = pen[i-1][j][1];// if b not started it don't start at this step, if started, it is still the case 
pen[i][j][2] = false;//we put a letter -> can't be increasing
pen[i][j][3] = true; //if b not started-> this value is not taking into account (the if is before the else if the paragraph just before)
}
else { //substitution 
res1[i][j]= res1[i-1][j-1]+a.charAt(i-1);
res2[i][j] = res2[i-1][j-1]+b.charAt(j-1);
lettres[i][j][0] = lettres[i-1][j-1][0] +1; //+1 letter in a modified 
lettres[i][j][1] = lettres[i-1][j-1][1] +1; //+1 letter in b modified 
pen[i][j][0] = false; //a and b start : they have at least 1 letter 
pen[i][j][1] = false;
pen[i][j][2] = false; //a and b not increasing -> we put a letter 
pen[i][j][3] = false;
} 
}

}

public static String[] ScorePenalty(String a, String b, int incr, int open) {
//main function that return the alignment (1st line : a modified, 2nd line : b modified) taking into account penalties 
int n = a.length();
int m=b.length();
String[][] res1 = new String[n+1][m+1];
String[][] res2 = new String[n+1][m+1];
int[][]t = new int[n+1][m+1];
boolean [][][] pen = new boolean [n+1][m+1][4];
int[][][] lettres= new int[n+1][m+1][2];
for (int i=0;i<n+1;i++) {
res1[i][0]="";
res2[i][0]="";
}
for (int j=0;j<m+1;j++){
res1[0][j]="";
res2[0][j]="";
}
for (int i=0;i<n+1;i++) {
for (int j=0;j<m+1;j++) {
ScorePenaltyAux(a,b,i,j,t, res1, res2, open, incr, pen, lettres);
}
}
String[] fin = new String[2];
fin[0]=res1[n][m];
fin[1]=res2[n][m];
return fin;
}

////////////TASK 6 : algorithm computes local alignment (with Blowsum50 matrix and penalties) ////////////
public static String[] LocalAlignment (String a, String b, int incr, int open) {
// naive version in term of space complexity 
int n = a.length();
int m = b.length();
int opt=0;
String loc1="";
String loc2="";
for (int i =0; i<n;i++) {
for(int j=0;j<m;j++) {
String[][] res1 = new String[n+1-i][m+1-j];
String[][] res2 = new String[n+1-i][m+1-j];
int[][]t = new int[n+1-i][m+1-j];
boolean [][][] pen = new boolean [n+1-i][m+1-j][4];
int[][][] lettres= new int[n+1-i][m+1-j][2];
for (int i2=0;i2<n+1-i;i2++) {
res1[i2][0]="";
res2[i2][0]="";
}
for (int j2=0;j2<m+1-j;j2++){
res1[0][j2]="";
res2[0][j2]="";
}
for (int i2=0;i2<n+1-i;i2++) {
for (int j2=0;j2<m+1-j;j2++) {
String ai=a.substring(i);
String bj=b.substring(j);
ScorePenaltyAux(ai,bj,i2,j2,t, res1, res2, open, incr, pen, lettres); 
// auxiliary function that fill in t, res1, res2, pen, lettres for a(i...i2-1) and b(j...j2)
if (t[i2][j2]>opt){ // we select the best score 
opt=t[i2][j2]; // we save the score in opt, a modified in loc1, b modified in loc2
loc1=res1[i2][j2];
loc2=res2[i2][j2];

}
}
}

}
}
String[] fin = new String[2];
fin[0]=loc1; 
fin[1]=loc2;
return fin; //output : best local alignment
}

public static String[] LocalAlignment2 (String a, String b, int incr, int open) { 
//more clever algorithm in term of space complexity (we do not create again t, res1, res2, pen, lettres for each i, j)
int n = a.length();
int m = b.length();
int opt=0;
String loc1="";
String loc2="";
String[][] res1 = new String[n+1][m+1];
String[][] res2 = new String[n+1][m+1];
int[][]t = new int[n+1][m+1];
boolean [][][] pen = new boolean [n+1][m+1][4];
int[][][] lettres= new int[n+1][m+1][2];
for (int i =0; i<n;i++) {
for(int j=0;j<m;j++) { 
for (int i2=i;i2<n+1;i2++) {
res1[i2][j]="";
res2[i2][j]="";
t[i2][j]=0;
}
for (int j2=j;j2<m+1;j2++){
res1[i][j2]="";
res2[i][j2]="";
t[i][j2]=0;
}
for (int i2=i;i2<n+1;i2++) {
for (int j2=j;j2<m+1;j2++) {
String ai=a.substring(i);
String bj=b.substring(j);
ScorePenaltyAux(ai,bj,i2-i,j2-j,t, res1, res2, open, incr, pen, lettres);
if (t[i2][j2]>opt){
opt=t[i2][j2];
loc1=res1[i2][j2];
loc2=res2[i2][j2]; 
}
}
} 
}
}
String[] fin = new String[2];
fin[0]=loc1;
fin[1]=loc2;
return fin;
}
////////////TASK 7 : algorithm returns all the indices that correspond to beginning of perfect matches //////////// 

public static arbre retournarbre(String g, int k) { 
//output : a tree of all words contained in g of size k 
int n = g.length();
arbre arbreg = new arbre(); // initialization of the tree 
if (k>n) { return arbreg;}
for (int i =0; i<n-k+1;i++) { // add all the words of size k ie substring g (i,i+k)
arbreg.add(g.substring(i, i+k)); 
}
return arbreg;
}

public static float getscore2mots (String motg, String mott) { 
//simple function returns the score between 2 words
int n = mott.length();
float s = 0;
for (int i =0;i<n;i++) {
s = s+ Blosum50.getScore(motg.charAt(i),mott.charAt(i));
}
return s; 

}

public static boolean estdansSg(String tk, arbre g,float th, float score, float autoscore,boolean res){
//determines if tk is in Sg 
if(g.isfeuille()){ 
return(score>=th*autoscore); //criteria of Sg 
}
else{
char c = tk.charAt(0);
tk=tk.substring(1);
Set<Character> T = g.branches.keySet();
Iterator<Character> it = T.iterator();
while (it.hasNext()){ //browses all the branches of g 
char key= it.next();
float aug= Blosum50.getScore(c, key); 
arbre h= g.branches.get(key);
boolean res1=estdansSg(tk, h ,th, aug+score, autoscore,res); //recursive relation 
res=res||res1;

}
}
return res;
}

public static LinkedList<Integer> indices (String g, String t, float th,int k ) {
// main function, output : indices of t that correspond to beginning of perfect matches 
int n = t.length();
LinkedList<Integer> indices = new LinkedList<Integer>();
arbre arbreg = retournarbre (g,k); //create the tree of words of g
String tk = t.substring(0,k);
for (int i =0;i<n-k+1;i++) { //browses all the possibilities for tk
float autoscore = getscore2mots(tk,tk);
if (estdansSg(tk,arbreg,th,0,autoscore,false)) { // if tk is in Sg then we can add the corresponding indice
indices.add(i);
}
tk = tk.substring (1); 
char c = t.charAt(k+i-1);
tk=tk+c;
}
return indices;

}

////////////TASK 8 : local alignments with sufficiently high score //////////// 

public static arbrebis retournarbrebis(String g, int k) {
// we need new structure arbrebis, each leaf has indices (reference to the indices of g corresponding to the place in of the subword in g )
int n = g.length();
arbrebis arbreg = new arbrebis();
if (k>n) { return arbreg;}
for (int i =0; i<n-k+1;i++) { // add all the words of size k ie substring g (i,i+k)
arbreg.add(g.substring(i, i+k),i);
}
return arbreg;
}

public static LinkedList<Integer> estdansSgbis(String tk, arbrebis g,float th, float score,float autoscore){
// return the list of beginning indices of g such that tk and the subword of g are corresponding (sens Sg)
LinkedList<Integer> res= new LinkedList<Integer>();
if(g.isfeuille()) {
if(score>=th*autoscore){ //criteria of Sg
for (Integer i: g.indices){ // return all the indices of the subword of g 
res.add(i); 
}
}
} 
else{
char c = tk.charAt(0);
tk=tk.substring(1);
for (char key : g.branches.keySet()){
float aug= Blosum50.getScore(c, key);
arbrebis h= g.branches.get(key);
LinkedList<Integer> res1=estdansSgbis(tk, h ,th, aug+score,autoscore); //same recursive relation than for estdansSg
for (Integer i: res1){
res.add(i); 
}
}
}
return res; 
}

public static HashMap<Integer, LinkedList<Integer>> correspondance (String g, String t, float th,int k) {
// output : the correspondences between all the indices of t and for each the corresponding indices of g such that tk is in Sg
int n = t.length();
HashMap<Integer, LinkedList<Integer>> correspondance = new HashMap<Integer, LinkedList<Integer>>();
arbrebis arbreg = retournarbrebis (g,k);
String tk = t.substring(0,k);
for (Integer i =0;i<n-k;i++) { //browses all the subwords of t except the last one
float autoscore= getscore2mots(tk,tk);
LinkedList<Integer> L= estdansSgbis(tk,arbreg,th,0,autoscore); // indices of g where tk and subword g are corresponding 
correspondance.put(i,L); // we add the relation in the dictionary correspondance between i (beginning indice of t) and L (beginning indices of g)
tk = tk.substring (1);
char c = t.charAt(k+i);
tk=tk+c;
}
float autoscore= getscore2mots(tk,tk); //for the last subword of t 
LinkedList<Integer> L= estdansSgbis(tk,arbreg,th,0,autoscore);
correspondance.put(n-k,L);
return correspondance; 
}

public static float AlignementScore (String g, String t, int gaucheg, int droiteg, int gauchet, int droitet) {
// output : the score of two substrings of g, t giving their extreme indices 
String subg = g.substring(gaucheg, droiteg+1); 
String subt = t.substring(gauchet, droitet+1);
return getscore2mots(subg,subt);
}

public static HashMap<Integer, LinkedList<alignement>> alignements (String g, String t, float th,int k, float thl) {
// output : for all the alignments we store in a dictionnary : 
// the beginning indice in t 
// the alignment (beginning indice of g, length, and associated score)
int n =g.length();
int m = t.length();
float scoremax = getscore2mots(g,g);
HashMap<Integer, LinkedList<Integer>> correspondance = correspondance (g, t, th,k);
HashMap<Integer, LinkedList<alignement>> alignements = new HashMap<Integer, LinkedList<alignement>>();
for (Integer it : correspondance.keySet()) {
for (Integer ig : correspondance.get(it)) {
int gauchet = it; // initialization of gauchet, before the extension 
int gaucheg = ig; //initialization of gauche g, before the extension 
while (gaucheg>0 && gauchet>0 && Blosum50.getScore(t.charAt(gauchet-1),g.charAt(gaucheg-1))>=0 ) {
// extends the matching at the left as long as the corresponding score does not decrease (ie we add a positive score)
gaucheg=gaucheg-1;
gauchet=gauchet-1;
}
int droitet =it+k-1; // initialization of droitet, before the extension 
int droiteg =ig+k-1; // initialization of droiteg, before the extension 
while (droitet<m-1 && droiteg<n-1 && Blosum50.getScore(t.charAt(droitet+1),g.charAt(droiteg+1)) >=0) {
// extends the matching at the right as long as the corresponding score does not decrease (ie we add a positive score)
droitet = droitet+1;
droiteg = droiteg+1;
}
float score = AlignementScore(g,t,gaucheg,droiteg,gauchet,droitet);
if(score>=thl*scoremax) { 
// criteria to store the alignment 
if(alignements.containsKey(gauchet)) { 
alignement valide = new alignement(gaucheg, droiteg-gaucheg+1,score);
boolean exist = false;
for (alignement a : alignements.get(gauchet)) {
exist = exist||a.egal(valide);
}
if (exist==false) { alignements.get(gauchet).add(valide);}
}
else {
alignement valide = new alignement(gaucheg, droiteg-gaucheg+1,score);
LinkedList<alignement> L = new LinkedList<alignement>();
L.add(valide);
alignements.put(gauchet, L);
}
}
}
}
return alignements;
}
////// FUNCTIONS TO REPRESENT OUR RESULTS ////

static void printalignements (String g, String t, float th,int k, float thl) {
HashMap<Integer, LinkedList<alignement>> alignements = alignements (g,t,th,k,thl);
for (Integer it : alignements.keySet()) {
System.out.println("Les alignements commençant par la "+it+"ème lettre de t sont :");
System.out.println();
for (alignement a : alignements.get(it)) {
System.out.println(" - alignement qui commence à l'indice "+a.indiceg+" de g et possède une taille "+a.taille+". Il correspond au score de "+ a.score);
System.out.println();
}
}
}



static void printcorrespondance (HashMap<Integer, LinkedList<Integer>> correspondance) {
for (Integer it : correspondance.keySet()) {
for (Integer ig : correspondance.get(it)) {
System.out.println("alignement qui correspond à l'indice "+it+"pour t et à l'indice "+ig+"");
}
}
}

static void printliste (LinkedList<Integer> L){
for (Integer i:L){
System.out.print(i+"");
} 
}

////PERSONAL TESTS///
public static void main(String[] args) {
String s1="ACTAAGCATCAG";
String s2 ="ATAGGGCAATCT";
String s3="CACTAAAAA";
String s4="CACTAAAAT";
String s5="AAAAAAAA";
String s6="CACTATTAA";


//String[] fin = LocalAlignment(s1,s2,0,1110);
//String[] fin2 = LocalAlignment2(s1,s2,0,1110);
//System.out.println(fin[0]);
//System.out.println(fin[1]);
//System.out.println(fin2[0]);
//System.out.println(fin2[1]);

//LinkedList<Integer> L = indices (s1,s2,0.9F,2);
//for (Integer i : L) {
//System.out.println(i);
//



//boolean resf = estdansSg("TTTT",retournarbre(s4,4),(float)0,0, getscore2mots("TTTT","TTTT"),false);
//System.out.println(resf);
//printliste(estdansSgbis("AAAA",retournarbrebis(s4,4),(float)1.0,0,getscore2mots("AAAA","AAAA")));
//retournarbrebis(s4, 4).printarbre();
//printcorrespondance (correspondance(s3,s4,(float)0.9,4));

//printalignements(s5,s5,(float)0.9, 4, (float)0.1);
}

}

