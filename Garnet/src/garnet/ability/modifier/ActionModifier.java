package garnet.ability.modifier;

public class ActionModifier {
	private int value = 0;
	private ActionModifierType type;
	
	public ActionModifier(ActionModifierType type, int value)
	{
		this.type = type;
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public ActionModifierType getType() {
		return type;
	}

	public void setType(ActionModifierType type) {
		this.type = type;
	}

	public enum ActionModifierType
	{
		COST_INCREASE,
		COST_DECREASE,
		DAMAGE_IN_INCREASE,
		DAMAGE_IN_DECREASE,
		HEAL_IN_INCREASE,
		HEAL_IN_DECREASE,
		DAMAGE_OUT_INCREASE,
		DAMAGE_OUT_DECREASE,
		HEAL_OUT_INCREASE,
		HEAL_OUT_DECREASE
	}
}
