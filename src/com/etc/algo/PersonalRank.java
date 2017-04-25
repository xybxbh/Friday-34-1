/*package com.etc.algo;

import java.util.HashMap;
import java.util.Iterator;

import com.rs.bean.Rate;

public class PersonalRank {
	private int userNum;
	private int ratingNum;
	private int itemNum;
	private double d;
	private HashMap<Integer, Double> userVector; 
	private HashMap<Integer, Double> itemVector;
	private HashMap<Integer, Double> userTemp;
	private HashMap<Integer, Double> itemTemp;
	private HashMap<Integer, Rate> ratingMatrix;
	private HashMap<Integer, HashMap<Integer, Double>> graph;
	
	public PersonalRank(int userNum, int ratingNum, int itemNum,
			int rank, double maxScore, double minScore,
			HashMap<Integer, Rate> ratingMatrix, HashMap<Integer, HashMap<Integer, Double>> graph) {
		this.userNum = userNum;
		this.ratingNum = ratingNum;
		this.itemNum = itemNum;
		this.d = 0.85;
		this.ratingMatrix = ratingMatrix;
		this.graph = graph;
		this.userVector = new HashMap<Integer, Double>();
		this.itemVector = new HashMap<Integer, Double>();
		this.userTemp = new HashMap<Integer, Double>();
		this.itemTemp = new HashMap<Integer, Double>();
	}

	public void personalRank(int root, int max_step){
		
		
		userVector.put(root, 1.0);
		for(int k = 0; k < max_step; k++){      //遍历trainSet
			
			
			Iterator iterator1 = graph.keySet().iterator();
			while (iterator1.hasNext()) {
				int nextuser = (int) iterator1.next();
				HashMap<Integer, Double> itemMap = graph.get(iterator1.next());
				Iterator it1 = itemMap.keySet().iterator();
				while (it1.hasNext()) {
					double t;
					if (itemTemp.containsKey(it1.next())) {
						t = itemTemp.get(it1.next()) + (double)(d*userVector.get(nextuser)/itemMap.size());
					}
					else {
						t = (double)(d*userVector.get(nextuser)/itemMap.size());
					}
					itemTemp.put((Integer) it1.next(), t);
				}
				
			}
			
			Iterator iterator2 = graph.keySet().iterator();
			while (iterator2.hasNext()) {
				int nextuser = (int) iterator2.next();
				HashMap<Integer, Double> itemMap = graph.get(iterator1.next());
				Iterator it1 = itemMap.keySet().iterator();
				while (it1.hasNext()) {
					double t;
					if (itemTemp.containsKey(it1.next())) {
						t = itemTemp.get(it1.next()) + (double)(d*userVector.get(nextuser)/itemMap.size());
					}
					else {
						t = (double)(d*userVector.get(nextuser)/itemMap.size());
					}
					itemTemp.put((Integer) it1.next(), t);
				}
				
			}
			
			Iterator<Integer> m=inverse_table.keySet().iterator();
			while(m.hasNext()){
				int mm=m.next();
				Set<Integer> us=inverse_table.get(mm);
				Iterator<Integer> it=us.iterator();
				while(it.hasNext()){
					temp1[it.next()]+=alpah*rank2[mm]/us.size();

				}
			}
			temp1[root]+=1-alpah;
			for(int i=0;i<temp1.length;i++)
				rank1[i]=temp1[i];
			
			
			for(int i=0;i<temp2.length;i++)
				rank2[i]=temp2[i];
			
			
		}
		recommendedMoviesList=new ArrayList<Rank>();
		Set<Integer> watched_movies=trainset.get(root);
		for(int i=0;i<rank2.length;i++){
			if(watched_movies.contains(i)||rank2[i]==0.0)
				continue;
			Rank r=new Rank();
			r.setMovie(i);
			r.setSum_simlatrity(rank2[i]);
			recommendedMoviesList.add(r);
		}
		Heapsort ss=new Heapsort();
		ss.sort(recommendedMoviesList, n);
	}
		
	public void evaluate(){
		int rec_count=0;
		int test_count=0;
		int hit=0;
		double popularSum=0;
		Set<Integer> all_rec_movies=new HashSet<Integer>();
		Iterator<Integer> it=trainset.keySet().iterator();
		while(it.hasNext()){
			int user=it.next();
			if(user%5==0)
				System.out.println("已经推荐了"+user+"个用户");
			
			Set<Integer> test_movies=testset.get(user);
			personalRank(user, 20);
			
			if(recommendedMoviesList!=null&&test_movies!=null){
				if(recommendedMoviesList.size()<n) n=recommendedMoviesList.size();
			for(int i=0;i<n;i++){
				Rank rec_movie=recommendedMoviesList.get(i);
				if(test_movies.contains(rec_movie.getMovie())){
					hit++;
				}
				all_rec_movies.add(rec_movie.getMovie());

				popularSum+=Math.log(1+movie_popular.get(rec_movie.getMovie()));

			}
			
			rec_count+=n;
			test_count+=test_movies.size();
			}
			
		}
		
		double precision=hit/(1.0*rec_count);
		double recall=hit/(1.0*test_count);
		double coverage=all_rec_movies.size()/(1.0*movie_count);
		double popularity=popularSum/(1.0*rec_count);
		System.out.println("precision=%"+precision*100+"\trecall=%"+recall*100+"\tcoverage=%"+coverage*100+"\tpopularity="+popularity);
		
	}
}
*/