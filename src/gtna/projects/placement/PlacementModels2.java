/* ===========================================================
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
 * PlacementModels2.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.placement;

import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CirclePlacementModel;
import gtna.networks.model.placementmodels.models.CirclePlacementModel.DistributionType;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.models.GridPlacementModel;
import gtna.networks.model.placementmodels.models.RandomPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.util.Config;

import java.io.IOException;

/**
 * @author benni
 * 
 */
public class PlacementModels2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// singles();
		combinations();
	}

	private static void combinations() {
		// plot(circleCircle1(200), "./plots/_combinations/circle-circle1");
		// plot(circleCircle2(200), "./plots/_combinations/circle-circle2");
		// plot(circleGrid(200), "./plots/_combinations/circle-grid");
		plot(communityCommunity(600),
				"./plots/_combinations/community-community");
	}

	private static Network circleCircle1(int nodes) {
		PlacementModel p1 = new CirclePlacementModel(800,
				DistributionType.FIXED, DistributionType.FIXED, false);
		PlacementModel p2 = new CirclePlacementModel(200,
				DistributionType.FIXED, DistributionType.FIXED, false);
		return new PlacementModelContainer(nodes, 10, xy, xy, xy, xy, p1, p2,
				partitioner, connector, null);
	}

	private static Network circleCircle2(int nodes) {
		PlacementModel p1 = new CirclePlacementModel(700,
				DistributionType.FIXED, DistributionType.FIXED, false);
		PlacementModel p2 = new CirclePlacementModel(300,
				DistributionType.FIXED, DistributionType.FIXED, false);
		return new PlacementModelContainer(nodes, 10, xy, xy, xy, xy, p1, p2,
				partitioner, connector, null);
	}

	private static Network circleGrid(int nodes) {
		PlacementModel p1 = new CirclePlacementModel(800,
				DistributionType.FIXED, DistributionType.FIXED, false);
		PlacementModel p2 = new GridPlacementModel(400, 400, 5, 4, false);
		return new PlacementModelContainer(nodes, 10, xy, xy, xy, xy, p1, p2,
				partitioner, connector, null);
	}

	private static Network communityCommunity(int nodes) {
		PlacementModel p1 = new CommunityPlacementModel(0.4 * xy, 0.4 * xy, true);
		PlacementModel p2 = new CommunityPlacementModel(0.1 * xy, 0.1 * xy, true);
		return new PlacementModelContainer(nodes, 10, xy, xy, xy, xy, p1, p2,
				partitioner, connector, null);
	}

	private static void singles() {
		plot(singleRandom(500), "./plots/_singles/random");
		plot(singleCircle(500), "./plots/_singles/circle");
		plot(singleGrid1(500), "./plots/_singles/grid1");
		plot(singleGrid2(500), "./plots/_singles/grid2");
		plot(singleGrid3(500), "./plots/_singles/grid3");
		plot(singleCommunities(500), "./plots/_singles/communities");
	}

	private static void plot(Network nw, String name) {
		Graph g = nw.generate();
		Gephi ge = new Gephi();
		IdentifierSpace idSpace = (IdentifierSpace) g.getProperty("ID_SPACE_0");
		String filename1 = name + ".pdf";
		String filename2 = name + ".jpg";
		// if ((new File(filename2)).exists()) {
		// return;
		// }
		System.out.println(nw.getFolder());
		System.out.println("=> " + filename1);
		ge.plot(g, idSpace, filename1);
		System.out.println("=> " + filename2);
		PlacementModels2.exec("/usr/local/bin/convert " + filename1 + " "
				+ filename2);
		PlacementModels2.exec("rm " + filename1);
		System.out.println();
	}

	private static final Partitioner partitioner = new SimplePartitioner();

	private static final NodeConnector connector = new UDGConnector(120);

	private static final double xy = 2000;

	private static Network singleCommunities(int nodes) {
		PlacementModel p1 = new RandomPlacementModel(xy, xy, true);
		PlacementModel p2 = new CommunityPlacementModel(0.2 * xy, 0.2 * xy,  true);
		return new PlacementModelContainer(nodes, 1, xy, xy, xy, xy, p1, p2,
				partitioner, connector, null);
	}

	private static Network singleCircle(int nodes) {
		PlacementModel p1 = new RandomPlacementModel(xy, xy, true);
		PlacementModel p2 = new CirclePlacementModel(900,
				DistributionType.FIXED, DistributionType.FIXED, true);
		return new PlacementModelContainer(nodes, 1, xy, xy, xy, xy, p1, p2,
				partitioner, connector, null);
	}

	private static Network singleGrid1(int nodes) {
		PlacementModel p1 = new RandomPlacementModel(xy, xy, true);
		int x = (int) Math.ceil(Math.sqrt(nodes));
		int y = (int) Math.ceil(Math.sqrt(nodes));
		PlacementModel p2 = new GridPlacementModel(xy, xy, x, y, false);
		return new PlacementModelContainer(nodes, 1, xy, xy, xy, xy, p1, p2,
				partitioner, connector, null);
	}

	private static Network singleGrid2(int nodes) {
		PlacementModel p1 = new RandomPlacementModel(xy, xy, true);
		int x = (int) Math.ceil(Math.sqrt(nodes));
		int y = (int) Math.ceil(Math.sqrt(nodes));
		PlacementModel p2 = new GridPlacementModel(xy, xy, x, y, false);
		return new PlacementModelContainer(nodes, 1, xy, xy, xy, xy, p1, p2,
				partitioner, new UDGConnector(150), null);
	}

	private static Network singleGrid3(int nodes) {
		PlacementModel p1 = new RandomPlacementModel(xy, xy, true);
		int x = (int) Math.ceil(Math.sqrt(nodes));
		int y = (int) Math.ceil(Math.sqrt(nodes));
		PlacementModel p2 = new GridPlacementModel(xy, xy, x, y, false);
		return new PlacementModelContainer(nodes, 1, xy, xy, xy, xy, p1, p2,
				partitioner, new UDGConnector(200), null);
	}

	private static Network singleRandom(int nodes) {
		PlacementModel p1 = new RandomPlacementModel(xy, xy, true);
		PlacementModel p2 = new RandomPlacementModel(xy, xy, true);
		return new PlacementModelContainer(nodes, 1, xy, xy, xy, xy, p1, p2,
				partitioner, connector, null);
	}

	private static void exec(String cmd) {
		String[] envp = new String[] { Config.get("GNUPLOT_ENVP") };
		Process p_1;
		try {
			p_1 = Runtime.getRuntime().exec(cmd, envp);
			p_1.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
