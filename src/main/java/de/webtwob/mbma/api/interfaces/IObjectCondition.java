package de.webtwob.mbma.api.interfaces;

/**
 * Created by bennet on 02.04.17.
 */
public interface IObjectCondition <T> {
    
    boolean checkCondition(T object);
}
