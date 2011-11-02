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
 * Node.java
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
package gtna.io.networks.googlePlus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author benni
 *
 */
public class Node {
	private Task task;

	private ArrayList<User> in;

	private ArrayList<User> out;

	public Node(Task task, ArrayList<User> in, ArrayList<User> out) {
		this.task = task;
		this.in = in;
		this.out = out;
	}

	public Task getTask() {
		return this.task;
	}

	public ArrayList<User> getIn() {
		return this.in;
	}

	public ArrayList<User> getOut() {
		return this.out;
	}

	public String toString() {
		return "N/" + this.task.getU_id() + "/" + this.task.getTid() + "/"
				+ this.in.size() + "/" + this.out.size();
	}
	
	public static Node read(String filename, Task task) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String[] temp = null;

			// # User:
			reader.readLine();

			// 115442395727432137701;;;""
			reader.readLine();

			// # Timestamp:
			reader.readLine();

			// 1309774065
			reader.readLine();

			// # Out:
			reader.readLine();

			// 0
			int out = Integer.parseInt(reader.readLine());
			ArrayList<User> OUT = new ArrayList<User>(out);

			// # In:
			reader.readLine();

			// 0
			int in = Integer.parseInt(reader.readLine());
			ArrayList<User> IN = new ArrayList<User>(in);

			// # Out list:
			reader.readLine();
			for (int i = 0; i < out; i++) {
				temp = reader.readLine().split(";;;");
				User u = new User(temp[0], temp[1].substring(1,
						temp[1].length() - 1));
				OUT.add(u);
			}

			// # In list:
			reader.readLine();
			for (int i = 0; i < in; i++) {
				temp = reader.readLine().split(";;;");
				User u = new User(temp[0], temp[1].substring(1,
						temp[1].length() - 1));
				IN.add(u);
			}

			reader.close();
			Node node = new Node(task, IN, OUT);
			return node;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
