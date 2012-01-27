package gtna.transformation.communities.matrices;

import java.util.Comparator;

public class MergeValueLongComparator implements Comparator<MergeValueLong> {

	@Override
	public int compare(MergeValueLong arg0, MergeValueLong arg1) {
		if(arg0.value < arg1.value || (arg0.value == arg1.value && arg0.i > arg1.i)  || (arg0.value == arg1.value && arg0.i == arg1.i && arg0.j > arg1.j)){
			return -1;
		}
		if(arg0.value == arg1.value && arg0.i == arg1.i && arg0.j == arg1.j)
			return 0;
		return 1;
	}

}
