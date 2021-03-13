import java.util.ArrayList;

public class Bridge {
    public int ID;
    public int mac_add;
    public ArrayList<Port> ports_list = new ArrayList();
    boolean root;
    
    public Bridge() {
    }
    
    public boolean isRoot() {
        return root;
    }
    
    public void setRoot(boolean root) {
        this.root = root;
    }
    
    public Bridge(int ID, int mac_add) {
        this.ID = ID;
        this.mac_add = mac_add;
    }
    
    public int getID() {
        return ID;
    }
    
    public void setID(int ID) {
        this.ID = ID;
    }
    
    public int getMac_add() {
        return mac_add;
    }
    
    public void setMac_add(int mac_add) {
        this.mac_add = mac_add;
    }
    
    public ArrayList<Port> getPorts_list() {
        return ports_list;
    }
    
    public void setPorts_list(ArrayList<Port> ports_list) {
        this.ports_list = ports_list;
    }
    
    @Override
    public String toString() {
        if(root)
            return "Bridge:^root^ "+'(' +ID+')';
        else
            return "Bridge:" + ID;
    }
}
