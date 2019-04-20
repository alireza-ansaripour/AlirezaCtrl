/*
 * Copyright (c) 2010, Swedish Institute of Computer Science.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 *
 */

package org.contikios.cooja.sdnwise;
import com.github.sdnwiselab.sdnwise.mote.core.*;
import com.github.sdnwiselab.sdnwise.packet.NetworkPacket;
import network.Mote;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

import java.util.Iterator;

/**
 * Example SdnWise mote.
 *
 * This mote is simulated in COOJA via the Imported App Mote Type.
 *
 * @author Sebastiano Milardo
 */
public abstract class AbstractCoojaMote extends Mote {

    AbstractCore core;

    public AbstractCoojaMote(int id) {
        super(id);
    }

    public abstract void init();



    public void receivedPacket(NetworkPacket p) {
        int id = 15;
        if( getID() == id && p.getDst().intValue() == 0)
            System.out.println("herer");
        if(p.getDst().intValue() == getID() && getID() == id && p.getTyp() == NetworkPacket.RESPONSE)
            System.out.println("goozoo");
        core.rxRadioPacket(p);

    }




    public void radioTX(final NetworkPacket np) {
        //TODO: implement LOGGER
        if (np.getSrc().intValue() == 0)
            System.out.println("LOG: sending packet SRC: " + getID() + ":" +  np);
        if (np.getTyp() == NetworkPacket.REQUEST)
            System.out.println("LOG: sending request packet SRC: " + getID() + ":" +  np);
        for (Mote mote : getNeighbours()){
            AbstractCoojaMote m = (AbstractCoojaMote)mote;
            m.receivedPacket(np);
        }
    }

    protected class SenderRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    radioTX(core.getNetworkPacketToBeSend());
                }
            } catch (InterruptedException ex) {

            }
        }
    }

}
