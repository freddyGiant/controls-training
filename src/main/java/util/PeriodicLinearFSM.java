package util;

public class PeriodicLinearFSM<E extends Enum<E> & LinearState>
{
    private E state;
    private final E[] states;

    public PeriodicLinearFSM(E initialState, E[] states)
    {
        state = initialState;
        this.states = states;
    }

    public E getState()
    {
      return state;
    }

    public E getNextState()
    { 
      if(state.ordinal() == states.length - 1) return null; // if on last state
      return states[state.ordinal() + 1];
    }

    public void updateState() 
    {
      if(getNextState() != null && getNextState().transitionCond()) 
        state = getNextState();
    }
}