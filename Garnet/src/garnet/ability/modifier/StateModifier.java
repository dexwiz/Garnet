package garnet.ability.modifier;

public class StateModifier {
	private int value = 0;
	private StateModifierType type = StateModifierType.NONE;
	
	public StateModifier(StateModifierType type, int value)
	{
		this.type = type;
		this.value = value;
	}
	
	public StateModifierType getType() {
		return type;
	}

	public void setType(StateModifierType type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public enum StateModifierType
	{
		NONE,
		MOVEMENT,
		HEALTH,
		MANA
	}
}
