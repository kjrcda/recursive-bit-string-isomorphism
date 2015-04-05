//This class read in a text file and parses it into
//an adjacency matrix, visited array and the numVert
public class GraphInput {

	private String filename;
	
	public GraphInput(String name){
		filename = name;
	}
	
	public GraphResult readInput(){
		return readInput(filename);
	}
	
	public GraphResult readInput(String name){
		filename = name;
		GraphResult retVal = null;
		
		try {
			java.util.Scanner in = new java.util.Scanner(new java.io.File(filename));
			String line;
			int numVert;
			int numEdges=0;
			int[][] matrix;
			boolean visited[];
			String graphName;
			
			graphName = in.nextLine();
			
			numVert = in.nextInt();
			in.nextLine();
			matrix = new int[numVert][numVert];
			visited = new boolean[numVert];
			
			//read input + create edges
			while(in.hasNext())
			{
				line = in.nextLine();
				String parts[] = line.split("\\s+");
				int u = -(Integer.parseInt(parts[0]));
				int v;
				
				for(int i=1; i< parts.length; i++)
				{
					v = Integer.parseInt(parts[i]);
					if(v!=0)
					{
						//create edges
						matrix[u-1][v-1] = 1;
						matrix[v-1][u-1] = 1;
						numEdges++;
					}
				}
			}
			in.close();
			for(int i=0; i<numVert; i++)
				visited[i] = false;
			retVal = new GraphResult(matrix, visited, numVert, numEdges, graphName);
		} catch(java.io.FileNotFoundException fnfe){
			System.out.println("Could not open file");
		}
		return retVal;
	}
	
	class GraphResult
	{
		int matrix[][];
		boolean visited[];
		int numVert;
		int numEdges;
		String name = "";
		
		public GraphResult(int rix[][], boolean vis[], int num, int edges, String name)
		{
			matrix = rix;
			visited = vis;
			numVert = num;
			numEdges = edges;
			this.name = name;
		}
		
		public void printMatrix()
		{
			for(int i=0; i<numVert; i++)
			{
				for(int j=0; j<numVert; j++)
					System.out.print(matrix[i][j] + " ");
				System.out.println();
			}
		}
	
	}
}
