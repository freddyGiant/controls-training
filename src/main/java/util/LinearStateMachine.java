// package util;

// public abstract class LinearStateMachine<E extends Enum, LinearState>
// {
//     private final Class<E> enumType;
//     private E state;

//     public LinearStateMachine(Class<E> enumType, E initialState)
//     {
//         this.enumType = enumType;
//         state = initialState;
//     }
    
//     public E getNextState() 
//     { 
//       if(state.ordinal() == E[].class.cast(enumType.getMethod("values").invoke(null)).length - 1) return null; // if on last state
//       if(nextState == null) nextState = Robot22State.values()[this.ordinal() + 1]; // memoize next state
//       return nextState;
//     }

//     public Robot22State updateState() { return getNextState() != null && getNextState().transitionCond() ? getNextState() : this; }
// }