
package com.plugin

class CusProperties {
    def pluginVersion
    def pluginName
    def option

    @Override
    String toString() {
        return "pluginVersion is  " + pluginVersion +"     pluginName  is   " +pluginName
    }

}