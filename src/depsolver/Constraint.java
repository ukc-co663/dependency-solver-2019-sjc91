/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver;

enum Action{
    install,
    uninstall
}
/**
 *
 * @author Sam
 */
public class Constraint {
    public String name;
    public String version;
    public Action action;
    
    public Constraint(String input){
        String[] temp = input.split("=");
        this.name = temp[0].substring(1);
        this.action = (temp[0].charAt(0)=='+' ? Action.install : Action.uninstall);
        if(temp.length==2){
            version = temp[1];
        }
    }
  
}
