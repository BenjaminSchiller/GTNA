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
 * IO.java
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

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.Filewriter;
import gtna.util.Config;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class IO {

    public static enum ExportFormat {DOT, GML};

    public static void write(Graph graph, CommunityList communities, Role[] roles, String filename){
        ExportFormat format = ExportFormat.valueOf(Config.get("COMMUNITY_EXPORT_FORMAT"));
        write(graph, communities, roles, filename, format);
    }

    public static void write(Graph graph, CommunityList communities, Role[] roles, String filename, ExportFormat format){
        Filewriter fr = new Filewriter(filename + "." + Config.get(format.toString()+"_FILE_EXTENSION"));
        switch(format){
            case DOT:
                fr.write(toDOT(graph, communities, roles));
                break;
            case GML:
                fr.write(toGML(graph,communities, roles));
                break;
        }
        fr.close();
    }

    public static String toDOT(Graph graph, CommunityList communities, Role[] roles){
        boolean useSubgraph = Config.getBoolean("COMMUNITY_EXPORT_DOT_SUBGRAPH");
        boolean useCluster = Config.getBoolean("COMMUNITY_EXPORT_DOT_CLUSTER");

        //prepare colors and shapes
        Map<Community, String> communityColors = null;
        if(Config.get("COMMUNITY_EXPORT_DOT_NODE_ATTRIBUTES").contains("%COMMUNITY_COLOR%")){
            communityColors = generateCommunityColors(communities);
        }
        Map<Role, String> roleColors = null;
        if(Config.get("COMMUNITY_EXPORT_DOT_NODE_ATTRIBUTES").contains("%ROLE_COLOR%")){
            roleColors = readRoleColors();
        }
        Map<Role, String> roleShapes = null;
        if(Config.get("COMMUNITY_EXPORT_DOT_NODE_ATTRIBUTES").contains("%ROLE_SHAPE%")){
            roleShapes = readRoleShapes();
        }

        StringBuilder out = new StringBuilder();
        out.append("graph \"");
        out.append(graph.name);
        out.append("\"{\n");

        //write nodes
        if(useSubgraph){
            for(Community community : communities.getCommunities()){
                out.append("subgraph ");
                if (useCluster){
                    out.append("cluster_");
                }
                out.append(community).append(" {\n");
                Node[] nodes = community.getNodes().toArray(new Node[community.getSize()]);
                writeNodes(out, nodes, communities, roles, communityColors, roleColors, roleShapes);
                out.append("}\n");
            }
        } else{
            writeNodes(out, graph.nodes, communities, roles, communityColors, roleColors, roleShapes);
        }

        //write edges
        HashMap<String, Boolean> printed = new HashMap<String, Boolean>(graph.edges * 2);
        for (Edge edge: graph.edges()){
            if(!Boolean.TRUE.equals(printed.get(Edge.toString(edge.src, edge.dst)))){
                printed.put(Edge.toString(edge.src, edge.dst), Boolean.TRUE);
                printed.put(Edge.toString(edge.dst, edge.src), Boolean.TRUE);
                out.append((edge.src.index() + 1));
                out.append(" -- ");
                out.append(edge.dst.index() + 1);
                out.append(";\n");
            }
        }

        out.append("}");
        return out.toString();
    }

    private static void writeNodes(StringBuilder out, Node[] nodes, CommunityList communities, Role[] roles,
                        Map<Community, String> communityColors, Map<Role, String> roleColors, Map<Role,String> roleShapes){
         for (Node node : nodes){
            String nodeAttributes = Config.get("COMMUNITY_EXPORT_DOT_NODE_ATTRIBUTES");
            if(communityColors != null){
            nodeAttributes = nodeAttributes.replace("%COMMUNITY_COLOR%", communityColors.get(communities.getCommunity(node)));
            }
            if(roleColors != null){
                nodeAttributes = nodeAttributes.replace("%ROLE_COLOR%", roleColors.get(roles[node.index()]));
            }
            if(roleShapes != null){
                nodeAttributes = nodeAttributes.replace("%ROLE_SHAPE%", roleShapes.get(roles[node.index()]));
            }
            nodeAttributes = nodeAttributes.replace("%ROLE%", roles[node.index()].toString());
            nodeAttributes = nodeAttributes.replace("%ID%", String.valueOf(node.index() + 1));
            nodeAttributes = nodeAttributes.replace("%COMMUNITY%", communities.getCommunity(node).toString());
            out.append(node.index() + 1);
            out.append(nodeAttributes);
            out.append(";\n");
        }
    }

    private static Map<Community, String> generateCommunityColors(CommunityList communities){
        Map<Community, String> colors = new HashMap<Community, String>(communities.getSize(), 1);

        String[] fixedColors = Config.get("COMMUNITY_EXPORT_DOT_COMMUNITY_COLORS").split(",");
        int index = 0;
        for (Community community : communities.getCommunities()){
            if (index < fixedColors.length){
                colors.put(community, fixedColors[index++]);
            } else {
                Color c = new Color((int) (Math.random() * 256),
                                    (int) (Math.random() * 256),
                                    (int) (Math.random() * 256));
                String r = Integer.toHexString(c.getRed()).toUpperCase();
                String g = Integer.toHexString(c.getGreen()).toUpperCase();
                String b = Integer.toHexString(c.getBlue()).toUpperCase();
                if (r.length() < 2) r = "0" + r;
                if (g.length() < 2) g = "0" + g;
                if (b.length() < 2) g = "0" + b;
                colors.put(community,"\"#" + r + g + b + "\"");
            }
        }
        return colors;
    }

    private static Map<Role, String> readRoleColors(){
        Map<Role, String> colors = new HashMap<Role, String>(Role.values().length, 1);
        for (Role role : Role.values()){
            colors.put(role, Config.get("COMMUNITY_EXPORT_DOT_COLOR_" + role.toString()));
        }
        return colors;
    }

    private static Map<Role, String> readRoleShapes(){
        Map<Role, String> shapes = new HashMap<Role, String>(Role.values().length, 1);
        for (Role role : Role.values()){
            shapes.put(role, Config.get("COMMUNITY_EXPORT_DOT_SHAPE_" + role.toString()));
        }
        return shapes;
    }

    public static String toGML(Graph graph, CommunityList communities, Role[] roles){
        boolean useQuotes = Config.getBoolean("COMMUNITY_EXPORT_GML_QUOTES");
        StringBuilder out = new StringBuilder();
        out.append("graph [\n");
        for(Node node : graph.nodes){
            out.append("  node [\n");
            out.append("    id ");
            if(useQuotes) out.append("\"");                           
            out.append(node.index() + 1);
            if(useQuotes) out.append("\"");
            out.append("\n");
            if(communities != null){
                out.append("    community ");
                if(useQuotes) out.append("\"");                                                  
                out.append(communities.getCommunity(node).getId() + 1);
                if(useQuotes) out.append("\"");
                out.append("\n");
            }
            if(roles != null){
                out.append("    role ");
                if(useQuotes) out.append("\"");   
                out.append(roles[node.index()].toString());
                if(useQuotes) out.append("\"");
                out.append("\n");
            }
            out.append("  ]\n");
        }
        HashMap<String, Boolean> printed = new HashMap<String, Boolean>(graph.edges * 2);
        for(Edge edge : graph.edges()){
            if(!Boolean.TRUE.equals(printed.get(Edge.toString(edge.src, edge.dst)))){
                printed.put(Edge.toString(edge.src, edge.dst), Boolean.TRUE);
                printed.put(Edge.toString(edge.dst, edge.src), Boolean.TRUE);
                out.append("  edge [\n");
                out.append("    source ");
                if(useQuotes) out.append("\"");
                out.append(edge.src.index() + 1);
                if(useQuotes) out.append("\"");
                out.append("\n");
                out.append("    target ");
                if(useQuotes) out.append("\"");
                out.append(edge.dst.index() + 1);
                if(useQuotes) out.append("\"");
                out.append("\n");
                out.append("  ]\n");
            }
        }
        out.append("]");
        return out.toString();
    }
}
