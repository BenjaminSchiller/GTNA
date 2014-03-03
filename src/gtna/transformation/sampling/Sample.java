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
 * Sample.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.sampling;

import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.transformation.sampling.sample.INetworkSample;
import gtna.transformation.sampling.sample.NetworkSampleFull;
import gtna.util.DeterministicRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class Sample extends GraphProperty {
    private INetworkSample sample;
    private int[] startnodes;
    private Random rng;

    /**
	 * 
	 */
    public Sample() {
    	this.setSample(new NetworkSampleFull());
    }

    /**
     * @param s
     * @param startNodeIndices
     */
    public Sample(INetworkSample s, int[] startNodeIndices) {
	this.setSample(s);
	this.startnodes = startNodeIndices;
    }

    /**
     * @param s
     * @param startNodeIndices
     * @param rng
     */
    public Sample(INetworkSample s, int[] startNodeIndices, Random rng) {
	this.setSample(s);
	this.startnodes = startNodeIndices;
	this.rng = rng;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.graph.GraphProperty#write(java.lang.String, java.lang.String)
     */
    @Override
    public boolean write(String filename, String key) {
	Filewriter fw = new Filewriter(filename);

	this.writeHeader(fw, this.getClass(), key);

	this.writeParameter(fw, "Sample algorithm", getSample().getAlgorithm());
	this.writeParameter(fw, "Sample scaledown", getSample().getScaledown());
	this.writeParameter(fw, "Sample size", getSample().getSampleSize());
	this.writeParameter(fw, "Sample dimension", getSample().getDimension());
	this.writeParameter(fw, "Sample revisiting", getSample().isRevisiting());
	this.writeParameter(fw, "Sample startnodes", Arrays
		.toString(startnodes).replace("[", "").replace("]", ""));
	if (rng instanceof DeterministicRandom
		&& ((DeterministicRandom) rng).getConfiguredSeed() != null) {
	    this.writeParameter(fw, "Sample randomseed",
		    ((DeterministicRandom) rng).getConfiguredSeed().toString());
	} else {
	    this.writeParameter(fw, "Sample randomseed", "-");
	}

	int size = getSample().getSampleSize();

	this.writeParameter(fw, "Sample node mapping: oldId;newId;round-i,round-i+n, ...", getSample().toString());

	return fw.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see gtna.graph.GraphProperty#read(java.lang.String)
     */
    @Override
    public String read(String filename) {
	Filereader fr = new Filereader(filename);

	String key = this.readHeader(fr);

	String algorithm = this.readString(fr);
	double scaledown = this.readDouble(fr);
	int size = this.readInt(fr);
	int dimension = this.readInt(fr);
	boolean revisiting = this.readBoolean(fr);
	int[] startnodes = this.readIntArray(fr);
	rng = this.readRNG(fr);

	setSample(new NetworkSampleFull(algorithm, scaledown, dimension, revisiting));
	for (int i = 0; i < size; i++) {
	    String l = fr.readLine();

	    String[] sampledNode = l.split(";");
	    int oldId = Integer.parseInt(sampledNode[0].trim());
	    int newId = Integer.parseInt(sampledNode[1].trim());
	    List<Integer> rf = parseRevisitFrequencyString(sampledNode[2]
		    .trim());

	    getSample().addNodeEntry(oldId, newId, rf);
	}

	fr.close();

	return key;
    }

    /**
     * @param fr
     * @return
     */
    private Random readRNG(Filereader fr) {
	String r = this.readString(fr);
	if(r.equalsIgnoreCase("-")) {
	    return new Random();
	} else {
	    Long seed = Long.parseLong(r);
	    return new DeterministicRandom(seed);
	}
    }

    /**
     * @param fr
     * @return
     */
    private int[] readIntArray(Filereader fr) {
	String sn = this.readString(fr);
	String[] sna = sn.split(", ");
	int[] s = new int[sna.length];
	for (int i = 0; i < s.length; i++) {
	    s[i] = Integer.parseInt(sna[i]);
	}

	return s;
    }

    /**
     * @param trim
     * @return
     */
    private List<Integer> parseRevisitFrequencyString(String trim) {
	List<Integer> rf = new ArrayList<Integer>();

	String[] visits = trim.split(",");

	for (String v : visits) {
	    rf.add(Integer.parseInt(v.trim()));
	}

	return rf;
    }

    /**
     * Returns the old node id of a sampled node or a negative integer if the
     * node is not sampled
     * 
     * @param newId
     *            : new node id
     * @return old node id <br>
     *         integer < 0 if not sampled
     */
    public int getOldNodeId(int newId) {
	Map<Integer, Integer> nodeIdMapping = getSample().getSampleNodeMapping();

	if (nodeIdMapping.containsValue(newId)) {
	    for (Map.Entry<Integer, Integer> e : nodeIdMapping.entrySet()) {
		if (e.getValue() == newId) {
		    return e.getKey();
		}
	    }
	}

	return -1;
    }

    /**
     * Returns the new node id of a sampled node or a negative integer if the
     * node is not sampled
     * 
     * @param oldId
     *            : node id in the original graph
     * @return new node id <br>
     *         integer < 0 if not sampled
     */
    public int getNewNodeId(int oldId) {
//    	return getSample().getNewIndexOfSampledNode(oldId);
    	
    	Map<Integer, Integer> nodeIdMapping = getSample().getSampleNodeMapping();

    	if (nodeIdMapping.containsKey(oldId)) {
    	    for (Map.Entry<Integer, Integer> e : nodeIdMapping.entrySet()) {
    	    	if (e.getKey() == oldId) {
    		    return e.getValue();
    	    	}
    	    }
    	}

    	return -1;

    }

    /**
     * Returns the Set of <b>old<b> node indices
     * 
     * @return Set of Integers
     */
    public Set<Integer> getSampledIds() {
	return getSample().getSampleNodeMapping().keySet();
    }

	/**
	 * @return the sample
	 */
	public INetworkSample getSample() {
		return sample;
	}

	/**
	 * @param sample the sample to set
	 */
	public void setSample(INetworkSample sample) {
		this.sample = sample;
	}

}
