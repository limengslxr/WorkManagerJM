cordova.define("cordova-plugin-userinfo.userInfo", function(require, exports, module) {
var exec = require('cordova/exec');

var userInfo = {
  getAccount : function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "UserInfo", "getAccount", []);
  }
};

module.exports = userInfo;

});