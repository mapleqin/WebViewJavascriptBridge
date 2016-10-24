/*
 * Copyright 2015 Soulwolf Ching
 * Copyright 2015 The Android Open Source Project for WebViewJavascriptBridge
 * 
 * Licens ed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.soulwolf.wvjsbridge;

import android.util.Log;

/**
 * author: EwenQin
 * since : 2016/10/23 下午1:02.
 */
public class L {

    public static final String TAG = "WJBridge";
    public static boolean debug = true;

    public static void i(String msg,Object ... args){
        if(debug){
            Log.i(TAG,String.format(msg,args));
        }
    }

    public static void e(String msg,Object ... args){
        L.e(msg,args,null);
    }

    public static void e(String msg,Object [] args,Throwable error){
        L.e(String.format(msg,args),error);
    }

    public static void e(String msg,Throwable error){
        if(debug){
            Log.e(TAG,msg,error);
        }
    }
}
