
package gtna.communities;

import gtna.graph.Node;

import java.util.ArrayList;
import java.util.Collection;

public class Community {

    private final int id;
    private final Collection<Node> nodes = new ArrayList<Node>();
    private CommunityList communityList = null;

    public Community(int id){
        this.id = id;
    }

    public void addNode(Node node){
        if (nodes.add(node) && communityList != null)
            communityList.addNodeMapping(node, this);
    }

    public void removeNode(Node node){
        if (nodes.remove(node) && communityList != null)
            communityList.removeNodeMapping(node);
    }

    public boolean containsNode(Node node){
        return nodes.contains(node);
    }

    public Collection<Node> getNodes(){
        return nodes;
    }
    
    public int getSize(){
        return nodes.size();
    }

    public int getId(){
        return id;
    }

    void setCommunityList(CommunityList communityList){
        this.communityList = communityList;
    }

    @Override
    public String toString(){
        return "Community" + String.valueOf(id);
    }

}
