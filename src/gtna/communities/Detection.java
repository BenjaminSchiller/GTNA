/*
 * ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 * 
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 * 
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * Detection.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.communities;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorting;
import gtna.util.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Detection {

    public static CommunityList detectCommunities(Graph graph){
        String algo = Config.get("COMMUNITY_DETECTION_ALGORITHM");
        if (algo.equals("lpa")){
            return lpa(graph);
        } else if(algo.equals("lpa_ext")){
            try{
              return lpaExtended(graph,
                                 (EdgeWeight) Class.forName(Config.get("COMMUNITY_DETECTION_LPA_EXT_W")).newInstance(),
                                 (NodeCharacteristic) Class.forName(Config.get("COMMUNITY_DETECTION_LPA_EXT_F")).newInstance(),
                                 Double.parseDouble(Config.get("COMMUNITY_DETECTION_LPA_EXT_M")),
                                 Double.parseDouble(Config.get("COMMUNITY_DETECTION_LPA_EXT_D")));
            } catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("invalid config - " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        } else{
            throw new RuntimeException("invalid config - unknown algorithm");
        }
    }
    
    public static CommunityList lpa(Graph graph){
        long start = System.currentTimeMillis();
        Node[] nodes = graph.nodes;
        int[] labels  = new int[nodes.length];
        for(int i = 0; i < labels.length; i++){
            labels[i] = i;
        }
        Random rand = new Random(System.currentTimeMillis());

        //label propagation loop
        int t = 0;
        boolean finished = false;
        while(!finished){
            finished = true;
            t++;
            Node[] X = NodeSorting.random(nodes, rand);
            for (Node x : X){
                List<Integer> maxLabel = selectMax(x.out(), labels);
                if (maxLabel.size() > 0){
                    int max = maxLabel.get(rand.nextInt(maxLabel.size()));
                    labels[x.index()] = max;
                }
            }
            for (Node x : X){
                List<Integer> maxLabel = selectMax(x.out(), labels);
                if (maxLabel.size() > 0 && !maxLabel.contains(labels[x.index()])){
                    finished = false;
                }
            }
        }
        long end = System.currentTimeMillis();
        CommunityList communities = createCommunityList(nodes, labels);
        communities.addInfo("ALGO_NAME", Config.get("COMMUNITY_DETECTION_LPA_NAME"));
        communities.addInfo("ALGO_RUNTIME", end-start);
        communities.addInfo("ALGO_ITERATIONS", t);
        return communities;
    }

    private static List<Integer> selectMax(Node[] neighbours, int[] labels){
        HashMap<Integer, Integer> count = new HashMap<Integer,Integer>(neighbours.length);
        for (Node n : neighbours){
            int label = labels[n.index()];
            Integer c = count.get(label);
            if (c == null){
                count.put(label, Integer.valueOf(1));
            } else {
                count.put(label, Integer.valueOf(c.intValue() + 1));
            }
        }
        int max = 0;
        List<Integer> maxLabel = new ArrayList<Integer>();
        for (int label : count.keySet()){
            int c = count.get(label);
            if (c == max){
                maxLabel.add(label);
            } else if (c > max){
                max = c;
                maxLabel.clear();
                maxLabel.add(label);
            }
        }
        return maxLabel;
    }

    private static CommunityList createCommunityList(Node[] nodes, int[] labels){
        HashMap<Integer, Community> communities = new HashMap<Integer, Community>();
        int cid = 0;
        for(Node node : nodes){
            int label = labels[node.index()];
            Community community = communities.get(label);
            if (community == null){
                community = new Community(cid++);
                community.addNode(node);
                communities.put(label, community);
            } else {
                community.addNode(node);
            }
        }
        return new CommunityList(communities.values());
    }

    public static CommunityList lpaExtended(Graph graph, EdgeWeight w, NodeCharacteristic f, double m, double d){
        long start = System.currentTimeMillis();
        Node[] nodes = graph.nodes;
        int[] labels  = new int[nodes.length];
        double[] scores = new double[nodes.length];
        for(int i = 0; i < labels.length; i++){
            labels[i] = i;
            scores[i] = 1.0;
        }
        Random rand = new Random(System.currentTimeMillis());

        //label propagation loop
        int t = 0;
        int finished = 0;
        while(finished < nodes.length){
            finished = 0;
            t++;
            Node[] X = NodeSorting.random(nodes, rand);
            for (Node x : X){
                int maxLabel = selectMaxExtended(x, x.out(), labels, scores, w, f , m);
                if (maxLabel != labels[x.index()]){
                    labels[x.index()] = maxLabel;
                    scores[x.index()] = scores[x.index()] - d;
                } else {
                    finished++;
                }
            }
        }
        long end = System.currentTimeMillis();
        CommunityList communities = createCommunityList(nodes, labels);
        communities.addInfo("ALGO_NAME", Config.get("COMMUNITY_DETECTION_LPA_EXT_NAME"));
        communities.addInfo("ALGO_RUNTIME", end-start);
        communities.addInfo("ALGO_ITERATIONS", t);
        return communities;
    }

    private static int selectMaxExtended(Node n, Node[] out, int[] l, double[] s, EdgeWeight w, NodeCharacteristic f, double m){
        HashMap<Integer, Double> sums = new HashMap<Integer, Double>();
        for(Node dst : out){
            int label = l[dst.index()];
            double weight = w.getWeight(n, dst) + w.getWeight(dst, n);
            double psum = s[dst.index()] * Math.pow(f.getCharacteristic(dst), m) * weight;
            Double sum = sums.get(label);
            if (sum != null){
                psum += sum;
            }
            sums.put(label, psum);
        }

        int maxLabel = l[n.index()];
        double maxSum = 0;
        for (int label : sums.keySet()){
            if (sums.get(label) > maxSum){
                maxSum = sums.get(label);
                maxLabel = label;
            }
        }
        return maxLabel;
    }

    public static interface EdgeWeight {
        public double getWeight(Node src, Node dst);
    }

    public static interface NodeCharacteristic {
        public double getCharacteristic(Node node);
    }

    public static class DefaultEdgeWeight implements EdgeWeight{
       public double getWeight(Node src, Node dst) {
           return 0.5;
       }
    }

    public static class DefaultNodeCharacteristic implements NodeCharacteristic{
       public double getCharacteristic(Node node) {
           return node.out().length;
       }
    }






    public static Role[] detectRoles(Graph graph, CommunityList communities){
        Role[] roles = new Role[graph.nodes.length];

        for (Node node : graph.nodes){
            //calculate relative within module degree z
            double k = communities.withinModuleDegree(node);
            double avgk = 0;
            double avgkquad = 0;
            Community com = communities.getCommunity(node);
            for(Node n : com.getNodes()){
                avgk += communities.withinModuleDegree(n);
                avgkquad += Math.pow(communities.withinModuleDegree(n), 2);
            }
            avgk /= com.getSize();
            avgkquad /= com.getSize();
            double z = (k - avgk) / Math.sqrt(avgkquad - Math.pow(avgk, 2));

            //calculate participation coefficient p
            HashMap<Community, Integer> counter = new HashMap<Community, Integer>();
            for (Node dst : node.out()){
                Integer c = counter.get(communities.getCommunity(dst));
                if (c == null){
                    c = 1;
                } else {
                    c += 1;
                }
                counter.put(communities.getCommunity(dst), c);
            }
            double psum = 0;
            for(Community community : counter.keySet()){
                psum += Math.pow(counter.get(community) / node.out().length, 2);
            }
            double p = 1 - psum;

            //role classification
            if(z < 2.5){
                if(p <= 0.05){
                    roles[node.index()] = Role.R1;
                } else if(p <= 0.62){
                    roles[node.index()] = Role.R2;
                } else if(p <= 0.8){
                    roles[node.index()] = Role.R3;
                } else {
                    roles[node.index()] = Role.R4;
                }
            } else {
                if(p <= 0.3){
                    roles[node.index()] = Role.R5;
                } else if (p <= 0.75){
                    roles[node.index()] = Role.R6;
                } else{
                    roles[node.index()] = Role.R7;
                }
            }
        }
        return roles;
    }

}
