package gtna.communities;

import gtna.graph.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommunityList {

    private ArrayList<Community> communities = new ArrayList<Community>();
    private Map<Node, Community> nodeMap = new HashMap<Node, Community>();
    private Map<String, Object> information = new HashMap<String, Object>();

    public CommunityList(Collection<Community> communities){
        for(Community community : communities){
            addCommunity(community);
        }
    }

    public void addCommunity(Community community){
        community.setCommunityList(this);
        communities.add(community);
        for (Node node : community.getNodes()){
            this.nodeMap.put(node, community);
        }
    }

    public void removeCommunity(Community community){
        communities.remove(community);
        for (Node node : community.getNodes()){
            this.nodeMap.remove(node);
        }
    }

    public int getSize(){
        return getCommunities().size();
    }

    public Collection<Community> getCommunities(){
        return communities;
    }

    public Community getCommunity(Node node){
        return nodeMap.get(node);
    }

    void addNodeMapping(Node node, Community community){
        nodeMap.put(node, community);
    }

    void removeNodeMapping(Node node){
        nodeMap.remove(node);
    }

    public void addInfo(String key, Object value){
        information.put(key, value);
    }

    public Object getInfo(String key){
        return information.get(key);
    }

    public int minCommunitySize(){
        int minSize = Integer.MAX_VALUE;
        for (Community c : getCommunities()){
            if (c.getSize() < minSize){
                minSize = c.getSize();
            }
        }
        return minSize == Integer.MAX_VALUE ? 0 : minSize;
    }

    public int maxCommunitySize(){
        int maxSize = 0;
        for (Community c : getCommunities()){
            if (c.getSize() > maxSize){
                maxSize = c.getSize();
            }
        }
        return maxSize;
    }

    public double avgCommunitySize(){
        double size = 0;
        for (Community c : getCommunities()){
            size += c.getSize();
        }
        return size / this.getSize();
    }

    public double calculateModularity(){
        double E = 0;
        for (Community c : getCommunities()){
            for (Node n : c.getNodes()){
                E += n.out().length;
            }
        }
        double Q = 0;
        for (Community c : getCommunities()){
            double IC = 0;
            double OC = 0;
            for (Node src : c.getNodes()){
                for (Node dst : src.out()){
                    if (getCommunity(dst) == c){
                        IC++;
                    } else {
                        OC++;
                    }
                }
            }
            Q += IC / E - Math.pow((2 * IC + OC) / (2 * E), 2);
        }
        return Q;
    }

    public int withinModuleDegree(Node node){
        int k = 0;
        for(Node dst : node.out()){
            if (getCommunity(dst) == getCommunity(node)){
                k++;
            }
        }
        return k;
    }

    public double avgWithinModuleDegree(Community community){
        double k = 0;
        for(Node n : community.getNodes()){
            k += withinModuleDegree(n);
        }
        return k / community.getSize();
    }

    @Override
    public String toString(){
        return "|communities| = " + getSize();
    }
}
