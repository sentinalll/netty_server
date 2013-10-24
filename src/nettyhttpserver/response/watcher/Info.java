/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nettyhttpserver.response.watcher;

import io.netty.channel.ChannelHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Roman
 */
@ChannelHandler.Sharable
public class Info {

    private static int requestCount = 0;
    private static int channelCount = 0;
    private static Map<Integer, String> IPbyID = new HashMap();
    private static Set<String> uniqueIp = new TreeSet();
    private static Map<String, Integer> requestCountForIp = new HashMap();
    private static Map<String, Long> requestLastTimestamp = new HashMap();
    private static Map<Integer, Long> timeStampToID = new HashMap();
    private static Map<String, Integer> redirect = new HashMap();
    private static Map<Integer, String> URLtoID = new HashMap();
    private static Map<Integer, Integer> recievedBytes = new HashMap();
    private static Map<Integer, Integer> sendBytes = new HashMap();

    public static Set<Integer> getAllID() {
        synchronized (IPbyID) {
            return IPbyID.keySet();
        }
    }

    public static void setRecievedBytes(int ID, int bytes) {
        synchronized (recievedBytes) {
            recievedBytes.put(ID, bytes);
        }
    }

    public static void setSendBytes(int ID, int bytes) {
        synchronized (sendBytes) {
            sendBytes.put(ID, bytes);
        }
    }

    public static int getRecievedBytes(int ID) {
        synchronized (recievedBytes) {
            if (recievedBytes.containsKey(ID)) {
                return recievedBytes.get(ID);
            }
        }
        return 0;
    }

    public static void setURLtoID(int ID, String url) {
        synchronized (URLtoID) {
            URLtoID.put(ID, url);
        }
    }

    public static String getURLbyID(int ID) {
        synchronized (URLtoID) {
            if (URLtoID.containsKey(ID)) {
                return URLtoID.get(ID);
            }
        }
        return "";
    }

    public static void setTimeStamptoID(int ID, long time) {
        synchronized (timeStampToID) {
            timeStampToID.put(ID, time);
        }
    }

    public static Long getTimeStampbyID(int ID) {
        synchronized (timeStampToID) {
            if (timeStampToID.containsKey(ID)) {
                return timeStampToID.get(ID);
            }
        }
        return 0L;
    }

    public static int getSendBytes(int ID) {
        synchronized (sendBytes) {
            if (sendBytes.containsKey(ID)) {
                return sendBytes.get(ID);
            }
        }
        return 0;
    }

    public static void incrementRequestCount(String Ip) {
        incrementRequestCountForIp(Ip);
        requestCount++;
    }

    public static void incrementChannelCount() {
        channelCount++;
    }

    public static void decrementChannelCount() {
        channelCount--;
    }

    public static int getRequestCount() {
        return requestCount;
    }

    public static int getChannelCount() {
        return channelCount;
    }

    public static void putIpAddress(int ID, String Ip) {
        synchronized (uniqueIp) {
            synchronized (IPbyID) {
                uniqueIp.add(Ip);
                IPbyID.put(ID, Ip);
            }
        }
    }

    public static String getIpAddressById(int ID) {
        synchronized (IPbyID) {
            if (IPbyID.containsKey(ID)) {
                return IPbyID.get(ID);
            }
        }
        return "";
    }

    public static int getCountUniqueRequest() {

        synchronized (uniqueIp) {
            return uniqueIp.size();
        }
    }

    public static void setRequestTimeStamp(String Ip, long timeStamp) {
        synchronized (requestLastTimestamp) {
            requestLastTimestamp.put(Ip, timeStamp);
        }
    }

    public static long getRequestTimeStamp(String Ip) {
        synchronized (requestLastTimestamp) {
            if (requestLastTimestamp.containsKey(Ip)) {
                return requestLastTimestamp.get(Ip);
            }
        }
        return 0L;
    }

    public static HashMap<String, Long> getRequestTimeStampForAllIp() {
        synchronized (requestLastTimestamp) {
            return (HashMap<String, Long>) requestLastTimestamp;
        }
    }

    public static void incrementRequestCountForIp(String Ip) {
        synchronized (requestCountForIp) {
            if (requestCountForIp.containsKey(Ip)) {
                int count = requestCountForIp.get(Ip);
                count++;
                requestCountForIp.put(Ip, count);
                return;
            }
            requestCountForIp.put(Ip, 1);
        }
    }

    public static int getRequestCountForIp(String Ip) {
        synchronized (requestCountForIp) {
            return requestCountForIp.get(Ip);
        }
    }

    public static void redirectCount(String url) {
        synchronized (redirect) {
            if (redirect.containsKey(url)) {
                int count = redirect.get(url);
                count++;
                redirect.put(url, count);
                return;
            }
            redirect.put(url, 1);
        }
    }

    public static int getRedirectCountForUrl(String url) {
        synchronized (redirect) {
            if (redirect.containsKey(url)) {
                return redirect.get(url);
            }
        }
        return 0;
    }

    public static HashMap<String, Integer> getRedirectCountForAllUrl() {
        synchronized (redirect) {
            return (HashMap<String, Integer>) redirect;
        }
    }

    public static HashMap<String, Integer> getRequestCountForAllIp() {
        return (HashMap<String, Integer>) requestCountForIp;
    }
}
