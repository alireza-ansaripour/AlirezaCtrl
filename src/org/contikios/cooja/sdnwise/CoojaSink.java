/*
 * Copyright (C) 2015 SDN-WISE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.contikios.cooja.sdnwise;
import Ctrl.Controller;
import com.github.sdnwiselab.sdnwise.mote.core.*;
import com.github.sdnwiselab.sdnwise.packet.NetworkPacket;
import com.github.sdnwiselab.sdnwise.util.NodeAddress;

import java.io.*;
import java.net.*;
import java.util.logging.*;

/**
 * @author Sebastiano Milardo
 */
public class CoojaSink extends AbstractCoojaMote {

    private Socket tcpSocket;
    private DataInputStream riceviOBJ;
    private DataOutputStream inviaOBJ;
    private InetAddress addrController;
    private int portController;

    public CoojaSink(int id) {
        super(id);
    }


    public void logI(String msg){
        core.log(Level.INFO, msg);
    }

    @Override
    public final void init() {
        Controller.getInstance(this);

        core = new SinkCore((byte) 1,
                new NodeAddress(this.getID()),
                null,
                "00000001",
                "00:01:02:03:04:05",
                1,
                null,
                this);

        new Thread(new TcpSender()).start();
        new Thread(new SenderRunnable()).start();
        core.start();
    }


    public void radioTX(final NetworkPacket np, boolean flowMatch) {
        if (flowMatch){
            core.send(np);
        }else
            super.radioTX(np);
    }



    private class TcpSender implements Runnable {

        @Override
        public void run() {
            Controller controller = Controller.getInstance();
            try {
                while (true) {
                    NetworkPacket np = ((SinkCore) core).getControllerPacketTobeSend();
                    controller.txControllerQueue.put(np);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(CoojaSink.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}