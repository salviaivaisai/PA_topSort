

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

/*
Clasa pentru graf ce contine 
noVertices = numarul de noduri (va fi 26, adica numarul de litere din alfabet)
adjMatrix = matricea de adiacenta cu 1 pe muchii
solution = String-ul cu alfabetul permutat
*/

class Graph {
	
	int noVertices;
	int[][] adjMatrix;
	static String solution;
	
	public Graph(int v){
		this.noVertices = v;
		this.adjMatrix = new int[v][v];
		
	}
	
	void addEdge(Integer u, Integer v){
		this.adjMatrix[u][v] = 1;
	}
	
	// intoarce primul nod din graf, adica 0, adica litera A
	// este folosit pentru detectarea ciclurilor
	int getFirstNode(){
		return 0;
	}
	
	/*metoda recursiva ajutatoare pentru verificarea ciclurilor
	foloseste 2 vectori boolean, unul pentru vizitare temporara - visited
	si unul pentru vizitare in recursivitate - rec
	recursivitatea incepe de la start
	metoda intoarce true - daca exista ciclu
	false - in caz contrar
	*/
	boolean hasCycleRec(int start, boolean[] visited, boolean[] rec){
		if (!visited[start]){
			visited[start] = true;
			rec[start] = true;
			
			for ( int i = 0; i < 26; i++){
				if (adjMatrix[start][i] == 1){
					if (!visited[i] && hasCycleRec(i, visited, rec))
						return true;
					else if (rec[i])
						return true;
				}
			}
		}
		rec[start] = false;
		return false;
	}
	
	/*
	metoda de detectare a ciclului, ce foloseste hasCycleRec
	incepe de la nodul start, face verificare pentru toate nodurile
	daca se gaseste un ciclu - intoarce true
	altfel - intoarce false
	*/
	boolean hasCycle(int start){
		boolean[] visited = new boolean[26];
		boolean[] rec = new boolean[26];
		
		for (int i = 0; i < noVertices; i++){
			if (hasCycleRec(i, visited, rec))
				return true;
		}
		return false;
	}
	
	/*
	metoda ajutatoare pentru sortarea topologica
	parametrii v - nodul de unde incepe
	visited - vector boolean ce marcheaza nodurile vizitate
	stack - stiva in care sunt introduse nodurile in ordinea sortarii
	*/
	void topSortRec(int v, boolean[] visited, Stack<Integer> stack){
		visited[v] = true;
		for (int i = 0; i < 26; i++){
			if (adjMatrix[v][i] != 0){
				if (!visited[i])
					topSortRec(i, visited, stack);
			}
		}
		stack.push(v);
	}
	
	/*
	metoda ce face sortarea efectiva
	foloseste metoda topSortRec pentru a construi stiva
	scoate pe rand nodurile din stiva si construieste String-ul solution
	*/
	void topSort(){
		Stack<Integer> stack = new Stack<Integer>();
		boolean[] visited = new boolean[26];
		
		for (int i = 0; i < 26; i++){
			if (visited[i] == false)
				topSortRec(i, visited, stack);
		}
		
		solution = "";
		while (!stack.empty()){
			int node = stack.pop();
			solution += Character.toString((char)(node + 97));
		}
	}
	
}

public class Permutari {

	public static void main(String[] args) {

		try {
			BufferedReader br = new BufferedReader(new FileReader("permutari.in"));
			
			int N = Integer.parseInt(br.readLine());
			
			String[] words = new String[N];
			for (int i = 0 ; i < N; i++){
				words[i] = br.readLine();
			}
			
			br.close();
			
			Graph g = new Graph(26);
			int n;
			
			/*
			construirea matricii de adiacenta:
			pentru fiecare doua cuvinte se compara literele
			daca literele sunt egale, merge mai departe
			daca sunt diferite, se trage muchie in ordinea aparitiei (i -> i+1)
			*/
			for (int i = 0; i < N - 1 ; i++){
				if (words[i].length() > words[i+1].length())
					n = words[i+1].length();
				else
					n = words[i].length();
				for (int j = 0; j < n; j++){
					if (words[i].charAt(j) == words[i+1].charAt(j))
						continue;
					else{
						int u = (int)words[i].charAt(j) - 97;
						int v = (int)words[i+1].charAt(j) - 97;
						g.addEdge(u, v);
					}
				}
			}
			
			g.topSort();
			

			Writer wr = new BufferedWriter(new FileWriter("permutari.out"));
			if (g.hasCycle(g.getFirstNode())){       //daca exista ciclu, se afiseaza "Imposibil"
				wr.write(String.valueOf("Imposibil"));
			}
			else{
				wr.write(String.valueOf(Graph.solution));
			}
			
			wr.close();


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
