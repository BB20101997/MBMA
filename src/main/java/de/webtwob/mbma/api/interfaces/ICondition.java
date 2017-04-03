package de.webtwob.mbma.api.interfaces;

/**
 * Created by bennet on 27.03.17.
 */
public interface ICondition extends IObjectCondition {

    boolean checkCondition();
    
    @Override
    default boolean checkCondition(Object object){
        return checkCondition();
    }
}
