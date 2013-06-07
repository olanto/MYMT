/**********
    Copyright © 2010-2013 Olanto Foundation Geneva

   This file is part of myMT.

   myMT is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    myMT is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with myMT.  If not, see <http://www.gnu.org/licenses/>.

**********/

package localMaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.smt.configStateCommons.exceptions.ServiceOperationException;

/**
 * WORKS ONLY ON AN UNIX OS
 * @author Lomig Mégard
 */
public class LocalMaster {

    /**
     * All services registered by this local master. The map key
     * is the port number of each service.
     */
    private Map<Integer, ServiceProcess> services;


    public LocalMaster() {
        services = new HashMap<Integer, ServiceProcess>();
    }

    /**
     * Run the perl deamon with the specified ip and port.
     * @param deamon the path and the name of the deamon
     * @param ip the ip used by the deamon
     * @param port the port used by the deamon
     * @throws IOException if an exception occurs during the execution.
     */
    public void startRestart(String daemon, String ip, int port) throws ServiceOperationException {

        //TODO bien comme ça ? 
        // stop a possible running process
        ServiceProcess serviceProcess = services.get(port);
        if (serviceProcess != null) {
            stop(port);
        }

        try {
            ProcessBuilder builder = new ProcessBuilder(Config.PERL_PATH, Config.DAEMON_DIR +
                     daemon, ip, Integer.toString(port));

            //builder = builder.directory(new File("/home/simple/moses-web/bin"));
            builder = builder.redirectErrorStream(true);

            System.out.println(builder.command());

            final Process proc = builder.start();


            //TODO pour récupérer la sortie standard, à enlever pour la version finale ?
            new Thread(new Runnable(){
                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        String line;
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException ex) {
                        System.out.println("Lost connection.");
                    }
                }
            }).start();
            

            serviceProcess = new ServiceProcess(daemon, ip, port, proc);
            services.put(port, serviceProcess);

            System.out.println("New process created : " + serviceProcess);
        } catch (IOException ex) {
            throw new ServiceOperationException("Error during the command execution. "
                    + ex.getMessage());
        }
    }

    public void stop(int port) throws ServiceOperationException {
        ServiceProcess service = services.get(port);
        if (service != null) {
            // stop the process
            service.process.destroy();

            //TODO test si le process est bien terminé ?

            services.remove(port);

            // wait 1s to give time to the process of releasing the port
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(LocalMaster.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            throw new ServiceOperationException("Any service is linked with the " +
                    "port " + port + ".");
        }
    }

    private class ServiceProcess {

        protected final String deamon;
        protected final String ip;
        protected final int port;
        protected final Process process;

        public ServiceProcess(String deamon, String ip, int port, Process process) {
            this.deamon = deamon;
            this.ip = ip;
            this.port = port;
            this.process = process;
        }

    }
}
