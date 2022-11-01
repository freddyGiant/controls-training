package util;

public interface LinearState {
    /**
     * Condition used by the state machine to determine whether to transition to this state
     * 
     * @returns whether to transition
     */
    public boolean transitionCond();
}
