package com.connection.bean;

/**
 * Created by taoy3 on 16/9/4.
 */
public class Device {
//    IP address       HW type     Flags       HW address            Mask     Device
//    192.168.43.73    0x1         0x2         64:a6:51:74:23:f7     *        wlan0
    private String ip;
    private int type;
    private int flags;
    private String address;
    private String mask;
    private String name;

    public Device(String ip, int type, int flags, String address, String mask, String name) {
        this.ip = ip;
        this.type = type;
        this.flags = flags;
        this.address = address;
        this.mask = mask;
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
