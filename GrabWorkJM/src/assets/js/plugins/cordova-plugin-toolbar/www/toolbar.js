cordova.define("cordova-plugin-toolbar.toolbar", function(require, exports, module) {
var exec = require('cordova/exec');

var toolbar = {
  updateToolbar : function(args, successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Toolbar", "updateToolbar", args);
  }
};

module.exports = toolbar;

});