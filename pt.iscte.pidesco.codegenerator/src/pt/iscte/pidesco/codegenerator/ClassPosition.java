package pt.iscte.pidesco.codegenerator;

public class ClassPosition {
	int startClassPosition = 0;
	int endClassPosition = 0;
	int totalCount = 0;

	/**
	 * @param startClassPosition
	 * @param endClassPosition
	 * @param totalCount
	 */
	public ClassPosition(int startClassPosition, int endClassPosition, int totalCount) {
		super();
		this.startClassPosition = startClassPosition;
		this.endClassPosition = endClassPosition;
		this.totalCount = totalCount;
	}

	public int getStartClassPosition() {
		return startClassPosition;
	}

	public void setStartClassPosition(int startClassPosition) {
		this.startClassPosition = startClassPosition;
	}

	public int getEndClassPosition() {
		return endClassPosition;
	}

	public void setEndClassPosition(int endClassPosition) {
		this.endClassPosition = endClassPosition;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}
