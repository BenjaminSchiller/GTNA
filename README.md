~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
~~ GTNA - Graph-Theoretic Network Analyzer
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
and Contributors

Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/

GTNA is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

GTNA is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

~~~~~~~~~~~~~~~~~~~~~~~~~~~
~~ ChangeLog (28.3.11)
~~~~~~~~~~~~~~~~~~~~~~~~~~~

- Transformations
	- interface Transformation, partial implementation TransformationImpl
	- can be used to change a graph after generating it using a network generator
	- e.g. GiangConnectedComponent removes all nodes except for the largest cluster
	- e.g. Bidirectional makes all conenctions bidirectional by adding the reverse edges as well
	- e.g. RandomWithSameDD created a random network with the same degree distribution
		- basically the same as gtna.networks.transformation.SameDD
	- NetworkImpl constructor now includes an array of transformations
	- gtna.networks.transformation hence removed
	- all these transformation are automatically applied during Series.generate()
	- can use parameters similar to RoutingAlgorithm and Network
- Metric dependencies
	- metrics can now be configured to declare dependencies
	- KEY_DEPENDENCY = DD, CC requires DD and CC to be executed before
- computed metrics
	- computeData method now includes a third parameter: Hashtable<String, Metric> m
	- contains all metrics computed so far to gain access to the computed data
	- removed method readData and readValues from the Metric interface and MetricImpl
- Routing
	- added Route class to represent the results from each individual routing attempt
		- adapted the interfaces and implementations where neccessarey
	- reorganized
	- many new nodes / identifiers / algorithms added
- Plots
	- Gnuplot output can now be customized in the configuration file
	- plot via EPS feature was removed
- MAIN_DATA_FOLDER & MAIN_PLOT_FOLDER
	- all data is stored in the folder Config.get("MAIN_DATA_FOLDER")
		- default is "./data/"
		- can be changed, e.g., using Config.overwrite("MAIN_DATA_FOLDER", "./data/myData");
	- all plots are stored in the folder Config.get("MAIN_PLOT_FOLDER");
		- default is "./plots/"
		- can be changed, e.g., using Config.overwrite("MAIN_PLOT_FOLDER", "./plots/myPlots");
		- all plots like, e.g., Plot.plotAll(s, "destination/") are then stored in "MAIN_PLOT_FOLDER" + "destination/"
		- therefore no need to always plot to "./plots/myPlots/destination/"
- Config files
	- reorganized
	- added examples, explanations, and comments to many files
- Comments
	- comments to explain part of the classes and implementations were added
- Cleanup
	- some classes / functionalities were merged
	- unneccessary classes were removed
	- some classes were renamed and/or moved
	- some additional interfaces were slightly changed to reflect other changes mentioned above
