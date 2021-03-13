

import java.util.*;


public class main {
    
    public static void main(String[] args) {
        
        String designated_port = "DES_PORT";
        String root_port = "ROOT_PORT";
        String blocking_port = "BLOCK_PORT";
        
        /*  Hardcoding the topology graph as a Hashmap data
            structure. Was not able to figure out how to design
            a menu driven program to add new graphs to the topology,
            especially in hashmaps.
            
            For further clarification of what the topology looks
            like, please refer to TOPOLOGY.pdf file attached with
            this program.
            
            The code however successfully determines the root bridge,
            and assigns all the ports their labels as either
            designated_port, root_port, or blocked_port, thereby
            fulfilling the STP criteria. 
        */
        
        Bridge a = new Bridge(1,123);
        Bridge b = new Bridge(2,143);
        Bridge c = new Bridge(3,113);
        Bridge d = new Bridge(4,111);
        Bridge e = new Bridge(5,312);
        Bridge f = new Bridge(6,313);
        Bridge g = new Bridge(7,412);
        
        HashMap<Bridge, ArrayList<Bridge>> topology = new HashMap<>();
        topology.put(a, new ArrayList<Bridge>(Arrays.asList(b,c)));
        topology.put(b, new ArrayList<Bridge>(Arrays.asList(a,c,d,g)));
        topology.put(c, new ArrayList<Bridge>(Arrays.asList(a,b,e,f)));
        topology.put(d, new ArrayList<Bridge>(Arrays.asList(b,e,g)));
        topology.put(e, new ArrayList<Bridge>(Arrays.asList(c,d,f)));
        topology.put(f, new ArrayList<Bridge>(Arrays.asList(e,c)));
        topology.put(g, new ArrayList<Bridge>(Arrays.asList(b,d)));
        
        //adds the corresponding bridges in the hashmaps as the connected ports
        topology.forEach((key, val) -> {
            for (Bridge bridge:val) {
                key.ports_list.add(new Port(bridge));
            }
        });
        
        var ref = new Object() {
            int min = 65536;
        };
        
        //elects the root bridge by comparing IDs
        topology.forEach((key, val) -> {
            if (key.getID()< ref.min)
                ref.min = key.getID();
        });
        topology.forEach((key, val) -> {
            if (key.getID()== ref.min)
                key.root = true;
        });
        
        //Sets all the ports on root bridge as forwarding ports and sets root object in line 40
        topology.forEach((key, val) -> {
            if (key.root){
                for (Port port: key.ports_list) {
                    port.setLabel(root_port);
                }
            }
        });
        
        
        
        //Bridge root_bridge = root_atom.get();
        
        Object[] all_bridges = topology.keySet().toArray();
        Bridge root_bridge = null;
        for(int i = 0; i<all_bridges.length; i++){
            if(((Bridge)all_bridges[i]).root){
                root_bridge = (Bridge)all_bridges[i];
            }
        }
        
        //performs dfs on the topology and sets root switches
        System.out.println();
        for(int i = 0; i<all_bridges.length; i++){
            Bridge nearest_bridge_to_root = bfs((Bridge)all_bridges[i], root_bridge, topology);
            //System.out.println((Bridge)all_bridges[i] + " --> " + nearest_bridge_to_root);
    
            ArrayList<Port> ports_list = ((Bridge)all_bridges[i]).getPorts_list();
    
            for (Port port_i: ports_list) {
                if(port_i.getConnected_bridge() == nearest_bridge_to_root || port_i.getConnected_bridge() == root_bridge){
                    port_i.setLabel(root_port);
                }
            }
        }
    
        //This for-loop finds the adjacent ports of the root ports and labels them as DES_PORT
        for(int i = 0; i<all_bridges.length; i++) {
            ArrayList<Port> ports = ((Bridge)all_bridges[i]).ports_list;
            for (Port port_i: ports) {
                
                if(port_i.getLabel().equals(root_port)){
                    //System.out.println("from "+all_bridges[i]+" connected_bridge at " + port_i + ": " + port_i.getConnected_bridge());
                    
                    Bridge bridge_whose_ports_to_map = port_i.getConnected_bridge();
                    ArrayList<Port> ports_to_map = bridge_whose_ports_to_map.ports_list;
    
                    for (Port port_j : ports_to_map) {
                        if(port_j.label.equals("0") && port_j.getConnected_bridge()==(Bridge)all_bridges[i]){
                            port_j.setLabel(designated_port);
                        }
                    }
                }
            }
        }
    
        //This for-loop maps all the unlabled ports (with label "0") as blocked ports
        for(int i = 0; i<all_bridges.length; i++) {
            for (Port port:((Bridge)all_bridges[i]).getPorts_list()) {
                if(port.getLabel().equals("0")){
                    port.setLabel(blocking_port);
                }
            }
        }
        
        //Prints the Topology
        topology.forEach((key, val) -> {
            System.out.print(key + " --> " + key.getPorts_list());
            System.out.println();
        });
        
    }
    
    static Bridge bfs(Bridge start, Bridge root, HashMap<Bridge, ArrayList<Bridge>> topology) {
        Set<Bridge> visited = new HashSet<Bridge>();
        Queue<Bridge> queue = new LinkedList<>();
    
        queue.add(start);
        visited.add(start);
    
        out:
        while (!queue.isEmpty()) {
    
            //+"\t\t\t\t\t\t , visited --> " + visited);
            Bridge start_bridge = queue.remove();
            //System.out.println("from " + start_bridge + ": visited ->" + visited);
            
            if (start_bridge == root) {
                //System.out.println("FOUND!! BREAKING!! \n\n");
                break;
            }
    
            for (Bridge con_bridge : topology.get(start_bridge)) {
                if (!visited.contains(con_bridge)) {
                    if (con_bridge == root) {
                        visited.add(con_bridge);
                        //System.out.println("from " + start_bridge + ": visited ->" + visited);
                        //System.out.println("BREAKING!\n");
                        return start_bridge;
                    }
                    queue.add(con_bridge); // 'stack' it up to be searched!
                    visited.add(con_bridge); // remember to say you visited it now.
                }
            }
        }
        return start;
    }
}
