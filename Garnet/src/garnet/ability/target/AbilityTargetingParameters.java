package garnet.ability.target;

public class AbilityTargetingParameters extends TargetingParameters{
	private boolean targetless = false;
	private boolean axisLocked = false;
	private boolean movement = false;
	public boolean isTargetless() {
		return targetless;
	}

	public boolean isAxisLocked() {
		return axisLocked;
	}

	public void setAxisLocked(boolean axisLocked) {
		this.axisLocked = axisLocked;
	}

	public void setTargetless(boolean targetless) {
		this.targetless = targetless;
	}

	public boolean isMovement() {
		return movement;
	}

	public void setMovement(boolean movement) {
		this.movement = movement;
	}
	

}
