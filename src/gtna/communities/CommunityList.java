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
 * GTNA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GTNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * CommunityList.java
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

import gtna.graph.NodeImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommunityList {

    private ArrayList<Community> communities = new ArrayList<Community>();
    private Map<NodeImpl, Community> nodeMap = new HashMap<NodeImpl, Community>();
    private Map<String, Object> information = new HashMap<String, Object>();

    public CommunityList(Collection<Community> communities){
        for(Community community : communities){
            addCommunity(community);
        }
    }

    public void addCommunity(Community community){
        community.setCommunityList(this);
        communities.add(community);
        for (NodeImpl node : community.getNodes()){
            this.nodeMap.put(node, community);
        }
    }

    public void removeCommunity(Community community){
        communities.remove(community);
        for (NodeImpl node : community.getNodes()){
            this.nodeMap.remove(node);
        }
    }

    public int getSize(){
        return getCommunities().size();
    }

    public Collection<Community> getCommunities(){
        return communities;
    }

    public Community getCommunity(NodeImpl node){
        return nodeMap.get(node);
    }

    void addNodeMapping(NodeImpl node, Community community){
        nodeMap.put(node, community);
    }

    void removeNodeMapping(NodeImpl node){
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
            for (NodeImpl n : c.getNodes()){
                E += n.out().length;
            }
        }
        double Q = 0;
        for (Community c : getCommunities()){
            double IC = 0;
            double OC = 0;
            for (NodeImpl src : c.getNodes()){
                for (NodeImpl dst : src.out()){
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

    public int withinModuleDegree(NodeImpl node){
        int k = 0;
        for(NodeImpl dst : node.out()){
            if (getCommunity(dst) == getCommunity(node)){
                k++;
            }
        }
        return k;
    }

    public double avgWithinModuleDegree(Community community){
        double k = 0;
        for(NodeImpl n : community.getNodes()){
            k += withinModuleDegree(n);
        }
        return k / community.getSize();
    }

    @Override
    public String toString(){
        return "|communities| = " + getSize();
    }
}
