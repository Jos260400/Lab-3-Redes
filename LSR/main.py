"""
LINK STATE ROUTING
"""

import getpass
from networkx.algorithms.shortest_paths.generic import shortest_path
import yaml
from aioconsole import ainput
import networkx as nx
import asyncio
import logging
import slixmpp
import networkx as nx
import random
from getpass import getpass
import sys

#Error para windows
if sys.platform == 'win32' and sys.version_info >= (3, 8):
    asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())


#Iniciacion de cliente
class Client(slixmpp.ClientXMPP):
    def __init__(self, jid, password, nodo, nodes, names, graph):
        super().__init__(jid, password)
        self.received = set()
        self.initialize(jid, password, nodo, nodes, names, graph)
        self.schedule(name="update", callback=self.tree_update, seconds=20, repeat=True)
        self.connected_event = asyncio.Event()
        self.presences_received = asyncio.Event()

        
        self.add_event_handler('session_start', self.start)
        self.add_event_handler('message', self.message)
        self.register_plugin('xep_0030') # Service Discovery
        self.register_plugin('xep_0045') # Multi-User Chat
        self.register_plugin('xep_0199') # Ping


    #Se inicia el cliente
    async def start(self, event):
        self.send_presence() 
        await self.get_roster()
        self.connected_event.set()

    #Recepcion de mensajes
    async def message(self, msg):
        if msg['type'] in ('normal', 'chat'):
            await self.forward_msg(msg['body'])

    #Retransmicion de mensajes.
    async def forward_msg(self, msg):
        message = msg.jsplit('|')
        if message[0] == 'msg':
            print("Retransmitiendo el mensaje... ")
                
            if message[2] == self.jid:
                    print("Este mensaje es para mi mismo " +  message[6])
                    
                    
            else:
                if int(message[3]) > 0:
                    lista = message[4].split(",")
                    if self.nodo not in lista:
                        message[4] = message[4] + "," + str(self.nodo)
                        message[3] = str(int(message[3]) - 1)
                        StrMessage = "|".join(message)
                        target = []
                            
                            
                        for x in self.graph.nodes().data():
                            if x[1]["jid"] == message[2]:
                                target.append(x)
                            
                            
                            shortest = nx.shortest_path(self.graph, source=self.nodo, target=target[0][0])
                            print('Path: ', shortest)
                            if len(shortest) > 0:
                                self.send_message(
                                    mto=self.names[shortest[1]],
                                    mbody=StrMessage,
                                    mtype='chat' 
                                )  
                else:
                    pass
       

    #Grafo
    def tree_update(self):
            for x in self.graph.nodes().data():
                if x[0] in self.nodes:
                    dataneighbors= x
            for x in self.graph.edges.data('weight'):
                if x[1] in self.nodes and x[0]==self.nodo:
                    dataedges = x
            StrNodes = str(dataneighbors) + "-" + str(dataedges)
            for i in self.nodes:
                update_msg = "update|" + str(self.jid) + "|" + str(self.names[i]) + "|" + str(self.graph.number_of_nodes()) + "||" + str(self.nodo) + "|" + StrNodes
                self.send_message(
                        mto=self.names[i],
                        mbody=update_msg,
                        mtype='chat' 
                    )
    
    def initialize(self, jid, password, nodo, nodes, names, graph):
        self.names = names
        self.graph = graph
        self.nodo = nodo
        self.nodes = nodes

#Clase TOPO
class Tree():
    
    def newTree(self, topo, names):
        G = nx.Graph()
        
        for key, value in names["config"].items():
            G.add_node(key, jid=value)
            
       
        for key, value in topo["config"].items():
            for i in value:
                weightA = random.uniform(0, 1)
                G.add_edge(key, i, weight=weightA)
        
        return G
    
# Main
async def main(xmpp: Client):
    mainexecute = True
    origin = ""
    destiny = ""
    while mainexecute:
            to_user = await ainput("Correo a quien enviar?:   ")
            active = True
            while active:
                mensaje = await ainput("Message >>> ")
                if (len(mensaje) > 0):
                        target=[]
                        for x in xmpp.graph.nodes().data():
                            if x[1]["jid"] == to_user:
                                target.append(x)
                        mensaje = "msg|" + str(xmpp.jid) + "|" + str(to_user) + "|" + str(xmpp.graph.number_of_nodes()) + "||" + str(xmpp.nodo) + "|" + str(mensaje)
                        
                        
                        shortest = nx.shortest_path(xmpp.graph, source=xmpp.nodo, target=target[0][0])
                        if len(shortest) > 0:
                            xmpp.send_message(
                                mto=xmpp.names[shortest[1]],
                                mbody=mensaje,
                                mtype='chat' 
                            )
                    
        


if __name__ == "__main__":
  
  
    lector_topo = open("topo.txt", "r", encoding="utf8")
    lector_names = open("names.txt", "r", encoding="utf8")
    topo_string = lector_topo.read()
    names_string = lector_names.read()
    topo = yaml.load(topo_string, Loader=yaml.FullLoader)
    names = yaml.load(names_string, Loader=yaml.FullLoader)

    #info
    jid = input("Su usuario: ")
    pswd = getpass("Su contra: ")
    

    tree = Tree()

    for key, value in names["config"].items():
            if jid == value:
                nodo = key
                nodes = topo["config"][key]

    graph = tree.newTree(topo, names)
    xmpp = Client(jid, pswd, nodo, nodes, names["config"], graph)
    xmpp.connect() 
    xmpp.loop.run_until_complete(xmpp.connected_event.wait())
    xmpp.loop.create_task(main(xmpp))
    xmpp.process(forever=False)
    
