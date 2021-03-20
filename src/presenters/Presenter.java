package presenters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * 
 */
public interface  Presenter extends PropertyChangeListener {
    
    public void setUpViewEvents();
    @Override
    public void propertyChange(PropertyChangeEvent evt);  
    
}
