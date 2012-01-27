package gtna.transformation.communities.matrices;

public class MergeValueInt {
	
	public int i;
	public int j;
	public int value;
	
	public MergeValueInt(int i, int j, int value){
		this.i = i;
		this.j = j;
		this.value = value;
	}

	public MergeValueInt(int i2, int j2) {
		this.i = i2;
		this.j = j2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if(i > j){
			result = prime * result + j;
			result = prime * result + i;			
		}
		else {
			result = prime * result + i;
			result = prime * result + j;
		}

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MergeValueInt other = (MergeValueInt) obj;
		if ((i == other.i && j == other.j) || (i == other.j && j == other.i))
			return true;
	
		return false;
	}
}
