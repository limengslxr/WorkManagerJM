// IMapAidlInterface.aidl
package com.sh3h.ipc;

import com.sh3h.ipc.location.MyLocation;

// Declare any non-default types here with import statements

interface IMapAidlInterface {
    void setLocation(in MyLocation myLocation);
    MyLocation getLocation();
}
