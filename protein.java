import java.util.LinkedList;

public class protein {
 String sequence;
 LinkedList<Integer> bloc;

 public protein(String sequence,LinkedList<Integer> bloc){
	 this.sequence=sequence;
	this.bloc=bloc;
 }
 
 public static void printListe(LinkedList<Integer> bloc){
	int N=bloc.size();
	for (int i=0; i<N; i++){
		System.out.print(bloc.get(i));
	}	
 }
 
 public static int FirstH(String S){
	 int compt=0;
	 int N=S.length();
	 char c=S.charAt(compt);
	 while (c!='H'&&compt<N){
		 compt=compt+1;
		 c=S.charAt(compt);
	 }
	 return compt;
 }
 
 public static int nextH(String S, int i){
	 int compt=1; 
	 int N=S.length();
	 if (i==N-1){
		 return(compt);	 
	 }
	 char c=S.charAt(i+1);
	 while ((compt+i+1<N)&&(c!='H')){	
		 c=S.charAt(i+compt);
		 compt=compt+1;
		 }
	 return (compt);
 }	 
	 
 
 public protein formationBloc(){
	 String s1=this.sequence;
	 LinkedList<Integer> blok=new LinkedList<Integer>();
	 int N=s1.length();
	 int lettreCourante=0;
	 int Init= FirstH(s1);
	 if (Init==0||Init==1){
		 for (int i=0; i<Init+1;i++){
			 blok.add(1);
		 }
		 lettreCourante=Init;
	 }
	 else{
		 for (int i=0; i<2*(Init/2)+1;i++){
				 blok.add(0);
			 }
		 lettreCourante=2*(Init/2);
		 }  
	 
	 
	 int QuelBloc=0;   // Oscille entre 0 et 1 et détermine le bloc courant
	 int NextH=N+1;
	 while (lettreCourante<N&&NextH>1){
		NextH = nextH(s1,lettreCourante); 
		int borne = NextH+lettreCourante;
		if (NextH%2==0){  
			while (lettreCourante<N&&lettreCourante<borne-1){
				blok.add(0);
				lettreCourante=lettreCourante+1;
			}
		QuelBloc=(QuelBloc+1)%2;	
		}
		else {
			if(QuelBloc==0){
				while (lettreCourante<N&&lettreCourante<borne-1){
					blok.add(1);
					lettreCourante=lettreCourante+1;	
				}
			}
			else {
				while (lettreCourante<N&&lettreCourante<borne-1){
					blok.add(2);
					lettreCourante=lettreCourante+1;
				}
			}
		 
		}
	 }
	 protein P=new protein(s1,blok);
	 return(P);
 }



 
 public static void main(String[] args) {
	// TODO Auto-generated method stub
	String s1= "HPPPHPHPPPPHPPPHPPHPPPPPH";
	 LinkedList<Integer> blok=new LinkedList<Integer>();
	 for (int i=0; i<4;i++){blok.add(i);}
	 protein P=new protein(s1,blok);
	 P= P.formationBloc();
	 System.out.println(P.sequence);
	 System.out.println(nextH(s1,4));
	 System.out.println(s1.length());
	 printListe(P.bloc);
	 System.out.println(FirstH(s1));
}

}
