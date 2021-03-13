public class Port {
    public  int number;
    public  String label;
    public Bridge connected_bridge;
    
    public Port() {
    }
    
    public Port(Bridge connected_bridge_ID) {
        this.connected_bridge = connected_bridge_ID;
        label = "0";
    }
    
    public int getNumber() {
        return number;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public Bridge getConnected_bridge() {
        return connected_bridge;
    }
    
    public void setConnected_bridge(Bridge connected_bridge) {
        this.connected_bridge = connected_bridge;
    }
    
    @Override
    public String toString() {
        return "{ con. to: "+connected_bridge.getID()+ " (" +label+ ")}";
    }
}
