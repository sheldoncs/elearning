package blackboard.checkbox.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;

public class CheckBoxListener implements ActionListener{
  
	public String val;
	public JButton b;
	
	public void actionPerformed(ActionEvent ev){  
		
		if (((JCheckBox)ev.getSource()).isSelected()){	
		  b.setEnabled(true);
		  val = ev.getActionCommand();  
		} else {
			val="";
			b.setEnabled(false);
		}
    }
	public void setComponent(JButton b){
		this.b=b;
	}
    public String getBoxAction(){
    	return val;
    }
}
