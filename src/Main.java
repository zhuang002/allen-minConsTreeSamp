import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph graph = new Graph();
		graph.load();
		Graph minConsTree = graph.getMinConsTree();
		minConsTree.print();
	}

}

class Graph {
	int nNodes;
	ArrayList<Path> paths = new ArrayList<>();
	
	public void load() {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		this.nNodes = sc.nextInt();
		int paths = sc.nextInt();
		for (int i=0;i<paths;i++) {
			int id1 = sc.nextInt();
			int id2 = sc.nextInt();
			int length = sc.nextInt();
			this.paths.add(new Path(id1,id2, length));
		}
	}

	public Graph getMinConsTree() {
		// TODO Auto-generated method stub
		Set<Set<Integer>> groups = new HashSet<>();
		int nNodesIncluded = 0;
		Graph tree = new Graph();
		
		Collections.sort(this.paths, (x,y)->x.length-y.length);
		
		for (Path path:this.paths) {
			if (nNodesIncluded>=this.nNodes && groups.size()==1)
				break;
			nNodesIncluded+=addPathNodesToGroups(path, groups, tree);
		}
		tree.nNodes = this.nNodes;
		return tree;
	}

	private int addPathNodesToGroups(Path path, Set<Set<Integer>> groups, Graph tree) {
		// TODO Auto-generated method stub
		Set<Integer> group1 = getGroupOfId(path.id1, groups);
		Set<Integer> group2 = getGroupOfId(path.id2, groups);
		
		if (group1 == null) {
			if (group2 == null) { // id1 and id2 are isolated.
				Set<Integer> group = new HashSet<>();
				group.add(path.id1);
				group.add(path.id2);
				groups.add(group);
				tree.paths.add(path);
				return 2;
			} else { // id1 is isolated, id2 is in group2
				group2.add(path.id1);
				tree.paths.add(path);
				return 1;
			}
		} else { // id1 is in group1
			if (group2 == null) { // id1 is in group1, id2 is isolated.
				group1.add(path.id2);
				tree.paths.add(path);
				return 1;
			} else { // id1 is in group1, id2 is in group2
				if (group1 == group2) {// id1 and id2 are in a same group
					return 0;
				} else { // id1 and id2 are in different groups.
					group1.addAll(group2);
					groups.remove(group2);
					tree.paths.add(path);
					return 0;
				}
			}
			
		}
	}

	private Set<Integer> getGroupOfId(int id, Set<Set<Integer>> groups) {
		// TODO Auto-generated method stub
		for (Set<Integer> group:groups) {
			if (group.contains(id)) {
				return group;
			}
		}
		return null;
	}

	public void print() {
		// TODO Auto-generated method stub
		Collections.sort(this.paths, (x,y)->{
			if (x.id1<y.id1)
				return -1;
			else if (x.id1>y.id1) {
				return 1;
			} else {
				if (x.id2<y.id2) {
					return -1;
				} else if (x.id2>y.id2) {
					return 1;
				} else {
					return 0;
				}
			} 
		});
		
		System.out.println(this.nNodes+" "+this.paths.size());
		for (Path path:this.paths) {
			System.out.println(path);
		}
	}
}

class Path {
	int id1,id2;
	int length;
	
	public Path(int id1, int id2, int length) {
		if (id1<id2) {
			this.id1 = id1;
			this.id2 = id2;
		} else {
			this.id1 = id2;
			this.id2 = id1;
		}
		this.length = length;
	}

	@Override
	public String toString() {
		return id1+" "+id2+" "+length;
	}
	
	
}
