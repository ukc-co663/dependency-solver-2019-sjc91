/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver;

/**
 *
 * @author Sam
 */
public class BuildVersion {
    public int major = 0;
    public int minor = 0;
    public int revision = 0;
    public String text_version = "";
    
    public BuildVersion(){
        
    }

    public BuildVersion(String input){
        this.text_version = input;
        String[] temp = input.split("\\.");
        if(temp.length==1){
            this.major = Integer.parseInt(temp[0]);
        }else if(temp.length==2){
            this.major = Integer.parseInt(temp[0]);
            this.minor = Integer.parseInt(temp[1]);
        }else if(temp.length==3){
            this.major = Integer.parseInt(temp[0]);
            this.minor = Integer.parseInt(temp[1]);
            this.revision = Integer.parseInt(temp[2]);
        }else{
            System.out.println("Unknown version syntax");
        }
    }
    
    public String pretty(){
        return text_version;
    }
    
    public boolean isEqual(BuildVersion version){
        if( this.major == version.major &&
            this.minor == version.minor &&
            this.revision == version.revision) return true;
        return false;
    }
    
    public boolean isLessThan(BuildVersion version){
        if(this.major < version.major) return true;
        if(this.minor < version.minor) return true;
        if(this.revision < version.revision) return true;
        return false;
    }
    
    public boolean isLessEqualThan(BuildVersion version){
        if(isEqual(version)) return true;
        return this.isLessThan(version);
    }
    
    public boolean isGreaterThan(BuildVersion version){
        if(this.major > version.major) return true;
        if(this.minor > version.minor) return true;
        if(this.revision > version.revision) return true;
        return false;
    }
    
    public boolean isGreaterEqualThan(BuildVersion version){
        if(isEqual(version)) return true;
        return this.isGreaterThan(version);
    }
}
