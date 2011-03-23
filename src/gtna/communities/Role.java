package gtna.communities;

public enum Role {
    R1 ("Ultra-Peripheral"),
    R2 ("Peripheral Nodes"),
    R3 ("Satellite Connector"),
    R4 ("Kinless Node"),
    R5 ("Provincial Hub"),
    R6 ("Connector Hub"),
    R7 ("Global Hub");

    private String name;

    private Role(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }        
}
