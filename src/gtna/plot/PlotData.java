package gtna.plot;

/**
 * 
 * @author benni
 *
 */
public class PlotData {
	public static final int POINTS = 1;

	public static final int DOTS = 2;

	public static final int LINE = 3;

	public static final int WHISKER = 4;

	public static final int FUNCTION = 5;

	public String file;

	public String title;

	public int type;

	public int lineType;

	public int lineWidth;

	public PlotData(String file, String title, int type, int lineType,
			int lineWidth) {
		this.file = file;
		this.title = title;
		this.type = type;
		this.lineType = lineType;
		this.lineWidth = lineWidth;
	}
	
	public String toString(){
		return "(" + this.type + ") " + this.title + " in " + this.file;
	}
}
