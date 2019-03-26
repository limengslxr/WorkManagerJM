cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/cordova-plugin-device/www/device.js",
        "id": "cordova-plugin-device.device",
        "clobbers": [
            "device"
        ]
    },
     {
        "file": "plugins/cordova-plugin-userinfo/www/userInfo.js",
        "id": "cordova-plugin-userinfo.userInfo",
        "clobbers": [
            "cordova.plugins.userInfo"
        ]
     },
     {
        "file": "plugins/cordova-plugin-toolbar/www/toolbar.js",
        "id": "cordova-plugin-toolbar.toolbar",
        "clobbers": [
            "cordova.plugins.toolbar"
        ]
     },
    {
        "file": "plugins/cordova-plugin-whitelist/whitelist.js",
        "id": "cordova-plugin-whitelist.whitelist",
        "runs": true
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-console": "1.0.3",
    "cordova-plugin-device": "1.1.2",
    "cordova-plugin-whitelist": "1.2.2",
    "cordova-plugin-userInfo": "1.0.0",
    "cordova-plugin-toolbar": "1.0.0"
};
// BOTTOM OF METADATA
});