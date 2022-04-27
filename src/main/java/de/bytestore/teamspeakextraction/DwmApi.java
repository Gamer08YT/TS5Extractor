package de.bytestore.teamspeakextraction;

import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Native;

public interface DwmApi extends StdCallLibrary {

    public final static DwmApi INSTANCE = Native.loadLibrary("dwmapi", DwmApi.class);

    //Native DwmApi function to enable dark mode title bar.
    int DwmSetWindowAttribute(HWND hwnd, int dwAttribute, PointerType pvAttribute, int cbAttribute);

    int DWMWA_USE_IMMERSIVE_DARK_MODE = 20;

    public static void setDarkModeTitleBar(HWND hwnd, final boolean enable){
        INSTANCE.DwmSetWindowAttribute(hwnd, DWMWA_USE_IMMERSIVE_DARK_MODE, new IntByReference(enable ? 1 : 0), 4);
    }
}
